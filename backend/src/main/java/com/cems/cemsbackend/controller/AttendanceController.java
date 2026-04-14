package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.mappers.AttendanceMapper;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.cemsbackend.service.AttendanceService;
import com.cems.shared.model.AttendanceDto.AttendanceResponseDTO;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for managing attendance-related operations for events.
 */
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

  private final AttendanceService attendanceService;
  private final UserRepository userRepository;
  private final EventRepository eventRepository;

  /**
   * Constructor for AttendanceController.
   *
   * @param attendanceService service for attendance logic.
   * @param userRepository     repository for user data.
   * @param eventRepository    repository for event data.
   */
  public AttendanceController(AttendanceService attendanceService,
                              UserRepository userRepository,
                              EventRepository eventRepository) {
    this.attendanceService = attendanceService;
    this.userRepository = userRepository;
    this.eventRepository = eventRepository;
  }

  /**
   * Records a user's attendance for a specific event.
   *
   * @param eventId the unique identifier of the event.
   * @return a success message upon successful check-in.
   */
  @PostMapping("/event/{eventId}/check-in")
  public ResponseEntity<?> checkIn(@PathVariable UUID eventId) {
    // Identity: Securely pull the UserID from the JWT
    UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = userRepository.findById(userId).orElseThrow();
    Event event = eventRepository.findById(eventId).orElseThrow();

    attendanceService.createCheckIn(user, event);
    TreeMap<String, String> responseMap = new TreeMap<>();
    responseMap.put("message", "Check in Successful. Thank you for your attendance.");

    return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
  }

  /**
   * Retrieves all attendance records for a specific event.
   *
   * @param eventId the unique identifier of the event.
   * @return a list of attendance DTOs.
   */
  @GetMapping("/event/{eventId}")
  public ResponseEntity<List<AttendanceResponseDTO>> getEventAttendance(
      @PathVariable UUID eventId) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

    List<AttendanceResponseDTO> response = attendanceService.getAttendanceByEvent(event)
        .stream()
        .map(AttendanceMapper::toDto)
        .collect(Collectors.toList());

    return ResponseEntity.ok(response);
  }

  /**
   * Checks if the authenticated user has already checked in to an event.
   *
   * @param eventId the unique identifier of the event.
   * @return a map containing the check-in status.
   */
  @GetMapping("/event/{eventId}/checked-in")
  public ResponseEntity<?> hasCheckedIn(
      @PathVariable UUID eventId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
    UUID userId = (UUID) auth.getPrincipal();
    User user = userRepository.getUserById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

    return ResponseEntity.status(HttpStatus.OK)
        .body(Map.of("checkedIn", attendanceService.hasCheckedIn(user, event)));
  }
}