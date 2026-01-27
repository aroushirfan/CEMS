package com.cems.cemsbackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_user_email", columnList = "email")
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String hashedPassword;
    private String refreshToken;
    @Column(nullable = false)
    private int accessLevel = AccessLevel.USER;
    @ManyToMany(mappedBy = "attendees")
    private List<Event> attendingEvents;
    @OneToMany(mappedBy = "eventOwner")
    private List<Event> ownedEvents;

    public User(String email, String hashedPassword, String refreshToken) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.refreshToken = refreshToken;
    }

    public User() {}

    public Event[] getOwnedEvents() {
        return ownedEvents.toArray(new Event[0]);
    }

    public Event[] getAttendingEvents() {
        return attendingEvents.toArray(new Event[0]);
    }

    public boolean addAttendingEvent(Event event) {
        return attendingEvents.add(event);
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
}
