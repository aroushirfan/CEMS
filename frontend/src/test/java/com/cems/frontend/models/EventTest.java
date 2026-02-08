package com.cems.frontend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
    }

    @Test
    void testPropertyChangeNotification() {
        // This simulates a UI Label or Field listening for changes
        final String[] result = new String[1];
        event.titleProperty().addListener((obs, oldVal, newVal) -> {
            result[0] = newVal;
        });

        // Use the setter to change the value
        event.setTitle("Metropolia Gala");

        // Verify the listener was triggered and the getter works
        assertEquals("Metropolia Gala", result[0], "The property listener should have detected the change");
        assertEquals("Metropolia Gala", event.getTitle(), "The getter should return the updated value");
    }

    @Test
    void testCapacityProperty() {
        event.setCapacity(150L);
        assertEquals(150L, event.getCapacity(), "LongProperty for capacity should update correctly");
    }
}