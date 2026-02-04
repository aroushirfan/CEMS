package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.shared.model.EventDto.EventRequestDTO;
import com.cems.shared.model.EventDto.EventResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class EventControllerTest {
    private final EventRepository eventRepository = Mockito.mock(EventRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final EventController eventController = new EventController(eventRepository, userRepository);

    //POST
    @Test
    public void testCreateEvent() {
        EventRequestDTO req = new EventRequestDTO(
                "Sample Event",
                "This is a sample event description.",
                "Helsinki",
                100L,
                Instant.now()
        );
        User fakeUser = new User("test@example.com", "hashed", "refresh");
        Event savedEvent = new Event(req.getTitle(), req.getDescription(), req.getLocation(), req.getCapacity(), req.getDateTime(), fakeUser, false);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(fakeUser);
        Mockito.when(eventRepository.save(Mockito.any(Event.class))).thenReturn(savedEvent);
        ResponseEntity<EventResponseDTO> res = eventController.createEvent(req);
        Assertions.assertEquals(201, res.getStatusCode().value());
        Assertions.assertNotNull(res.getBody());
    }

    //PUT
    @Test
    public void testUpdateEvent() {

        UUID id = UUID.randomUUID();

        Event existing = new Event(
                "Old Title",
                "Old Desc",
                "Old Loc",
                50L,
                Instant.now(),
                new User("old@example.com", "hashed", "refresh"),
                false
        );

        EventRequestDTO update = new EventRequestDTO(
                "New Title",
                "New Desc",
                "New Loc",
                200L,
                Instant.now()
        );

        Mockito.when(eventRepository.getEventById(id)).thenReturn(Optional.of(existing));
        Mockito.when(eventRepository.save(Mockito.any(Event.class))).thenReturn(existing);

        ResponseEntity<EventResponseDTO> res = eventController.updateEvent(id.toString(), update);

        Assertions.assertEquals(200, res.getStatusCode().value());
        Assertions.assertNotNull(res.getBody());
    }

    //DELETE
    @Test
    public void testDeleteEvent() {

        UUID id = UUID.randomUUID();

        Event event = new Event(
                "Title",
                "Desc",
                "Loc",
                10L,
                Instant.now(),
                new User("owner@example.com", "hashed", "refresh"),
                false
        );

        Mockito.when(eventRepository.deleteEventById(id)).thenReturn(Optional.of(event));

        ResponseEntity<?> res = eventController.deleteEvent(id.toString());

        Assertions.assertEquals(204, res.getStatusCode().value());
    }
}