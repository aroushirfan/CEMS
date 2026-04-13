package com.cems.frontend.view;

import com.cems.frontend.controllers.components.NavbarController;
import com.cems.frontend.controllers.pages.AttendanceController;
import com.cems.frontend.controllers.pages.EditEventController;
import com.cems.frontend.controllers.pages.EventDetailController;
import com.cems.frontend.controllers.pages.NavigationController;
import com.cems.frontend.models.Event;
import com.cems.frontend.models.NavigationMemento;
import com.cems.frontend.models.Paths;
import com.cems.frontend.utils.LocaleUtil;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Centralized navigation utility for switching scenes and loading page content.
 *
 * <p>This class stores shared navigation controllers, tracks current navigation
 * state, and provides localized FXML loading helpers.</p>
 */
public class SceneNavigator {
  private static Stage mainStage;
  private static NavigationController navigationController;
  private static NavbarController navbarController;
  private static NavigationMemento currentState;

  /**
   * Utility class constructor.
   */
  private SceneNavigator() {
  }

  /**
   * Sets the primary application stage.
   *
   * @param stage primary JavaFX stage
   */
  public static void setStage(Stage stage) {
    mainStage = stage;
  }

  /**
   * Sets the navigation controller and initializes the default state.
   *
   * @param navController controller responsible for loading navigation content
   */
  public static void setNavigationController(NavigationController navController) {
    navigationController = navController;
    currentState = new NavigationMemento(Paths.HOME, null);
  }

  /**
   * Sets the navbar controller used for role-based/navbar refresh behavior.
   *
   * @param navbar navbar controller instance
   */
  public static void setNavbarController(NavbarController navbar) {
    navbarController = navbar;
  }

  /**
   * Refreshes navbar visibility and state.
   */
  public static void refreshNavbar() {
    navbarController.refreshVisibility();
  }

  /**
   * Updates the current navigation state snapshot.
   *
   * @param currentState navigation memento to store
   */
  public static void setCurrentState(NavigationMemento currentState) {
    SceneNavigator.currentState = currentState;
  }

  /**
   * Returns the current navigation state snapshot.
   *
   * @return current navigation memento
   */
  public static NavigationMemento getCurrentState() {
    return currentState;
  }

  /**
   * Loads content into the navigation container.
   *
   * @param fxmlPath target view path enum
   */
  public static void loadContent(Paths fxmlPath) {
    navigationController.loadContent(fxmlPath);
  }

  /**
   * Loads a standalone page FXML into a new scene on the main stage.
   *
   * <p>The loaded scene uses the active locale resource bundle and applies
   * shared/global stylesheets.</p>
   *
   * @param fxmlPath filename under the pages FXML directory
   */
  public static void loadPage(String fxmlPath) {
    try {

      URL resource = SceneNavigator.class
          .getResource("/com/cems/frontend/view/pages/" + fxmlPath);

      if (resource == null) {
        System.err.println("Error: Could not find FXML file at: /com/cems/frontend/view/pages/"
            + fxmlPath);
        return;
      }

      double width = mainStage.getWidth();
      double height = mainStage.getHeight();

      Locale locale = LocaleUtil.getInstance().getLocale();
      ResourceBundle resourceBundle = ResourceBundle.getBundle(
          "com.cems.frontend.view.i18n.Bundles", locale);

      FXMLLoader loader = new FXMLLoader(resource, resourceBundle);
      Scene scene = new Scene(loader.load(), width, height);
      if (LocaleUtil.getInstance().getLanguage().getFontCss() != null) {
        scene.getStylesheets().add(SceneNavigator.class
            .getResource(LocaleUtil.getInstance()
            .getLanguage().getFontCss()).toExternalForm());
      }


      scene.getStylesheets().add(
          SceneNavigator.class.getResource("/com/cems/frontend/view/css/sidebar.css")
              .toExternalForm()
      );

      mainStage.setScene(scene);
      mainStage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reloads the current navigation view UI.
   */
  public static void reloadNavigationView() {
    navigationController.reloadUi();
  }

  /**
   * Loads the event detail page for the given event and stores navigation state.
   *
   * @param event selected event
   */
  public static void loadEventDetail(Event event) {
    currentState = new NavigationMemento(Paths.EVENT_DETAIL_VIEW, event);
    EventDetailController controller = navigationController.loadContent(Paths.EVENT_DETAIL_VIEW);
    controller.initData(event);
  }

  /**
   * Loads the edit-event page for the given event and stores navigation state.
   *
   * @param event selected event
   */
  public static void loadEditPage(Event event) {
    currentState = new NavigationMemento(Paths.EDIT_VIEW, event);
    EditEventController controller = navigationController.loadContent(Paths.EDIT_VIEW);
    controller.initData(event);
  }

  /**
   * Loads the attendance page for the given event and stores navigation state.
   *
   * @param event selected event
   */
  public static void loadAttendancePage(Event event) {
    currentState = new NavigationMemento(Paths.ATTENDANCE_VIEW, event);
    AttendanceController controller = navigationController.loadContent(Paths.ATTENDANCE_VIEW);
    controller.loadAttendanceForEvent(event);
  }

  /**
   * Creates a localized FXML loader for the given resource.
   *
   * @param resource FXML resource URL
   * @return configured {@link FXMLLoader} using current locale bundle
   */
  public static FXMLLoader getLoader(URL resource) {
    Locale locale = LocaleUtil.getInstance().getLocale();
    ResourceBundle resourceBundle = ResourceBundle.getBundle("com.cems.frontend.view.i18n.Bundles",
        locale);
    return new FXMLLoader(resource, resourceBundle);
  }

  /**
   * Returns the currently configured navigation controller.
   *
   * @return navigation controller instance
   */
  public static NavigationController getNavigationController() {
    return navigationController;
  }
}


