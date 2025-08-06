package com.demo.language_booking.auth.authorization;

import org.springframework.security.core.Authentication;

public interface IAuthPolicyHandler {
    default boolean isResourceOwner(Authentication authentication) {
        return false;
    };

    default long getResourceOwner(long resourceId) {
        return 0;
    }
}
