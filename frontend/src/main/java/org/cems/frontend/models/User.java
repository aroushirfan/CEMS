package org.cems.frontend.models;

import javafx.beans.property.*;
import java.util.UUID;

public class User {
    private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
    private final StringProperty email = new SimpleStringProperty();
    private final IntegerProperty accessLevel = new SimpleIntegerProperty();

    public User() {}

    // Constructor for Mock Service
    public User(UUID id, String email, int accessLevel) {
        this.id.set(id);
        this.email.set(email);
        this.accessLevel.set(accessLevel);
    }

    // Property getters for UI binding
    public StringProperty emailProperty() { return email; }
    public IntegerProperty accessLevelProperty() { return accessLevel; }

    // Getters/Setters
    public UUID getId() { return id.get(); }
    public String getEmail() { return email.get(); }
    public int getAccessLevel() { return accessLevel.get(); }
}