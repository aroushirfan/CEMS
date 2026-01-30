package org.cems.frontend.controllers.components;

import javafx.fxml.FXML;
import org.cems.frontend.view.SceneNavigator;

public class NavbarController {

    @FXML
    private void handleLoginRedirect() {
        // Redirects to the login page using navigator
        SceneNavigator.loadPage("login-view.fxml");
    }

    @FXML
    private void handleSignupRedirect() {
        // Redirects to signup (create signup-view.fxml later)
        SceneNavigator.loadPage("signup-view.fxml");
    }
}