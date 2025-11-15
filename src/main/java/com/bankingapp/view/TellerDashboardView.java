package com.bankingapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Bank Teller Dashboard View - Boundary Class for Teller Operations
 * Pure UI implementation with no business logic
 */
public class TellerDashboardView {
    private BorderPane view;
    private Button onboardCustomerButton;
    private Button openAccountButton;
    private Button viewCustomersButton;
    private Button processTransactionButton;
    private Button viewReportsButton;
    private Button logoutButton;
    private Label welcomeLabel;
    private TextArea infoTextArea;
    private TabPane tabPane;
    private ProgressIndicator loadingIndicator;

    public TellerDashboardView() {
        createView();
    }

    private void createView() {
        view = new BorderPane();
        view.setPadding(new Insets(20));
        view.getStyleClass().add("root");

        view.setTop(createTopBar());
        view.setCenter(createMainContent());
        view.setBottom(createStatusBar());
    }

    private HBox createTopBar() {
        VBox welcomeSection = new VBox(5);
        welcomeLabel = new Label("Bank Teller Dashboard");
        welcomeLabel.getStyleClass().add("header-primary");
       
        Label subtitleLabel = new Label("Customer Management & Operations");
        subtitleLabel.getStyleClass().add("body-text");
       
        welcomeSection.getChildren().addAll(welcomeLabel, subtitleLabel);

        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(false);
        loadingIndicator.setPrefSize(20, 20);

        logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("danger-button");

        HBox rightSection = new HBox(15);
        rightSection.setAlignment(Pos.CENTER);
        rightSection.getChildren().addAll(loadingIndicator, logoutButton);

        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        HBox.setHgrow(welcomeSection, Priority.ALWAYS);
        topBar.getChildren().addAll(welcomeSection, rightSection);

        return topBar;
    }

    private VBox createMainContent() {
        VBox mainContent = new VBox(20);
       
        // Quick Actions Panel
        VBox quickActionsPanel = createQuickActionsPanel();
       
        // Information Tabs
        tabPane = createTabPane();
       
        mainContent.getChildren().addAll(quickActionsPanel, tabPane);
        return mainContent;
    }

    private VBox createQuickActionsPanel() {
        Label actionsTitle = new Label("Quick Actions");
        actionsTitle.getStyleClass().add("header-secondary");

        // First row of buttons
        HBox buttonRow1 = new HBox(15);
        buttonRow1.setAlignment(Pos.CENTER_LEFT);
       
        onboardCustomerButton = new Button("Onboard New Customer");
        onboardCustomerButton.getStyleClass().add("success-button");
        onboardCustomerButton.setPrefSize(200, 50);
       
        openAccountButton = new Button("Open New Account");
        openAccountButton.getStyleClass().add("primary-button");
        openAccountButton.setPrefSize(200, 50);
       
        buttonRow1.getChildren().addAll(onboardCustomerButton, openAccountButton);

        // Second row of buttons
        HBox buttonRow2 = new HBox(15);
        buttonRow2.setAlignment(Pos.CENTER_LEFT);
       
        viewCustomersButton = new Button("View Customers");
        viewCustomersButton.getStyleClass().add("warning-button");
        viewCustomersButton.setPrefSize(200, 50);
       
        processTransactionButton = new Button("Process Transaction");
        processTransactionButton.getStyleClass().add("primary-button");
        processTransactionButton.setPrefSize(200, 50);
       
        viewReportsButton = new Button("View Reports");
        viewReportsButton.getStyleClass().add("secondary-button");
        viewReportsButton.setPrefSize(200, 50);
       
        buttonRow2.getChildren().addAll(viewCustomersButton, processTransactionButton, viewReportsButton);

        VBox quickActionsPanel = new VBox(15);
        quickActionsPanel.setPadding(new Insets(20));
        quickActionsPanel.getStyleClass().add("card");
        quickActionsPanel.getChildren().addAll(actionsTitle, buttonRow1, buttonRow2);
       
        return quickActionsPanel;
    }

    private TabPane createTabPane() {
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Customer Management Tab
        Tab customerTab = new Tab("Customer Management");
        customerTab.setContent(createCustomerManagementContent());
        customerTab.setClosable(false);

        // Account Management Tab
        Tab accountTab = new Tab("Account Management");
        accountTab.setContent(createAccountManagementContent());
        accountTab.setClosable(false);

        // System Info Tab
        Tab systemTab = new Tab("System Information");
        systemTab.setContent(createSystemInfoContent());
        systemTab.setClosable(false);

        tabPane.getTabs().addAll(customerTab, accountTab, systemTab);
        return tabPane;
    }

