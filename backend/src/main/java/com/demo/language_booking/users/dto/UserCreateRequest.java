package com.demo.language_booking.users.dto;


import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
@Builder
public class UserCreateRequest {
    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @Email(message = "Invalid email address")
    private String email;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
