package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.model.Attendance;
import com.cems.cemsbackend.model.AttendanceId;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.AttendanceRepository;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.cemsbackend.mappers.EventMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/rsvp")
public class RsvpController {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    public RsvpController(EventRepository eventRepository, UserRepository userRepository, AttendanceRepository attendanceRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @PostMapping("/{eventId}")
    @Transactional
    public ResponseEntity<?> rsvp(@PathVariable UUID eventId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");

        UUID userId = (UUID) auth.getPrincipal();
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (event.getAttendees().contains(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already RSVPed");
        }

        if (event.getCapacity() > 0 && event.getAttendees().size() >= event.getCapacity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event is full");
        }

        event.addAttendee(user);


        eventRepository.save(event);

        var attendance = new Attendance();
        attendance.setId(new AttendanceId(userId, eventId));
        attendance.setStatus("ABSENT");
        attendance.setUser(user);
        attendance.setEvent(event);
        attendanceRepository.save(attendance);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "RSVP successful"));
    }

    @DeleteMapping("/{eventId}")
    @Transactional
    public ResponseEntity<?> cancel(@PathVariable UUID eventId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");

        UUID userId = (UUID) auth.getPrincipal();
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        if (!event.getAttendees().contains(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have not RSVPed to this event");
        }

        attendanceRepository.deleteAttendanceByUser_IdAndEvent_Id(userId, eventId);

        event.removeAttendee(user);
        eventRepository.save(event);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-events")
    public ResponseEntity<?> myRsvps() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");

        UUID userId = (UUID) auth.getPrincipal();
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        var dtos = user.getAttendingEvents()
                .stream()
                .map(EventMapper::toDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{eventId}/registered")
    public ResponseEntity<?> checkUserEventRsvp(@PathVariable UUID eventId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");

        UUID userId = (UUID) auth.getPrincipal();
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        return ResponseEntity.ok(Map.of("registered",event.getAttendees().contains(user)));
    }
}

