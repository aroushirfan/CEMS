package com.cems.frontend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceTest {
    Attendance attendance;
    String name;
    String firstName;
    String lastName;
    String email;
    private UUID eventId;
    private Instant checkInTime;
    private String status;

    @BeforeEach
    void setUp() {
        firstName = "Firstname";
        lastName = "Lastname";
        name = firstName+ " " + lastName;
        email = "example.email@metropolia.fi";
        eventId = UUID.randomUUID();
        checkInTime = Instant.now();
        status = "Checked in";
        attendance = new Attendance(eventId,firstName,lastName,email,checkInTime,status);
    }

    @Test
    void testConstructorSetsAllFieldsCorrectly() {
        Attendance attendance = new Attendance(eventId,firstName,lastName,email,checkInTime,status);
        assertEquals(name, attendance.getName());
        assertEquals(email, attendance.getEmail());
        assertEquals(eventId, attendance.getEventId());
        assertEquals(checkInTime, attendance.getCheckInTime());
        assertEquals(status, attendance.getStatus());
    }

    @Test
    void testAttendanceModelGetters() {
        assertEquals(name, attendance.getName());
        assertEquals(email, attendance.getEmail());
        assertEquals(eventId,attendance.getEventId());
        assertEquals(checkInTime,attendance.getCheckInTime());
        assertEquals(status,attendance.getStatus());
    }

    @Test
    void testConstructorWithFutureCheckInTimeSetsCorrectly() {
        Instant future = Instant.now().plusSeconds(3600);
        Attendance attendance = new Attendance(eventId,firstName,lastName,email,future, status);
        assertEquals(future, attendance.getCheckInTime());
    }

    @Test
    void testConstructorWithPastCheckInTimeSetsCorrectly() {
        Instant past = Instant.now().minusSeconds(3600);
        Attendance attendance = new Attendance(eventId,firstName,lastName,email,past, status);
        assertEquals(past, attendance.getCheckInTime());
    }

}