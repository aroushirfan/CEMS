package com.cems.cemsbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Entity representing an attendance record for a user at a specific event.
 */
@Entity
@Table(name = "attendance")
public class Attendance {

  @EmbeddedId
  private AttendanceId id;

  @ManyToOne(fetch = FetchType.EAGER)
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.EAGER)
  @MapsId("eventId")
  @JoinColumn(name = "event_id")
  private Event event;

  @Column(name = "check_in_time")
  private Instant checkInTime = null;

  @Column(nullable = false)
  private String status;

  /**
   * Default constructor for JPA.
   */
  public Attendance() {
  }

  // Getters and Setters
  public AttendanceId getId() {
    return id;
  }

  public void setId(AttendanceId id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Event getEvent() {
    return event;
  }

  public void setEvent(Event event) {
    this.event = event;
  }

  public Instant getCheckInTime() {
    return checkInTime;
  }

  public void setCheckInTime(Instant checkInTime) {
    this.checkInTime = checkInTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}