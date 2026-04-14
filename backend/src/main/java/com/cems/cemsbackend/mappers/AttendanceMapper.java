package com.cems.cemsbackend.mappers;

import com.cems.cemsbackend.model.Attendance;
import com.cems.shared.model.AttendanceDto.AttendanceResponseDTO;

/**
 * Mapper utility to convert Attendance entities into AttendanceResponseDTOs.
 */
public class AttendanceMapper {

  /**
   * Maps an Attendance entity to a response DTO for the frontend.
   *
   * @param attendance the attendance record entity.
   * @return a DTO containing the user, event, and check-in details.
   */
  public static AttendanceResponseDTO toDto(Attendance attendance) {
    return new AttendanceResponseDTO(
        attendance.getUser().getId(),
        attendance.getEvent().getId(),
        attendance.getUser().getFirstName(),
        attendance.getUser().getLastName(),
        attendance.getUser().getEmail(),
        attendance.getCheckInTime(),
        attendance.getStatus()
    );
  }
}