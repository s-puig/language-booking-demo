package com.demo.language_booking.lessons;

import jakarta.validation.ConstraintValidator;

public class ValidPriceRangeConstraint implements ConstraintValidator<ValidPriceRange, LessonFilter> {
	@Override
	public boolean isValid(LessonFilter lessonFilter, jakarta.validation.ConstraintValidatorContext context) {
		if (lessonFilter == null) return true;
		if (lessonFilter.getMinPrice() == null || lessonFilter.getMaxPrice() == null) return true;

		return lessonFilter.getMinPrice()
				.compareTo(lessonFilter.getMaxPrice()) <= 0;
	}
}
