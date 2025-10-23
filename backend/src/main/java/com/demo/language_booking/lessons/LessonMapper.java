package com.demo.language_booking.lessons;

import com.demo.language_booking.lessons.dto.LessonCreateRequest;
import com.demo.language_booking.lessons.dto.LessonResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface LessonMapper {
	/**
	 * Converts a Lesson entity to a LessonResponse.
	 *
	 * @param lesson the lesson entity to convert
	 * @return the corresponding LessonResponse
	 */
	LessonResponse toLessonResponse(Lesson lesson);

	/**
	 * Converts a LessonCreateRequest to a Lesson entity.
	 *
	 * @param lessonCreateRequest the lesson creation request to convert
	 * @return the corresponding Lesson entity
	 */
	Lesson toLesson(LessonCreateRequest lessonCreateRequest);
}
