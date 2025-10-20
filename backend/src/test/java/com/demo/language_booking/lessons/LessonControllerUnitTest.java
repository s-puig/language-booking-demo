package com.demo.language_booking.lessons;

import com.demo.language_booking.InvalidFieldCase;
import com.demo.language_booking.SecurityConfig;
import com.demo.language_booking.TestType;
import com.demo.language_booking.auth.MvcAuthConfig;
import com.demo.language_booking.auth.MvcAuthUserResolver;
import com.demo.language_booking.auth.SessionFactory;
import com.demo.language_booking.common.exceptions.ResourceNotFoundException;
import com.demo.language_booking.lessons.dto.LessonCreateRequest;
import com.demo.language_booking.lessons.dto.LessonUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Tag(TestType.UNIT)
@Import({SecurityConfig.class, MvcAuthConfig.class, MvcAuthUserResolver.class})
@TestPropertySource(
		properties = "auth.filter.enabled=false"
)
@WebMvcTest(LessonController.class)
public final class LessonControllerUnitTest {
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
		Lesson lesson = LessonFactory.VALID_LESSON;
		long id = lesson.getId();

		when(lessonService.findById(id)).thenReturn(java.util.Optional.of(lesson));
		when(lessonMapper.toLessonResponse(lesson)).thenReturn(LessonResponseFactory.VALID_LESSON_RESPONSE);

		mockMvc.perform(get("/api/v1/lessons/%s".formatted(id)))
				.andExpect(status().isOk());
	}

	@DisplayName("GET /v1/lessons/{id} - Malformed id is Bad Request")
	@ParameterizedTest
	@ValueSource(strings = {"test", " ", "1asd48"})
	public void getMalformedLessonId(String invalidId) throws Exception {
		mockMvc.perform(get("/api/v1/lessons/%s".formatted(invalidId)))
				.andExpect(status().isBadRequest());
	}

	@DisplayName("GET /v1/lessons/{id} - Non-existent lesson is Not Found")
	@Test
	public void getNonExistentLesson() throws Exception {
		Long id = 1L;

		when(lessonService.findById(anyLong())).thenReturn(Optional.empty());

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

	@DisplayName("GET /v1/lessons - Filtered")
	@ParameterizedTest
	@CsvSource({"tutorId, 10",
			"country, ES",
			"lessonCategory, TEST_PREPARATION",
			"minPrice, 10.99",
			"maxPrice, 21.99"})
	public void getAllLessonsFilter(String param, String value) throws Exception {
		mockMvc.perform(get("/api/v1/lessons").param(param, value))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@DisplayName("GET /v1/lessons - Malformed filter is Bad Request")
	@ParameterizedTest
	@CsvSource({"tutorId, as",
			"country, ESSE",
			"lessonCategory, TESTi_PREPARATION",
			"minPrice, -20.0,",
			"maxPrice, -10.0"})
	public void getAllLessonsMalformedFilter(String param, String value) throws Exception {
		mockMvc.perform(get("/api/v1/lessons").param(param, value))
				.andExpect(status().isBadRequest());
	}

	@DisplayName("GET /v1/lessons - Higher minimum than maximum price is Bad Request")
	@Test
	public void getAllLessonsPriceConstraint() throws Exception {
		mockMvc.perform(get("/api/v1/lessons").param("minPrice", "20.0")
						.param("maxPrice", "10.0"))
				.andExpect(status().isBadRequest());
	}

	@DisplayName("POST /v1/lessons")
	@Test
	public void createLesson() throws Exception {
		LessonCreateRequest lessonCreateRequest = LessonCreateRequestFactory.VALID_LESSON_CREATE_REQUEST;
		Lesson lesson = LessonFactory.from(lessonCreateRequest);

		when(lessonService.create(anyLong(),
				eq(lessonCreateRequest))).thenReturn(LessonFactory.from(lessonCreateRequest));

		mockMvc.perform(post("/api/v1/lessons").contentType(MediaType.APPLICATION_JSON)
						.requestAttr("session", SessionFactory.VALID_USER_PUBLIC_RESPONSE)
						.content(objectMapper.writeValueAsString(lessonCreateRequest)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location",
						"/api/v1/lessons/%s".formatted(lesson.getId())));
	}

	@DisplayName("POST /v1/lessons - invalid data is Bad Request")
	@ParameterizedTest
	@MethodSource("com.demo.language_booking.lessons.LessonCreateRequestFactory#getInvalidTestCases")
	public void createLessonWithInvalidData(InvalidFieldCase invalidFieldCase) throws Exception {
		LessonCreateRequest lessonCreateRequest = LessonCreateRequestFactory.validCreateLessonRequestBuilder()
				.build();
		Field field = LessonCreateRequest.class.getDeclaredField(invalidFieldCase.fieldName());
		field.setAccessible(true);
		field.set(lessonCreateRequest, invalidFieldCase.invalidValue());

		mockMvc.perform(post("/api/v1/lessons").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(lessonCreateRequest)))
				.andExpect(status().isBadRequest());
	}

	@DisplayName("DELETE /v1/lessons")
	@Test
	public void deleteLesson() throws Exception {
		Long id = 1L;

		mockMvc.perform(delete("/api/v1/lessons/%s".formatted(id)))
				.andExpect(status().isNoContent());
	}

	@DisplayName("DELETE /v1/lessons/ - Non-existent lesson is Not Found")
	@Test
	public void deleteNonExistentLesson() throws Exception {
		long id = 1;

		doThrow(ResourceNotFoundException.class).when(lessonService)
				.delete(id);

		mockMvc.perform(delete("/api/v1/lessons/%s".formatted(id)))
				.andExpect(status().isNotFound());
	}

	@DisplayName("PUT /v1/lessons/{id}")
	@Test
	public void updateLesson() throws Exception {
		LessonUpdateRequest lessonUpdateRequest = LessonUpdateRequestFactory.VALID_UPDATE_LESSON;
		Lesson lesson = LessonFactory.VALID_LESSON;
		long id = lesson.getId();

		when(lessonService.update(id, lessonUpdateRequest)).thenReturn(lesson);
		when(lessonMapper.toLessonResponse(lesson)).thenReturn(LessonResponseFactory.VALID_LESSON_RESPONSE);

		mockMvc.perform(put("/api/v1/lessons/%s".formatted(id))
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(lessonUpdateRequest)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(objectMapper.writeValueAsString(LessonResponseFactory.VALID_LESSON_RESPONSE)));
	}

	@DisplayName("PUT /v1/lessons/{id} - Invalid data is Bad Request")
	@ParameterizedTest
	@MethodSource("com.demo.language_booking.lessons.LessonUpdateRequestFactory#getInvalidTestCases")
	public void updateLessonWithInvalidData(InvalidFieldCase invalidFieldCase) throws Exception {
		LessonUpdateRequest lessonUpdateRequest = LessonUpdateRequestFactory.validUpdateLessonRequestBuilder()
				.build();
		Lesson lesson = LessonFactory.VALID_LESSON;
		Long id = lesson.getId();

		Field field = LessonUpdateRequest.class.getDeclaredField(invalidFieldCase.fieldName());
		field.setAccessible(true);
		field.set(lessonUpdateRequest, invalidFieldCase.invalidValue());

		mockMvc.perform(put("/api/v1/lessons/%s".formatted(id))
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(lessonUpdateRequest)))
				.andExpect(status().isBadRequest());
	}
}
