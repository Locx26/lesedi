module com.bankingapp {
    // JavaFX modules used by the application
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    // JDK modules
    requires java.logging;
    requires java.sql;

    // Export packages consumed by other modules / for tests or external usage
    exports com.bankingapp;
    exports com.bankingapp.gui;
    exports com.bankingapp.model;
    exports com.bankingapp.util;

    // Allow FXMLLoader reflective access to GUI/controller packages
    opens com.bankingapp.gui to javafx.fxml;
    opens com.bankingapp to javafx.fxml;

    // If you use reflection against model classes from FXML or frameworks, open them as needed:
    // opens com.bankingapp.model to javafx.base;
}