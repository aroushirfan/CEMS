package com.cems.frontend.controllers.pages;

import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.services.IEventService;
import com.cems.frontend.services.MockEventService;
import com.cems.frontend.models.Event;
import com.cems.frontend.controllers.components.EventCardController;
import com.cems.frontend.utils.SideBarState;
import com.cems.frontend.view.SceneNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;


public class MainHomeController {

    @FXML
    private FlowPane eventGrid;

    private IEventService eventService = new ApiEventService(); // real API
//    private final MockEventService mockService = new MockEventService();

    @FXML
    public void initialize() {
        loadEvents();
    }


    @FXML
    private void goToAllEvents() {
        SceneNavigator.loadPage("home-view.fxml");
    }

    private void loadEvents() {
        try {
//          List<Event> events = mockService.getAllEvents();
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
