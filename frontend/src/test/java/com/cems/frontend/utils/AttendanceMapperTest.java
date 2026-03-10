package com.cems.frontend.utils;

import com.cems.frontend.models.Attendance;
import com.cems.shared.model.AttendanceDto.AttendanceResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class AttendanceMapperTest {
    AttendanceResponseDTO dto;
    UUID userId;
    String name;
    String firstName;
    String lastName;
    String email;
    UUID eventId;
    Instant checkInTime;

    @BeforeEach
    void setUp() {
        firstName = "Firstname";
        lastName = "Lastname";
        name = firstName+ " " + lastName;
        email = "example.email@metropolia.fi";
        eventId = UUID.randomUUID();
        userId = UUID.randomUUID();
        checkInTime = Instant.now();
        dto = new AttendanceResponseDTO(userId,eventId,firstName,lastName,email, checkInTime, "Checked in");
    }

    @Test
    void testToModel_returnsAllMappedAttendance() {
        Attendance attendanceModel = AttendanceMapper.toModel(dto);

        assertNotNull(attendanceModel);
        assertEquals(name, attendanceModel.getName());
        assertEquals(email, attendanceModel.getEmail());
        assertEquals(eventId, attendanceModel.getEventId());
        assertEquals(checkInTime, attendanceModel.getCheckInTime());
        assertEquals("Checked in", attendanceModel.getStatus());
    }

    @Test
    void toModel_throwsException_whenDTOIsNull() {
        assertNull(AttendanceMapper.toModel(null));
    }

    @Test
    void testToModelList_returnsAllMappedAttendances() {
        Attendance attendanceModel = AttendanceMapper.toModel(dto);

        assertNotNull(attendanceModel);
        assertEquals(name, attendanceModel.getName());
        assertEquals(eventId, attendanceModel.getEventId());
        assertEquals(checkInTime, attendanceModel.getCheckInTime());
        assertEquals("Checked in", attendanceModel.getStatus());
    }

//    @Test
//    void testMapperToModelList() {
//        AttendanceResponseDTO dto2 = new AttendanceResponseDTO(UUID.randomUUID(), eventId, firstName,lastName, email, checkInTime, "Pending");
//        List<AttendanceResponseDTO> dtoList = List.of(dto, dto2);
//
//        List<Attendance> attendanceModel = AttendanceMapper.toModelList(dtoList);
//        assertEquals(2, attendanceModel.size(), "List size should match");
//        assertEquals("Checked in", attendanceModel.get(0).getStatus());
//        assertEquals("Pending", attendanceModel.get(1).getStatus());
//        assertEquals(attendanceModel.get(1).getEventId(), attendanceModel.get(0).getEventId());
//        assertNotEquals(attendanceModel.get(1).getName(), attendanceModel.get(0).getName());
//    }
}