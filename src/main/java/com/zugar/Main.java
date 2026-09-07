package com.zugar;

import com.zugar.controller.LoginController;
import com.zugar.controller.MainLayoutController;
import com.zugar.db.DatabaseManager;
import com.zugar.model.User;
import com.zugar.repository.RentalRepository;
import com.zugar.repository.RentalRepositoryImpl;
import com.zugar.repository.UserRepository;
import com.zugar.repository.UserRepositoryImpl;
import com.zugar.service.AuthService;
import com.zugar.service.JwtService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class Main extends Application {
    private Stage primaryStage;
    private AuthService authService;
    private RentalRepository rentalRepository;

    @Override
    public void init() throws Exception {
        DatabaseManager.initializeDatabase();
        UserRepository userRepository = new UserRepositoryImpl();
        JwtService jwtService = new JwtService("zugar-secret-key-for-jwt-signing-2026-p2p");
        this.authService = new AuthService(userRepository, jwtService);
        this.rentalRepository = new RentalRepositoryImpl();
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Zugar - Peer-to-Peer Rental Marketplace");
        showLoginView();
        stage.show();
    }

    private void showLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            LoginController controller = loader.getController();
            controller.init(authService, this::showMainLayout);

            Scene scene = new Scene(root, 900, 650);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMainLayout(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_layout.fxml"));
            Parent root = loader.load();
            MainLayoutController controller = loader.getController();
            controller.init(authService, rentalRepository, user, this::showLoginView);

            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
