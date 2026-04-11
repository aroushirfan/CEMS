package com.cems.frontend.models;

import java.time.LocalDate;
import java.util.UUID;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * User model representing a user in the system.
 * Contains properties for user details and access level.
 */
public class User {

  private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
  private final StringProperty email = new SimpleStringProperty();
  private final IntegerProperty accessLevel = new SimpleIntegerProperty();
  private final StringProperty firstName = new SimpleStringProperty();
  private final StringProperty middleName = new SimpleStringProperty();
  private final StringProperty lastName = new SimpleStringProperty();
  private final StringProperty phone = new SimpleStringProperty();
  private final ObjectProperty<LocalDate> dob = new SimpleObjectProperty<>();
  private final StringProperty language = new SimpleStringProperty();
  private final StringProperty profileImageUrl = new SimpleStringProperty();

  /**
   * Default constructor for User.
   */
  public User() {
  }

  /**
   * Parameterized constructor for User.
   *
   * @param id          Unique identifier for the user
   * @param email       User's email address
   * @param accessLevel Access level of the user (e.g., 0 = regular user, 1 = admin)
   * @param firstName   User's first name
   * @param middleName  User's middle name
   * @param lastName    User's last name
   */
  public User(UUID id, String email, int accessLevel, String firstName,
              String middleName, String lastName) {
    this.id.set(id);
    this.email.set(email);
    this.accessLevel.set(accessLevel);
    this.firstName.set(firstName);
    this.middleName.set(middleName);
    this.lastName.set(lastName);
  }

  /**
   * Returns the observable ID property.
   *
   * @return the ID property
   */
  public ObjectProperty<UUID> idProperty() {
    return id;
  }

  /**
   * Returns the observable email property.
   *
   * @return the email property
   */
  public StringProperty emailProperty() {
    return email;
  }

  /**
   * Returns the observable access level property.
   *
   * @return the access level property
   */
  public IntegerProperty accessLevelProperty() {
    return accessLevel;
  }

  /**
   * Returns the observable first name property.
   *
   * @return the first name property
   */
  public StringProperty firstNameProperty() {
    return firstName;
  }

  /**
   * Returns the observable middle name property.
   *
   * @return the middle name property
   */
  public StringProperty middleNameProperty() {
    return middleName;
  }

  /**
   * Returns the observable last name property.
   *
   * @return the last name property
   */
  public StringProperty lastNameProperty() {
    return lastName;
  }

  /**
   * Returns the observable phone property.
   *
   * @return the phone property
   */
  public StringProperty phoneProperty() {
    return phone;
  }

  /**
   * Returns the observable date of birth property.
   *
   * @return the date of birth property
   */
  public ObjectProperty<LocalDate> dobProperty() {
    return dob;
  }

  /**
   * Returns the observable language property.
   *
   * @return the language property
   */
  public StringProperty languageProperty() {
    return language;
  }

  /**
   * Returns the observable profile image URL property.
   *
   * @return the profile image URL property
   */
  public StringProperty profileImageUrlProperty() {
    return profileImageUrl;
  }

  // Getters

  /**
   * Returns the user ID.
   *
   * @return the user ID
   */
  public UUID getId() {
    return id.get();
  }

  /**
   * Returns the user email.
   *
   * @return the user email
   */
  public String getEmail() {
    return email.get();
  }

  /**
   * Returns the user access level.
   *
   * @return the user access level
   */
  public int getAccessLevel() {
    return accessLevel.get();
  }

  /**
   * Returns the user first name.
   *
   * @return the user first name
   */
  public String getFirstName() {
    return firstName.get();
  }

  /**
   * Returns the user middle name.
   *
   * @return the user middle name
   */
  public String getMiddleName() {
    return middleName.get();
  }

  /**
   * Returns the user last name.
   *
   * @return the user last name
   */
  public String getLastName() {
    return lastName.get();
  }

  /**
   * Returns the user phone number.
   *
   * @return the user phone number
   */
  public String getPhone() {
    return phone.get();
  }

  /**
   * Returns the user date of birth.
   *
   * @return the user date of birth
   */
  public LocalDate getDob() {
    return dob.get();
  }

  /**
   * Returns the user preferred language.
   *
   * @return the user preferred language
   */
  public String getLanguage() {
    return language.get();
  }

  /**
   * Returns the user profile image URL.
   *
   * @return the user profile image URL
   */
  public String getProfileImageUrl() {
    return profileImageUrl.get();
  }

  // Setters

  /**
   * Sets the user ID.
   *
   * @param id the user ID to set
   */
  public void setId(UUID id) {
    this.id.set(id);
  }

  /**
   * Sets the user email.
   *
   * @param email the user email to set
   */
  public void setEmail(String email) {
    this.email.set(email);
  }

  /**
   * Sets the user access level.
   *
   * @param accessLevel the user access level to set
   */
  public void setAccessLevel(int accessLevel) {
    this.accessLevel.set(accessLevel);
  }

  /**
   * Sets the user first name.
   *
   * @param firstName the user first name to set
   */
  public void setFirstName(String firstName) {
    this.firstName.set(firstName);
  }

  /**
   * Sets the user middle name.
   *
   * @param middleName the user middle name to set
   */
  public void setMiddleName(String middleName) {
    this.middleName.set(middleName);
  }

  /**
   * Sets the user last name.
   *
   * @param lastName the user last name to set
   */
  public void setLastName(String lastName) {
    this.lastName.set(lastName);
  }

  /**
   * Sets the user phone number.
   *
   * @param phone the user phone number to set
   */
  public void setPhone(String phone) {
    this.phone.set(phone);
  }

  /**
   * Sets the user date of birth.
   *
   * @param dob the user date of birth to set
   */
  public void setDob(LocalDate dob) {
    this.dob.set(dob);
  }

  /**
   * Sets the user preferred language.
   *
   * @param language the user preferred language to set
   */
  public void setLanguage(String language) {
    this.language.set(language);
  }

  /**
   * Sets the user profile image URL.
   *
   * @param profileImageUrl the user profile image URL to set
   */
  public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl.set(profileImageUrl);
  }
}
