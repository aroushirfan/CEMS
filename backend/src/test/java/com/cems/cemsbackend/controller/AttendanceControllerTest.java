package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.shared.model.AttendanceDto.AttendanceResponseDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

import java.time.Instant;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // Keeps your database clean after each test
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AttendanceControllerTest {

    @Autowired
    private AttendanceController attendanceController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    private User testUser;
    private Event testEvent;

    @BeforeEach
    void setupIdentityAndData() throws Exception {
        // 1. Create and Save User
        User user = new User();
        user.setEmail("jeena@metropolia.fi");
        user.setFirstName("Jeena");
        user.setLastName("Sen");
        user.setHashedPassword("Sailesh1103");
        testUser = userRepository.saveAndFlush(user);

        // 2. Simulate JWT Authentication (Crucial for your controller)
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(testUser.getId(), null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 3. Create and Save Event
        testEvent = new Event("Test Lab", "Desc", "Vantaa", 10L, Instant.now(), testUser, true);

        // Manual initialization of attendees to satisfy Gatekeeper
        Field field = Event.class.getDeclaredField("attendees");
        field.setAccessible(true);
        field.set(testEvent, new ArrayList<>());

        testEvent.addAttendee(testUser);
        testEvent = eventRepository.saveAndFlush(testEvent);
    }

    @Test
    @DisplayName("POST /attendance/event/{id}/check-in should return 201 Created")
    void checkIn() {
        var response = attendanceController.checkIn(testEvent.getId());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PRESENT", response.getBody().getStatus());
    }

    @Test
    @DisplayName("GET /attendance/event/{id} should return list of attendees")
    void getEventAttendance() {
        // Perform check-in first
        attendanceController.checkIn(testEvent.getId());

        // Test retrieval
        var response = attendanceController.getEventAttendance(testEvent.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<AttendanceResponseDTO> list = response.getBody();
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    @DisplayName("GET /attendance/event/{id} should return 404 when event does not exist")
    void getEventAttendance_NotFound() {
        UUID fakeEventId = UUID.randomUUID();

        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            attendanceController.getEventAttendance(fakeEventId);
        });
    }

    @Test
    @DisplayName("POST /check-in should fail (403) if user is not on attendee list")
    void checkIn_Forbidden_NotRSVPd() {
        // Create a user who hasn't RSVP'd
        User stranger = new User();
        stranger.setEmail("stranger@metropolia.fi");
        stranger.setFirstName("Not");
        stranger.setLastName("Invited");
        stranger.setHashedPassword("password");
        User savedStranger = userRepository.saveAndFlush(stranger);

        // Switch security identity to the stranger
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(savedStranger.getId(), null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Calling check-in should now throw FORBIDDEN (handled by AttendanceService logic)
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            attendanceController.checkIn(testEvent.getId());
        });
    }

    @Test
    @DisplayName("POST /check-in should fail (409) if user is already checked in")
    void checkIn_Conflict_Duplicate() {
        // First check-in
        attendanceController.checkIn(testEvent.getId());

        // Second check-in should throw CONFLICT
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            attendanceController.checkIn(testEvent.getId());
        });
    }
}