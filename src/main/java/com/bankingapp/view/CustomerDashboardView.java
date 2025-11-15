package com.bankingapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Customer Dashboard View - Boundary Class for Customer Banking Operations
 * Pure UI implementation with no business logic
 */
public class CustomerDashboardView {
    private BorderPane view;
    private ListView<String> accountsList;
    private Button depositButton;
    private Button withdrawButton;
    private Button transferButton;
    private Button viewTransactionsButton;
    private Button logoutButton;
    private Button refreshButton;
    private Label welcomeLabel;
    private Label balanceLabel;
    private Label accountInfoLabel;
    private TextField amountField;
    private TextArea transactionArea;
    private ProgressIndicator loadingIndicator;

    public CustomerDashboardView() {
        createView();
    }

    private void createView() {
        view = new BorderPane();
        view.setPadding(new Insets(20));
        view.getStyleClass().add("root");

        // Create all sections
        view.setTop(createTopBar());
        view.setLeft(createAccountsPanel());
        view.setCenter(createOperationsPanel());
        view.setBottom(createStatusBar());
    }

    private HBox createTopBar() {
        // Welcome section
        VBox welcomeSection = new VBox(5);
        welcomeLabel = new Label("Welcome, Customer!");
        welcomeLabel.getStyleClass().add("header-primary");
       
        accountInfoLabel = new Label("Select an account to begin");
        accountInfoLabel.getStyleClass().add("body-text");
       
        welcomeSection.getChildren().addAll(welcomeLabel, accountInfoLabel);

        // Action buttons
        refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("secondary-button");
       
        logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("danger-button");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(refreshButton, logoutButton);

        // Top bar layout
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        HBox.setHgrow(welcomeSection, Priority.ALWAYS);
        topBar.getChildren().addAll(welcomeSection, buttonBox);

        return topBar;
    }

    private VBox createAccountsPanel() {
        Label accountsLabel = new Label("Your Accounts");
        accountsLabel.getStyleClass().add("header-secondary");

        accountsList = new ListView<>();
        accountsList.setPrefWidth(300);
        accountsList.setPrefHeight(400);
        accountsList.getStyleClass().add("transaction-list");

        // Sample accounts for UI demonstration
        accountsList.getItems().addAll(
            "💰 Savings Account ••••1234 • $5,250.75",
            "💳 Checking Account ••••5678 • $2,150.30",
            "🏢 Business Account ••••9012 • $12,500.00"
        );

        VBox accountsPanel = new VBox(15);
        accountsPanel.setPadding(new Insets(20));
        accountsPanel.getStyleClass().add("card");
        accountsPanel.getChildren().addAll(accountsLabel, accountsList);
        accountsPanel.setPrefWidth(340);

        return accountsPanel;
    }

    private VBox createOperationsPanel() {
        VBox operationsPanel = new VBox(20);
        operationsPanel.setPadding(new Insets(20));
        operationsPanel.getStyleClass().add("card");

        // Balance Display
        VBox balanceSection = createBalanceSection();
       
        // Quick Actions
        VBox actionsSection = createActionsSection();
       
        // Transactions
        VBox transactionsSection = createTransactionsSection();

        operationsPanel.getChildren().addAll(balanceSection, actionsSection, transactionsSection);
        return operationsPanel;
    }

    private VBox createBalanceSection() {
        Label balanceTitle = new Label("Account Balance");
        balanceTitle.getStyleClass().add("header-secondary");

        balanceLabel = new Label("$0.00");
        balanceLabel.getStyleClass().add("balance-display");

        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(false);
        loadingIndicator.setPrefSize(30, 30);

        HBox balanceDisplay = new HBox(15);
        balanceDisplay.setAlignment(Pos.CENTER_LEFT);
        balanceDisplay.getChildren().addAll(balanceLabel, loadingIndicator);

        VBox balanceSection = new VBox(10);
        balanceSection.getChildren().addAll(balanceTitle, balanceDisplay);
       
        return balanceSection;
    }

    private VBox createActionsSection() {
        Label actionsTitle = new Label("Quick Actions");
        actionsTitle.getStyleClass().add("header-tertiary");

        // Amount input
        HBox amountSection = new HBox(10);
        amountSection.setAlignment(Pos.CENTER_LEFT);
       
        Label amountLabel = new Label("Amount:");
        amountLabel.getStyleClass().add("body-text");
       
        amountField = new TextField();
        amountField.setPromptText("0.00");
        amountField.setPrefWidth(150);
        amountField.getStyleClass().add("text-field");
       
        amountSection.getChildren().addAll(amountLabel, amountField);

        // Action buttons
        HBox buttonRow1 = new HBox(15);
        buttonRow1.setAlignment(Pos.CENTER_LEFT);
       
        depositButton = new Button("Deposit");
        depositButton.getStyleClass().add("success-button");
        depositButton.setPrefWidth(120);
       
        withdrawButton = new Button("Withdraw");
        withdrawButton.getStyleClass().add("warning-button");
        withdrawButton.setPrefWidth(120);
       
        transferButton = new Button("Transfer");
        transferButton.getStyleClass().add("primary-button");
        transferButton.setPrefWidth(120);
       
        buttonRow1.getChildren().addAll(depositButton, withdrawButton, transferButton);

        VBox actionsSection = new VBox(15);
        actionsSection.getChildren().addAll(actionsTitle, amountSection, buttonRow1);
       
        return actionsSection;
    }

