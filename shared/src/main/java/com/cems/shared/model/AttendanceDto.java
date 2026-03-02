package com.cems.shared.model;

import java.time.Instant;
import java.util.UUID;

public class AttendanceDto {

    // Simplified: userId comes from JWT, eventId from URL path
    public static class AttendanceRequestDTO {
        private String status;

        public AttendanceRequestDTO() {}

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // Expanded: Includes firstName, lastName, and email
    public static class AttendanceResponseDTO {
        private UUID userId;
        private UUID eventId;
        private String firstName;
        private String lastName;
        private String email;
        private Instant checkInTime;
        private String status;

        public AttendanceResponseDTO() {}

        public AttendanceResponseDTO(UUID userId, UUID eventId, String firstName,
                                     String lastName, String email, Instant checkInTime, String status) {
            this.userId = userId;
            this.eventId = eventId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.checkInTime = checkInTime;
            this.status = status;
        }

        // Getters and Setters
        public UUID getUserId() { return userId; }
        public void setUserId(UUID userId) { this.userId = userId; }

        public UUID getEventId() { return eventId; }
        public void setEventId(UUID eventId) { this.eventId = eventId; }

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public Instant getCheckInTime() { return checkInTime; }
        public void setCheckInTime(Instant checkInTime) { this.checkInTime = checkInTime; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}