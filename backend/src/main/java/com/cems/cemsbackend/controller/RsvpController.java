package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.mappers.EventMapper;
import com.cems.cemsbackend.model.Attendance;
import com.cems.cemsbackend.model.AttendanceId;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.AttendanceRepository;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cems.shared.model.EventDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for managing event RSVP operations.
 */
@RestController
@RequestMapping("/rsvp")
public class RsvpController {

  private final EventRepository eventRepository;
  private final UserRepository userRepository;
  private final AttendanceRepository attendanceRepository;
  
  private static final String USER_NOT_FOUND_RESPONSE_TEXT = "User not found";
  private static final String EVENT_NOT_FOUND_RESPONSE_TEXT = "Event not found";

  /**
   * Constructor for RsvpController.
   *
   * @param eventRepository      repository for event data.
   * @param userRepository       repository for user data.
   * @param attendanceRepository repository for attendance data.
   */
  public RsvpController(EventRepository eventRepository,
                        UserRepository userRepository,
                        AttendanceRepository attendanceRepository) {
    this.eventRepository = eventRepository;
    this.userRepository = userRepository;
    this.attendanceRepository = attendanceRepository;
  }

  /**
   * Registers the authenticated user for an event.
   *
   * @param eventId the unique identifier of the event.
   * @return a success message.
   */
  @PostMapping("/{eventId}")
  @Transactional
  public ResponseEntity<Map<String, String>> rsvp(@PathVariable UUID eventId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
    }

    UUID userId = (UUID) auth.getPrincipal();
    User user = userRepository.getUserById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_RESPONSE_TEXT));

    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EVENT_NOT_FOUND_RESPONSE_TEXT));

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

  /**
   * Cancels the authenticated user's RSVP for an event.
   *
   * @param eventId the unique identifier of the event.
   * @return a no-content response.
   */
  @DeleteMapping("/{eventId}")
  @Transactional
  public ResponseEntity<Map<String, String>> cancel(@PathVariable UUID eventId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
    }

    UUID userId = (UUID) auth.getPrincipal();
    User user = userRepository.getUserById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_RESPONSE_TEXT));

    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EVENT_NOT_FOUND_RESPONSE_TEXT));

    if (!event.getAttendees().contains(user)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not RSVPed to this event");
    }

    attendanceRepository.deleteAttendanceByUser_IdAndEvent_Id(userId, eventId);

    event.removeAttendee(user);
    eventRepository.save(event);

    return ResponseEntity.noContent().build();
  }

  /**
   * Retrieves all events the authenticated user has RSVPed to.
   *
   * @return a list of event DTOs.
   */
  @GetMapping("/my-events")
  public ResponseEntity<List<EventDto.EventResponseDTO>> myRsvps() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
    }

    UUID userId = (UUID) auth.getPrincipal();
    User user = userRepository.getUserById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_RESPONSE_TEXT));

    var dtos = user.getAttendingEvents()
        .stream()
        .map(EventMapper::toDto)
        .toList();

    return ResponseEntity.ok(dtos);
  }

  /**
   * Checks if the authenticated user is registered for a specific event.
   *
   * @param eventId the unique identifier of the event.
   * @return a map containing the registration status.
   */
  @GetMapping("/{eventId}/registered")
  public ResponseEntity<Map<String, Boolean>> checkUserEventRsvp(@PathVariable UUID eventId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
    }

    UUID userId = (UUID) auth.getPrincipal();
    User user = userRepository.getUserById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_RESPONSE_TEXT));

    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, EVENT_NOT_FOUND_RESPONSE_TEXT));

    return ResponseEntity.ok(Map.of("registered", event.getAttendees().contains(user)));
  }
}