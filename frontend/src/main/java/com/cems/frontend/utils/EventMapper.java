package com.cems.frontend.utils;

import com.cems.frontend.models.Event;
import com.cems.shared.model.EventDto.EventResponseDTO;
import java.util.List;

public class EventMapper {

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
                null
        );
    }

    public static List<Event> toModelList(List<EventResponseDTO> dtos) {
        return dtos.stream()
                .map(EventMapper::toModel)
                .toList();
    }
}