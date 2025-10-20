package com.demo.language_booking.lessons;

import com.demo.language_booking.InvalidFieldCase;
import com.demo.language_booking.TestType;
import com.demo.language_booking.common.Country;
import com.demo.language_booking.common.exceptions.ResourceNotFoundException;
import com.demo.language_booking.lessons.dto.LessonCreateRequest;
import com.demo.language_booking.lessons.dto.LessonUpdateRequest;
import com.demo.language_booking.users.User;
import com.demo.language_booking.users.UserFactory;
import com.demo.language_booking.users.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag(TestType.INTEGRATION)
@Transactional
@SpringBootTest
public final class LessonServiceIntegrationTest {
	@Autowired
	private DefaultLessonService lessonService;
	@Autowired
	private LessonRepository lessonRepository;
	@Autowired
	private UserRepository userRepository;

	@DisplayName("Add lesson")
	@Test
	public void createLesson() {
		LessonCreateRequest lessonCreateRequest = LessonCreateRequestFactory.VALID_LESSON_CREATE_REQUEST;
		User user = userRepository.save(UserFactory.getValidUserBuilder()
				.build());

		Lesson lesson = lessonService.create(user.getId(), lessonCreateRequest);

		assertTrue(lessonRepository.existsById(lesson.getId()));
		assertSame(lesson.getTutor()
				.getId(), user.getId());
		assertSame(lessonCreateRequest.getPrice(), lesson.getPrice());
		assertEquals(lessonCreateRequest.getName(), lesson.getName());
		assertEquals(lessonCreateRequest.getDescription(), lesson.getDescription());
		assertEquals(lessonCreateRequest.getLessonCategories(), lesson.getLessonCategories());
	}

	@DisplayName("Add malformed lesson throws ConstraintViolationException")
	@ParameterizedTest
	@MethodSource("com.demo.language_booking.lessons.LessonCreateRequestFactory#getInvalidTestCases")
	public void createMalformedLesson(InvalidFieldCase invalidFieldCase) throws Exception {
		User user = UserFactory.getValidUserBuilder()
				.build();
		LessonCreateRequest lessonCreateRequest = LessonCreateRequestFactory.validCreateLessonRequestBuilder()
				.build();
		Field field = LessonCreateRequest.class.getDeclaredField(invalidFieldCase.fieldName());
		field.setAccessible(true);
		field.set(lessonCreateRequest, invalidFieldCase.invalidValue());

		assertThrows(ConstraintViolationException.class,
				() -> lessonService.create(userRepository.save(user)
						.getId(), lessonCreateRequest));
	}

	@DisplayName("Add lesson with missing User throws ResourceNotFoundException")
	@Test
	public void createLessonWithMissingUser() {
		LessonCreateRequest lessonCreateRequest = LessonCreateRequestFactory.VALID_LESSON_CREATE_REQUEST;

		assertThrows(Exception.class, () -> lessonService.create(1L, lessonCreateRequest));
	}

	@DisplayName("Add lesson with invalid User throws ResourceNotFoundException")
	@Test
	public void createLessonWithInvalidUser() {
		LessonCreateRequest lessonCreateRequest = LessonCreateRequestFactory.validCreateLessonRequestBuilder()
				.build();

		assertThrows(ResourceNotFoundException.class, () -> lessonService.create(1L, lessonCreateRequest));
	}

	@DisplayName("Get lesson by id")
	@Test
	public void getLessonById() {
		LessonCreateRequest lessonCreateRequest = LessonCreateRequestFactory.validCreateLessonRequestBuilder()
				.build();
		User user = userRepository.save(UserFactory.getValidUserBuilder()
				.build());
		Lesson lesson = LessonFactory.from(lessonCreateRequest, user);
		lesson.setId(null);
		lesson = lessonRepository.save(lesson);
		Lesson fetchedLesson = lessonService.findById(lesson.getId())
				.orElseThrow();

		assertThat(fetchedLesson).usingRecursiveComparison()
				.ignoringFields("tutor")
				.isEqualTo(lesson);

	}

	@DisplayName("Get lesson by id - Deleted")
	@Test
	public void getDeletedLessonById() {
		LessonCreateRequest lessonCreateRequest = LessonCreateRequestFactory.validCreateLessonRequestBuilder()
				.build();
		User user = userRepository.save(UserFactory.getValidUserBuilder()
				.build());
		Lesson lesson = LessonFactory.from(lessonCreateRequest, user);
		lesson.setId(null);
		lesson = lessonRepository.save(lesson);
		assertTrue(lessonRepository.existsById(lesson.getId()));
		lessonRepository.delete(lesson);
		lesson = lessonRepository.findByIdIncludeDeleted(lesson.getId())
				.orElseThrow();

		assertNotNull(lesson);
		assertNotNull(lesson.getDeletedAt());
	}

