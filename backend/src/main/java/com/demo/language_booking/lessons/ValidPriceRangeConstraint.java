package com.demo.language_booking.lessons;

import jakarta.validation.ConstraintValidator;

/**
 * Constraint validator to ensure that the minimum price is not greater than the maximum price
 * in a lesson filter.
 */
public class ValidPriceRangeConstraint implements ConstraintValidator<ValidPriceRange, LessonFilter> {
	/**
	 * Checks if the price range in the lesson filter is valid.
	 *
	 * @param lessonFilter the lesson filter to validate
	 * @param context      the validation context
	 * @return true if the price range is valid or if the filter is null, false otherwise
	 */
	@Override
	public boolean isValid(LessonFilter lessonFilter, jakarta.validation.ConstraintValidatorContext context) {
		if (lessonFilter == null) return true;
		if (lessonFilter.getMinPrice() == null || lessonFilter.getMaxPrice() == null) return true;

		return lessonFilter.getMinPrice()
				.compareTo(lessonFilter.getMaxPrice()) <= 0;
	}
}
