package com.demo.language_booking.auth.authorization;

import com.demo.language_booking.auth.authentication.Authenticated;

import java.lang.annotation.*;

@Authenticated
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AuthorizeCompilation {
    Authorize[] value();
}
