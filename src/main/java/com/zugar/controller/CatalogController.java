package com.zugar.controller;

import com.zugar.model.RentalItem;
import com.zugar.model.RentalRequest;
import com.zugar.model.User;
import com.zugar.repository.RentalRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CatalogController {
    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private ListView<RentalItem> itemsListView;

    private RentalRepository rentalRepository;
    private User currentUser;

    public void init(RentalRepository rentalRepository, User currentUser) {
        this.rentalRepository = rentalRepository;
        this.currentUser = currentUser;

        categoryFilter.setItems(FXCollections.observableArrayList("All Categories", "Electronics", "Photography", "Audio/Visual", "Vehicles", "Sports", "Other"));
        categoryFilter.getSelectionModel().selectFirst();
        categoryFilter.setOnAction(e -> handleSearch());

        itemsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(RentalItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(createItemCard(item));
                }
            }
        });

        loadItems();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText() != null ? searchField.getText().toLowerCase().trim() : "";
        String cat = categoryFilter.getValue();

        List<RentalItem> all = rentalRepository.findAllItems();
        List<RentalItem> filtered = all.stream().filter(item -> {
            boolean isNotMine = currentUser == null || !item.getOwnerId().equals(currentUser.getId());
            boolean matchQuery = query.isEmpty() || item.getTitle().toLowerCase().contains(query) || item.getDescription().toLowerCase().contains(query);
            boolean matchCat = cat == null || cat.equals("All Categories") || item.getCategory().equalsIgnoreCase(cat);
            return isNotMine && matchQuery && matchCat;
        }).collect(Collectors.toList());

        itemsListView.setItems(FXCollections.observableArrayList(filtered));
    }

    public void loadItems() {
        handleSearch();
    }

    private VBox createItemCard(RentalItem item) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");

        HBox header = new HBox(10);
        Label title = new Label(item.getTitle());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label badge = new Label(item.isAvailable() ? "Directly Available" : "Restricted / Rented");
        badge.getStyleClass().add(item.isAvailable() ? "badge-available" : "badge-restricted");
        header.getChildren().addAll(title, badge);

        Label details = new Label("Category: " + item.getCategory() + " | Price: ৳" + item.getPricePerDay() + "/day | Deposit: ৳" + item.getDeposit() + " | Location: " + item.getLocation());
        details.setStyle("-fx-text-fill: #475569;");

        Label desc = new Label(item.getDescription());
        desc.setStyle("-fx-text-fill: #64748b;");

        HBox actions = new HBox(10);
        Button requestBtn = new Button("Request Rental");
        requestBtn.getStyleClass().add("primary-button");
        requestBtn.setOnAction(e -> openRentalRequestDialog(item));

        actions.getChildren().add(requestBtn);
        card.getChildren().addAll(header, details, desc, actions);
        return card;
    }

    private void openRentalRequestDialog(RentalItem item) {
        if (currentUser == null) return;

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Request Rental: " + item.getTitle());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox content = new VBox(10);
        content.setPadding(new Insets(16));

        TextField startField = new TextField("2026-09-10");
        TextField endField = new TextField("2026-09-12");
        TextField priceField = new TextField(String.valueOf(item.getPricePerDay()));
        TextArea messageArea = new TextArea("I would like to rent this item.");

        content.getChildren().addAll(
                new Label("Start Date (YYYY-MM-DD):"), startField,
                new Label("End Date (YYYY-MM-DD):"), endField,
                new Label("Offered Price / Day (৳):"), priceField,
                new Label("Message to Owner:"), messageArea
        );

        dialog.getDialogPane().setContent(content);
        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    double offered = Double.parseDouble(priceField.getText());
                    RentalRequest req = new RentalRequest(
                            UUID.randomUUID().toString(),
                            item.getId(),
                            currentUser.getId(),
                            item.getOwnerId(),
                            startField.getText(),
                            endField.getText(),
                            offered,
                            "PENDING",
                            messageArea.getText()
                    );
                    rentalRepository.saveRequest(req);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Rental request sent successfully!");
                    alert.showAndWait();
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to submit request: " + ex.getMessage());
                    alert.showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
}
