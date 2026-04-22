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

import java.util.List;
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
    void checkInFails_WhenUserNotInEventRoster() {
        event.getAttendees().clear(); // User not in attendee list

        Attendance attendance = new Attendance();
        attendance.setStatus("RSVP_CONFIRMED");

        when(attendanceRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(attendance));

        assertThrows(ResponseStatusException.class, () -> attendanceService.createCheckIn(user, event));
    }

    @Test
    void checkInSuccess() {
        event.getAttendees().add(user);

        Attendance attendance = new Attendance();
        attendance.setStatus("RSVP_CONFIRMED"); // Use a realistic pre-checkin status

        when(attendanceRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(attendance));
        when(attendanceRepository.saveAndFlush(any(Attendance.class))).thenReturn(attendance);

        Attendance result = attendanceService.createCheckIn(user, event);

        assertEquals("PRESENT", result.getStatus());
        assertNotNull(result.getCheckInTime());
        verify(attendanceRepository).saveAndFlush(attendance);
    }

    @Test
    void getAttendanceByEvent_ReturnsAllAttendance() {
        Attendance attendance1 = new Attendance();
        Attendance attendance2 = new Attendance();
        List<Attendance> attendanceList = List.of(attendance1, attendance2);

        when(attendanceRepository.findAllByEvent(event)).thenReturn(attendanceList);

        List<Attendance> result = attendanceService.getAttendanceByEvent(event);

        assertEquals(2, result.size());
        verify(attendanceRepository).findAllByEvent(event);
    }

    @Test
    void getAttendanceByEvent_ReturnsEmptyListWhenNoAttendance() {
        when(attendanceRepository.findAllByEvent(event)).thenReturn(List.of());

        List<Attendance> result = attendanceService.getAttendanceByEvent(event);

        assertTrue(result.isEmpty());
        verify(attendanceRepository).findAllByEvent(event);
    }

    @Test
    void hasCheckedIn_ReturnsTrueWhenUserPresent() {
        Attendance attendance = new Attendance();
        attendance.setStatus("PRESENT");

        when(attendanceRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(attendance));

        boolean result = attendanceService.hasCheckedIn(user, event);

        assertTrue(result);
    }

    @Test
    void hasCheckedIn_ReturnsFalseWhenUserNotPresent() {
        Attendance attendance = new Attendance();
        attendance.setStatus("RSVP_CONFIRMED");

        when(attendanceRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(attendance));

        boolean result = attendanceService.hasCheckedIn(user, event);

        assertFalse(result);
    }

    @Test
    void hasCheckedIn_ReturnsFalseWhenNoAttendance() {
        when(attendanceRepository.findByUserAndEvent(user, event)).thenReturn(Optional.empty());

        boolean result = attendanceService.hasCheckedIn(user, event);

        assertFalse(result);
    }

}
