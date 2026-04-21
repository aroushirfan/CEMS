package com.cems.frontend.controllers.pages;

import com.cems.frontend.controllers.components.EventCardController;
import com.cems.frontend.models.Event;
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.services.IEventService;
import com.cems.frontend.view.SceneNavigator;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main home page of the application.
 * Displays a hero image and a grid of upcoming events with pagination.
 */
public class MainHomeController {

  @FXML
  private FlowPane eventGrid;
  @FXML
  private Label viewMoreLabel;
  @FXML
  private ImageView heroImage;
  @FXML
  private ScrollPane scrollPane;

  private final IEventService eventService = new ApiEventService();

  private List<Event> allEvents = new ArrayList<>();
  private int currentIndex = 0;
  private static final int PAGE_SIZE = 4; // number of events per page
  private final Logger logger = Logger.getLogger(getClass().getName());

  /**
   * Initializes the controller by loading events and setting up the UI components.
   * This method is called automatically after the FXML file has been loaded.
   */
  @FXML
  public void initialize() {

    scrollPane.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
      heroImage.setFitWidth(newVal.getWidth());
    });

    loadAllEvents();
    loadNextPage();
    setupViewMoreButton();
  }

  @FXML
  private void goToAllEvents() {
    SceneNavigator.loadContent(Paths.ALL_EVENTS);

  }

  private void loadAllEvents() {
    try {
      allEvents = eventService.getApprovedEvents();
    } catch (Exception e) {
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
      logger.log(Level.WARNING, e.getMessage(), e);
    }
  }

  private void loadNextPage() {
    if (allEvents == null || allEvents.isEmpty()) {
      viewMoreLabel.setVisible(false);
      return;
    }

    int end = Math.min(currentIndex + PAGE_SIZE, allEvents.size());
    List<Event> page = allEvents.subList(currentIndex, end);

    for (Event event : page) {
      try {
        FXMLLoader loader = SceneNavigator.getLoader(
            getClass().getResource("/com/cems/frontend/view/components/event-card.fxml")
        );

        VBox card = loader.load();
        EventCardController controller = loader.getController();
        controller.setEventModel(event);


        card.setOnMouseClicked(e -> SceneNavigator.loadEventDetail(event));
        controller.getLearnMoreButton().setOnAction(e -> SceneNavigator.loadEventDetail(event));

        eventGrid.getChildren().add(card);

      } catch (Exception e) {
        logger.log(Level.WARNING, e.getMessage(), e);
      }
    }

    currentIndex = end;

    if (currentIndex >= allEvents.size()) {
      viewMoreLabel.setVisible(false);
    }
  }

  private void setupViewMoreButton() {
    viewMoreLabel.setOnMouseClicked(event -> loadNextPage());
  }
}