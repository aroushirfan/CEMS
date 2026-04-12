package com.cems.cemsbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Represents a user entity stored in the system.
 */
@SuppressWarnings({
    "PMD.DataClass",
    "PMD.ShortClassName",
    "PMD.ImmutableField",
    "PMD.UnnecessaryConstructor"
})
@Entity
@Table(
        indexes = {
          @Index(name = "idx_user_email", columnList = User.COLUMN_EMAIL)
        }
)
public class User {

  /** Constant for email column name to avoid duplicate literals. */
  public static final String COLUMN_EMAIL = "email";

  /** Unique identifier for the user. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.BINARY)
  private UUID id;

  /** Email address of the user. */
  @Column(nullable = false, unique = true)
  private String email;

  /** Hashed password of the user. */
  @Column(nullable = false)
  private String hashedPassword;

  /** Access level of the user. */
  @Column(nullable = false)
  private int accessLevel;

  /** First name of the user. */
  @Column(nullable = false)
  private String firstName;

  /** Middle name of the user. */
  @Column(nullable = true)
  private String middleName;

  /** Last name of the user. */
  @Column(nullable = true)
  private String lastName;

  /** Phone number of the user. */
  @Column(nullable = true)
  private String phone;

  /** Date of birth of the user. */
  @Column(nullable = true)
  private LocalDate dob;

  /** Profile image URL of the user. */
  @Column(nullable = true)
  private String profileImageUrl;

  /** Events the user is attending. */
  @ManyToMany(mappedBy = "attendees")
  private List<Event> attendingEvents = new ArrayList<>();

  /** Events owned by the user. */
  @OneToMany(mappedBy = "eventOwner")
  private List<Event> ownedEvents = new ArrayList<>();

  /**
   * Default constructor required by JPA.
   */
  public User() {
    // intentionally empty
  }

  // -------------------------
  // Getters & Setters
  // -------------------------

  /** Returns the user ID. */
  public UUID getId() {
    return id;
  }

  /** Sets the user ID. */
  public void setId(final UUID id) {
    this.id = id;
  }

  /** Returns the email. */
  public String getEmail() {
    return email;
  }

  /** Sets the email. */
  public void setEmail(final String email) {
    this.email = email;
  }

  /** Returns the hashed password. */
  public String getHashedPassword() {
    return hashedPassword;
  }

  /** Sets the hashed password. */
  public void setHashedPassword(final String hashedPassword) {
    this.hashedPassword = hashedPassword;
  }

  /** Returns the access level. */
  public int getAccessLevel() {
    return accessLevel;
  }

  /** Sets the access level. */
  public void setAccessLevel(final int accessLevel) {
    this.accessLevel = accessLevel;
  }

  /** Returns the first name. */
  public String getFirstName() {
    return firstName;
  }

  /** Sets the first name. */
  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  /** Returns the middle name. */
  public String getMiddleName() {
    return middleName;
  }

  /** Sets the middle name. */
  public void setMiddleName(final String middleName) {
    this.middleName = middleName;
  }

  /** Returns the last name. */
  public String getLastName() {
    return lastName;
  }

  /** Sets the last name. */
  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  /** Returns the phone number. */
  public String getPhone() {
    return phone;
  }

  /** Sets the phone number. */
  public void setPhone(final String phone) {
    this.phone = phone;
  }

  /** Returns the date of birth. */
  public LocalDate getDob() {
    return dob;
  }

  /** Sets the date of birth. */
  public void setDob(final LocalDate dob) {
    this.dob = dob;
  }

  /** Returns the profile image URL. */
  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  /** Sets the profile image URL. */
  public void setProfileImageUrl(final String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
  }

  /** Returns the events the user is attending. */
  public List<Event> getAttendingEvents() {
    return attendingEvents;
  }

  /** Adds an event to the user's attending list. */
  public boolean addAttendingEvent(final Event event) {
    return attendingEvents.add(event);
  }

  /** Returns the events owned by the user. */
  public List<Event> getOwnedEvents() {
    return ownedEvents;
  }
}
