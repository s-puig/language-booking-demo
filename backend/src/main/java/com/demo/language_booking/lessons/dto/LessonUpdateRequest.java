package com.demo.language_booking.lessons.dto;

import com.demo.language_booking.lessons.LessonCategory;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Data transfer object for updating lesson information.
 */
@Builder
@Data
public class LessonUpdateRequest {
	/**
	 * The name of the lesson.
	 */
	@NotBlank
	@Size(max = 32, message = "Name must be less than 32 characters")
	private String name;
	/**
	 * The description of the lesson.
	 */
	@Size(max = 256, message = "Description must be less than 256 characters")
	private String description;
	/**
	 * The price of the lesson.
	 */
	@DecimalMin(value = "0.00", message = "Price must be positive")
	private BigDecimal price;
	/**
	 * The categories associated with the lesson.
	 */
	@NotEmpty(message = "At least one category is required")
	private Set<LessonCategory> lessonCategories;
}
