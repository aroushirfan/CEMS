package com.cems.frontend.utils;

import com.cems.frontend.models.Attendance;
import com.cems.shared.model.AttendanceDto.AttendanceResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceMapperTest {
  private AttendanceResponseDTO dto;
  private String firstName;
  private String lastName;
  private String expectedFullName;
  private String email;
  private UUID eventId;
  private Instant checkInTime;

  @BeforeEach
  void setUp() {
    firstName = "Firstname";
    lastName = "Lastname";
    expectedFullName = firstName + " " + lastName; // Matching how Attendance model joins them
    email = "example.email@metropolia.fi";
    eventId = UUID.randomUUID();
    checkInTime = Instant.now();
    dto = new AttendanceResponseDTO(UUID.randomUUID(), eventId, firstName, lastName, email, checkInTime, "Checked in");
  }

  @Test
  void toModel_returnsMappedAttendance_whenDtoIsValid() {
    Attendance result = AttendanceMapper.toModel(dto);

    assertNotNull(result);
    assertEquals(expectedFullName, result.getName());
    assertEquals(email, result.getEmail());
    assertEquals(eventId, result.getEventId());
    assertEquals(checkInTime, result.getCheckInTime());
    assertEquals("Checked in", result.getStatus());
  }

  @Test
  void toModel_returnsNull_whenDtoIsNull() {
    assertNull(AttendanceMapper.toModel(null));
  }

  @Test
  void toModelList_returnsMappedList_whenListIsValid() {
    AttendanceResponseDTO dto2 = new AttendanceResponseDTO(UUID.randomUUID(), eventId, "Jane", "Doe", "jane@test.fi", checkInTime, "Pending");
    List<AttendanceResponseDTO> dtoList = List.of(dto, dto2);

    List<Attendance> results = AttendanceMapper.toModelList(dtoList);

    assertEquals(2, results.size());
    assertEquals("Checked in", results.get(0).getStatus());
    assertEquals("Pending", results.get(1).getStatus());
    assertEquals("Jane Doe", results.get(1).getName());
  }

  @Test
  void toModelList_throwsException_whenListIsNull() {
    assertThrows(NullPointerException.class, () -> AttendanceMapper.toModelList(null));
  }
}