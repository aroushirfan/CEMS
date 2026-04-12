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
import jakarta.validation.Valid;
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
 * Controller for managing events, including localization and admin operations.
 */
@RestController
@RequestMapping("/events")
@SuppressWarnings({
    "PMD.LawOfDemeter",
    "PMD.CyclomaticComplexity",
    "PMD.ShortVariable",
    "PMD.LongVariable",
    "PMD.UseExplicitTypes",
    "PMD.MethodArgumentCouldBeFinal",
    "PMD.LocalVariableCouldBeFinal",
    "PMD.OnlyOneReturn"
})
public class EventController {

  private static final String ERR_NOT_FOUND = "Event with id not found";
  private static final String ERR_USER_NOT_FOUND = "User not found";
  private static final String ERR_NO_AUTH = "User not authenticated";

  /** Repository for event entities. */
  private final EventRepository eventRepo;

  /** Repository for user entities. */
  private final UserRepository userRepo;

  /** Repository for attendance records. */
  private final AttendanceRepository attendanceRepo;

  /** Repository for localized event translations. */
  private final EventTranslationRepository translationRepo;


  /**
   * Constructs the controller.
   *
   * @param eventRepo repository for events
   * @param userRepo repository for users
   * @param attendanceRepo repository for attendance
   * @param translationRepo repository for event translations
   */
  public EventController(
          final EventRepository eventRepo,
          final UserRepository userRepo,
          final AttendanceRepository attendanceRepo,
          final EventTranslationRepository translationRepo) {

    this.eventRepo = eventRepo;
    this.userRepo = userRepo;
    this.attendanceRepo = attendanceRepo;
    this.translationRepo = translationRepo;
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
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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
    return eventRepo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ERR_NOT_FOUND));
  }

  /**
   * Retrieves a user or throws FORBIDDEN.
   *
   * @param id user ID
   * @return user entity
   */
  private User getUserOrThrow(final UUID id) {
    return userRepo.getUserById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                     ERR_USER_NOT_FOUND));
  }

  // ---------------------------------------------------------------------------
  // Public endpoints
  // ---------------------------------------------------------------------------

  /**
   * Retrieves all events.
   *
   * @return list of event DTOs
   */
  @GetMapping
  public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
    final List<Event> events = eventRepo.findAll();
    if (events.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
    return ResponseEntity.ok(events.stream().map(EventMapper::toDto).toList());
  }

  /**
   * Retrieves all events with localization.
   *
   * @param lang language code
   * @return list of localized event DTOs
   */
  @GetMapping("/all/{lang}")
  public ResponseEntity<List<EventResponseDTO>> getAllEventsLocalized(
          @PathVariable final String lang) {

    final List<Event> events = eventRepo.findAll();
    if (events.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    final List<EventResponseDTO> response =
            events.stream()
                    .map(
                            event ->
                                    translationRepo
                                            .getByRefEventAndLanguage(event, lang)
                                            .map(t -> EventMapper.toDto(event, t))
                                            .orElseGet(() -> EventMapper.toDto(event)))
                    .toList();

    return ResponseEntity.ok(response);
  }

  /**
   * Retrieves an event by ID.
   *
   * @param id event ID
   * @return event DTO
   */
  @GetMapping("/{id}")
  public ResponseEntity<EventResponseDTO> getEventById(@PathVariable final UUID id) {
    final Event event = getEventOrThrow(id);
    return ResponseEntity.ok(EventMapper.toDto(event));
  }

  /**
   * Retrieves a localized event by ID.
   *
   * @param id event ID
   * @param lang language code
   * @return localized event DTO
   */
  @GetMapping("/{id}/{lang}")
  public ResponseEntity<EventResponseDTO> getEventByIdLocalized(
          @PathVariable final UUID id, @PathVariable final String lang) {

    final Event event = getEventOrThrow(id);

    final Optional<EventTranslation> translation =
            translationRepo.getByRefEventAndLanguage(event, lang);

    return translation
            .map(t -> ResponseEntity.ok(EventMapper.toDto(event, t)))
            .orElseGet(() -> ResponseEntity.ok(EventMapper.toDto(event)));
  }

  /**
   * Creates a new event.
   *
   * @param body event request data
   * @return created event DTO
   */
  @PostMapping
  @Transactional
  public ResponseEntity<EventResponseDTO> createEvent(
          @RequestBody @Valid final EventRequestDTO body) {

    final User owner = getUserOrThrow(resolveUserId());

    final Event event =
            new Event(
                    body.getTitle(),
                    body.getDescription(),
                    body.getLocation(),
                    body.getCapacity(),
                    body.getDateTime(),
                    owner,
                    false);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(EventMapper.toDto(eventRepo.save(event)));
  }

  /**
   * Updates an event.
   *
   * @param id event ID
   * @param body updated event data
   * @return updated event DTO
   */
  @PutMapping("/{id}")
  public ResponseEntity<EventResponseDTO> updateEvent(
          @PathVariable final UUID id, @RequestBody final EventRequestDTO body) {

    final Event event = getEventOrThrow(id);
    event.updateFromDto(body);
    return ResponseEntity.ok(EventMapper.toDto(eventRepo.save(event)));
  }

  /**
   * Updates localized fields of an event.
   *
   * @param id event ID
   * @param lang language code
   * @param dto localized update data
   * @return localized event DTO
   */
  @PutMapping("/{id}/{lang}")
  @Transactional
  public ResponseEntity<EventResponseDTO> updateEventLocalized(
          @PathVariable final UUID id,
          @PathVariable final String lang,
          @RequestBody final EventLocalRequestDTO dto) {

    final Event event = getEventOrThrow(id);

    final EventTranslation translation =
            translationRepo
                    .getByRefEventAndLanguage(event, lang)
                    .orElseGet(
                            () -> {
                              final EventTranslation t = new EventTranslation();
                              t.setLanguage(lang);
                              t.setRefEvent(event);
                              return t;
                            });

    translation.updateFromDTO(dto);
    translationRepo.save(translation);

    return ResponseEntity.ok(EventMapper.toDto(event, translation));
  }

  /**
   * Approves an event.
   *
   * @param id event ID
   * @return approved event DTO
   */
  @PutMapping("/{id}/approve")
  @Transactional
  public ResponseEntity<EventResponseDTO> approveEvent(@PathVariable final UUID id) {
    final Event event = getEventOrThrow(id);
    event.setApproved(true);
    return ResponseEntity.ok(EventMapper.toDto(eventRepo.save(event)));
  }

  /**
   * Deletes an event.
   *
   * @param id event ID
   * @return no content
   */
  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<Void> deleteEvent(@PathVariable final UUID id) {
    final Event event = getEventOrThrow(id);
    attendanceRepo.deleteAttendancesByEvent_Id(id);
    eventRepo.delete(event);
    return ResponseEntity.noContent().build();
  }

  /**
   * Retrieves all approved events.
   *
   * @return list of approved event DTOs
   */
  @GetMapping("/approved")
  public ResponseEntity<List<EventResponseDTO>> getApprovedEvents() {
    final List<Event> events = eventRepo.findByApprovedTrue();
    if (events.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
    return ResponseEntity.ok(events.stream().map(EventMapper::toDto).toList());
  }

  /**
   * Retrieves localized approved events.
   *
   * @param lang language code
   * @return list of localized approved event DTOs
   */
  @GetMapping("/approved/{lang}")
  public ResponseEntity<List<EventResponseDTO>> getApprovedEventsLocalized(
          @PathVariable final String lang) {

    final List<Event> events = eventRepo.findByApprovedTrue();
    if (events.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    final List<EventResponseDTO> response =
            events.stream()
                    .map(
                            event ->
                                    translationRepo
                                            .getByRefEventAndLanguage(event, lang)
                                            .map(t -> EventMapper.toDto(event, t))
                                            .orElseGet(() -> EventMapper.toDto(event)))
                    .toList();

    return ResponseEntity.ok(response);
  }

  /**
   * Retrieves events owned by the authenticated user.
   *
   * @return list of owned event DTOs
   */
  @GetMapping("/admin")
  public ResponseEntity<List<EventResponseDTO>> getEventsByOwner() {
    final User owner = getUserOrThrow(resolveUserId());
    final List<Event> events = eventRepo.getEventsByEventOwner(owner);
    return ResponseEntity.ok(events.stream().map(EventMapper::toDto).toList());
  }

  /**
   * Retrieves a specific event owned by the authenticated user.
   *
   * @param id event ID
   * @return event DTO
   */
  @GetMapping("/admin/{id}")
  public ResponseEntity<EventResponseDTO> getEventByOwnerAndId(
          @PathVariable final UUID id) {

    final User owner = getUserOrThrow(resolveUserId());

    final Event event =
            eventRepo
                    .getEventByEventOwnerAndId(owner, id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            ERR_NOT_FOUND));

    return ResponseEntity.ok(EventMapper.toDto(event));
  }

  /**
   * Adds a new localization for an event owned by the authenticated user.
   *
   * @param id event ID
   * @param lang language code
   * @param dto localization data
   * @return OK response
   */
  @PostMapping("/admin/{id}/{lang}")
  @Transactional
  public ResponseEntity<Void> addEventLocalization(
          @PathVariable final UUID id,
          @PathVariable final String lang,
          @RequestBody final EventLocalRequestDTO dto) {

    final User owner = getUserOrThrow(resolveUserId());

    final Event event =
            eventRepo
                    .getEventByEventOwnerAndId(owner, id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            ERR_NOT_FOUND));

    if (translationRepo.existsByRefEventAndLanguage(event, lang)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Localization exists");
    }

    translationRepo.save(EventTranslation.createFromDTO(dto, event, lang));
    return ResponseEntity.ok().build();
  }
}
