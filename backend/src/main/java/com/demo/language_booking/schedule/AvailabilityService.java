package com.demo.language_booking.schedule;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public interface AvailabilityService {
    /**
     * @param id Id of the schedule or user.
     * @param startTime The UTC starting HOUR. Minutes will be ignored.
     * @return a boolean with the availability of the schedule.
     */
    boolean isTimeSlotAvailable(long id, @NotNull Instant startTime);
}
