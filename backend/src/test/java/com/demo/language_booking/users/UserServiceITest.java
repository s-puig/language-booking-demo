package com.demo.language_booking.users;

import com.demo.language_booking.common.CEFRLevel;
import com.demo.language_booking.common.Language;
import com.demo.language_booking.common.exceptions.DuplicateLanguageException;
import com.demo.language_booking.common.Country;
import com.demo.language_booking.common.exceptions.ResourceNotFoundException;

// Additional imports if needed
import com.demo.language_booking.users.dto.UserCreateRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Integration")
@SpringBootTest
public class UserServiceITest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    private UserCreateRequest.UserCreateRequestBuilder defaultCreateUserRequestBuilder() {
        return UserCreateRequest.builder()
                .username("defaultUser")
                .email("default@example.com")
                .password("defaultPassword")
                .countryCode(Country.DE);
    }

    @DisplayName("Get all users returns empty list if no users are present")
    @Test
    public void testGetAll_emptyList() {
        List<User> result = userService.getAll();
        assertTrue(result.isEmpty());
    }

    @DisplayName("Get all users")
    @Test
    public void testGetAll() {
        // Create some users before checking if getAll returns any
        UserCreateRequest request1 = defaultCreateUserRequestBuilder()
                .build();
        userRepository.save(userMapper.mapToUser(request1));

        UserCreateRequest request2 = defaultCreateUserRequestBuilder()
                .username("testUser2")
                .email("test2@example.com")
                .build();
        userRepository.save(userMapper.mapToUser(request2));

        List<User> result = userService.getAll();
        assertFalse(result.isEmpty());
    }

    @DisplayName("Create a user")
    @Test
    public void testCreateUser() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .build();

        User createdUser = userService.create(request);
        assertNotNull(createdUser.getId());
    }

    @DisplayName("Create user with duplicate username fails")
    @Test
    public void testCreateUser_duplicateUsername() {
        UserCreateRequest request1 = defaultCreateUserRequestBuilder()
                .build();
        userService.create(request1);

        UserCreateRequest request2 = defaultCreateUserRequestBuilder()
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> userService.create(request2));
    }

    @DisplayName("Create user with duplicate email fails")
    @Test
    public void testCreateUser_duplicateEmail() {
        UserCreateRequest request1 = defaultCreateUserRequestBuilder()
                .build();
        userService.create(request1);

        UserCreateRequest request2 = defaultCreateUserRequestBuilder()
                .email("default@example.com")
                .username("anotherUser")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> userService.create(request2));
    }

    
    @DisplayName("Create user with invalid username fails validation")
    @Test
    public void testCreateUser_invalidUsername() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .username("")
                .build();

        assertThrows(ConstraintViolationException.class, () -> userService.create(request));
    }

    @DisplayName("Create user with invalid email fails validation")
    @Test
    public void testCreateUser_invalidEmail() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .email("invalid-email")
                .build();

        assertThrows(ConstraintViolationException.class, () -> userService.create(request));
    }

    @DisplayName("Create user with invalid password fails validation")
    @Test
    public void testCreateUser_invalidPassword() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .password("12345")
                .build();

        assertThrows(ConstraintViolationException.class, () -> userService.create(request));
    }

    @DisplayName("Get a user by ID")
    @Test
    public void testGetById() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .build();

        User createdUser = userRepository.save(userMapper.mapToUser(request));
        Long id = createdUser.getId();
        Optional<User> result = userService.getById(id);
        assertTrue(result.isPresent());
    }

    @DisplayName("Get a user by ID with non-existent ID returns an empty optional")
    @Test
    public void testGetById_nonExistentId() {
        Optional<User> result = userService.getById(Long.MAX_VALUE);
        assertFalse(result.isPresent());
    }

    @DisplayName("Update a user")
    @Test
    public void testUpdateUser() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .build();

        User createdUser = userRepository.save(userMapper.mapToUser(request));
        Long id = createdUser.getId();
        UserCreateRequest updateRequest = defaultCreateUserRequestBuilder()
                .username("updatedUsername")
                .email("updated@example.com")
                .password("newPassword")
                .build();

        User updatedUser = userService.update(id, updateRequest);
        assertNotNull(updatedUser);
        assertEquals("updatedUsername", updatedUser.getUsername());
        assertEquals("updated@example.com", updatedUser.getEmail());
    }

    @DisplayName("Update a user with non-existent ID fails")
    @Test
    public void testUpdateUser_nonExistentId() {
        UserCreateRequest updateRequest = defaultCreateUserRequestBuilder()
                .username("updatedUsername")
                .email("updated@example.com")
                .password("newPassword")
                .build();

        assertThrows(ResourceNotFoundException.class, () -> userService.update(Long.MAX_VALUE, updateRequest));
    }

    @DisplayName("Update a user with existing username fails")
    @Test
    public void testUpdateUser_usernameExists() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .build();
        userRepository.save(userMapper.mapToUser(request));

        UserCreateRequest request2 = defaultCreateUserRequestBuilder()
                .username("existingUsername")
                .email("newEmail@example.com")
                .password("newPassword")
                .build();
        User createdUser2 = userRepository.save(userMapper.mapToUser(request2));
        Long id = createdUser2.getId();

        UserCreateRequest updateRequest = defaultCreateUserRequestBuilder()
                .email("updated@example.com")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> userService.update(id, updateRequest));
    }

    @DisplayName("Update a user with existing email fails")
    @Test
    public void testUpdateUser_emailExists() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .build();
        userRepository.save(userMapper.mapToUser(request));

        UserCreateRequest request2 = defaultCreateUserRequestBuilder()
                .username("newUsername")
                .email("existingEmail@example.com")
                .password("newPassword")
                .build();
        User createdUser2 = userRepository.save(userMapper.mapToUser(request2));
        Long id = createdUser2.getId();

        UserCreateRequest updateRequest = defaultCreateUserRequestBuilder()
                .username("updatedUsername")
                .email("default@example.com") // Existing email
                .password("newPassword")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> userService.update(id, updateRequest));
    }

    @DisplayName("Update a user with invalid username fails validation")
    @Test
    public void testUpdateUser_invalidUsername() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .build();

        User createdUser = userRepository.save(userMapper.mapToUser(request));
        Long id = createdUser.getId();
        UserCreateRequest updateRequest = defaultCreateUserRequestBuilder()
                .username("")
                .email("updated@example.com")
                .password("newPassword")
                .build();

        assertThrows(ConstraintViolationException.class, () -> userService.update(id, updateRequest));
    }

    @DisplayName("Update a user with invalid email fails validation")
    @Test
    public void testUpdateUser_invalidEmail() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .build();

        User createdUser = userRepository.save(userMapper.mapToUser(request));
        Long id = createdUser.getId();
        UserCreateRequest updateRequest = defaultCreateUserRequestBuilder()
                .username("updatedUser")
                .email("invalid-email")
                .password("newPassword")
                .build();

        assertThrows(ConstraintViolationException.class, () -> userService.update(id, updateRequest));
    }

    @DisplayName("Update a user with invalid password fails validation")
    @Test
    public void testUpdateUser_invalidPassword() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .build();

        User createdUser = userRepository.save(userMapper.mapToUser(request));
        Long id = createdUser.getId();
        UserCreateRequest updateRequest = defaultCreateUserRequestBuilder()
                .username("updatedUser")
                .email("updated@example.com")
                .password("12345")
                .build();

        assertThrows(ConstraintViolationException.class, () -> userService.update(id, updateRequest));
    }

    @DisplayName("Update a non-existent user fails with an appropriate exception")
    @Test
    public void testUpdateNonExistentUser() {
        UserCreateRequest updateRequest = defaultCreateUserRequestBuilder()
                .username("updatedUser")
                .email("updated@example.com")
                .password("newPassword")
                .build();

        assertThrows(ResourceNotFoundException.class, () -> userService.update(Long.MAX_VALUE, updateRequest));
    }

    @DisplayName("Delete a user")
    @Test
    public void testDeleteUser() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .build();

        User createdUser = userRepository.save(userMapper.mapToUser(request));
        Long id = createdUser.getId();
        userService.delete(id);

        Optional<User> result = userRepository.findById(id);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getDeletedAt());
    }

    @DisplayName("Delete a user with non-existent ID fails with ResourceNotFoundException")
    @Test
    public void testDeleteUser_nonExistentId() {
        assertThrows(ResourceNotFoundException.class, () -> userService.delete(Long.MAX_VALUE));
    }

    @DisplayName("Add a language to a user")
    @Test
    public void testAddLanguage() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .build();
        Language spokenLanguage = Language.fromCode("EN");
        CEFRLevel languageLevel = CEFRLevel.B2;
        User createdUser = userRepository.save(userMapper.mapToUser(request));
        Long userId = createdUser.getId();

        User user = userService.addLanguage(userId, spokenLanguage, languageLevel);
        assertFalse(user.getSpokenLanguages().isEmpty());

        UserLanguageLevel userLanguageLevel = user.getSpokenLanguages().stream().findFirst().get();
        assertNotNull(user);
        assertEquals(spokenLanguage, userLanguageLevel.getLanguage());
        assertEquals(languageLevel, userLanguageLevel.getLevel());
    }

    @DisplayName("Add a language to a non-existent user throws ResourceNotFoundException")
    @Test
    public void testAddLanguage_nonExistentUser() {
        assertThrows(ResourceNotFoundException.class, () -> userService.addLanguage(Long.MAX_VALUE, Language.fromCode("EN"), CEFRLevel.valueOf("B2")));
    }

    @DisplayName("Add the same language twice to a user should fail")
    @Test
    public void testAddLanguageTwiceShouldThrow() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .build();
        Language spokenLanguage = Language.fromCode("EN");
        CEFRLevel languageLevel = CEFRLevel.B2;
        User createdUser = userRepository.save(userMapper.mapToUser(request));
        Long userId = createdUser.getId();

        User user = userService.addLanguage(userId, spokenLanguage, languageLevel);
        assertFalse(user.getSpokenLanguages().isEmpty());
        CEFRLevel otherLevel = CEFRLevel.A1;
        assertNotEquals(otherLevel, languageLevel);
        assertThrows(DuplicateLanguageException.class, () -> userService.addLanguage(userId, spokenLanguage, otherLevel));
    }

    @DisplayName("Remove a language from a user")
    @Test
    public void testRemoveLanguage() {
        UserCreateRequest request = defaultCreateUserRequestBuilder()
                .build();
        Language spokenLanguage = Language.fromCode("EN");
        CEFRLevel languageLevel = CEFRLevel.B2;
        User createdUser = userRepository.save(userMapper.mapToUser(request));
        Long userId = createdUser.getId();

        User user = userService.addLanguage(userId, spokenLanguage, languageLevel);
        assertFalse(user.getSpokenLanguages().isEmpty());

        userService.removeLanguage(userId, spokenLanguage);

        Optional<UserLanguageLevel> result = userRepository.findById(userId).orElseThrow().getSpokenLanguages()
                .stream()
                .filter(lang -> lang.getLanguage() == spokenLanguage)
                .findFirst();
        assertFalse(result.isPresent());
    }

    @DisplayName("Remove a language from a non-existent user throws ResourceNotFoundException")
    @Test
    public void testRemoveLanguage_nonExistentUser() {
        assertThrows(ResourceNotFoundException.class, () -> userService.removeLanguage(Long.MAX_VALUE, Language.fromCode("EN")));
    }
}
