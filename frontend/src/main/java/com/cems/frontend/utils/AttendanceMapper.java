package com.cems.frontend.utils;

import com.cems.frontend.models.Attendance;
import com.cems.shared.model.AttendanceDto.AttendanceResponseDTO;
import java.util.List;

/**
 * Utility mapper for converting attendance DTOs into frontend attendance models.
 */
public final class AttendanceMapper {

  private AttendanceMapper() {
  }

  /**
   * Converts a single attendance response DTO into an attendance model.
   *
   * @param attendanceDto source attendance DTO
   * @return mapped attendance model, or {@code null} when input is {@code null}
   */
  public static Attendance toModel(AttendanceResponseDTO attendanceDto) {
    if (attendanceDto == null) {
      return null;
    }
    return new Attendance(
        attendanceDto.getEventId(),
        attendanceDto.getFirstName(),
        attendanceDto.getLastName(),
        attendanceDto.getEmail(),
        attendanceDto.getCheckInTime(),
        attendanceDto.getStatus()

    );
  }

  /**
   * Converts a list of attendance response DTOs into attendance models.
   *
   * @param attendanceDtoList list of source attendance DTOs
   * @return mapped list of attendance models
   */
  public static List<Attendance> toModelList(List<AttendanceResponseDTO>
                                                 attendanceDtoList) {
    return attendanceDtoList.stream()
        .map(AttendanceMapper::toModel)
        .toList();
  }
}