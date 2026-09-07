package com.zugar.controller;

import com.zugar.model.User;
import com.zugar.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private AuthService authService;
    private Runnable onNavigateToLogin;

    public void init(AuthService authService, Runnable onNavigateToLogin) {
        this.authService = authService;
        this.onNavigateToLogin = onNavigateToLogin;
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            User user = authService.register(username, password);
            statusLabel.setStyle("-fx-text-fill: #16a34a;");
            statusLabel.setText("Account created! Redirecting to login...");
            if (onNavigateToLogin != null) {
                onNavigateToLogin.run();
            }
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: #ef4444;");
            statusLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void showLogin() {
        if (onNavigateToLogin != null) {
            onNavigateToLogin.run();
        }
    }
}
