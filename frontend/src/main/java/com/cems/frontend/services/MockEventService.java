package com.cems.frontend.services;



import com.cems.frontend.models.Event;
import com.cems.frontend.models.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class MockEventService {

    /**
     * Returns a list of test/mock events for the frontend.
     */
    public List<Event> getAllEvents() {
        // Dummy user as event owner
        User dummyOwner = new User();
        dummyOwner.setId(UUID.randomUUID());
        dummyOwner.setEmail("owner@example.com");
        dummyOwner.setAccessLevel(0);
        dummyOwner.setFirstName("John");
        dummyOwner.setMiddleName("M.");
        dummyOwner.setLastName("Doe");


        return List.of(
                new Event(
                        UUID.randomUUID(),
                        "Career Fair 2026",
                        "Meet top companies and explore internships.",
                        "Auditorium",
                        200,
                        Instant.parse("2026-02-10T12:00:00Z"),
                        true,
                        dummyOwner
                ),
                new Event(
                        UUID.randomUUID(),
                        "Tech Talk: AI",
                        "Learn about the latest AI trends in industry and academia.",
                        "Room 101",
                        100,
                        Instant.parse("2026-03-05T14:00:00Z"),
                        true,
                        dummyOwner
                ),
                new Event(
                        UUID.randomUUID(),
                        "Music Festival",
                        "Outdoor music event with local bands and food trucks.",
                        "Open Stage",
                        500,
                        Instant.parse("2026-04-20T16:00:00Z"),
                        true,
                        dummyOwner
                )
        );
    }
}
