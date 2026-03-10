package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.mappers.AttendanceMapper;
import com.cems.cemsbackend.model.Attendance;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.cemsbackend.service.AttendanceService;
import com.cems.shared.model.AttendanceDto.AttendanceRequestDTO;
import com.cems.shared.model.AttendanceDto.AttendanceResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public AttendanceController(AttendanceService attendanceService,
                                UserRepository userRepository,
                                EventRepository eventRepository) {
        this.attendanceService = attendanceService;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @PostMapping("/event/{eventId}/check-in")
    public ResponseEntity<?> checkIn(@PathVariable UUID eventId) {
        // Identity: Securely pull the UserID from the JWT
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();

        // Service Call: No status needed from the frontend
        Attendance attendance = attendanceService.createCheckIn(user, event);

        TreeMap<String, String> responseMap = new TreeMap<>();
        responseMap.put("message", "Check in Successful. Thank you for your attendance.");

        return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<AttendanceResponseDTO>> getEventAttendance(@PathVariable UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        List<AttendanceResponseDTO> response = attendanceService.getAttendanceByEvent(event)
                .stream()
                .map(AttendanceMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/event/{eventId}/checked-in")
    public ResponseEntity<?> hasCheckedIn(
            @PathVariable UUID eventId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        UUID userId = (UUID) auth.getPrincipal();
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("checkedIn", attendanceService.hasCheckedIn(user,event)));
    }
}