package org.example.planifyfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.example.planifyfx.model.BirthdayEvent;
import org.example.planifyfx.model.Client;
import org.example.planifyfx.model.SeminarEvent;
import org.example.planifyfx.model.Venue;
import org.example.planifyfx.model.WeddingEvent;
import org.example.planifyfx.repository.ClientRepository;
import org.example.planifyfx.repository.EventRepository;
import org.example.planifyfx.repository.VenueRepository;
import org.example.planifyfx.util.SceneManager;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for the Create Event screen.
 * Handles event creation with dynamic fields based on event type.
 */
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
    private ComboBox<String> venueCombo;

    @FXML
    private TextField clientNameField;
    @FXML
    private TextField clientEmailField;
    @FXML
    private TextField clientPhoneField;

    @FXML
    private VBox dynamicFieldsContainer;

    // Wedding fields
    private TextField brideNameField;
    private TextField groomNameField;
    private CheckBox photographerCheckBox;

    // Birthday fields
    private TextField ageField;
    private TextField themeField;
    private TextField numberOfKidsField;

    // Seminar fields
    private TextField chiefGuestField;
    private TextField speakerField;
    private TextField topicField;

    // Venue mapping (display name -> Venue object)
    private Map<String, Venue> venueMap = new HashMap<>();

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
        loadVenues();
    }

    /**
     * Loads venues from the database into the venue dropdown.
     */
    private void loadVenues() {
        VenueRepository.fetchAll(
            venues -> {
                venueCombo.getItems().clear();
                venueMap.clear();
                
                for (Venue venue : venues) {
                    String displayName = venue.getName() + " (Capacity: " + venue.getCapacity() + ")";
                    venueCombo.getItems().add(displayName);
                    venueMap.put(displayName, venue);
                }
            },
            error -> {
                System.err.println("Failed to load venues: " + error.getMessage());
                error.printStackTrace();
            }
        );
    }

    /**
     * Gets the selected venue from the dropdown.
     * @return The selected Venue, or null if none selected
     */
    private Venue getSelectedVenue() {
        String selected = venueCombo.getValue();
        if (selected == null || selected.isEmpty()) {
            return null;
        }
        return venueMap.get(selected);
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
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #F1F5F9;");

        Label sectionLabel = new Label("Wedding Details");
        sectionLabel.setFont(Font.font("System Bold", 15.0));
        sectionLabel.setStyle("-fx-text-fill: #1E293B;");

        GridPane grid = new GridPane();
        grid.setHgap(24.0);
        grid.setVgap(16.0);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        col2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(col1, col2);

        // Bride Name
        VBox brideBox = new VBox(6);
        Label brideLabel = new Label("Bride Name");
        brideLabel.setFont(Font.font("System Bold", 13.0));
        brideLabel.setStyle("-fx-text-fill: #1E293B;");
        brideNameField = new TextField();
        brideNameField.setPromptText("Enter bride's name");
        brideNameField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10;");
        brideBox.getChildren().addAll(brideLabel, brideNameField);
        grid.add(brideBox, 0, 0);

        // Groom Name
        VBox groomBox = new VBox(6);
        Label groomLabel = new Label("Groom Name");
        groomLabel.setFont(Font.font("System Bold", 13.0));
        groomLabel.setStyle("-fx-text-fill: #1E293B;");
        groomNameField = new TextField();
        groomNameField.setPromptText("Enter groom's name");
        groomNameField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10;");
        groomBox.getChildren().addAll(groomLabel, groomNameField);
        grid.add(groomBox, 1, 0);

        // Photographer Required
        VBox photoBox = new VBox(6);
        Label photoLabel = new Label("Photographer Required");
        photoLabel.setFont(Font.font("System Bold", 13.0));
        photoLabel.setStyle("-fx-text-fill: #1E293B;");
        photographerCheckBox = new CheckBox("Yes, include photographer");
        photographerCheckBox.setStyle("-fx-text-fill: #64748B;");
        photoBox.getChildren().addAll(photoLabel, photographerCheckBox);
        grid.add(photoBox, 0, 1);

        dynamicFieldsContainer.getChildren().addAll(separator, sectionLabel, grid);
    }

    private void showBirthdayFields() {
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #F1F5F9;");

        Label sectionLabel = new Label("Birthday Details");
        sectionLabel.setFont(Font.font("System Bold", 15.0));
        sectionLabel.setStyle("-fx-text-fill: #1E293B;");

        GridPane grid = new GridPane();
        grid.setHgap(24.0);
        grid.setVgap(16.0);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        col2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(col1, col2);

        // Age
        VBox ageBox = new VBox(6);
        Label ageLabel = new Label("Age");
        ageLabel.setFont(Font.font("System Bold", 13.0));
        ageLabel.setStyle("-fx-text-fill: #1E293B;");
        ageField = new TextField();
        ageField.setPromptText("Enter age");
        ageField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10;");
        ageField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                ageField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
        ageBox.getChildren().addAll(ageLabel, ageField);
        grid.add(ageBox, 0, 0);

        // Theme
        VBox themeBox = new VBox(6);
        Label themeLabel = new Label("Theme");
        themeLabel.setFont(Font.font("System Bold", 13.0));
        themeLabel.setStyle("-fx-text-fill: #1E293B;");
        themeField = new TextField();
        themeField.setPromptText("e.g., Superhero, Princess");
        themeField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10;");
        themeBox.getChildren().addAll(themeLabel, themeField);
        grid.add(themeBox, 1, 0);

        // Number of Kids
        VBox kidsBox = new VBox(6);
        Label kidsLabel = new Label("Number of Kids");
        kidsLabel.setFont(Font.font("System Bold", 13.0));
        kidsLabel.setStyle("-fx-text-fill: #1E293B;");
        numberOfKidsField = new TextField();
        numberOfKidsField.setPromptText("Enter number");
        numberOfKidsField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10;");
        numberOfKidsField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                numberOfKidsField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
        kidsBox.getChildren().addAll(kidsLabel, numberOfKidsField);
        grid.add(kidsBox, 0, 1);

        dynamicFieldsContainer.getChildren().addAll(separator, sectionLabel, grid);
    }

    private void showSeminarFields() {
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #F1F5F9;");

        Label sectionLabel = new Label("Seminar Details");
        sectionLabel.setFont(Font.font("System Bold", 15.0));
        sectionLabel.setStyle("-fx-text-fill: #1E293B;");

        GridPane grid = new GridPane();
        grid.setHgap(24.0);
        grid.setVgap(16.0);

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        col2.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(col1, col2);

        // Chief Guest
        VBox chiefGuestBox = new VBox(6);
        Label chiefGuestLabel = new Label("Chief Guest");
        chiefGuestLabel.setFont(Font.font("System Bold", 13.0));
        chiefGuestLabel.setStyle("-fx-text-fill: #1E293B;");
        chiefGuestField = new TextField();
        chiefGuestField.setPromptText("Enter chief guest name");
        chiefGuestField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10;");
        chiefGuestBox.getChildren().addAll(chiefGuestLabel, chiefGuestField);
        grid.add(chiefGuestBox, 0, 0);

        // Speaker
        VBox speakerBox = new VBox(6);
        Label speakerLabel = new Label("Speaker");
        speakerLabel.setFont(Font.font("System Bold", 13.0));
        speakerLabel.setStyle("-fx-text-fill: #1E293B;");
        speakerField = new TextField();
        speakerField.setPromptText("Enter speaker name");
        speakerField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10;");
        speakerBox.getChildren().addAll(speakerLabel, speakerField);
        grid.add(speakerBox, 1, 0);

        // Topic
        VBox topicBox = new VBox(6);
        Label topicLabel = new Label("Topic");
        topicLabel.setFont(Font.font("System Bold", 13.0));
        topicLabel.setStyle("-fx-text-fill: #1E293B;");
        topicField = new TextField();
        topicField.setPromptText("Enter seminar topic");
        topicField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10;");
        topicBox.getChildren().addAll(topicLabel, topicField);
        grid.add(topicBox, 0, 1, 2, 1);

        dynamicFieldsContainer.getChildren().addAll(separator, sectionLabel, grid);
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
            // Disable save button to prevent double-clicks
            saveBtn.setDisable(true);
            saveBtn.setText("Saving...");
            
            if (editingEventId == null) {
                createNewEvent();
            } else {
                updateEvent();
            }
        }
    }

    /**
     * Called when event is saved successfully.
     */
    private void onSaveSuccess() {
        saveBtn.setDisable(false);
        saveBtn.setText("Save Event");
        showAlert("Success", "Event saved successfully!", Alert.AlertType.INFORMATION);
        SceneManager.getInstance().switchScene("Events.fxml");
    }

    /**
     * Called when event save fails.
     */
    private void onSaveError(Throwable error) {
        saveBtn.setDisable(false);
        saveBtn.setText("Save Event");
        showAlert("Error", "Failed to save event: " + error.getMessage(), Alert.AlertType.ERROR);
        error.printStackTrace();
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

        // Get selected venue (optional)
        Venue selectedVenue = getSelectedVenue();

        System.out.println("Event Details:");
        System.out.println("Type: " + eventType);
        System.out.println("Name: " + eventName);
        System.out.println("Date & Time: " + eventDateTime);
        System.out.println("Attendance: " + attendance);
        System.out.println("Venue: " + (selectedVenue != null ? selectedVenue.getName() : "None"));
        System.out.println("Client: " + clientName + " (" + clientEmail + ", " + clientPhone + ")");

        // Create client with direct constructor (name, email, phoneNumber)
        Client client = new Client(clientName, clientEmail, clientPhone);

        if ("Wedding".equals(eventType)) {
            String brideName = brideNameField.getText().trim();
            String groomName = groomNameField.getText().trim();
            boolean photographerRequired = photographerCheckBox.isSelected();

            System.out.println("Wedding Details:");
            System.out.println("Bride: " + brideName);
            System.out.println("Groom: " + groomName);
            System.out.println("Photographer Required: " + photographerRequired);

            // Save client first, then create and save event with generated client ID
            ClientRepository.save(client, clientId -> {
                client.setId(clientId);
                WeddingEvent wedding = new WeddingEvent(eventName, eventDateTime, attendance, client,
                        brideName, groomName, photographerRequired);
                wedding.setVenue(selectedVenue);
                EventRepository.save(wedding,
                        () -> onSaveSuccess(),
                        error -> onSaveError(error));
            }, error -> onSaveError(error));

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

            // Capture for lambda
            final int finalNumberOfKids = numberOfKids;

            // Save client first, then create and save event with generated client ID
            ClientRepository.save(client, clientId -> {
                client.setId(clientId);
                BirthdayEvent birthday = new BirthdayEvent(eventName, eventDateTime, attendance, client,
                        age, theme, finalNumberOfKids);
                birthday.setVenue(selectedVenue);
                EventRepository.save(birthday,
                        () -> onSaveSuccess(),
                        error -> onSaveError(error));
            }, error -> onSaveError(error));

        } else if ("Seminar".equals(eventType)) {
            String chiefGuest = chiefGuestField.getText().trim();
            String speaker = speakerField.getText().trim();
            String topic = topicField.getText().trim();

            System.out.println("Seminar Details:");
            System.out.println("Chief Guest: " + chiefGuest);
            System.out.println("Speaker: " + speaker);
            System.out.println("Topic: " + topic);

            // Save client first, then create and save event with generated client ID
            ClientRepository.save(client, clientId -> {
                client.setId(clientId);
                SeminarEvent seminar = new SeminarEvent(eventName, eventDateTime, attendance, client,
                        chiefGuest, speaker, topic);
                seminar.setVenue(selectedVenue);
                EventRepository.save(seminar,
                        () -> onSaveSuccess(),
                        error -> onSaveError(error));
            }, error -> onSaveError(error));
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
        venueCombo.setValue(null);
        clientNameField.clear();
        clientEmailField.clear();
        clientPhoneField.clear();
        dynamicFieldsContainer.getChildren().clear();
        editingEventId = null;
        pageTitle.setText("Create New Event");
    }
}
