package com.cems.frontend.controller.pages;

import com.cems.frontend.controller.components.EventCardController;
import com.cems.frontend.models.Event;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.services.IEventService;
import com.cems.frontend.view.SceneNavigator;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the Home page, responsible for displaying a grid of approved events
 * and providing search functionality to filter events by title or location.
 */
public class HomeController {
  @FXML
  private FlowPane eventGrid;
  @FXML
  private TextField searchField;

  private final IEventService eventService = new ApiEventService();
  private final ObservableList<Event> masterData = FXCollections.observableArrayList();
  private final Logger logger = Logger.getLogger(getClass().getName());

  /**
   * Initializes the controller by setting up the search filter
   * and fetching the list of approved events. The search filter
   * allows users to dynamically filter events based on their title
   * or location as they type. The events are fetched asynchronously
   * to avoid blocking the UI thread, and once retrieved, they are displayed in a grid layout.
   */
  @FXML
  public void initialize() {
    setupSearchFilter();
    fetchEvents();
  }

  private void setupSearchFilter() {
    FilteredList<Event> filteredData = new FilteredList<>(masterData, p -> true);
    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredData.setPredicate(event -> {
        if (newValue == null || newValue.isBlank()) {
          return true;
        }

        String filter = newValue.toLowerCase();
        return event.getTitle().toLowerCase().contains(filter)
            || event.getLocation().toLowerCase().contains(filter);
      });
      populateGrid(filteredData);
    });
  }

  private void fetchEvents() {
    Task<List<Event>> task = new Task<>() {
      @Override
      protected List<Event> call() throws Exception {
        return eventService.getApprovedEvents();
      }
    };

    task.setOnSucceeded(e -> {
      masterData.setAll(task.getValue());
      populateGrid(masterData);
    });

    new Thread(task).start();
  }

  private void populateGrid(List<Event> events) {
    eventGrid.getChildren().clear();

    for (Event eventModel : events) {
      try {
        FXMLLoader loader = SceneNavigator.getLoader(
            getClass().getResource("/com/cems/frontend/view/components/event-card.fxml"));
        VBox card = loader.load();

        EventCardController cardController = loader.getController();
        cardController.setEventModel(eventModel);

        card.setOnMouseClicked(e -> SceneNavigator.loadEventDetail(eventModel));
        cardController.getLearnMoreButton().setOnAction(
            e -> SceneNavigator.loadEventDetail(eventModel));

        eventGrid.getChildren().add(card);
      } catch (IOException ex) {
        logger.log(Level.WARNING, ex.getMessage(), ex);
      }
    }
  }
}