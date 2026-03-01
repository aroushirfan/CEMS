package com.cems.frontend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceTest {
    Attendance attendance;
    private UUID userId;
    private UUID eventId;
    private Instant checkInTime;
    private String status;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        eventId = UUID.randomUUID();
        checkInTime = Instant.now();
        status = "Checked in";
        attendance = new Attendance(userId,eventId,checkInTime,status);
    }

    @Test
    void constructor_setsAllFieldsCorrectly() {
        Attendance attendance = new Attendance(userId,eventId,checkInTime,status);
        assertEquals(userId, attendance.getUserId());
        assertEquals(eventId, attendance.getEventId());
        assertEquals(checkInTime, attendance.getCheckInTime());
        assertEquals(status, attendance.getStatus());
    }

    @Test
    void testAttendanceModelGetters() {
        assertEquals(userId,attendance.getUserId());
        assertEquals(eventId,attendance.getEventId());
        assertEquals(checkInTime,attendance.getCheckInTime());
        assertEquals(status,attendance.getStatus());
    }

    @Test
    void testConstructor_withFutureCheckInTime_setsCorrectly() {
        Instant future = Instant.now().plusSeconds(3600);
        Attendance attendance = new Attendance(userId, eventId,future, status);
        assertEquals(future, attendance.getCheckInTime());
    }

    @Test
    void testConstructor_withPastCheckInTime_setsCorrectly() {
        Instant past = Instant.now().minusSeconds(3600);
        Attendance attendance = new Attendance(userId, eventId,past, status);
        assertEquals(past, attendance.getCheckInTime());
    }

}