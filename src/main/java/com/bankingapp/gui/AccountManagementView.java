package com.bankingapp.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Professional Account Management Interface
 * Features account creation, management, and analytics
 */
public class AccountManagementView extends BorderPane {
    private TableView<AccountRow> accountTable;
    private TextField searchField;
    private Button openAccountBtn, closeAccountBtn, viewDetailsBtn;

    public AccountManagementView() {
        initializeProfessionalUI();
        loadSampleData();
    }

    private void initializeProfessionalUI() {
        // Header section
        VBox headerSection = createHeaderSection();

        // Toolbar with search and actions
        HBox toolbar = createToolbar();

        // Main content area
        VBox mainContent = createMainContent();

        // Layout assembly
        VBox topSection = new VBox(15, headerSection, toolbar);
        topSection.setPadding(new Insets(20));

        this.setTop(topSection);
        this.setCenter(mainContent);
    }

    private VBox createHeaderSection() {
        VBox header = new VBox(10);

        Label title = new Label("Account Management Portal");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #2c3e50;");

        Label subtitle = new Label("Manage customer accounts, balances, and account operations");
        subtitle.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private HBox createToolbar() {
        HBox toolbar = new HBox(15);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(15, 0, 15, 0));

        // Search field
        searchField = new TextField();
        searchField.setPromptText("Search accounts by number, customer, or type...");
        searchField.setPrefWidth(300);

        // Action buttons
        openAccountBtn = createToolbarButton("➕ Open Account", "btn-primary");
        closeAccountBtn = createToolbarButton("🔒 Close Account", "btn-danger");
        viewDetailsBtn = createToolbarButton("👁️ View Details", "btn-success");

        // Placeholder handlers (wire to controller as needed)
        openAccountBtn.setOnAction(e -> updateStatus("Open Account action triggered"));
        closeAccountBtn.setOnAction(e -> updateStatus("Close Account action triggered"));
        viewDetailsBtn.setOnAction(e -> updateStatus("View Details action triggered"));

        HBox.setHgrow(searchField, Priority.ALWAYS);
        toolbar.getChildren().addAll(searchField, openAccountBtn, closeAccountBtn, viewDetailsBtn);

        return toolbar;
    }

