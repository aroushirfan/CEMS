package com.cems.frontend.controllers.components;

import com.cems.frontend.controllers.pages.NavigationController;
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.AuthService;
import com.cems.frontend.utils.Language;
import com.cems.frontend.utils.LocalStorage;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.frontend.view.SceneNavigator;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

/**
 * Controller for the sidebar navigation component.
 * Manages button visibility based on user role, dark mode toggling, and navigation.
 */
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

    languageComboBox.getItems().addAll(
        Language.EN.getDisplayName(), Language.TH.getDisplayName(),
        Language.UR.getDisplayName());

    this.pathMap = Map.of(
        Paths.CREATE_EVENT, createEventBtn,
        Paths.EVENT_MANAGEMENT, eventManagementBtn,
        Paths.USER_MANAGEMENT, userManagementBtn,
        Paths.ALL_EVENTS, allEventsButton,
        Paths.USER_SETTINGS, settingsButton,
        Paths.HOME, homeButton);

    languageComboBox.setValue(LocaleUtil.getInstance().getLanguage().getDisplayName());
  }
  /**
   * Handles language change when a new language is selected from the ComboBox.
   * Updates the application's locale accordingly.
   */

  public void handleLanguageChange() {
    if (languageComboBox.getValue().equalsIgnoreCase(
        LocaleUtil.getInstance().getLanguage().getDisplayName())) {
      return;
    }

    LocaleUtil.getInstance()
        .setLocale(Language.fromDisplayName(languageComboBox.getValue()));
  }

  /**
   * Refreshes the visibility of sidebar buttons based on the user's authentication status and role.
   * Shows or hides buttons according to the permissions associated with the user's role.
   */
  public void refreshVisibility() {

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
    String role = LocalStorage.get("role");
    boolean loggedIn = role != null && !role.isBlank();
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
    if (scene == null) {
      return;
    }

    // Retrieve the ResourceBundle used to load this FXML
    ResourceBundle bundle = (ResourceBundle) sidebarRoot.getProperties()
        .get("org.openjfx.resourcebundle");

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
    SceneNavigator.refreshNavbar();
    SceneNavigator.loadContent(Paths.HOME);
  }

  private void setActiveButton(Button selectedButton) {
    if (selectedButton == null) {
      return;
    }
    if (activeButton != null) {
      activeButton.getStyleClass().remove("active");
    }
    selectedButton.getStyleClass().add("active");
    activeButton = selectedButton;
  }

  private void removeActiveButton() {
    if (activeButton == null) {
      return;
    }
    activeButton.getStyleClass().remove("active");
    activeButton = null;
  }

  /**
   * Sets the active button in the sidebar based on the provided FXML path.
   * Highlights the corresponding button to indicate the current page.
   *
   * @param fxmlPath The FXML path of the page to set as active.
   */
  public void setActiveByPath(Paths fxmlPath) {
    if (pathMap == null) {
      return;
    }
    Button btn = pathMap.get(fxmlPath);
    if (btn == null && activeButton != null) {
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
