package org.example.planifyfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.example.planifyfx.util.Authentication;
import org.example.planifyfx.util.SceneManager;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ResetPasswordController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button resetButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label messageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernameField.setOnKeyPressed(this::handleKeyPressed);
        newPasswordField.setOnKeyPressed(this::handleKeyPressed);
        confirmPasswordField.setOnKeyPressed(this::handleKeyPressed);

        messageLabel.setVisible(false);
    }

    @FXML
    private void handleResetPassword() {
        String username = usernameField.getText().trim();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        messageLabel.setVisible(false);

        Optional<String> error = validateInputs(username, newPassword, confirmPassword);
        if (error.isPresent()) {
            showMessage(error.get(), false);
            return;
        }

        setUIBusy(true);

        Authentication.userExists(username,
                exists -> {
                    if (!exists) {
                        setUIBusy(false);
                        showMessage("Username not found.", false);
                        return;
                    }

                    Authentication.resetPassword(username, newPassword,
                            success -> {
                                resetButton.setDisable(false);
                                cancelButton.setDisable(false);
                                resetButton.setText("Reset Password");

                                if (success) {
                                    showMessage("Password reset successfully! Redirecting to login...", true);
                                    SceneManager.getInstance().switchScene("LoginPage.fxml");
                                }
                            },
                            errorMessage -> {
                                setUIBusy(false);
                                showMessage("Error: " + errorMessage, false);
                            }
                    );
                },
                errorMessage -> {
                    setUIBusy(false);
                    showMessage("Error checking username: " + errorMessage, false);
                }
        );
    }

    @FXML
    private void handleCancel() {
        try {
            SceneManager.getInstance().switchScene("LoginPage.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Failed to return to login page.", false);
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleResetPassword();
        }
    }

    private void showMessage(String message, boolean isSuccess) {
        messageLabel.setText(message);
        messageLabel.setStyle(isSuccess ? "-fx-text-fill: #10B981;" : "-fx-text-fill: #EF4444;");
        messageLabel.setVisible(true);
    }

    private void setUIBusy(boolean isBusy) {
        resetButton.setDisable(isBusy);
        cancelButton.setDisable(isBusy);
        resetButton.setText(isBusy ? "Resetting..." : "Reset Password");
    }

    private Optional<String> validateInputs(String username, String newPassword, String confirmPassword) {
        if (username.isEmpty()) {
            return Optional.of("Please enter a username.");
        }
        if (newPassword.isEmpty()) {
            return Optional.of("Please enter a new password.");
        }
        if (newPassword.length() < 6) {
            return Optional.of("Password must be at least 6 characters long.");
        }
        if (!newPassword.equals(confirmPassword)) {
            return Optional.of("Passwords do not match.");
        }
        return Optional.empty();
    }
}