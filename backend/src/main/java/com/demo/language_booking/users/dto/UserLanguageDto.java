package com.demo.language_booking.users.dto;

import com.demo.language_booking.common.CEFRLevel;
import com.demo.language_booking.common.Language;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLanguageDto {
    @NotNull
    private CEFRLevel level;
    @NotNull
    private Language language;
}
