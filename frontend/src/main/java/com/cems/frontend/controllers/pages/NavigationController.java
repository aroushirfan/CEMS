package com.cems.frontend.controllers.pages;

import com.cems.frontend.controllers.components.SidebarController;
import com.cems.frontend.models.Event;
import com.cems.frontend.models.NavigationMemento;
import com.cems.frontend.models.Paths;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.frontend.view.SceneNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class NavigationController {
    @FXML private SidebarController sideBarController;
    @FXML private AnchorPane rootView;
    @FXML private StackPane contentArea;


    @FXML
    public void initialize() throws IOException {
        SceneNavigator.setNavigationController(this);
        loadContent(Paths.HOME);
    }

    public <T> T loadContent(Paths fxmlPath){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath.path));

            Locale locale = LocaleUtil.getInstance().getLocale();
            ResourceBundle resourceBundle = ResourceBundle.getBundle(fxmlPath.bundlePath, locale);
            loader.setResources(resourceBundle);

            Node content = loader.load();
            T controller = loader.getController();
            contentArea.getChildren().setAll(content);
            SceneNavigator.setCurrentState(new NavigationMemento(fxmlPath,SceneNavigator.getCurrentState().getPayload(Event.class)));
            sideBarController.setActiveByPath(fxmlPath);
            return controller;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void reloadUI() {
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
            e.printStackTrace();
        }
    }

    public void restoreState(NavigationMemento navigationMemento) {
        SceneNavigator.setCurrentState(navigationMemento);
        Object controller = loadContent(navigationMemento.getPath());
        if (navigationMemento.getPath().equals(Paths.EVENT_DETAIL_VIEW)) {
            ((EventDetailController) controller).initData(navigationMemento.getPayload(Event.class));
        } else if (navigationMemento.getPath().equals(Paths.ATTENDANCE_VIEW)) {
            ((AttendanceController) controller).loadAttendanceForEvent(navigationMemento.getPayload(Event.class));
        }
    }

    public StackPane getContentArea() {
        return contentArea;
    }
}
