package com.cems.frontend.controllers.pages;

import com.cems.frontend.controllers.components.SidebarController;
import com.cems.frontend.models.Event;
import com.cems.frontend.models.NavigationMemento;
import com.cems.frontend.models.Paths;
import com.cems.frontend.utils.Language;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.frontend.view.SceneNavigator;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * Controller for the main navigation view. It manages the sidebar and
 * the content area where different views are loaded based on user interaction.
 */
public class NavigationController {
  @FXML
  private SidebarController sideBarController;
  @FXML
  private AnchorPane rootView;
  @FXML
  private StackPane contentArea;
  private final Logger logger = Logger.getLogger(getClass().getName());

  /**
   * Initializes the navigation controller by setting up the scene navigator,
   * loading the default content, and configuring the UI orientation based on
   * the current locale.
   */
  @FXML
  public void initialize() {
    SceneNavigator.setNavigationController(this);
    loadContent(Paths.HOME);
    setOrientation();
  }

  /**
   * Sets the orientation of the UI based on the current language.
   * If the language is Urdu, it sets the orientation to right-to-left; otherwise,
   * it defaults to left-to-right.
   */
  public void setOrientation() {
    boolean ltr = LocaleUtil.getInstance().getLanguage().equals(Language.UR);
    rootView.setNodeOrientation(!ltr ? NodeOrientation.LEFT_TO_RIGHT
        : NodeOrientation.RIGHT_TO_LEFT);
  }

  /**
   * Loads the content for a given FXML path and updates the content area.
   * It also updates the current navigation state and highlights the active sidebar item.
   *
   * @param fxmlPath The path to the FXML file to load.
   * @param <T> The type of the controller associated with the loaded FXML.
   * @return The controller instance of the loaded FXML, or null if an error occurs.
   */
  public <T> T loadContent(Paths fxmlPath) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath.path));

      Locale locale = LocaleUtil.getInstance().getLocale();
      ResourceBundle resourceBundle = ResourceBundle.getBundle(fxmlPath.bundlePath, locale);
      loader.setResources(resourceBundle);

      Node content = loader.load();
      contentArea.getChildren().setAll(content);
      SceneNavigator.setCurrentState(new NavigationMemento(fxmlPath,
          SceneNavigator.getCurrentState().getPayload(Event.class)));
      sideBarController.setActiveByPath(fxmlPath);
      T controller = loader.getController();
      return controller;
    } catch (IOException e) {
      logger.log(Level.WARNING, e.getMessage(), e);
      return null;
    }
  }

  /**
   * Reloads the entire navigation UI, preserving the current state.
   * This is useful when changing languages or making significant UI updates.
   */
  public void reloadUi() {
    try {
      NavigationMemento currentState = SceneNavigator.getCurrentState();
      ResourceBundle bundle = LocaleUtil.getInstance().getBundle(Paths.NAVIGATION);
      FXMLLoader loader = new FXMLLoader(
          getClass().getResource(Paths.NAVIGATION.path),
          bundle
      );
      Parent contentRoot = loader.load();
      NavigationController updatedNavigationController = loader.getController();
      updatedNavigationController.restoreState(currentState);
      contentArea.getScene().setRoot(contentRoot);
    } catch (IOException e) {
      logger.log(Level.WARNING, e.getMessage(), e);
    }
  }

  /**
   * Restores the navigation state based on the provided memento. It loads the appropriate
   * content and initializes any necessary data for specific views.
   *
   * @param navigationMemento The memento containing the state to restore.
   */
  public void restoreState(NavigationMemento navigationMemento) {
    SceneNavigator.setCurrentState(navigationMemento);
    Object controller = loadContent(navigationMemento.getPath());
    if (navigationMemento.getPath().equals(Paths.EVENT_DETAIL_VIEW)) {
      ((EventDetailController) controller).initData(navigationMemento.getPayload(Event.class));
    } else if (navigationMemento.getPath().equals(Paths.ATTENDANCE_VIEW)) {
      ((AttendanceController) controller)
          .loadAttendanceForEvent(navigationMemento.getPayload(Event.class));
    }
  }

  public StackPane getContentArea() {
    return contentArea;
  }
}
