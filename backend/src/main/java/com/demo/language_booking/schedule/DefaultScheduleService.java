package com.demo.language_booking.schedule;

import com.demo.language_booking.common.exceptions.ResourceNotFoundException;
import com.demo.language_booking.schedule.dto.ScheduleRequest;
import com.demo.language_booking.users.User;
import com.demo.language_booking.users.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.BitSet;
import java.util.Optional;


@Service
@Validated
@AllArgsConstructor
public class DefaultScheduleService implements ScheduleService, AvailabilityService {
    private final UserService userService;
    private final RegularScheduleRepository regularScheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    public Optional<RegularSchedule> findById(long id) {
        return regularScheduleRepository.findById(id);
    }

    @Override
    public Optional<RegularSchedule> findByUser(User user) {
        return findById(user.getId());
    }

    @Override
    @NonNull
    @Transactional
    public RegularSchedule create(long id, @Valid @NotNull ScheduleRequest schedule) {
        User tutor = userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor not found for id: %s".formatted(id)));
        RegularSchedule parsedSchedule = scheduleMapper.toRegularSchedule(schedule);
        parsedSchedule.setTutor(tutor);
        return regularScheduleRepository.save(parsedSchedule);
    }

    @Override
    @NonNull
    @Transactional
    public RegularSchedule update(long id, @Valid @NotNull ScheduleRequest schedule) {
        RegularSchedule existingSchedule = regularScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found for user: %s".formatted(id)));
        RegularSchedule parsedSchedule = scheduleMapper.toRegularSchedule(schedule);

        existingSchedule.setTimezone(parsedSchedule.getTimezone());
        existingSchedule.setAvailableTime(parsedSchedule.getAvailableTime());

        return regularScheduleRepository.save(existingSchedule);
    }

    @Override
    @Transactional
    public void delete(long id) {
        regularScheduleRepository.deleteById(id);
    }

    private boolean isScheduleSlotAvailable(@NotNull BitSet availableTime, @NotNull BitSet requestedTime) {
        if (requestedTime.isEmpty()) return false;
        // Clone because for some reason bit operations mutate instead of returning a new one
        BitSet clonedRequest = (BitSet) requestedTime.clone();
        int numBits = clonedRequest.cardinality();
        clonedRequest.and(availableTime);
        return clonedRequest.cardinality() == numBits;
    }

    @Override
    public boolean isTimeSlotAvailable(long id, @NotNull Instant startTime) {
        Optional<RegularSchedule> maybeSchedule = regularScheduleRepository.findById(id);
        if (maybeSchedule.isEmpty()) return false;
        RegularSchedule schedule = maybeSchedule.get();
        ZonedDateTime zonedTime = startTime.atZone(schedule.getTimezone());
        BitSet requestedTime = new BitSet();
        // DayOfWeek enum starts at 1, but our index starts at 0.
        int mappedDayOfWeek = zonedTime.getDayOfWeek().getValue() - 1;
        int hourIndex = mappedDayOfWeek*24 + zonedTime.getHour();
        requestedTime.set(hourIndex);
        return isScheduleSlotAvailable(schedule.getAvailableTime(), requestedTime);
    }
}
