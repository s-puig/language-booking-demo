package com.demo.language_booking.common;

import java.time.Instant;

public interface IScheduleAvailability {
    /**
     * @param id Id of the schedule or user.
     * @param startTime The UTC starting HOUR. Minutes will be ignored.
     * @return a boolean with the availability of the schedule.
     */
    boolean isTimeSlotAvailable(long id, Instant startTime);
}
