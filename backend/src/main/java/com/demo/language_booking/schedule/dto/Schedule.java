package com.demo.language_booking.schedule.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a schedule entity containing tutor availability information.
 * This class holds the identifier of a tutor, the timezone associated with their schedule,
 * and a two-dimensional boolean array indicating their available time slots in the week.
 */
@Data
@Builder
public class Schedule {
    private long tutor_id;
    private String timezone;
    private boolean[][] availableTime;
}
