package com.demo.language_booking.auth;

import com.demo.language_booking.common.exceptions.InvalidAuthException;
import com.demo.language_booking.users.dto.UserPublicResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for authentification and JWT token management
 */
@Validated
public interface AuthService {
    /**
     * Logs in a user with the provided credentials
     * @param login The login request containing username and password.
     * @return a JWT string token containing the user information.
     */
    @NonNull
    String login(@Valid @NotNull LoginRequest login);

    /**
     * Parses a JWT token to its user representation
     * @param token The JWT user token
     * @return {@link UserPublicResponse} representing the User held in the token.
     * @throws InvalidAuthException if the token is invalid or expired
     */
    @NonNull
    UserPublicResponse parseToken(@NotNull String token);

    /**
     * Checks if the session token is valid
     * @param token The JWT user token
     * @return the validity of the token
     */
    boolean isTokenValid(@NotNull String token);
}
