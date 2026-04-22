//package com.cems.shared.model;
//
//import java.util.UUID;
//
//public class UserDTO {
//    private UUID id;
//    private String email;
//    private int accessLevel;
//    private String firstName;
//    private String middleName;
//    private String lastName;
//
//    public UserDTO() {}
//
//    public UserDTO(UUID id, String email, int accessLevel, String firstName, String middleName, String lastName) {
//        this.id = id;
//        this.email = email;
//        this.accessLevel = accessLevel;
//        this.firstName = firstName;
//        this.middleName = middleName;
//        this.lastName = lastName;
//    }
//
//    public UUID getId() { return id; }
//    public String getEmail() { return email; }
//    public int getAccessLevel() { return accessLevel; }
//    public String getFirstName() { return firstName; }
//    public String getMiddleName() { return middleName; }
//    public String getLastName() { return lastName; }
//
//    public void setId(UUID id) { this.id = id; }
//    public void setEmail(String email) { this.email = email; }
//    public void setAccessLevel(int accessLevel) { this.accessLevel = accessLevel; }
//    public void setFirstName(String firstName) { this.firstName = firstName; }
//    public void setMiddleName(String middleName) { this.middleName = middleName; }
//    public void setLastName(String lastName) { this.lastName = lastName; }
//}
package com.cems.shared.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class UserDTO {

    private UUID id;
    private String email;
    private int accessLevel;
    private String firstName;
    private String middleName;
    private String lastName;


    private String phone;
    private LocalDate dob;

    private String profileImageUrl;

    public UserDTO() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return getAccessLevel() == userDTO.getAccessLevel() && Objects.equals(getId(), userDTO.getId()) && Objects.equals(getEmail(), userDTO.getEmail()) && Objects.equals(getFirstName(), userDTO.getFirstName()) && Objects.equals(getMiddleName(), userDTO.getMiddleName()) && Objects.equals(getLastName(), userDTO.getLastName()) && Objects.equals(getPhone(), userDTO.getPhone()) && Objects.equals(getDob(), userDTO.getDob()) && Objects.equals(getProfileImageUrl(), userDTO.getProfileImageUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getAccessLevel(), getFirstName(), getMiddleName(), getLastName(), getPhone(), getDob(), getProfileImageUrl());
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", accessLevel=" + accessLevel +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", dob=" + dob +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}
