package com.example.quanlykho.util;

import com.example.quanlykho.Launcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class JavaFXUtil {
    public static Stage createStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static Scene createScene(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(Launcher.class.getResource(path));
        return new Scene(loader.load());
    }
}