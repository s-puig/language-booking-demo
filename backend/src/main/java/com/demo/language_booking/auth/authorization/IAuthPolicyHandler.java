package com.demo.language_booking.auth.authorization;

/**
 * Defines the contract for handling authentication policy decisions regarding resource ownership.
 * Implementations of this interface are responsible for determining the owner of a given resource
 * based on the resource identifier.
 */
public interface IAuthPolicyHandler {
    long getResourceOwner(long resourceId);
}
