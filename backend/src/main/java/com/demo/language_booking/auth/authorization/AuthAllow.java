package com.demo.language_booking.auth.authorization;

import com.demo.language_booking.auth.authentication.RequireJWT;
import com.demo.language_booking.users.User;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@RequireJWT
public @interface AuthAllow {
    User.Role value();
}
