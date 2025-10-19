package com.demo.language_booking.lessons.dto;

import com.demo.language_booking.lessons.LessonCategory;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Builder
@Data
public class LessonCreateRequest {
	@Positive(message = "Tutor id must be positive")
	private long tutorId;
	@NotBlank
	@Size(max = 32, message = "Name must be less than 32 characters")
	private String name;
	@Size(max = 256, message = "Description must be less than 256 characters")
	private String description;
	@DecimalMin(value = "0.00", message = "Price must be positive")
	private BigDecimal price;
	@NotNull(message = "Categories cannot be null")
	@NotEmpty(message = "At least one category is required")
	private Set<LessonCategory> lessonCategories;
}
