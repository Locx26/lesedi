package com.bankingapp.controller;

import com.bankingapp.view.CustomerDashboardView;
import com.bankingapp.view.LoginView;
import com.bankingapp.service.UserService;
import com.bankingapp.service.AccountService;
import com.bankingapp.service.TransactionService;
import com.bankingapp.model.User;
import com.bankingapp.model.Account;
import com.bankingapp.model.Transaction;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;

/**
 * Customer Dashboard Controller - Handles customer dashboard events and operations
 */
public class CustomerDashboardController {
    private CustomerDashboardView view;
    private UserService userService;
    private AccountService accountService;
    private TransactionService transactionService;
    private Stage primaryStage;
    private User currentUser;
    private Account selectedAccount;

    public CustomerDashboardController(CustomerDashboardView view, UserService userService, Stage primaryStage) {
        this.view = view;
        this.userService = userService;
        this.accountService = new AccountService(userService);
        this.transactionService = new TransactionService(accountService);
        this.primaryStage = primaryStage;
        this.currentUser = userService.getCurrentUser();
       
        initializeController();
        attachEventHandlers();
        loadUserData();
    }

    private void initializeController() {
        // Set welcome message
        view.setWelcomeMessage(currentUser.getFullName());
    }

    private void attachEventHandlers() {
        // Account selection
        view.getAccountsList().getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> handleAccountSelection(newValue));

        // Transaction buttons
        view.getDepositButton().setOnAction(event -> handleDeposit());
        view.getWithdrawButton().setOnAction(event -> handleWithdraw());
        view.getTransferButton().setOnAction(event -> handleTransfer());
        view.getViewTransactionsButton().setOnAction(event -> handleViewTransactions());

