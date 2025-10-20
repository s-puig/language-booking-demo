package com.demo.language_booking.lessons;

import com.demo.language_booking.common.exceptions.ResourceNotFoundException;
import com.demo.language_booking.lessons.dto.LessonCreateRequest;
import com.demo.language_booking.lessons.dto.LessonUpdateRequest;
import com.demo.language_booking.users.User;
import com.demo.language_booking.users.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Validated
@Service
public class DefaultLessonService implements LessonService {
	private final LessonRepository lessonRepository;
	private final LessonMapper lessonMapper;
	private final UserService userService;

	@Override
	public Optional<Lesson> findById(long id) {
		return lessonRepository.findById(id);
	}

	@NonNull
	@Override
	public List<Lesson> findAll() {
		return lessonRepository.findAll();
	}

	@NonNull
	@Override
	public List<Lesson> findAll(@Valid @NotNull LessonFilter filter) {
		return List.of();
	}

	@Transactional
	@NonNull
	@Override
	public Lesson create(long userId, @Valid @NotNull LessonCreateRequest lessonCreateRequest) {
		Lesson lesson = lessonMapper.toLesson(lessonCreateRequest);
		User user = userService.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
		lesson.setTutor(user);
		return lessonRepository.save(lesson);
	}

	@NonNull
	@Transactional
	@Override
	public Lesson update(long id, @Valid @NotNull LessonUpdateRequest lessonUpdateRequest) {
		Lesson lesson = lessonRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
		lesson.setName(lessonUpdateRequest.getName());
		lesson.setDescription(lessonUpdateRequest.getDescription());
		lesson.setLessonCategories(new HashSet<>(lessonUpdateRequest.getLessonCategories()));
		lesson.setPrice(lessonUpdateRequest.getPrice());

		return lessonRepository.save(lesson);
	}

	@Transactional
	@Override
	public void delete(long id) {
		if (!lessonRepository.existsById(id)) {
			throw new ResourceNotFoundException("Lesson not found with id: " + id);
		}
		lessonRepository.deleteById(id);
	}

	@Transactional
	@NonNull
	@Override
	public Lesson save(Lesson lesson) {
		return lessonRepository.save(lesson);
	}
}
