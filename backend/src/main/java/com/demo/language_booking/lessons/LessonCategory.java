package com.demo.language_booking.lessons;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LessonCategory {
	VOCABULARY,
	ACCENT_REDUCTION,
	SPEAKING,
	LISTENING,
	TRANSLATION,
	TEST_PREPARATION,
	INTERVIEW_PREPARATION;

	@JsonCreator
	public static LessonCategory from(String value) {
		return valueOf(value.toUpperCase());
	}

	@JsonValue
	public String toValue() {
		return name().toLowerCase();
	}
}
