package com.cems.frontend.models;

import java.time.Instant;
import java.util.UUID;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents an Event in the system with properties suitable for JavaFX bindings.
 */
public class Event {
  private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
  private final StringProperty title = new SimpleStringProperty();
  private final StringProperty description = new SimpleStringProperty();
  private final StringProperty location = new SimpleStringProperty();
  private final LongProperty capacity = new SimpleLongProperty();
  private final ObjectProperty<Instant> dateTime = new SimpleObjectProperty<>();
  private final BooleanProperty approved = new SimpleBooleanProperty();
  private final ObjectProperty<User> eventOwner = new SimpleObjectProperty<>();

  /**
   * Default constructor for Event.
   */
  public Event() {
  }

  /**
   * Constructor to initialize all properties of the Event.
   *
   * @param id          Unique identifier for the event
   * @param title       Title of the event
   * @param description Description of the event
   * @param location    Location where the event will take place
   * @param capacity    Maximum number of attendees allowed
   * @param dateTime    Date and time when the event is scheduled
   * @param approved    Whether the event is approved or not
   * @param owner       The user who created the event
   */
  public Event(UUID id, String title, String description,
               String location, long capacity, Instant dateTime, boolean approved, User owner) {
    this.id.set(id);
    this.title.set(title);
    this.description.set(description);
    this.location.set(location);
    this.capacity.set(capacity);
    this.dateTime.set(dateTime);
    this.approved.set(approved);
    this.eventOwner.set(owner);
  }

  /**
   * Returns the observable ID property.
   *
   * @return the ID property
   */
  public ObjectProperty<UUID> idProperty() {
    return id;
  }

  /**
   * Returns the observable title property.
   *
   * @return the title property
   */
  public StringProperty titleProperty() {
    return title;
  }

  /**
   * Returns the observable description property.
   *
   * @return the description property
   */
  public StringProperty descriptionProperty() {
    return description;
  }

  /**
   * Returns the observable location property.
   *
   * @return the location property
   */
  public StringProperty locationProperty() {
    return location;
  }

  /**
   * Returns the observable capacity property.
   *
   * @return the observable capacity property
   */
  public LongProperty capacityProperty() {
    return capacity;
  }

  /**
   * Returns the observable dateTime property.
   *
   * @return the observable dateTime property
   */
  public ObjectProperty<Instant> dateTimeProperty() {
    return dateTime;
  }

  /**
   * Returns the observable approved property.
   *
   * @return the observable approval status property
   */
  public BooleanProperty approvedProperty() {
    return approved;
  }

  /**
   * Returns the observable eventOwner property.
   *
   * @return the observable eventOwner property
   */
  public ObjectProperty<User> eventOwnerProperty() {
    return eventOwner;
  }

  // Getters
  /**
   * Returns the event ID.
   *
   * @return the event ID
   */
  public UUID getId() {
    return id.get();
  }

  /**
   * Returns the event title.
   *
   * @return the event title
   */
  public String getTitle() {
    return title.get();
  }

  /**
   * Returns the event description.
   *
   * @return the event description
   */
  public String getDescription() {
    return description.get();
  }

  /**
   * Returns the event location.
   *
   * @return the event location
   */
  public String getLocation() {
    return location.get();
  }

  /**
   * Returns the event capacity.
   *
   * @return the event capacity
   */
  public long getCapacity() {
    return capacity.get();
  }

  /**
   * Returns the event date and time.
   *
   * @return the event date and time
   */
  public Instant getDateTime() {
    return dateTime.get();
  }

  /**
   * Returns whether the event is approved.
   *
   * @return true if the event is approved, false otherwise
   */
  public boolean isApproved() {
    return approved.get();
  }

  // Setters
  /**
   * Sets the event ID.
   *
   * @param id the unique identifier to set
   */
  public void setId(UUID id) {
    this.id.set(id);
  }

  /**
   * Sets the event title.
   *
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title.set(title);
  }

  /**
   * Sets the event description.
   *
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description.set(description);
  }

  /**
   * Sets the event location.
   *
   * @param location the location to set
   */
  public void setLocation(String location) {
    this.location.set(location);
  }

  /**
   * Sets the event capacity.
   *
   * @param capacity the maximum number of attendees to set
   */
  public void setCapacity(long capacity) {
    this.capacity.set(capacity);
  }

  /**
   * Sets the event date and time.
   *
   * @param dateTime the date and time to set
   */
  public void setDateTime(Instant dateTime) {
    this.dateTime.set(dateTime);
  }

  /**
   * Sets the event approval status.
   *
   * @param approved the approval status to set
   */
  public void setApproved(boolean approved) {
    this.approved.set(approved);
  }

  /**
   * Sets the event owner.
   *
   * @param owner the user who created the event to set
   */
  public void setEventOwner(User owner) {
    this.eventOwner.set(owner);
  }
}