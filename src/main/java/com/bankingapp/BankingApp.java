package com.bankingapp;

import com.bankingapp.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankingApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create the login view (boundary class)
        LoginView loginView = new LoginView();

        // Create scene and setup stage
        Scene scene = new Scene(loginView.getView(), 400, 300);
        primaryStage.setTitle("Banking System - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}