    private VBox createTransactionsSection() {
        Label transactionsTitle = new Label("Recent Transactions");
        transactionsTitle.getStyleClass().add("header-tertiary");

        transactionArea = new TextArea();
        transactionArea.setPrefHeight(200);
        transactionArea.setEditable(false);
        transactionArea.setWrapText(true);
        transactionArea.getStyleClass().add("text-field");

        // Sample transactions for UI demonstration
        transactionArea.setText(
            "2024-01-15 • DEPOSIT • +$500.00 • Balance: $5,250.75\n" +
            "2024-01-14 • WITHDRAWAL • -$200.00 • Balance: $4,750.75\n" +
            "2024-01-12 • TRANSFER • -$100.00 • Balance: $4,950.75\n" +
            "2024-01-10 • INTEREST • +$0.75 • Balance: $5,050.75\n" +
            "2024-01-08 • DEPOSIT • +$1,000.00 • Balance: $5,050.00"
        );

        viewTransactionsButton = new Button("View All Transactions");
        viewTransactionsButton.getStyleClass().add("secondary-button");

        VBox transactionsSection = new VBox(10);
        transactionsSection.getChildren().addAll(transactionsTitle, transactionArea, viewTransactionsButton);
       
        return transactionsSection;
    }

    private HBox createStatusBar() {
        Label statusLabel = new Label("Ready • Secure connection established");
        statusLabel.getStyleClass().add("body-text");

        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(10, 0, 0, 0));
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.getChildren().add(statusLabel);
       
        return statusBar;
    }

    // ===== PUBLIC API FOR CONTROLLERS =====
   
    public BorderPane getView() { return view; }
    public ListView<String> getAccountsList() { return accountsList; }
    public Button getDepositButton() { return depositButton; }
    public Button getWithdrawButton() { return withdrawButton; }
    public Button getTransferButton() { return transferButton; }
    public Button getViewTransactionsButton() { return viewTransactionsButton; }
    public Button getLogoutButton() { return logoutButton; }
    public Button getRefreshButton() { return refreshButton; }
    public TextField getAmountField() { return amountField; }
    public TextArea getTransactionArea() { return transactionArea; }
    public Label getWelcomeLabel() { return welcomeLabel; }
    public Label getBalanceLabel() { return balanceLabel; }
    public Label getAccountInfoLabel() { return accountInfoLabel; }
    public ProgressIndicator getLoadingIndicator() { return loadingIndicator; }

    // ===== VIEW UPDATE METHODS (No Business Logic) =====
   
    public void setWelcomeMessage(String customerName) {
        welcomeLabel.setText("Welcome, " + customerName + "!");
    }

    public void setBalance(String balance) {
        balanceLabel.setText(balance);
    }

    public void setAccountInfo(String info) {
        accountInfoLabel.setText(info);
    }

    public void clearAmountField() {
        amountField.clear();
    }

    public void setLoading(boolean loading) {
        loadingIndicator.setVisible(loading);
        if (loading) {
            balanceLabel.setText("Updating...");
        }
    }

    public void showAccountSelectionWarning() {
        setAccountInfo("⚠️ Please select an account first!");
        balanceLabel.setText("$0.00");
    }

    public boolean validateAmount() {
        try {
            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                showTransactionMessage("Please enter an amount", "error");
                return false;
            }
           
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showTransactionMessage("Amount must be positive", "error");
                return false;
            }
           
            return true;
        } catch (NumberFormatException e) {
            showTransactionMessage("Please enter a valid amount", "error");
            return false;
        }
    }

    public void showTransactionMessage(String message, String type) {
        switch (type.toLowerCase()) {
            case "success":
                setAccountInfo("✅ " + message);
                break;
            case "error":
                setAccountInfo("❌ " + message);
                break;
            case "warning":
                setAccountInfo("⚠️ " + message);
                break;
            default:
                setAccountInfo(message);
        }
    }

    public void updateTransactionHistory(String transactions) {
        transactionArea.setText(transactions);
    }

    public void clearSelection() {
        accountsList.getSelectionModel().clearSelection();
        setBalance("$0.00");
        setAccountInfo("Select an account to view details");
        clearAmountField();
    }
}