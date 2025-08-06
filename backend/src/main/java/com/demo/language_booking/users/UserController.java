package com.demo.language_booking.users;

import com.demo.language_booking.common.exceptions.ResourceNotFoundException;
import com.demo.language_booking.users.dto.UserCreateRequest;
import com.demo.language_booking.users.dto.UserPublicResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper = UserMapper.USER_MAPPER;

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
                java.net.URI.create("/api/users/" + savedUser.getId())
        ).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPublicResponse> getById(@PathVariable Long id) {
        User user = userService.getById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        UserPublicResponse response = userMapper.mapToUserPublicResponse(user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserPublicResponse> update(@PathVariable Long id, @RequestBody @Valid UserCreateRequest userCreateRequest) {
        User updatedUser = userService.update(id, userCreateRequest);
        UserPublicResponse response = userMapper.mapToUserPublicResponse(updatedUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
