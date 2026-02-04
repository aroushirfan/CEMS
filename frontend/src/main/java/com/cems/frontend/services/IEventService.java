package com.cems.frontend.services;

import com.cems.frontend.models.Event;
import com.cems.shared.model.EventDto.EventRequestDTO;
import java.util.List;

public interface IEventService {
    // Returns UI Models for the Grid
    List<Event> getAllEvents() throws Exception;

    // Optional: Return the new/updated Model to update the UI immediately
    Event createEvent(EventRequestDTO data) throws Exception;

    Event updateEvent(String id, EventRequestDTO data) throws Exception;

    void deleteEvent(String id) throws Exception;

    // This is where your "Get One" route would live if you need a fresh copy
    Event getEventById(String id) throws Exception;
}