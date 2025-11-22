package com.bankingapp.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Professional Reports and Analytics Interface
 * Features financial reporting and business intelligence
 */
public class ReportView extends BorderPane {
    private TextArea reportArea;
    private ComboBox<String> reportTypeCombo;
    private Button generateBtn, exportBtn, printBtn;

    public ReportView() {
        initializeProfessionalUI();
    }

    private void initializeProfessionalUI() {
        // Header section
        VBox headerSection = createHeaderSection();
       
        // Main content area
        VBox mainContent = createMainContent();
       
        // Layout assembly
        this.setTop(headerSection);
        this.setCenter(mainContent);
    }

    private VBox createHeaderSection() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(20));
       
        Label title = new Label("Reports & Analytics Dashboard");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #2c3e50;");
       
        Label subtitle = new Label("Generate comprehensive financial reports and business analytics");
        subtitle.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");
       
        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private VBox createMainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
       
        // Report controls
        HBox controlsPanel = createControlsPanel();
       
        // Report display area
        VBox reportPanel = createReportPanel();
       
        mainContent.getChildren().addAll(controlsPanel, reportPanel);
        VBox.setVgrow(reportPanel, Priority.ALWAYS);
       
        return mainContent;
    }

    private HBox createControlsPanel() {
        HBox controlsPanel = new HBox(20);
        controlsPanel.setAlignment(Pos.CENTER_LEFT);
        controlsPanel.setPadding(new Insets(15));
        controlsPanel.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
       
        Label reportTypeLabel = new Label("Report Type:");
        reportTypeLabel.setStyle("-fx-font-weight: bold;");
       
        reportTypeCombo = new ComboBox<>();
        reportTypeCombo.getItems().addAll(
            "Customer Portfolio Analysis",
            "Transaction Volume Report",
            "Interest Accrual Statement",
            "Account Growth Analysis",
            "Risk Assessment Report",
            "Regulatory Compliance Report",
            "Financial Performance Summary",
            "Customer Behavior Analytics"
        );
        reportTypeCombo.setValue("Customer Portfolio Analysis");
        reportTypeCombo.setPrefWidth(250);
       
        generateBtn = new Button("📊 Generate Report");
        exportBtn = new Button("💾 Export to PDF");
        printBtn = new Button("🖨️ Print Report");
       
        generateBtn.getStyleClass().add("btn-primary");
        exportBtn.getStyleClass().add("btn-success");
        printBtn.getStyleClass().add("btn-warning");
       
        controlsPanel.getChildren().addAll(reportTypeLabel, reportTypeCombo, generateBtn, exportBtn, printBtn);
        return controlsPanel;
    }

    private VBox createReportPanel() {
        VBox reportPanel = new VBox(15);
        reportPanel.getStyleClass().add("card");
        reportPanel.setPadding(new Insets(25));
       
        Label title = new Label("Financial Report");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #2c3e50;");
       
        reportArea = new TextArea();
        reportArea.setPrefHeight(500);
        reportArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        reportArea.setText(getSampleReport());
       
        reportPanel.getChildren().addAll(title, reportArea);
        VBox.setVgrow(reportArea, Priority.ALWAYS);
       
        return reportPanel;
    }

    private String getSampleReport() {
        return """
            ==================================================
                    SECURETRUST BANKING SYSTEM
                   FINANCIAL PERFORMANCE REPORT
                   Generated: March 20, 2024 14:30
            ==================================================

            EXECUTIVE SUMMARY
            • Total Customers: 1,247
            • Active Accounts: 2,843
            • Total Assets Under Management: BWP 148,750,000
            • Monthly Interest Paid: BWP 742,150

            ACCOUNT BREAKDOWN
            ┌─────────────────┬────────────┬───────────────┐
            │ Account Type    │ Count      │ Total Balance │
            ├─────────────────┼────────────┼───────────────┤
            │ Savings         │ 842        │ BWP 12,630,000│
            │ Investment      │ 356        │ BWP 89,250,000│
            │ Cheque          │ 645        │ BWP 46,870,000│
            └─────────────────┴────────────┴───────────────┘

            TRANSACTION ANALYSIS
            • Total Deposits (30 days): BWP 8,450,000
            • Total Withdrawals (30 days): BWP 6,230,000
            • Net Cash Flow: BWP 2,220,000

            INTEREST ANALYSIS
            • Savings Interest Rate: 0.05% monthly
            • Investment Interest Rate: 5% monthly
            • Total Interest Paid (Month): BWP 742,150

            CUSTOMER SEGMENTATION
            • Premium Customers (>BWP 50,000): 187
            • Standard Customers: 860
            • New Customers (30 days): 45

            RISK ASSESSMENT
            • Low Risk Accounts: 92%
            • Medium Risk Accounts: 7%
            • High Risk Accounts: 1%

            RECOMMENDATIONS
            1. Consider increasing savings interest rates to attract more deposits
            2. Launch targeted marketing for investment accounts
            3. Review high-risk accounts for enhanced monitoring

            ==================================================
                           END OF REPORT
            ==================================================
            """;
    }
}