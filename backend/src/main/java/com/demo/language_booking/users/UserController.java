package com.demo.language_booking.users;

import com.demo.language_booking.auth.authorization.AuthPolicy;
import com.demo.language_booking.auth.authorization.Authorize;
import com.demo.language_booking.common.Language;
import com.demo.language_booking.common.exceptions.ResourceNotFoundException;
import com.demo.language_booking.users.dto.UserLanguageDto;
import com.demo.language_booking.users.dto.UserCreateRequest;
import com.demo.language_booking.users.dto.UserPublicResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@AuthPolicy(UserSecurityPolicy.class)
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper = UserMapper.USER_MAPPER;

    @Authorize(User.Role.ADMIN)
    @GetMapping
    public ResponseEntity<List<UserPublicResponse>> getAll() {
        List<User> users = userService.getAll();
        List<UserPublicResponse> responses = users.stream()
                .map(userMapper::mapToUserPublicResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<UserPublicResponse> create(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        User savedUser = userService.create(userCreateRequest);
        UserPublicResponse response = userMapper.mapToUserPublicResponse(savedUser);
        return ResponseEntity.created(
                java.net.URI.create("/api/v1/users/" + savedUser.getId())
        ).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPublicResponse> getById(@PathVariable Long id) {
        User user = userService.getById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        UserPublicResponse response = userMapper.mapToUserPublicResponse(user);
        return ResponseEntity.ok(response);
    }

    @Authorize(User.Role.ADMIN)
    @Authorize(value = User.Role.STUDENT, requireOwnership = true)
    @PutMapping("/{id}")
    public ResponseEntity<UserPublicResponse> update(@PathVariable Long id, @RequestBody @Valid UserCreateRequest userCreateRequest) {
        User updatedUser = userService.update(id, userCreateRequest);
        return ResponseEntity.ok(userMapper.mapToUserPublicResponse(updatedUser));
    }

    @Authorize(User.Role.ADMIN)
    @Authorize(value = User.Role.STUDENT, requireOwnership = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Authorize(User.Role.ADMIN)
    @Authorize(value = User.Role.STUDENT, requireOwnership = true)
    @PostMapping("/{id}/lang")
    public ResponseEntity<UserPublicResponse> addLanguageToUser(@PathVariable Long id, @RequestBody @Valid UserLanguageDto languageDto) {
        User updatedUser = userService.addLanguage(id, languageDto.getLanguage(), languageDto.getLevel());
        return ResponseEntity.created(java.net.URI.create("/api/v1/users/%s/lang/%s".formatted(id, languageDto.getLanguage().getCode()))).body(userMapper.mapToUserPublicResponse(updatedUser));
    }

    @Authorize(User.Role.ADMIN)
    @Authorize(value = User.Role.STUDENT, requireOwnership = true)
    @DeleteMapping("/{id}/lang/{lang}")
    public ResponseEntity<?> deleteLanguageToUser(@PathVariable Long id, @PathVariable @NotNull Language lang) {
        userService.removeLanguage(id, lang);
        return ResponseEntity.noContent().build();
    }

    @Authorize(User.Role.ADMIN)
    @Authorize(value = User.Role.STUDENT, requireOwnership = true)
    @PutMapping("/{id}/lang")
    public ResponseEntity<UserPublicResponse> updateLanguageToUser(@PathVariable Long id, @RequestBody @Valid UserLanguageDto languageDto) {
        User updatedUser = userService.updateLanguage(id, languageDto.getLanguage(), languageDto.getLevel());
        return ResponseEntity.ok(userMapper.mapToUserPublicResponse(updatedUser));
    }
}
