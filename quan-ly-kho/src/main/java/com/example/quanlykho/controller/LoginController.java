package com.example.quanlykho.controller;

import com.example.quanlykho.Launcher;
import com.example.quanlykho.bus.AccountBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    private final AccountBus accountBus;

    public LoginController() {
        accountBus = new AccountBus();
    }

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label statusLabel;

    @FXML
    protected void handleLogin(ActionEvent event) throws SQLException, IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (accountBus.login(username, password)) {
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("views/dashboard-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Quản Lý Kho");
            stage.setWidth(1200);
            stage.setHeight(750);
            stage.centerOnScreen();
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Tài khoản hoặc mật khẩu sai, hoặc tài khoản đã bị khóa.");
            alert.showAndWait();
        }
    }
}

