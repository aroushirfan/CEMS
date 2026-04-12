package com.cems.cemsbackend.mappers;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.EventTranslation;
import com.cems.shared.model.EventDto.EventResponseDTO;

/**
 * Utility class for mapping Event entities to response DTOs,
 * including support for localized translations.
 */
public final class EventMapper {

  /**
   * Private constructor to prevent instantiation.
   */
  private EventMapper() {
    // intentionally empty
  }

  /**
   * Maps a base Event entity to a response DTO.
   *
   * @param event the event entity
   * @return the mapped response DTO
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

  /**
   * Maps an Event entity and its translation to a localized response DTO.
   *
   * @param event the base event entity
   * @param translation the localized translation entity
   * @return the mapped localized response DTO
   */
  public static EventResponseDTO toDto(
          final Event event,
          final EventTranslation translation
  ) {
    final String title = isBlank(translation.getTitle())
            ? event.getTitle()
            : translation.getTitle();

    final String description = isBlank(translation.getDescription())
            ? event.getDescription()
            : translation.getDescription();

    final String location = isBlank(translation.getLocation())
            ? event.getLocation()
            : translation.getLocation();

    return new EventResponseDTO(
            event.getId(),
            title,
            description,
            location,
            event.getCapacity(),
            event.getDateTime(),
            event.isApproved()
    );
  }

  /**
   * Utility method to check if a string is null or blank.
   *
   * @param value the string to check
   * @return true if null or blank, false otherwise
   */
  private static boolean isBlank(final String value) {
    return value == null || value.isBlank();
  }
}
