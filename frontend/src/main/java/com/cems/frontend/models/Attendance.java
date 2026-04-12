package com.cems.frontend.models;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents an attendance record for an event.
 */
public class Attendance {

  private final String name;
  private final String email;
  private final UUID eventId;
  private final Instant checkInTime;
  private final String status;

  /**
   * Constructs an Attendance object.
   *
   * @param eventId     the ID of the event
   * @param firstName   the first name of the attendee
   * @param lastName    the last name of the attendee
   * @param email       the email of the attendee
   * @param checkInTime the time when the attendee checked in
   * @param status      the attendance status (e.g., "Checked In", "Absent")
   */
  public Attendance(UUID eventId, String firstName, String lastName,
                    String email, Instant checkInTime, String status) {
    this.name = firstName + " " + lastName;
    this.email = email;
    this.eventId = eventId;
    this.checkInTime = checkInTime;
    this.status = status;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public UUID getEventId() {
    return eventId;
  }

  public Instant getCheckInTime() {
    return checkInTime;
  }

  public String getStatus() {
    return status;
  }

}
