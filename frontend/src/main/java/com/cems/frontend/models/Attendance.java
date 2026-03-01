package com.cems.frontend.models;

import java.time.Instant;
import java.util.UUID;

public class Attendance {
    private final UUID userId;
    private final UUID eventId;
    private final Instant checkInTime;
    private final String status;

    public Attendance(UUID userId,UUID eventId, Instant checkInTime, String status) {
        this.userId = userId;
        this.eventId = eventId;
        this.checkInTime = checkInTime;
        this.status = status;
    }

    public UUID getUserId() {
        return userId;
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
