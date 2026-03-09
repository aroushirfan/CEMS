//package com.cems.frontend.controllers.components;
//
//import com.cems.frontend.utils.LocalStorage;
//import javafx.fxml.FXML;
//import javafx.scene.Scene;
//import javafx.scene.control.ToggleButton;
//import javafx.scene.layout.VBox;
//import com.cems.frontend.view.SceneNavigator;
//
//public class SidebarController {
//
//    @FXML private VBox sidebarRoot;
//    @FXML private ToggleButton darkModeToggle;
//
//    @FXML
//    private void handleDarkMode() {
//        Scene scene = sidebarRoot.getScene();
//        if (scene == null) return;
//
//        if (darkModeToggle.isSelected()) {
//            darkModeToggle.setText("ON");
//            applyDarkMode(scene);
//        } else {
//            darkModeToggle.setText("OFF");
//            removeDarkMode(scene);
//        }
//    }
//
//    private void applyDarkMode(Scene scene) {
//        String darkTheme = getClass().getResource("/com/cems/frontend/view/css/dark-theme.css").toExternalForm();
//        if (!scene.getStylesheets().contains(darkTheme)) {
//            scene.getStylesheets().add(darkTheme);
//        }
//    }
//
//    private void removeDarkMode(Scene scene) {
//        String darkTheme = getClass().getResource("/com/cems/frontend/view/css/dark-theme.css").toExternalForm();
//        scene.getStylesheets().remove(darkTheme);
//    }
//
//    @FXML
//    private void goToHome() {
//        SceneNavigator.loadPage("MainHome.fxml");
/// /        SceneNavigator.loadPage("home-view.fxml");
//    }
//
//    @FXML
//    private void goToAllEvents() {
//        SceneNavigator.loadPage("home-view.fxml");
//    }
//
//    @FXML
//    private void goToCreateEvent() {
//        SceneNavigator.loadPage("create-event-view.fxml");
//    }
//
////    @FXML
////    private void goToSettings() {
////        SceneNavigator.loadPage("/com/cems/frontend/view/pages/UserSettings.fxml");
////    }
//    @FXML
//    private void goToSettings() {
//         System.out.println("Settings clicked!");
//         SceneNavigator.loadPage("/com/cems/frontend/view/pages/UserSettings.fxml");
//}
//
//    @FXML
//    private void handleLogout() {
//        LocalStorage.set("token", "");
//        SceneNavigator.loadPage("login-view.fxml");
//    }
//}
//
//package com.cems.frontend.controllers.components;
//
//import com.cems.frontend.utils.LocalStorage;
//import javafx.fxml.FXML;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.ToggleButton;
//import javafx.scene.layout.VBox;
//import com.cems.frontend.view.SceneNavigator;
//
//public class SidebarController {
//
//    @FXML private VBox sidebarRoot;
//    @FXML private ToggleButton darkModeToggle;
//
//    // Add all sidebar buttons so you can highlight them later
//    @FXML private Button homeBtn;
//    @FXML private Button allEventsBtn;
//    @FXML private Button createEventBtn;
//    @FXML private Button usersBtn;
//    @FXML private Button settingsBtn;
//    @FXML private Button languageBtn;
//    @FXML private Button logoutButton;
//
//
//    @FXML
//    private void handleDarkMode() {
//        Scene scene = sidebarRoot.getScene();
//        if (scene == null) return;
//
//        if (darkModeToggle.isSelected()) {
//            darkModeToggle.setText("ON");
//            applyDarkMode(scene);
//        } else {
//            darkModeToggle.setText("OFF");
//            removeDarkMode(scene);
//        }
//    }
//
//    private void applyDarkMode(Scene scene) {
//        String darkTheme = getClass()
//                .getResource("/com/cems/frontend/view/css/dark-theme.css")
//                .toExternalForm();
//
//        if (!scene.getStylesheets().contains(darkTheme)) {
//            scene.getStylesheets().add(darkTheme);
//        }
//    }
//
//    private void removeDarkMode(Scene scene) {
//        String darkTheme = getClass()
//                .getResource("/com/cems/frontend/view/css/dark-theme.css")
//                .toExternalForm();
//
//        scene.getStylesheets().remove(darkTheme);
//    }
//
//
//
//    // -------------------------
//    // Navigation
//    // -------------------------
//
//    @FXML
//    private void goToHome() {
//        SceneNavigator.loadPage("MainHome.fxml");
//        highlight(homeBtn);
//    }
//
//    @FXML
//    private void goToAllEvents() {
//        SceneNavigator.loadPage("home-view.fxml");
//        highlight(allEventsBtn);
//    }
//
//    @FXML
//    private void goToCreateEvent() {
//        SceneNavigator.loadPage("create-event-view.fxml");
//        highlight(createEventBtn);
//    }
//
//    @FXML
//    private void goToSettings() {
//        SceneNavigator.loadPage("UserSettings.fxml");
//        highlight(settingsBtn);
//    }
//
//    @FXML
//    private void handleLogout() {
//        LocalStorage.set("token", "");
//        SceneNavigator.loadPage("login-view.fxml");
//    }
//
//    // -------------------------
//    // Highlight active button
//    // -------------------------
//
//    private void highlight(Button active) {
//        resetAll();
//        active.getStyleClass().add("active");
//    }
//
//
//
//
//    private void resetAll() {
//        reset(homeBtn);
//        reset(allEventsBtn);
//        reset(createEventBtn);
//        reset(usersBtn);
//        reset(settingsBtn);
//        reset(languageBtn);
//    }
//
//    private void reset(Button btn) {
//        btn.getStyleClass().remove("active");
//    }
//}
package com.cems.frontend.controllers.components;

