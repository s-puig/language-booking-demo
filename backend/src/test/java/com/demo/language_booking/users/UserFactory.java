package com.demo.language_booking.users;

import com.demo.language_booking.common.Country;

public class UserFactory {
	public static final User VALID_USER = getValidUserBuilder().build();

	public static User.UserBuilder getValidUserBuilder() {
		return User.builder()
				.username("testUsername")
				.password("testPassword")
				.email("test@test.com")
				.countryCode(Country.ES)
				.role(User.Role.TEACHER);
	}
}
