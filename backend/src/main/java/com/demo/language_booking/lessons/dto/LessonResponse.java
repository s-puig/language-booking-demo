package com.demo.language_booking.lessons.dto;

import com.demo.language_booking.lessons.LessonCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Builder
@Data
public class LessonResponse {
    public long id;
    public String name;
    public String description;
    public BigDecimal price;
    public Set<LessonCategory> lessonCategories;
}
