package com.cems.frontend.utils;

import com.cems.frontend.models.Attendance;
import com.cems.shared.model.AttendanceDto;
import java.util.List;

public class AttendanceMapper {
    public static Attendance toModel(AttendanceDto.AttendanceResponseDTO attendanceResponseDTO) {
        if (attendanceResponseDTO == null) return null;
        return new Attendance(
                attendanceResponseDTO.getUserId(),
                attendanceResponseDTO.getEventId(),
                attendanceResponseDTO.getCheckInTime(),
                attendanceResponseDTO.getStatus()
        );
    }

    public static List<Attendance> toModelList(List<AttendanceDto.AttendanceResponseDTO> attendanceResponseDTOList) {
        return attendanceResponseDTOList.stream()
                .map(AttendanceMapper::toModel)
                .toList();
    }
}