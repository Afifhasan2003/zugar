package com.zugar;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public final class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Zugar");
        stage.setScene(new Scene(new Label("Zugar"), 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
