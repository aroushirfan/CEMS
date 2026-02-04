package com.cems.frontend.controllers.components;

import com.cems.frontend.models.Event;
import com.cems.shared.model.EventDto;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class EventCardController {
    @FXML private Label titleLabel;
    @FXML private Label dateTimeLabel;
    @FXML private Label locationLabel;
    @FXML private Label spotsLabel;

    @FXML private Button learnMoreButton;

    public void setEventModel(Event event) {
        titleLabel.textProperty().bind(event.titleProperty());
        // Date formatting
        if (event.getDateTime() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    .withZone(ZoneId.systemDefault());
            dateTimeLabel.setText(formatter.format(event.getDateTime()));
        }
        // Location (Null check)
        locationLabel.setText(event.getLocation() != null ? event.getLocation() : "TBD");
        // Capacity binding
        spotsLabel.textProperty().bind(event.capacityProperty().asString("Capacity: %d"));
    }

    public Button getLearnMoreButton() {
        return learnMoreButton;
    }
}