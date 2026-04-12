package com.cems.frontend.utils;

import com.cems.frontend.models.Event;
import com.cems.shared.model.EventDto.EventResponseDTO;
import java.util.List;

/**
 * Utility mapper for converting event DTOs into frontend event models.
 */
public final class EventMapper {

  /**
   * Utility class constructor.
   */
  private EventMapper() {
  }

  /**
   * Converts a single event response DTO into an event model.
   *
   * @param dto source event DTO
   * @return mapped event model, or {@code null} when input is {@code null}
   */
  public static Event toModel(EventResponseDTO dto) {
    if (dto == null) {
      return null;
    }
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

  /**
   * Converts a list of event response DTOs into event models.
   *
   * @param dtos list of source event DTOs
   * @return mapped list of event models
   */
  public static List<Event> toModelList(List<EventResponseDTO> dtos) {
    return dtos.stream()
        .map(EventMapper::toModel)
        .toList();
  }
}