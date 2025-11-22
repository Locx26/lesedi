package com.bankingapp.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Professional Transaction Processing Interface
 * Features deposit, withdrawal, and transaction history
 */
public class TransactionView extends BorderPane {
    private TableView<Transaction> transactionTable;
    private TextField accountField, amountField;
    private Button depositBtn, withdrawBtn, viewHistoryBtn;

    public TransactionView() {
        initializeProfessionalUI();
        loadSampleData();
    }

    private void initializeProfessionalUI() {
        // Header section
        VBox headerSection = createHeaderSection();
       
        // Main content area
        SplitPane mainContent = createMainContent();
       
        // Layout assembly
        this.setTop(headerSection);
        this.setCenter(mainContent);
    }

    private VBox createHeaderSection() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(20));
       
        Label title = new Label("Transaction Processing");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #2c3e50;");
       
        Label subtitle = new Label("Process deposits, withdrawals, and view transaction history");
        subtitle.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");
       
        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private SplitPane createMainContent() {
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.4);
       
        // Left panel - transaction form
        VBox leftPanel = createTransactionPanel();
       
        // Right panel - transaction history
        VBox rightPanel = createHistoryPanel();
       
        splitPane.getItems().addAll(leftPanel, rightPanel);
        return splitPane;
    }

    private VBox createTransactionPanel() {
        VBox transactionPanel = new VBox(20);
        transactionPanel.getStyleClass().add("card");
        transactionPanel.setPadding(new Insets(25));
       
        Label title = new Label("Process Transaction");
        title.getStyleClass().add("card-title");
       
        // Transaction form
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20, 0, 20, 0));
       
        // Account selection
        Label accountLabel = new Label("Account Number:");
        accountLabel.getStyleClass().add("form-label");
       
        accountField = new TextField();
        accountField.setPromptText("Enter account number");
       
        // Amount input
        Label amountLabel = new Label("Amount (BWP):");
        amountLabel.getStyleClass().add("form-label");
       
        amountField = new TextField();
        amountField.setPromptText("Enter amount");
       
        // Transaction type
        Label typeLabel = new Label("Transaction Type:");
        typeLabel.getStyleClass().add("form-label");
       
        ToggleGroup transactionGroup = new ToggleGroup();
        RadioButton depositRadio = new RadioButton("Deposit");
        RadioButton withdrawRadio = new RadioButton("Withdrawal");
        depositRadio.setToggleGroup(transactionGroup);
        withdrawRadio.setToggleGroup(transactionGroup);
        depositRadio.setSelected(true);
       
        HBox radioBox = new HBox(20, depositRadio, withdrawRadio);
       
        // Action buttons
        depositBtn = new Button("💳 Process Deposit");
        withdrawBtn = new Button("💸 Process Withdrawal");
        depositBtn.getStyleClass().add("btn-success");
        withdrawBtn.getStyleClass().add("btn-warning");
       
        HBox buttonBox = new HBox(15, depositBtn, withdrawBtn);
       
        // Result area
        TextArea resultArea = new TextArea();
        resultArea.setPrefHeight(120);
        resultArea.setPromptText("Transaction results will appear here...");
        resultArea.setEditable(false);
       
        // Add components to form
        form.add(accountLabel, 0, 0);
        form.add(accountField, 1, 0);
        form.add(amountLabel, 0, 1);
        form.add(amountField, 1, 1);
        form.add(typeLabel, 0, 2);
        form.add(radioBox, 1, 2);
        form.add(buttonBox, 1, 3);
        form.add(resultArea, 0, 4, 2, 1);
       
        GridPane.setHgrow(accountField, Priority.ALWAYS);
        GridPane.setHgrow(amountField, Priority.ALWAYS);
        GridPane.setHgrow(resultArea, Priority.ALWAYS);
       
        transactionPanel.getChildren().addAll(title, form);
        return transactionPanel;
    }

    private VBox createHistoryPanel() {
        VBox historyPanel = new VBox(15);
        historyPanel.setPadding(new Insets(25));
       
        Label title = new Label("Transaction History");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #2c3e50;");
       
        // Filter controls
        HBox filterPanel = new HBox(15);
        filterPanel.setAlignment(Pos.CENTER_LEFT);
       
        ComboBox<String> periodFilter = new ComboBox<>();
        periodFilter.getItems().addAll("Last 7 days", "Last 30 days", "Last 90 days", "This year");
        periodFilter.setValue("Last 30 days");
       
        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.getItems().addAll("All transactions", "Deposits only", "Withdrawals only");
        typeFilter.setValue("All transactions");
       
        viewHistoryBtn = new Button("🔍 Refresh History");
        viewHistoryBtn.getStyleClass().add("btn-primary");
       
        filterPanel.getChildren().addAll(
            new Label("Period:"), periodFilter,
            new Label("Type:"), typeFilter,
            viewHistoryBtn
        );
       
        // Transaction table
        transactionTable = new TableView<>();
        transactionTable.getStyleClass().add("table-view");
        transactionTable.setPrefHeight(400);
       
        // Define professional columns
        TableColumn<Transaction, String> dateCol = new TableColumn<>("DATE & TIME");
        TableColumn<Transaction, String> accNumCol = new TableColumn<>("ACCOUNT");
        TableColumn<Transaction, String> typeCol = new TableColumn<>("TYPE");
        TableColumn<Transaction, String> amountCol = new TableColumn<>("AMOUNT");
        TableColumn<Transaction, String> balanceCol = new TableColumn<>("BALANCE");
        TableColumn<Transaction, String> statusCol = new TableColumn<>("STATUS");
       
        transactionTable.getColumns().addAll(dateCol, accNumCol, typeCol, amountCol, balanceCol, statusCol);
       
        historyPanel.getChildren().addAll(title, filterPanel, transactionTable);
        VBox.setVgrow(transactionTable, Priority.ALWAYS);
       
        return historyPanel;
    }

    private void loadSampleData() {
        // Add sample transaction data
        transactionTable.getItems().addAll(
            new Transaction("2024-03-20 14:30", "SAV001", "Deposit", "BWP 500.00", "BWP 1,500.00", "Completed"),
            new Transaction("2024-03-19 10:15", "INV001", "Withdrawal", "BWP 1,000.00", "BWP 5,000.00", "Completed"),
            new Transaction("2024-03-18 16:45", "CHQ001", "Deposit", "BWP 2,500.00", "BWP 2,500.00", "Completed"),
            new Transaction("2024-03-17 09:20", "SAV002", "Interest", "BWP 12.50", "BWP 812.50", "Completed"),
            new Transaction("2024-03-16 11:30", "INV002", "Deposit", "BWP 3,000.00", "BWP 7,500.00", "Completed")
        );
    }

    // Inner Transaction class for table data
    public static class Transaction {
        private final String dateTime;
        private final String accountNumber;
        private final String type;
        private final String amount;
        private final String balance;
        private final String status;

        public Transaction(String dateTime, String accountNumber, String type, String amount, String balance, String status) {
            this.dateTime = dateTime;
            this.accountNumber = accountNumber;
            this.type = type;
            this.amount = amount;
            this.balance = balance;
            this.status = status;
        }

        public String getDateTime() { return dateTime; }
        public String getAccountNumber() { return accountNumber; }
        public String getType() { return type; }
        public String getAmount() { return amount; }
        public String getBalance() { return balance; }
        public String getStatus() { return status; }
    }
}