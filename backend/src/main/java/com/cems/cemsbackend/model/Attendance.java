package com.cems.cemsbackend.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "attendance")
public class Attendance {

    @EmbeddedId
    private AttendanceId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId") // References field in AttendanceId
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("eventId") // References field in AttendanceId
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    private Event event;

    @Column(nullable = false, name = "check_in_time")
    private Instant checkInTime;

    @Column(nullable = false)
    private String status;

    public Attendance() {}

    // Getters and Setters
    public AttendanceId getId() { return id; }
    public void setId(AttendanceId id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public Instant getCheckInTime() { return checkInTime; }
    public void setCheckInTime(Instant checkInTime) { this.checkInTime = checkInTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}