package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.mappers.EventMapper;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.EventTranslation;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.AttendanceRepository;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.repository.EventTranslationRepository;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.shared.model.EventDto.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final EventTranslationRepository eventTranslationRepository;

    public EventController(EventRepository eventRepository, UserRepository userRepository, AttendanceRepository attendanceRepository, EventTranslationRepository eventTranslationRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        this.eventTranslationRepository = eventTranslationRepository;
    }

    // get all
    // GET /events/all
    @GetMapping()
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        try {
            List<Event> events = eventRepository.findAll();
            if (events.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            List<EventResponseDTO> response = events.stream()
                    .map(EventMapper::toDto)
                    .toList();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to fetch events");
        }
    }

    // get all (localized)
    // GET /events/all/{lang}
    @GetMapping(path = "all/{lang}")
    public ResponseEntity<List<EventResponseDTO>> getAllEventsLocal(
            @PathVariable String lang
    ) {
        try {
            List<Event> events = eventRepository.findAll();
            if (events.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            List<EventResponseDTO> response = events.stream()
                    .map(event -> {
                        var translationOpt = eventTranslationRepository.getByRefEventAndLanguage(event, lang);
                        return translationOpt.map(eventTranslation -> EventMapper.toDto(event, eventTranslation)).orElseGet(() -> EventMapper.toDto(event));
                    })
                    .toList();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to fetch events");
        }
    }

    // get by id
    // GET /events/{id}
    @GetMapping(path = "/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(
            @PathVariable UUID id
    ) {
        try {
            var event = eventRepository.findById(id).orElse(null);
            if (event == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(EventMapper.toDto(event));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to fetch event");
        }
    }

    // get by id (localized)
    // GET /events/{id}/{lang}
    @GetMapping(path = "/{id}/{lang}")
    public ResponseEntity<EventResponseDTO> getEventByIdLocal(
            @PathVariable UUID id,
            @PathVariable String lang
    ) {
        try {
            var event = eventRepository.getEventById(id).orElse(null);
            if (event == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
            }
            var translation = eventTranslationRepository.getByRefEventAndLanguage(event, lang).orElse(null);
            if (translation == null) {
                return ResponseEntity.ok(EventMapper.toDto(event));
            } else {
                return ResponseEntity.ok(EventMapper.toDto(event, translation));
            }
        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw e;
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // create an event
    // POST /events
    @PostMapping
    @Transactional
    public ResponseEntity<EventResponseDTO> createEvent(
            @RequestBody @Valid EventRequestDTO body
    ) {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "");
            }
            Optional<User> userOpt = Optional.empty();
            try {
                userOpt = userRepository.getUserById((UUID) auth.getPrincipal());
            } catch (Exception ignored) {
            }
            if (userOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User ID");
            }
            User user = userOpt.get();
            Event event = eventRepository.save(new Event(
                    body.getTitle(),
                    body.getDescription(),
                    body.getLocation(),
                    body.getCapacity(),
                    body.getDateTime(),
                    user,
                    false
            ));
//            public EventResponseDTO(UUID id, String title, String description, String location, long capacity, Instant dateTime, boolean approved)
            return ResponseEntity.status(HttpStatus.CREATED).body(new EventResponseDTO(
                    event.getId(),
                    event.getTitle(),
                    event.getDescription(),
                    event.getLocation(),
                    event.getCapacity(),
                    event.getDateTime(),
                    event.isApproved()
            ));
        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw e;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
    }

    // update an event
    // PUT /events/{id}
    @PutMapping(path = "/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable String id,
            @RequestBody EventRequestDTO body
    ) {
        try {
            Optional<Event> eventOpt = eventRepository.getEventById(UUID.fromString(id));
            if (eventOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with id not found");
            }
            Event event = eventOpt.get();
            event.updateFromDto(body);
            Event eventResult = eventRepository.save(event);
            return ResponseEntity.ok(new EventResponseDTO(
                    eventResult.getId(),
                    eventResult.getTitle(),
                    eventResult.getDescription(),
                    eventResult.getLocation(),
                    eventResult.getCapacity(),
                    eventResult.getDateTime(),
                    eventResult.isApproved()
            ));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Id");
        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw e;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
    }

    @PutMapping(path = "/{id}/{lang}")
    @Transactional
    public ResponseEntity<EventResponseDTO> updateEventLocal(
            @PathVariable String lang,
            @PathVariable String id,
            @RequestBody EventLocalRequestDTO body
    ) {
        try {
            Optional<Event> eventOpt = eventRepository.getEventById(UUID.fromString(id));
            if (eventOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with id not found");
            }
            Event event = eventOpt.get();
            Optional<EventTranslation> eventTranslationOptional = eventTranslationRepository.getByRefEventAndLanguage(event, lang);
            EventResponseDTO response;
            EventTranslation eventTranslate;
            if (eventTranslationOptional.isEmpty()) {
                eventTranslate = new EventTranslation();
                eventTranslate.setLanguage(lang);
                eventTranslate.setRefEvent(event);
            } else {
                eventTranslate = eventTranslationOptional.get();
            }
            eventTranslate.updateFromDTO(body);
            response = EventMapper.toDto(event, eventTranslationRepository.save(eventTranslate));
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Id");
        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw e;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
    }

    //approve an event
    @PutMapping(path = "/{id}/approve")
    @Transactional
    public ResponseEntity<EventResponseDTO> approveEvent(@PathVariable String id) {
        try {
            UUID eventId;
            try {
                eventId = UUID.fromString(id);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Event ID");
            }
            var eventOpt = eventRepository.findById(eventId);
            if (eventOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with id not found");
            }
            Event event = eventOpt.get();
            event.setApproved(true);
            Event saved = eventRepository.save(event);
            return ResponseEntity.ok(EventMapper.toDto(saved));
        } catch (Exception e) {
            if (e instanceof ResponseStatusException) throw e;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // delete an event
    // DELETE /events/{id}
    @DeleteMapping(path = "/{id}")
    @Transactional
    public ResponseEntity<?> deleteEvent(
            @PathVariable String id
    ) {
        Optional<Event> eventOpt;
        try {
            eventOpt = eventRepository.getEventById(UUID.fromString(id));
            if (eventOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with id not found");
            }
            attendanceRepository.deleteAttendancesByEvent_Id(UUID.fromString(id));
            eventRepository.delete(eventOpt.get());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Id");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
    @GetMapping("/approved")
    public ResponseEntity<List<EventResponseDTO>> getApprovedEvents() {
        try {
            List<Event> events = eventRepository.findByApprovedTrue();
            if (events.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            List<EventResponseDTO> response = events.stream()
                    .map(EventMapper::toDto)
                    .toList();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to fetch approved events");
        }
    }

    @GetMapping("/approved/{lang}")
    public ResponseEntity<List<EventResponseDTO>> getLocalApprovedEvents(
            @PathVariable String lang
    ) {
        try {
            List<Event> events = eventRepository.findByApprovedTrue();
            if (events.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            List<EventResponseDTO> response = events.stream()
                    .map(event -> {
                        var translationOpt = eventTranslationRepository.getByRefEventAndLanguage(event, lang);
                        return translationOpt.map(eventTranslation -> {
                            return EventMapper.toDto(event, eventTranslation);
                        }).orElseGet(() -> EventMapper.toDto(event));
                    })
                    .toList();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to fetch approved events");
        }
    }

    // get event by owner
    // GET /events/admin
    @GetMapping(path = "/admin")
    public ResponseEntity<List<EventResponseDTO>> getEventByOwner() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not authenticated");
            }
            Optional<User> userOpt = userRepository.getUserById((UUID) authentication.getPrincipal());
            if (userOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not found");
            }
            var events = eventRepository.getEventsByEventOwner(userOpt.get());
            return ResponseEntity.ok(events.stream().map(EventMapper::toDto).toList());
        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw e;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
    }

    // get event by owner and id
    // GET /events/admin/{id}
    @GetMapping(path = "/admin/{id}")
    public ResponseEntity<EventResponseDTO> getEventByOwnerAndId(
            @PathVariable String id
    ) {
        try {
            UUID eventId;
            try {
                eventId = UUID.fromString(id);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event ID is invalid");
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not authenticated");
            }
            var userId = (UUID) authentication.getPrincipal();
            Optional<User> userOpt = userRepository.getUserById(userId);
            if (userOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not found");
            }
            var event = eventRepository.getEventByEventOwnerAndId(userOpt.get(), eventId);
            if (event.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with ID not found");
            }
            return ResponseEntity.ok(EventMapper.toDto(event.get()));
        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw e;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }

    }

    @PostMapping(path = "/admin/{id}/{lang}")
    @Transactional
    public ResponseEntity<?> addEventLocalization(
            @PathVariable String lang,
            @PathVariable String id,
            @RequestBody EventLocalRequestDTO body
    ) {
        try {
            UUID eventId;
            try {
                eventId = UUID.fromString(id);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event ID is invalid");
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not authenticated");
            }
            var userId = (UUID) authentication.getPrincipal();
            Optional<User> userOpt = userRepository.getUserById(userId);
            if (userOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not found");
            }
            Optional<Event> event = eventRepository.getEventByEventOwnerAndId(userOpt.get(), eventId);
            if (event.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with ID not found");
            }
            if (eventTranslationRepository.existsByRefEventAndLanguage(event.get(), lang)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Localization exists");
            }
            eventTranslationRepository.save(EventTranslation.createFromDTO(body, event.get(), lang));
        } catch (Exception e) {
            if (e instanceof ResponseStatusException) {
                throw e;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
        return ResponseEntity.ok(null);
    }

}
