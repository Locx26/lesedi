package com.bankingapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Login View - Boundary Class for User Authentication
 * Pure UI implementation with no business logic
 */
public class LoginView {
    private VBox view;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Label messageLabel;
    private ComboBox<String> userTypeComboBox;
    private Hyperlink forgotPasswordLink;
    private Hyperlink registerLink;

    public LoginView() {
        createView();
    }

    private void createView() {
        // Main container
        VBox mainContainer = new VBox(30);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(40));
        mainContainer.getStyleClass().add("card");
        mainContainer.setMaxWidth(400);

        // Header Section
        VBox headerSection = createHeaderSection();
       
        // Login Form
        VBox formSection = createFormSection();
       
        // Footer Links
        HBox footerSection = createFooterSection();

        mainContainer.getChildren().addAll(headerSection, formSection, footerSection);
        this.view = mainContainer;
    }

    private VBox createHeaderSection() {
        // Banking Icon (Text-based for simplicity)
        Text bankIcon = new Text("🏦");
        bankIcon.setFont(Font.font(36));
       
        // Title
        Label titleLabel = new Label("SecureTrust Banking");
        titleLabel.getStyleClass().add("header-primary");
        titleLabel.setAlignment(Pos.CENTER);
       
        // Subtitle
        Label subtitleLabel = new Label("Secure access to your finances");
        subtitleLabel.getStyleClass().add("body-text");
        subtitleLabel.setAlignment(Pos.CENTER);

        VBox headerSection = new VBox(10);
        headerSection.setAlignment(Pos.CENTER);
        headerSection.getChildren().addAll(bankIcon, titleLabel, subtitleLabel);
       
        return headerSection;
    }

    private VBox createFormSection() {
        // User type selection
        Label userTypeLabel = new Label("I am a:");
        userTypeLabel.getStyleClass().add("header-tertiary");
       
        userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("Customer", "Bank Teller", "Administrator");
        userTypeComboBox.setValue("Customer");
        userTypeComboBox.setPrefWidth(300);
        userTypeComboBox.getStyleClass().add("combo-box");

        // Username field
        Label usernameLabel = new Label("Username:");
        usernameLabel.getStyleClass().add("header-tertiary");
       
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefWidth(300);
        usernameField.getStyleClass().add("text-field");

        // Password field
        Label passwordLabel = new Label("Password:");
        passwordLabel.getStyleClass().add("header-tertiary");
       
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefWidth(300);
        passwordField.getStyleClass().add("password-field");

        // Login button
        loginButton = new Button("Sign In to Banking");
        loginButton.setPrefWidth(300);
        loginButton.getStyleClass().add("primary-button");

        // Message label
        messageLabel = new Label();
        messageLabel.setPrefWidth(300);
        messageLabel.setAlignment(Pos.CENTER);

        VBox formSection = new VBox(15);
        formSection.setAlignment(Pos.CENTER_LEFT);
        formSection.getChildren().addAll(
            userTypeLabel, userTypeComboBox,
            usernameLabel, usernameField,
            passwordLabel, passwordField,
            loginButton, messageLabel
        );
       
        return formSection;
    }

    private HBox createFooterSection() {
        forgotPasswordLink = new Hyperlink("Forgot Password?");
        registerLink = new Hyperlink("Open New Account");

        HBox footerSection = new HBox(20);
        footerSection.setAlignment(Pos.CENTER);
        footerSection.getChildren().addAll(forgotPasswordLink, registerLink);
       
        return footerSection;
    }

    // ===== PUBLIC API FOR CONTROLLERS =====
   
    public VBox getView() {
        return view;
    }
   
    public TextField getUsernameField() {
        return usernameField;
    }
   
    public PasswordField getPasswordField() {
        return passwordField;
    }
   
    public Button getLoginButton() {
        return loginButton;
    }
   
    public Label getMessageLabel() {
        return messageLabel;
    }
   
    public ComboBox<String> getUserTypeComboBox() {
        return userTypeComboBox;
    }
   
    public Hyperlink getForgotPasswordLink() {
        return forgotPasswordLink;
    }
   
    public Hyperlink getRegisterLink() {
        return registerLink;
    }

    // ===== VIEW UPDATE METHODS (No Business Logic) =====
   
    public void showSuccessMessage(String message) {
        messageLabel.getStyleClass().removeAll("error-message", "warning-message");
        messageLabel.getStyleClass().add("success-message");
        messageLabel.setText(message);
    }

    public void showErrorMessage(String message) {
        messageLabel.getStyleClass().removeAll("success-message", "warning-message");
        messageLabel.getStyleClass().add("error-message");
        messageLabel.setText(message);
    }

    public void showWarningMessage(String message) {
        messageLabel.getStyleClass().removeAll("success-message", "error-message");
        messageLabel.getStyleClass().add("warning-message");
        messageLabel.setText(message);
    }

    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");
        usernameField.requestFocus();
    }

    public void setLoadingState(boolean loading) {
        loginButton.setDisable(loading);
        if (loading) {
            loginButton.setText("Signing In...");
        } else {
            loginButton.setText("Sign In to Banking");
        }
    }

    public boolean validateInput() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty()) {
            showErrorMessage("Please enter your username");
            usernameField.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            showErrorMessage("Please enter your password");
            passwordField.requestFocus();
            return false;
        }

        if (password.length() < 3) { // Basic validation for demo
            showErrorMessage("Password must be at least 3 characters");
            passwordField.requestFocus();
            return false;
        }

        return true;
    }
}