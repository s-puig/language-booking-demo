package com.demo.language_booking.lessons;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "api/v1/lessons/", produces = MediaType.APPLICATION_JSON_VALUE)
public class LessonController {
    private final LessonService lessonService;
}
