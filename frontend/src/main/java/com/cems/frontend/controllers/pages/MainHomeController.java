package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.NavigationNotifier;
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.services.IEventService;
import com.cems.frontend.models.Event;
import com.cems.frontend.controllers.components.EventCardController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MainHomeController {

    @FXML private FlowPane eventGrid;
    @FXML private ScrollPane rootScroll;
    @FXML private ImageView heroImage;
    @FXML private Label viewMoreLabel;

    private final IEventService eventService = new ApiEventService();

    private List<Event> allEvents = new ArrayList<>();
    private int loadedCount = 0;
    private final int batchSize = 4; // how many events

    @FXML
    public void initialize() {

        if (rootScroll != null && heroImage != null) {
            rootScroll.viewportBoundsProperty().addListener((obs, oldVal, newVal) ->
                    heroImage.setFitWidth(newVal.getWidth())
            );
        }

        loadEvents();


        if (viewMoreLabel != null) {
            viewMoreLabel.setOnMouseClicked(e -> loadMoreEvents());
        }
    }

    @FXML
    private void goToAllEvents() {
        NavigationNotifier.getInstance().notifyAllObservers(Paths.ALL_EVENTS);
    }

    private void loadEvents() {
        try {
            allEvents = eventService.getAllEvents();
            loadMoreEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMoreEvents() {
        int end = Math.min(loadedCount + batchSize, allEvents.size());

        for (int i = loadedCount; i < end; i++) {
            Event event = allEvents.get(i);

            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/cems/frontend/view/components/event-card.fxml")
                );

                VBox card = loader.load();
                EventCardController controller = loader.getController();
                controller.setEventModel(event);

                eventGrid.getChildren().add(card);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        loadedCount = end;

        // Hide "View more" when all loaded
        if (loadedCount >= allEvents.size()) {
            viewMoreLabel.setVisible(false);
        } else {
            viewMoreLabel.setVisible(true);
        }
    }
}
