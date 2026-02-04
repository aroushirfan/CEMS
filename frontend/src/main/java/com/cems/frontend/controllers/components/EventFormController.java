package com.cems.frontend.controllers.components;

import com.cems.frontend.models.Event; // Using your property-based model
import com.cems.shared.model.EventDto.EventRequestDTO; // Still used for sending data
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Instant;
import java.time.ZonedDateTime; // Required for precise time mapping

public class EventFormController {

    @FXML private ComboBox<String> hourComboBox;
    @FXML private ComboBox<String> minuteComboBox;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private TextField locationField;
    @FXML private TextField capacityField;
    @FXML private DatePicker datePicker;

    @FXML
    public void initialize() {
        // Populate hours 00-23
        for (int i = 0; i < 24; i++) {
            hourComboBox.getItems().add(String.format("%02d", i));
        }
        // Populate minutes in 15-minute increments
        minuteComboBox.getItems().addAll("00", "15", "30", "45");

        // Defaults
        hourComboBox.setValue("12");
        minuteComboBox.setValue("00");

        // Prevent selecting past dates
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    /**
     * Collects data from the form to create a Request DTO for the API.
     */
    public EventRequestDTO getFormData() {
        if (datePicker.getValue() == null) throw new IllegalArgumentException("Date is required");

        int hour = Integer.parseInt(hourComboBox.getValue());
        int minute = Integer.parseInt(minuteComboBox.getValue());

        Instant eventInstant = datePicker.getValue()
                .atTime(hour, minute)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        return new EventRequestDTO(
                titleField.getText(),
                descriptionField.getText(),
                locationField.getText(),
                Long.parseLong(capacityField.getText()), // Matches backend long type
                eventInstant
        );
    }

    /**
     * Best Practice: Populates the UI fields using the Frontend Model.
     * This decouples the form from the backend DTO structure.
     */
    public void setFormData(Event event) {
        // Standard text field population
        titleField.setText(event.getTitle());
        descriptionField.setText(event.getDescription());
        locationField.setText(event.getLocation());
        capacityField.setText(String.valueOf(event.getCapacity()));

        if (event.getDateTime() != null) {
            ZonedDateTime zdt = event.getDateTime().atZone(ZoneId.systemDefault());

            // Set the DatePicker
            datePicker.setValue(zdt.toLocalDate());

            // Set the Time selection based on model data
            hourComboBox.setValue(String.format("%02d", zdt.getHour()));

            // Logic to snap to the nearest 15-minute increment in the dropdown
            int minute = zdt.getMinute();
            if (minute < 15) minuteComboBox.setValue("00");
            else if (minute < 30) minuteComboBox.setValue("15");
            else if (minute < 45) minuteComboBox.setValue("30");
            else minuteComboBox.setValue("45");
        }
    }
}