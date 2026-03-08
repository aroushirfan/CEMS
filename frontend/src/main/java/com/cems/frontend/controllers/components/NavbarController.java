package com.cems.frontend.controllers.components;

import com.cems.frontend.services.AuthService;
import javafx.fxml.FXML;
import com.cems.frontend.view.SceneNavigator;
import javafx.scene.layout.HBox;

public class NavbarController {

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

    @FXML
    private void initialize() {
        navBarHBox.setVisible(AuthService.getInstance().getToken().isEmpty());
    }
}