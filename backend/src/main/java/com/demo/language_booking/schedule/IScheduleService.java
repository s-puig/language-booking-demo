package com.demo.language_booking.schedule;

import com.demo.language_booking.common.IScheduleAvailability;
import com.demo.language_booking.schedule.dto.ScheduleRequest;
import com.demo.language_booking.users.User;
import jakarta.validation.Valid;

import java.util.Optional;

public interface IScheduleService extends IScheduleAvailability {
    Optional<RegularSchedule> findById(long id);

    Optional<RegularSchedule> findByUser(User user);

    RegularSchedule create(long id, @Valid ScheduleRequest schedule);

    RegularSchedule update(long id, @Valid ScheduleRequest schedule);

    void delete(long id);
}
