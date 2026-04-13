package com.cems.frontend.view;

import javafx.scene.control.Alert;

/**
 * Utility helper for displaying JavaFX alert dialogs.
 */
public class AlertHelper {

  /**
   * Utility class constructor.
   */
  private AlertHelper() {
  }

  /**
   * Shows an error alert dialog.
   *
   * @param title   alert window title
   * @param content alert message content
   */
  public static void showError(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }

  /**
   * Shows an informational alert dialog.
   *
   * @param title   alert window title
   * @param content alert message content
   */
  public static void showInfo(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}
