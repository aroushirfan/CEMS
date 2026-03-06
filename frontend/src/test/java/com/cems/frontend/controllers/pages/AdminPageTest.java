package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AdminPageControllerLogicTest {

    @Test
    void testSearchFiltering() {
        ObservableList<Event> events = FXCollections.observableArrayList(
                new Event(UUID.randomUUID(), "Metropolia Gala", "Desc", "Helsinki", 100, Instant.now(), true, null),
                new Event(UUID.randomUUID(), "Java Workshop", "Desc", "Espoo", 50, Instant.now(), false, null)
        );

        String filter = "helsinki";

        List<Event> filtered = events.filtered(e ->
                e.getTitle().toLowerCase().contains(filter) ||
                        e.getLocation().toLowerCase().contains(filter) ||
                        e.getDescription().toLowerCase().contains(filter)
        );

        assertEquals(1, filtered.size());
        assertEquals("Metropolia Gala", filtered.get(0).getTitle());
    }

    @Test
    void testEventModelProperties() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        Event e = new Event(id, "Test", "Desc", "Espoo", 10, now, true, null);

        assertEquals(id, e.getId());
        assertEquals("Test", e.getTitle());
        assertEquals("Espoo", e.getLocation());
        assertEquals(10, e.getCapacity());
        assertEquals(now, e.getDateTime());
        assertTrue(e.isApproved());
    }
}