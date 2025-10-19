package com.demo.language_booking.lessons;

import com.demo.language_booking.lessons.dto.LessonCreateRequest;
import com.demo.language_booking.lessons.dto.LessonUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Service
public class DefaultLessonService implements LessonService {
	private final LessonRepository lessonRepository;

	@Override
	public Optional<Lesson> findById(long id) {
		return Optional.empty();
	}

	@NonNull
	@Override
	public List<Lesson> findAll() {
		return List.of();
	}

	@NonNull
	@Override
	public List<Lesson> findAll(@Valid @NotNull LessonFilter filter) {
		return List.of();
	}

	@NonNull
	@Override
	public Lesson create(@Valid @NotNull LessonCreateRequest lessonCreateRequest) {
		return null;
	}

	@NonNull
	@Override
	public Lesson update(long id, @Valid @NotNull LessonUpdateRequest lessonUpdateRequest) {
		return null;
	}

	@Override
	public void delete(long id) {
	}

	@NonNull
	@Override
	public Lesson save(Lesson lesson) {
		return null;
	}
}
