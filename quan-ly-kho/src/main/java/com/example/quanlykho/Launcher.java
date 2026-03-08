package com.example.quanlykho;

import com.example.quanlykho.app.MainApplication;
import com.example.quanlykho.util.DatabaseConnection;
import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        DatabaseConnection.getConnection();
        Application.launch(MainApplication.class, args);
    }
}
