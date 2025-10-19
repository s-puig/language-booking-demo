package com.demo.language_booking.lessons;

import com.demo.language_booking.common.Country;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.stream.Stream;

@ValidPriceRange
@Builder
@Data
public class LessonFilter {
	// Tutor's filter properties
	@Positive(message = "Tutor has to be a postive number")
	private Long tutorId;
	private Country country;

	// Lesson's filter properties
	private LessonCategory lessonCategory;
	// TODO: Missing min max price validator
	@DecimalMin(value = "0.00", message = "Minimum price must be positive")
	private BigDecimal minPrice;
	@DecimalMin(value = "0.00", message = "Maximum price must be positive")
	private BigDecimal maxPrice;

	public boolean isEmpty() {
		return Stream.of(tutorId, country, lessonCategory, minPrice, maxPrice)
				.allMatch(java.util.Objects::isNull);
	}
}
