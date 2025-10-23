package com.demo.language_booking.lessons;

import com.demo.language_booking.lessons.dto.LessonCreateRequest;
import com.demo.language_booking.lessons.dto.LessonUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing lessons.
 * Provides operations for creating, reading, updating, and deleting lesson entities.
 */
public interface LessonService {
	/**
	 * Finds a lesson by its ID.
	 *
	 * @param id the ID of the lesson to find
	 * @return an Optional containing the lesson if found, or empty if not found
	 */
	Optional<Lesson> findById(long id);

	/**
	 * Retrieves all lessons.
	 *
	 * @return a list of all lessons
	 */
	@NonNull
	List<Lesson> findAll();

	/**
	 * Retrieves all lessons matching the given filter criteria.
	 *
	 * @param filter the filter criteria to apply
	 * @return a list of lessons matching the filter criteria
	 */
	@NonNull
	List<Lesson> findAll(@Valid @NotNull LessonFilter filter);

	/**
	 * Creates a new lesson.
	 *
	 * @param userId              the ID of the user creating the lesson
	 * @param lessonCreateRequest the details for creating the lesson
	 * @return the created lesson
	 */
	@NonNull
	Lesson create(long userId, @Valid @NotNull LessonCreateRequest lessonCreateRequest);

	/**
	 * Updates an existing lesson.
	 *
	 * @param id                  the ID of the lesson to update
	 * @param lessonUpdateRequest the details for updating the lesson
	 * @return the updated lesson
	 */
	@NonNull
	Lesson update(long id, @Valid @NotNull LessonUpdateRequest lessonUpdateRequest);

	/**
	 * Soft-deletes a lesson by its ID.
	 *
	 * @param id the ID of the lesson to delete
	 */
	void delete(long id);

	/**
	 * Saves a lesson.
	 *
	 * @param lesson the lesson to save
	 * @return the saved lesson
	 */
	Lesson save(@Valid @NotNull Lesson lesson);
}
