package org.example.planifyfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import org.example.planifyfx.model.BirthdayEvent;
import org.example.planifyfx.model.Client;
import org.example.planifyfx.model.SeminarEvent;
import org.example.planifyfx.model.WeddingEvent;
import org.example.planifyfx.repository.ClientRepository;
import org.example.planifyfx.repository.EventRepository;
import org.example.planifyfx.util.ContactInfo;
import org.example.planifyfx.util.EventInfo;
import org.example.planifyfx.util.SceneManager;
import org.example.planifyfx.util.Statistics;


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


public class CreateEventController implements Initializable {

    @FXML
    private Button dashboardBtn;
    @FXML
    private Button eventsBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private Label pageTitle;

    @FXML
    private ComboBox<String> eventTypeCombo;
    @FXML
    private TextField eventNameField;
    @FXML
    private DatePicker eventDatePicker;
    @FXML
    private ComboBox<String> hourCombo;
    @FXML
    private ComboBox<String> minuteCombo;
    @FXML
    private TextField attendanceField;

    @FXML
    private TextField clientNameField;
    @FXML
    private TextField clientEmailField;
    @FXML
    private TextField clientPhoneField;

    @FXML
    private VBox dynamicFieldsContainer;

    private TextField brideNameField;
    private TextField groomNameField;
    private CheckBox photographerCheckBox;

    private TextField ageField;
    private TextField themeField;
    private TextField numberOfKidsField;

    private TextField chiefGuestField;
    private TextField speakerField;
    private TextField topicField;


