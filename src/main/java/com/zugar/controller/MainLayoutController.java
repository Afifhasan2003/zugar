package com.zugar.controller;

import com.zugar.factory.ViewFactory;
import com.zugar.model.User;
import com.zugar.repository.RentalRepository;
import com.zugar.service.AuthService;
import javafx.fxml.FXML;
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
        ViewFactory.ViewResult res = ViewFactory.loadView("/fxml/catalog.fxml");
        CatalogController controller = res.getController();
        controller.init(rentalRepository, currentUser);
        mainContainer.setCenter(res.getRoot());
        updateNavState(catalogNavBtn);
    }

    @FXML
    public void showAddItem() {
        ViewFactory.ViewResult res = ViewFactory.loadView("/fxml/add_item.fxml");
        AddItemController controller = res.getController();
        controller.init(rentalRepository, currentUser, this::showCatalog);
        mainContainer.setCenter(res.getRoot());
        updateNavState(addItemNavBtn);
    }

    @FXML
    public void showRequests() {
        ViewFactory.ViewResult res = ViewFactory.loadView("/fxml/requests.fxml");
        RequestsController controller = res.getController();
        controller.init(rentalRepository, currentUser);
        mainContainer.setCenter(res.getRoot());
        updateNavState(requestsNavBtn);
    }

    @FXML
    public void showMyItems() {
        ViewFactory.ViewResult res = ViewFactory.loadView("/fxml/my_items.fxml");
        MyItemsController controller = res.getController();
        controller.init(rentalRepository, currentUser);
        mainContainer.setCenter(res.getRoot());
        updateNavState(myItemsNavBtn);
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
