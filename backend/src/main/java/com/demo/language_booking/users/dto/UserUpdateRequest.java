package com.demo.language_booking.users.dto;

import com.demo.language_booking.common.Country;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a request data object for updating user information.
 */
@Builder
@Data
public class UserUpdateRequest {
	@Size(min = 5, max = 32, message = "Username must be between 5 and 32 characters long")
	@NotBlank(message = "Username cannot be empty")
	private String username;
	@Size(min = 5, max = 64, message = "Email must be at most 64 characters long")
	@Email(message = "Invalid email address")
	private String email;
	@Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters long")
	@NotBlank(message = "Password cannot be empty")
	private String password;
	@NotNull(message = "Country code cannot be null")
	private Country countryCode;
}
