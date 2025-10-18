package com.demo.language_booking.lessons;

import com.demo.language_booking.lessons.dto.LessonCreateRequest;

import java.math.BigDecimal;
import java.util.Set;

public class LessonCreateRequestFactory {
	public static final LessonCreateRequest VALID_CREATE_LESSON = validCreateLessonRequestBuilder().build();

	public static LessonCreateRequest.LessonCreateRequestBuilder validCreateLessonRequestBuilder() {
		return LessonCreateRequest.builder()
				.tutor(1)
				.name("Default lesson")
				.description("Default lesson description")
				.lessonCategories(
						Set.of(LessonCategory.INTERVIEW_PREPARATION))
				.price(BigDecimal.valueOf(15.99));
	}
}
