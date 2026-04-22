package com.cems.cemsbackend.mappers;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.EventTranslation;
import com.cems.shared.model.EventDto.EventResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EventMapperTest {

  private Event createBaseEvent() {
    UUID id = UUID.randomUUID();
    Instant now = Instant.now();
    Event event = new Event();

    ReflectionTestUtils.setField(event, "id", id);
    ReflectionTestUtils.setField(event, "title", "Base Title");
    ReflectionTestUtils.setField(event, "description", "Base Description");
    ReflectionTestUtils.setField(event, "location", "Base Location");
    ReflectionTestUtils.setField(event, "capacity", 100L);
    ReflectionTestUtils.setField(event, "dateTime", now);
    ReflectionTestUtils.setField(event, "approved", true);

    return event;
  }

  @Test
  void shouldMapEventToEventResponseDTO() {
    Event event = createBaseEvent();

    EventResponseDTO dto = EventMapper.toDto(event);

    assertThat(dto).isNotNull();
    assertThat(dto.getId()).isEqualTo(event.getId());
    assertThat(dto.getTitle()).isEqualTo("Base Title");
    assertThat(dto.getDescription()).isEqualTo("Base Description");
    assertThat(dto.getLocation()).isEqualTo("Base Location");
    assertThat(dto.getCapacity()).isEqualTo(100L);
    assertThat(dto.getDateTime()).isEqualTo(event.getDateTime());
    assertThat(dto.isApproved()).isTrue();
  }

  @Test
  void shouldMapLocalizedTranslation_WhenTranslationHasValues() {
    Event event = createBaseEvent();

    EventTranslation translation = new EventTranslation();
    ReflectionTestUtils.setField(translation, "title", "Translated Title");
    ReflectionTestUtils.setField(translation, "description", "Translated Description");
    ReflectionTestUtils.setField(translation, "location", "Translated Location");

    EventResponseDTO dto = EventMapper.toDto(event, translation);

    assertThat(dto.getTitle()).isEqualTo("Translated Title");
    assertThat(dto.getDescription()).isEqualTo("Translated Description");
    assertThat(dto.getLocation()).isEqualTo("Translated Location");
  }

  @Test
  void shouldFallbackToBaseEvent_WhenTranslationFieldsAreBlank() {
    Event event = createBaseEvent();

    EventTranslation translation = new EventTranslation();
    ReflectionTestUtils.setField(translation, "title", "   "); // blank
    ReflectionTestUtils.setField(translation, "description", "");
    ReflectionTestUtils.setField(translation, "location", "   ");

    EventResponseDTO dto = EventMapper.toDto(event, translation);

    assertThat(dto.getTitle()).isEqualTo("Base Title");
    assertThat(dto.getDescription()).isEqualTo("Base Description");
    assertThat(dto.getLocation()).isEqualTo("Base Location");
  }

  @Test
  void shouldFallbackToBaseEvent_WhenTranslationFieldsAreNull() {
    Event event = createBaseEvent();

    EventTranslation translation = new EventTranslation();
    ReflectionTestUtils.setField(translation, "title", null);
    ReflectionTestUtils.setField(translation, "description", null);
    ReflectionTestUtils.setField(translation, "location", null);

    EventResponseDTO dto = EventMapper.toDto(event, translation);

    assertThat(dto.getTitle()).isEqualTo("Base Title");
    assertThat(dto.getDescription()).isEqualTo("Base Description");
    assertThat(dto.getLocation()).isEqualTo("Base Location");
  }

  @Test
  void privateConstructor_CanBeInvokedForCoverage() throws Exception {
    Constructor<EventMapper> constructor = EventMapper.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    EventMapper instance = constructor.newInstance();
    assertThat(instance).isNotNull();
  }
}
