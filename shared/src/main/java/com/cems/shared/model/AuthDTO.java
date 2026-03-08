package com.cems.shared.model;

import jakarta.validation.constraints.NotBlank;

public class AuthDTO {

    static public class RegisterRequestDTO {

        @NotBlank
        private String firstName;
        private String middleName;
        private String lastName;
        @NotBlank
        private String email;
        @NotBlank
        private String password;
        @NotBlank
        private String repeatPassword;

        public RegisterRequestDTO(String firstName, String middleName, String lastName, String email, String password, String repeatPassword) {
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.repeatPassword = repeatPassword;
        }

        public RegisterRequestDTO() {
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRepeatPassword() {
            return repeatPassword;
        }

        public void setRepeatPassword(String repeatPassword) {
            this.repeatPassword = repeatPassword;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }
    }

    static public class LoginRequestDTO {
        @NotBlank
        private String email;
        @NotBlank
        private String password;

        public LoginRequestDTO(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public LoginRequestDTO() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    static public class AuthResponseDTO {
        private String token;

        public AuthResponseDTO(String token) {
            this.token = token;
        }

        public AuthResponseDTO() {
        }

        public String getToken() {
            return token;
        }
    }

}
