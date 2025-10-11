package com.demo.language_booking.schedule;

import com.demo.language_booking.schedule.dto.ScheduleRequest;
import com.demo.language_booking.users.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ScheduleService {
    /**
     * Retrieves a regular schedule by its unique identifier.
     *
     * @param id the user's unique identifier
     * @return an Optional containing the RegularSchedule if found, or empty if not found
     */
    Optional<RegularSchedule> findById(long id);

    /**
     * Retrieves the regular schedule associated with the specified user.
     *
     * @param user the user for which to find the schedule
     * @return an Optional containing the RegularSchedule if found, or empty if no schedule exists for the user
     */
    Optional<RegularSchedule> findByUser(@NotNull User user);

    /**
     * Creates a new regular schedule for a user with the specified ID using the provided schedule request data.
     *
     * @param id the unique identifier of the user for whom the schedule is being created
     * @param schedule the schedule request containing timezone and available time information
     * @return the newly created RegularSchedule entity
     */
    @NonNull
    RegularSchedule create(long id, @Valid @NotNull ScheduleRequest schedule);

    /**
     * Updates an existing regular schedule with the provided schedule data.
     *
     * @param id the unique identifier of the schedule to update
     * @param schedule the schedule request containing updated schedule data
     * @return the updated RegularSchedule entity
     */
    @NonNull
    RegularSchedule update(long id, @Valid @NotNull ScheduleRequest schedule);

    /**
     * Deletes a schedule record identified by the given ID.
     *
     * @param id the unique identifier of the schedule to delete
     */
    void delete(long id);
}
