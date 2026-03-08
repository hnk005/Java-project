package com.example.quanlykho.controller;

import com.example.quanlykho.bus.AccountBus;
import com.example.quanlykho.util.JavaFXUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (accountBus.login(username, password)) {
//            Scene scene = JavaFXUtil.createScene("views/dashboard-view.fxml");
//            Stage stage = JavaFXUtil.createStage(event);
//
//            stage.setTitle("Hello");
//            stage.setScene(scene);
//            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Tài khoản hoặc mật khẩu sai");
            alert.showAndWait();
        }

    }
}