package com.cems.cemsbackend.mappers;

import com.cems.cemsbackend.model.Event;
import com.cems.shared.model.EventDto.EventResponseDTO;

/**
 * Utility class for mapping Event entities to Response DTOs.
 */
public final class EventMapper {

  /**
   * Private constructor to prevent instantiation of this utility class.
   */
  private EventMapper() {
    // This constructor is intentionally empty.
  }

  /**
   * Converts an Event entity to an EventResponseDTO.
   *
   * @param event the event entity to be mapped.
   * @return a new EventResponseDTO containing the event details.
   */
  public static EventResponseDTO toDto(final Event event) {
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