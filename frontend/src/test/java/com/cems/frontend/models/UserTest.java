package com.cems.frontend.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void setAndGetFields() {
        User u = new User();

        UUID id = UUID.randomUUID();
        LocalDate dob = LocalDate.of(1999, 12, 31);
        u.setId(id);
        u.setEmail("test@mail.com");
        u.setAccessLevel(2);
        u.setFirstName("Sam");
        u.setMiddleName("T.");
        u.setLastName("Lee");
        u.setPhone("+35840111222");
        u.setDob(dob);
        u.setLanguage("en");
        u.setProfileImageUrl("https://example.com/profile.png");

        assertEquals(id, u.getId());
        assertEquals("test@mail.com", u.getEmail());
        assertEquals(2, u.getAccessLevel());
        assertEquals("Sam", u.getFirstName());
        assertEquals("T.", u.getMiddleName());
        assertEquals("Lee", u.getLastName());
        assertEquals("+35840111222", u.getPhone());
        assertEquals(dob, u.getDob());
        assertEquals("en", u.getLanguage());
        assertEquals("https://example.com/profile.png", u.getProfileImageUrl());
    }

    @Test
    void propertyValuesChange() {
        User u = new User();
        LocalDate dob = LocalDate.of(2000, 1, 1);

        u.emailProperty().set("hello@mail.com");
        assertEquals("hello@mail.com", u.getEmail());

        u.accessLevelProperty().set(5);
        assertEquals(5, u.getAccessLevel());

        u.firstNameProperty().set("Mia");
        assertEquals("Mia", u.getFirstName());

        u.phoneProperty().set("+35844999888");
        assertEquals("+35844999888", u.getPhone());

        u.dobProperty().set(dob);
        assertEquals(dob, u.getDob());

        u.languageProperty().set("fi");
        assertEquals("fi", u.getLanguage());

        u.profileImageUrlProperty().set("https://example.com/new-profile.png");
        assertEquals("https://example.com/new-profile.png", u.getProfileImageUrl());
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