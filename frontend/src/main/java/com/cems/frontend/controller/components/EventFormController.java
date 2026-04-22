package com.cems.frontend.controller.components;

import com.cems.frontend.models.Event;
import com.cems.frontend.utils.EventMapper;
import com.cems.frontend.utils.LocaleUtil;
import com.cems.shared.model.EventDto.EventRequestDTO;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.Chronology;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controller for the event form component, handling both creation and editing of events.
 */
public class EventFormController {

  @FXML
  private ComboBox<String> hourComboBox;
  @FXML
  private ComboBox<String> minuteComboBox;
  @FXML
  private TextField titleField;
  @FXML
  private TextArea descriptionField;
  @FXML
  private TextField locationField;
  @FXML
  private TextField capacityField;
  @FXML
  private DatePicker datePicker;

  /**
   * Initializes the form with default values and sets up the date picker to disable past dates.
   */
  @FXML
  public void initialize() {
    for (int i = 0; i < 24; i++) {
      hourComboBox.getItems().add(String.format("%02d", i));
    }
    minuteComboBox.getItems().addAll("00", "15", "30", "45");
    hourComboBox.setValue("12");
    minuteComboBox.setValue("00");
    datePicker.setChronology(Chronology.ofLocale(LocaleUtil.getInstance().getLocale()));
    datePicker.setDayCellFactory(picker -> new DateCell() {
      @Override
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        setDisable(empty || date.isBefore(LocalDate.now()));
      }
    });
  }

  /**
   * Collects the data from the form fields and constructs an EventRequestDTO.
   *
   * @return An EventRequestDTO containing the form data.
   * @throws IllegalArgumentException if the date is not selected.
   */
  public EventRequestDTO getFormData() throws IllegalArgumentException {
    if (datePicker.getValue() == null) {
      throw new IllegalArgumentException("Date is required");
    }


    int hour = Integer.parseInt(hourComboBox.getValue());
    int minute = Integer.parseInt(minuteComboBox.getValue());

    return EventMapper.map(
            datePicker.getValue(),
            hour,
            minute,
            titleField.getText(),
            descriptionField.getText(),
            locationField.getText(),
            Long.parseLong(capacityField.getText())
    );
  }
  /**
   * Populates the form fields with data from an existing event, used for editing.
   *
   * @param event The event whose data is to be loaded into the form.
   */

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
      if (minute < 15) {
        minuteComboBox.setValue("00");
      } else if (minute < 30) {
        minuteComboBox.setValue("15");
      } else if (minute < 45) {
        minuteComboBox.setValue("30");
      } else {
        minuteComboBox.setValue("45");
      }
    }
  }
}