package com.demo.language_booking.users;

import com.demo.language_booking.users.dto.UserCreateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @NotNull
    public List<User> getAll() {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public User create(@Valid UserCreateRequest userCreateRequest) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public Optional<User> getById(Long id) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public User update(long id, @Valid UserCreateRequest user) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public void delete(long id) {
        throw new UnsupportedOperationException("Not Implemented");
    }
}
