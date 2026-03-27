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
}
