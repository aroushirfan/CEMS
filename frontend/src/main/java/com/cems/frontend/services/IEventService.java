package com.cems.frontend.services;

import com.cems.frontend.models.Event;
import com.cems.shared.model.EventDto.EventRequestDTO;
import java.io.IOException;
import java.util.List;

/**
 * Contract for event-related operations in the frontend service layer.
 *
 * <p>Implementations are responsible for fetching, creating, updating, deleting,
 * and approving events.</p>
 */
public interface IEventService {

  /**
   * Fetches all events.
   *
   * @return list of all events
   * @throws IOException if retrieval fails
   */
  List<Event> getAllEvents() throws IOException, InterruptedException;

  /**
   * Fetches all approved events.
   *
   * @return list of approved events
   * @throws IOException if retrieval fails
   */
  List<Event> getApprovedEvents() throws IOException, InterruptedException;

  /**
   * Creates a new event.
   *
   * @param data event creation payload
   * @return created event
   * @throws IOException if creation fails
   */
  Event createEvent(EventRequestDTO data) throws IOException, InterruptedException;

  /**
   * Updates an existing event.
   *
   * @param id   event identifier
   * @param data event update payload
   * @return updated event
   * @throws IOException if update fails
   */
  Event updateEvent(String id, EventRequestDTO data) throws IOException, InterruptedException;

  /**
   * Deletes an event.
   *
   * @param id event identifier
   * @throws IOException if deletion fails
   */
  void deleteEvent(String id) throws IOException, InterruptedException;

  /**
   * Fetches an event by identifier.
   *
   * @param id event identifier
   * @return event matching the given identifier
   * @throws IOException if retrieval fails
   */
  Event getEventById(String id) throws IOException, InterruptedException;

  /**
   * Approves an event.
   *
   * @param id event identifier
   * @return approved event
   * @throws IOException if approval fails
   */
  Event approveEvent(String id) throws IOException, InterruptedException;

}