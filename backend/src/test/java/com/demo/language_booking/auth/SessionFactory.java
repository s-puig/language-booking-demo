package com.demo.language_booking.auth;

import com.demo.language_booking.common.Country;
import com.demo.language_booking.users.User;
import com.demo.language_booking.users.dto.UserPublicResponse;

import java.util.Set;

public class SessionFactory {
	// TODO: Create a valid JWT token for testing purposes instead of hardcoding this value
	public static final UserPublicResponse VALID_USER_PUBLIC_RESPONSE = UserPublicResponse.builder()
			.id(1L)
			.role(User.Role.TEACHER)
			.email("test@test.com")
			.country(
					Country.DE)
			.spokenLanguages(Set.of())
			.username("testdtest")
			.build();
}
