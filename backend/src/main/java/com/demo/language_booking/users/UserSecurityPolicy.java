package com.demo.language_booking.users;

import com.demo.language_booking.auth.authorization.IAuthPolicyHandler;
import org.springframework.stereotype.Component;

@Component
public class UserSecurityPolicy implements IAuthPolicyHandler {
    @Override
    public long getResourceOwner(long resourceId) {
        return resourceId;
    }
}
