package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;

public class AttendanceController {

    @FXML
    private TableView<String> AttendanceTableView;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label totalRegisteredLabel;

    @FXML
    private Label attendeesCheckedInLabel;

    @FXML
    private Label attendeesPendingLabel;


    @FXML
    private ComboBox<String> sortByComboBox;

    @FXML
    private TableColumn<String,String> nameColumn;

    @FXML
    private TableColumn<String,String> emailColumn;

    @FXML
    private TableColumn<String,String> statusColumn;

    @FXML
    private TableColumn<String,String> checkInTimeColumn;

    ObservableList<String> sortList = FXCollections.observableArrayList("Attended","Pending");

    @FXML
    void sortByChanged(ActionEvent event) {

    }

    private void setupAttendanceSearchFilter() {
        FilteredList<Event> filteredData = new FilteredList<>(masterData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(event -> {
                if (newValue == null || newValue.isBlank()) return true;

                String filter = newValue.toLowerCase();
                return event.getTitle().toLowerCase().contains(filter) ||
                        event.getLocation().toLowerCase().contains(filter);
            });
            populateGrid(filteredData);
        });
    }

    @FXML
    public void initialize() {
        sortByComboBox.setItems(sortList);
    }

}

