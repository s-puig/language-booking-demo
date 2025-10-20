package com.demo.language_booking.lessons;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPriceRangeConstraint.class)
@Documented
public @interface ValidPriceRange {
	String message() default "minPrice must be less than or equal to maxPrice";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
