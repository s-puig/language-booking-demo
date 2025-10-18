package com.demo.language_booking.lessons;

import com.demo.language_booking.lessons.dto.LessonCreateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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
	public Optional<Lesson> findById(Long id) {
		return Optional.empty();
	}

	@Override
	public List<Lesson> findAll() {
		return List.of();
	}

	@Override
	public List<Lesson> findAll(@Valid @NotNull LessonFilter filter) {
		return List.of();
	}

	@Override
	public Lesson create(@Valid @NotNull LessonCreateRequest lessonCreateRequest) {
		return null;
	}
}
