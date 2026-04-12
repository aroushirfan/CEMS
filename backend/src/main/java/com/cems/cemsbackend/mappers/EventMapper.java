package com.cems.cemsbackend.mappers;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.EventTranslation;
import com.cems.shared.model.EventDto;
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

    public static EventResponseDTO toDto(Event event, EventTranslation eventTranslation) {
        return new EventResponseDTO(
                event.getId(),
                eventTranslation.getTitle() == null || eventTranslation.getTitle().isBlank() ? event.getTitle() : eventTranslation.getTitle(),
                eventTranslation.getDescription() == null || eventTranslation.getDescription().isBlank() ? event.getDescription() : eventTranslation.getDescription(),
                eventTranslation.getLocation() == null || eventTranslation.getLocation().isBlank() ? event.getLocation() : eventTranslation.getLocation(),
                event.getCapacity(),
                event.getDateTime(),
                event.isApproved()
        );
    }
}
