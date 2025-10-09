package com.demo.language_booking.users;

import com.demo.language_booking.common.CEFRLevel;
import com.demo.language_booking.common.Language;
import com.demo.language_booking.common.exceptions.DuplicateLanguageException;
import com.demo.language_booking.common.exceptions.ResourceNotFoundException;
import com.demo.language_booking.users.dto.UserCreateRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Validated
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @NotNull
    public Optional<User> authenticate(String username, String unhashedPassword) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) return Optional.empty();
        if (!passwordEncoder.matches(unhashedPassword, user.get().getPassword())) return Optional.empty();
        return user;
    }

    @NotNull
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @NotNull
    @Transactional
    public User create(@Valid UserCreateRequest userCreateRequest) {
        User user = userMapper.mapToUser(userCreateRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> getById(long id) {
        return userRepository.findById(id);
    }

    @NotNull
    @Transactional
    public User update(long id, @Valid UserCreateRequest user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(existingUser);
    }

    @Transactional
    public void delete(long id) {
        User user = getById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @NotNull
    @Transactional
    public User addLanguage(long id, @NotNull Language language, @NotNull CEFRLevel level) {
        User user = getById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // TODO: Remove this iteration instead of just finding the key
        if (user.getSpokenLanguages().stream().anyMatch((userLanguage) -> userLanguage.getLanguage() == language))
            throw new DuplicateLanguageException("User already has a language: " + language.getCode());

        UserLanguageLevel userLanguageLevel = new UserLanguageLevel();
        userLanguageLevel.setUser(user);
        userLanguageLevel.setLanguage(language);
        userLanguageLevel.setLevel(level);
        user.getSpokenLanguages().add(userLanguageLevel);
        return userRepository.save(user);
    }

    @Transactional
    public void removeLanguage(long id, @NotNull Language language) {
        User user = getById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        Set<UserLanguageLevel> userLanguages = user.getSpokenLanguages();
        if (!userLanguages.removeIf(userLanguageLevel -> userLanguageLevel.getLanguage().equals(language)))
            throw new ResourceNotFoundException("User with id '%s' does not have language '%s'".formatted(id, language.getCode()));
        userRepository.save(user);
    }

    @NotNull
    @Transactional
    public User updateLanguage(long id, @NotNull Language language, @NotNull CEFRLevel level) {
        User user = getById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        UserLanguageLevel existingLanguage = user.getSpokenLanguages().stream()
                .filter(userLanguageLevel -> userLanguageLevel.getLanguage() == language)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User does not have language: " + language.getCode()));
        existingLanguage.setLevel(level);
        return userRepository.save(user);
    }
}
