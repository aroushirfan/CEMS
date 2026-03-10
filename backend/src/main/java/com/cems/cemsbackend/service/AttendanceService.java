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
import java.util.Optional;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public Attendance createCheckIn(User user, Event event) {
        // Gatekeeper: Validate RSVP against partner's model
        List<User> registeredAttendees = event.getAttendees();
        Optional<Attendance> attendanceOpt = attendanceRepository.findByUserAndEvent(user, event);
        if (!registeredAttendees.contains(user) || attendanceOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User must RSVP before checking in.");
        }
        Attendance attendance = attendanceOpt.get();
        // Prevent Duplicate Check-ins
        if (attendance.getStatus().equals("PRESENT")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already checked in.");
        }

        attendance.setCheckInTime(Instant.now());
        attendance.setStatus("PRESENT"); // Automatically set to PRESENT

        return attendanceRepository.saveAndFlush(attendance);
    }

    public List<Attendance> getAttendanceByEvent(Event event) {
        return attendanceRepository.findAllByEvent(event);
    }

    public boolean hasCheckedIn(User user, Event event) {
        var attendanceOpt = attendanceRepository.findByUserAndEvent(user, event);
        return attendanceOpt.isPresent() && attendanceOpt.get().getStatus().equals("PRESENT");
    }
}