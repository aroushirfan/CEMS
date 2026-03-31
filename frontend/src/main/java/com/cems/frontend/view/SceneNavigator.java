//package com.cems.frontend.view;
//
//import com.cems.frontend.controllers.pages.EditEventController;
//import com.cems.frontend.controllers.pages.EventDetailController;
//
//import com.cems.frontend.models.Event; // Import your property-based model
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import java.io.IOException;
//import java.net.URL;
//
//public class SceneNavigator {
//    private static Stage mainStage;
//
//    public static void setStage(Stage stage) {
//        mainStage = stage;
//    }
//
//    public static void loadPage(String fxmlPath) {
//        try {
//            URL resource = SceneNavigator.class.getResource("/com/cems/frontend/view/pages/" + fxmlPath);
//
//            if (resource == null) {
//                System.err.println("Error: Could not find FXML file at: /com/cems/frontend/view/pages/" + fxmlPath);
//                return;
//            }
//            double width = mainStage.getWidth();
//            double height = mainStage.getHeight();
//
//            FXMLLoader loader = new FXMLLoader(resource);
//            Scene scene = new Scene(loader.load(), width, height);
//            mainStage.setScene(scene);
//            mainStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
//    }
//
//    public static void loadEventDetail(Event event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/cems/frontend/view/pages/event-detail-view.fxml"));
//            Scene scene = new Scene(loader.load(), mainStage.getWidth(), mainStage.getHeight());
//            EventDetailController controller = loader.getController();
//            controller.initData(event);
//            mainStage.setScene(scene);
//            mainStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void loadEditPage(Event event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/cems/frontend/view/pages/edit-event-view.fxml"));
//
//            Scene scene = new Scene(loader.load(), mainStage.getWidth(), mainStage.getHeight());
//
//            EditEventController controller = loader.getController();
//            controller.initData(event);
//            mainStage.setScene(scene);
//            mainStage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}

package com.cems.frontend.view;

import com.cems.frontend.controllers.components.NavbarController;
import com.cems.frontend.controllers.pages.*;
import com.cems.frontend.models.Event;
import com.cems.frontend.models.NavigationMemento;
import com.cems.frontend.models.Paths;
import com.cems.frontend.utils.LocaleUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class SceneNavigator {
    private static Stage mainStage;
    private static NavigationController navigationController;
    private static NavbarController navbarController;
    private static NavigationMemento currentState;

    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static void setNavigationController(NavigationController navController) {
        navigationController = navController;
        currentState = new NavigationMemento(Paths.HOME,null);
    }

    public static void setNavbarController(NavbarController navbar) {
        navbarController = navbar;
    }

    public static void refreshNavbar() {
        navbarController.refreshVisibility();
    }

    public static void setCurrentState(NavigationMemento currentState) {
        SceneNavigator.currentState = currentState;
    }

    public static NavigationMemento getCurrentState() {
        return currentState;
    }

    public static void loadContent(Paths fxmlPath) {
        navigationController.loadContent(fxmlPath);
    }

    public static void loadPage(String fxmlPath) {
        try {

            URL resource = SceneNavigator.class.getResource("/com/cems/frontend/view/pages/" + fxmlPath);

            if (resource == null) {
                System.err.println("Error: Could not find FXML file at: /com/cems/frontend/view/pages/" + fxmlPath);
                return;
            }

            double width = mainStage.getWidth();
            double height = mainStage.getHeight();

            Locale locale = LocaleUtil.getInstance().getLocale();
            ResourceBundle resourceBundle = ResourceBundle.getBundle("com.cems.frontend.view.i18n.Bundles", locale);

            FXMLLoader loader = new FXMLLoader(resource,resourceBundle);
            Scene scene = new Scene(loader.load(), width, height);
            if (LocaleUtil.getInstance().getLanguage().getFontCss() != null) {
                scene.getStylesheets().add(SceneNavigator.class.getResource(LocaleUtil.getInstance().getLanguage().getFontCss()).toExternalForm());
            }


            scene.getStylesheets().add(
                    SceneNavigator.class.getResource("/com/cems/frontend/view/css/sidebar.css").toExternalForm()
            );

            mainStage.setScene(scene);
            mainStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadNavigationView() {
        navigationController.reloadUI();
    }

    public static void loadEventDetail(Event event) {
        currentState = new NavigationMemento(Paths.EVENT_DETAIL_VIEW, event);
        EventDetailController controller = navigationController.loadContent(Paths.EVENT_DETAIL_VIEW);
        controller.initData(event);
    }

    public static void loadEditPage(Event event) {
        currentState = new NavigationMemento(Paths.EDIT_VIEW, event);
        EditEventController controller = navigationController.loadContent(Paths.EDIT_VIEW);
        controller.initData(event);
    }
    public static void loadAttendancePage(Event event) {
        currentState = new NavigationMemento(Paths.ATTENDANCE_VIEW, event);
        AttendanceController controller = navigationController.loadContent(Paths.ATTENDANCE_VIEW);
        controller.loadAttendanceForEvent(event);
    }

    // new method to current SceneNavigator class
    public static FXMLLoader getLoader(URL resource) {
        // Matches your existing locale logic
        Locale locale = LocaleUtil.getInstance().getLocale();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("com.cems.frontend.view.i18n.Bundles", locale);

        // Returns a loader that already "knows" the dictionary
        return new FXMLLoader(resource, resourceBundle);
    }

    public static NavigationController getNavigationController() {
        return navigationController;
    }
}


