package com.zugar.controller;

import com.zugar.model.User;
import com.zugar.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private AuthService authService;
    private Consumer<User> onLoginSuccess;

    public void init(AuthService authService, Consumer<User> onLoginSuccess) {
        this.authService = authService;
        this.onLoginSuccess = onLoginSuccess;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            String token = authService.login(username, password);
            User user = authService.validateAndGetUser(token);
            if (user != null && onLoginSuccess != null) {
                onLoginSuccess.accept(user);
            }
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: #ef4444;");
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            User user = authService.register(username, password);
            statusLabel.setStyle("-fx-text-fill: #16a34a;");
            statusLabel.setText("Registered! Click Login to proceed.");
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: #ef4444;");
            statusLabel.setText(e.getMessage());
        }
    }
}
