package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Attendance;
import com.cems.frontend.models.Event;
import com.cems.frontend.utils.LocalHttpClientHelper;
import com.cems.frontend.services.AttendanceService;
import com.cems.frontend.view.SceneNavigator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class AttendanceController {
    @FXML
    public Label eventOrganizer;

    @FXML
    public Label eventName;

    @FXML
    public Label eventDate;

    @FXML
    public Label eventLocation;

    @FXML
    public Button homeButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private HBox searchBox;

    @FXML
    private Label totalRegisteredLabel;

    @FXML
    private Label attendeesCheckedInLabel;

    @FXML
    private Label attendeesPendingLabel;

    @FXML
    private ComboBox<String> sortByComboBox;

    @FXML
    private TableView<Attendance> attendanceTableView;

    @FXML
    private TableColumn<Attendance,String> nameColumn;

    @FXML
    private TableColumn<Attendance,String> emailColumn;

    @FXML
    private TableColumn<Attendance,String> statusColumn;

    @FXML
    private TableColumn<Attendance, Instant> checkInTimeColumn;

    private final String SORT_BY =  "Sort by";
    private final String CHECKED_IN =  "Checked In";
    private final String PENDING =  "Pending";

    public void loadAttendanceForEvent(Event event) {
        eventName.setText(event.getTitle());
        eventOrganizer.setText(event.getTitle());
        eventLocation.setText(event.getLocation() != null ? event.getLocation() : "TBD");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy - HH:mm");
        if (event.getDateTime() != null) {
            eventDate.setText(event.getDateTime().atZone(ZoneId.systemDefault()).format(formatter));
        }

        fetchAttendanceRecords(event.getId());
    }


    private final ObservableList<Attendance> attendanceModelObservable = FXCollections.observableArrayList();

    private final AttendanceService attendanceService = new AttendanceService(LocalHttpClientHelper.getClient(), LocalHttpClientHelper.getMapper());

    @FXML
    public void initialize() {
        attendanceTableView.setPlaceholder(new Label("No Attendance data available at the moment"));
        //  set up the table view to display records
        setupTableView();
        attendanceSearchFilter();
        sortByComboBox.getItems().addAll(SORT_BY,CHECKED_IN,PENDING);
        handleSearchFocus();
    }

    //    TODO: sort functionality: sort by status of attendance
    @FXML
    void sortByChanged() {
        String sortValue = sortByComboBox.getValue();
        switch (sortValue) {
            case SORT_BY -> attendanceTableView.getSortOrder().clear();
            case CHECKED_IN -> {
                attendanceTableView.getSortOrder().clear();
                statusColumn.setSortType(TableColumn.SortType.DESCENDING);
                attendanceTableView.getSortOrder().add(statusColumn);
                attendanceTableView.sort();
            }
            case PENDING -> {
                attendanceTableView.getSortOrder().clear();
                statusColumn.setSortType(TableColumn.SortType.ASCENDING);
                attendanceTableView.getSortOrder().add(statusColumn);
                attendanceTableView.sort();
            }
        }
    }

    //    TODO: search functionality: search by name or email of attendee
    private void attendanceSearchFilter() {
//        initial filtered list
        FilteredList<Attendance> filteredData = new FilteredList<>(attendanceModelObservable, b -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(attendee ->{
//                Display all if there is no search value
                if (newValue == null || newValue.isBlank()) return true;
                String search = newValue.toLowerCase();

//                TODO: update to be name or email
                return  attendee.getEmail().toLowerCase().contains(search) || attendee.getName().toLowerCase().contains(search);
            });
        });

        SortedList<Attendance> sortedData = new SortedList<>(filteredData);
//        Bind the sorted list with the table view
        sortedData.comparatorProperty().bind(attendanceTableView.comparatorProperty());
        attendanceTableView.setItems(sortedData);
    }

    private void setupTableView (){
//        Property value factory corresponds to AttendanceModel fields
//        The table column is annotated above
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        checkInTimeColumn.setCellValueFactory(new PropertyValueFactory<>("checkInTime"));
    }

    private void fetchAttendanceRecords(UUID eventId){
//        create a thread to fetch data in the background
        Task<List<Attendance>> fetchTask = new Task<>() {
            @Override
            protected List<Attendance> call() throws Exception {
                return attendanceService.getEventAttendance(eventId.toString());
            }
        };

        fetchTask.setOnSucceeded(e -> {
            Platform.runLater(() -> {
                //  populate the observable lust
                attendanceModelObservable.setAll(fetchTask.getValue());
                //  Update labels
                updateAttendanceRecords();
                //  populate the table rows with the attendance data
                attendanceTableView.setItems(attendanceModelObservable);
            });

        });
//      run the thread to fetch attendance data in the background
        new Thread(fetchTask).start();
    }


    private void handleSearchFocus(){
        searchTextField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                searchBox.getStyleClass().add("search-box-focused");
            } else {
                searchBox.getStyleClass().remove("search-box-focused");
            }
        });
    }

    private void updateAttendanceRecords(){
        totalRegisteredLabel.setText(String.valueOf(attendanceModelObservable.size()));
        //        Filter by status to get count for checked in and pending attendees
        attendeesCheckedInLabel.setText(String.valueOf(attendanceModelObservable.size()));
        attendeesPendingLabel.setText(String.valueOf(attendanceModelObservable.size()));
    }

    @FXML
    private void handleGoHome() {
        SceneNavigator.loadPage("home-view.fxml");
    }

}

