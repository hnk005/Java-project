package com.example.quanlykho.app;

import com.example.quanlykho.Launcher;
import com.example.quanlykho.util.DatabaseConnection;
import com.example.quanlykho.util.JavaFXUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        DatabaseConnection.getConnection();
        Scene scene = JavaFXUtil.createScene("views/login-view.fxml");
        stage.setTitle("Đăng nhập");
        stage.setScene(scene);
        stage.show();

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println("Lỗi chưa được xử lý trên thread: " + thread.getName());
            throwable.printStackTrace();

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Đã có lỗi xảy ra");
                alert.showAndWait();
            });
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DatabaseConnection.closeConnection();
    }
}
