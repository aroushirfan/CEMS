//package com.cems.frontend.controllers.pages;
//
//import com.cems.frontend.models.NavigationNotifier;
//import com.cems.frontend.models.Paths;
//import com.cems.frontend.services.ApiEventService;
//import com.cems.frontend.services.IEventService;
//import com.cems.frontend.services.MockEventService;
//import com.cems.frontend.models.Event;
//import com.cems.frontend.controllers.components.EventCardController;
//import com.cems.frontend.utils.SideBarState;
//import com.cems.frontend.view.SceneNavigator;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.layout.FlowPane;
//import javafx.scene.layout.VBox;
//
//import java.util.List;
//
//
//public class MainHomeController {
//
//    @FXML
//    private FlowPane eventGrid;
//
//    private IEventService eventService = new ApiEventService(); // real API
////    private final MockEventService mockService = new MockEventService();
//
//    @FXML
//    public void initialize() {
//        loadEvents();
//    }
//
//
//    @FXML
//    private void goToAllEvents() {
//        NavigationNotifier.getInstance().notifyAllObservers(Paths.ALL_EVENTS);
//    }
//
//
//    private void loadEvents() {
//        try {
////          List<Event> events = mockService.getAllEvents();
//            List<Event> events = eventService.getAllEvents();
//
//            for (Event event : events) {
//                FXMLLoader loader = new FXMLLoader(
//                        getClass().getResource("/com/cems/frontend/view/components/event-card.fxml")
//                );
//
//                VBox card = loader.load();
//                EventCardController controller = loader.getController();
//                controller.setEventModel(event);
//
//                eventGrid.getChildren().add(card);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.NavigationNotifier;
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.services.IEventService;
import com.cems.frontend.models.Event;
import com.cems.frontend.controllers.components.EventCardController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class MainHomeController {

    @FXML
    private FlowPane eventGrid;

    @FXML
    private ScrollPane rootScroll;

    @FXML
    private ImageView heroImage;

    private final IEventService eventService = new ApiEventService();

    @FXML
    public void initialize() {

        // Prevent null pointer if FXML didn't load heroImage
        if (rootScroll != null && heroImage != null) {
            rootScroll.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
                heroImage.setFitWidth(newVal.getWidth());
            });
        }

        loadEvents();
    }

    @FXML
    private void goToAllEvents() {
        NavigationNotifier.getInstance().notifyAllObservers(Paths.ALL_EVENTS);
    }

    private void loadEvents() {
        try {
            List<Event> events = eventService.getAllEvents();

            for (Event event : events) {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/cems/frontend/view/components/event-card.fxml")
                );

                VBox card = loader.load();
                EventCardController controller = loader.getController();
                controller.setEventModel(event);

                eventGrid.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
