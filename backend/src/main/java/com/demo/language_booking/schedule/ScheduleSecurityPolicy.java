package com.demo.language_booking.schedule;

import com.demo.language_booking.auth.authorization.IAuthPolicyHandler;
import org.springframework.stereotype.Component;

@Component
public class ScheduleSecurityPolicy implements IAuthPolicyHandler {

    @Override
    public long getResourceOwner(long resourceId) {
        throw new RuntimeException("Not implemented");
    }
}