import com.cems.frontend.models.NavigationNotifier;
import com.cems.frontend.models.NavigationObserver;
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.AuthService;
import com.cems.frontend.utils.LocalStorage;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import com.cems.frontend.view.SceneNavigator;

import java.util.ArrayList;
import java.util.List;

public class SidebarController implements NavigationObserver {

    @FXML
    private Button allEventsButton;

    @FXML
    private Button createEventBtn;

    @FXML
    private ToggleButton darkModeToggle;

    @FXML
    private Button homeButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button settingsButton;

    @FXML
    private VBox sidebarRoot;
    @FXML
    private Button eventManagementBtn;
    @FXML
    private Button userManagementBtn;

    private Button currentHighlighted = null;

    private List<NavigationObserver> observers = new ArrayList<>();

    @FXML
    private void initialize() {
        NavigationNotifier.getInstance().addObserver(this);
        refreshVisibility();
        currentHighlighted = homeButton;
        homeButton.getStyleClass().removeAll("regular");
        homeButton.getStyleClass().add("select");

        logoutButton.setVisible(!AuthService.getInstance().getToken().isEmpty());
    }

    public void refreshVisibility() {
        String role = LocalStorage.get("role");
        boolean loggedIn = role != null && !role.isBlank();

        createEventBtn.setVisible(false);
        createEventBtn.setManaged(false);

        settingsButton.setVisible(false);
        settingsButton.setManaged(false);

        eventManagementBtn.setVisible(false);
        eventManagementBtn.setManaged(false);

        userManagementBtn.setVisible(false);
        userManagementBtn.setManaged(false);

        logoutButton.setVisible(false);
        logoutButton.setManaged(false);
        if (loggedIn) {
            settingsButton.setVisible(true);
            settingsButton.setManaged(true);

            logoutButton.setVisible(true);
            logoutButton.setManaged(true);
        }
        if ("FACULTY".equals(role) || "ADMIN".equals(role)) {
            createEventBtn.setVisible(true);
            createEventBtn.setManaged(true);
        }

        if ("ADMIN".equals(role)) {
            eventManagementBtn.setVisible(true);
            eventManagementBtn.setManaged(true);
            userManagementBtn.setVisible(true);
            userManagementBtn.setManaged(true);
        }
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
        String darkTheme = getClass()
                .getResource("/com/cems/frontend/view/css/dark-theme.css")
                .toExternalForm();

        if (!scene.getStylesheets().contains(darkTheme)) {
            scene.getStylesheets().add(darkTheme);
        }
    }

    private void removeDarkMode(Scene scene) {
        String darkTheme = getClass()
                .getResource("/com/cems/frontend/view/css/dark-theme.css")
                .toExternalForm();

        scene.getStylesheets().remove(darkTheme);
    }
    // Navigation


    @FXML
    private void goToHome() {
//        SceneNavigator.loadPage("home-view.fxml");
        NavigationNotifier.getInstance().notifyAllObservers(Paths.HOME);
    }

    @FXML
    private void goToAllEvents() {
//        SceneNavigator.loadPage("home-view.fxml");
        NavigationNotifier.getInstance().notifyAllObservers(Paths.ALL_EVENTS);
    }

    @FXML
    private void goToCreateEvent() {
//        SceneNavigator.loadPage("create-event-view.fxml");
        NavigationNotifier.getInstance().notifyAllObservers(Paths.CREATE_EVENT);
    }

    @FXML
    private void goToSettings() {
//        SceneNavigator.loadPage("UserSettings.xml");
        NavigationNotifier.getInstance().notifyAllObservers(Paths.USER_SETTINGS);
    }

    @FXML
    private void handleLogout() {
        LocalStorage.remove("role");
        LocalStorage.remove("token");
        SceneNavigator.loadPage("navigation.fxml");
        NavigationNotifier.getInstance().notifyAllObservers(Paths.ALL_EVENTS);
    }

    public void removeAllNavigationObservers() {
        observers = new ArrayList<>();
    }

    public void setSidebarSelected(Paths path) {
        switch (path) {
            case HOME:
                selectButton(homeButton);
                break;
            case ALL_EVENTS:
                selectButton(allEventsButton);
                break;
            case USER_SETTINGS:
                selectButton(settingsButton);
                break;
            case USER_MANAGEMENT:
                selectButton(userManagementBtn);
                break;
            case EVENT_MANAGEMENT:
                selectButton(eventManagementBtn);
                break;
            case CREATE_EVENT:
                selectButton(createEventBtn);
                break;
        }
    }

    private void clearSelected() {
        if (currentHighlighted != null) {
            currentHighlighted.getStyleClass().removeAll("select");
            currentHighlighted.getStyleClass().add("regular");
        }
    }

    private void selectButton(Button button) {
        clearSelected();
        button.getStyleClass().removeAll("regular");
        button.getStyleClass().add("select");
        currentHighlighted = button;
    }

    @FXML
    private void goToEventManagement() {
        NavigationNotifier.getInstance().notifyAllObservers(Paths.EVENT_MANAGEMENT);
    }

    @FXML
    private void goToUserManagement() {
        NavigationNotifier.getInstance().notifyAllObservers(Paths.USER_MANAGEMENT);
    }


    @Override
    public void setPage(Paths page) {
        refreshVisibility();
        setSidebarSelected(page);
    }
} 