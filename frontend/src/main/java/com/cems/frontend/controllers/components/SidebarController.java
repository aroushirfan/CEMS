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

import com.cems.frontend.controllers.pages.NavigationController;
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.AuthService;
import com.cems.frontend.utils.Language;
import com.cems.frontend.utils.LocalStorage;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.frontend.view.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import java.util.Map;
import java.util.ResourceBundle;

public class SidebarController {

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

    @FXML
    private ComboBox<String> languageComboBox;

    private Button activeButton;

    private Map<Paths, Button> pathMap;

    @FXML
    private void initialize() {
        refreshVisibility();
        logoutButton.setVisible(!AuthService.getInstance().getToken().isEmpty());

        languageComboBox.getItems().addAll(Language.EN.getDisplayName(),Language.TH.getDisplayName(),Language.UR.getDisplayName());

        this.pathMap = Map.of(
                Paths.CREATE_EVENT, createEventBtn,
                Paths.EVENT_MANAGEMENT, eventManagementBtn,
                Paths.USER_MANAGEMENT, userManagementBtn,
                Paths.ALL_EVENTS, allEventsButton,
                Paths.USER_SETTINGS, settingsButton,
                Paths.HOME,homeButton);

        languageComboBox.setValue(LocaleUtil.getInstance().getLanguage().getDisplayName());
    }

    public void handleLanguageChange(){
        if (languageComboBox.getValue().equalsIgnoreCase(LocaleUtil.getInstance().getLanguage().getDisplayName())) return;
        System.out.println("Changing language to "+LocaleUtil.getInstance().getLocale().getDisplayName());
        LocaleUtil.getInstance().setLocale(Language.fromDisplayName(languageComboBox.getValue()));
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

        // Retrieve the ResourceBundle used to load this FXML
        ResourceBundle bundle = (ResourceBundle) sidebarRoot.getProperties().get("org.openjfx.resourcebundle");

        // Fallback if bundle is null (for safety)
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("Bundles", java.util.Locale.getDefault());
        }
        if (darkModeToggle.isSelected()) {
            darkModeToggle.setText(bundle.getString("sidebar.toggle_button.on"));
            applyDarkMode(scene);
        } else {
            darkModeToggle.setText(bundle.getString("sidebar.toggle_button.off"));
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
        SceneNavigator.loadContent(Paths.HOME);
    }

    @FXML
    private void goToAllEvents() {
        SceneNavigator.loadContent(Paths.ALL_EVENTS);
    }

    @FXML
    private void goToCreateEvent() {
        SceneNavigator.loadContent(Paths.CREATE_EVENT);
    }

    @FXML
    private void goToSettings() {
        SceneNavigator.loadContent(Paths.USER_SETTINGS);
    }

    @FXML
    private void handleLogout() {
        LocalStorage.remove("role");
        LocalStorage.remove("token");
        refreshVisibility();
        SceneNavigator.loadContent(Paths.HOME);
    }

    private void setActiveButton(Button selectedButton) {
        if (selectedButton == null) return;
        if (activeButton != null) {
            activeButton.getStyleClass().remove("active");
        }
        selectedButton.getStyleClass().add("active");
        activeButton = selectedButton;
    }

    private void removeActiveButton() {
        activeButton.getStyleClass().remove("active");
        activeButton = null;
    }

    public void setActiveByPath(Paths fxmlPath) {
        if (pathMap == null) return;
        Button btn = pathMap.get(fxmlPath);
        if (btn == null) {
            removeActiveButton();
            return;
        }
        setActiveButton(btn);
    }

    @FXML
    private void goToEventManagement() {
        SceneNavigator.loadContent(Paths.EVENT_MANAGEMENT);
    }

    @FXML
    private void goToUserManagement() {
        SceneNavigator.loadContent(Paths.USER_MANAGEMENT);
    }
}