    private Integer editingEventId = null;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[\\d\\s\\-\\+\\(\\)]{10,}$");


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupEventTypeListener();
        setupValidation();
        setupDateRestrictions();
    }

    private void setupEventTypeListener() {
        eventTypeCombo.setOnAction(e -> {
            String selectedType = eventTypeCombo.getValue();
            showDynamicFields(selectedType);
        });
    }

    private void setupValidation() {
        attendanceField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                attendanceField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        clientPhoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[\\d\\s\\-\\+\\(\\)]*")) {
                clientPhoneField.setText(oldVal);
            }
        });
    }

    private void setupDateRestrictions() {
        eventDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
    }

    private void showDynamicFields(String eventType) {
        dynamicFieldsContainer.getChildren().clear();

        if ("Wedding".equals(eventType)) {
            showWeddingFields();
        } else if ("Birthday".equals(eventType)) {
            showBirthdayFields();
        } else if ("Seminar".equals(eventType)) {
            showSeminarFields();
        }
    }

    private void showWeddingFields() {
        Label sectionLabel = new Label("Wedding Details");
        sectionLabel.setFont(Font.font("System Bold", 16.0));

        GridPane grid = new GridPane();
        grid.setHgap(15.0);
        grid.setVgap(10.0);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        col2.setHgrow(Priority.SOMETIMES);
        grid.getColumnConstraints().addAll(col1, col2);

        grid.add(new Label("Bride Name *"), 0, 0);
        brideNameField = new TextField();
        grid.add(brideNameField, 1, 0);

        grid.add(new Label("Groom Name *"), 0, 1);
        groomNameField = new TextField();
        grid.add(groomNameField, 1, 1);

        grid.add(new Label("Photographer Required"), 0, 2);
        photographerCheckBox = new CheckBox();
        grid.add(photographerCheckBox, 1, 2);

        dynamicFieldsContainer.getChildren().addAll(sectionLabel, grid);
    }

    private void showBirthdayFields() {
        Label sectionLabel = new Label("Birthday Details");
        sectionLabel.setFont(Font.font("System Bold", 16.0));

        GridPane grid = new GridPane();
        grid.setHgap(15.0);
        grid.setVgap(10.0);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        col2.setHgrow(Priority.SOMETIMES);
        grid.getColumnConstraints().addAll(col1, col2);

        grid.add(new Label("Age *"), 0, 0);
        ageField = new TextField();
        ageField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                ageField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
        grid.add(ageField, 1, 0);

        grid.add(new Label("Theme"), 0, 1);
        themeField = new TextField();
        grid.add(themeField, 1, 1);

        grid.add(new Label("Number of Kids"), 0, 2);
        numberOfKidsField = new TextField();
        numberOfKidsField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                numberOfKidsField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
        grid.add(numberOfKidsField, 1, 2);

        dynamicFieldsContainer.getChildren().addAll(sectionLabel, grid);
    }

    private void showSeminarFields() {
        Label sectionLabel = new Label("Seminar Details");
        sectionLabel.setFont(Font.font("System Bold", 16.0));

        GridPane grid = new GridPane();
        grid.setHgap(15.0);
        grid.setVgap(10.0);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        col2.setHgrow(Priority.SOMETIMES);
        grid.getColumnConstraints().addAll(col1, col2);

        grid.add(new Label("Chief Guest *"), 0, 0);
        chiefGuestField = new TextField();
        grid.add(chiefGuestField, 1, 0);

        grid.add(new Label("Speaker *"), 0, 1);
        speakerField = new TextField();
        grid.add(speakerField, 1, 1);

        grid.add(new Label("Topic *"), 0, 2);
        topicField = new TextField();
        grid.add(topicField, 1, 2);

        dynamicFieldsContainer.getChildren().addAll(sectionLabel, grid);
    }

    @FXML
    private void showDashboard() {
        SceneManager.getInstance().switchScene("Dashboard.fxml");
    }

    @FXML
    private void showEvents() {
        SceneManager.getInstance().switchScene("Events.fxml");

        System.out.println("Navigate to Events");
    }

    @FXML
    private void cancel() {
        boolean hasData = hasFormData();

        if (hasData) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Cancel");
            confirmAlert.setHeaderText("Unsaved Changes");
            confirmAlert.setContentText("You have unsaved changes. Are you sure you want to cancel?");

            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    navigateToEvents();
                }
            });
        } else {
            navigateToEvents();
        }
    }

    private boolean hasFormData() {
        return !eventNameField.getText().trim().isEmpty() ||
                eventDatePicker.getValue() != null ||
                !clientNameField.getText().trim().isEmpty() ||
                !clientEmailField.getText().trim().isEmpty() ||
                !clientPhoneField.getText().trim().isEmpty() ||
                !attendanceField.getText().trim().isEmpty();
    }

    private void navigateToEvents() {
        SceneManager.getInstance().switchScene("Events.fxml");
        System.out.println("Cancel and go back to Events");
    }

    @FXML
    private void saveEvent() {
        if (validateForm()) {
            try {
                if (editingEventId == null) {
                    createNewEvent();
                } else {
                    updateEvent();
                }

                showAlert("Success", "Event saved successfully!", Alert.AlertType.INFORMATION);

                SceneManager.getInstance().switchScene("Events.fxml");
                System.out.println("Event saved successfully");

            } catch (Exception e) {
                showAlert("Error", "Failed to save event: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (eventTypeCombo.getValue() == null) {
            errors.append("• Event type is required\n");
        }

        if (eventNameField.getText().trim().isEmpty()) {
            errors.append("• Event name is required\n");
        }

        if (eventDatePicker.getValue() == null) {
            errors.append("• Event date is required\n");
        } else if (eventDatePicker.getValue().isBefore(LocalDate.now())) {
            errors.append("• Event date cannot be in the past\n");
        }

        if (hourCombo.getValue() == null || minuteCombo.getValue() == null) {
            errors.append("• Event time is required\n");
        }

        if (attendanceField.getText().trim().isEmpty()) {
            errors.append("• Expected attendance is required\n");
        } else {
            try {
                int attendance = Integer.parseInt(attendanceField.getText().trim());
                if (attendance <= 0) {
                    errors.append("• Expected attendance must be greater than 0\n");
                }
            } catch (NumberFormatException e) {
                errors.append("• Expected attendance must be a valid number\n");
            }
        }

        if (clientNameField.getText().trim().isEmpty()) {
            errors.append("• Client name is required\n");
        }

        if (clientEmailField.getText().trim().isEmpty()) {
            errors.append("• Client email is required\n");
        } else if (!EMAIL_PATTERN.matcher(clientEmailField.getText().trim()).matches()) {
            errors.append("• Client email format is invalid\n");
        }

        if (clientPhoneField.getText().trim().isEmpty()) {
            errors.append("• Client phone number is required\n");
        } else if (!PHONE_PATTERN.matcher(clientPhoneField.getText().trim()).matches()) {
            errors.append("• Client phone number format is invalid\n");
        }

        String eventType = eventTypeCombo.getValue();
        if ("Wedding".equals(eventType)) {
            if (brideNameField == null || brideNameField.getText().trim().isEmpty()) {
                errors.append("• Bride name is required for weddings\n");
            }
            if (groomNameField == null || groomNameField.getText().trim().isEmpty()) {
                errors.append("• Groom name is required for weddings\n");
            }
        } else if ("Birthday".equals(eventType)) {
            if (ageField == null || ageField.getText().trim().isEmpty()) {
                errors.append("• Age is required for birthday parties\n");
            } else {
                try {
                    int age = Integer.parseInt(ageField.getText().trim());
                    if (age <= 0 || age > 150) {
                        errors.append("• Age must be between 1 and 150\n");
                    }
                } catch (NumberFormatException e) {
                    errors.append("• Age must be a valid number\n");
                }
            }

            if (numberOfKidsField != null && !numberOfKidsField.getText().trim().isEmpty()) {
                try {
                    int numKids = Integer.parseInt(numberOfKidsField.getText().trim());
                    if (numKids < 0) {
                        errors.append("• Number of kids cannot be negative\n");
                    }
                } catch (NumberFormatException e) {
                    errors.append("• Number of kids must be a valid number\n");
                }
            }
        } else if ("Seminar".equals(eventType)) {
            if (chiefGuestField == null || chiefGuestField.getText().trim().isEmpty()) {
                errors.append("Chief Guest is required for Seminars\n");
            }
            if (speakerField == null || speakerField.getText().trim().isEmpty()) {
                errors.append("Speaker is required for Seminars\n");
            }
            if (topicField == null || topicField.getText().trim().isEmpty()) {
                errors.append("Topic is required for Seminars\n");
            }

        }

        if (errors.length() > 0) {
            showAlert("Validation Error", "Please fix the following errors:\n\n" + errors.toString(), Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void createNewEvent() {
        System.out.println("Creating new event...");

        String eventType = eventTypeCombo.getValue();
        String eventName = eventNameField.getText().trim();
        LocalDate eventDate = eventDatePicker.getValue();
        LocalTime eventTime = LocalTime.of(
                Integer.parseInt(hourCombo.getValue()),
                Integer.parseInt(minuteCombo.getValue())
        );
        LocalDateTime eventDateTime = LocalDateTime.of(eventDate, eventTime);
        int attendance = Integer.parseInt(attendanceField.getText().trim());

        String clientName = clientNameField.getText().trim();
        String clientEmail = clientEmailField.getText().trim();
        String clientPhone = clientPhoneField.getText().trim();

        System.out.println("Event Details:");
        System.out.println("Type: " + eventType);
        System.out.println("Name: " + eventName);
        System.out.println("Date & Time: " + eventDateTime);
        System.out.println("Attendance: " + attendance);
        System.out.println("Client: " + clientName + " (" + clientEmail + ", " + clientPhone + ")");

        if ("Wedding".equals(eventType)) {
            String brideName = brideNameField.getText().trim();
            String groomName = groomNameField.getText().trim();
            boolean photographerRequired = photographerCheckBox.isSelected();

            System.out.println("Wedding Details:");
            System.out.println("Bride: " + brideName);
            System.out.println("Groom: " + groomName);
            System.out.println("Photographer Required: " + photographerRequired);

            ContactInfo contactInfo = new ContactInfo(clientPhone, clientEmail);

            Client client = new Client(clientName, contactInfo);
            EventInfo eventInfo = new EventInfo(eventName, attendance, client);

            WeddingEvent wedding = new WeddingEvent(eventInfo, brideName, groomName, photographerRequired);
            wedding.setTime(eventDateTime);
            ClientRepository.save(client, clientId -> {
                client.setId(clientId);
                wedding.setClient(client);
                EventRepository.save(wedding);
            }, Throwable::printStackTrace);
            Statistics.totalEvents++;
            Statistics.totalWeddingEvents++;
            Statistics.eventsAdded++;

        } else if ("Birthday".equals(eventType)) {
            int age = Integer.parseInt(ageField.getText().trim());
            String theme = themeField != null ? themeField.getText().trim() : "";
            int numberOfKids = 0;
            if (numberOfKidsField != null && !numberOfKidsField.getText().trim().isEmpty()) {
                numberOfKids = Integer.parseInt(numberOfKidsField.getText().trim());
            }

            System.out.println("Birthday Details:");
            System.out.println("Age: " + age);
            System.out.println("Theme: " + theme);
            System.out.println("Number of Kids: " + numberOfKids);


            ContactInfo contactInfo = new ContactInfo(clientPhone, clientEmail);

            Client client = new Client(clientName, contactInfo);
            EventInfo eventInfo = new EventInfo(eventName, attendance, client);

            BirthdayEvent birthday = new BirthdayEvent(eventInfo, age, theme, numberOfKids);
            birthday.setTime(eventDateTime);

            ClientRepository.save(client, clientId -> {
                client.setId(clientId);
                birthday.setClient(client);
                EventRepository.save(birthday);
            }, Throwable::printStackTrace);
            Statistics.totalEvents++;
            Statistics.totalBirthdayEvents++;
            Statistics.eventsAdded++;
        } else if ("Seminar".equals(eventType)) {
            String chiefGuest = chiefGuestField.getText().trim();
            String speaker = speakerField.getText().trim();
            String topic = topicField.getText().trim();

            System.out.println("Seminar Details:");
            System.out.println("Chief Guest: " + chiefGuest);
            System.out.println("Speaker: " + speaker);
            System.out.println("Topic: " + topic);

            ContactInfo contactInfo = new ContactInfo(clientPhone, clientEmail);
            Client client = new Client(clientName, contactInfo);
            EventInfo eventInfo = new EventInfo(eventName, attendance, client);

            SeminarEvent seminar = new SeminarEvent(eventInfo, chiefGuest, speaker, topic);
            seminar.setTime(eventDateTime);

            ClientRepository.save(client, clientId -> {
                client.setId(clientId);
                seminar.setClient(client);
                EventRepository.save(seminar);
            }, Throwable::printStackTrace);
            Statistics.totalEvents++;
            Statistics.totalSeminarEvents++;
            Statistics.eventsAdded++;
        }
    }

    private void updateEvent() {
        System.out.println("Updating event with ID: " + editingEventId);
    }

    public void setEventForEditing(Integer eventId, String eventType, String eventName,
                                   LocalDate eventDate, LocalTime eventTime, int attendance,
                                   String clientName, String clientEmail, String clientPhone) {
        this.editingEventId = eventId;
        pageTitle.setText("Edit Event");


        eventTypeCombo.setValue(eventType);
        eventNameField.setText(eventName);
        eventDatePicker.setValue(eventDate);
        hourCombo.setValue(String.format("%02d", eventTime.getHour()));
        minuteCombo.setValue(String.format("%02d", eventTime.getMinute()));
        attendanceField.setText(String.valueOf(attendance));

        clientNameField.setText(clientName);
        clientEmailField.setText(clientEmail);
        clientPhoneField.setText(clientPhone);

        showDynamicFields(eventType);
    }

    public void setWeddingData(String brideName, String groomName, boolean photographerRequired) {
        brideNameField.setText(brideName);
        groomNameField.setText(groomName);
        photographerCheckBox.setSelected(photographerRequired);
    }

    public void setBirthdayData(int age, String theme, int numberOfKids) {
        ageField.setText(String.valueOf(age));
        themeField.setText(theme);
        numberOfKidsField.setText(String.valueOf(numberOfKids));
    }

    public void setSeminarData(String chiefGuest, String speaker, String topic) {
        chiefGuestField.setText(chiefGuest);
        speakerField.setText(speaker);
        topicField.setText(topic);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void clearForm() {
        eventTypeCombo.setValue(null);
        eventNameField.clear();
        eventDatePicker.setValue(null);
        hourCombo.setValue(null);
        minuteCombo.setValue(null);
        attendanceField.clear();
        clientNameField.clear();
        clientEmailField.clear();
        clientPhoneField.clear();
        dynamicFieldsContainer.getChildren().clear();
        editingEventId = null;
        pageTitle.setText("Create New Event");
    }
}
