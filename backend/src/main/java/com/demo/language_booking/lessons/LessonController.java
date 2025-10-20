package com.demo.language_booking.lessons;

import com.demo.language_booking.auth.CurrentSession;
import com.demo.language_booking.auth.authorization.Authorize;
import com.demo.language_booking.common.exceptions.ResourceNotFoundException;
import com.demo.language_booking.lessons.dto.LessonCreateRequest;
import com.demo.language_booking.lessons.dto.LessonResponse;
import com.demo.language_booking.lessons.dto.LessonUpdateRequest;
import com.demo.language_booking.users.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/lessons", produces = MediaType.APPLICATION_JSON_VALUE)
public class LessonController {
	private final LessonService lessonService;
	private final LessonMapper lessonMapper;

	@GetMapping
	public ResponseEntity<List<LessonResponse>> getAll(@Valid @ModelAttribute LessonFilter filter) {
		List<LessonResponse> lessons = lessonService.findAll(filter)
				.stream()
				.map(lessonMapper::toLessonResponse)
				.toList();

		return ResponseEntity.ok(lessons);
	}

	@GetMapping("{id}")
	public ResponseEntity<LessonResponse> getById(@PathVariable Long id) {
		Lesson lesson = lessonService.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Lesson id %s not found".formatted(id)));

		return ResponseEntity.ok(lessonMapper.toLessonResponse(lesson));
	}

	@Authorize(User.Role.ADMIN)
	@Authorize(value = User.Role.TEACHER, requireOwnership = true)
	@PutMapping("{id}")
	public ResponseEntity<LessonResponse> updateById(@PathVariable Long id, @Valid @RequestBody LessonUpdateRequest lessonUpdateRequest) {
		Lesson lesson = lessonService.update(id, lessonUpdateRequest);

		return ResponseEntity.ok(lessonMapper.toLessonResponse(lesson));
	}


	@Authorize(User.Role.ADMIN)
	@Authorize(User.Role.TEACHER)
	@PostMapping
	public ResponseEntity<LessonResponse> create(@Valid @RequestBody LessonCreateRequest lessonCreateRequest, CurrentSession session) {
		Lesson lesson = lessonService.create(session.getId(), lessonCreateRequest);

		return ResponseEntity.created(java.net.URI.create("/api/v1/lessons/%s".formatted(lesson.getId())))
				.body(lessonMapper.toLessonResponse(lesson));
	}

	@Authorize(User.Role.ADMIN)
	@Authorize(value = User.Role.TEACHER, requireOwnership = true)
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		lessonService.delete(id);

		return ResponseEntity.noContent()
				.build();
	}
}
