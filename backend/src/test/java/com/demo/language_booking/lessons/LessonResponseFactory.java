package com.demo.language_booking.lessons;

import com.demo.language_booking.lessons.dto.LessonResponse;

public final class LessonResponseFactory {
	public static final LessonResponse VALID_LESSON_RESPONSE = LessonToLessonResponseBuilder(LessonFactory.VALID_LESSON).build();

	public static LessonResponse.LessonResponseBuilder LessonToLessonResponseBuilder(Lesson lesson) {
		return LessonResponse.builder()
				.id(lesson.getId())
				.tutorId(lesson.getTutor()
						.getId())
				.name(lesson.getName())
				.description(lesson.getDescription())
				.price(lesson.getPrice())
				.lessonCategories(lesson.getLessonCategories());
	}
}
