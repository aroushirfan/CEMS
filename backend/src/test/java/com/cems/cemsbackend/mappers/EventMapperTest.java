package com.cems.cemsbackend.mappers;

import com.cems.cemsbackend.model.Event;
import com.cems.shared.model.EventDto.EventResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils; // The "Magic" utility
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

class EventMapperTest {

    @Test
    void shouldMapEventToEventResponseDTO() {
        // 1. ARRANGE
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        Event event = new Event();

        // Use Reflection to set private fields because there are no Setters
        ReflectionTestUtils.setField(event, "id", id);
        ReflectionTestUtils.setField(event, "title", "Annual Gala");
        ReflectionTestUtils.setField(event, "description", "A formal evening event");
        ReflectionTestUtils.setField(event, "location", "Grand Ballroom");
        ReflectionTestUtils.setField(event, "capacity", 500L);
        ReflectionTestUtils.setField(event, "dateTime", now);
        ReflectionTestUtils.setField(event, "approved", true);

        // 2. ACT
        EventResponseDTO dto = EventMapper.toDto(event);

        // 3. ASSERT
        assertThat(dto).isNotNull();

        // Use the PUBLIC GETTERS from your EventResponseDTO class
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getTitle()).isEqualTo("Annual Gala");
        assertThat(dto.getDescription()).isEqualTo("A formal evening event");
        assertThat(dto.getLocation()).isEqualTo("Grand Ballroom");
        assertThat(dto.getCapacity()).isEqualTo(500L);
        assertThat(dto.getDateTime()).isEqualTo(now);
        assertThat(dto.isApproved()).isTrue();
    }
}