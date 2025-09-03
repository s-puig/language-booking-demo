package com.demo.language_booking.schedule.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Schedule {
    private long tutor_id;
    private String timezone;
    private boolean[][] availableTime;
}
