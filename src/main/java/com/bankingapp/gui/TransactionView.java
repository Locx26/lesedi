package com.bankingapp.gui;

import com.bankingapp.controller.BankingController;
import com.bankingapp.model.Account;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Professional Transaction Processing Interface
 * Features deposit, withdrawal, and transaction history
 *
 * This view can be constructed with a BankingController so buttons call:
 *  - controller.processDeposit(accountNumber, amount)
 *  - controller.processWithdrawal(accountNumber, amount)
 * and it will update the table with a simple transaction row after success.
 *
 * Note: For a full transaction audit/history, add a controller method such as
 * List<TransactionRecord> getTransactionHistory(String accountNumber) that delegates
 * to AccountDAO.getTransactionHistory(...) — I can add that method if you want.
 */
public class TransactionView extends BorderPane {
    private TableView<TransactionRow> transactionTable;
    private TextField accountField, amountField;
    private Button depositBtn, withdrawBtn, viewHistoryBtn;
    private TextArea resultArea;

    private final BankingController controller;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TransactionView() {
        this(new BankingController());
    }

    /**
     * Construct with an existing BankingController (preferred so the view uses the same service layer)
     */
    public TransactionView(BankingController controller) {
        this.controller = controller != null ? controller : new BankingController();
        initializeProfessionalUI();
        loadSampleData();
        wireActions();
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
        resultArea = new TextArea();
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

        // Define professional columns and bind to TransactionRow properties
        TableColumn<TransactionRow, String> dateCol = new TableColumn<>("DATE & TIME");
        TableColumn<TransactionRow, String> accNumCol = new TableColumn<>("ACCOUNT");
        TableColumn<TransactionRow, String> typeCol = new TableColumn<>("TYPE");
        TableColumn<TransactionRow, String> amountCol = new TableColumn<>("AMOUNT");
        TableColumn<TransactionRow, String> balanceCol = new TableColumn<>("BALANCE");
        TableColumn<TransactionRow, String> statusCol = new TableColumn<>("STATUS");

        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        accNumCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Optional sizing
        dateCol.setPrefWidth(160);
        accNumCol.setPrefWidth(120);
        typeCol.setPrefWidth(110);
        amountCol.setPrefWidth(110);
        balanceCol.setPrefWidth(120);
        statusCol.setPrefWidth(100);

        transactionTable.getColumns().addAll(dateCol, accNumCol, typeCol, amountCol, balanceCol, statusCol);

        historyPanel.getChildren().addAll(title, filterPanel, transactionTable);
        VBox.setVgrow(transactionTable, Priority.ALWAYS);

        return historyPanel;
    }

    private void loadSampleData() {
        // Add sample transaction data (keeps UI useful even if controller can't supply history)
        transactionTable.getItems().addAll(
            new TransactionRow("2024-03-20 14:30", "SAV001", "Deposit", "BWP 500.00", "BWP 1,500.00", "Completed"),
            new TransactionRow("2024-03-19 10:15", "INV001", "Withdrawal", "BWP 1,000.00", "BWP 5,000.00", "Completed"),
            new TransactionRow("2024-03-18 16:45", "CHQ001", "Deposit", "BWP 2,500.00", "BWP 2,500.00", "Completed")
        );
    }

    private void wireActions() {
        depositBtn.setOnAction(e -> handleDeposit());
        withdrawBtn.setOnAction(e -> handleWithdrawal());
        viewHistoryBtn.setOnAction(e -> handleRefreshHistory());
    }

    private void handleDeposit() {
        String acc = accountField.getText().trim();
        String amountStr = amountField.getText().trim();
        if (acc.isEmpty()) {
            appendResult("❌ Please enter an account number");
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (Exception ex) {
            appendResult("❌ Please enter a valid numeric amount");
            return;
        }

        try {
            String res = controller.processDeposit(acc, amount);
            appendResult(res);

            // If deposit succeeded, update table with a new row and refresh balance display
            if (res.contains("✅")) {
                Account account = controller.getAccountByNumber(acc);
                String balanceStr = account != null ? String.format("BWP %.2f", account.getBalance()) : "Unknown";

                String now = LocalDateTime.now().format(dtf);
                transactionTable.getItems().add(0, new TransactionRow(now, acc, "Deposit", String.format("BWP %.2f", amount), balanceStr, "Completed"));
            }
        } catch (Exception ex) {
            appendResult("❌ Error processing deposit: " + ex.getMessage());
        }
    }

    private void handleWithdrawal() {
        String acc = accountField.getText().trim();
        String amountStr = amountField.getText().trim();
        if (acc.isEmpty()) {
            appendResult("❌ Please enter an account number");
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (Exception ex) {
            appendResult("❌ Please enter a valid numeric amount");
            return;
        }

        try {
            String res = controller.processWithdrawal(acc, amount);
            appendResult(res);

            // If withdrawal succeeded, update table with a new row and refresh balance display
            if (res.contains("✅")) {
                Account account = controller.getAccountByNumber(acc);
                String balanceStr = account != null ? String.format("BWP %.2f", account.getBalance()) : "Unknown";

                String now = LocalDateTime.now().format(dtf);
                transactionTable.getItems().add(0, new TransactionRow(now, acc, "Withdrawal", String.format("BWP %.2f", amount), balanceStr, "Completed"));
            }
        } catch (Exception ex) {
            appendResult("❌ Error processing withdrawal: " + ex.getMessage());
        }
    }

    private void handleRefreshHistory() {
        // Best-effort: if controller exposes a transaction history API we can use it.
        // For now we refresh the visible rows only (keep sample + new runtime transactions).
        appendResult("ℹ️ Transaction history refreshed (showing recent runtime transactions).");
        // If you'd like, I can add controller.getTransactionHistory(accountNumber) and wire it here.
    }

    private void appendResult(String msg) {
        if (resultArea == null) return;
        resultArea.appendText(msg + "\n\n");
    }

    // Inner TransactionRow class for table data to avoid colliding with domain model TransactionRecord
    public static class TransactionRow {
        private final String dateTime;
        private final String accountNumber;
        private final String type;
        private final String amount;
        private final String balance;
        private final String status;

        public TransactionRow(String dateTime, String accountNumber, String type, String amount, String balance, String status) {
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