package com.demo.language_booking.schedule;


import com.demo.language_booking.schedule.dto.ScheduleRequest;
import com.demo.language_booking.users.User;
import com.demo.language_booking.users.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ScheduleServiceITTest {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RegularScheduleRepository regularScheduleRepository;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    User.UserBuilder defaultUserBuilder() {
        return User.builder().username("test").email("test@test.net").password("Test1234");
    }

    ScheduleRequest.ScheduleRequestBuilder defaultScheduleBuilder() {
        boolean[][] availableTime = new boolean[7][24];
        for (boolean[] day : availableTime) {
            Arrays.fill(day, true);
        }
        return ScheduleRequest.builder().timezone("UTC").availableTime(availableTime);
    }

    @Test
    public void testCreateValidSchedule() {
        User user = defaultUserBuilder().build();
        user = userRepository.save(user);

        ScheduleRequest request = defaultScheduleBuilder().build();

        RegularSchedule createdSchedule = scheduleService.create(user.getId(), request);
        assertNotNull(createdSchedule);
        assertEquals(ZoneId.of("UTC"), createdSchedule.getTimezone());
        assertNotNull(createdSchedule.getAvailableTime());
    }

    @Test
    public void testUpdateSchedule() {
        User user = defaultUserBuilder().build();
        user = userRepository.save(user);
        ScheduleRequest request = defaultScheduleBuilder().build();
        scheduleService.create(user.getId(), request);

        final String updatedTimezone = "Europe/Madrid";
        assertNotSame(updatedTimezone, request.getTimezone());
        request.setTimezone(updatedTimezone);
        RegularSchedule updatedSchedule = scheduleService.update(user.getId(), request);
        assertNotNull(updatedSchedule);
        assertEquals(ZoneId.of(updatedTimezone), updatedSchedule.getTimezone());
    }

    @Test
    public void testFindById() throws JsonProcessingException {
        User user = defaultUserBuilder().build();
        user = userRepository.save(user);
        ScheduleRequest request = defaultScheduleBuilder().build();
        RegularSchedule createdSchedule = scheduleService.create(user.getId(), request);

        Optional<RegularSchedule> schedule = scheduleService.findById(user.getId());
        assertTrue(schedule.isPresent());
        assertEquals(objectMapper.writeValueAsString(createdSchedule), objectMapper.writeValueAsString(schedule.get()));
    }

    @Test
    public void testFindByUser() throws JsonProcessingException {
        User user = defaultUserBuilder().build();
        user = userRepository.save(user);
        ScheduleRequest request = defaultScheduleBuilder().build();
        RegularSchedule createdSchedule = scheduleService.create(user.getId(), request);

        Optional<RegularSchedule> schedule = scheduleService.findByUser(user);
        assertTrue(schedule.isPresent());
        assertEquals(objectMapper.writeValueAsString(createdSchedule), objectMapper.writeValueAsString(schedule.get()));
    }

    @Test
    public void testDeleteSchedule() {
        User user = defaultUserBuilder().build();
        user = userRepository.save(user);
        ScheduleRequest request = defaultScheduleBuilder().build();
        RegularSchedule createdSchedule = scheduleService.create(user.getId(), request);

        assertNotNull(createdSchedule);
        assertTrue(scheduleService.findById(user.getId()).isPresent());

        scheduleService.delete(user.getId());

        assertFalse(scheduleService.findById(user.getId()).isPresent());
    }


}
