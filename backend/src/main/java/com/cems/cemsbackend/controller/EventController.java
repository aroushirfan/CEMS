package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.mappers.EventMapper;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.EventTranslation;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.AttendanceRepository;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.repository.EventTranslationRepository;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.shared.model.EventDto.EventLocalRequestDTO;
import com.cems.shared.model.EventDto.EventRequestDTO;
import com.cems.shared.model.EventDto.EventResponseDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
 * Controller for managing events and localized event data.
 */
@RestController
@RequestMapping("/events")
@SuppressWarnings({
  "PMD.CyclomaticComplexity",
  "PMD.LawOfDemeter",
  "PMD.UseExplicitTypes",
  "PMD.MethodArgumentCouldBeFinal",
  "PMD.LocalVariableCouldBeFinal"
})
public class EventController {

  /** Error message for missing event. */
  private static final String ERR_NOT_FOUND = "Event with id not found";

  /** Error message for missing user. */
  private static final String ERR_USER_NOT_FOUND = "User not found";

  /** Error message for missing authentication. */
  private static final String ERR_NO_AUTH = "User not authenticated";

  /** Repository for event entities. */
  private final EventRepository eventRepository;

  /** Repository for user entities. */
  private final UserRepository userRepository;

  /** Repository for attendance records. */
  private final AttendanceRepository attendanceRepository;

  /** Repository for localized event translations. */
  private final EventTranslationRepository translationRepository;

  /**
   * Constructs the controller.
   *
   * @param eventRepository repository for events
   * @param userRepository repository for users
   * @param attendanceRepository repository for attendance
   * @param translationRepository repository for event translations
   */
  public EventController(
          final EventRepository eventRepository,
          final UserRepository userRepository,
          final AttendanceRepository attendanceRepository,
          final EventTranslationRepository translationRepository) {

    this.eventRepository = eventRepository;
    this.userRepository = userRepository;
    this.attendanceRepository = attendanceRepository;
    this.translationRepository = translationRepository;
  }

  // ---------------------------------------------------------------------------
  // Helper methods
  // ---------------------------------------------------------------------------

