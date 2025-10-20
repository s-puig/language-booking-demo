package com.demo.language_booking.lessons;

import com.demo.language_booking.InvalidFieldCase;
import com.demo.language_booking.lessons.dto.LessonCreateRequest;
import com.demo.language_booking.lessons.dto.LessonUpdateRequest;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;

public final class LessonUpdateRequestFactory {
	public static final LessonUpdateRequest VALID_UPDATE_LESSON = validUpdateLessonRequestBuilder().build();

	public static LessonUpdateRequest.LessonUpdateRequestBuilder validUpdateLessonRequestBuilder() {
		LessonCreateRequest LESSON = LessonCreateRequestFactory.VALID_LESSON_CREATE_REQUEST;
		return LessonUpdateRequest.builder()
				.lessonCategories(LESSON.getLessonCategories())
				.description(LESSON.getDescription())
				.name(LESSON.getName())
				.price(LESSON.getPrice());

	}

	public static Stream<InvalidFieldCase> getInvalidTestCases() {
		return Stream.of(new InvalidFieldCase("name", ""),
				new InvalidFieldCase("name", "A".repeat(33)),
				new InvalidFieldCase("description", "A".repeat(257)),
				new InvalidFieldCase("price", BigDecimal.valueOf(-10.5)),
				new InvalidFieldCase("lessonCategories", Set.of()));
	}
}
