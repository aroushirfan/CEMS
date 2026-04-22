package com.cems.frontend.utils;

import com.cems.frontend.models.Event;
import com.cems.shared.model.EventDto;
import com.cems.shared.model.EventDto.EventResponseDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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

  /**
   * Create EventRequestDTO from raw form data
   * @return {@link EventDto.EventRequestDTO}
   */
  public static EventDto.EventRequestDTO map(LocalDate date, int hour, int minute, String title, String description, String location, long capacity) {
    if (date == null) {
      throw new IllegalArgumentException("Date is required");
    }

    Instant eventInstant = date
            .atTime(hour, minute)
            .atZone(ZoneId.systemDefault())
            .toInstant();

    return new EventDto.EventRequestDTO(
            title,
            description,
            location,
            capacity,
            eventInstant
    );
  }
}