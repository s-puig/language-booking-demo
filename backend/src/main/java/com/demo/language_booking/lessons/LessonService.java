package com.demo.language_booking.lessons;

import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface LessonService {
    Optional<Lesson> findById(Long id);

    @NonNull
    List<Lesson> findAll();

    @NonNull
    List<Lesson> findAll(@NotNull LessonFilter filter);
}
