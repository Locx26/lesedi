package com.bankingapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CustomerDashboardView {
    private BorderPane view;
    private ListView<String> accountsList;
    private Button depositButton;
    private Button withdrawButton;
    private Button viewTransactionsButton;
    private Button logoutButton;
    private Label welcomeLabel;
    private Label balanceLabel;
    private TextField amountField;

    public CustomerDashboardView() {
        createView();
    }

    private void createView() {
        view = new BorderPane();
        view.setPadding(new Insets(20));

        // Top: Welcome section
        HBox topBar = createTopBar();
        view.setTop(topBar);

        // Left: Accounts list
        VBox leftPanel = createAccountsPanel();
        view.setLeft(leftPanel);

        // Center: Operations
        VBox centerPanel = createOperationsPanel();
        view.setCenter(centerPanel);
    }

    private HBox createTopBar() {
        welcomeLabel = new Label("Welcome, Customer!");
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

    private VBox createAccountsPanel() {
        Label accountsLabel = new Label("Your Accounts");
        accountsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        accountsList = new ListView<>();
        accountsList.setPrefWidth(250);
        accountsList.setPrefHeight(300);

        VBox accountsPanel = new VBox(10);
        accountsPanel.getChildren().addAll(accountsLabel, accountsList);
        accountsPanel.setPadding(new Insets(10));
        accountsPanel.setStyle("-fx-background-color: #e8e8e8; -fx-border-color: #cccccc;");

        return accountsPanel;
    }

    private VBox createOperationsPanel() {
        // Balance display
        balanceLabel = new Label("Select an account to view balance");
        balanceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        balanceLabel.setTextFill(Color.DARKGREEN);

        // Amount input
        Label amountLabel = new Label("Amount:");
        amountField = new TextField();
        amountField.setPromptText("Enter amount");
        amountField.setMaxWidth(200);

        // Buttons
        depositButton = new Button("Deposit");
        depositButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        depositButton.setPrefWidth(120);

        withdrawButton = new Button("Withdraw");
        withdrawButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        withdrawButton.setPrefWidth(120);

        viewTransactionsButton = new Button("View Transactions");
        viewTransactionsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        viewTransactionsButton.setPrefWidth(120);

        // Button layout
        HBox buttonRow1 = new HBox(10);
        buttonRow1.setAlignment(Pos.CENTER);
        buttonRow1.getChildren().addAll(depositButton, withdrawButton);

        HBox buttonRow2 = new HBox(10);
        buttonRow2.setAlignment(Pos.CENTER);
        buttonRow2.getChildren().addAll(viewTransactionsButton);

        VBox operationsPanel = new VBox(15);
        operationsPanel.setAlignment(Pos.CENTER);
        operationsPanel.getChildren().addAll(
            balanceLabel,
            new Separator(),
            amountLabel,
            amountField,
            buttonRow1,
            buttonRow2
        );

        operationsPanel.setPadding(new Insets(20));
        operationsPanel.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #cccccc;");

        return operationsPanel;
    }

    // Getters for controller
    public BorderPane getView() { return view; }
    public ListView<String> getAccountsList() { return accountsList; }
    public Button getDepositButton() { return depositButton; }
    public Button getWithdrawButton() { return withdrawButton; }
    public Button getViewTransactionsButton() { return viewTransactionsButton; }
    public Button getLogoutButton() { return logoutButton; }
    public TextField getAmountField() { return amountField; }
    public Label getWelcomeLabel() { return welcomeLabel; }
    public Label getBalanceLabel() { return balanceLabel; }

    // View update methods (no business logic!)
    public void setWelcomeMessage(String customerName) {
        welcomeLabel.setText("Welcome, " + customerName + "!");
    }

    public void setBalance(String balanceInfo) {
        balanceLabel.setText(balanceInfo);
    }

    public void clearAmountField() {
        amountField.clear();
    }

    public void showAccountSelectionWarning() {
        balanceLabel.setText("Please select an account first!");
        balanceLabel.setTextFill(Color.RED);
    }
}
