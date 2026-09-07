package com.zugar.controller;

import com.zugar.model.RentalItem;
import com.zugar.model.RentalRequest;
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

public class RequestsController {
    @FXML private ListView<RentalRequest> requestsListView;

    private RentalRepository rentalRepository;
    private User currentUser;

    public void init(RentalRepository rentalRepository, User currentUser) {
        this.rentalRepository = rentalRepository;
        this.currentUser = currentUser;

        requestsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(RentalRequest req, boolean empty) {
                super.updateItem(req, empty);
                if (empty || req == null) {
                    setGraphic(null);
                } else {
                    setGraphic(createRequestCard(req));
                }
            }
        });

        loadRequests();
    }

    public void loadRequests() {
        if (currentUser == null) return;
        List<RentalRequest> requests = rentalRepository.findRequestsByOwner(currentUser.getId());
        requestsListView.setItems(FXCollections.observableArrayList(requests));
    }

    private VBox createRequestCard(RentalRequest req) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");

        RentalItem item = rentalRepository.findItemById(req.getItemId());
        String itemTitle = item != null ? item.getTitle() : "Item #" + req.getItemId();

        Label header = new Label("Request for: " + itemTitle);
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label dates = new Label("Dates: " + req.getStartDate() + " to " + req.getEndDate() + " | Offered: ৳" + req.getOfferedPrice());
        dates.setStyle("-fx-text-fill: #475569;");

        Label status = new Label("Status: " + req.getStatus() + " | Message: " + req.getMessage());
        status.setStyle("-fx-text-fill: #64748b;");

        HBox actions = new HBox(10);
        if ("PENDING".equals(req.getStatus())) {
            Button acceptBtn = new Button("Accept");
            acceptBtn.getStyleClass().add("primary-button");
            acceptBtn.setOnAction(e -> {
                rentalRepository.updateRequestStatus(req.getId(), "ACCEPTED");
                loadRequests();
            });

            Button rejectBtn = new Button("Reject");
            rejectBtn.getStyleClass().add("danger-button");
            rejectBtn.setOnAction(e -> {
                rentalRepository.updateRequestStatus(req.getId(), "REJECTED");
                loadRequests();
            });

            actions.getChildren().addAll(acceptBtn, rejectBtn);
        }

        card.getChildren().addAll(header, dates, status, actions);
        return card;
    }
}
