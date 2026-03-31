package com.cems.frontend.controllers.pages;

import com.cems.frontend.models.Attendance;
import com.cems.frontend.models.Event;
import com.cems.frontend.models.Paths;
import com.cems.frontend.utils.LocalHttpClientHelper;
import com.cems.frontend.services.AttendanceService;
import com.cems.frontend.utils.LocaleUtil;
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
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
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
    public Button backButton;

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

    private Event loadedEvent;

    private final ObservableList<Attendance> attendanceModelObservable = FXCollections.observableArrayList();

    private final AttendanceService attendanceService = new AttendanceService(LocalHttpClientHelper.getClient(), LocalHttpClientHelper.getMapper());

    private ResourceBundle resourceBundle;
    private LocaleUtil localeService = LocaleUtil.getInstance();

    public void loadAttendanceForEvent(Event event) {
        loadedEvent = event;
        eventName.setText(event.getTitle());
        eventOrganizer.setText(event.getTitle());
        eventLocation.setText(event.getLocation() != null ? event.getLocation() : "TBD");
        DateTimeFormatter formatter = localeService.dateTime(FormatStyle.FULL, FormatStyle.SHORT);
        if (event.getDateTime() != null) {
            eventDate.setText(event.getDateTime().atZone(ZoneId.systemDefault()).format(formatter));
        }
        fetchAttendanceRecords(event.getId());
    }

    @FXML
    public void initialize() {
        resourceBundle = localeService.getBundle(Paths.ATTENDANCE_VIEW);
        attendanceTableView.setPlaceholder(new Label(resourceBundle.getString("attendance.no_attendance_data")));
        //  set up the table view to display records
        setupTableView();
        attendanceSearchFilter();
        handleSearchFocus();
    }

    private void attendanceSearchFilter() {
//        initial filtered list
        FilteredList<Attendance> filteredData = new FilteredList<>(attendanceModelObservable, b -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(attendee ->{
//                Display all if there is no search value
                if (newValue == null || newValue.isBlank()) return true;
                String search = newValue.toLowerCase();

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
        checkInTimeColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Instant time, boolean empty) {
                super.updateItem(time, empty);
                if (empty) {
                    setText(null);
                }else {
                    setText(time == null ? resourceBundle.getString("attendance.check_in_time_unavailable") : time.atZone(ZoneId.systemDefault()).format(localeService.time(FormatStyle.MEDIUM)));
                }
             }
        });
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
        long totalAttendanceCount = attendanceModelObservable.size();
        long attendancePresentCount = attendanceModelObservable.stream().filter(attendance->attendance.getStatus().equals("PRESENT")).count();
        totalRegisteredLabel.setText(String.valueOf(totalAttendanceCount));
        //        Filter by status to get count for checked in and pending attendees
        attendeesCheckedInLabel.setText(String.valueOf(attendancePresentCount));
        attendeesPendingLabel.setText(String.valueOf(totalAttendanceCount - attendancePresentCount));
    }

    @FXML
    private void handleGoBack() {
        SceneNavigator.loadEventDetail(loadedEvent);
    }

}

