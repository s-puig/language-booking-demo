package com.demo.language_booking.lessons;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
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
	public List<Lesson> findAll(LessonFilter filter) {
		return List.of();
	}
}
