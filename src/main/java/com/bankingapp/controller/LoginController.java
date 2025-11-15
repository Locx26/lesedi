package com.bankingapp.controller;

import com.bankingapp.view.LoginView;
import com.bankingapp.view.CustomerDashboardView;
import com.bankingapp.view.TellerDashboardView;
import com.bankingapp.service.UserService;
import com.bankingapp.enums.UserRole;
import com.bankingapp.model.User;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Login Controller - Handles login view events and authentication
 */
public class LoginController {
    private LoginView loginView;
    private UserService userService;
    private Stage primaryStage;

    public LoginController(LoginView loginView, UserService userService, Stage primaryStage) {
        this.loginView = loginView;
        this.userService = userService;
        this.primaryStage = primaryStage;
        attachEventHandlers();
    }

    private void attachEventHandlers() {
        // Login button event handler
        loginView.getLoginButton().setOnAction(event -> handleLogin());

        // Enter key support for login
        loginView.getPasswordField().setOnAction(event -> handleLogin());

        // Forgot password link
        loginView.getForgotPasswordLink().setOnAction(event -> handleForgotPassword());

        // Register link
        loginView.getRegisterLink().setOnAction(event -> handleRegister());
    }

    private void handleLogin() {
        try {
            // Validate input
            if (!loginView.validateInput()) {
                return;
            }

            // Show loading state
            loginView.setLoadingState(true);

            // Get input values
            String username = loginView.getUsernameField().getText().trim();
            String password = loginView.getPasswordField().getText();
            UserRole userRole = determineUserRole(loginView.getUserTypeComboBox().getValue());

            // Authenticate user
            boolean isAuthenticated = userService.authenticate(username, password, userRole);

            if (isAuthenticated) {
                User currentUser = userService.getCurrentUser();
                loginView.showSuccessMessage("Welcome back, " + currentUser.getFirstName() + "!");

                // Navigate to appropriate dashboard based on user role
                switch (userRole) {
                    case CUSTOMER:
                        navigateToCustomerDashboard(currentUser);
                        break;
                    case BANK_TELLER:
                    case ADMIN:
                        navigateToTellerDashboard(currentUser);
                        break;
                    default:
                        loginView.showErrorMessage("Unsupported user role");
                }
            } else {
                loginView.showErrorMessage("Invalid username, password, or user role selection");
            }

        } catch (Exception e) {
            loginView.showErrorMessage("Login failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            loginView.setLoadingState(false);
        }
    }

    private UserRole determineUserRole(String selectedRole) {
        switch (selectedRole) {
            case "Customer":
                return UserRole.CUSTOMER;
            case "Bank Teller":
                return UserRole.BANK_TELLER;
            case "Administrator":
                return UserRole.ADMIN;
            default:
                return UserRole.CUSTOMER;
        }
    }

    private void navigateToCustomerDashboard(User user) {
        try {
            CustomerDashboardView customerView = new CustomerDashboardView();
            CustomerDashboardController customerController = new CustomerDashboardController(
                customerView, userService, primaryStage);
           
            Scene customerScene = new Scene(customerView.getView(), 1200, 800);
            customerScene.getStylesheets().add(getClass().getResource("/com/bankingapp/styles/banking.css").toExternalForm());
           
            primaryStage.setTitle("SecureTrust Banking - Customer Dashboard");
            primaryStage.setScene(customerScene);
            primaryStage.centerOnScreen();

        } catch (Exception e) {
            loginView.showErrorMessage("Failed to load customer dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void navigateToTellerDashboard(User user) {
        try {
            TellerDashboardView tellerView = new TellerDashboardView();
            TellerDashboardController tellerController = new TellerDashboardController(
                tellerView, userService, primaryStage);
           
            Scene tellerScene = new Scene(tellerView.getView(), 1200, 800);
            tellerScene.getStylesheets().add(getClass().getResource("/com/bankingapp/styles/banking.css").toExternalForm());
           
            primaryStage.setTitle("SecureTrust Banking - Teller Dashboard");
            primaryStage.setScene(tellerScene);
            primaryStage.centerOnScreen();

        } catch (Exception e) {
            loginView.showErrorMessage("Failed to load teller dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleForgotPassword() {
        loginView.showWarningMessage("Please contact customer support at 1-800-BANK-HELP to reset your password.");
    }

    private void handleRegister() {
        loginView.showWarningMessage("Please visit your local branch or call 1-800-OPEN-ACCOUNT to open a new account.");
    }
}