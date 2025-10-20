package com.demo.language_booking.users;

import com.demo.language_booking.common.CEFRLevel;
import com.demo.language_booking.common.Language;
import com.demo.language_booking.common.exceptions.DuplicateLanguageException;
import com.demo.language_booking.common.exceptions.ResourceNotFoundException;
import com.demo.language_booking.users.dto.UserCreateRequest;
import com.demo.language_booking.users.dto.UserUpdateRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Validated
@AllArgsConstructor
@Primary
@Service
public class DefaultUserService implements UserService, UserLanguageService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	@NonNull
	@Override
	public Optional<User> authenticate(@NotNull String username, @NotNull String unhashedPassword) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isEmpty()) return Optional.empty();
		if (!passwordEncoder.matches(unhashedPassword,
				user.get()
						.getPassword())) return Optional.empty();
		return user;
	}

	@NonNull
	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@NonNull
	@Transactional
	@Override
	public User create(@Valid @NotNull UserCreateRequest userCreateRequest) {
		User user = userMapper.mapToUser(userCreateRequest);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public Optional<User> findById(long id) {
		return userRepository.findById(id);
	}

	@NonNull
	@Transactional
	@Override
	public User update(long id, @Valid @NotNull UserUpdateRequest userUpdateRequest) {
		User existingUser = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

		existingUser.setUsername(userUpdateRequest.getUsername());
		existingUser.setEmail(userUpdateRequest.getEmail());
		existingUser.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));

		return userRepository.save(existingUser);
	}

	@Transactional
	@Override
	public void delete(long id) {
		User user = findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		user.setDeletedAt(LocalDateTime.now());
		userRepository.save(user);
	}

	@NonNull
	@Transactional
	@Override
	public User addLanguage(long id, @NotNull Language language, @NotNull CEFRLevel cefrLevel) {
		User user = findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

		// TODO: Remove this iteration instead of just finding the key
		if (user.getSpokenLanguages()
				.stream()
				.anyMatch((userLanguage) -> userLanguage.getLanguage() == language))
			throw new DuplicateLanguageException("User already has a language: " + language.getCode());

		UserLanguageLevel userLanguageLevel = new UserLanguageLevel();
		userLanguageLevel.setUser(user);
		userLanguageLevel.setLanguage(language);
		userLanguageLevel.setLevel(cefrLevel);
		user.getSpokenLanguages()
				.add(userLanguageLevel);
		return userRepository.save(user);
	}

	@Transactional
	@Override
	public void deleteLanguage(long id, @NotNull Language language) {
		User user = findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		Set<UserLanguageLevel> userLanguages = user.getSpokenLanguages();
		if (!userLanguages.removeIf(userLanguageLevel -> userLanguageLevel.getLanguage()
				.equals(language)))
			throw new ResourceNotFoundException("User with id '%s' does not have language '%s'".formatted(id,
					language.getCode()));
		userRepository.save(user);
	}

	@NonNull
	@Transactional
	@Override
	public User updateLanguage(long id, @NotNull Language language, @NotNull CEFRLevel cefrLevel) {
		User user = findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		UserLanguageLevel existingLanguage = user.getSpokenLanguages()
				.stream()
				.filter(userLanguageLevel -> userLanguageLevel.getLanguage() == language)
				.findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("User does not have language: " + language.getCode()));
		existingLanguage.setLevel(cefrLevel);
		return userRepository.save(user);
	}
}
