package com.demo.language_booking.auth.authorization;

import com.demo.language_booking.auth.authentication.Authenticated;
import com.demo.language_booking.users.User;

import java.lang.annotation.*;

@Authenticated
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Repeatable(AuthorizeCompilation.class)
public @interface Authorize {
    User.Role value();
    Class<? extends IAuthPolicyHandler> policy() default NoPolicy.class;
    String resourceKey() default "";
    boolean requireOwnership() default false;
}
