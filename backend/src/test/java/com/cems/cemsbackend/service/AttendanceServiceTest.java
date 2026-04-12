package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.Attendance;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.AttendanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    private User user;
    private Event event;

    @BeforeEach
    void setup() {
        user = new User();
        event = new Event();
    }

    @Test
    void checkInFails_WhenUserNotRSVP() {
        event.getAttendees().clear();

        when(attendanceRepository.findByUserAndEvent(user, event)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> attendanceService.createCheckIn(user, event));
    }

    @Test
    void checkInFails_WhenAlreadyPresent() {
        event.getAttendees().add(user);

        Attendance attendance = new Attendance();
        attendance.setStatus("PRESENT");

        when(attendanceRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(attendance));

        assertThrows(ResponseStatusException.class, () -> attendanceService.createCheckIn(user, event));
    }

    @Test
    void checkInSuccess() {
        event.getAttendees().add(user);

        Attendance attendance = new Attendance();
        attendance.setStatus("ABSENT");

        when(attendanceRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(attendance));
        when(attendanceRepository.saveAndFlush(attendance)).thenReturn(attendance);

        Attendance result = attendanceService.createCheckIn(user, event);

        assertEquals("PRESENT", result.getStatus());
        assertNotNull(result.getCheckInTime());
    }
}
