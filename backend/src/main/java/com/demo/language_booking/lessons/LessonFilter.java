package com.demo.language_booking.lessons;

import com.demo.language_booking.common.Country;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Filter class for querying lessons with various criteria.
 */
@ValidPriceRange
@Builder
@Data
public class LessonFilter {
	/**
	 * The ID of the tutor to filter lessons by.
	 */
	@Positive(message = "Tutor has to be a postive number")
	private Long tutorId;
	/**
	 * The author's country to filter lessons by.
	 */
	private Country country;
	/**
	 * The lesson category to filter lessons by.
	 * A lesson only has to include one of the specified categories, not match all of them.
	 */
	private Set<LessonCategory> lessonCategory;
	/**
	 * The minimum price to filter lessons by.
	 */
	@DecimalMin(value = "0.00", message = "Minimum price must be positive")
	private BigDecimal minPrice;
	/**
	 * The maximum price to filter lessons by.
	 */
	@DecimalMin(value = "0.00", message = "Maximum price must be positive")
	private BigDecimal maxPrice;

	/**
	 * Checks if all filter fields are null.
	 *
	 * @return true if all filter fields are null, false otherwise
	 */
	public boolean isEmpty() {
		return Stream.of(tutorId, country, lessonCategory, minPrice, maxPrice)
				.allMatch(java.util.Objects::isNull);
	}
}
