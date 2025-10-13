package com.demo.language_booking.lessons;

import com.demo.language_booking.common.exceptions.ResourceNotFoundException;
import com.demo.language_booking.lessons.dto.LessonResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/lessons/", produces = MediaType.APPLICATION_JSON_VALUE)
public class LessonController {
	private final LessonService lessonService;
	private final LessonMapper lessonMapper = LessonMapper.INSTANCE;

	@GetMapping("{id}")
	public ResponseEntity<LessonResponse> getById(@PathVariable Long id) {
		Lesson lesson = lessonService.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Lesson id %s not found".formatted(id)));

		return ResponseEntity.ok(lessonMapper.toLessonResponse(lesson));
	}


}
