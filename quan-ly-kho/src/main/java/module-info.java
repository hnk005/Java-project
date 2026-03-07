module com.example.quanlykho {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.quanlykho to javafx.fxml;
    exports com.example.quanlykho;
    exports com.example.quanlykho.app;
    opens com.example.quanlykho.app to javafx.fxml;
    exports com.example.quanlykho.controller;
    opens com.example.quanlykho.controller to javafx.fxml;
}