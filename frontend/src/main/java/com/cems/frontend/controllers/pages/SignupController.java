package com.cems.frontend.controllers.pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;

    @FXML
    public void initialize() {
        System.out.println("Signup view loaded");
    }

/// signup button
    @FXML
    private void handleSignup(ActionEvent event) {
        String firstName = firstNameField.getText();
        String email = emailField.getText();

        if (firstName.isEmpty() || email.isEmpty() ) {
            System.out.println("Required fields missing");
            return;
        }

        // Temporary (replace with backend later)
        System.out.println("User registered: " + email);


        goToLogin(event);
    }

    //  Cancel button
    @FXML
    private void handleCancel(ActionEvent event) {
        clearFields();
    }

    //  Redirect to login page
    @FXML
    private void goToLogin(ActionEvent event) {
        switchScene(event, "/org/cems/frontend/view/pages/Login.fxml");
    }

    // Scene switcher
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();

    }
}
