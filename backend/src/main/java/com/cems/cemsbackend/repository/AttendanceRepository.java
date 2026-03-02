package com.cems.cemsbackend.repository;

import com.cems.cemsbackend.model.Attendance;
import com.cems.cemsbackend.model.AttendanceId;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceId> {

    Optional<Attendance> findById(AttendanceId id);

    // Find all check-ins for a specific event
    List<Attendance> findAllByEvent(Event event);

    // Find all events a specific user actually attended
    List<Attendance> findAllByUser(User user);

    // Find a specific check-in record by User and Event
    // Note: The AttendanceService utilizes this for the "Duplicate Check"
    Optional<Attendance> findByUserAndEvent(User user, Event event);
}