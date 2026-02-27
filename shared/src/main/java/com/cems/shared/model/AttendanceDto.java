package com.cems.shared.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;

public class AttendanceDto {

    // Used when the frontend sends a check-in request
    public static class AttendanceRequestDTO {

        @JsonProperty("userId") // Ensures JSON "userId" maps to this field
        private UUID userId;

        @JsonProperty("eventId") // Ensures JSON "eventId" maps to this field
        private UUID eventId;

        private String status;

        public AttendanceRequestDTO() {}

        public UUID getUserId() { return userId; }
        public void setUserId(UUID userId) { this.userId = userId; }

        public UUID getEventId() { return eventId; }
        public void setEventId(UUID eventId) { this.eventId = eventId; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // Used when the backend sends attendance data back
    public static class AttendanceResponseDTO {
        private UUID userId;
        private UUID eventId;
        private Instant checkInTime;
        private String status;

        // No-args constructor for JSON deserialization
        public AttendanceResponseDTO() {}

        public AttendanceResponseDTO(UUID userId, UUID eventId, Instant checkInTime, String status) {
            this.userId = userId;
            this.eventId = eventId;
            this.checkInTime = checkInTime;
            this.status = status;
        }

        public UUID getUserId() { return userId; }
        public void setUserId(UUID userId) { this.userId = userId; }

        public UUID getEventId() { return eventId; }
        public void setEventId(UUID eventId) { this.eventId = eventId; }

        public Instant getCheckInTime() { return checkInTime; }
        public void setCheckInTime(Instant checkInTime) { this.checkInTime = checkInTime; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}