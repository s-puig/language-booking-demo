package com.demo.language_booking.lessons;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {
	/**
	 * Finds a lesson by ID including deleted lessons.
	 *
	 * @param id the ID of the lesson to find
	 * @return the lesson if found, or empty Optional if not found
	 */
	@Query(value = "SELECT * FROM lessons WHERE lesson_id = :id", nativeQuery = true)
	Optional<Lesson> findByIdIncludeDeleted(@Param("id") Long id);
}
