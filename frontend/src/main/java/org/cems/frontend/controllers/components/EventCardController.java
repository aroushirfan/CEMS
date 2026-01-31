package org.cems.frontend.controllers.components;

import com.cems.shared.model.EventDto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class EventCardController {
    @FXML private Label titleLabel;
    @FXML private Label dateTimeLabel;
    @FXML private Label locationLabel;
    @FXML private Label spotsLabel;

    public void setEventData(EventDto.EventResponseDTO event) {
        titleLabel.setText(event.getTitle());
        locationLabel.setText(event.getLocation());

        if (event.getDateTime() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    .withZone(ZoneId.systemDefault());
            dateTimeLabel.setText(formatter.format(event.getDateTime()));
        }

        // Use the real capacity from your database
        spotsLabel.setText("Capacity: " + event.getCapacity());
    }
}