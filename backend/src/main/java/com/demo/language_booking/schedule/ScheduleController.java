package com.demo.language_booking.schedule;

import com.demo.language_booking.auth.CurrentSession;
import com.demo.language_booking.auth.authorization.AuthPolicy;
import com.demo.language_booking.auth.authorization.Authorize;
import com.demo.language_booking.auth.authorization.IdentityPolicy;
import com.demo.language_booking.common.exceptions.ResourceNotFoundException;
import com.demo.language_booking.schedule.dto.Schedule;
import com.demo.language_booking.schedule.dto.ScheduleRequest;
import com.demo.language_booking.users.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@AuthPolicy(IdentityPolicy.class)
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

	private final ScheduleService scheduleService;

	private final ScheduleMapper scheduleMapper;

	@GetMapping("/{id}")
	public ResponseEntity<Schedule> getById(@PathVariable long id) {
		RegularSchedule schedule = scheduleService.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Schedule resource does not exist"));
		return ResponseEntity.ok(scheduleMapper.toSchedule(schedule));
	}

	@Authorize(value = User.Role.TEACHER, requireOwnership = true)
	@PostMapping
	public ResponseEntity<Schedule> create(CurrentSession user, @RequestBody ScheduleRequest request) {
		RegularSchedule createdSchedule = scheduleService.create(user.getId(), request);
		return ResponseEntity.ok(scheduleMapper.toSchedule(createdSchedule));
	}

	@Authorize(User.Role.TEACHER)
	@PutMapping("/{id}")
	public ResponseEntity<Schedule> update(CurrentSession user, @RequestBody ScheduleRequest request) {
		RegularSchedule schedule = scheduleService.update(user.getId(), request);
		return ResponseEntity.ok(scheduleMapper.toSchedule(schedule));
	}

	@Authorize(value = User.Role.TEACHER, requireOwnership = true)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable long id) {
		scheduleService.delete(id);
		return ResponseEntity.noContent()
				.build();
	}
}
