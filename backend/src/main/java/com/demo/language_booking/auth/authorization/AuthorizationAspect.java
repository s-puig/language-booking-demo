package com.demo.language_booking.auth.authorization;

import com.demo.language_booking.common.exceptions.InvalidPolicyHandlerException;
import com.demo.language_booking.common.exceptions.MalformedAuthorizeAnnotationException;
import com.demo.language_booking.common.exceptions.PermissionDeniedException;
import com.demo.language_booking.users.dto.UserPublicResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

@Component
@Aspect
public class AuthorizationAspect {
    @Autowired
    private ApplicationContext applicationContext;

    @Before("@annotation(Authorize) || @annotation(AuthorizeCompilation)")
    public void authorizeEndpoint(JoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        final Class<? extends IAuthPolicyHandler> classSecurityPolicy = method.getDeclaringClass().isAnnotationPresent(AuthPolicy.class) ? method.getDeclaringClass().getAnnotation(AuthPolicy.class).value() : NoPolicy.class;
        final Authorize[] authorizes = method.getAnnotationsByType(Authorize.class);
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final UserPublicResponse currentSession = (UserPublicResponse) request.getAttribute("session");

        boolean allowed = Arrays.stream(authorizes).anyMatch(condition -> {
            if (currentSession.getRole() != condition.value()) return false;
            if (condition.requireOwnership()) {
                final long resourceId;
                try {
                    resourceId = extractResourceId(request, condition.resourceKey());
                } catch (Exception e) {
                    throw new MalformedAuthorizeAnnotationException("The authorization annotation resource ownership path is empty, malformed or ambiguous");
                }
                Class<? extends IAuthPolicyHandler> policy = Stream.of(condition.policy(), classSecurityPolicy)
                        .filter(candidatePolicy -> candidatePolicy != NoPolicy.class)
                        .findFirst()
                        .orElseThrow(() -> new InvalidPolicyHandlerException("Endpoint is missing a policy handler"));
                return applicationContext.getBean(policy).getResourceOwner(resourceId) == currentSession.getId();
            }

            return true;
        });

        if (!allowed) throw new PermissionDeniedException("Access denied. Authenticated user lacks permission.");
    }

    private Long extractResourceId(HttpServletRequest request, String key) throws Exception {
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (key != null && !key.isBlank()) {
            String resourceId = pathVariables.get(key);
            if (resourceId == null) throw new IllegalArgumentException("Path variable '%s' was empty".formatted(key));
            return Long.valueOf(resourceId);
        }
        if (pathVariables.size() > 1) throw new RuntimeException("Ambiguous operation");
        if (pathVariables.isEmpty()) throw new RuntimeException("No path variables found");

        return Long.valueOf(pathVariables.values().stream().findFirst().get());
    }
}
