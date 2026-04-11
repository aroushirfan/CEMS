package com.cems.cemsbackend.model;

import com.cems.shared.model.EventDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


/**
 *  an event entity stored in the system.
 */
@Entity
@Table(
        indexes = {
          @Index(name = "idx_event_datetime", columnList = "date_time")
        }
)
public class Event {

  /**  identifier for the event. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.BINARY)
  private UUID id;

  /** Title of the event. */
  @Column(nullable = false)
  private String title;

  /** Description of the event. */
  private String description;

  /** Location of the event. */
  private String location;

  /** Maximum number of attendees allowed. */
  private long capacity;

  /** Date and time of the event. */
  @Column(nullable = false, name = "date_time")
  private Instant dateTime;

  /** Whether the event is approved. */
  @Column(nullable = false)
  private boolean approved;

  /** List of attendees. */
  @ManyToMany
  @JoinTable(
          name = "event_attendees",
          joinColumns = @JoinColumn(name = "event_id"),
          inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private List<User> attendees = new ArrayList<>();

  /** Owner of the event. */
  @ManyToOne(optional = false)
  private User eventOwner;

  /**
   * Default constructor required by JPA.
   */
  public Event() {
    // intentionally empty
  }

  /**
   * Full constructor for creating an event.
   */
  public Event(
          final String title,
          final String description,
          final String location,
          final long capacity,
          final Instant dateTime,
          final User eventOwner,
          final boolean approved
  ) {
    this.title = title;
    this.description = description;
    this.location = location;
    this.capacity = capacity;
    this.dateTime = dateTime;
    this.eventOwner = eventOwner;
    this.approved = approved;
    this.attendees = new ArrayList<>();
  }

  /**
   * Updates this event using values from the DTO.
   *
   * @param dto the DTO containing updated values
   * @return this event instance
   */
  public Event updateFromDto(final EventDto.EventRequestDTO dto) {
    if (dto.getTitle() != null) {
      this.setTitle(dto.getTitle());
    }
    if (dto.getDescription() != null) {
      this.setDescription(dto.getDescription());
    }
    if (dto.getLocation() != null) {
      this.setLocation(dto.getLocation());
    }
    if (dto.getCapacity() != null) {
      this.setCapacity(dto.getCapacity());
    }
    if (dto.getDateTime() != null) {
      this.setDateTime(dto.getDateTime());
    }
    return this;
  }

  /**
   * Returns the event owner.
   *
   * @return the event owner
   */
  public User getEventOwner() {
    return eventOwner;
  }

  /**
   * Sets the event owner.
   *
   * @param eventOwner the owner to assign
   */
  public void setEventOwner(final User eventOwner) {
    this.eventOwner = eventOwner;
  }

  /**
   * Returns the event ID.
   *
   * @return the event ID
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the event ID.
   *
   * @param id the ID to assign
   */
  @SuppressWarnings("PMD.ShortVariable")
  public void setId(final UUID id) {
    this.id = id;
  }

  /**
   * Returns the list of attendees.
   *
   * @return list of attendees
   */
  public List<User> getAttendees() {
    return attendees;
  }

  /**
   * Adds an attendee to the event.
   *
   * @param attendee the attendee to add
   * @return true if added successfully
   */
  public boolean addAttendee(final User attendee) {
    return attendees.add(attendee);
  }

  /**
   * Removes an attendee from the event.
   *
   * @param attendee the attendee to remove
   * @return true if removed successfully
   */
  public boolean removeAttendee(final User attendee) {
    return attendees.remove(attendee);
  }

  /**
   * Returns the event title.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the event title.
   *
   * @param title the title to assign
   */
  public void setTitle(final String title) {
    this.title = title;
  }

  /**
   * Returns the event date and time.
   *
   * @return the date and time
   */
  public Instant getDateTime() {
    return dateTime;
  }

  /**
   * Sets the event date and time.
   *
   * @param dateTime the date and time to assign
   */
  public void setDateTime(final Instant dateTime) {
    this.dateTime = dateTime;
  }

  /**
   * Returns whether the event is approved.
   *
   * @return true if approved
   */
  public boolean isApproved() {
    return approved;
  }

  /**
   * Sets the approval status.
   *
   * @param approved the approval status
   */
  public void setApproved(final boolean approved) {
    this.approved = approved;
  }

  /**
   * Returns the event description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the event description.
   *
   * @param description the description to assign
   */
  public void setDescription(final String description) {
    this.description = description;
  }

  /**
   * Returns the event location.
   *
   * @return the location
   */
  public String getLocation() {
    return location;
  }

  /**
   * Sets the event location.
   *
   * @param location the location to assign
   */
  public void setLocation(final String location) {
    this.location = location;
  }

  /**
   * Returns the event capacity.
   *
   * @return the capacity
   */
  public long getCapacity() {
    return capacity;
  }

  /**
   * Sets the event capacity.
   *
   * @param capacity the capacity to assign
   */
  public void setCapacity(final long capacity) {
    this.capacity = capacity;
  }
}
