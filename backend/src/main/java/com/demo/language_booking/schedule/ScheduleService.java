package com.demo.language_booking.schedule;

import com.demo.language_booking.common.exceptions.ResourceNotFoundException;
import com.demo.language_booking.schedule.dto.ScheduleRequest;
import com.demo.language_booking.users.User;
import com.demo.language_booking.users.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.Optional;


@Service
@Validated
@AllArgsConstructor
public class ScheduleService {
    private final UserService userService;
    private final RegularScheduleRepository regularScheduleRepository;
    private final ScheduleMapper scheduleMapper;

    public Optional<RegularSchedule> findById(long id) {
        return regularScheduleRepository.findById(id);
    }

    public Optional<RegularSchedule> findByUser(User user) {
        return findById(user.getId());
    }

    @NotNull
    @Transactional
    public RegularSchedule create(long id, @Valid ScheduleRequest schedule) {
        User tutor = userService.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor not found for id: %s".formatted(id)));
        RegularSchedule parsedSchedule = scheduleMapper.toRegularSchedule(schedule);
        parsedSchedule.setTutor(tutor);
        return regularScheduleRepository.save(parsedSchedule);
    }

    @NotNull
    @Transactional
    public RegularSchedule update(long id, @Valid ScheduleRequest schedule) {
        RegularSchedule existingSchedule = regularScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found for user: %s".formatted(id)));
        RegularSchedule parsedSchedule = scheduleMapper.toRegularSchedule(schedule);

        existingSchedule.setTimezone(parsedSchedule.getTimezone());
        existingSchedule.setAvailableTime(parsedSchedule.getAvailableTime());

        return regularScheduleRepository.save(existingSchedule);
    }

    @Transactional
    public void delete(long id) {
        regularScheduleRepository.deleteById(id);
    }
}
