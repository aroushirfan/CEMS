package com.cems.cemsbackend.repository;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Event entity operations.
 */
public interface EventRepository extends JpaRepository<Event, UUID> {

  /**
   * Finds event by ID.
   *
   * @param uuid unique identifier
   * @return optional event
   */
  Optional<Event> getEventById(UUID uuid);

  /**
   * Deletes event by ID.
   *
   * @param uuid unique identifier
   * @return deleted event
   */
  Optional<Event> deleteEventById(UUID uuid);

  /**
   * Finds events by owner.
   *
   * @param eventOwner owner entity
   * @return list of events
   */
  List<Event> getEventsByEventOwner(User eventOwner);

  /**
   * Finds event by owner and ID.
   *
   * @param eventOwner owner entity
   * @param uuid unique identifier
   * @return optional event
   */
  Optional<Event> getEventByEventOwnerAndId(User eventOwner, UUID uuid);

  /**
   * Finds all approved events.
   *
   * @return list of events
   */
  List<Event> findByApprovedTrue();

  /**
   * Checks attendee list for a specific event.
   *
   * @param uuid unique identifier
   * @param attendees list of users
   * @return true if exists
   */
  boolean existsEventByIdAndAttendeesContains(UUID uuid, List<User> attendees);
}