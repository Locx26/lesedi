package com.bankingapp.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Professional Customer Management Interface
 * Features advanced filtering, search, and bulk operations
 */
public class CustomerManagementView extends BorderPane {
    private TableView<CustomerRow> customerTable;
    private TextField searchField;
    private Button addCustomerBtn, editCustomerBtn, deleteCustomerBtn, exportBtn;

    public CustomerManagementView() {
        initializeProfessionalUI();
        loadSampleData();
    }

    private void initializeProfessionalUI() {
        // Header section
        VBox headerSection = createHeaderSection();

        // Toolbar with search and actions
        HBox toolbar = createToolbar();

        // Main content area
        SplitPane mainContent = createMainContent();

        // Layout assembly
        VBox topSection = new VBox(15, headerSection, toolbar);
        topSection.setPadding(new Insets(20));

        this.setTop(topSection);
        this.setCenter(mainContent);
    }

    private VBox createHeaderSection() {
        VBox header = new VBox(10);

        Label title = new Label("Customer Relationship Management");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #2c3e50;");

        Label subtitle = new Label("Manage customer profiles, accounts, and relationships");
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
        searchField.setPromptText("Search customers by name, ID, or contact...");
        searchField.setPrefWidth(300);

        // Action buttons
        addCustomerBtn = createToolbarButton("➕ Add Customer", "btn-primary");
        editCustomerBtn = createToolbarButton("✏️ Edit", "btn-success");
        deleteCustomerBtn = createToolbarButton("🗑️ Delete", "btn-danger");
        exportBtn = createToolbarButton("📊 Export", "btn-warning");

        // Placeholder handlers
        addCustomerBtn.setOnAction(e -> updateStatus("Add Customer action triggered"));
        editCustomerBtn.setOnAction(e -> updateStatus("Edit Customer action triggered"));
        deleteCustomerBtn.setOnAction(e -> updateStatus("Delete Customer action triggered"));
        exportBtn.setOnAction(e -> updateStatus("Export action triggered"));

        HBox.setHgrow(searchField, Priority.ALWAYS);
        toolbar.getChildren().addAll(searchField, addCustomerBtn, editCustomerBtn, deleteCustomerBtn, exportBtn);

        return toolbar;
    }

