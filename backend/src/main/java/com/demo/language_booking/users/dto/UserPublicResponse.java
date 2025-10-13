package com.demo.language_booking.users.dto;

import com.demo.language_booking.common.Country;
import com.demo.language_booking.users.User.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * Represents a public response object for user information.
 * This class contains essential user details that can be safely exposed to external systems
 * or clients without revealing sensitive information.
 */
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
