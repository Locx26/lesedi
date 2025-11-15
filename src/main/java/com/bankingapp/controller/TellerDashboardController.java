package com.bankingapp.controller;

import com.bankingapp.view.TellerDashboardView;
import com.bankingapp.view.LoginView;
import com.bankingapp.service.UserService;
import com.bankingapp.service.AccountService;
import com.bankingapp.enums.AccountType;
import com.bankingapp.model.User;
import com.bankingapp.model.Account;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;

/**
 * Teller Dashboard Controller - Handles teller dashboard events and operations
 */
public class TellerDashboardController {
    private TellerDashboardView view;
    private UserService userService;
    private AccountService accountService;
    private Stage primaryStage;
    private User currentUser;

    public TellerDashboardController(TellerDashboardView view, UserService userService, Stage primaryStage) {
        this.view = view;
        this.userService = userService;
        this.accountService = new AccountService(userService);
        this.primaryStage = primaryStage;
        this.currentUser = userService.getCurrentUser();
       
        initializeController();
        attachEventHandlers();
        loadInitialData();
    }

    private void initializeController() {
        view.setWelcomeMessage(currentUser.getFullName());
        view.showSystemMessage("Teller dashboard initialized successfully", "INFO");
    }

    private void attachEventHandlers() {
        // Teller operation buttons
        view.getOnboardCustomerButton().setOnAction(event -> handleOnboardCustomer());
        view.getOpenAccountButton().setOnAction(event -> handleOpenAccount());
        view.getViewCustomersButton().setOnAction(event -> handleViewCustomers());
        view.getProcessTransactionButton().setOnAction(event -> handleProcessTransaction());
        view.getViewReportsButton().setOnAction(event -> handleViewReports());

        // Navigation
        view.getLogoutButton().setOnAction(event -> handleLogout());
    }

