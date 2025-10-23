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

/**
 * REST controller for managing lesson resources.
 * Provides endpoints for creating, reading, updating, and deleting lessons.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/lessons", produces = MediaType.APPLICATION_JSON_VALUE)
public class LessonController {
	private final LessonService lessonService;
	private final LessonMapper lessonMapper;

	/**
	 * Retrieves all lessons matching the provided filter criteria.
	 *
	 * @param filter the filter criteria to apply when retrieving lessons
	 * @return a ResponseEntity containing a list of lesson responses
	 */
	@GetMapping
	public ResponseEntity<List<LessonResponse>> getAll(@Valid @ModelAttribute LessonFilter filter) {
		List<LessonResponse> lessons = lessonService.findAll(filter)
				.stream()
				.map(lessonMapper::toLessonResponse)
				.toList();

		return ResponseEntity.ok(lessons);
	}

	/**
	 * Retrieves a lesson by its ID.
	 *
	 * @param id the ID of the lesson to retrieve
	 * @return a ResponseEntity containing the lesson response
	 */
	@GetMapping("{id}")
	public ResponseEntity<LessonResponse> getById(@PathVariable Long id) {
		Lesson lesson = lessonService.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Lesson id %s not found".formatted(id)));

		return ResponseEntity.ok(lessonMapper.toLessonResponse(lesson));
	}

	/**
	 * Updates a lesson by its ID.
	 *
	 * @param id                  the ID of the lesson to update
	 * @param lessonUpdateRequest the request containing updated lesson details
	 * @return a ResponseEntity containing the updated lesson response
	 */
	@Authorize(User.Role.ADMIN)
	@Authorize(value = User.Role.TEACHER, requireOwnership = true)
	@PutMapping("{id}")
	public ResponseEntity<LessonResponse> updateById(@PathVariable Long id, @Valid @RequestBody LessonUpdateRequest lessonUpdateRequest) {
		Lesson lesson = lessonService.update(id, lessonUpdateRequest);

		return ResponseEntity.ok(lessonMapper.toLessonResponse(lesson));
	}


	/**
	 * Creates a new lesson.
	 *
	 * @param lessonCreateRequest the request containing details for creating the lesson
	 * @param session             the current user session
	 * @return a ResponseEntity containing the created lesson response with location header
	 */
	@Authorize(User.Role.ADMIN)
	@Authorize(User.Role.TEACHER)
	@PostMapping
	public ResponseEntity<LessonResponse> create(@Valid @RequestBody LessonCreateRequest lessonCreateRequest, CurrentSession session) {
		Lesson lesson = lessonService.create(session.getId(), lessonCreateRequest);

		return ResponseEntity.created(java.net.URI.create("/api/v1/lessons/%s".formatted(lesson.getId())))
				.body(lessonMapper.toLessonResponse(lesson));
	}

	/**
	 * Deletes a lesson by its ID.
	 *
	 * @param id the ID of the lesson to delete
	 * @return a ResponseEntity indicating no content
	 */
	@Authorize(User.Role.ADMIN)
	@Authorize(value = User.Role.TEACHER, requireOwnership = true)
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		lessonService.delete(id);

		return ResponseEntity.noContent()
				.build();
	}
}
