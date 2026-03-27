//package com.cems.frontend.models;
//
//import javafx.beans.property.*;
//import java.util.UUID;
//
//public class User {
//
//    private final ObjectProperty<UUID> id = new SimpleObjectProperty<>();
//    private final StringProperty email = new SimpleStringProperty();
//    private final IntegerProperty accessLevel = new SimpleIntegerProperty();
//    private final StringProperty firstName = new SimpleStringProperty();
//    private final StringProperty middleName = new SimpleStringProperty();
//    private final StringProperty lastName = new SimpleStringProperty();
//
//    public User() {}
//
//    public User(UUID id, String email, int accessLevel,
//                String firstName, String middleName, String lastName) {
//
//        this.id.set(id);
//        this.email.set(email);
//        this.accessLevel.set(accessLevel);
//        this.firstName.set(firstName);
//        this.middleName.set(middleName);
//        this.lastName.set(lastName);
//    }
//
//    // Properties for JavaFX binding
//    public ObjectProperty<UUID> idProperty() { return id; }
//    public StringProperty emailProperty() { return email; }
//    public IntegerProperty accessLevelProperty() { return accessLevel; }
//    public StringProperty firstNameProperty() { return firstName; }
//    public StringProperty middleNameProperty() { return middleName; }
//    public StringProperty lastNameProperty() { return lastName; }
//
//    // Getters
//    public UUID getId() { return id.get(); }
//    public String getEmail() { return email.get(); }
//    public int getAccessLevel() { return accessLevel.get(); }
//    public String getFirstName() { return firstName.get(); }
//    public String getMiddleName() { return middleName.get(); }
//    public String getLastName() { return lastName.get(); }
//
//    // Setters
//    public void setId(UUID id) { this.id.set(id); }
//    public void setEmail(String email) { this.email.set(email); }
//    public void setAccessLevel(int accessLevel) { this.accessLevel.set(accessLevel); }
//    public void setFirstName(String firstName) { this.firstName.set(firstName); }
//    public void setMiddleName(String middleName) { this.middleName.set(middleName); }
//    public void setLastName(String lastName) { this.lastName.set(lastName); }
//}


package com.cems.frontend.models;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.util.UUID;

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

    public User() {}


    public ObjectProperty<UUID> idProperty() { return id; }
    public StringProperty emailProperty() { return email; }
    public IntegerProperty accessLevelProperty() { return accessLevel; }
    public StringProperty firstNameProperty() { return firstName; }
    public StringProperty middleNameProperty() { return middleName; }
    public StringProperty lastNameProperty() { return lastName; }

    public StringProperty phoneProperty() { return phone; }
    public ObjectProperty<LocalDate> dobProperty() { return dob; }
    public StringProperty languageProperty() { return language; }
    public StringProperty profileImageUrlProperty() { return profileImageUrl; }

    // Getters
    public UUID getId() { return id.get(); }
    public String getEmail() { return email.get(); }
    public int getAccessLevel() { return accessLevel.get(); }
    public String getFirstName() { return firstName.get(); }
    public String getMiddleName() { return middleName.get(); }
    public String getLastName() { return lastName.get(); }

    public String getPhone() { return phone.get(); }
    public LocalDate getDob() { return dob.get(); }
    public String getLanguage() { return language.get(); }
    public String getProfileImageUrl() { return profileImageUrl.get(); }

    // Setters
    public void setId(UUID id) { this.id.set(id); }
    public void setEmail(String email) { this.email.set(email); }
    public void setAccessLevel(int accessLevel) { this.accessLevel.set(accessLevel); }
    public void setFirstName(String firstName) { this.firstName.set(firstName); }
    public void setMiddleName(String middleName) { this.middleName.set(middleName); }
    public void setLastName(String lastName) { this.lastName.set(lastName); }

    public void setPhone(String phone) { this.phone.set(phone); }
    public void setDob(LocalDate dob) { this.dob.set(dob); }
    public void setLanguage(String language) { this.language.set(language); }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl.set(profileImageUrl); }
}
