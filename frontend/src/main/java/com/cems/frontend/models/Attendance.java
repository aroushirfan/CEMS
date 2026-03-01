package com.cems.frontend.models;

import java.time.Instant;
import java.util.UUID;

public class Attendance {
    private UUID userId;
    private UUID eventId;
    private Instant checkInTime;
    private String status;

    public Attendance(UUID userId,UUID eventId, Instant checkInTime, String status) {
        this.userId = userId;
        this.eventId = eventId;
        this.checkInTime = checkInTime;
        this.status = status;
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
