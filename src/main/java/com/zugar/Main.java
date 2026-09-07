package com.zugar;

import com.zugar.controller.LoginController;
import com.zugar.controller.MainLayoutController;
import com.zugar.controller.RegisterController;
import com.zugar.db.DatabaseManager;
import com.zugar.factory.ViewFactory;
import com.zugar.model.User;
import com.zugar.repository.RentalRepository;
import com.zugar.repository.RentalRepositoryImpl;
import com.zugar.repository.UserRepository;
import com.zugar.repository.UserRepositoryImpl;
import com.zugar.service.AuthService;
import com.zugar.service.JwtService;
import javafx.application.Application;
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
        ViewFactory.ViewResult res = ViewFactory.loadView("/fxml/login.fxml");
        LoginController controller = res.getController();
        controller.init(authService, this::showMainLayout, this::showRegisterView);

        Scene scene = new Scene(res.getRoot(), 900, 650);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    private void showRegisterView() {
        ViewFactory.ViewResult res = ViewFactory.loadView("/fxml/register.fxml");
        RegisterController controller = res.getController();
        controller.init(authService, this::showLoginView);

        Scene scene = new Scene(res.getRoot(), 900, 650);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    private void showMainLayout(User user) {
        ViewFactory.ViewResult res = ViewFactory.loadView("/fxml/main_layout.fxml");
        MainLayoutController controller = res.getController();
        controller.init(authService, rentalRepository, user, this::showLoginView);

        Scene scene = new Scene(res.getRoot(), 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
