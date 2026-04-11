package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.mappers.EventMapper;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.AttendanceRepository;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.shared.model.EventDto.EventRequestDTO;
import com.cems.shared.model.EventDto.EventResponseDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for managing events.
 */
@RestController
@RequestMapping("/events")
public class EventController {

  /** Error message. */
  private static final String NOT_FOUND = "Event with id not found";
  /** Auth error message. */
  private static final String NO_AUTH = "User not authenticated";

  /** Repository for events. */
  private final EventRepository eventRepo;
  /** Repository for users. */
  private final UserRepository userRepo;
  /** Repository for attendances. */
  private final AttendanceRepository attendRepo;

  /**
   * Constructor for EventController.
   *
   * @param eventRepo  the repository for events
   * @param userRepo   the repository for users
   * @param attendRepo the repository for attendances
   */
  public EventController(final EventRepository eventRepo,
                         final UserRepository userRepo,
                         final AttendanceRepository attendRepo) {
    this.eventRepo = eventRepo;
    this.userRepo = userRepo;
    this.attendRepo = attendRepo;
  }

  /**
   * Fetches all events.
   *
   * @return list of event DTOs
   */
  @GetMapping
  public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
    final List<Event> eventList = eventRepo.findAll();
    ResponseEntity<List<EventResponseDTO>> response =
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    if (!eventList.isEmpty()) {
      response = ResponseEntity.ok(eventList.stream().map(EventMapper::toDto).toList());
    }
    return response;
  }

  /**
   * Fetches event by ID.
   *
   * @param eventUuid the UUID of the event
   * @return the event DTO
   */
  @GetMapping(path = "/{id}")
  public ResponseEntity<EventResponseDTO> getEventById(@PathVariable("id") final UUID eventUuid) {
    final Optional<Event> optionalEvent = eventRepo.findById(eventUuid);
    ResponseEntity<EventResponseDTO> response = ResponseEntity.notFound().build();
    if (optionalEvent.isPresent()) {
      response = ResponseEntity.ok(EventMapper.toDto(optionalEvent.get()));
    }
    return response;
  }

  /**
   * Creates an event.
   *
   * @param body the request data
   * @return the created event
   */
  @PostMapping
  @Transactional
  public ResponseEntity<EventResponseDTO>
      createEvent(@RequestBody @Valid final EventRequestDTO body) {
    final UUID userId = resolveId();
    final User owner = userRepo.getUserById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User error"));

    final Event event = new Event(
            body.getTitle(), body.getDescription(), body.getLocation(),
            body.getCapacity(), body.getDateTime(), owner, false
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(EventMapper.toDto(eventRepo.save(event)));
  }

  /**
   * Updates an event.
   *
   * @param eventUuid the string ID
   * @param body      updated data
   * @return updated event
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<EventResponseDTO> updateEvent(
          @PathVariable("id") final String eventUuid,
          @RequestBody final EventRequestDTO body) {
    final Event target = eventRepo.getEventById(UUID.fromString(eventUuid))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND));
    target.updateFromDto(body);
    return ResponseEntity.ok(EventMapper.toDto(eventRepo.save(target)));
  }

  /**
   * Approves an event.
   *
   * @param eventUuid the string ID
   * @return approved event
   */
  @PutMapping(path = "/{id}/approve")
  @Transactional
  public ResponseEntity<EventResponseDTO> approveEvent(@PathVariable("id") final String eventUuid) {
    final Event target = eventRepo.findById(UUID.fromString(eventUuid))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND));
    target.setApproved(true);
    return ResponseEntity.ok(EventMapper.toDto(eventRepo.save(target)));
  }

  /**
   * Deletes an event.
   *
   * @param eventUuid the string ID
   * @return no content
   */
  @DeleteMapping(path = "/{id}")
  @Transactional
  public ResponseEntity<?> deleteEvent(@PathVariable("id") final String eventUuid) {
    final UUID uuid = UUID.fromString(eventUuid);
    final Event target = eventRepo.getEventById(uuid)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND));
    attendRepo.deleteAttendancesByEvent_Id(uuid);
    eventRepo.delete(target);
    return ResponseEntity.noContent().build();
  }

  /**
   * Gets approved events.
   *
   * @return list of events
   */
  @GetMapping("/approved")
  public ResponseEntity<List<EventResponseDTO>> getApprovedEvents() {
    final List<Event> list = eventRepo.findByApprovedTrue();
    ResponseEntity<List<EventResponseDTO>> response =
            ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    if (!list.isEmpty()) {
      response = ResponseEntity.ok(list.stream().map(EventMapper::toDto).toList());
    }
    return response;
  }

  /**
   * Gets events for the admin.
   *
   * @return owner events
   */
  @GetMapping(path = "/admin")
  public ResponseEntity<List<EventResponseDTO>> getEventByOwner() {
    final User owner = userRepo.getUserById(resolveId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden"));
    return ResponseEntity.ok(eventRepo.getEventsByEventOwner(owner)
            .stream().map(EventMapper::toDto).toList());
  }

  /**
   * Gets specific owner event.
   *
   * @param eventUuid the string ID
   * @return event DTO
   */
  @GetMapping(path = "/admin/{id}")
  public ResponseEntity<EventResponseDTO> getEventByOwnerAndId(
          @PathVariable("id") final String eventUuid) {
    final User owner = userRepo.getUserById(resolveId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden"));
    final Event event = eventRepo.getEventByEventOwnerAndId(owner, UUID.fromString(eventUuid))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND));
    return ResponseEntity.ok(EventMapper.toDto(event));
  }

  /**
   * Resolves ID by interacting only with immediate dependencies.
   * * @return UUID of the principal
   */
  private UUID resolveId() {
    final SecurityContext context = SecurityContextHolder.getContext();
    final Authentication authentication = context.getAuthentication();

    if (authentication == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_AUTH);
    }

    final Object principal = authentication.getPrincipal();
    return (UUID) principal;
  }
}