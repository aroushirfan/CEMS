package com.cems.frontend.controllers.components;

import com.cems.frontend.utils.LocalStorage;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import com.cems.frontend.view.SceneNavigator;

public class SidebarController {

    @FXML private VBox sidebarRoot;
    @FXML private ToggleButton darkModeToggle;
    @FXML private Button adminEventBtn;
    @FXML private Button adminUserBtn;
    @FXML private Button logoutButton;
    public static SidebarController instance;

    @FXML
    public void initialize() {
        instance=this;
        refreshVisibility();
    }

    public void refreshVisibility() {
        String role = LocalStorage.get("role");
        String token = LocalStorage.get("token");

        boolean isLoggedIn = token != null && !token.isBlank();
        boolean isAdmin = "ADMIN".equals(role);

        adminEventBtn.setVisible(isLoggedIn && isAdmin);
        adminUserBtn.setVisible(isLoggedIn && isAdmin);
        logoutButton.setVisible(isLoggedIn);
    }

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
    private void goToSettings() {
        SceneNavigator.loadPage("UserSettings.fxml");
    }
    @FXML
    private void handleEvents() {
        SceneNavigator.loadPage("admin-page.fxml");
    }

    @FXML
    private void handleUsers() {
        SceneNavigator.loadPage("user-management.fxml");
    }
    @FXML
    private void handleLogout() {
        LocalStorage.set("token", "");
        LocalStorage.set("role", "");
        refreshVisibility();
        if (NavbarController.instance != null) {
            NavbarController.instance.refreshVisibility();
        }
        SceneNavigator.loadPage("home-view.fxml");
    }
}