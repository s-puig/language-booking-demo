package com.demo.language_booking.schedule;

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

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable long id) {
        Optional<RegularSchedule> schedule = scheduleService.findById(id);
        return schedule.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@RequestBody ScheduleRequest request) {
        User user = new User(); // This is a placeholder - actual implementation should get current user
        user.setId(1L); // This is a placeholder - actual implementation should get current user
        RegularSchedule createdSchedule = scheduleService.create(user.getId(), request);
        return ResponseEntity.ok(scheduleService.toSchedule(createdSchedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
