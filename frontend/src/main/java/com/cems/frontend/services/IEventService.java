package com.cems.frontend.services;

import com.cems.frontend.models.Event;
import com.cems.shared.model.EventDto.EventRequestDTO;
import java.util.List;

public interface IEventService {

    List<Event> getAllEvents() throws Exception;

    Event createEvent(EventRequestDTO data) throws Exception;

    Event updateEvent(String id, EventRequestDTO data) throws Exception;

    void deleteEvent(String id) throws Exception;

    Event getEventById(String id) throws Exception;
}