package com.cems.frontend.services;

import com.cems.shared.model.EventDto;

import java.util.List;

public interface IEventService {
    List<EventDto.EventResponseDTO> getAllEvents() throws Exception;
    EventDto.EventResponseDTO createEvent(EventDto.EventRequestDTO data) throws Exception;

}
