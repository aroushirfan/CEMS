package com.cems.cemsbackend.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Composite key for the Attendance entity, combining User and Event IDs.
 */
@Embeddable
public class AttendanceId implements Serializable {

  @JdbcTypeCode(SqlTypes.BINARY)
  private UUID userId;

  @JdbcTypeCode(SqlTypes.BINARY)
  private UUID eventId;

  /**
   * Default constructor for JPA.
   */
  public AttendanceId() {
  }

  /**
   * Constructs an AttendanceId with specific user and event identifiers.
   *
   * @param userId  the unique identifier of the user.
   * @param eventId the unique identifier of the event.
   */
  public AttendanceId(UUID userId, UUID eventId) {
    this.userId = userId;
    this.eventId = eventId;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public UUID getEventId() {
    return eventId;
  }

  public void setEventId(UUID eventId) {
    this.eventId = eventId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttendanceId that = (AttendanceId) o;
    return Objects.equals(userId, that.userId) && Objects.equals(eventId, that.eventId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, eventId);
  }
}