    private Button createToolbarButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        button.setPrefHeight(35);
        return button;
    }

    private SplitPane createMainContent() {
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.3);

        // Left panel - filters and quick stats
        VBox leftPanel = createFiltersPanel();

        // Right panel - customer table
        VBox rightPanel = createCustomerTablePanel();

        splitPane.getItems().addAll(leftPanel, rightPanel);
        return splitPane;
    }

    private VBox createFiltersPanel() {
        VBox filtersPanel = new VBox(20);
        filtersPanel.getStyleClass().add("card");
        filtersPanel.setPadding(new Insets(20));
        filtersPanel.setPrefWidth(350);

        Label filterTitle = new Label("Filters & Analytics");
        filterTitle.getStyleClass().add("card-title");

        // Account type filter
        VBox accountTypeFilter = createFilterSection("Account Type",
            new CheckBox("Savings Accounts"),
            new CheckBox("Investment Accounts"),
            new CheckBox("Cheque Accounts")
        );

        // Status filter
        VBox statusFilter = createFilterSection("Customer Status",
            new CheckBox("Active"),
            new CheckBox("Inactive"),
            new CheckBox("Pending Verification")
        );

        // Quick stats
        VBox quickStats = createQuickStats();

        filtersPanel.getChildren().addAll(filterTitle, accountTypeFilter, statusFilter, quickStats);
        return filtersPanel;
    }

    private VBox createFilterSection(String title, CheckBox... checkboxes) {
        VBox section = new VBox(10);

        Label sectionTitle = new Label(title);
        sectionTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox checkboxesContainer = new VBox(8);
        for (CheckBox checkbox : checkboxes) {
            checkbox.setStyle("-fx-text-fill: #5a6c7d;");
            checkboxesContainer.getChildren().add(checkbox);
        }

        section.getChildren().addAll(sectionTitle, checkboxesContainer);
        return section;
    }

    private VBox createQuickStats() {
        VBox stats = new VBox(15);
        stats.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-background-radius: 8;");

        Label statsTitle = new Label("Quick Statistics");
        statsTitle.setStyle("-fx-font-weight: bold;");

        String[] statData = {
            "Total Customers: 1,247",
            "Active This Month: 894",
            "New This Week: 23",
            "Accounts per Customer: 2.3"
        };

        for (String stat : statData) {
            Label statLabel = new Label(stat);
            statLabel.setStyle("-fx-text-fill: #5a6c7d; -fx-font-size: 13px;");
            stats.getChildren().add(statLabel);
        }

        return stats;
    }

    private VBox createCustomerTablePanel() {
        VBox tablePanel = new VBox(15);
        tablePanel.setPadding(new Insets(20));

        customerTable = new TableView<>();
        customerTable.getStyleClass().add("table-view");

        // Define professional columns and bind to CustomerRow properties
        TableColumn<CustomerRow, String> idCol = new TableColumn<>("CUSTOMER ID");
        TableColumn<CustomerRow, String> nameCol = new TableColumn<>("FULL NAME");
        TableColumn<CustomerRow, String> contactCol = new TableColumn<>("CONTACT INFO");
        TableColumn<CustomerRow, String> accountsCol = new TableColumn<>("ACCOUNTS");
        TableColumn<CustomerRow, String> statusCol = new TableColumn<>("STATUS");
        TableColumn<CustomerRow, String> joinDateCol = new TableColumn<>("MEMBER SINCE");

        idCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        accountsCol.setCellValueFactory(new PropertyValueFactory<>("accounts"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        joinDateCol.setCellValueFactory(new PropertyValueFactory<>("joinDate"));

        // Optional column sizing
        idCol.setPrefWidth(130);
        nameCol.setPrefWidth(220);
        contactCol.setPrefWidth(260);
        accountsCol.setPrefWidth(120);
        statusCol.setPrefWidth(100);
        joinDateCol.setPrefWidth(140);

        customerTable.getColumns().addAll(idCol, nameCol, contactCol, accountsCol, statusCol, joinDateCol);

        // Add table controls
        HBox tableControls = createTableControls();

        tablePanel.getChildren().addAll(customerTable, tableControls);
        VBox.setVgrow(customerTable, Priority.ALWAYS);

        return tablePanel;
    }

    private HBox createTableControls() {
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_RIGHT);

        Label pageInfo = new Label("Showing 1-25 of 1,247 customers");
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
        // Add sample customer data using CustomerRow to avoid collisions with domain model
        customerTable.getItems().addAll(
            new CustomerRow("CUST001", "John Doe", "john.doe@email.com | 71123456", "2 Accounts", "Active", "2024-01-15"),
            new CustomerRow("CUST002", "Jane Smith", "jane.smith@email.com | 72123456", "1 Account", "Active", "2024-02-20"),
            new CustomerRow("CUST003", "Bob Johnson", "bob.johnson@email.com | 73123456", "3 Accounts", "Active", "2024-01-05"),
            new CustomerRow("CUST004", "Alice Brown", "alice.brown@email.com | 74123456", "2 Accounts", "Inactive", "2024-03-10"),
            new CustomerRow("CUST005", "Charlie Wilson", "charlie.wilson@email.com | 75123456", "1 Account", "Active", "2024-02-28")
        );
    }

    // Inner CustomerRow class for table data to avoid conflicting with domain model
    public static class CustomerRow {
        private final String customerId;
        private final String fullName;
        private final String contact;
        private final String accounts;
        private final String status;
        private final String joinDate;

        public CustomerRow(String customerId, String fullName, String contact, String accounts, String status, String joinDate) {
            this.customerId = customerId;
            this.fullName = fullName;
            this.contact = contact;
            this.accounts = accounts;
            this.status = status;
            this.joinDate = joinDate;
        }

        public String getCustomerId() { return customerId; }
        public String getFullName() { return fullName; }
        public String getContact() { return contact; }
        public String getAccounts() { return accounts; }
        public String getStatus() { return status; }
        public String getJoinDate() { return joinDate; }
    }

    // Simple in-view status update (replace with app-level status bar integration as needed)
    private void updateStatus(String message) {
        // For now log to console; integrate with global status bar if available
        System.out.println("[CustomerManagementView] " + message);
    }
}