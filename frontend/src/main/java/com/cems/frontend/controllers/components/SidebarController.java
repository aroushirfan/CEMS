package com.cems.frontend.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import com.cems.frontend.view.SceneNavigator;

public class SidebarController {

    @FXML private VBox sidebarRoot;
    @FXML private ToggleButton darkModeToggle;

    /**
     * Toggles between light and dark themes.
     */
    @FXML
    private void handleDarkMode() {
        Scene scene = sidebarRoot.getScene();
        if (scene == null) return;

        if (darkModeToggle.isSelected()) {
            darkModeToggle.setText("ON");
            applyDarkMode(scene);
        } else {
            darkModeToggle.setText("OFF");
            removeDarkMode(scene);
        }
    }

    private void applyDarkMode(Scene scene) {
        String darkTheme = getClass().getResource("/com/cems.frontend.view/css/dark-theme.css").toExternalForm();
        if (!scene.getStylesheets().contains(darkTheme)) {
            scene.getStylesheets().add(darkTheme);
        }
    }

    private void removeDarkMode(Scene scene) {
        String darkTheme = getClass().getResource("/com/cems.frontend.view/css/dark-theme.css").toExternalForm();
        scene.getStylesheets().remove(darkTheme);
    }

    @FXML
    private void goToHome() {
        SceneNavigator.loadPage("home-view.fxml");
    }

    @FXML
    private void goToAllEvents() {
        // All Events
        SceneNavigator.loadPage("home-view.fxml");
    }

    @FXML
    private void handleLogout() {
        // Uses utility to return to the login screen
        SceneNavigator.loadPage("login-view.fxml");
    }
}