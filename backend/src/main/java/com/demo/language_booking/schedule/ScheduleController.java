package com.demo.language_booking.schedule;

import com.demo.language_booking.auth.CurrentSession;
import com.demo.language_booking.schedule.dto.ScheduleRequest;
import com.demo.language_booking.schedule.dto.Schedule;
import com.demo.language_booking.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    private final ScheduleMapper scheduleMapper;

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable long id) {
        Optional<RegularSchedule> schedule = scheduleService.findById(id);
        return schedule.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(CurrentSession user, @RequestBody ScheduleRequest request) {
        RegularSchedule createdSchedule = scheduleService.create(user.getId(), request);
        return ResponseEntity.ok(scheduleMapper.toSchedule(createdSchedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
