package com.cems.frontend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NavigationMementoTest {
    NavigationMemento navigationMemento;
    @BeforeEach
    void setUp() {
        navigationMemento = new NavigationMemento(Paths.HOME,"test payload");
    }

    @Test
    void testGetPath() {
        assertEquals(Paths.HOME,navigationMemento.getPath());
    }

    @Test
    void testGetPayload() {
        assertEquals("test payload",navigationMemento.getPayload(String.class));
        assertNull(navigationMemento.getPayload(Event.class));
    }
}