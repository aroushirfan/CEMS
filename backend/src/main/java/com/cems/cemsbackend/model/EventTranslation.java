package com.cems.cemsbackend.model;

import com.cems.shared.model.EventDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Entity representing translated event details for multi-language support.
 */
@Entity
public class EventTranslation {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.BINARY)
  private UUID id;
  private String title;
  private String description;
  private String location;
  private String language;

  @ManyToOne(optional = false)
  private Event refEvent;

  /**
   * Constructs a new EventTranslation.
   *
   * @param title       translated title.
   * @param description translated description.
   * @param location    translated location.
   * @param language    language code.
   * @param refEvent    reference to the parent event.
   */
  public EventTranslation(String title, String description, String location,
                          String language, Event refEvent) {
    this.title = title;
    this.description = description;
    this.location = location;
    this.refEvent = refEvent;
    this.language = language;
  }

  /**
   * Default constructor for JPA.
   */
  public EventTranslation() {
  }

  public UUID getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getLocation() {
    return location;
  }

  public Event getRefEvent() {
    return refEvent;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setRefEvent(Event refEvent) {
    this.refEvent = refEvent;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Creates a translation entity from a DTO.
   */
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public static EventTranslation createFromDTO(EventDto.EventLocalRequestDTO dto,
                                               Event refEvent, String lang) {
    return new EventTranslation(
            dto.getTitle(),
            dto.getDescription(),
            dto.getLocation(),
            lang,
            refEvent
    );
  }

  /**
   * Updates fields from the provided DTO if values are present.
   */
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public void updateFromDTO(EventDto.EventLocalRequestDTO dto) {
    if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
      title = dto.getTitle();
    }
    if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
      description = dto.getDescription();
    }
    if (dto.getLocation() != null && !dto.getLocation().isBlank()) {
      location = dto.getLocation();
    }
  }
}