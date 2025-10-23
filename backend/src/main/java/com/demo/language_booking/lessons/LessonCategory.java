package com.demo.language_booking.lessons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum representing different categories of language lessons.
 * This enum is used to classify and filter lessons based on their educational focus.
 */
public enum LessonCategory {
	VOCABULARY,
	ACCENT_REDUCTION,
	SPEAKING,
	LISTENING,
	TRANSLATION,
	TEST_PREPARATION,
	INTERVIEW_PREPARATION;

	/**
	 * Creates a LessonCategory instance from a string representation.
	 * The input string is converted to uppercase and matched against enum constants.
	 *
	 * @param value the string representation of the lesson category
	 * @return the corresponding LessonCategory enum constant
	 */
	@JsonCreator
	public static LessonCategory from(String value) {
		return valueOf(value.toUpperCase());
	}

	/**
	 * Converts the LessonCategory instance to its string representation.
	 * The returned string is the lowercase version of the enum constant name.
	 *
	 * @return the lowercase string representation of this lesson category
	 */
	@JsonValue
	public String toValue() {
		return name().toLowerCase();
	}
}
