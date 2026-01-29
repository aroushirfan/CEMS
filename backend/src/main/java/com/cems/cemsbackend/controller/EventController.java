package com.cems.cemsbackend.controller;

import com.cems.shared.model.EventDto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    // get all
    // GET /events
    @GetMapping()
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    // get by id
    // GET /events/{id}
    @GetMapping(path = "/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(
            @PathVariable String id
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    // create an event
    // POST /events
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(
            @RequestBody @Valid EventRequestDTO body
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    // update an event
    // PUT /events/{id}
    @PutMapping(path = "/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable String id
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    // delete an event
    // DELETE /events/{id}
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteEvent (
            @PathVariable String id
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
