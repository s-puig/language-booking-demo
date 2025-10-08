package com.demo.language_booking.users.dto;

import com.demo.language_booking.common.CEFRLevel;
import com.demo.language_booking.common.Language;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LanguageCreateRequest {
    private CEFRLevel level;
    private Language language;
}
