module com.example.quanlykho {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.quanlykho to javafx.fxml;
    exports com.example.quanlykho;

    exports com.example.quanlykho.app;
    opens com.example.quanlykho.app to javafx.fxml;

    exports com.example.quanlykho.controller;
    opens com.example.quanlykho.controller to javafx.fxml;

    exports com.example.quanlykho.bus;
    exports com.example.quanlykho.dao;
    exports com.example.quanlykho.util;

    exports com.example.quanlykho.model.account;
    opens com.example.quanlykho.model.account to javafx.fxml;

    exports com.example.quanlykho.model.category;
    opens com.example.quanlykho.model.category to javafx.fxml;

    exports com.example.quanlykho.model.supplier;
    opens com.example.quanlykho.model.supplier to javafx.fxml;

    exports com.example.quanlykho.model.product;
    opens com.example.quanlykho.model.product to javafx.fxml;

    exports com.example.quanlykho.model.ticket;
    opens com.example.quanlykho.model.ticket to javafx.fxml;

    exports com.example.quanlykho.model.dashboard;
    opens com.example.quanlykho.model.dashboard to javafx.fxml;
}

