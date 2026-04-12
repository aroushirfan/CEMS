package com.cems.cemsbackend.model;

import com.cems.shared.model.EventDto;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

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

    public EventTranslation(String title, String description, String location, String language, Event refEvent) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.refEvent = refEvent;
        this.language = language;
    }

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

    public static EventTranslation createFromDTO(EventDto.EventLocalRequestDTO dto, Event refEvent, String lang) {
        return new EventTranslation(
                dto.getTitle(),
                dto.getDescription(),
                dto.getLocation(),
                lang,
                refEvent
        );
    }

    public void updateFromDTO(EventDto.EventLocalRequestDTO dto) {
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) title = dto.getTitle();
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) description = dto.getDescription();
        if (dto.getLocation() != null && !dto.getLocation().isBlank()) location = dto.getLocation();
    }
}
