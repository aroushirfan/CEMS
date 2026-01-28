package org.cems.frontend.models;

import javafx.beans.property.*;

import java.time.Instant;
import java.util.UUID;

public class Event {
    private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
    private final StringProperty title = new SimpleStringProperty();
    private final ObjectProperty<Instant> dateTime = new SimpleObjectProperty<>();
    private final BooleanProperty approved = new SimpleBooleanProperty();
    private final ObjectProperty<User> eventOwner = new SimpleObjectProperty<>();

    public Event(UUID id, String title, Instant dateTime, boolean approved, User owner) {
        this.id.set(id);
        this.title.set(title);
        this.dateTime.set(dateTime);
        this.approved.set(approved);
        this.eventOwner.set(owner);
    }

    // Property methods for TableView binding
    public StringProperty titleProperty() { return title; }
    public ObjectProperty<Instant> dateTimeProperty() { return dateTime; }
    public BooleanProperty approvedProperty() { return approved; }

    // Getters for logic
    public String getTitle() { return title.get(); }
    public Instant getDateTime() { return dateTime.get(); }
}
