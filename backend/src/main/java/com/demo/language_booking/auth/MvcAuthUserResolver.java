package com.demo.language_booking.auth;

import com.demo.language_booking.common.exceptions.UnavailableSessionException;
import com.demo.language_booking.users.dto.UserPublicResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MvcAuthUserResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(CurrentSession.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest httpRequest =(HttpServletRequest) webRequest.getNativeRequest();
        UserPublicResponse userSession = (UserPublicResponse) httpRequest.getAttribute("session");
        if (userSession == null) throw new UnavailableSessionException("Active session not found");
        // TODO: This should use a mapper instead of being done by hand
        return CurrentSession.builder()
                .id(userSession.getId())
                .role(userSession.getRole())
                .email(userSession.getEmail())
                .username(userSession.getUsername())
                .build();
    }
}
