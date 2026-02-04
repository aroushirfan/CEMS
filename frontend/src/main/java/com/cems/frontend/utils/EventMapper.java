package com.cems.frontend.utils;

import com.cems.frontend.models.Event;
import com.cems.shared.model.EventDto.EventResponseDTO;
import java.util.List;

public class EventMapper {

    /**
     * Converts a single DTO to a Frontend Model.
     * Centralizing this here means if your model changes, you fix it in one place.
     */
    public static Event toModel(EventResponseDTO dto) {
        if (dto == null) return null;
        return new Event(
                dto.getId(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getLocation(),
                dto.getCapacity(),
                dto.getDateTime(),
                dto.isApproved(),
                null // Map owner here if needed later
        );
    }

    /**
     * Converts a list of DTOs to a list of Frontend Models.
     */
    public static List<Event> toModelList(List<EventResponseDTO> dtos) {
        return dtos.stream()
                .map(EventMapper::toModel)
                .toList();
    }
}