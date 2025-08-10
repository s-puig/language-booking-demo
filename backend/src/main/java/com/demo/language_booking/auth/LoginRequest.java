package com.demo.language_booking.auth;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Builder
@Data
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
