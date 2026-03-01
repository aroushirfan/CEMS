package com.cems.cemsbackend.controller;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponseDTO> checkIn(@RequestBody AttendanceRequestDTO request) {
        // Fetch entities to ensure they are managed by JPA
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        Attendance attendance = attendanceService.createCheckIn(user, event, request.getStatus());

        AttendanceResponseDTO response = new AttendanceResponseDTO(
                attendance.getUser().getId(),
                attendance.getEvent().getId(),
                attendance.getCheckInTime(),
                attendance.getStatus()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<AttendanceResponseDTO>> getEventAttendance(@PathVariable UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        List<AttendanceResponseDTO> response = attendanceService.getAttendanceByEvent(event)
                .stream()
                .map(a -> new AttendanceResponseDTO(
                        a.getUser().getId(),
                        a.getEvent().getId(),
                        a.getCheckInTime(),
                        a.getStatus()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}