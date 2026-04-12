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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Represents an event entity stored in the system.
 */
@Entity
@Table(
        indexes = {
          @Index(name = "idx_event_datetime", columnList = "date_time")
        }
)
public class Event {

  /** Unique identifier for the event. */
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

  /** Localized translations for this event. */
  @OneToMany(mappedBy = "refEvent")
  private List<EventTranslation> translations = new ArrayList<>();

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

  /** Default constructor required by JPA. */
  public Event() {
    // intentionally empty
  }

  /** Full constructor for creating an event. */
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
  }

  /** Updates this event using values from the DTO. */
  public Event updateFromDto(final EventDto.EventRequestDTO dto) {
    if (dto.getTitle() != null) {
      this.title = dto.getTitle();
    }
    if (dto.getDescription() != null) {
      this.description = dto.getDescription();
    }
    if (dto.getLocation() != null) {
      this.location = dto.getLocation();
    }
    if (dto.getCapacity() != null) {
      this.capacity = dto.getCapacity();
    }
    if (dto.getDateTime() != null) {
      this.dateTime = dto.getDateTime();
    }
    return this;
  }

  public UUID getId() {
    return id;
  }

  public void setId(final UUID id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(final String location) {
    this.location = location;
  }

  public long getCapacity() {
    return capacity;
  }

  public void setCapacity(final long capacity) {
    this.capacity = capacity;
  }

  public Instant getDateTime() {
    return dateTime;
  }

  public void setDateTime(final Instant dateTime) {
    this.dateTime = dateTime;
  }

  public boolean isApproved() {
    return approved;
  }

  public void setApproved(final boolean approved) {
    this.approved = approved;
  }

  public List<User> getAttendees() {
    return attendees;
  }

  public boolean addAttendee(final User attendee) {
    return attendees.add(attendee);
  }

  public boolean removeAttendee(final User attendee) {
    return attendees.remove(attendee);
  }

  public User getEventOwner() {
    return eventOwner;
  }

  public void setEventOwner(final User eventOwner) {
    this.eventOwner = eventOwner;
  }

  public List<EventTranslation> getTranslations() {
    return translations;
  }
}
