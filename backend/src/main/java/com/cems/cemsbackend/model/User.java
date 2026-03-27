//package com.cems.cemsbackend.model;
//
//import jakarta.persistence.*;
//import org.hibernate.annotations.JdbcTypeCode;
//import org.hibernate.type.SqlTypes;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Entity
//@Table(
//        indexes = {
//                @Index(name = "idx_user_email", columnList = "email")
//        }
//)
//public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @JdbcTypeCode(SqlTypes.BINARY)
//    private UUID id;
//
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    @Column(nullable = false)
//    private String hashedPassword;
//
//    @Column(nullable = false)
//    private int accessLevel;
//
//    @Column(nullable = false)
//    private String firstName;
//
//    @Column(nullable = true)
//    private String middleName;
//
//    @Column(nullable = true)
//    private String lastName;
//
//    @ManyToMany(mappedBy = "attendees")
//    private List<Event> attendingEvents = new ArrayList<>();
//
//    @OneToMany(mappedBy = "eventOwner")
//    private List<Event> ownedEvents;
//
//    public User(String email, String hashedPassword, int accessLevel, String firstName, String middleName, String lastName) {
//        this.email = email;
//        this.hashedPassword = hashedPassword;
//        this.accessLevel = accessLevel;
//        this.firstName = firstName;
//        this.middleName = middleName;
//        this.lastName = lastName;
//    }
//
//    public User() {}
//
//    public List<Event> getOwnedEvents() {
//        return ownedEvents;
//    }
//
//    public List<Event> getAttendingEvents() {
//        return attendingEvents;
//    }
//
//    public boolean addAttendingEvent(Event event) {
//        return attendingEvents.add(event);
//    }
//
//    public UUID getId() {
//        return id;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getHashedPassword() {
//        return hashedPassword;
//    }
//
//    public void setHashedPassword(String hashedPassword) {
//        this.hashedPassword = hashedPassword;
//    }
//
//    public int getAccessLevel() {
//        return accessLevel;
//    }
//
//    public void setAccessLevel(int accessLevel) {
//        this.accessLevel = accessLevel;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getMiddleName() {
//        return middleName;
//    }
//
//    public void setMiddleName(String middleName) {
//        this.middleName = middleName;
//    }
//}
package com.cems.cemsbackend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Column(nullable = false)
    private int accessLevel;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = true)
    private String middleName;

    @Column(nullable = true)
    private String lastName;



    @Column(nullable = true)
    private String phone;

    @Column(nullable = true)
    private LocalDate dob;




    @Column(nullable = true)
    private String profileImageUrl;

    @ManyToMany(mappedBy = "attendees")
    private List<Event> attendingEvents = new ArrayList<>();

    @OneToMany(mappedBy = "eventOwner")
    private List<Event> ownedEvents = new ArrayList<>();

    public User() {}

    // -------------------------
    // Getters & Setters
    // -------------------------

    public UUID getId() { return id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    public int getAccessLevel() { return accessLevel; }
    public void setAccessLevel(int accessLevel) { this.accessLevel = accessLevel; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }


    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }


    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }



    public List<Event> getAttendingEvents() {
        return attendingEvents;
    }

    public boolean addAttendingEvent(Event event) {
        return attendingEvents.add(event);
    }

    public List<Event> getOwnedEvents() {
        return ownedEvents;
    }
}
