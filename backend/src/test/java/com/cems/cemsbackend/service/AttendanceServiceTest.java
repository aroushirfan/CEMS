package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.Attendance;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.AttendanceRepository;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // Rolls back changes so the "fake" database stays clean
class AttendanceServiceTest {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    private User testUser;
    private Event testEvent;

    @BeforeEach
    void setUp() throws Exception {
        // 1. Create real User with ALL required fields
        User user = new User();
        user.setEmail("student@metropolia.fi");
        user.setFirstName("Jeena");
        user.setLastName("Sen"); // satisfying potential null constraint

        // IMPORTANT: Matches the 'hashed_password' column in your DB sidebar
        // If this method name is red, try user.setPassword("Sailesh1103")
        user.setHashedPassword("Sailesh1103");

        testUser = userRepository.saveAndFlush(user);

        // 2. Create real Event using the 7-argument constructor
        testEvent = new Event("Lab", "Desc", "Helsinki", 10L, Instant.now(), testUser, true);

        // 3. Initialize attendee list using reflection
        Field field = Event.class.getDeclaredField("attendees");
        field.setAccessible(true);
        field.set(testEvent, new ArrayList<>());

        // 4. Satisfy your 'Gatekeeper' logic
        testEvent.addAttendee(testUser);
        testEvent = eventRepository.saveAndFlush(testEvent);
    }

    @Test
    void testCheckInSuccess() {
        Attendance result = attendanceService.createCheckIn(testUser, testEvent);
        assertNotNull(result);
        assertEquals("PRESENT", result.getStatus());
    }

    @Test
    void testCheckIn_Fail_NotRSVPd() {
        // 1. Create the stranger object
        User userObj = new User();
        userObj.setEmail("stranger@metropolia.fi");
        userObj.setFirstName("Not");
        userObj.setLastName("Invited");
        userObj.setHashedPassword("secure123");

        // 2. Use a different variable name for the saved version to keep it final
        final User stranger = userRepository.saveAndFlush(userObj);
        final Event currentEvent = testEvent;

        // Assert that the service throws 403 Forbidden
        org.springframework.web.server.ResponseStatusException exception =
                assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
                    // stranger and currentEvent are now final
                    attendanceService.createCheckIn(stranger, currentEvent);
                });

        assertEquals(org.springframework.http.HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void testCheckIn_Fail_Duplicate() {
        // Make variables final for the lambda
        final User user = testUser;
        final Event event = testEvent;

        // First check-in works
        attendanceService.createCheckIn(user, event);

        // Second check-in must throw 409 Conflict
        org.springframework.web.server.ResponseStatusException exception =
                assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
                    attendanceService.createCheckIn(user, event);
                });

        assertEquals(org.springframework.http.HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void testGetAttendanceByEvent() {
        // 1. Perform a successful check-in first
        attendanceService.createCheckIn(testUser, testEvent);

        // 2. Call the retrieval method
        List<Attendance> list = attendanceService.getAttendanceByEvent(testEvent);

        // 3. Assertions
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(testUser.getId(), list.get(0).getUser().getId());
    }
}