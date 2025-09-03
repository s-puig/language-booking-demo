package com.demo.language_booking.schedule.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.ZoneId;

@Data
@Builder
public class ScheduleRequest {
    String timezone;

    @Size(min= 7, max = 7, message = "availableTime must be exactly 21 bytes")
    boolean[][] availableTime;
}