        // Navigation buttons
        view.getRefreshButton().setOnAction(event -> refreshData());
        view.getLogoutButton().setOnAction(event -> handleLogout());
    }

    private void loadUserData() {
        try {
            // Load user accounts
            List<Account> accounts = accountService.getAccountsForUser(currentUser.getUserId());
            view.getAccountsList().getItems().clear();
           
            for (Account account : accounts) {
                String accountInfo = String.format("💰 %s ••••%s • %s",
                    account.getAccountType().getDisplayName(),
                    account.getAccountNumber().substring(account.getAccountNumber().length() - 4),
                    account.getFormattedBalance());
                view.getAccountsList().getItems().add(accountInfo);
            }

            // Select first account if available
            if (!accounts.isEmpty()) {
                view.getAccountsList().getSelectionModel().selectFirst();
            }

        } catch (Exception e) {
            view.showTransactionMessage("Error loading accounts: " + e.getMessage(), "error");
            e.printStackTrace();
        }
    }

    private void handleAccountSelection(String selectedAccountInfo) {
        if (selectedAccountInfo == null) {
            view.showAccountSelectionWarning();
            return;
        }

        try {
            // Extract account number from selection
            String accountNumber = extractAccountNumber(selectedAccountInfo);
           
            // Find the account object
            List<Account> accounts = accountService.getAccountsForUser(currentUser.getUserId());
            selectedAccount = accounts.stream()
                    .filter(acc -> acc.getAccountNumber().contains(accountNumber))
                    .findFirst()
                    .orElse(null);

            if (selectedAccount != null) {
                // Update UI with account details
                view.setBalance(selectedAccount.getFormattedBalance());
                view.setAccountInfo(selectedAccount.getAccountType().getDisplayName() + " • " + selectedAccount.getAccountNumber());
               
                // Load recent transactions
                loadRecentTransactions(selectedAccount.getAccountId());
            }

        } catch (Exception e) {
            view.showTransactionMessage("Error selecting account: " + e.getMessage(), "error");
            e.printStackTrace();
        }
    }

    private String extractAccountNumber(String accountInfo) {
        // Extract last 4 digits from account info string
        try {
            int startIndex = accountInfo.indexOf("••••") + 4;
            int endIndex = startIndex + 4;
            return accountInfo.substring(startIndex, endIndex);
        } catch (Exception e) {
            return "";
        }
    }

    private void handleDeposit() {
        if (selectedAccount == null) {
            view.showAccountSelectionWarning();
            return;
        }

        if (!view.validateAmount()) {
            return;
        }

        try {
            double amount = Double.parseDouble(view.getAmountField().getText().trim());
            boolean success = accountService.deposit(selectedAccount.getAccountId(), amount, "ATM Deposit");

            if (success) {
                view.showTransactionMessage("Deposit successful!", "success");
                view.clearAmountField();
                refreshAccountData();
            } else {
                view.showTransactionMessage("Deposit failed. Please try again.", "error");
            }

        } catch (Exception e) {
            view.showTransactionMessage("Error processing deposit: " + e.getMessage(), "error");
            e.printStackTrace();
        }
    }

    private void handleWithdraw() {
        if (selectedAccount == null) {
            view.showAccountSelectionWarning();
            return;
        }

        if (!view.validateAmount()) {
            return;
        }

        try {
            double amount = Double.parseDouble(view.getAmountField().getText().trim());
           
            // Check sufficient funds
            if (selectedAccount.getBalance() >= amount) {
                boolean success = accountService.withdraw(selectedAccount.getAccountId(), amount, "ATM Withdrawal");

                if (success) {
                    view.showTransactionMessage("Withdrawal successful!", "success");
                    view.clearAmountField();
                    refreshAccountData();
                } else {
                    view.showTransactionMessage("Withdrawal failed. Please try again.", "error");
                }
            } else {
                view.showTransactionMessage("Insufficient funds for withdrawal", "error");
            }

        } catch (Exception e) {
            view.showTransactionMessage("Error processing withdrawal: " + e.getMessage(), "error");
            e.printStackTrace();
        }
    }

    private void handleTransfer() {
        if (selectedAccount == null) {
            view.showAccountSelectionWarning();
            return;
        }

        if (!view.validateAmount()) {
            return;
        }

        // For now, show message about transfer functionality
        view.showTransactionMessage("Transfer functionality requires selection of target account. Please visit the full transfer section.", "warning");
       
        // In a complete implementation, this would open a transfer dialog
        // to select target account and complete transfer
    }

    private void handleViewTransactions() {
        if (selectedAccount == null) {
            view.showAccountSelectionWarning();
            return;
        }

        try {
            loadRecentTransactions(selectedAccount.getAccountId());
            view.showTransactionMessage("Transactions refreshed", "success");
        } catch (Exception e) {
            view.showTransactionMessage("Error loading transactions: " + e.getMessage(), "error");
            e.printStackTrace();
        }
    }

    private void loadRecentTransactions(String accountId) {
        try {
            List<Transaction> recentTransactions = transactionService.getRecentTransactions(accountId, 10);
            StringBuilder transactionsText = new StringBuilder();
           
            for (Transaction transaction : recentTransactions) {
                transactionsText.append(transaction.toString()).append("\n");
            }
           
            if (recentTransactions.isEmpty()) {
                transactionsText.append("No recent transactions");
            }
           
            view.updateTransactionHistory(transactionsText.toString());

        } catch (Exception e) {
            view.updateTransactionHistory("Error loading transaction history");
            e.printStackTrace();
        }
    }

    private void refreshData() {
        view.setLoading(true);
        try {
            loadUserData();
            if (selectedAccount != null) {
                refreshAccountData();
            }
            view.showTransactionMessage("Data refreshed successfully", "success");
        } catch (Exception e) {
            view.showTransactionMessage("Error refreshing data: " + e.getMessage(), "error");
        } finally {
            view.setLoading(false);
        }
    }

    private void refreshAccountData() {
        if (selectedAccount != null) {
            // Reload account data
            Account refreshedAccount = accountService.getAccount(selectedAccount.getAccountId());
            if (refreshedAccount != null) {
                selectedAccount = refreshedAccount;
                view.setBalance(selectedAccount.getFormattedBalance());
                loadRecentTransactions(selectedAccount.getAccountId());
            }
        }
    }

    private void handleLogout() {
        try {
            userService.logout();
           
            // Navigate back to login
            LoginView loginView = new LoginView();
            LoginController loginController = new LoginController(loginView, userService, primaryStage);
           
            Scene loginScene = new Scene(loginView.getView(), 900, 650);
            loginScene.getStylesheets().add(getClass().getResource("/com/bankingapp/styles/banking.css").toExternalForm());
           
            primaryStage.setTitle("SecureTrust Banking System - Login");
            primaryStage.setScene(loginScene);
            primaryStage.centerOnScreen();

        } catch (Exception e) {
            view.showTransactionMessage("Error during logout: " + e.getMessage(), "error");
            e.printStackTrace();
        }
    }
} 