package com.cems.cemsbackend.mappers;

import com.cems.cemsbackend.model.Attendance;
import com.cems.shared.model.AttendanceDto.AttendanceResponseDTO;

public class AttendanceMapper {
    public static AttendanceResponseDTO toDto(Attendance attendance) {
        return new AttendanceResponseDTO(
                attendance.getUser().getId(),
                attendance.getEvent().getId(),
                attendance.getCheckInTime(),
                attendance.getStatus()
        );
    }
}