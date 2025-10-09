package com.demo.language_booking.users.dto;

import com.demo.language_booking.common.Country;
import com.demo.language_booking.users.User.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class UserPublicResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private Country country;
    private Set<UserLanguageDto> spokenLanguages;
}
