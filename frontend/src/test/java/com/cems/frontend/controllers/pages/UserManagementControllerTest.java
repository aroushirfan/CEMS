package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserManagementControllerTest {

    @Test
    void searchFiltering() {
        ObservableList<User> users = FXCollections.observableArrayList(
                new User(UUID.randomUUID(), "alice@mail.com", 0, "Alice", "", "Stone"),
                new User(UUID.randomUUID(), "bob@mail.com", 1, "Bob", "", "Smith"),
                new User(UUID.randomUUID(), "charlie@mail.com", 2, "Charlie", "", "Brown")
        );

        String filter = "bob";

        var filtered = users.filtered(u ->
                u.getEmail().toLowerCase().contains(filter) ||
                        u.getFirstName().toLowerCase().contains(filter) ||
                        u.getLastName().toLowerCase().contains(filter)
        );

        assertEquals(1, filtered.size());
        assertEquals("bob@mail.com", filtered.get(0).getEmail());
    }

    @Test
    void roleButtons() {
        User u0 = new User(UUID.randomUUID(), "a@mail.com", 0, "A", "", "A");
        User u1 = new User(UUID.randomUUID(), "b@mail.com", 1, "B", "", "B");
        User u2 = new User(UUID.randomUUID(), "c@mail.com", 2, "C", "", "C");

        assertEquals(0, u0.getAccessLevel());
        assertEquals(1, u1.getAccessLevel());
        assertEquals(2, u2.getAccessLevel());
    }

    @Test
    void userFields() {
        UUID id = UUID.randomUUID();
        User u = new User(id, "test@mail.com", 1, "John", "Q", "Doe");

        assertEquals(id, u.getId());
        assertEquals("test@mail.com", u.getEmail());
        assertEquals(1, u.getAccessLevel());
        assertEquals("John", u.getFirstName());
        assertEquals("Q", u.getMiddleName());
        assertEquals("Doe", u.getLastName());
    }
}