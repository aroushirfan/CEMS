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
import com.cems.shared.model.EventDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventControllerTest {

    @Autowired
    public EventController controller;
    public EventDto.EventResponseDTO firstEventResponse;

    @BeforeAll
    void testContext() {
        assertNotNull(controller);
        for (int i = 0; i < 10; i++) {
            EventDto.EventRequestDTO eventRequestDTO = new EventDto.EventRequestDTO(
                    String.format("test%d", i),
                    String.format("description%d", i),
                    String.format("location%d", i),
                    (long) i,
                    Instant.now()
            );
            if (i == 0) {
                firstEventResponse = controller.createEvent(eventRequestDTO).getBody();
            } else {
                controller.createEvent(eventRequestDTO);
            }
        }
    }

    @Test
    @DisplayName("GET /events Should return all events")
    void getAllEvents() {
        assertEquals(10, controller.getAllEvents().getBody().toArray().length);
    }

    @Test
    @DisplayName("GET /events/{id} should return an event with the ID or 404 if not found")
    void getEventById() {
        assertEquals(firstEventResponse, controller.getEventById(firstEventResponse.getId()).getBody());
        assertEquals(HttpStatus.NOT_FOUND, controller.getEventById(UUID.randomUUID()).getStatusCode());
    }
}