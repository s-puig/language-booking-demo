package com.demo.language_booking.auth.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;

@Component
public class AuthenticationValidationFilter extends OncePerRequestFilter {
    @Autowired
    RequestMappingHandlerMapping handlerMapping;

    @Autowired
    AuthenticationConfig authenticationConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HandlerExecutionChain handle;
        try {
            handle = handlerMapping.getHandler(request);
            if(handle == null) throw new NullPointerException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HandlerMethod handlerMethod = (HandlerMethod) handle.getHandler();
        boolean isEndpointProtected = handlerMethod.hasMethodAnnotation(RequireJWT.class) ||
                    handlerMethod.getBeanType().isAnnotationPresent(RequireJWT.class);
        if (isEndpointProtected) {
            String jwt = request.getHeader("Authorization");
        }
        filterChain.doFilter(request, response);
    }
}
