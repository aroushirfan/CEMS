package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Attendance;
import com.cems.frontend.services.LocalHttpClient;
import com.cems.frontend.services.attendance.ApiAttendanceService;
import com.cems.frontend.services.attendance.IAttendanceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class AttendanceController {

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

    private final ObservableList<Attendance> attendanceModelObservable = FXCollections.observableArrayList(
//            TODO: remove dummy data
            new Attendance(UUID.randomUUID(), UUID.randomUUID(),Instant.now(),"checked in"),
            new Attendance(UUID.randomUUID(), UUID.randomUUID(),Instant.now(),"Pending"),
            new Attendance(UUID.randomUUID(), UUID.randomUUID(),Instant.now(),"checked in"),
            new Attendance(UUID.randomUUID(), UUID.randomUUID(),Instant.now(),"Pending")
    );

    private final IAttendanceService attendanceService = new ApiAttendanceService(LocalHttpClient.getClient(),LocalHttpClient.getMapper());

    @FXML
    public void initialize() {
        attendanceTableView.setPlaceholder(new Label("No Attendance data available at the moment"));
        fetchAttendanceRecords();
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
                return  attendee.getStatus().toLowerCase().contains(search);
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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        checkInTimeColumn.setCellValueFactory(new PropertyValueFactory<>("checkInTime"));

//        populate the table rows with the attendance data
        attendanceTableView.setItems(attendanceModelObservable);
    }

    private void fetchAttendanceRecords(){
//        create a thread to fetch data in the background
        Task<List<Attendance>> fetchTask = new Task<>() {
            @Override
            protected List<Attendance> call() throws Exception {
                return attendanceService.getEventAttendance(UUID.randomUUID().toString());
            }
        };

        fetchTask.setOnSucceeded(e -> {
//            populate the observable lust
            attendanceModelObservable.setAll(fetchTask.getValue());

//          set up the table view to display records
            setupTableView();
//            Update labels
            updateAttendanceRecords();
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

}

