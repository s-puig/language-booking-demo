package com.demo.language_booking.lessons.dto;

import com.demo.language_booking.lessons.LessonCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Data transfer object representing a lesson response.
 */
@Builder
@Data
public class LessonResponse {
	/**
	 * The unique identifier of the lesson.
	 */
	public long id;
	/**
	 * The unique identifier of the tutor who created the lesson.
	 */
	public long tutorId;
	/**
	 * The name of the lesson.
	 */
	public String name;
	/**
	 * The description of the lesson.
	 */
	public String description;
	/**
	 * The price of the lesson.
	 */
	public BigDecimal price;
	/**
	 * The categories associated with the lesson.
	 */
	public Set<LessonCategory> lessonCategories;
}
