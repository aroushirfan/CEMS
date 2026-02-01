package com.cems.shared.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class EventDto {
    static public class EventRequestDTO {
        @NotBlank(message = "title is required.")
        private String title;
        private String description;
        private String location;
        @NotNull
        private Long capacity;
        @NotNull
        private Instant dateTime;

        public EventRequestDTO() {
        }

        public EventRequestDTO(String title, String description, String location, Long capacity, Instant dateTime) {
            this.title = title;
            this.description = description;
            this.location = location;
            this.capacity = capacity;
            this.dateTime = dateTime;
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

        public Long getCapacity() {
            return capacity;
        }

        public void setCapacity(Long capacity) {
            this.capacity = capacity;
        }

        public Instant getDateTime() {
            return dateTime;
        }

        public void setDateTime(Instant dateTime) {
            this.dateTime = dateTime;
        }

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

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            EventResponseDTO that = (EventResponseDTO) o;
            return getCapacity() == that.getCapacity() && isApproved() == that.isApproved() && Objects.equals(getId(), that.getId()) && Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getLocation(), that.getLocation()) && Objects.equals(getDateTime(), that.getDateTime());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getTitle(), getDescription(), getLocation(), getCapacity(), getDateTime(), isApproved());
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