package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.NavigationNotifier;
import com.cems.frontend.models.Paths;
import com.cems.frontend.services.ApiEventService;
import com.cems.frontend.services.IEventService;
import com.cems.frontend.models.Event;
import com.cems.frontend.controllers.components.EventCardController;
import com.cems.frontend.view.SceneNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MainHomeController {

    @FXML
    private FlowPane eventGrid;

    @FXML
    private Label viewMoreLabel;

    private final IEventService eventService = new ApiEventService();

    private List<Event> allEvents = new ArrayList<>();
    private int currentIndex = 0;
    private final int pageSize = 4; // number of events per page

    @FXML
    public void initialize() {
        loadAllEvents();
        loadNextPage();
        setupViewMoreButton();
    }

    @FXML
    private void goToAllEvents() {
        NavigationNotifier.getInstance().notifyAllObservers(Paths.ALL_EVENTS);

    }

    private void loadAllEvents() {
        try {
            allEvents = eventService.getApprovedEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNextPage() {
        if (allEvents == null || allEvents.isEmpty()) {
            viewMoreLabel.setVisible(false);
            return;
        }

        int end = Math.min(currentIndex + pageSize, allEvents.size());
        List<Event> page = allEvents.subList(currentIndex, end);

        for (Event event : page) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/cems/frontend/view/components/event-card.fxml")
                );

                VBox card = loader.load();
                EventCardController controller = loader.getController();
                controller.setEventModel(event);


                card.setOnMouseClicked(e -> SceneNavigator.loadEventDetail(event));


                controller.getLearnMoreButton().setOnAction(e -> SceneNavigator.loadEventDetail(event));

                eventGrid.getChildren().add(card);

            } catch (Exception e) {
                e.printStackTrace();
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
