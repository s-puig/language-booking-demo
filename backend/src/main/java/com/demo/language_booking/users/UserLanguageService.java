package com.demo.language_booking.users;

import com.demo.language_booking.common.CEFRLevel;
import com.demo.language_booking.common.Language;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

public interface UserLanguageService {
    /**
     * Adds a language with its corresponding CEFR level to the user.
     *
     * @param userId the unique identifier of the user to whom the language will be added
     * @param language the language to be added to the user
     * @param cefrLevel the CEFR level associated with the language for the user
     * @return the updated user entity with the new language added
     */
    @NonNull
    User addLanguage(long userId, @NotNull Language language, @NotNull CEFRLevel cefrLevel);

    /**
     * Updates the language and CEFR level for a specific user.
     *
     * @param userId the unique identifier of the user whose language will be updated
     * @param language the language to be updated for the user
     * @param cefrLevel the CEFR level associated with the language for the user
     * @return the updated user entity with the language and level modified
     */
    @NonNull
    User updateLanguage(long userId, @NotNull Language language, @NotNull CEFRLevel cefrLevel);

    /**
     * Removes a language entry from the specified user's profile.
     *
     * @param userId the unique identifier of the user from whom the language will be removed
     * @param language the language to be deleted from the user's profile
     */
    void deleteLanguage(long userId, @NotNull Language language);
}
