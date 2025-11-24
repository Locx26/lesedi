module com.bankingapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires java.logging;
    requires java.sql;
    requires java.desktop;
    requires jdk.httpserver;

    exports com.bankingapp;
    exports com.bankingapp.gui;
    exports com.bankingapp.model;
    exports com.bankingapp.util;

    opens com.bankingapp.gui to javafx.fxml;
    opens com.bankingapp to javafx.fxml;
}