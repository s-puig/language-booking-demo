package com.demo.language_booking.auth.authentication;

import io.jsonwebtoken.LocatorAdapter;
import io.jsonwebtoken.ProtectedHeader;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.security.Key;

@EqualsAndHashCode(callSuper = true)
@Data
@Configuration
@Slf4j
public class AuthenticationConfig extends LocatorAdapter<Key> {
    public enum TokenType {
        SESSION,
        REFRESH
    }
    private static final String DEFAULT_SECRET_KEY = "lES1atQ25GLadTgGQxjHG9+9Ww6pAtNwZPsnzCp5yi5WpV9AYbycWrJ6H8fq/UU5";

    //TODO: Get rid of this by using a hashmap/array in the future
    private SecretKey sessionSecret;
    private SecretKey refreshSecret;

    @Value("${jwt.session.expiration:3600000}")
    private long sessionExpiration;

    @Value("${jwt.refresh.expiration:604800000}")
    private long refreshExpiration;

    @Autowired
    public AuthenticationConfig(@Value("${jwt.session.secret:" + DEFAULT_SECRET_KEY + "}") String sessionSecret, @Value("${jwt.refresh.secret:"+ DEFAULT_SECRET_KEY +"}") String refreshSecret) {
        if (sessionSecret.equals(DEFAULT_SECRET_KEY) || refreshSecret.equals(DEFAULT_SECRET_KEY)) {
            log.warn("Using the default session secret(s)! You should change them in a production environment!");
        }
        this.sessionSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(sessionSecret));
        this.refreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));
    }

    @Override
    protected Key locate(ProtectedHeader header) {
        String kid = header.getKeyId();
        if (kid == null || kid.isEmpty()) {
            throw new IllegalArgumentException("JWT KID header was empty or null");
        }

        try {
            TokenType tokenType = TokenType.valueOf(kid);
            return switch (tokenType) {
                case SESSION -> sessionSecret;
                case REFRESH -> refreshSecret;
            };
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("JWT KID header '%s' is invalid", kid));
        }
    }
}