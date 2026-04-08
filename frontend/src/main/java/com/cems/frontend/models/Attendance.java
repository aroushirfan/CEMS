package com.cems.frontend.models;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.UUID;

public class Attendance {

    private final String  name;
    private final String  email;
    private final UUID eventId;
    private final Instant checkInTime;
    private final String status;

    public Attendance(UUID eventId,String firstName, String lastName, String email, Instant checkInTime, String status) {
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
