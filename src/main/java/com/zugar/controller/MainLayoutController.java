package com.zugar.controller;

import com.zugar.model.User;
import com.zugar.repository.RentalRepository;
import com.zugar.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MainLayoutController {
    @FXML private BorderPane mainContainer;
    @FXML private Label userLabel;
    @FXML private Button catalogNavBtn;
    @FXML private Button addItemNavBtn;
    @FXML private Button requestsNavBtn;
    @FXML private Button myItemsNavBtn;

    private AuthService authService;
    private RentalRepository rentalRepository;
    private User currentUser;
    private Runnable onLogout;

    public void init(AuthService authService, RentalRepository rentalRepository, User user, Runnable onLogout) {
        this.authService = authService;
        this.rentalRepository = rentalRepository;
        this.currentUser = user;
        this.onLogout = onLogout;

        userLabel.setText("User: " + user.getUsername());
        showCatalog();
    }

    @FXML
    public void showCatalog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/catalog.fxml"));
            Node view = loader.load();
            CatalogController controller = loader.getController();
            controller.init(rentalRepository, currentUser);
            mainContainer.setCenter(view);
            updateNavState(catalogNavBtn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showAddItem() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_item.fxml"));
            Node view = loader.load();
            AddItemController controller = loader.getController();
            controller.init(rentalRepository, currentUser, this::showCatalog);
            mainContainer.setCenter(view);
            updateNavState(addItemNavBtn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showRequests() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/requests.fxml"));
            Node view = loader.load();
            RequestsController controller = loader.getController();
            controller.init(rentalRepository, currentUser);
            mainContainer.setCenter(view);
            updateNavState(requestsNavBtn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showMyItems() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/my_items.fxml"));
            Node view = loader.load();
            MyItemsController controller = loader.getController();
            controller.init(rentalRepository, currentUser);
            mainContainer.setCenter(view);
            updateNavState(myItemsNavBtn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        if (onLogout != null) {
            onLogout.run();
        }
    }

    private void updateNavState(Button activeBtn) {
        catalogNavBtn.getStyleClass().remove("nav-button-active");
        addItemNavBtn.getStyleClass().remove("nav-button-active");
        requestsNavBtn.getStyleClass().remove("nav-button-active");
        myItemsNavBtn.getStyleClass().remove("nav-button-active");
        if (activeBtn != null && !activeBtn.getStyleClass().contains("nav-button-active")) {
            activeBtn.getStyleClass().add("nav-button-active");
        }
    }
}
