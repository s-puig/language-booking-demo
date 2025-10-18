package com.demo.language_booking.lessons;

import com.demo.language_booking.SecurityConfig;
import com.demo.language_booking.TestType;
import com.demo.language_booking.lessons.dto.LessonCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Tag(TestType.UNIT)
@Import(SecurityConfig.class)
@TestPropertySource(
		properties = "auth.filter.enabled=false"
)
@WebMvcTest(LessonController.class)
public final class LessonControllerTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private LessonMapper lessonMapper;
	@MockitoBean
	private LessonService lessonService;

	@DisplayName("GET /v1/lessons/{id}")
	@Test
	public void getLesson() throws Exception {
		Long id = 1L;
		Lesson lesson = LessonFactory.VALID_LESSON;

		when(lessonService.findById(id)).thenReturn(java.util.Optional.of(lesson));
		when(lessonMapper.toLessonResponse(lesson)).thenReturn(LessonResponseFactory.VALID_LESSON_RESPONSE);

		mockMvc.perform(get("/api/v1/lessons/%s".formatted(id)))
				.andExpect(status().isOk());
	}

	@DisplayName("GET /v1/lessons/{id} with malformed id is Bad Request")
	@ParameterizedTest
	@ValueSource(strings = {"test", " ", "1asd48"})
	public void getMalformedLessonId(String invalidId) throws Exception {
		mockMvc.perform(get("/api/v1/lessons/%s".formatted(invalidId)))
				.andExpect(status().isBadRequest());
	}

	@DisplayName("GET /v1/lessons/{id} with non-existent lesson is Not Found")
	@Test
	public void getNonExistentLesson() throws Exception {
		Long id = 1L;

		when(lessonService.findById(any())).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/v1/lessons/%s".formatted(id)))
				.andExpect(status().isNotFound());
	}

	@DisplayName("GET /v1/lessons")
	@Test
	public void getAllLessons() throws Exception {
		List<Lesson> lessons = List.of(LessonFactory.VALID_LESSON, LessonFactory.VALID_LESSON);
		when(lessonService.findAll(any())).thenReturn(lessons);
		when(lessonMapper.toLessonResponse(LessonFactory.VALID_LESSON)).thenReturn(LessonResponseFactory.VALID_LESSON_RESPONSE);

		mockMvc.perform(get("/api/v1/lessons"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(lessons
						.stream()
						.map(lessonMapper::toLessonResponse)
						.toList())));
	}

	@DisplayName("GET /v1/lessons with filters")
	@ParameterizedTest
	@CsvSource({"tutor_id, 10",
			"country, ES",
			"category, TEST_PREPARATION",
			"country, en",
			"category, speaking",
			"minPrice, 10.99",
			"maxPrice, 21.99"})
	public void getAllLessonsFilter(String param, String value) throws Exception {
		mockMvc.perform(get("/api/v1/lessons").param(param, value))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@DisplayName("GET /v1/lessons malformed filter is Bad Request")
	@ParameterizedTest
	@CsvSource({"tutor_id, as",
			"country, ESSE",
			"category, TESTi_PREPARATION",
			"minPrice, -20.0,",
			"maxPrice, -10.0"})
	public void getAllLessonsMalformedFilter(String param, String value) throws Exception {
		mockMvc.perform(get("/api/v1/lessons").param(param, value))
				.andExpect(status().isBadRequest());
	}

	@DisplayName("POST /v1/lessons")
	@Test
	public void createLesson() throws Exception {
		LessonCreateRequest lessonCreateRequest = LessonCreateRequestFactory.VALID_CREATE_LESSON;

		when(lessonService.create(lessonCreateRequest)).thenReturn(LessonFactory.from(lessonCreateRequest));

		mockMvc.perform(post("/api/v1/lessons").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(lessonCreateRequest)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "/api/v1/lessons/1"));
	}

	@DisplayName("POST /v1/lessons with invalid data is Bad Request")
	@ParameterizedTest
	@CsvSource
	public void createLessonWithInvalidData() throws Exception {
		mockMvc.perform(post("/api/v1/lessons")
						.contentType("application/json")
						.content("{}"))
				.andExpect(status().isBadRequest());
	}

	@DisplayName("Soft-delete a lesson")
	@Test
	public void deleteLesson() throws Exception {
		Long id = 1L;
		mockMvc.perform(delete("/api/v1/lessons/".formatted(id)))
				.andExpect(status().isNoContent());
	}

	@DisplayName("Delete a non-existent lesson")
	@Test
	public void deleteNonExistentLesson() throws Exception {
		Long id = 1L;
		mockMvc.perform(delete("/api/v1/lessons").param("id", id.toString()))
				.andExpect(status().isNotFound());
	}

	@DisplayName("Update a lesson")
	@Test
	public void updateLesson() throws Exception {
		Long id = 1L;
		mockMvc.perform(put("/api/v1/lessons/".formatted(id)))
				.andExpect(status().isOk());
	}

	@DisplayName("Update lesson with invalid data")
	@Test
	public void updateLessonWithInvalidData() throws Exception {
		Long id = 1L;
		mockMvc.perform(put("/api/v1/lessons/".formatted(id))
						.contentType("application/json")
						.content("{}"))
				.andExpect(status().isBadRequest());
	}
}
