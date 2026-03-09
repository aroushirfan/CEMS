package com.cems.frontend.controllers.components;

import com.cems.frontend.services.AuthService;
import com.cems.frontend.utils.LocalStorage;
import javafx.fxml.FXML;
import com.cems.frontend.view.SceneNavigator;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class NavbarController {
    public static NavbarController instance;
    @FXML private Label loginLabel;
    @FXML private Button signupBtn;

    @FXML
    public void initialize() {
        instance = this;
        refreshVisibility();
    }

    public void refreshVisibility() {
        String token = LocalStorage.get("token");
        boolean isLoggedIn = token != null && !token.isBlank();

        loginLabel.setVisible(!isLoggedIn);
        loginLabel.setManaged(!isLoggedIn);

        signupBtn.setVisible(!isLoggedIn);
        signupBtn.setManaged(!isLoggedIn);
    }

    @FXML
    private HBox navBarHBox;

    @FXML
    private void handleLoginRedirect() {
        SceneNavigator.loadPage("Login.fxml");
    }

    @FXML
    private void handleSignupRedirect() {
        SceneNavigator.loadPage("SignUp.fxml");
    }
}