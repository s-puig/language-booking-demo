package com.demo.language_booking.users.dto;

import java.time.LocalDateTime;

import com.demo.language_booking.users.User.Role;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserPublicResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
