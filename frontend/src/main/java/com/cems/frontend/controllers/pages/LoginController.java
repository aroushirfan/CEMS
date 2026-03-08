package com.cems.frontend.controllers.pages;


import com.cems.frontend.controllers.components.NavbarController;
import com.cems.frontend.controllers.components.SidebarController;
import com.cems.frontend.services.AuthService;
import com.cems.frontend.utils.LocalStorage;
import com.cems.frontend.view.SceneNavigator;
import com.cems.shared.model.AuthDTO;
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
        goToHome(event);
    }

    @FXML
    private void handlePasswordReset(ActionEvent event) {
        System.out.println("Password reset clicked");
        //  redirect to reset-password page later
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


        // TEMP Auth(replace with backend later)
//        if (username.equals("admin") && password.equals("admin")) {
//            System.out.println("Login successful");
//            goToHome(event);
//        } else {
//            System.out.println("Invalid credentials");
//        }
        try {
            AuthDTO.AuthResponseDTO response = authService.login(username, password);

            if (response != null) {
                LocalStorage.set("token", response.getToken());
                LocalStorage.set("role", response.getRole());
                if (SidebarController.instance != null) {
                    SidebarController.instance.refreshVisibility();
                }
                if (NavbarController.instance != null) {
                    NavbarController.instance.refreshVisibility();
                }

                String role = response.getRole();

                if ("ADMIN".equals(role)) {
                    SceneNavigator.loadPage("admin-page.fxml");
                } else {
                    SceneNavigator.loadPage("home-view.fxml");

                }

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
        switchScene(event, "/com/cems/frontend/view/pages/Signup.fxml");
    }

    // Redirect to home page
    private void goToHome(ActionEvent event) {
        switchScene(event, "/com/cems/frontend/view/pages/navigation.fxml");
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
