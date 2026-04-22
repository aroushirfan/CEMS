package com.cems.frontend.utils;

import com.cems.frontend.models.Event;
import com.cems.shared.model.EventDto.EventResponseDTO;
import com.cems.shared.model.EventDto.EventRequestDTO;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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

    @Test
    void mapToDTO() {
        for (int i = 0; i < 5; i++) {
            Random random = new Random();
            LocalDate date = LocalDate.ofEpochDay(
                    ThreadLocalRandom.current().nextLong(
                            LocalDate.of(2000, 1, 1).toEpochDay(),
                            LocalDate.of(2025, 12, 31).toEpochDay()
                    )
            );
            var randomHour = random.nextInt(0, 25);
            var randomMinute = random.nextInt(0, 60);
            var title = RandomString.generateRandomString(10);
            var description = RandomString.generateRandomString(10);
            var location = RandomString.generateRandomString(10);
            var capacity = random.nextLong(1, 1000);

            EventRequestDTO refDTO = new EventRequestDTO(
                    title,
                    description,
                    location,
                    capacity,
                    date
                            .atTime(randomHour, randomMinute)
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
            );
            EventRequestDTO testDTO = EventMapper.mapToRequestDTO(
                    date,
                    randomHour,
                    randomMinute,
                    title,
                    description,
                    location,
                    capacity
            );

            assertEquals(refDTO, testDTO);
        }
    }
}