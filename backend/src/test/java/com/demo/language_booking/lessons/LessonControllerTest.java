package com.demo.language_booking.lessons;

import com.demo.language_booking.SecurityConfig;
import com.demo.language_booking.TestType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag(TestType.UNIT)
@Import(SecurityConfig.class)
@TestPropertySource(
        properties = "auth.filter.enabled=false"
)
@WebMvcTest(LessonController.class)
public class LessonControllerTest {
    @Autowired
    private LessonMapper lessonMapper;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private LessonService lessonService;

    @DisplayName("Get a lesson")
    @Test
    public void getLesson() throws Exception {
        Long id = 1L;
        mockMvc.perform(get("api/v1/lessons/".formatted(id)))
                .andExpect(status().isOk());
    }

    @DisplayName("Get a non-existent lesson throws a ResourceNotFoundException")
    @Test
    public void getNonExistentLesson() throws Exception {
        Long id = 1L;
        mockMvc.perform(get("/api/v1/lessons/".formatted(id)))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get all lessons")
    @Test
    public void getAllLessons() throws Exception {
        mockMvc.perform(get("/api/v1/lessons")).andExpect(status().isOk());
    }

    @DisplayName("Get all lessons with tag")
    @Test
    public void getAllLessonsTagged() throws Exception {
        mockMvc.perform(get("/api/v1/lessons?tag=tag")).andExpect(status().isOk());
    }

    @DisplayName("Get all lessons with tutor id")
    @Test
    public void getAllLessonsWithTutorId() throws Exception {
        Long id = 1L;
        mockMvc.perform(get("/api/v1/lessons?tutorId=".formatted(id))).andExpect(status().isOk());
    }

    @DisplayName("Get all lessons with malformed tag throws MalformedException")
    @Test
    public void getAllLessonsMalformedTag() throws Exception {
        mockMvc.perform(get("/api/v1/lessons?tag=test")).andExpect(status().isBadRequest());
    }

    @DisplayName("Get all lessons with non-existent tutor returns empty")
    @Test
    public void getAllLessonsWithNonExistentTutor() throws Exception {
        Long id = 1L;
        mockMvc.perform(get("/api/v1/lessons?tutorId=".formatted(id)))
                .andExpect(status().isOk());
    }

    @DisplayName("Create a new lesson")
    @Test
    public void createLesson() throws Exception {
        mockMvc.perform(post("/api/v1/lessons")).andExpect(status().isCreated());
    }

    @DisplayName("Create a new lesson with invalid data throws")
    @Test
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
        mockMvc.perform(delete("/api/v1/lessons/".formatted(id)))
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
