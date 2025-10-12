package com.demo.language_booking.auth;

import com.demo.language_booking.auth.authentication.AuthenticationConfig;
import com.demo.language_booking.common.exceptions.InvalidAuthException;
import com.demo.language_booking.users.User;
import com.demo.language_booking.users.UserMapper;
import com.demo.language_booking.users.UserService;
import com.demo.language_booking.users.dto.UserPublicResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.StringReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * The primary authentification service
 */
@AllArgsConstructor
@Validated
@Service
@Primary
public class DefaultAuthService implements AuthService {
    private final static String APP_ISSUER = "language-booking";

    private final UserService userService;
    private final AuthenticationConfig authenticationConfig;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;

    @Override
    @NonNull
    public String login(@Valid @NotNull LoginRequest login){
        User user = userService.authenticate(login.getUsername(), login.getPassword()).orElseThrow(() -> new InvalidAuthException("Invalid user/password"));
        return generateSessionToken(user);
    }

    @Override
    @NonNull
    public UserPublicResponse parseToken(@NotNull String token) {
        if (!isTokenValid(token)) throw new InvalidAuthException("Token is invalid or expired");
        Jwt<?, Claims> jwt = (Jwt<?, Claims>) Jwts.parser().keyLocator(authenticationConfig).build().parse(new StringReader(token));

        String user = jwt.getPayload().get("user", String.class);
        try {
            return objectMapper.readValue(user, UserPublicResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON could not be parsed");
        }
    }

    @Override
    public boolean isTokenValid(@NotNull String token) {
        try {
            Jwt<?, Claims> jwt = (Jwt<?, Claims>) Jwts.parser()
                    .requireIssuer(APP_ISSUER)
                    .keyLocator(authenticationConfig)
                    .build()
                    .parse(new StringReader(token));

            Date expiration = jwt.getPayload().getExpiration();
            return expiration.after(Date.from(Instant.now()));
        } catch (JwtException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a JWT token from a given User
     * @param user The {@link User} that will be turned into a token
     * @return A JWT token of the User.
     */
    @NonNull
    private String generateSessionToken(User user) {
        Instant now = Instant.now();
        String userJson;
        try {
            userJson = objectMapper.writeValueAsString(userMapper.mapToUserPublicResponse(user));
        } catch (JsonProcessingException e) {
            // This should not be possible to occur under normal circumstances.
            throw new RuntimeException(e);
        }
        return Jwts.builder()
                .header()
                .keyId(AuthenticationConfig.TokenType.SESSION.toString())
                .and()
                .issuer(APP_ISSUER)
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(authenticationConfig.getSessionExpiration(), ChronoUnit.SECONDS)))
                .signWith(authenticationConfig.getSessionSecret())
                .claim("user", userJson)
                .compact();
    }
}
