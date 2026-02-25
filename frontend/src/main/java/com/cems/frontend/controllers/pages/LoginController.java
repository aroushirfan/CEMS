package com.cems.frontend.controllers.pages;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    public void initialize() {
        System.out.println("Login view loaded");

    }
    @FXML
    private void handleCancel(ActionEvent event) {
        // Clear fields or close app
        usernameField.clear();
        passwordField.clear();
    }
    @FXML
    private void handlePasswordReset(ActionEvent event) {
        System.out.println("Password reset clicked");
        //  redirect to reset-password page later
    }




    //Login button
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username or password empty");
            return;
        }


        // TEMP Auth(replace with backend later)
        if (username.equals("admin") && password.equals("admin")) {
            System.out.println("Login successful");
            goToHome(event);
        } else {
            System.out.println("Invalid credentials");
        }
    }

    //Go to signup page
    @FXML
    private void goToSignup(ActionEvent event) {
        switchScene(event, "/org/cems/frontend/view/pages/signup.fxml");
    }

    // Redirect to home page
    private void goToHome(ActionEvent event) {
        switchScene(event, "/com/cems.frontend.view/pages/home-view.fxml");
    }

    //  Scene switch helper
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
