package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.Attendance;
import com.cems.cemsbackend.model.AttendanceId;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.AttendanceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public Attendance createCheckIn(User user, Event event, String status) {
        // Validate RSVP
        /*
        List<User> registeredAttendees = Arrays.asList(event.getAttendees());
        if (!registeredAttendees.contains(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User must RSVP before checking in.");
        }*/

        // Prevent Duplicate Check-ins
        if (attendanceRepository.findByUserAndEvent(user, event).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already checked in.");
        }

        // Construct ID manually to satisfy Hibernate 7.x requirements
        AttendanceId attendanceId = new AttendanceId(user.getId(), event.getId());

        // Build Entity
        Attendance attendance = new Attendance();
        attendance.setId(attendanceId);
        attendance.setUser(user);
        attendance.setEvent(event);
        attendance.setCheckInTime(Instant.now());
        attendance.setStatus(status != null ? status : "PRESENT");

        // Use saveAndFlush to commit immediately to MariaDB
        return attendanceRepository.saveAndFlush(attendance);
    }

    public List<Attendance> getAttendanceByEvent(Event event) {
        return attendanceRepository.findAllByEvent(event);
    }
}