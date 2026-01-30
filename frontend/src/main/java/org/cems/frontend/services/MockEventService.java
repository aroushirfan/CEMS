package org.cems.frontend.services;

import org.cems.frontend.models.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class MockEventService implements IEventService {
    @Override
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();

        // Creating a fake user to act as the Event Owner
        User admin = new User(UUID.randomUUID(), "admin@cems.com", AccessLevel.ADMIN);
        User faculty = new User(UUID.randomUUID(), "professor@metropolia.fi", AccessLevel.FACULTY);

        // Generating a fake list of varied events
        events.add(new Event(UUID.randomUUID(), "JavaFX Mastery Workshop", Instant.now().plus(1, ChronoUnit.DAYS), true, admin));
        events.add(new Event(UUID.randomUUID(), "Networking Mixer", Instant.now().plus(3, ChronoUnit.DAYS), false, faculty));
        events.add(new Event(UUID.randomUUID(), "Spring Boot Deep Dive", Instant.now().plus(5, ChronoUnit.DAYS), true, admin));
        events.add(new Event(UUID.randomUUID(), "UI/UX Design Basics", Instant.now().plus(7, ChronoUnit.DAYS), true, faculty));
        events.add(new Event(UUID.randomUUID(), "CEMS Project Sync", Instant.now().minus(1, ChronoUnit.DAYS), true, admin));
        events.add(new Event(UUID.randomUUID(), "Tech Career Fair", Instant.now().plus(10, ChronoUnit.DAYS), false, admin));

        return events;
    }
}