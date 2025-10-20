package com.demo.language_booking.users;

import com.demo.language_booking.common.Country;
import com.demo.language_booking.users.dto.UserCreateRequest;

public class UserCreateRequestFactory {
	public static final UserCreateRequest VALID_USER_CREATE_REQUEST = getValidUserCreateRequestBuilder().build();

	public static UserCreateRequest.UserCreateRequestBuilder getValidUserCreateRequestBuilder() {
		return UserCreateRequest.builder()
				.username("testUsername")
				.email("test@test.com")
				.password("testPassword")
				.countryCode(
						Country.ES);
	}
}
