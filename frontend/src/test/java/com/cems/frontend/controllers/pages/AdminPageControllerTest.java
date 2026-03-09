package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AdminPageControllerTest {

    @Test
    void searchFiltering() {
        ObservableList<Event> events = FXCollections.observableArrayList(
                new Event(UUID.randomUUID(), "Metropolia Gala", "Desc", "Helsinki", 100, Instant.now(), true, null),
                new Event(UUID.randomUUID(), "Java Workshop", "Desc", "Espoo", 50, Instant.now(), false, null)
        );

        String filter = "helsinki";

        var filtered = events.filtered(e ->
                e.getTitle().toLowerCase().contains(filter) ||
                        e.getLocation().toLowerCase().contains(filter) ||
                        e.getDescription().toLowerCase().contains(filter)
        );

        assertEquals(1, filtered.size());
        assertEquals("Metropolia Gala", filtered.get(0).getTitle());
    }

    @Test
    void eventProps() {
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

    @Test
    void dateFormat() {
        Instant now = Instant.parse("2024-01-01T10:00:00Z");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formatted = formatter.format(now.atZone(ZoneId.systemDefault()));

        assertNotNull(formatted);
        assertTrue(formatted.contains("2024"));
    }
}