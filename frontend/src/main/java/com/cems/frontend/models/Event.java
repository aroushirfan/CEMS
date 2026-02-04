package com.cems.frontend.models;

import javafx.beans.property.*;
import java.time.Instant;
import java.util.UUID;

public class Event {
    private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty location = new SimpleStringProperty();
    private final LongProperty capacity = new SimpleLongProperty();
    private final ObjectProperty<Instant> dateTime = new SimpleObjectProperty<>();
    private final BooleanProperty approved = new SimpleBooleanProperty();
    private final ObjectProperty<User> eventOwner = new SimpleObjectProperty<>();

    public Event(UUID id, String title, String description, String location, long capacity, Instant dateTime, boolean approved, User owner) {
        this.id.set(id);
        this.title.set(title);
        this.description.set(description);
        this.location.set(location);
        this.capacity.set(capacity);
        this.dateTime.set(dateTime);
        this.approved.set(approved);
        this.eventOwner.set(owner);
    }

    // Property Getters for Binding
    public ObjectProperty<UUID> idProperty() { return id; }
    public StringProperty titleProperty() { return title; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty locationProperty() { return location; }
    public LongProperty capacityProperty() { return capacity; }
    public ObjectProperty<Instant> dateTimeProperty() { return dateTime; }
    public BooleanProperty approvedProperty() { return approved; }
    public ObjectProperty<User> eventOwnerProperty() { return eventOwner; }

    // Standard Getters
    public UUID getId() { return id.get(); }
    public String getTitle() { return title.get(); }
    public String getDescription() { return description.get(); }
    public String getLocation() { return location.get(); }
    public long getCapacity() { return capacity.get(); }
    public Instant getDateTime() { return dateTime.get(); }
    public boolean isApproved() { return approved.get(); }

    // Standard Setters (Required for refresh logic)
    public void setId(UUID id) { this.id.set(id); }
    public void setTitle(String title) { this.title.set(title); }
    public void setDescription(String description) { this.description.set(description); }
    public void setLocation(String location) { this.location.set(location); }
    public void setCapacity(long capacity) { this.capacity.set(capacity); }
    public void setDateTime(Instant dateTime) { this.dateTime.set(dateTime); }
    public void setApproved(boolean approved) { this.approved.set(approved); }
    public void setEventOwner(User owner) { this.eventOwner.set(owner); }
}