package com.cems.frontend.services;

import com.cems.shared.model.EventDto.EventResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ApiEventServiceTest {
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        // replicate the exact mapper config used in ApiEventService
        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    @Test
    void testJsonToDtoMapping() throws Exception {
        // Simulate a JSON string exactly as it comes from the backend (Snake Case)
        UUID id = UUID.randomUUID();
        String json = String.format(
                "{\"id\":\"%s\", \"title\":\"Gala\", \"location\":\"Helsinki\", \"date_time\":\"2026-02-08T12:00:00Z\", \"approved\":true}",
                id
        );

        // Test if the Jackson configuration service can handle
        EventResponseDTO dto = mapper.readValue(json, EventResponseDTO.class);

        // Assertions
        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals("Gala", dto.getTitle());
        assertTrue(dto.isApproved());
        assertNotNull(dto.getDateTime(), "Snake case date_time should map to dateTime property");
    }
}