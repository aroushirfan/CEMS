package com.cems.shared.model;

import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String email;
    private int accessLevel;
    private String firstName;
    private String middleName;
    private String lastName;

    public UserDTO() {}

    public UserDTO(UUID id, String email, int accessLevel, String firstName, String middleName, String lastName) {
        this.id = id;
        this.email = email;
        this.accessLevel = accessLevel;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public int getAccessLevel() { return accessLevel; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }

    public void setId(UUID id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setAccessLevel(int accessLevel) { this.accessLevel = accessLevel; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}