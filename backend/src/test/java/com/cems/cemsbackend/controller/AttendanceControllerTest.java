package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.model.Attendance;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.cemsbackend.service.AttendanceService;
import com.cems.shared.model.AttendanceDto.AttendanceResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttendanceControllerTest {

    @Mock
    private AttendanceService attendanceService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;

    private AttendanceController attendanceController;
    private UUID userId;
    private UUID eventId;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        attendanceController = new AttendanceController(attendanceService, userRepository, eventRepository);

        userId = UUID.randomUUID();
        eventId = UUID.randomUUID();

        // Simulate JWT Authentication by putting the UUID in the security context
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testCheckInSuccess() throws Exception {
        User user = new User();
        setPrivateField(user, "id", userId);

        Event event = new Event();
        setPrivateField(event, "id", eventId);

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setEvent(event);
        attendance.setStatus("PRESENT");

        // Mock the lookups and service call
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(attendanceService.createCheckIn(user, event)).thenReturn(attendance);

        var response = attendanceController.checkIn(eventId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetEventAttendanceSuccess() throws Exception {
        Event event = new Event();
        setPrivateField(event, "id", eventId);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(attendanceService.getAttendanceByEvent(event)).thenReturn(List.of());

        var response = attendanceController.getEventAttendance(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(attendanceService).getAttendanceByEvent(event);
    }
}