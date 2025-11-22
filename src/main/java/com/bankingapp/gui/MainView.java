package com.bankingapp.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Professional Banking System Main Interface
 * Features modern UI with dashboard, analytics, and professional styling
 */
public class MainView extends Application {
    private BorderPane root;
    private StackPane contentArea;
    private VBox sidebar;
    private HBox header;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SecureTrust Banking System v2.0 - Enterprise Banking Platform");

        initializeRootLayout();
        createProfessionalHeader();
        createModernSidebar();
        createDashboard();

        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/com/bankingapp/gui/styles.css").toExternalForm());
       
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    private void initializeRootLayout() {
        root = new BorderPane();
        root.getStyleClass().add("main-container");
       
        // Main content area
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));
        root.setCenter(contentArea);
    }

    private void createProfessionalHeader() {
        header = new HBox();
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 25, 15, 25));
       
        // Logo and title section
        VBox titleSection = new VBox(5);
        Label mainTitle = new Label("SecureTrust Banking System");
        mainTitle.getStyleClass().add("header-title");
       
        Label subtitle = new Label("Enterprise Financial Management Platform v2.0");
        subtitle.getStyleClass().add("header-subtitle");
       
        titleSection.getChildren().addAll(mainTitle, subtitle);
       
        // User info section
        HBox userSection = new HBox(15);
        userSection.setAlignment(Pos.CENTER_RIGHT);
       
        Label welcomeLabel = new Label("Welcome, System Administrator");
        welcomeLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: 600;");
       
        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("btn-primary");
       
        userSection.getChildren().addAll(welcomeLabel, logoutBtn);
       
        HBox.setHgrow(titleSection, Priority.ALWAYS);
        header.getChildren().addAll(titleSection, userSection);
       
        root.setTop(header);
    }

    private void createModernSidebar() {
        sidebar = new VBox(5);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(280);
        sidebar.setPadding(new Insets(20, 0, 20, 0));
       
        // Navigation items
        String[] menuItems = {
            "Dashboard", "Customer Management", "Account Management",
            "Transaction Processing", "Interest Calculation", "Reports & Analytics",
            "System Settings", "User Management", "Audit Logs"
        };
       
        String[] icons = {"📊", "👥", "💳", "💸", "📈", "📋", "⚙️", "👤", "🔍"};
       
        for (int i = 0; i < menuItems.length; i++) {
            Button menuItem = createMenuItem(icons[i] + "  " + menuItems[i]);
            final int index = i;
            menuItem.setOnAction(e -> handleMenuSelection(index));
            sidebar.getChildren().add(menuItem);
        }
       
        VBox.setVgrow(sidebar, Priority.ALWAYS);
        root.setLeft(sidebar);
    }

    private Button createMenuItem(String text) {
        Button menuItem = new Button(text);
        menuItem.getStyleClass().add("sidebar-item");
        menuItem.setMaxWidth(Double.MAX_VALUE);
        menuItem.setAlignment(Pos.CENTER_LEFT);
        menuItem.setContentDisplay(ContentDisplay.LEFT);
        return menuItem;
    }

    private void handleMenuSelection(int index) {
        switch (index) {
            case 0:
                createDashboard();
                break;
            case 1:
                showCustomerManagement();
                break;
            case 2:
                showAccountManagement();
                break;
            case 3:
                showTransactionProcessing();
                break;
            case 4:
                showInterestCalculation();
                break;
            case 5:
                showReportsAnalytics();
                break;
            default:
                createDashboard();
        }
    }

    private void createDashboard() {
        VBox dashboard = new VBox(20);
        dashboard.getStyleClass().add("dashboard-pane");
       
        // Dashboard header
        Label dashboardTitle = new Label("Executive Dashboard");
        dashboardTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        dashboardTitle.setStyle("-fx-text-fill: #2c3e50;");
       
        // Statistics cards
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);
        statsGrid.setPadding(new Insets(20, 0, 20, 0));
       
        // Add stat cards
        statsGrid.add(createStatCard("Total Customers", "1,247", "👥", "#3498db"), 0, 0);
        statsGrid.add(createStatCard("Active Accounts", "2,843", "💳", "#27ae60"), 1, 0);
        statsGrid.add(createStatCard("Total Assets", "BWP 148.7M", "💰", "#f39c12"), 2, 0);
        statsGrid.add(createStatCard("Monthly Interest", "BWP 742K", "📈", "#e74c3c"), 3, 0);
       
        // Quick actions
        VBox quickActions = createQuickActionsPanel();
       
        // Recent activity
        VBox recentActivity = createRecentActivityPanel();
       
        HBox contentRow = new HBox(20);
        contentRow.getChildren().addAll(quickActions, recentActivity);
       
        dashboard.getChildren().addAll(dashboardTitle, statsGrid, contentRow);
        contentArea.getChildren().setAll(dashboard);
    }

    private VBox createStatCard(String title, String value, String icon, String color) {
        VBox card = new VBox(10);
        card.getStyleClass().add("stat-card");
        card.setStyle("-fx-background-color: " + color + ";");
        card.setPadding(new Insets(20));
        card.setPrefSize(200, 120);
       
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
       
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-value");
       
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("stat-label");
       
        card.getChildren().addAll(iconLabel, valueLabel, titleLabel);
        return card;
    }

    private VBox createQuickActionsPanel() {
        VBox panel = new VBox(15);
        panel.getStyleClass().add("card");
        panel.setPrefWidth(400);
       
        Label title = new Label("Quick Actions");
        title.getStyleClass().add("card-title");
       
        String[] actions = {
            "➕ Open New Account", "👥 Register Customer", "💸 Process Transaction",
            "📈 Calculate Interest", "📊 Generate Report", "⚙️ System Settings"
        };
       
        VBox actionButtons = new VBox(10);
        for (String action : actions) {
            Button btn = new Button(action);
            btn.getStyleClass().add("btn-primary");
            btn.setMaxWidth(Double.MAX_VALUE);
            actionButtons.getChildren().add(btn);
        }
       
        panel.getChildren().addAll(title, actionButtons);
        return panel;
    }

    private VBox createRecentActivityPanel() {
        VBox panel = new VBox(15);
        panel.getStyleClass().add("card");
        panel.setPrefWidth(500);
       
        Label title = new Label("Recent Activity");
        title.getStyleClass().add("card-title");
       
        TableView<String> activityTable = new TableView<>();
       
        TableColumn<String, String> timeCol = new TableColumn<>("Time");
        TableColumn<String, String> activityCol = new TableColumn<>("Activity");
        TableColumn<String, String> userCol = new TableColumn<>("User");
       
        activityTable.getColumns().addAll(timeCol, activityCol, userCol);
        activityTable.setPrefHeight(200);
       
        panel.getChildren().addAll(title, activityTable);
        return panel;
    }

    private void showCustomerManagement() {
        CustomerManagementView customerView = new CustomerManagementView();
        contentArea.getChildren().setAll(customerView);
    }

    private void showAccountManagement() {
        AccountManagementView accountView = new AccountManagementView();
        contentArea.getChildren().setAll(accountView);
    }

    private void showTransactionProcessing() {
        TransactionView transactionView = new TransactionView();
        contentArea.getChildren().setAll(transactionView);
    }

    private void showInterestCalculation() {
        VBox interestView = new VBox(20);
        interestView.getStyleClass().add("dashboard-pane");
       
        Label title = new Label("Interest Calculation Engine");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
       
        // Interest calculation form
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));
       
        Label accountLabel = new Label("Account Number:");
        TextField accountField = new TextField();
        accountField.setPromptText("Enter account number");
       
        Label amountLabel = new Label("Interest Rate (%):");
        TextField amountField = new TextField();
        amountField.setPromptText("Enter interest rate");
       
        Button calculateBtn = new Button("Calculate Interest");
        calculateBtn.getStyleClass().add("btn-primary");
       
        TextArea resultArea = new TextArea();
        resultArea.setPrefHeight(150);
        resultArea.setPromptText("Interest calculation results will appear here...");
       
        form.add(accountLabel, 0, 0);
        form.add(accountField, 1, 0);
        form.add(amountLabel, 0, 1);
        form.add(amountField, 1, 1);
        form.add(calculateBtn, 1, 2);
        form.add(resultArea, 0, 3, 2, 1);
       
        GridPane.setHgrow(accountField, Priority.ALWAYS);
        GridPane.setHgrow(amountField, Priority.ALWAYS);
        GridPane.setHgrow(resultArea, Priority.ALWAYS);
       
        interestView.getChildren().addAll(title, form);
        contentArea.getChildren().setAll(interestView);
    }

    private void showReportsAnalytics() {
        VBox reportsView = new VBox(20);
        reportsView.getStyleClass().add("dashboard-pane");
       
        Label title = new Label("Reports & Analytics");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
       
        // Report selection
        HBox reportSelection = new HBox(15);
        reportSelection.setAlignment(Pos.CENTER_LEFT);
       
        ComboBox<String> reportType = new ComboBox<>();
        reportType.getItems().addAll(
            "Customer Portfolio Analysis",
            "Transaction Volume Reports",
            "Interest Accrual Statements",
            "Risk Assessment Metrics",
            "Regulatory Compliance Reports"
        );
        reportType.setValue("Customer Portfolio Analysis");
       
        Button generateBtn = new Button("Generate Report");
        generateBtn.getStyleClass().add("btn-primary");
       
        reportSelection.getChildren().addAll(new Label("Report Type:"), reportType, generateBtn);
       
        // Report display area
        TextArea reportArea = new TextArea();
        reportArea.setPrefHeight(400);
        reportArea.setText("Financial Reports Dashboard\n\n" +
            "• Customer Portfolio Analysis\n" +
            "• Transaction Volume Reports\n" +
            "• Interest Accrual Statements\n" +
            "• Risk Assessment Metrics\n" +
            "• Regulatory Compliance Reports\n\n" +
            "Select a report type and click 'Generate Report' to view detailed analytics.");
       
        reportsView.getChildren().addAll(title, reportSelection, reportArea);
        contentArea.getChildren().setAll(reportsView);
    }

    public static void main(String[] args) {
        launch(args);
    }
}