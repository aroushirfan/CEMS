package com.cems.frontend.controller.pages;

import com.cems.frontend.services.AuthService;
import com.cems.frontend.utils.Language;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.frontend.view.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the Signup view. Handles user input for signing up
 * and interacts with the AuthService.
 */
public class SignupController {

  @FXML
  private TextField firstNameField;
  @FXML
  private TextField lastNameField;
  @FXML
  private TextField emailField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private PasswordField confirmPasswordField;
  @FXML
  private AnchorPane rootView;

  private final AuthService authService = AuthService.getInstance();
  private final Logger logger = Logger.getLogger(getClass().getName());

  /**
   * Initializes the controller. Sets the orientation of the view based on the
   * current language.
   */
  @FXML
  public void initialize() {
    logger.log(Level.INFO,"Signup view loaded");
    setOrientation();
  }

  /**
   * Sets the orientation of the view based on the current language. If the
   * language is Urdu, it sets the orientation to right-to-left; otherwise, it
   * sets it to left-to-right.
   */
  public void setOrientation() {
    boolean ltr = LocaleUtil.getInstance().getLanguage().equals(Language.UR);
    rootView.setNodeOrientation(!ltr ? NodeOrientation.LEFT_TO_RIGHT
        : NodeOrientation.RIGHT_TO_LEFT);
  }

  // signup button
  @FXML
  private void handleSignup(ActionEvent event) {
    String firstName = firstNameField.getText();
    String lastName = lastNameField.getText();
    String email = emailField.getText();
    String password = passwordField.getText();
    String confirmPassword = confirmPasswordField.getText();

    if (firstName.isEmpty() || email.isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setContentText("Required fields missing");
      alert.show();
      return;
    }

    try {
      authService.signUp(firstName, lastName, email, password, confirmPassword);
    } catch (Exception e) {
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setContentText(e.getLocalizedMessage());
      alert.show();
      clearFields();
      return;
    }

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText("Sign Up Successful. Please login.");
    alert.show();

    alert.setOnCloseRequest(dialogEvent ->
      SceneNavigator.loadPage("Login.fxml")
    );
  }

  //  Cancel button
  @FXML
  private void handleCancel(ActionEvent event) {
    clearFields();
  }

  //  Redirect to login page
  @FXML
  private void goToLogin(ActionEvent event) {
    SceneNavigator.loadPage("Login.fxml");
  }

  private void clearFields() {
    firstNameField.clear();
    lastNameField.clear();
    emailField.clear();
    passwordField.clear();
    confirmPasswordField.clear();
  }
}
