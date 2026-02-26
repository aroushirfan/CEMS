package com.cems.frontend.controllers.pages;

import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.services.IEventService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import com.cems.frontend.models.Event;
import com.cems.frontend.controllers.components.EventCardController;

import java.util.List;

public class MainHomeController {

    @FXML
    private FlowPane eventsContainer;

    private IEventService eventService = new ApiEventService();

    @FXML
    public void initialize() {
        loadEvents();
    }

    private void loadEvents() {

        try {

            List<Event> events = eventService.getAllEvents();

            for (Event event : events) {

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/org/cems/frontend/components/event-card.fxml")
                );

                VBox card = loader.load();

                EventCardController controller = loader.getController();

                controller.setEventModel(event);



                eventsContainer.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


