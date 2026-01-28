package org.cems.frontend.controllers.components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.cems.frontend.models.Event;

public class EventCardController {
    @FXML private Label titleLabel;
    @FXML private Label dateTimeLabel;
    @FXML private Label locationLabel;
    @FXML private Label spotsLabel;

    public void setEventData(Event event) {
        titleLabel.setText(event.getTitle());
        // In a real app, format the Instant here
        dateTimeLabel.setText(event.getDateTime().toString());

        // For now, these are static as per the design
        locationLabel.setText("Auditorium");
        spotsLabel.setText("55 spots left (145/200)");
    }
}