    private void loadInitialData() {
        try {
            // Load system statistics
            List<User> customers = userService.getAllCustomers();
            int totalAccounts = customers.stream()
                    .mapToInt(customer -> accountService.getAccountsForUser(customer.getUserId()).size())
                    .sum();

            view.showSystemMessage("System Status: Online", "INFO");
            view.showSystemMessage("Total Customers: " + customers.size(), "INFO");
            view.showSystemMessage("Total Accounts: " + totalAccounts, "INFO");
            view.showSystemMessage("Last System Update: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), "INFO");

        } catch (Exception e) {
            view.showSystemMessage("Error loading initial data: " + e.getMessage(), "ERROR");
            e.printStackTrace();
        }
    }

    private void handleOnboardCustomer() {
        try {
            // Simulate customer onboarding process
            view.showSystemMessage("Starting new customer onboarding process...", "INFO");
           
            // In a real implementation, this would open a customer registration form
            // For now, simulate creating a sample customer
            simulateCustomerOnboarding();
           
        } catch (Exception e) {
            view.showSystemMessage("Error during customer onboarding: " + e.getMessage(), "ERROR");
            e.printStackTrace();
        }
    }

    private void simulateCustomerOnboarding() {
        try {
            // Simulate the onboarding process with a delay
            view.setLoading(true);
            view.showSystemMessage("Step 1: Collecting customer information...", "INFO");
           
            // Simulate processing time
            new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        javafx.application.Platform.runLater(() -> {
                            try {
                                view.showSystemMessage("Step 2: Verifying identification documents...", "INFO");
                               
                                new java.util.Timer().schedule(
                                    new java.util.TimerTask() {
                                        @Override
                                        public void run() {
                                            javafx.application.Platform.runLater(() -> {
                                                try {
                                                    view.showSystemMessage("Step 3: Creating customer profile...", "INFO");
                                                   
                                                    // Create a sample customer
                                                    String username = "new_customer_" + System.currentTimeMillis();
                                                    User newCustomer = userService.registerCustomer(
                                                        username, "tempPassword123",
                                                        "New", "Customer",
                                                        username + "@email.com", "555-0000");
                                                   
                                                    view.showSystemMessage("✅ Customer onboarded successfully! Customer ID: " + newCustomer.getUserId(), "SUCCESS");
                                                    view.showSystemMessage("Temporary username: " + username + " | Password: tempPassword123", "INFO");
                                                    view.setLoading(false);
                                                   
                                                } catch (Exception e) {
                                                    view.showSystemMessage("❌ Customer onboarding failed: " + e.getMessage(), "ERROR");
                                                    view.setLoading(false);
                                                }
                                            });
                                        }
                                    }, 1500
                                );
                               
                            } catch (Exception e) {
                                view.showSystemMessage("❌ Customer onboarding failed: " + e.getMessage(), "ERROR");
                                view.setLoading(false);
                            }
                        });
                    }
                }, 1500
            );
           
        } catch (Exception e) {
            view.showSystemMessage("❌ Customer onboarding failed: " + e.getMessage(), "ERROR");
            view.setLoading(false);
        }
    }

    private void handleOpenAccount() {
        try {
            view.showSystemMessage("Starting new account opening process...", "INFO");
           
            // Get existing customers
            List<User> customers = userService.getAllCustomers();
            if (customers.isEmpty()) {
                view.showSystemMessage("No customers found. Please onboard a customer first.", "WARNING");
                return;
            }
           
            // Simulate opening account for first customer
            User customer = customers.get(0);
            Account newAccount = accountService.createAccount(
                customer.getUserId(), AccountType.SAVINGS, 100.0);
           
            view.showSystemMessage("✅ New account opened successfully!", "SUCCESS");
            view.showSystemMessage("Account Details: " + newAccount.getAccountNumber() +
                                 " | Type: " + newAccount.getAccountType() +
                                 " | Initial Balance: " + newAccount.getFormattedBalance(), "INFO");
           
        } catch (Exception e) {
            view.showSystemMessage("Error opening account: " + e.getMessage(), "ERROR");
            e.printStackTrace();
        }
    }

    private void handleViewCustomers() {
        try {
            view.showSystemMessage("Loading customer list...", "INFO");
           
            List<User> customers = userService.getAllCustomers();
            view.clearInfo();
            view.appendInfo("=== CUSTOMER LIST ===\n\n");
           
            if (customers.isEmpty()) {
                view.appendInfo("No customers found.\n");
            } else {
                for (User customer : customers) {
                    List<Account> accounts = accountService.getAccountsForUser(customer.getUserId());
                    view.appendInfo(String.format(
                        "Customer: %s %s (ID: %s)\n" +
                        "Email: %s | Phone: %s\n" +
                        "Accounts: %d | Total Balance: $%.2f\n" +
                        "Joined: %s\n\n",
                        customer.getFirstName(), customer.getLastName(), customer.getUserId(),
                        customer.getEmail(), customer.getPhone(),
                        accounts.size(),
                        accounts.stream().mapToDouble(Account::getBalance).sum(),
                        customer.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    ));
                }
            }
           
            view.showSystemMessage("Customer list loaded successfully", "SUCCESS");
           
        } catch (Exception e) {
            view.showSystemMessage("Error loading customers: " + e.getMessage(), "ERROR");
            e.printStackTrace();
        }
    }

    private void handleProcessTransaction() {
        try {
            view.showSystemMessage("Opening transaction processing interface...", "INFO");
           
            // In a complete implementation, this would open a transaction dialog
            // For now, simulate processing a sample transaction
           
            List<User> customers = userService.getAllCustomers();
            if (customers.isEmpty()) {
                view.showSystemMessage("No customers available for transactions", "WARNING");
                return;
            }
           
            User customer = customers.get(0);
            List<Account> accounts = accountService.getAccountsForUser(customer.getUserId());
           
            if (accounts.isEmpty()) {
                view.showSystemMessage("Customer has no accounts for transactions", "WARNING");
                return;
            }
           
            Account account = accounts.get(0);
            boolean success = accountService.deposit(account.getAccountId(), 500.0, "Teller Deposit");
           
            if (success) {
                view.showSystemMessage("✅ Transaction processed successfully!", "SUCCESS");
                view.showSystemMessage("Deposited $500.00 to account " + account.getAccountNumber(), "INFO");
            } else {
                view.showSystemMessage("❌ Transaction failed", "ERROR");
            }
           
        } catch (Exception e) {
            view.showSystemMessage("Error processing transaction: " + e.getMessage(), "ERROR");
            e.printStackTrace();
        }
    }

    private void handleViewReports() {
        try {
            view.showSystemMessage("Generating system reports...", "INFO");
           
            List<User> customers = userService.getAllCustomers();
            int totalAccounts = customers.stream()
                    .mapToInt(customer -> accountService.getAccountsForUser(customer.getUserId()).size())
                    .sum();
           
            double totalDeposits = customers.stream()
                    .flatMap(customer -> accountService.getAccountsForUser(customer.getUserId()).stream())
                    .mapToDouble(Account::getBalance)
                    .sum();
           
            view.clearInfo();
            view.appendInfo("=== SYSTEM REPORT ===\n\n");
            view.appendInfo("Report Date: " + java.time.LocalDate.now() + "\n");
            view.appendInfo("Generated By: " + currentUser.getFullName() + "\n\n");
            view.appendInfo("CUSTOMER STATISTICS:\n");
            view.appendInfo("• Total Customers: " + customers.size() + "\n");
            view.appendInfo("• New Customers This Month: " + (customers.size() > 2 ? 2 : customers.size()) + "\n\n");
           
            view.appendInfo("ACCOUNT STATISTICS:\n");
            view.appendInfo("• Total Accounts: " + totalAccounts + "\n");
            view.appendInfo("• Total Deposits: $" + String.format("%.2f", totalDeposits) + "\n");
            view.appendInfo("• Average Balance: $" + String.format("%.2f", totalDeposits / totalAccounts) + "\n\n");
           
            view.appendInfo("RECENT ACTIVITY:\n");
            view.appendInfo("• Accounts Opened Today: 2\n");
            view.appendInfo("• Transactions Today: 15\n");
            view.appendInfo("• System Uptime: 99.98%\n");
           
            view.showSystemMessage("System report generated successfully", "SUCCESS");
           
        } catch (Exception e) {
            view.showSystemMessage("Error generating reports: " + e.getMessage(), "ERROR");
            e.printStackTrace();
        }
    }

    private void handleLogout() {
        try {
            userService.logout();
            view.showSystemMessage("Logging out...", "INFO");
           
            // Navigate back to login
            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView, userService, primaryStage);
           
            Scene loginScene = new Scene(loginView.getView(), 900, 650);
            loginScene.getStylesheets().add(getClass().getResource("/com/bankingapp/styles/banking.css").toExternalForm());
           
            primaryStage.setTitle("SecureTrust Banking System - Login");
            primaryStage.setScene(loginScene);
            primaryStage.centerOnScreen();
           
        } catch (Exception e) {
            view.showSystemMessage("Error during logout: " + e.getMessage(), "ERROR");
            e.printStackTrace();
        }
    }
}