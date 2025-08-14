package com.demo.language_booking.auth;

import com.demo.language_booking.auth.authentication.AuthenticationConfig;
import com.demo.language_booking.common.exceptions.InvalidAuthException;
import com.demo.language_booking.users.User;
import com.demo.language_booking.users.UserMapper;
import com.demo.language_booking.users.UserService;
import com.demo.language_booking.users.dto.UserPublicResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;
import java.io.StringReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

@AllArgsConstructor
@Validated
@Service
public class AuthService {
    private final static String APP_ISSUER = "language-booking";

    private final UserService userService;
    private final AuthenticationConfig authenticationConfig;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;

    public String login(@Valid LoginRequest login){
        User user = userService.authenticate(login.getUsername(), login.getPassword()).orElseThrow(() -> new InvalidAuthException("Invalid user/password"));
        return generateSessionToken(user);
    }

    @NonNull
    public String generateSessionToken(User user) {
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

    @NonNull
    public UserPublicResponse parseToken(@NonNull String token) {
        if (!isTokenValid(token)) throw new InvalidAuthException("Token is invalid or expired");
        Jwt<?, Claims> jwt = (Jwt<?, Claims>) Jwts.parser().keyLocator(authenticationConfig).build().parse(new StringReader(token));

        String user = jwt.getPayload().get("user", String.class);
        try {
            return objectMapper.readValue(user, UserPublicResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON could not be parsed");
        }
    }

    public boolean isTokenValid(@NonNull String token) {
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
}
