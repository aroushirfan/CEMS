package com.cems.frontend.controllers.pages;


import com.cems.frontend.services.AuthService;
import com.cems.frontend.utils.Language;
import com.cems.frontend.utils.LocalStorage;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.frontend.view.SceneNavigator;
import com.cems.shared.model.AuthDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML private AnchorPane rootView;

    private final AuthService authService = AuthService.getInstance();

    @FXML
    public void initialize() {
        System.out.println("Login view loaded");
        setOrientation();
    }

    public void setOrientation(){
        boolean ltr = LocaleUtil.getInstance().getLanguage().equals(Language.UR);
        rootView.setNodeOrientation(!ltr ?NodeOrientation.LEFT_TO_RIGHT:NodeOrientation.RIGHT_TO_LEFT);
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
