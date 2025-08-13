package com.demo.language_booking.auth.authorization;

public class NoPolicy implements IAuthPolicyHandler {

    @Override
    public long getResourceOwner(long resourceId) {
        throw new RuntimeException("This should never be called");
    }
}
