package com.cems.cemsbackend.mappers;

import com.cems.cemsbackend.model.Attendance;
import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import com.cems.shared.model.AttendanceDto.AttendanceResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceMapperTest {

    @Test
    void toDto_MapsCorrectly() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("Aroush");
        user.setLastName("Irfan");
        user.setEmail("test@mail.com");

        Event event = new Event();
        event.setId(UUID.randomUUID());

        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setEvent(event);
        attendance.setStatus("PRESENT");
        attendance.setCheckInTime(Instant.now());

        AttendanceResponseDTO dto = AttendanceMapper.toDto(attendance);

        assertEquals(user.getId(), dto.getUserId());
        assertEquals(event.getId(), dto.getEventId());
        assertEquals("PRESENT", dto.getStatus());
    }
}
