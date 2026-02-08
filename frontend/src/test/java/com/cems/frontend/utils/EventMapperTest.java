package com.cems.frontend.utils;

import com.cems.frontend.models.Event;
import com.cems.shared.model.EventDto.EventResponseDTO;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

    @Test
    void testToModelMapping() {
        // Setup mock DTO
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        EventResponseDTO dto = new EventResponseDTO(id, "Test Event", "Desc", "Helsinki", 100L, now, true);

        // Map
        Event model = EventMapper.toModel(dto);

        // Assert
        assertNotNull(model);
        assertEquals(id, model.getId());
        assertEquals("Test Event", model.getTitle());
        assertEquals(now, model.getDateTime());
        assertTrue(model.isApproved());
        assertNull(model.eventOwnerProperty().get(), "Owner should be null as per current mapper implementation");
    }

    @Test
    void testToModelListMapping() {
        // Setup list of DTOs
        EventResponseDTO dto1 = new EventResponseDTO(UUID.randomUUID(), "Event 1", "D1", "L1", 10L, Instant.now(), true);
        EventResponseDTO dto2 = new EventResponseDTO(UUID.randomUUID(), "Event 2", "D2", "L2", 20L, Instant.now(), false);
        List<EventResponseDTO> dtos = List.of(dto1, dto2);

        // Map list
        List<Event> models = EventMapper.toModelList(dtos);

        // Assert
        assertEquals(2, models.size(), "List size should match");
        assertEquals("Event 1", models.get(0).getTitle());
        assertEquals("Event 2", models.get(1).getTitle());
    }

    @Test
    void testNullHandling() {
        // null check in mapper for NullPointerExceptions
        assertNull(EventMapper.toModel(null), "Mapping a null DTO should return null");
    }
}