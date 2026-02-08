package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Event;
import com.cems.frontend.view.SceneNavigator;
import com.cems.frontend.controllers.components.EventCardController;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.services.IEventService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class HomeController {
    @FXML private FlowPane eventGrid;
    @FXML private TextField searchField; // Injected from FXML

    private final IEventService eventService = new ApiEventService();
    private final ObservableList<Event> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupSearchFilter();
        fetchEvents();
    }

    private void setupSearchFilter() {
        // Wrap master data in a FilteredList
        FilteredList<Event> filteredData = new FilteredList<>(masterData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(event -> {
                if (newValue == null || newValue.isBlank()) return true;

                String filter = newValue.toLowerCase();
                return event.getTitle().toLowerCase().contains(filter) ||
                        event.getLocation().toLowerCase().contains(filter);
            });
            populateGrid(filteredData);
        });
    }

    private void fetchEvents() {
        Task<List<Event>> task = new Task<>() {
            @Override
            protected List<Event> call() throws Exception {
                return eventService.getAllEvents();
            }
        };

        task.setOnSucceeded(e -> {
            masterData.setAll(task.getValue()); // Update master list
            populateGrid(masterData); // Initial population
        });

        new Thread(task).start();
    }

    private void populateGrid(List<Event> events) {
        eventGrid.getChildren().clear();

        for (Event eventModel : events) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cems/frontend/view/components/event-card.fxml"));
                VBox card = loader.load();

                EventCardController cardController = loader.getController();
                cardController.setEventModel(eventModel);

                card.setOnMouseClicked(e -> SceneNavigator.loadEventDetail(eventModel));
                cardController.getLearnMoreButton().setOnAction(e -> SceneNavigator.loadEventDetail(eventModel));

                eventGrid.getChildren().add(card);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}