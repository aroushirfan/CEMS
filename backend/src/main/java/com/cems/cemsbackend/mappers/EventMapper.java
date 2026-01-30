package com.cems.cemsbackend.mappers;

import com.cems.cemsbackend.model.Event;
import com.cems.shared.model.EventDto.EventResponseDTO;
public class EventMapper {
    public static EventResponseDTO toDto(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getCapacity(),
                event.getDateTime(),
                event.isApproved()
        );
    }
}