    private Button createToolbarButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        button.setPrefHeight(35);
        return button;
    }

    private VBox createMainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));

        // Account statistics
        HBox statsPanel = createStatsPanel();

        // Account table
        VBox tablePanel = createAccountTablePanel();

        mainContent.getChildren().addAll(statsPanel, tablePanel);
        return mainContent;
    }

    private HBox createStatsPanel() {
        HBox statsPanel = new HBox(20);
        statsPanel.setAlignment(Pos.CENTER_LEFT);

        // Account type statistics (static placeholders, wire to controller to make dynamic)
        VBox savingsStats = createAccountTypeCard("Savings Accounts", "842", "#3498db", "💰");
        VBox investmentStats = createAccountTypeCard("Investment Accounts", "356", "#27ae60", "📈");
        VBox chequeStats = createAccountTypeCard("Cheque Accounts", "645", "#e74c3c", "💳");
        VBox totalStats = createAccountTypeCard("Total Assets", "BWP 148.7M", "#f39c12", "🏦");

        statsPanel.getChildren().addAll(savingsStats, investmentStats, chequeStats, totalStats);
        return statsPanel;
    }

    private VBox createAccountTypeCard(String title, String value, String color, String icon) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setStyle("-fx-border-color: " + color + "; -fx-border-width: 0 0 4 0;");
        card.setPadding(new Insets(15));
        card.setPrefWidth(180);

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 20px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        header.getChildren().addAll(iconLabel, titleLabel);

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        card.getChildren().addAll(header, valueLabel);
        return card;
    }

    private VBox createAccountTablePanel() {
        VBox tablePanel = new VBox(15);

        Label tableTitle = new Label("All Accounts");
        tableTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        accountTable = new TableView<>();
        accountTable.getStyleClass().add("table-view");
        accountTable.setPrefHeight(400);

        // Define professional columns
        TableColumn<AccountRow, String> accNumCol = new TableColumn<>("ACCOUNT NUMBER");
        TableColumn<AccountRow, String> typeCol = new TableColumn<>("ACCOUNT TYPE");
        TableColumn<AccountRow, String> customerCol = new TableColumn<>("CUSTOMER");
        TableColumn<AccountRow, String> balanceCol = new TableColumn<>("BALANCE");
        TableColumn<AccountRow, String> statusCol = new TableColumn<>("STATUS");
        TableColumn<AccountRow, String> openedCol = new TableColumn<>("OPENED DATE");

        // Map columns to properties of AccountRow via PropertyValueFactory
        accNumCol.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("accountType"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        openedCol.setCellValueFactory(new PropertyValueFactory<>("openedDate"));

        // Set preferred widths (optional)
        accNumCol.setPrefWidth(150);
        typeCol.setPrefWidth(140);
        customerCol.setPrefWidth(200);
        balanceCol.setPrefWidth(140);
        statusCol.setPrefWidth(100);
        openedCol.setPrefWidth(150);

        accountTable.getColumns().addAll(accNumCol, typeCol, customerCol, balanceCol, statusCol, openedCol);

        // Add table controls
        HBox tableControls = createTableControls();

        tablePanel.getChildren().addAll(tableTitle, accountTable, tableControls);
        VBox.setVgrow(accountTable, Priority.ALWAYS);

        return tablePanel;
    }

    private HBox createTableControls() {
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_RIGHT);

        Label pageInfo = new Label("Showing 1-25 of 1,843 accounts");
        pageInfo.setStyle("-fx-text-fill: #7f8c8d;");

        Button prevBtn = new Button("◀ Previous");
        Button nextBtn = new Button("Next ▶");

        prevBtn.getStyleClass().add("btn-primary");
        nextBtn.getStyleClass().add("btn-primary");

        ComboBox<String> pageSize = new ComboBox<>();
        pageSize.getItems().addAll("25 per page", "50 per page", "100 per page");
        pageSize.setValue("25 per page");

        controls.getChildren().addAll(pageInfo, prevBtn, pageSize, nextBtn);
        return controls;
    }

    private void loadSampleData() {
        // Add sample account data using the AccountRow helper
        accountTable.getItems().addAll(
            new AccountRow("SAV001", "Savings", "John Doe", "BWP 1,500.00", "Active", "2024-01-15"),
            new AccountRow("INV001", "Investment", "John Doe", "BWP 5,000.00", "Active", "2024-01-20"),
            new AccountRow("CHQ001", "Cheque", "Jane Smith", "BWP 2,500.00", "Active", "2024-02-01"),
            new AccountRow("SAV002", "Savings", "Bob Johnson", "BWP 800.00", "Active", "2024-02-15"),
            new AccountRow("INV002", "Investment", "Alice Brown", "BWP 7,500.00", "Active", "2024-03-01")
        );
    }

    // Inner AccountRow class for table data to avoid conflicting with domain model
    public static class AccountRow {
        private final String accountNumber;
        private final String accountType;
        private final String customer;
        private final String balance;
        private final String status;
        private final String openedDate;

        public AccountRow(String accountNumber, String accountType, String customer, String balance, String status, String openedDate) {
            this.accountNumber = accountNumber;
            this.accountType = accountType;
            this.customer = customer;
            this.balance = balance;
            this.status = status;
            this.openedDate = openedDate;
        }

        public String getAccountNumber() { return accountNumber; }
        public String getAccountType() { return accountType; }
        public String getCustomer() { return customer; }
        public String getBalance() { return balance; }
        public String getStatus() { return status; }
        public String getOpenedDate() { return openedDate; }
    }

    // Simple in-view status update (replace with app-level status bar integration as needed)
    private void updateStatus(String message) {
        // This view doesn't own a global status bar; for now log to console and optionally show a short-lived alert
        System.out.println("[AccountManagementView] " + message);
    }
}