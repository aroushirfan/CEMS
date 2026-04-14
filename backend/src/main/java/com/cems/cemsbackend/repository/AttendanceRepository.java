package com.cems.cemsbackend.repository;

import com.cems.cemsbackend.model.Attendance;
import com.cems.cemsbackend.model.AttendanceId;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Attendance entity operations.
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {

  /**
   * Finds an attendance record by its composite ID.
   *
   * @param id the composite attendance ID.
   * @return an optional containing the found attendance.
   */
  Optional<Attendance> findById(AttendanceId id);

  /**
   * Find all check-ins for a specific event.
   *
   * @param event the event entity.
   * @return a list of attendance records.
   */
  List<Attendance> findAllByEvent(Event event);

  /**
   * Find all events a specific user actually attended.
   *
   * @param user the user entity.
   * @return a list of attendance records.
   */
  List<Attendance> findAllByUser(User user);

  /**
   * Find a specific check-in record by User and Event for duplicate checks.
   *
   * @param user  the user entity.
   * @param event the event entity.
   * @return an optional containing the attendance record.
   */
  Optional<Attendance> findByUserAndEvent(User user, Event event);

  /**
   * Deletes all attendance records associated with a specific event ID.
   *
   * @param eventId the UUID of the event.
   */
  @SuppressWarnings("checkstyle:MethodName")
  void deleteAttendancesByEvent_Id(UUID eventId);

  /**
   * Deletes a specific attendance record using user and event UUIDs.
   *
   * @param userId  the UUID of the user.
   * @param eventId the UUID of the event.
   */
  @SuppressWarnings("checkstyle:MethodName")
  void deleteAttendanceByUser_IdAndEvent_Id(UUID userId, UUID eventId);
}