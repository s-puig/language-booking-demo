package com.demo.language_booking.auth;

import com.demo.language_booking.auth.authentication.AuthenticationConfig;
import com.demo.language_booking.users.User;
import com.demo.language_booking.users.UserMapper;
import com.demo.language_booking.users.UserService;
import com.demo.language_booking.users.dto.UserPublicResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;
import java.io.StringReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

@Validated
@Service
public class AuthService {
    private final static String APP_ISSUER = "language-booking";

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationConfig authenticationConfig;

    @NonNull
    public String generateSessionToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .header()
                .keyId(AuthenticationConfig.TokenType.SESSION.toString())
                .and()
                .issuer(APP_ISSUER)
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(authenticationConfig.getSessionExpiration(), ChronoUnit.SECONDS)))
                .signWith(authenticationConfig.getSessionSecret())
                .claim("role", user.getRole())
                .claim("id", user.getId())
                .compact();
    }

    public boolean isTokenValid(@Valid @NonNull String token, SecretKey secretKey) {
        Jwt jwt = Jwts.parser()
                .keyLocator(authenticationConfig)
                .build()
                .parse(new StringReader(token));
    }
}
