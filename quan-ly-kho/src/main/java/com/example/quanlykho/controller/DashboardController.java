package com.example.quanlykho.controller;

import com.example.quanlykho.Launcher;
import com.example.quanlykho.model.account.Account;
import com.example.quanlykho.model.account.Role;
import com.example.quanlykho.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private BorderPane mainPane;
    @FXML
    private StackPane contentArea;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userRoleLabel;
    @FXML
    private Button btnAccounts;

    @FXML
    private Button btnOverview;
    @FXML
    private Button btnProducts;
    @FXML
    private Button btnCategories;
    @FXML
    private Button btnSuppliers;
    @FXML
    private Button btnImports;
    @FXML
    private Button btnExports;

    private Button activeButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Account current = SessionManager.getInstance().getCurrentAccount();
        if (current != null) {
            userNameLabel.setText(current.getFullName());
            userRoleLabel.setText(current.getRole() == Role.ADMIN ? "Quản trị viên" : "Nhân viên");
            if (current.getRole() != Role.ADMIN) {
                btnAccounts.setVisible(false);
                btnAccounts.setManaged(false);
            }
        }
        loadView("views/overview-view.fxml", btnOverview);
    }

    public void loadView(String fxmlPath, Button sourceButton) {
        try {
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
            if (activeButton != null) {
                activeButton.getStyleClass().remove("sidebar-btn-active");
            }
            if (sourceButton != null) {
                sourceButton.getStyleClass().add("sidebar-btn-active");
                activeButton = sourceButton;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onOverview() {
        loadView("views/overview-view.fxml", btnOverview);
    }

    @FXML
    private void onProducts() {
        loadView("views/product-view.fxml", btnProducts);
    }

    @FXML
    private void onCategories() {
        loadView("views/category-view.fxml", btnCategories);
    }

    @FXML
    private void onSuppliers() {
        loadView("views/supplier-view.fxml", btnSuppliers);
    }

    @FXML
    private void onImports() {
        loadView("views/import-view.fxml", btnImports);
    }

    @FXML
    private void onExports() {
        loadView("views/export-view.fxml", btnExports);
    }

    @FXML
    private void onAccounts() {
        loadView("views/account-view.fxml", btnAccounts);
    }

    @FXML
    private void onLogout(ActionEvent event) throws IOException {
        SessionManager.getInstance().logout();
        FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("views/login-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Đăng nhập");
        stage.setWidth(400);
        stage.setHeight(450);
        stage.centerOnScreen();
        stage.show();
    }
}

