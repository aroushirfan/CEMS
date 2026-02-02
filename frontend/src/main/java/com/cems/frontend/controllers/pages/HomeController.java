package com.cems.frontend.controllers.pages;

import com.cems.shared.model.EventDto;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import com.cems.frontend.controllers.components.EventCardController;
import com.cems.frontend.services.ApiEventService; // Your new service
import com.cems.frontend.services.IEventService;

import java.io.IOException;
import java.util.List;

public class HomeController {
    @FXML private FlowPane eventGrid;

    // Use the interface for flexibility
    private final IEventService eventService = new ApiEventService();

    @FXML
    public void initialize() {
        fetchEvents();
    }

    private void fetchEvents() {
        // Create a background task to prevent UI freezing
        Task<List<EventDto.EventResponseDTO>> task = new Task<>() {
            @Override
            protected List<EventDto.EventResponseDTO> call() throws Exception {
                return eventService.getAllEvents(); // API call
            }
        };

        // When the data arrives successfully
        task.setOnSucceeded(e -> {
            List<EventDto.EventResponseDTO> events = task.getValue();
            populateGrid(events);
        });

        // If the API call fails
        task.setOnFailed(e -> {
            Throwable problem = task.getException();
            problem.printStackTrace();
            // In a real app, you would show an error dialog here
        });

        // Start the thread
        new Thread(task).start();
    }

    private void populateGrid(List<EventDto.EventResponseDTO> events) {
        eventGrid.getChildren().clear(); // Clear grid before adding new data

        for (EventDto.EventResponseDTO event : events) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cems/frontend/view/components/event-card.fxml"));
                VBox card = loader.load();

                EventCardController cardController = loader.getController();

                // CRITICAL: You must update setEventData in EventCardController
                // to accept EventDto.EventResponseDTO
                cardController.setEventData(event);

                eventGrid.getChildren().add(card);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}