package com.cems.frontend.services.attendance;

import com.cems.frontend.models.Attendance;

import java.util.List;

public interface IAttendanceService {
    List<Attendance> getEventAttendance(String eventId) throws Exception;
}
