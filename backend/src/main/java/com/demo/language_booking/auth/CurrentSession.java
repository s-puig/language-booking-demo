package com.demo.language_booking.auth;

import com.demo.language_booking.users.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CurrentSession {
    long id;
    private String username;
    private String email;
    private User.Role role;
}
