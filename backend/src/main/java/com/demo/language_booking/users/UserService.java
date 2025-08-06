package com.demo.language_booking.users;

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

@Service
@Validated
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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
}
