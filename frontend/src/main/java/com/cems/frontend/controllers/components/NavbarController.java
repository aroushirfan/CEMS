package com.cems.frontend.controllers.components;

import javafx.fxml.FXML;
import com.cems.frontend.view.SceneNavigator;

public class NavbarController {

    @FXML
    private void handleLoginRedirect() {
        SceneNavigator.loadPage("login-view.fxml");
    }

    @FXML
    private void handleSignupRedirect() {
        SceneNavigator.loadPage("signup-view.fxml");
    }
}