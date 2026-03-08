package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.NavigationNotifier;
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.AuthService;
import com.cems.frontend.view.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    private AuthService authService = AuthService.getInstance();

    @FXML
    public void initialize() {
        System.out.println("Signup view loaded");
    }

    /// signup button
    @FXML
    private void handleSignup(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (firstName.isEmpty() || email.isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Required fields missing");
            alert.show();
            return;
        }

        // Temporary (replace with backend later)
//        System.out.println("User registered: " + email);
        try {
            authService.signUp(firstName, lastName, email, password, confirmPassword);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getLocalizedMessage());
            alert.show();
            clearFields();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Sign Up Successful. Please login.");
        alert.show();

        alert.setOnCloseRequest((dialogEvent) -> {
            SceneNavigator.loadPage("Login.fxml");
        });
    }

    //  Cancel button
    @FXML
    private void handleCancel(ActionEvent event) {
        clearFields();
    }

    //  Redirect to login page
    @FXML
    private void goToLogin(ActionEvent event) {
        SceneNavigator.loadPage("Login.fxml");
    }

    // Scene switcher
   // private void switchScene(ActionEvent event, String fxmlPath) {
     //   try {
       //     Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
         //   Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
           // stage.setScene(new Scene(root));
        //} catch (IOException e) {
          //  e.printStackTrace();
        //}
    //}

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
}
