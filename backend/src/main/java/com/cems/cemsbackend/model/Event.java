package com.cems.cemsbackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_event_datetime", columnList = "date_time")
        }
)
public class Event {
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

    public Event() {}

    public Event(String title, String description, String location, long capacity, Instant dateTime, User eventOwner, boolean approved) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.capacity = capacity;
        this.dateTime = dateTime;
        this.eventOwner = eventOwner;
        this.approved = approved;
    }

    public User getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(User eventOwner) {
        this.eventOwner = eventOwner;
    }

    public UUID getId() {
        return id;
    }

    public User[] getAttendees() {
        return attendees.toArray(new User[0]);
    }

    public boolean addAttendee(User attendee) {
        return attendees.add(attendee);
    }

    public boolean removeAttendee(User attendee) {
        return attendees.remove(attendee);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

}
