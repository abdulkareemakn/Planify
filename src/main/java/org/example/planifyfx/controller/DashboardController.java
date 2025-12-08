package org.example.planifyfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.planifyfx.util.SceneManager;
import org.example.planifyfx.util.Statistics;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Dashboard screen.
 * Displays event statistics and provides navigation to other screens.
 */
public class DashboardController implements Initializable {

    @FXML
    private Button dashboardBtn;
    @FXML
    private Button eventsBtn;
    @FXML
    private Button addEventBtn;

    @FXML
    private Label totalEventsLabel;
    @FXML
    private Label weddingEventsLabel;
    @FXML
    private Label birthdayEventsLabel;
    @FXML
    private Label seminarEventsLabel;

    @FXML
    private VBox recentEventsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDashboardData();
        setActiveNavButton(dashboardBtn);
    }

    /**
     * Loads all dashboard data including statistics.
     */
    private void loadDashboardData() {
        loadStatistics();
    }

    /**
     * Loads event statistics from the database and updates the labels.
     */
    private void loadStatistics() {
        totalEventsLabel.setText(String.valueOf(Statistics.getTotalEvents()));
        weddingEventsLabel.setText(String.valueOf(Statistics.getTotalWeddingEvents()));
        birthdayEventsLabel.setText(String.valueOf(Statistics.getTotalBirthdayEvents()));
        seminarEventsLabel.setText(String.valueOf(Statistics.getTotalSeminarEvents()));
    }

    @FXML
    private void showDashboard() {
        setActiveNavButton(dashboardBtn);
        loadDashboardData();
    }

    @FXML
    private void showEvents() {
        setActiveNavButton(eventsBtn);
        try {
            SceneManager.getInstance().switchScene("Events.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            setActiveNavButton(dashboardBtn);
        }
    }

    @FXML
    private void addNewEvent() {
        setActiveNavButton(addEventBtn);
        try {
            SceneManager.getInstance().switchScene("CreateEvent.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            setActiveNavButton(dashboardBtn);
        }
    }

    @FXML
    private void handleLogout() {
        SceneManager.getInstance().switchScene("LoginPage.fxml");
    }

    /**
     * Updates the visual style of navigation buttons to show which is active.
     */
    private void setActiveNavButton(Button activeButton) {
        String inactiveStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6; -fx-cursor: hand;";
        String activeStyle = "-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6; -fx-cursor: hand;";
        
        dashboardBtn.setStyle(inactiveStyle);
        eventsBtn.setStyle(inactiveStyle);
        activeButton.setStyle(activeStyle);
    }

    /**
     * Refreshes the dashboard data. Called when returning from other screens.
     */
    public void refreshDashboard() {
        loadDashboardData();
    }
}
