package com.cems.frontend.controllers.components;

import com.cems.frontend.models.NavigationNotifier;
import com.cems.frontend.models.NavigationObserver;
import com.cems.frontend.models.Paths;
import com.cems.frontend.utils.LocalStorage;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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
    private Button englishButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button settingsButton;

    @FXML
    private VBox sidebarRoot;

    @FXML
    private Button usersButton;

    private Button currentHighlighted = null;

    private List<NavigationObserver> observers = new ArrayList<>();

    @FXML
    private void initialize() {
        NavigationNotifier.getInstance().addObserver(this);
        currentHighlighted = allEventsButton;
        allEventsButton.getStyleClass().add("selected");
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
//        SceneNavigator.loadPage("home-view.fxml");
    }

    @FXML
    private void goToAllEvents() {
//        SceneNavigator.loadPage("home-view.fxml");
        NavigationNotifier.getInstance().notifyAllObservers(Paths.HOME);
    }

    @FXML
    private void goToCreateEvent() {
//        SceneNavigator.loadPage("create-event-view.fxml");
    }

    @FXML
    private void goToSettings() {
//        SceneNavigator.loadPage("UserSettings.xml");
        NavigationNotifier.getInstance().notifyAllObservers(Paths.USER_SETTINGS);
    }

    @FXML
    private void handleLogout() {
        LocalStorage.set("token", "");
        SceneNavigator.loadPage("login-view.fxml");
    }

    public void removeAllNavigationObservers() {
        observers = new ArrayList<>();
    }

    public void setSidebarSelected(Paths path) {
        Platform.runLater(() -> {
            if (path.equals(Paths.HOME)) {
                clearSelected();
                allEventsButton.getStyleClass().removeAll("regular");
                allEventsButton.getStyleClass().add("select");
                currentHighlighted = allEventsButton;
            } else if (path.equals(Paths.USER_SETTINGS)) {
                clearSelected();
                settingsButton.getStyleClass().removeAll("regular");
                settingsButton.getStyleClass().add("select");
                currentHighlighted = settingsButton;
            }
        });
    }

    private void clearSelected() {
        if (currentHighlighted != null) {
            currentHighlighted.getStyleClass().removeAll("selected");
            currentHighlighted.getStyleClass().add("regular");
        }
    }

    @Override
    public void setPage(Paths page) {
        setSidebarSelected(page);
    }
}