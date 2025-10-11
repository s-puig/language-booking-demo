package com.demo.language_booking.schedule.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 * Data transfer object representing a creation or update request to schedule availability.
 */
@Data
@Builder
public class ScheduleRequest {
    /**
     * Represents the timezone identifier for the schedule.
     */
    String timezone;
    /**
     * Two-dimensional array representing the availability schedule.
     * The boolean array must contain a matrix of exactly 7(days)x24(hours).
     */
    @Size(min= 7, max = 7, message = "availableTime must be exactly 21 bytes")
    boolean[][] availableTime;
}
