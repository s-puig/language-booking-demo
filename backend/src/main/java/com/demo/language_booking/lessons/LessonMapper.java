package com.demo.language_booking.lessons;

import com.demo.language_booking.lessons.dto.LessonResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface LessonMapper {
	LessonResponse toLessonResponse(Lesson lesson);
}
