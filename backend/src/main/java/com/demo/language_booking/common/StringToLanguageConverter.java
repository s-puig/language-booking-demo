package com.demo.language_booking.common;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class StringToLanguageConverter implements Converter<String, Language> {
    @Override
    @Nullable
    public Language convert(@NonNull String source) {
        return Language.fromCode(source);
    }
}
