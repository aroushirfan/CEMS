package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.AttendanceRepository;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RsvpControllerTest {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private RsvpController rsvpController;
    private AttendanceRepository attendanceRepository;

    private UUID userId;
    private UUID eventId;

    @BeforeEach
    void setup() {

        eventRepository = Mockito.mock(EventRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        attendanceRepository = Mockito.mock(AttendanceRepository.class);

        rsvpController = new RsvpController(eventRepository, userRepository, attendanceRepository);

        userId = UUID.randomUUID();
        eventId = UUID.randomUUID();

        // fake authentication
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userId, null);

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void rsvpSuccess() {

        User user = new User();
        Event event = new Event(
                "Test",
                "Desc",
                "Helsinki",
                10,
                Instant.now(),
                user,
                true
        );

        when(userRepository.getUserById(userId)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        ResponseEntity<?> response = rsvpController.rsvp(eventId);

        assertEquals(201, response.getStatusCode().value());
        verify(eventRepository).save(event);
        assertEquals(1, event.getAttendees().size());
    }

    @Test
    void rsvpAlreadyExists() {

        User user = new User();
        Event event = new Event(
                "Test",
                "Desc",
                "Helsinki",
                10,
                Instant.now(),
                user,
                true
        );

        event.addAttendee(user);

        when(userRepository.getUserById(userId)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        assertThrows(
                org.springframework.web.server.ResponseStatusException.class,
                () -> rsvpController.rsvp(eventId)
        );
    }

    @Test
    void cancelRsvpSuccess() {

        User user = new User();
        Event event = new Event(
                "Test",
                "Desc",
                "Helsinki",
                10,
                Instant.now(),
                user,
                true
        );

        event.addAttendee(user);

        when(userRepository.getUserById(userId)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        ResponseEntity<?> response = rsvpController.cancel(eventId);

        assertEquals(204, response.getStatusCode().value());
        verify(eventRepository).save(event);
    }

    @Test
    void cancelWithoutRsvpShouldFail() {

        User user = new User();
        Event event = new Event(
                "Test",
                "Desc",
                "Helsinki",
                10,
                Instant.now(),
                user,
                true
        );

        when(userRepository.getUserById(userId)).thenReturn(Optional.of(user));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        assertThrows(
                org.springframework.web.server.ResponseStatusException.class,
                () -> rsvpController.cancel(eventId)
        );
    }
}