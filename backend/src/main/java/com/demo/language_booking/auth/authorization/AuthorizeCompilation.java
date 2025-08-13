package com.demo.language_booking.auth.authorization;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AuthorizeCompilation {
    Authorize[] value();
}
