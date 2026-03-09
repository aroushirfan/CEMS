package com.cems.frontend.models;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void setAndGetFields() {
        User u = new User();

        UUID id = UUID.randomUUID();
        u.setId(id);
        u.setEmail("test@mail.com");
        u.setAccessLevel(2);
        u.setFirstName("Sam");
        u.setMiddleName("T.");
        u.setLastName("Lee");

        assertEquals(id, u.getId());
        assertEquals("test@mail.com", u.getEmail());
        assertEquals(2, u.getAccessLevel());
        assertEquals("Sam", u.getFirstName());
        assertEquals("T.", u.getMiddleName());
        assertEquals("Lee", u.getLastName());
    }

    @Test
    void propertyValuesChange() {
        User u = new User();

        u.emailProperty().set("hello@mail.com");
        assertEquals("hello@mail.com", u.getEmail());

        u.accessLevelProperty().set(5);
        assertEquals(5, u.getAccessLevel());

        u.firstNameProperty().set("Mia");
        assertEquals("Mia", u.getFirstName());
    }

    @Test
    void constructorWorks() {
        UUID id = UUID.randomUUID();

        User u = new User(
                id,
                "abc@mail.com",
                1,
                "John",
                "Q",
                "Doe"
        );

        assertEquals(id, u.getId());
        assertEquals("abc@mail.com", u.getEmail());
        assertEquals(1, u.getAccessLevel());
        assertEquals("John", u.getFirstName());
        assertEquals("Q", u.getMiddleName());
        assertEquals("Doe", u.getLastName());
    }
}