package com.cems.frontend.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import com.cems.frontend.view.SceneNavigator;

public class SidebarController {

    @FXML private VBox sidebarRoot;
    @FXML private ToggleButton darkModeToggle;

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
        String darkTheme = getClass().getResource("/com/cems/frontend/view/css/dark-theme.css").toExternalForm();
        if (!scene.getStylesheets().contains(darkTheme)) {
            scene.getStylesheets().add(darkTheme);
        }
    }

    private void removeDarkMode(Scene scene) {
        String darkTheme = getClass().getResource("/com/cems/frontend/view/css/dark-theme.css").toExternalForm();
        scene.getStylesheets().remove(darkTheme);
    }

    @FXML
    private void goToHome() {
        SceneNavigator.loadPage("home-view.fxml");
    }

    @FXML
    private void goToAllEvents() {
        SceneNavigator.loadPage("home-view.fxml");
    }

    @FXML
    private void goToCreateEvent() {
        SceneNavigator.loadPage("create-event-view.fxml");
    }

    @FXML
    private void handleLogout() {
        SceneNavigator.loadPage("login-view.fxml");
    }
}