	@DisplayName("Get lesson by id - Non-existent lesson")
	@Test
	public void getNonExistentLessonById() {
		assertTrue(lessonService.findById(Long.MAX_VALUE)
				.isEmpty());
	}

	@DisplayName("Get all lessons")
	@Test
	public void getAllLessons() {
		LessonCreateRequest lessonCreateRequest = LessonCreateRequestFactory.validCreateLessonRequestBuilder()
				.build();
		User user = userRepository.saveAndFlush(UserFactory.getValidUserBuilder()
				.build());
		Lesson lesson = LessonFactory.from(lessonCreateRequest, user);
		lesson.setId(null);
		lesson = lessonRepository.saveAndFlush(lesson);

		lessonCreateRequest.setName("Second lesson");
		Lesson secondLesson = LessonFactory.from(lessonCreateRequest, user);
		secondLesson.setId(null);
		secondLesson = lessonRepository.saveAndFlush(secondLesson);

		lessonCreateRequest.setName("Third lesson");
		Lesson thirdLesson = LessonFactory.from(lessonCreateRequest, user);
		thirdLesson.setId(null);
		thirdLesson = lessonRepository.saveAndFlush(thirdLesson);

		assertEquals(3, lessonRepository.findAll()
				.size());
		lessonRepository.deleteById(thirdLesson.getId());

		assertEquals(2, lessonRepository.findAll()
				.size());

		assertEquals(2, lessonService.findAll()
				.size());
	}

	private void prepareLesson(LessonCreateRequest lessonCreateRequest, User user) {
		Lesson lesson = LessonFactory.from(lessonCreateRequest, user);
		lesson.setId(null);
		lessonRepository.saveAndFlush(lesson);
	}

	@DisplayName("Get all lessons - Filtered")
	@ParameterizedTest
	@MethodSource("com.demo.language_booking.lessons.LessonFilterFactory#lessonFilterProvider")
	public void getAllLessonsFiltered(LessonFilter filter, Set<String> validLessonNames) {
		User user = userRepository.save(User.builder()
				.email("test@test.com")
				.password("12345679")
				.username("firstUser")
				.role(
						User.Role.TEACHER)
				.countryCode(Country.ES)
				.build());

		prepareLesson(LessonCreateRequest.builder()
				.name("Vocabulary")
				.lessonCategories(Set.of(LessonCategory.VOCABULARY))
				.price(BigDecimal.valueOf(9.5))
				.build(), user);
		prepareLesson(LessonCreateRequestFactory.validCreateLessonRequestBuilder()
				.name("Listening")
				.price(BigDecimal.valueOf(18.0))
				.lessonCategories(Set.of(LessonCategory.LISTENING))
				.build(), user);

		// FIXME: This is a hack to make the test pass, because the ID's don't reset due to a bug in User tests.
		if (filter.getTutorId() != null) filter.setTutorId(user.getId());

		user = userRepository.save(User.builder()
				.email("second@test.com")
				.username("secondUser")
				.password("12345648789")
				.role(
						User.Role.TEACHER)
				.countryCode(
						Country.DE)
				.build());

		prepareLesson(LessonCreateRequestFactory.validCreateLessonRequestBuilder()
				.name("German")
				.price(BigDecimal.valueOf(18.5))
				.lessonCategories(Set.of(LessonCategory.TRANSLATION))
				.build(), user);
		prepareLesson(LessonCreateRequestFactory.validCreateLessonRequestBuilder()
				.name("Interview Prep")
				.price(BigDecimal.valueOf(30.0))
				.lessonCategories(Set.of(LessonCategory.INTERVIEW_PREPARATION))
				.build(), user);


		List<Lesson> filteredLessons = lessonService.findAll(filter);
		assertEquals(validLessonNames.size(), filteredLessons.size());
		assertTrue(filteredLessons.stream()
				.allMatch(lesson -> validLessonNames.contains(lesson.getName())));
	}

