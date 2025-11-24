package com.bankingapp.gui;

import com.bankingapp.controller.BankingController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Professional Reports and Analytics Interface
 * Features financial reporting and business intelligence
 *
 * This version wires the Generate button to the BankingController so reports are produced from
 * the actual system data. Export saves the report as UTF‑8 plain text. Print uses JavaFX PrinterJob.
 */
public class ReportView extends BorderPane {
    private final BankingController controller;
    private TextArea reportArea;
    private ComboBox<String> reportTypeCombo;
    private Button generateBtn, exportBtn, printBtn;

    public ReportView() {
        this(new BankingController());
    }

    /**
     * Construct ReportView with a controller (allows easier testing / injection)
     */
    public ReportView(BankingController controller) {
        this.controller = controller;
        initializeProfessionalUI();
        // Populate initial report from live controller data
        reportArea.setText(controller.generateFinancialReport());
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
        reportTypeCombo.setValue("Financial Performance Summary");
        reportTypeCombo.setPrefWidth(250);

        generateBtn = new Button("📊 Generate Report");
        exportBtn = new Button("💾 Export to File");
        printBtn = new Button("🖨️ Print Report");

        generateBtn.getStyleClass().add("btn-primary");
        exportBtn.getStyleClass().add("btn-success");
        printBtn.getStyleClass().add("btn-warning");

        // Actions
        generateBtn.setOnAction(e -> generateReport());
        exportBtn.setOnAction(e -> exportReport());
        printBtn.setOnAction(e -> printReport());

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
        reportArea.setWrapText(true);

        reportPanel.getChildren().addAll(title, reportArea);
        VBox.setVgrow(reportArea, Priority.ALWAYS);

        return reportPanel;
    }

    /**
     * Request a report from the controller and display it.
     * This keeps the view thin and delegates business logic to the controller.
     */
    private void generateReport() {
        try {
            // In future we may switch on reportTypeCombo to produce different structured reports.
            String selected = reportTypeCombo.getValue();
            // For now the controller provides a comprehensive financial report.
            String report = controller.generateFinancialReport();

            // Optionally include the selected type in the heading
            if (selected != null && !selected.isBlank() && !selected.equals("Financial Performance Summary")) {
                report = ("--- " + selected + " ---\n\n") + report;
            }

            reportArea.setText(report);
            showInfo("Report generated", "The report was generated successfully.");
        } catch (Exception ex) {
            showError("Generate report failed", ex.getMessage());
        }
    }

    /**
     * Save the current report text to a UTF-8 file chosen by the user.
     */
    private void exportReport() {
        Window window = getScene() != null ? getScene().getWindow() : null;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export Report");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        chooser.setInitialFileName("securetrust-report.txt");
        File file = chooser.showSaveDialog(window);

        if (file == null) return;

        try {
            Files.writeString(file.toPath(), reportArea.getText(), StandardCharsets.UTF_8);
            showInfo("Export complete", "Report saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            showError("Export failed", e.getMessage());
        }
    }

    /**
     * Print the report using the JavaFX printer job (simple text-node print).
     */
    private void printReport() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null) {
            showError("Print failed", "No available printers or printer job could not be created.");
            return;
        }

        boolean proceed = job.showPrintDialog(getScene() != null ? getScene().getWindow() : null);
        if (!proceed) {
            job.endJob();
            return;
        }

        // Use the text area node for printing; for nicer formatting consider rendering to a WebView or formatted node.
        boolean success = job.printPage(reportArea);
        if (success) {
            job.endJob();
            showInfo("Printed", "Report sent to printer.");
        } else {
            showError("Print failed", "Failed to print the report.");
        }
    }

    private void showInfo(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showError(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg != null ? msg : "Unknown error");
        a.showAndWait();
    }

    /**
     * A small sample fallback report (kept for compatibility if controller not available)
     */
    @SuppressWarnings("unused")
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

            (sample content...)
            """;
    }
}