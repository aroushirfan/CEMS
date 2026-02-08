package com.cems.frontend.controllers.components;

import com.cems.frontend.models.Event;
import com.cems.shared.model.EventDto.EventRequestDTO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Instant;
import java.time.ZonedDateTime;

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
        for (int i = 0; i < 24; i++) {
            hourComboBox.getItems().add(String.format("%02d", i));
        }
        minuteComboBox.getItems().addAll("00", "15", "30", "45");

        hourComboBox.setValue("12");
        minuteComboBox.setValue("00");
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

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
                Long.parseLong(capacityField.getText()),
                eventInstant
        );
    }

    public void setFormData(Event event) {
        titleField.setText(event.getTitle());
        descriptionField.setText(event.getDescription());
        locationField.setText(event.getLocation());
        capacityField.setText(String.valueOf(event.getCapacity()));

        if (event.getDateTime() != null) {
            ZonedDateTime zdt = event.getDateTime().atZone(ZoneId.systemDefault());
            datePicker.setValue(zdt.toLocalDate());
            hourComboBox.setValue(String.format("%02d", zdt.getHour()));
            int minute = zdt.getMinute();
            if (minute < 15) minuteComboBox.setValue("00");
            else if (minute < 30) minuteComboBox.setValue("15");
            else if (minute < 45) minuteComboBox.setValue("30");
            else minuteComboBox.setValue("45");
        }
    }
}