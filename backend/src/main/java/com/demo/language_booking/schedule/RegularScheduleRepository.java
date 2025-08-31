package com.demo.language_booking.schedule;

import com.demo.language_booking.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegularScheduleRepository extends JpaRepository<RegularSchedule, Long> {

}
