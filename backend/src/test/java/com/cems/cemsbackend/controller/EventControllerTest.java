package com.cems.cemsbackend.controller;

import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.AttendanceRepository;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.repository.UserRepository;

import com.cems.shared.model.EventDto.EventRequestDTO;
import com.cems.shared.model.EventDto.EventResponseDTO;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class EventControllerTest {

  @Autowired
  public EventController controller;

  @Autowired
  public UserRepository userRepository;

  @Autowired
  public EventRepository eventRepository;

  @Autowired
  public AttendanceRepository attendanceRepository;

  public EventResponseDTO firstEventResponse;
  public EventResponseDTO secondEventResponse;
  public Authentication auth;

  @BeforeEach
  void testContext() {

    // ⭐ FIX: Delete attendance FIRST (FK constraint)
    attendanceRepository.deleteAll();
    eventRepository.deleteAll();
    userRepository.deleteAll();

    SecurityContextHolder.clearContext();

    assertNotNull(controller);

    // Create test user
    User user = new User();
    user.setEmail("email@example.com");
    user.setHashedPassword("hashed");
    user.setAccessLevel(1);
    user.setFirstName("first");

    User userEntity = userRepository.save(user);

    // Mock authentication
    auth = new UsernamePasswordAuthenticationToken(
            userEntity.getId(),
            null,
            List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
    );

    SecurityContextHolder.getContext().setAuthentication(auth);

    // Create initial events
    for (int i = 0; i < 10; i++) {
      EventRequestDTO eventRequestDTO =
              new EventRequestDTO(
                      "test" + i,
                      "description" + i,
                      "location" + i,
                      (long) i,
                      Instant.now()
              );

      if (i == 0) {
        firstEventResponse = controller.createEvent(eventRequestDTO).getBody();
      } else {
        controller.createEvent(eventRequestDTO);
      }
    }

    EventRequestDTO secondReq = new EventRequestDTO(
            "Second Event",
            "Event used for update/delete tests",
            "Test Location",
            50L,
            Instant.now()
    );

    secondEventResponse = controller.createEvent(secondReq).getBody();
    assertNotNull(secondEventResponse);
  }

  @Test
  @DisplayName("GET /events Should return all events")
  void getAllEvents() {
    assertEquals(11, controller.getAllEvents().getBody().size());
  }

  @Test
  @DisplayName("GET /events/{id} should return an event or 404")
  void getEventById() {
    var fetched = controller.getEventById(firstEventResponse.getId()).getBody();

    assertNotNull(fetched);
    assertEquals(firstEventResponse.getId(), fetched.getId());
    assertEquals(firstEventResponse.getTitle(), fetched.getTitle());
    assertEquals(firstEventResponse.getDescription(), fetched.getDescription());
    assertEquals(firstEventResponse.getLocation(), fetched.getLocation());
    assertEquals(firstEventResponse.getCapacity(), fetched.getCapacity());

    assertEquals(
            HttpStatus.NOT_FOUND,
            controller.getEventById(UUID.randomUUID()).getStatusCode()
    );
  }

  @Test
  @DisplayName("POST /events should create a new event")
  void createEvent() {
    EventRequestDTO req = new EventRequestDTO(
            "Sample Event",
            "This is a sample event description.",
            "Helsinki",
            100L,
            Instant.now()
    );

    var res = controller.createEvent(req);

    assertEquals(201, res.getStatusCode().value());
    assertNotNull(res.getBody());
  }

  @Test
  @DisplayName("PUT /events/{id} should update an existing event")
  void updateEvent() {
    EventRequestDTO update = new EventRequestDTO(
            "Updated Title",
            "Updated Desc",
            "Updated Loc",
            200L,
            Instant.now()
    );

    var res = controller.updateEvent(
            secondEventResponse.getId().toString(),
            update
    );

    assertEquals(200, res.getStatusCode().value());
    assertNotNull(res.getBody());
    assertEquals("Updated Title", res.getBody().getTitle());
  }

  @Test
  @DisplayName("DELETE /events/{id} should delete an existing event")
  void deleteEvent() {
    var res = controller.deleteEvent(secondEventResponse.getId().toString());
    assertEquals(204, res.getStatusCode().value());
  }

  @Test
  @DisplayName("GET non-existing event should return 404")
  void getNonExistingEvent() {
    var res = controller.getEventById(UUID.randomUUID());
    assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
  }

  @Test
  @DisplayName("PUT non-existing event should return 404")
  void updateNonExistingEvent() {
    EventRequestDTO update = new EventRequestDTO(
            "Updated",
            "Desc",
            "Loc",
            10L,
            Instant.now()
    );

    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
            controller.updateEvent(UUID.randomUUID().toString(), update)
    );

    assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    assertEquals("Event with id not found", ex.getReason());
  }

  @Test
  @DisplayName("DELETE non-existing event should return 404")
  void deleteNonExistingEvent() {
    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
            controller.deleteEvent(UUID.randomUUID().toString())
    );

    assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    assertEquals("Event with id not found", ex.getReason());
  }
}