    private VBox createCustomerManagementContent() {
        Label titleLabel = new Label("Customer Management");
        titleLabel.getStyleClass().add("header-tertiary");

        infoTextArea = new TextArea();
        infoTextArea.setPrefHeight(250);
        infoTextArea.setEditable(false);
        infoTextArea.setWrapText(true);
        infoTextArea.getStyleClass().add("text-field");
       
        // Sample customer data for UI demonstration
        infoTextArea.setText(
            "=== CUSTOMER MANAGEMENT SYSTEM ===\n\n" +
            "Recent Activities:\n" +
            "• 2024-01-15: New customer - John Smith (ID: CUST001)\n" +
            "• 2024-01-14: Updated contact info - Sarah Johnson\n" +
            "• 2024-01-13: Account review completed - Michael Brown\n" +
            "• 2024-01-12: Address verification - Emily Davis\n\n" +
            "Pending Actions:\n" +
            "• 3 customers awaiting document verification\n" +
            "• 2 address updates pending review\n" +
            "• 1 customer requires follow-up call"
        );

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("card");
        content.getChildren().addAll(titleLabel, infoTextArea);
       
        return content;
    }

    private VBox createAccountManagementContent() {
        Label titleLabel = new Label("Account Management");
        titleLabel.getStyleClass().add("header-tertiary");

        TextArea accountTextArea = new TextArea();
        accountTextArea.setPrefHeight(250);
        accountTextArea.setEditable(false);
        accountTextArea.setWrapText(true);
        accountTextArea.getStyleClass().add("text-field");
       
        accountTextArea.setText(
            "=== ACCOUNT MANAGEMENT ===\n\n" +
            "Account Statistics:\n" +
            "• Total Accounts: 1,247\n" +
            "• Savings Accounts: 856\n" +
            "• Checking Accounts: 321\n" +
            "• Business Accounts: 70\n\n" +
            "Today's Activities:\n" +
            "• New accounts opened: 5\n" +
            "• Accounts closed: 2\n" +
            "• Status changes: 3\n\n" +
            "Alerts:\n" +
            "• 12 accounts approaching minimum balance\n" +
            "• 3 accounts require attention"
        );

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("card");
        content.getChildren().addAll(titleLabel, accountTextArea);
       
        return content;
    }

    private VBox createSystemInfoContent() {
        Label titleLabel = new Label("System Information");
        titleLabel.getStyleClass().add("header-tertiary");

        TextArea systemTextArea = new TextArea();
        systemTextArea.setPrefHeight(250);
        systemTextArea.setEditable(false);
        systemTextArea.setWrapText(true);
        systemTextArea.getStyleClass().add("text-field");
       
        systemTextArea.setText(
            "=== SYSTEM STATUS ===\n\n" +
            "Core Banking System: ✅ ONLINE\n" +
            "Database Connection: ✅ STABLE\n" +
            "Transaction Processor: ✅ OPERATIONAL\n" +
            "Security Module: ✅ ACTIVE\n\n" +
            "Performance Metrics:\n" +
            "• Uptime: 99.98%\n" +
            "• Response Time: < 200ms\n" +
            "• Active Users: 47\n" +
            "• Transactions Today: 1,842\n\n" +
            "Security Status:\n" +
            "• Last Security Scan: 2024-01-15 14:30\n" +
            "• Encryption: AES-256 ✅\n" +
            "• Firewall: ACTIVE ✅"
        );

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("card");
        content.getChildren().addAll(titleLabel, systemTextArea);
       
        return content;
    }

    private HBox createStatusBar() {
        Label statusLabel = new Label("Teller System Active • All services operational");
        statusLabel.getStyleClass().add("body-text");

        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(10, 0, 0, 0));
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.getChildren().add(statusLabel);
       
        return statusBar;
    }

    // ===== PUBLIC API FOR CONTROLLERS =====
   
    public BorderPane getView() { return view; }
    public Button getOnboardCustomerButton() { return onboardCustomerButton; }
    public Button getOpenAccountButton() { return openAccountButton; }
    public Button getViewCustomersButton() { return viewCustomersButton; }
    public Button getProcessTransactionButton() { return processTransactionButton; }
    public Button getViewReportsButton() { return viewReportsButton; }
    public Button getLogoutButton() { return logoutButton; }
    public TextArea getInfoTextArea() { return infoTextArea; }
    public Label getWelcomeLabel() { return welcomeLabel; }
    public TabPane getTabPane() { return tabPane; }
    public ProgressIndicator getLoadingIndicator() { return loadingIndicator; }

    // ===== VIEW UPDATE METHODS =====
   
    public void setWelcomeMessage(String tellerName) {
        welcomeLabel.setText("Bank Teller Dashboard - " + tellerName);
    }

    public void appendInfo(String info) {
        infoTextArea.appendText(info + "\n");
    }

    public void clearInfo() {
        infoTextArea.clear();
    }

    public void setLoading(boolean loading) {
        loadingIndicator.setVisible(loading);
    }

    public void showSystemMessage(String message, String type) {
        String timestamp = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        String formattedMessage = String.format("[%s] %s", timestamp, message);
       
        appendInfo(formattedMessage);
    }
}