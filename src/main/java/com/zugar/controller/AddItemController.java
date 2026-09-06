package com.zugar.controller;

import com.zugar.model.RentalItem;
import com.zugar.model.User;
import com.zugar.repository.RentalRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.UUID;

public class AddItemController {
    @FXML private TextField titleField;
    @FXML private ComboBox<String> categoryBox;
    @FXML private TextField priceField;
    @FXML private TextField depositField;
    @FXML private TextField conditionField;
    @FXML private TextField locationField;
    @FXML private ComboBox<String> restrictionBox;
    @FXML private TextArea descriptionArea;
    @FXML private Label statusLabel;

    private RentalRepository rentalRepository;
    private User currentUser;
    private Runnable onItemAdded;

    public void init(RentalRepository rentalRepository, User currentUser, Runnable onItemAdded) {
        this.rentalRepository = rentalRepository;
        this.currentUser = currentUser;
        this.onItemAdded = onItemAdded;

        categoryBox.setItems(FXCollections.observableArrayList("Electronics", "Photography", "Audio/Visual", "Vehicles", "Sports", "Other"));
        categoryBox.getSelectionModel().selectFirst();

        restrictionBox.setItems(FXCollections.observableArrayList("Anyone", "University Members Only", "Verified Members Only", "Housing Society Only"));
        restrictionBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handlePublish() {
        if (currentUser == null) {
            return;
        }
        try {
            String title = titleField.getText();
            String category = categoryBox.getValue();
            double price = Double.parseDouble(priceField.getText());
            double deposit = Double.parseDouble(depositField.getText());
            String condition = conditionField.getText();
            String location = locationField.getText();
            String restriction = restrictionBox.getValue();
            String description = descriptionArea.getText();

            RentalItem item = new RentalItem(
                    UUID.randomUUID().toString(),
                    currentUser.getId(),
                    title,
                    category,
                    description,
                    price,
                    deposit,
                    condition,
                    location,
                    restriction,
                    true
            );

            rentalRepository.saveItem(item);
            statusLabel.setStyle("-fx-text-fill: #16a34a;");
            statusLabel.setText("Item published successfully!");
            clearForm();
            if (onItemAdded != null) {
                onItemAdded.run();
            }
        } catch (Exception e) {
            statusLabel.setStyle("-fx-text-fill: #ef4444;");
            statusLabel.setText("Failed to publish item: " + e.getMessage());
        }
    }

    private void clearForm() {
        titleField.clear();
        priceField.clear();
        depositField.clear();
        conditionField.clear();
        locationField.clear();
        descriptionArea.clear();
    }
}
