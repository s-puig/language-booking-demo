package com.demo.language_booking.lessons;

import com.demo.language_booking.lessons.dto.LessonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.WARN)
public interface LessonMapper {
	LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

	LessonResponse toLessonResponse(Lesson lesson);
}
