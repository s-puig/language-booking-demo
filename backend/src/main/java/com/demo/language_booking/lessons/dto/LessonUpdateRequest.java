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

@Builder
@Data
public class LessonUpdateRequest {
	@NotBlank
	@Size(max = 32, message = "Name must be less than 32 characters")
	private String name;
	@Size(max = 256, message = "Description must be less than 256 characters")
	private String description;
	@DecimalMin(value = "0.00", message = "Price must be positive")
	private BigDecimal price;
	@NotEmpty(message = "At least one category is required")
	private Set<LessonCategory> lessonCategories;
}
