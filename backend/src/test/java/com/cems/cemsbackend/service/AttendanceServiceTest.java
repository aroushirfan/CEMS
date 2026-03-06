package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.Attendance;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.AttendanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    private User testUser;
    private Event testEvent;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        setPrivateField(testUser, "id", UUID.randomUUID());

        testEvent = new Event();
        setPrivateField(testEvent, "id", UUID.randomUUID());
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testCheckInSuccess() throws Exception {
        // 1. Prepare data
        testEvent.addAttendee(testUser);

        // 2. Mock the duplicate check
        when(attendanceRepository.findByUserAndEvent(testUser, testEvent))
                .thenReturn(Optional.empty());

        // 3. Prepare the returned object
        Attendance mockAttendance = new Attendance();
        mockAttendance.setStatus("PRESENT");

        // FIX: Change 'save' to 'saveAndFlush' to match your Service code!
        when(attendanceRepository.saveAndFlush(any(Attendance.class))).thenReturn(mockAttendance);

        // 4. Execute
        Attendance result = attendanceService.createCheckIn(testUser, testEvent);

        // 5. Assertions
        assertNotNull(result, "The service should not return null");
        assertEquals("PRESENT", result.getStatus());
    }

    @Test
    void testCheckIn_Fail_NotRSVPd() {
        // User not in attendees list
        assertThrows(ResponseStatusException.class, () -> {
            attendanceService.createCheckIn(testUser, testEvent);
        });
    }

    @Test
    void testCheckIn_Fail_Duplicate() {
        testEvent.addAttendee(testUser);

        // Return an Optional containing a fake attendance to simulate a duplicate
        when(attendanceRepository.findByUserAndEvent(testUser, testEvent))
                .thenReturn(Optional.of(new Attendance()));

        assertThrows(ResponseStatusException.class, () -> {
            attendanceService.createCheckIn(testUser, testEvent);
        });
    }

    @Test
    void testGetAttendanceByEvent() {
        java.util.List<Attendance> mockList = new java.util.ArrayList<>();
        mockList.add(new Attendance());

        when(attendanceRepository.findAllByEvent(testEvent)).thenReturn(mockList);

        var result = attendanceService.getAttendanceByEvent(testEvent);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}