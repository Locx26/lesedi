package com.bankingapp.gui;

import com.bankingapp.controller.BankingController;
import com.bankingapp.model.Account;
import com.bankingapp.model.Customer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Consolidated, compile-safe MainView for SecureTrust Banking System.
 * This file removes duplicate fields/methods and syntax errors from the
 * previously merged/duplicated source while preserving the intended UI.
 */
public class MainView extends Application {

    private BankingController controller;
    private Stage primaryStage;

    // Root layout sections
    private BorderPane root;
    private HBox header;
    private VBox sidebar;
    private StackPane contentArea;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new BankingController();

        primaryStage.setTitle("🏦 SecureTrust Banking System - Enterprise v2.0.0");
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        initializeRootLayout();
        createProfessionalHeader();
        createModernSidebar();
        showDashboard();

        Scene scene = new Scene(root, 1200, 800);

        // Optionally load CSS if available
        try {
            if (getClass().getResource("/com/bankingapp/gui/styles.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/com/bankingapp/gui/styles.css").toExternalForm());
            }
        } catch (Exception ex) {
            System.out.println("⚠️ Could not load CSS: " + ex.getMessage());
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeRootLayout() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f2f5;");

        // Header, sidebar and content area
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));
        root.setCenter(contentArea);

        // Status bar
        HBox statusBar = new HBox();
        statusBar.setStyle("-fx-background-color: #34495e; -fx-padding: 8px;");
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusLabel = new Label("Ready - SecureTrust Banking System v2.0.0");
        statusLabel.setStyle("-fx-text-fill: white;");
        statusBar.getChildren().add(statusLabel);
        root.setBottom(statusBar);
    }

    private void createProfessionalHeader() {
        header = new HBox();
        header.setStyle("-fx-background-color: #2c3e50; -fx-padding: 12 20;");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10);

        VBox titleSection = new VBox(2);
        Label mainTitle = new Label("SecureTrust Banking System");
        mainTitle.setStyle("-fx-text-fill: white;");
        mainTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label subtitle = new Label("Enterprise Financial Management Platform v2.0");
        subtitle.setStyle("-fx-text-fill: #bdc3c7; -fx-font-size: 12px;");

        titleSection.getChildren().addAll(mainTitle, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox userSection = new HBox(10);
        userSection.setAlignment(Pos.CENTER_RIGHT);
        Label welcome = new Label("Welcome, System Administrator");
        welcome.setStyle("-fx-text-fill: #ecf0f1;");

        Button logout = new Button("Logout");
        logout.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        logout.setOnAction(e -> {
            // simple logout placeholder
            updateStatus("User logged out");
        });

        userSection.getChildren().addAll(welcome, logout);

        header.getChildren().addAll(titleSection, spacer, userSection);
        root.setTop(header);
    }

    private void createModernSidebar() {
        sidebar = new VBox(6);
        sidebar.setStyle("-fx-background-color: #34495e;");
        sidebar.setPrefWidth(260);
        sidebar.setPadding(new Insets(12));

        String[] menuItems = {
            "Dashboard", "Customer Management", "Account Management",
            "Transaction Processing", "Interest Calculation", "Reports & Analytics"
        };
        String[] icons = {"📊", "👥", "💳", "💸", "📈", "📋"};

        for (int i = 0; i < menuItems.length; i++) {
            Button b = new Button(icons[i] + "  " + menuItems[i]);
            b.setMaxWidth(Double.MAX_VALUE);
            b.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-alignment: CENTER_LEFT; -fx-padding: 8 12;");
            final int idx = i;
            b.setOnAction(e -> handleMenuSelection(idx));
            sidebar.getChildren().add(b);
        }

        root.setLeft(sidebar);
    }

    private void handleMenuSelection(int index) {
        switch (index) {
            case 0 -> showDashboard();
            case 1 -> showCustomerManagement();
            case 2 -> showAccountManagement();
            case 3 -> showTransactionProcessing();
            case 4 -> showInterestCalculation();
            case 5 -> showReportsAnalytics();
            default -> showDashboard();
        }
    }

    private void showDashboard() {
        VBox dashboard = new VBox(16);
        dashboard.setPadding(new Insets(18));
        dashboard.setStyle("-fx-background-color: white; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        Label headerLabel = new Label("📊 Executive Dashboard");
        headerLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        headerLabel.setStyle("-fx-text-fill: #2c3e50;");

        HBox stats = new HBox(12);

        try {
            var statsObj = controller.getSystemStatistics(); // expects getters: getTotalCustomers/getTotalAccounts/getTotalAssets
            VBox card1 = makeSimpleCard("Customers", String.valueOf(statsObj.getTotalCustomers()), "#3498db");
            VBox card2 = makeSimpleCard("Accounts", String.valueOf(statsObj.getTotalAccounts()), "#27ae60");
            VBox card3 = makeSimpleCard("Total Assets", "BWP " + String.format("%,.2f", statsObj.getTotalAssets()), "#e74c3c");
            stats.getChildren().addAll(card1, card2, card3);
        } catch (Exception ex) {
            stats.getChildren().addAll(makeSimpleCard("Customers", "—", "#3498db"),
                                      makeSimpleCard("Accounts", "—", "#27ae60"),
                                      makeSimpleCard("Total Assets", "—", "#e74c3c"));
            updateStatus("Warning: could not load stats: " + ex.getMessage());
        }

        HBox actions = new HBox(10);
        Button reg = new Button("➕ Register Customer"); reg.setStyle("-fx-background-color:#3498db; -fx-text-fill:white;");
        reg.setOnAction(e -> showCustomerRegistration());

        Button open = new Button("🏦 Open Account"); open.setStyle("-fx-background-color:#27ae60; -fx-text-fill:white;");
        open.setOnAction(e -> showAccountOpening());

        Button tx = new Button("💸 Transactions"); tx.setStyle("-fx-background-color:#e67e22; -fx-text-fill:white;");
        tx.setOnAction(e -> showTransactionProcessing());

        Button rpt = new Button("📊 Report"); rpt.setStyle("-fx-background-color:#9b59b6; -fx-text-fill:white;");
        rpt.setOnAction(e -> showFinancialReport());

        actions.getChildren().addAll(reg, open, tx, rpt);

        dashboard.getChildren().addAll(headerLabel, stats, new Separator(), new Label("Quick Actions:"), actions);
        setContent(dashboard);
        updateStatus("Dashboard loaded");
    }

    private VBox makeSimpleCard(String title, String value, String color) {
        VBox c = new VBox(6);
        c.setPadding(new Insets(12));
        c.setPrefWidth(220);
        c.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 6px;");
        Label t = new Label(title); t.setStyle("-fx-text-fill:white;");
        Label v = new Label(value); v.setStyle("-fx-text-fill:white; -fx-font-size:18px; -fx-font-weight:bold;");
        c.getChildren().addAll(t, v);
        return c;
    }

    private void showCustomerRegistration() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(14));
        form.setStyle("-fx-background-color: white;");

        Label title = new Label("Register New Customer"); title.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        GridPane grid = new GridPane(); grid.setHgap(8); grid.setVgap(8);
        TextField fn = new TextField(), sn = new TextField(), addr = new TextField(), phone = new TextField(), email = new TextField();
        grid.addRow(0, new Label("First name:"), fn);
        grid.addRow(1, new Label("Surname:"), sn);
        grid.addRow(2, new Label("Address:"), addr);
        grid.addRow(3, new Label("Phone:"), phone);
        grid.addRow(4, new Label("Email:"), email);

        Button submit = new Button("Register");
        submit.setOnAction(e -> {
            String result = controller.registerCustomer(fn.getText(), sn.getText(), addr.getText(), phone.getText(), email.getText());
            showAlert("Registration Result", result);
            updateStatus("Customer registration attempted");
        });

        form.getChildren().addAll(title, grid, submit);
        setContent(form);
    }

    private void showAccountOpening() {
        VBox placeholder = new VBox(10, new Label("Account opening forms are available (Savings/Investment/Cheque)."));
        placeholder.setPadding(new Insets(14));
        placeholder.setStyle("-fx-background-color: white;");
        setContent(placeholder);
        updateStatus("Account opening");
    }

    private void showTransactionProcessing() {
        VBox placeholder = new VBox(10, new Label("Transaction processing (deposit/withdraw/transfer)"));
        placeholder.setPadding(new Insets(14));
        placeholder.setStyle("-fx-background-color: white;");
        setContent(placeholder);
        updateStatus("Transaction processing");
    }

    private void showAccountManagement() {
        VBox v = new VBox(10, new Label("Account Management view (placeholder)"));
        v.setPadding(new Insets(14)); v.setStyle("-fx-background-color:white;");
        setContent(v);
    }

    private void showCustomerManagement() {
        VBox v = new VBox(10, new Label("Customer Management view (placeholder)"));
        v.setPadding(new Insets(14)); v.setStyle("-fx-background-color:white;");
        setContent(v);
    }

    private void showInterestCalculation() {
        VBox v = new VBox(10, new Label("Interest Calculation view (placeholder)"));
        v.setPadding(new Insets(14)); v.setStyle("-fx-background-color:white;");
        setContent(v);
    }

    private void showReportsAnalytics() {
        VBox v = new VBox(10, new Label("Reports & Analytics view (placeholder)"));
        v.setPadding(new Insets(14)); v.setStyle("-fx-background-color:white;");
        setContent(v);
    }

    private void showFinancialReport() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(14)); box.setStyle("-fx-background-color:white;");
        Label t = new Label("Financial Performance Report"); t.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextArea report = new TextArea();
        report.setPrefRowCount(20);
        Button gen = new Button("Generate");
        gen.setOnAction(e -> report.setText(controller.generateFinancialReport()));
        box.getChildren().addAll(t, gen, report);
        setContent(box);
    }

    private void setContent(javafx.scene.Node node) {
        contentArea.getChildren().setAll(node);
    }

    private void updateStatus(String msg) {
        if (statusLabel != null) statusLabel.setText(msg + " • " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void showAlert(String title, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        TextArea ta = new TextArea(content);
        ta.setEditable(false); ta.setWrapText(true); ta.setPrefSize(600, 300);
        a.getDialogPane().setContent(ta);
        a.showAndWait();
    }

    public static void main(String[] args) {
        // If running in a truly headless environment, don't attempt to start JavaFX UI.
        if (java.awt.GraphicsEnvironment.isHeadless()) {
            System.out.println("Headless environment detected. Run the application in web/headless mode instead.");
            return;
        }
        launch(args);
    }
}