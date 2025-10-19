package com.demo.language_booking.lessons;

import com.demo.language_booking.InvalidFieldCase;
import com.demo.language_booking.lessons.dto.LessonCreateRequest;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;

public class LessonCreateRequestFactory {
	public static final LessonCreateRequest VALID_CREATE_LESSON = validCreateLessonRequestBuilder().build();

	public static LessonCreateRequest.LessonCreateRequestBuilder validCreateLessonRequestBuilder() {
		return LessonCreateRequest.builder()
				.name("Default lesson")
				.description("Default lesson description")
				.lessonCategories(
						Set.of(LessonCategory.INTERVIEW_PREPARATION))
				.price(BigDecimal.valueOf(15.99));
	}

	public static Stream<InvalidFieldCase> getInvalidTestCases() {
		return Stream.of(new InvalidFieldCase("name", ""),
				new InvalidFieldCase("name", "A".repeat(33)),
				new InvalidFieldCase("description", "A".repeat(257)),
				new InvalidFieldCase("price", BigDecimal.valueOf(-10.5)),
				new InvalidFieldCase("lessonCategories", Set.of()));
	}
}