  /**
   * Resolves the authenticated user's UUID.
   *
   * @return UUID of the authenticated user
   */
  private UUID resolveUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERR_NO_AUTH);
    }
    return (UUID) auth.getPrincipal();
  }

  /**
   * Retrieves an event or throws NOT_FOUND.
   *
   * @param id event ID
   * @return event entity
   */
  private Event getEventOrThrow(final UUID id) {
    return eventRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ERR_NOT_FOUND));
  }

  /**
   * Retrieves a user or throws FORBIDDEN.
   *
   * @param id user ID
   * @return user entity
   */
  private User getUserOrThrow(final UUID id) {
    return userRepository.getUserById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                    ERR_USER_NOT_FOUND));
  }

  /**
   * Parses a string to UUID or throws BAD_REQUEST.
   *
   * @param id string representation of UUID
   * @return parsed UUID
   */
  private UUID parseUuidOrThrow(final String id) {
    try {
      return UUID.fromString(id);
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Id", ex);
    }
  }

  // ---------------------------------------------------------------------------
  // Public endpoints
  // ---------------------------------------------------------------------------

  /** Gets all events. */
  @GetMapping
  public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
    try {
      List<Event> events = eventRepository.findAll();
      if (events.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
      }
      return ResponseEntity.ok(events.stream().map(EventMapper::toDto).toList());
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to fetch events", e);
    }
  }

  /** Gets all events with localization. */
  @GetMapping("/all/{lang}")
  public ResponseEntity<List<EventResponseDTO>> getAllEventsLocal(
          @PathVariable final String lang) {
    try {
      List<Event> events = eventRepository.findAll();
      if (events.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
      }

      List<EventResponseDTO> response =
              events.stream()
                      .map(event ->
                              translationRepository
                                      .getByRefEventAndLanguage(event, lang)
                                      .map(t -> EventMapper.toDto(event, t))
                                      .orElseGet(() -> EventMapper.toDto(event)))
                      .toList();

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to fetch events", e);
    }
  }

  /** Gets an event by ID. */
  @GetMapping("/{id}")
  public ResponseEntity<EventResponseDTO> getEventById(@PathVariable final UUID id) {
    try {
      Event event = eventRepository.findById(id).orElse(null);
      if (event == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
      return ResponseEntity.ok(EventMapper.toDto(event));
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to fetch event", e);
    }
  }

  /** Gets a localized event by ID. */
  @GetMapping("/{id}/{lang}")
  public ResponseEntity<EventResponseDTO> getEventByIdLocal(
          @PathVariable final UUID id,
          @PathVariable final String lang) {
    try {
      Event event = eventRepository.getEventById(id).orElse(null);
      if (event == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
      }

      Optional<EventTranslation> translation =
              translationRepository.getByRefEventAndLanguage(event, lang);

      return translation
              .map(t -> ResponseEntity.ok(EventMapper.toDto(event, t)))
              .orElseGet(() -> ResponseEntity.ok(EventMapper.toDto(event)));
    } catch (Exception e) {
      if (e instanceof ResponseStatusException) {
        throw e;
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  /** Creates a new event. */
  @PostMapping
  @Transactional
  public ResponseEntity<EventResponseDTO> createEvent(
          @RequestBody final EventRequestDTO body) {
    try {
      UUID userId = resolveUserId();
      User owner = getUserOrThrow(userId);

      Event event =
              new Event(
                      body.getTitle(),
                      body.getDescription(),
                      body.getLocation(),
                      body.getCapacity(),
                      body.getDateTime(),
                      owner,
                      false);

      return ResponseEntity.status(HttpStatus.CREATED)
              .body(EventMapper.toDto(eventRepository.save(event)));
    } catch (Exception e) {
      if (e instanceof ResponseStatusException) {
        throw e;
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  /** Updates an event. */
  @PutMapping("/{id}")
  public ResponseEntity<EventResponseDTO> updateEvent(
          @PathVariable final String id,
          @RequestBody final EventRequestDTO body) {
    try {
      UUID uuid = parseUuidOrThrow(id);
      Event event = getEventOrThrow(uuid);

      event.updateFromDto(body);
      return ResponseEntity.ok(EventMapper.toDto(eventRepository.save(event)));
    } catch (Exception e) {
      if (e instanceof ResponseStatusException) {
        throw e;
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  /** Updates a localized event. */
  @PutMapping("/{id}/{lang}")
  @Transactional
  public ResponseEntity<EventResponseDTO> updateEventLocal(
          @PathVariable final String id,
          @PathVariable final String lang,
          @RequestBody final EventLocalRequestDTO dto) {
    try {
      UUID uuid = parseUuidOrThrow(id);
      Event event = getEventOrThrow(uuid);

      EventTranslation translation =
              translationRepository
                      .getByRefEventAndLanguage(event, lang)
                      .orElseGet(() -> {
                        EventTranslation t = new EventTranslation();
                        t.setLanguage(lang);
                        t.setRefEvent(event);
                        return t;
                      });

      translation.updateFromDTO(dto);
      translationRepository.save(translation);

      return ResponseEntity.ok(EventMapper.toDto(event, translation));
    } catch (Exception e) {
      if (e instanceof ResponseStatusException) {
        throw e;
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  /** Approves an event. */
  @PutMapping("/{id}/approve")
  @Transactional
  public ResponseEntity<EventResponseDTO> approveEvent(@PathVariable final String id) {
    try {
      UUID uuid = parseUuidOrThrow(id);
      Event event = getEventOrThrow(uuid);

      event.setApproved(true);
      return ResponseEntity.ok(EventMapper.toDto(eventRepository.save(event)));
    } catch (Exception e) {
      if (e instanceof ResponseStatusException) {
        throw e;
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  /** Deletes an event. */
  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<Void> deleteEvent(@PathVariable final String id) {
    try {
      UUID uuid = parseUuidOrThrow(id);
      Event event = getEventOrThrow(uuid);

      attendanceRepository.deleteAttendancesByEvent_Id(uuid);
      eventRepository.delete(event);

      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      if (e instanceof ResponseStatusException) {
        throw e;
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  /** Gets all approved events. */
  @GetMapping("/approved")
  public ResponseEntity<List<EventResponseDTO>> getApprovedEvents() {
    try {
      List<Event> events = eventRepository.findByApprovedTrue();
      if (events.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
      }
      return ResponseEntity.ok(events.stream().map(EventMapper::toDto).toList());
    } catch (Exception e) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Failed to fetch approved events",
              e
      );
    }
  }

  /** Gets all approved events with localization. */
  @GetMapping("/approved/{lang}")
  public ResponseEntity<List<EventResponseDTO>> getApprovedEventsLocal(
          @PathVariable final String lang) {
    try {
      List<Event> events = eventRepository.findByApprovedTrue();
      if (events.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
      }

      List<EventResponseDTO> response =
              events.stream()
                      .map(event ->
                              translationRepository
                                      .getByRefEventAndLanguage(event, lang)
                                      .map(t -> EventMapper.toDto(event, t))
                                      .orElseGet(() -> EventMapper.toDto(event)))
                      .toList();

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "Failed to fetch approved events",
              e
      );
    }
  }

  /** Gets events owned by the authenticated user. */
  @GetMapping("/admin")
  public ResponseEntity<List<EventResponseDTO>> getEventsByOwner() {
    try {
      UUID userId = resolveUserId();
      User owner = getUserOrThrow(userId);

      List<Event> events = eventRepository.getEventsByEventOwner(owner);
      return ResponseEntity.ok(events.stream().map(EventMapper::toDto).toList());
    } catch (Exception e) {
      if (e instanceof ResponseStatusException) {
        throw e;
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  /** Gets an event owned by the authenticated user. */
  @GetMapping("/admin/{id}")
  public ResponseEntity<EventResponseDTO> getEventByOwnerAndId(
          @PathVariable final String id) {
    try {
      UUID uuid = parseUuidOrThrow(id);
      UUID userId = resolveUserId();
      User owner = getUserOrThrow(userId);

      Event event =
              eventRepository
                      .getEventByEventOwnerAndId(owner, uuid)
                      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                              ERR_NOT_FOUND));

      return ResponseEntity.ok(EventMapper.toDto(event));
    } catch (Exception e) {
      if (e instanceof ResponseStatusException) {
        throw e;
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  /** Adds a localization entry for an event. */
  @PostMapping("/admin/{id}/{lang}")
  @Transactional
  public ResponseEntity<Void> addEventLocalization(
          @PathVariable final String id,
          @PathVariable final String lang,
          @RequestBody final EventLocalRequestDTO dto) {
    try {
      UUID uuid = parseUuidOrThrow(id);
      UUID userId = resolveUserId();
      User owner = getUserOrThrow(userId);

      Event event =
              eventRepository
                      .getEventByEventOwnerAndId(owner, uuid)
                      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                              ERR_NOT_FOUND));

      if (translationRepository.existsByRefEventAndLanguage(event, lang)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Localization exists");
      }

      translationRepository.save(EventTranslation.createFromDTO(dto, event, lang));
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      if (e instanceof ResponseStatusException) {
        throw e;
      }
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }
}
