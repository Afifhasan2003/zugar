package com.zugar.controller;

import com.zugar.command.AcceptRequestCommand;
import com.zugar.command.CounterOfferCommand;
import com.zugar.command.RejectRequestCommand;
import com.zugar.model.RentalItem;
import com.zugar.model.RentalRequest;
import com.zugar.model.User;
import com.zugar.repository.RentalRepository;
import com.zugar.state.RentalRequestState;
import com.zugar.state.RentalRequestStateFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.List;

public class RequestsController {
    @FXML private ListView<RentalRequest> incomingListView;
    @FXML private ListView<RentalRequest> sentListView;

    private RentalRepository rentalRepository;
    private User currentUser;

    public void init(RentalRepository rentalRepository, User currentUser) {
        this.rentalRepository = rentalRepository;
        this.currentUser = currentUser;

        incomingListView.setCellFactory(new Callback<ListView<RentalRequest>, ListCell<RentalRequest>>() {
            @Override
            public ListCell<RentalRequest> call(ListView<RentalRequest> view) {
                return new ListCell<RentalRequest>() {
                    @Override
                    protected void updateItem(RentalRequest req, boolean empty) {
                        super.updateItem(req, empty);
                        if (empty || req == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(createIncomingRequestCard(req));
                        }
                    }
                };
            }
        });

        sentListView.setCellFactory(new Callback<ListView<RentalRequest>, ListCell<RentalRequest>>() {
            @Override
            public ListCell<RentalRequest> call(ListView<RentalRequest> view) {
                return new ListCell<RentalRequest>() {
                    @Override
                    protected void updateItem(RentalRequest req, boolean empty) {
                        super.updateItem(req, empty);
                        if (empty || req == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(createSentRequestCard(req));
                        }
                    }
                };
            }
        });

        loadRequests();
    }

    public void loadRequests() {
        if (currentUser == null) return;

        List<RentalRequest> incoming = rentalRepository.findRequestsByOwner(currentUser.getId());
        incomingListView.setItems(FXCollections.observableArrayList(incoming));

        List<RentalRequest> sent = rentalRepository.findRequestsByRenter(currentUser.getId());
        sentListView.setItems(FXCollections.observableArrayList(sent));
    }

    private VBox createIncomingRequestCard(RentalRequest req) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");

        RentalItem item = rentalRepository.findItemById(req.getItemId());
        String itemTitle = item != null ? item.getTitle() : "Item #" + req.getItemId();

        Label header = new Label("Request for: " + itemTitle);
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        RentalRequestState state = RentalRequestStateFactory.fromStatus(req.getStatus());
        Label dates = new Label("Dates: " + req.getStartDate() + " to " + req.getEndDate() + " | Offered: ৳" + req.getOfferedPrice());
        dates.setStyle("-fx-text-fill: #475569;");

        Label status = new Label("Status: " + state.getDisplayName() + " | Message: " + req.getMessage());
        status.setStyle("-fx-text-fill: #64748b;");

        HBox actions = new HBox(10);
        if (req.getCounterPrice() != null) {
            actions.getChildren().add(new Label("Current counter-offer: ৳" + req.getCounterPrice()));
        }
        if (state.canAccept()) {
            Button acceptBtn = new Button("Accept");
            acceptBtn.getStyleClass().add("primary-button");
            acceptBtn.setOnAction(e -> {
                new AcceptRequestCommand(rentalRepository, req).execute();
                loadRequests();
            });

            actions.getChildren().add(acceptBtn);
        }
        if (state.canReject()) {
            Button rejectBtn = new Button("Reject");
            rejectBtn.getStyleClass().add("danger-button");
            rejectBtn.setOnAction(e -> {
                new RejectRequestCommand(rentalRepository, req).execute();
                loadRequests();
            });
            actions.getChildren().add(rejectBtn);
        }
        if (state.canCounter()) {
            addCounterOfferControls(actions, req);
        }

        card.getChildren().addAll(header, dates, status, actions);
        return card;
    }

    private VBox createSentRequestCard(RentalRequest req) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");

        RentalItem item = rentalRepository.findItemById(req.getItemId());
        String itemTitle = item != null ? item.getTitle() : "Item #" + req.getItemId();

        Label header = new Label("Sent Request: " + itemTitle);
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        RentalRequestState state = RentalRequestStateFactory.fromStatus(req.getStatus());
        Label dates = new Label("Dates: " + req.getStartDate() + " to " + req.getEndDate() + " | Offered: ৳" + req.getOfferedPrice());
        dates.setStyle("-fx-text-fill: #475569;");

        Label badge = new Label("Status: " + state.getDisplayName());
        if ("ACCEPTED".equals(req.getStatus())) {
            badge.getStyleClass().add("badge-available");
        } else {
            badge.getStyleClass().add("badge-restricted");
        }

        Label msg = new Label("My Message: " + req.getMessage());
        msg.setStyle("-fx-text-fill: #64748b;");

        card.getChildren().addAll(header, dates, badge, msg);
        if (req.getCounterPrice() != null) {
            card.getChildren().add(new Label("Owner counter-offer: ৳" + req.getCounterPrice()));
        }
        if (state.canCounter() && req.getCounterPrice() != null) {
            HBox actions = new HBox(10);
            if (state.canAccept()) {
                Button acceptButton = new Button("Accept counter-offer");
                acceptButton.getStyleClass().add("primary-button");
                acceptButton.setOnAction(e -> {
                    new AcceptRequestCommand(rentalRepository, req).execute();
                    loadRequests();
                });
                actions.getChildren().add(acceptButton);
            }
            addCounterOfferControls(actions, req);
            card.getChildren().add(actions);
        }
        return card;
    }

    private void addCounterOfferControls(HBox actions, RentalRequest req) {
        TextField counterPriceField = new TextField();
        counterPriceField.setPromptText("Counter price");
        counterPriceField.setPrefWidth(120);

        Button counterButton = new Button("Counter");
        counterButton.setOnAction(e -> {
            try {
                double counterPrice = Double.parseDouble(counterPriceField.getText().trim());
                if (counterPrice <= 0) {
                    return;
                }
                new CounterOfferCommand(rentalRepository, req, counterPrice).execute();
                loadRequests();
            } catch (NumberFormatException ignored) {
                counterPriceField.setPromptText("Enter a valid price");
            }
        });
        actions.getChildren().addAll(counterPriceField, counterButton);
    }
}
