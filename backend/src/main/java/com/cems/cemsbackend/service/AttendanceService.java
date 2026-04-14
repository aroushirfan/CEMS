package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.Attendance;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.cemsbackend.repository.AttendanceRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service for managing event check-ins and attendance records.
 */
@Service
public class AttendanceService {

  private static final String STATUS_PRESENT = "PRESENT";
  private final AttendanceRepository attendanceRepository;

  /**
   * Constructor for AttendanceService.
   */
  public AttendanceService(AttendanceRepository attendanceRepository) {
    this.attendanceRepository = attendanceRepository;
  }

  /**
   * Validates RSVP status and performs check-in for a user at a specific event.
   *
   * @param user  the user checking in
   * @param event the target event
   * @return the updated attendance record
   * @throws ResponseStatusException if user hasn't RSVP'd or is already checked in
   */
  public Attendance createCheckIn(User user, Event event) {
    Attendance attendance = attendanceRepository.findByUserAndEvent(user, event)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
            "User must RSVP before checking in."));

    // Validate that the user is actually in the event's attendee list
    if (!event.getAttendees().contains(user)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN,
          "User record not found in event roster.");
    }

    if (STATUS_PRESENT.equals(attendance.getStatus())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already checked in.");
    }

    attendance.setCheckInTime(Instant.now());
    attendance.setStatus(STATUS_PRESENT);

    return attendanceRepository.saveAndFlush(attendance);
  }

  /**
   * Retrieves all attendance records for a given event.
   */
  public List<Attendance> getAttendanceByEvent(Event event) {
    return attendanceRepository.findAllByEvent(event);
  }

  /**
   * Checks if a specific user has already checked into an event.
   */
  public boolean hasCheckedIn(User user, Event event) {
    return attendanceRepository.findByUserAndEvent(user, event)
        .map(a -> STATUS_PRESENT.equals(a.getStatus()))
        .orElse(false);
  }
}