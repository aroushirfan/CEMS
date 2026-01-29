package com.cems.cemsbackend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public class EventDto {
    static public class EventRequestDTO {
        @NotBlank(message = "title is required.")
        private String title;
        private String description;
        private String location;
        @NotBlank @NotNull
        private Long capacity;
        @NotBlank @NotNull
        private Instant dateTime;
        private boolean approved = false;
    }
    static public class EventResponseDTO {
        private UUID id;
        private String title;
        private String description;
        private String location;
        private long capacity;
        private Instant dateTime;
        private boolean approved;

        public EventResponseDTO() {
        }

        public EventResponseDTO(UUID id, String title, String description, String location, long capacity, Instant dateTime, boolean approved) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.location = location;
            this.capacity = capacity;
            this.dateTime = dateTime;
            this.approved = approved;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public long getCapacity() {
            return capacity;
        }

        public void setCapacity(long capacity) {
            this.capacity = capacity;
        }

        public Instant getDateTime() {
            return dateTime;
        }

        public void setDateTime(Instant dateTime) {
            this.dateTime = dateTime;
        }

        public boolean isApproved() {
            return approved;
        }

        public void setApproved(boolean approved) {
            this.approved = approved;
        }
    }
}

/*
@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID id;
    @Column(nullable = false)
    private String title;
    private String description;
    private String location;
    private long capacity;
    @Column(nullable = false, name = "date_time")
    private Instant dateTime;
    @Column(nullable = false)
    private boolean approved = false;
    @ManyToMany
    private List<User> attendees;
    @ManyToOne(optional = false)
    private User eventOwner;
**/
