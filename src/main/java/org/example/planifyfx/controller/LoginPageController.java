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
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordField.setOnKeyPressed(this::handleKeyPressed);
        usernameField.setOnKeyPressed(this::handleKeyPressed);

        errorLabel.setVisible(false);
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        errorLabel.setVisible(false);

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        loginButton.setDisable(true);
        loginButton.setText("Logging in...");

        Authentication.loginUser(username, password,
            isValid -> {
                loginButton.setDisable(false);
                loginButton.setText("Login");

                if (isValid) {
                    try {
                        SceneManager.getInstance().switchScene("Dashboard.fxml");
                    } catch (Exception e) {
                        e.printStackTrace();
                        showError("Failed to load dashboard. Please try again.");
                    }
                } else {
                    showError("Invalid username or password. Please try again.");
                    passwordField.clear();
                }
            },
            errorMessage -> {
                loginButton.setDisable(false);
                loginButton.setText("Login");
                showError("Login failed: " + errorMessage);
                passwordField.clear();
            }
        );
    }

    @FXML
    private void handleResetPassword() {
        try {
            SceneManager.getInstance().switchScene("ResetPassword.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load password reset page.");
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public void clearForm() {
        usernameField.clear();
        passwordField.clear();
        errorLabel.setVisible(false);
    }
}