package org.cems.frontend.services;

import com.cems.shared.model.EventDto;

import java.util.List;

public interface IEventService {
    List<EventDto.EventResponseDTO> getAllEvents() throws Exception;
}
