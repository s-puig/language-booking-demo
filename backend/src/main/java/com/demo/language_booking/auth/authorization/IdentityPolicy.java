package com.demo.language_booking.auth.authorization;

import org.springframework.stereotype.Component;

@Component
public class IdentityPolicy implements IAuthPolicyHandler {
	@Override
	public long getResourceOwner(long resourceId) {
		return resourceId;
	}
}
