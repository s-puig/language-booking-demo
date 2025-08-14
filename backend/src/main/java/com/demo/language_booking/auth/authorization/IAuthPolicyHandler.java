package com.demo.language_booking.auth.authorization;

public interface IAuthPolicyHandler {
    long getResourceOwner(long resourceId);
}