	@DisplayName("Delete lesson")
	@Test
	public void deleteLesson() {
		LessonCreateRequest lessonCreateRequest = LessonCreateRequestFactory.validCreateLessonRequestBuilder()
				.build();
		User user = userRepository.save(UserFactory.getValidUserBuilder()
				.build());
		Lesson lesson = LessonFactory.from(lessonCreateRequest, user);
		lesson.setId(null);
		lesson = lessonRepository.save(lesson);

		assertNull(lesson.getDeletedAt());

		lessonService.delete(lesson.getId());

		assertTrue(lessonRepository.findById(lesson.getId())
				.isEmpty());
		Lesson fetchedLesson = lessonRepository.findByIdIncludeDeleted(lesson.getId())
				.orElseThrow();

		assertNotNull(fetchedLesson.getDeletedAt());
	}

	@DisplayName("Delete lesson - Non-existent lesson")
	@Test
	public void deleteNonExistentLesson() {
		assertThrows(ResourceNotFoundException.class, () -> lessonService.delete(Long.MAX_VALUE));
	}

	@DisplayName("Update lesson")
	@Test
	public void updateLesson() {
		LessonUpdateRequest updateRequest = LessonUpdateRequestFactory.validUpdateLessonRequestBuilder()
				.price(
						BigDecimal.valueOf(25.0))
				.name("EXTRA LESSONS")
				.description("Now way pricier!")
				.lessonCategories(Set.of(LessonCategory.LISTENING, LessonCategory.TRANSLATION))
				.build();
		LessonCreateRequest lessonCreateRequest = LessonCreateRequestFactory.validCreateLessonRequestBuilder()
				.build();
		User user = userRepository.save(UserFactory.getValidUserBuilder()
				.build());
		Lesson lesson = LessonFactory.from(lessonCreateRequest, user);
		lesson.setId(null);
		lessonRepository.saveAndFlush(lesson);

		lesson = lessonService.update(lesson.getId(), updateRequest);

		assertEquals(lesson.getTutor()
				.getId(), user.getId());
		assertEquals(lesson.getPrice(), updateRequest.getPrice());
		assertEquals(lesson.getLessonCategories(), updateRequest.getLessonCategories());
		assertEquals(lesson.getName(), updateRequest.getName());
		assertEquals(lesson.getDescription(), updateRequest.getDescription());
	}

	@DisplayName("Update lesson - Malformed")
	@ParameterizedTest
	@MethodSource("com.demo.language_booking.lessons.LessonUpdateRequestFactory#getInvalidTestCases")
	public void updateMalformedLesson(InvalidFieldCase invalidFieldCase) throws Exception {
		LessonUpdateRequest updateRequest = LessonUpdateRequestFactory.validUpdateLessonRequestBuilder()
				.build();
		Field field = LessonUpdateRequest.class.getDeclaredField(invalidFieldCase.fieldName());
		field.setAccessible(true);
		field.set(updateRequest, invalidFieldCase.invalidValue());
		LessonCreateRequest lessonCreateRequest = LessonCreateRequestFactory.validCreateLessonRequestBuilder()
				.build();
		User user = userRepository.save(UserFactory.getValidUserBuilder()
				.build());
		Lesson lesson = LessonFactory.from(lessonCreateRequest, user);
		lesson.setId(null);
		lessonRepository.saveAndFlush(lesson);

		assertThrows(ConstraintViolationException.class, () -> lessonService.update(lesson.getId(), updateRequest));
	}

	@DisplayName("Update lesson - Non-existent lesson throws ResourceNotFound")
	@Test
	public void updateNonExistentLesson() {
		assertThrows(ResourceNotFoundException.class,
				() -> lessonService.update(Long.MAX_VALUE,
						LessonUpdateRequestFactory.validUpdateLessonRequestBuilder()
								.build()));
	}

	@DisplayName("Update lesson - Deleted lesson throws ResourceNotFound")
	@Test
	public void updateDeletedLesson() {
		LessonCreateRequest lessonCreateRequest = LessonCreateRequestFactory.validCreateLessonRequestBuilder()
				.build();
		User user = userRepository.save(UserFactory.getValidUserBuilder()
				.build());
		Lesson lesson = LessonFactory.from(lessonCreateRequest, user);
		lesson.setId(null);
		lesson = lessonRepository.save(lesson);

		assertNull(lesson.getDeletedAt());

		lessonRepository.deleteById(lesson.getId());

		assertThrows(ResourceNotFoundException.class,
				() -> lessonService.update(Long.MAX_VALUE,
						LessonUpdateRequestFactory.validUpdateLessonRequestBuilder()
								.build()));
	}
}
