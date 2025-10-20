package com.demo.language_booking.lessons;

import com.demo.language_booking.lessons.dto.LessonCreateRequest;
import com.demo.language_booking.users.User;

public final class LessonFactory {
	public static final Lesson VALID_LESSON = validLessonBuilder().build();

	public static final Lesson INVALID_NAME_LESSON = validLessonBuilder()
			.name("test")
			.build();

	private static Lesson.LessonBuilder lessonBuilder(LessonCreateRequest lessonCreateRequest) {
		return Lesson.builder()
				.tutor(User.builder()
						.id(1L)
						.build())
				.name(lessonCreateRequest.getName())
				.description(lessonCreateRequest.getDescription())
				.lessonCategories(lessonCreateRequest.getLessonCategories())
				.price(lessonCreateRequest.getPrice())
				.id(1L);
	}

	public static Lesson from(LessonCreateRequest lessonCreateRequest) {
		return lessonBuilder(lessonCreateRequest).build();
	}

	public static Lesson from(LessonCreateRequest lessonCreateRequest, User user) {
		return lessonBuilder(lessonCreateRequest)
				.tutor(user)
				.build();
	}

	public static Lesson.LessonBuilder validLessonBuilder() {
		return lessonBuilder(LessonCreateRequestFactory.VALID_LESSON_CREATE_REQUEST);
	}
}
