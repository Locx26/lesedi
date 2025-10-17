package com.bankingapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TellerDashboardView {
    private BorderPane view;
    private Button onboardCustomerButton;
    private Button openAccountButton;
    private Button viewCustomersButton;
    private Button logoutButton;
    private Label welcomeLabel;
    private TextArea infoTextArea;

    public TellerDashboardView() {
        createView();
    }

    private void createView() {
        view = new BorderPane();
        view.setPadding(new Insets(20));

        // Top: Welcome section
        HBox topBar = createTopBar();
        view.setTop(topBar);

        // Center: Teller operations
        VBox centerPanel = createOperationsPanel();
        view.setCenter(centerPanel);
    }

    private HBox createTopBar() {
        welcomeLabel = new Label("Bank Teller Dashboard");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        welcomeLabel.setTextFill(Color.DARKBLUE);

        logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getChildren().addAll(welcomeLabel, logoutButton);
        HBox.setHgrow(welcomeLabel, Priority.ALWAYS);

        return topBar;
    }

    private VBox createOperationsPanel() {
        Label operationsLabel = new Label("Teller Operations");
        operationsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Operation buttons
        onboardCustomerButton = new Button("Onboard New Customer");
        onboardCustomerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        onboardCustomerButton.setPrefSize(200, 40);

        openAccountButton = new Button("Open Account");
        openAccountButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        openAccountButton.setPrefSize(200, 40);

        viewCustomersButton = new Button("View Customers");
        viewCustomersButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        viewCustomersButton.setPrefSize(200, 40);

        // Information area
        infoTextArea = new TextArea();
        infoTextArea.setPromptText("System information and customer details will appear here...");
        infoTextArea.setPrefHeight(200);
        infoTextArea.setEditable(false);

        VBox buttonPanel = new VBox(15);
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.getChildren().addAll(
            operationsLabel,
            onboardCustomerButton,
            openAccountButton,
            viewCustomersButton
        );

        VBox operationsPanel = new VBox(20);
        operationsPanel.setAlignment(Pos.CENTER);
        operationsPanel.getChildren().addAll(buttonPanel, infoTextArea);
        operationsPanel.setPadding(new Insets(20));

        return operationsPanel;
    }

    // Getters for controller
    public BorderPane getView() { return view; }
    public Button getOnboardCustomerButton() { return onboardCustomerButton; }
    public Button getOpenAccountButton() { return openAccountButton; }
    public Button getViewCustomersButton() { return viewCustomersButton; }
    public Button getLogoutButton() { return logoutButton; }
    public TextArea getInfoTextArea() { return infoTextArea; }
    public Label getWelcomeLabel() { return welcomeLabel; }

    // View update methods
    public void setWelcomeMessage(String tellerName) {
        welcomeLabel.setText("Bank Teller Dashboard - " + tellerName);
    }

    public void appendInfo(String info) {
        infoTextArea.appendText(info + "\n");
    }

    public void clearInfo() {
        infoTextArea.clear();
    }
}