package com.demo.language_booking.auth.authorization;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class AuthAspect {
    @Autowired
    private ApplicationContext context;

    @Before("@annotation(AuthAllow)")
    public void authorizeEndpoint(JoinPoint joinPoint) {
        System.out.println("I was run!");
        AuthPolicy authPolicy = joinPoint.getClass().getAnnotation(AuthPolicy.class);
        //if (authPolicy == null) throw new RuntimeException("Class is missing a security policy");
        IAuthPolicyHandler authPolicyHandler = context.getBean(authPolicy.value());
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        AuthAllow authAllow = method.getAnnotation(AuthAllow.class);
        System.out.println(authAllow.value());
    }
}
