package com.cems.frontend.controllers.pages;

import com.cems.frontend.controllers.components.SidebarController;
import com.cems.frontend.models.Paths;
import com.cems.frontend.utils.Language;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.frontend.view.SceneNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class NavigationController {
    @FXML private SidebarController sideBarController;
    @FXML private AnchorPane rootView;
    @FXML private StackPane contentArea;

    private Paths currentPath;

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
            currentPath = fxmlPath;
            sideBarController.setActiveByPath(fxmlPath);
            return controller;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void reloadUI() {
        try {
            ResourceBundle bundle = LocaleUtil.getInstance().getBundle(Paths.NAVIGATION);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(Paths.NAVIGATION.path),
                    bundle
            );
            Parent contentRoot = loader.load();
            NavigationController updatedNavigationController = loader.getController();
            updatedNavigationController.restoreState(currentPath);
            contentArea.getScene().setRoot(contentRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreState(Paths fxmlPath) {
        currentPath = fxmlPath;
        loadContent(currentPath);
    }
}
