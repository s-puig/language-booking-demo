package com.demo.language_booking.auth.authentication;

import com.demo.language_booking.auth.AuthService;
import com.demo.language_booking.common.exceptions.InvalidAuthException;
import com.demo.language_booking.users.dto.UserPublicResponse;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
@ConditionalOnProperty(
        name = "auth.filter.enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class AuthenticationValidationFilter extends OncePerRequestFilter {
    /**
     * Immutable set of endpoint representations that require authentication validation.
     * This collection is built at application startup by scanning all controller classes and methods
     * annotated with @Authenticated.
     * Thread-safe
     */
    Set<String> securedEndpoints;
    ApplicationContext applicationContext;
    RequestMappingHandlerMapping handlerMapping;
    AuthService authService;

    /// Build a set of all endpoints that required authentification validation.
    /// Runs immediately after constructor injection.
    @PostConstruct
    private void init() {
        Set<String> securedEndpoints = new HashSet<>();
        Stream<Method> methodsAnnotated = Arrays.stream(applicationContext.getBeanNamesForAnnotation(Controller.class))
                .map(beanName -> applicationContext.getType(beanName, false))
                .filter(Objects::nonNull)
                .flatMap(beanClass -> MethodIntrospector.selectMethods(beanClass, new AuthenticatedMethodFilter())
                        .stream());
        Stream<Method> methodsAnnotatedFromClass = applicationContext.getBeansWithAnnotation(Authenticated.class).keySet()
                .stream()
                .map(beanName -> applicationContext.getType(beanName, false))
                .filter(Objects::nonNull)
                .flatMap(beanClass -> MethodIntrospector.selectMethods(beanClass, new RequestMappingMethodFilter())
                        .stream());
        Stream.concat(methodsAnnotatedFromClass, methodsAnnotated)
                .forEach(securedMethod -> securedEndpoints.add(securedMethod.toGenericString()));

        this.securedEndpoints = Collections.unmodifiableSet(securedEndpoints);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!isEndpointSecured(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = request.getHeader("Authorization");
        if (!authService.isTokenValid(jwt)) throw new InvalidAuthException("Session token expired");

        UserPublicResponse userSession = authService.parseToken(jwt);
        request.setAttribute("session", userSession);

        filterChain.doFilter(request, response);
    }

    /**
     * Determines whether the requested endpoint requires authentication validation.
     *
     * @param request the HTTP servlet request
     * @return true if the endpoint is annotated with {@link Authenticated} and requires authentication, false otherwise
     * @throws RuntimeException if an internal error occurs fetching the handle of the request
     */
    private boolean isEndpointSecured(HttpServletRequest request) throws RuntimeException {
        HandlerExecutionChain handle;
        try {
          handle = handlerMapping.getHandler(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(handle == null) throw new NullPointerException();
        Method endpointMethod = ((HandlerMethod) handle.getHandler()).getMethod();
        return securedEndpoints.contains(endpointMethod.toGenericString());
    }
}

class AuthenticatedMethodFilter implements ReflectionUtils.MethodFilter {

    @Override
    public boolean matches(Method method) {
        return method.isAnnotationPresent(Authenticated.class);
    }
}

class RequestMappingMethodFilter implements ReflectionUtils.MethodFilter {

    @Override
    public boolean matches(Method method) {
        try {
            Annotation mapping = method.getAnnotation(Mapping.class);
            return true;
        } catch (NullPointerException _) {}
        return false;
    }
}