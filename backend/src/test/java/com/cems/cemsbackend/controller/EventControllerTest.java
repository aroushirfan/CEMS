package com.cems.cemsbackend.controller;

import com.cems.shared.model.EventDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void createEvent() {
        fail("Test not implemented");
    }

    @Test
    void updateEvent() {
        fail("Test not implemented");
    }

    @Test
    void deleteEvent() {
        fail("Test not implemented");
    }
}