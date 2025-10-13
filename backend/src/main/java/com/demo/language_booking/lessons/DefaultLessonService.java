package com.demo.language_booking.lessons;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DefaultLessonService implements LessonService{
    private final LessonRepository lessonRepository;
}
