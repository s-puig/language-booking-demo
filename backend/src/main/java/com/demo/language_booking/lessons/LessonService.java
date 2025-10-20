package com.demo.language_booking.lessons;

import com.demo.language_booking.lessons.dto.LessonCreateRequest;
import com.demo.language_booking.lessons.dto.LessonUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface LessonService {
	Optional<Lesson> findById(long id);

	@NonNull
	List<Lesson> findAll();

	@NonNull
	List<Lesson> findAll(@Valid @NotNull LessonFilter filter);

	@NonNull
	Lesson create(long userId, @Valid @NotNull LessonCreateRequest lessonCreateRequest);

	@NonNull
	Lesson update(long id, @Valid @NotNull LessonUpdateRequest lessonUpdateRequest);

	void delete(long id);

	Lesson save(@Valid @NotNull Lesson lesson);
}
