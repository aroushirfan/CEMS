package com.cems.frontend.controllers.components;

import com.cems.frontend.models.Paths;
import com.cems.frontend.utils.LocalStorage;
import com.cems.frontend.view.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Controller for the navigation bar component. Manages the visibility of login/signup buttons
 * and profile access based on user authentication status.
 */
public class NavbarController {
  public static NavbarController instance;
  @FXML
  private Label loginLabel;
  @FXML
  private Button signupBtn;
  @FXML
  private Button profileBtn;

  /**
   * Initializes the navigation bar controller. Sets the instance reference and refreshes
   * the visibility of buttons based on the user's authentication status.
   */
  @FXML
  public void initialize() {
    SceneNavigator.setNavbarController(this);
    instance = this;
    refreshVisibility();
  }

  /**
   * Refreshes the visibility of the login/signup buttons and profile button
   * based on whether the user is logged in or not. Checks for the presence
   * of a token in local storage to determine authentication status.
   */
  public void refreshVisibility() {
    String token = LocalStorage.get("token");
    boolean isLoggedIn = token != null && !token.isBlank();

    loginLabel.setVisible(!isLoggedIn);
    loginLabel.setManaged(!isLoggedIn);

    signupBtn.setVisible(!isLoggedIn);
    signupBtn.setManaged(!isLoggedIn);

    profileBtn.setVisible(isLoggedIn);
    profileBtn.setManaged(isLoggedIn);
  }

  @FXML
  private void handleViewProfile() {
    SceneNavigator.loadContent(Paths.USER_SETTINGS);
  }

  @FXML
  private HBox navBarHbox;

  @FXML
  private void handleLoginRedirect() {
    SceneNavigator.loadPage("Login.fxml");
  }

  @FXML
  private void handleSignupRedirect() {
    SceneNavigator.loadPage("SignUp.fxml");
  }
}