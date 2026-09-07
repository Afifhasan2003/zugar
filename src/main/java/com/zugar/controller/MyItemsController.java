package com.zugar.controller;

import com.zugar.model.RentalItem;
import com.zugar.model.User;
import com.zugar.repository.RentalRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class MyItemsController {
    @FXML private ListView<RentalItem> myItemsListView;

    private RentalRepository rentalRepository;
    private User currentUser;

    public void init(RentalRepository rentalRepository, User currentUser) {
        this.rentalRepository = rentalRepository;
        this.currentUser = currentUser;

        myItemsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(RentalItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(createMyItemCard(item));
                }
            }
        });

        loadMyItems();
    }

    public void loadMyItems() {
        if (currentUser == null) return;
        List<RentalItem> items = rentalRepository.findItemsByOwner(currentUser.getId());
        myItemsListView.setItems(FXCollections.observableArrayList(items));
    }

    private VBox createMyItemCard(RentalItem item) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");

        HBox header = new HBox(10);
        Label title = new Label(item.getTitle());
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label badge = new Label(item.isAvailable() ? "Available" : "Rented / Unavailable");
        badge.getStyleClass().add(item.isAvailable() ? "badge-available" : "badge-restricted");
        header.getChildren().addAll(title, badge);

        Label details = new Label("Price: ৳" + item.getPricePerDay() + "/day | Deposit: ৳" + item.getDeposit() + " | Category: " + item.getCategory());
        details.setStyle("-fx-text-fill: #475569;");

        HBox actions = new HBox(10);
        Button toggleBtn = new Button(item.isAvailable() ? "Mark Rented" : "Mark Available");
        toggleBtn.getStyleClass().add("secondary-button");
        toggleBtn.setOnAction(e -> {
            item.setAvailable(!item.isAvailable());
            rentalRepository.updateItem(item);
            loadMyItems();
        });

        Button deleteBtn = new Button("Delete Listing");
        deleteBtn.getStyleClass().add("danger-button");
        deleteBtn.setOnAction(e -> {
            rentalRepository.deleteItem(item.getId());
            loadMyItems();
        });

        actions.getChildren().addAll(toggleBtn, deleteBtn);
        card.getChildren().addAll(header, details, actions);
        return card;
    }
}
