package com.zugar.factory;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class ViewFactory {

    public static ViewResult loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource(fxmlPath));
            Parent root = loader.load();
            return new ViewResult(root, loader.getController());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load view: " + fxmlPath, e);
        }
    }

    public static class ViewResult {
        private final Parent root;
        private final Object controller;

        public ViewResult(Parent root, Object controller) {
            this.root = root;
            this.controller = controller;
        }

        public Parent getRoot() {
            return root;
        }

        @SuppressWarnings("unchecked")
        public <T> T getController() {
            return (T) controller;
        }
    }
}
