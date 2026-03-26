package com.cems.frontend.controllers.pages;


import com.cems.frontend.services.AuthService;
import com.cems.frontend.utils.LocalStorage;
import com.cems.frontend.view.SceneNavigator;
import com.cems.shared.model.AuthDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    private final AuthService authService = AuthService.getInstance();

    @FXML
    public void initialize() {
        System.out.println("Login view loaded");
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        // Clear fields or close app
        emailField.clear();
        passwordField.clear();
        SceneNavigator.loadPage("navigation.fxml");
    }

    @FXML
    private void handlePasswordReset(ActionEvent event) {
        System.out.println("Password reset clicked");
    }


    //Login button
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = emailField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username or password empty");
            return;
        }

        try {
            AuthDTO.AuthResponseDTO response = authService.login(username, password);

            if (response != null) {
                LocalStorage.set("token", response.getToken());
                LocalStorage.set("role", response.getRole());
                SceneNavigator.loadPage("navigation.fxml");
            } else {
                System.out.println("Invalid credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Go to signup page
    @FXML
    private void goToSignup(ActionEvent event) {
        SceneNavigator.loadPage("Signup.fxml");
    }
}
