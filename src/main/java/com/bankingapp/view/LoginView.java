package com.bankingapp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginView {
    private VBox view;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Label messageLabel;
    private ComboBox<String> userTypeComboBox;

    public LoginView() {
        createView();
    }

    private void createView() {
        // Title
        Label titleLabel = new Label("Banking System");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.DARKBLUE);

        // User type selection
        Label userTypeLabel = new Label("Login As:");
        userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("Customer", "Bank Teller");
        userTypeComboBox.setValue("Customer");

        // Username field
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setMaxWidth(200);

        // Password field
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setMaxWidth(200);

        // Login button
        loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        loginButton.setMaxWidth(200);

        // Message label
        messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);

        // Layout
        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.getChildren().addAll(
            titleLabel,
            new Separator(),
            userTypeLabel,
            userTypeComboBox,
            usernameLabel,
            usernameField,
            passwordLabel,
            passwordField,
            loginButton,
            messageLabel
        );

        formLayout.setPadding(new Insets(20));
        formLayout.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #cccccc; -fx-border-width: 1;");

        this.view = formLayout;
    }

    // Getters for controller to access these components
    public VBox getView() { return view; }
    public TextField getUsernameField() { return usernameField; }
    public PasswordField getPasswordField() { return passwordField; }
    public Button getLoginButton() { return loginButton; }
    public Label getMessageLabel() { return messageLabel; }
    public ComboBox<String> getUserTypeComboBox() { return userTypeComboBox; }

    // Methods to update the view (no business logic!)
    public void showSuccessMessage(String message) {
        messageLabel.setTextFill(Color.GREEN);
        messageLabel.setText(message);
    }

    public void showErrorMessage(String message) {
        messageLabel.setTextFill(Color.RED);
        messageLabel.setText(message);
    }

    public void clearFields() {
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");
    }
}