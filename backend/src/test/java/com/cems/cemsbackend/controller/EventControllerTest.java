package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.repository.UserRepository;
import com.cems.shared.model.EventDto.EventRequestDTO;
import com.cems.shared.model.EventDto.EventResponseDTO;
import com.cems.shared.model.EventDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EventControllerTest {
    @Autowired
    public EventController controller;
    public EventResponseDTO firstEventResponse;
    public EventResponseDTO secondEventResponse;

    @Autowired
    public UserRepository userRepository;
    @Autowired
    public EventRepository eventRepository;
    public Authentication auth;

    @BeforeEach
    void testContext() {
        eventRepository.deleteAll();
        userRepository.deleteAll();
        assertNotNull(controller);
        User user = new User("test", "test", 2, "first", null, null);
        User userEntity = userRepository.save(user);
        auth = new UsernamePasswordAuthenticationToken(userEntity.getId(), null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);
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
        EventRequestDTO secondReq = new EventRequestDTO("Second Event", "Event used for update/delete tests", "Test Location", 50L, Instant.now());
        secondEventResponse = controller.createEvent(secondReq).getBody();
        assertNotNull(secondEventResponse);
    }

    @Test
    @DisplayName("GET /events Should return all events")
    void getAllEvents() {
        assertEquals(11, controller.getAllEvents().getBody().toArray().length);
    }

    @Test
    @DisplayName("GET /events/{id} should return an event with the ID or 404 if not found")
    void getEventById() {
        var fetched = controller.getEventById(firstEventResponse.getId()).getBody();
        assertNotNull(fetched);
        assertEquals(firstEventResponse.getId(), fetched.getId());
        assertEquals(firstEventResponse.getTitle(), fetched.getTitle());
        assertEquals(firstEventResponse.getDescription(), fetched.getDescription());
        assertEquals(firstEventResponse.getLocation(), fetched.getLocation());
        assertEquals(firstEventResponse.getCapacity(), fetched.getCapacity());
        assertEquals(HttpStatus.NOT_FOUND, controller.getEventById(UUID.randomUUID()).getStatusCode());
    }

    // POST
    @Test
    @DisplayName("POST /events should create a new event")
    void createEvent() {
        EventRequestDTO req = new EventRequestDTO("Sample Event", "This is a sample event description.", "Helsinki", 100L, Instant.now());
        var res = controller.createEvent(req);
        assertEquals(201, res.getStatusCode().value());
        assertNotNull(res.getBody());
    }

    // PUT
    @Test
    @DisplayName("PUT /events/{id} should update an existing event.")
    void updateEvent() {
        EventRequestDTO update = new EventRequestDTO("Updated Title", "Updated Desc", "Updated Loc", 200L, Instant.now());
        var res = controller.updateEvent(secondEventResponse.getId().toString(), update);
        assertEquals(200, res.getStatusCode().value());
        assertNotNull(res.getBody());
        assertEquals("Updated Title", res.getBody().getTitle());
    }

    //DELETE
    @Test
    @DisplayName("DELETE /events/{id} should delete an existing event.")
    void deleteEvent() {
        var res = controller.deleteEvent(secondEventResponse.getId().toString());
        assertEquals(204, res.getStatusCode().value());
    }
}