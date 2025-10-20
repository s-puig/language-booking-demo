package com.demo.language_booking.lessons;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {
	@Query(value = "SELECT * FROM lessons WHERE lesson_id = :id", nativeQuery = true)
	Optional<Lesson> findByIdIncludeDeleted(@Param("id") Long id);
}
