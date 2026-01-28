package org.cems.frontend.controllers.pages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.cems.frontend.controllers.components.EventCardController;
import org.cems.frontend.models.Event;
import org.cems.frontend.services.MockEventService;

import java.io.IOException;
import java.util.List;

public class HomeController {
    @FXML private FlowPane eventGrid;
    private final MockEventService eventService = new MockEventService();

    @FXML
    public void initialize() {
        // Fetch data from Mock Service
        List<Event> events = eventService.getAllEvents();

        for (Event event : events) {
            try {
                // Load the component from the components folder
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/cems/frontend/view/components/event-card.fxml"));
                VBox card = loader.load();

                // Inject the data into the card's controller
                EventCardController cardController = loader.getController();
                cardController.setEventData(event);

                // Add to the grid in home-view.fxml
                eventGrid.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace(); // Handle with AlertHelper later
            }
        }
    }
}