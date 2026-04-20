package com.cems.frontend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    private Event event;
    private User owner;

    @BeforeEach
    void setUp() {
        event = new Event();
        owner = new User();
        owner.setFirstName("Owner");
    }

    @Test
    void testCorePropertiesAndGettersSetters() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        event.setId(id);
        event.setTitle("Metropolia Gala");
        event.setDescription("Annual celebration");
        event.setLocation("Helsinki");
        event.setCapacity(500L);
        event.setDateTime(now);
        event.setApproved(true);

        // Assert
        assertEquals(id, event.getId());
        assertEquals("Metropolia Gala", event.getTitle());
        assertEquals("Annual celebration", event.getDescription());
        assertEquals("Helsinki", event.getLocation());
        assertEquals(500L, event.getCapacity());
        assertEquals(now, event.getDateTime());
        assertTrue(event.isApproved());
    }

    @Test
    void testDynamicPropertyBindings() {

        final String[] titleResult = new String[1];
        event.titleProperty().addListener((obs, oldV, newV) -> titleResult[0] = newV);
        event.setTitle("Updated Title");
        assertEquals("Updated Title", titleResult[0], "TitleProperty listener should fire");

        // LongProperty Binding
        event.setCapacity(100L);
        assertEquals(100L, event.capacityProperty().get(), "CapacityProperty should hold value");

        // BooleanProperty Binding
        event.setApproved(false);
        assertFalse(event.approvedProperty().get(), "ApprovedProperty should hold value");

        // ObjectProperty (Instant) Binding
        Instant time = Instant.now();
        event.setDateTime(time);
        assertEquals(time, event.dateTimeProperty().get(), "DateTimeProperty should hold value");
    }

    @Test
    void testConstructorWithArguments() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        Event fullEvent = new Event(id, "Title", "Desc", "Loc", 50L, now, true, owner);

        assertAll("Verify all constructor arguments match",
                () -> assertEquals(id, fullEvent.getId()),
                () -> assertEquals("Desc", fullEvent.getDescription()),
                () -> assertEquals("Loc", fullEvent.getLocation()),
                () -> assertEquals("Title", fullEvent.getTitle()),
                () -> assertEquals(50L, fullEvent.getCapacity()),
                () -> assertEquals(now, fullEvent.getDateTime()),
                () -> assertEquals(owner, fullEvent.eventOwnerProperty().get()),
                () -> assertTrue(fullEvent.isApproved())
        );
    }

    @Test
    void testEventOwnerProperty() {
        event.setEventOwner(owner);
        assertEquals(owner, event.eventOwnerProperty().get());
    }
}