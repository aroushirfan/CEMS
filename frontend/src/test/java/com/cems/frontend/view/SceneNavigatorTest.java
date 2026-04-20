package com.cems.frontend.view;

import com.cems.frontend.controllers.components.NavbarController;
import com.cems.frontend.controllers.pages.NavigationController;
import com.cems.frontend.models.Event;
import com.cems.frontend.models.NavigationMemento;
import com.cems.frontend.models.Paths;
import javafx.fxml.FXMLLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class SceneNavigatorTest {

  @BeforeEach
  void resetState() {
    SceneNavigator.setCurrentState(null);
    SceneNavigator.setNavigationController(null);
    SceneNavigator.setNavbarController(null);
  }

  @Test
  void testSetAndGetCurrentState() {
    NavigationMemento memento = new NavigationMemento(Paths.HOME, null);
    SceneNavigator.setCurrentState(memento);

    assertEquals(memento, SceneNavigator.getCurrentState());
  }

  @Test
  void testSetNavigationControllerInitializesDefaultState() {
    NavigationController nav = new NavigationController();
    SceneNavigator.setNavigationController(nav);

    assertNotNull(SceneNavigator.getCurrentState());
    assertEquals(Paths.HOME, SceneNavigator.getCurrentState().getPath());
  }

  @Test
  void testSetNavbarControllerStoresController() {
    NavbarController navbar = new NavbarController();
    SceneNavigator.setNavbarController(navbar);
    assertNotNull(navbar);
  }

  @Test
  void testGetLoaderReturnsValidLoader() {
    URL dummy = getClass().getResource("/com/cems/frontend/view/pages/dummy.fxml");

    FXMLLoader loader = SceneNavigator.getLoader(dummy);

    assertNotNull(loader);
    assertNotNull(loader.getResources());
  }

  @Test
  void testLoadEventDetailUpdatesState() {
    // We do NOT call navigationController.loadContent() because it loads FXML
    SceneNavigator.setNavigationController(new NavigationController());

    Event event = new Event();
    SceneNavigator.setCurrentState(new NavigationMemento(Paths.EVENT_DETAIL_VIEW, event));

    NavigationMemento state = SceneNavigator.getCurrentState();

    assertEquals(Paths.EVENT_DETAIL_VIEW, state.getPath());
    assertEquals(event, state.getPayload(Event.class));
  }

  @Test
  void testLoadEditPageUpdatesState() {
    SceneNavigator.setNavigationController(new NavigationController());

    Event event = new Event();
    SceneNavigator.setCurrentState(new NavigationMemento(Paths.EDIT_VIEW, event));

    NavigationMemento state = SceneNavigator.getCurrentState();

    assertEquals(Paths.EDIT_VIEW, state.getPath());
    assertEquals(event, state.getPayload(Event.class));
  }

  @Test
  void testLoadAttendancePageUpdatesState() {
    SceneNavigator.setNavigationController(new NavigationController());

    Event event = new Event();
    SceneNavigator.setCurrentState(new NavigationMemento(Paths.ATTENDANCE_VIEW, event));

    NavigationMemento state = SceneNavigator.getCurrentState();

    assertEquals(Paths.ATTENDANCE_VIEW, state.getPath());
    assertEquals(event, state.getPayload(Event.class));
  }
}
