package com.bankingapp;

import com.bankingapp.view.LoginView;
import com.bankingapp.controller.LoginController;
import com.bankingapp.service.UserService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main Banking Application Class
 * Initializes and launches the JavaFX banking application with MVC pattern
 */
public class BankingApp extends Application {

    private Stage primaryStage;
    private UserService userService;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userService = new UserService();
        initializeApplication();
    }

    private void initializeApplication() {
        try {
            // Create the login view (boundary class)
            LoginView loginView = new LoginView();
           
            // Create the login controller
            LoginController loginController = new LoginController(loginView, userService, primaryStage);

            // Create scene with CSS styling
            Scene scene = new Scene(loginView.getView(), 900, 650);
            scene.getStylesheets().add(getClass().getResource("/com/bankingapp/styles/banking.css").toExternalForm());

            // Configure primary stage
            primaryStage.setTitle("SecureTrust Banking System");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
           
            // Set application icon
            try {
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/bankingapp/images/bank-icon.png")));
            } catch (Exception e) {
                System.out.println("Icon not found, using default");
            }

            primaryStage.show();
           
            // Center window on screen
            primaryStage.centerOnScreen();

            System.out.println("Banking Application started successfully with MVC architecture");

        } catch (Exception e) {
            showErrorDialog("Application Initialization Error",
                "Failed to initialize banking application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showErrorDialog(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public UserService getUserService() {
        return userService;
    }

    public static void main(String[] args) {
        System.out.println("Starting SecureTrust Banking System...");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("JavaFX Version: " + System.getProperty("javafx.version"));
        System.out.println("Architecture: MVC Pattern with layered services");
       
        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("Application launch failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 