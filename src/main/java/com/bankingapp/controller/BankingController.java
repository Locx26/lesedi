package com.bankingapp.controller;

import com.bankingapp.model.*;
import com.bankingapp.dao.CustomerDAO;
import com.bankingapp.dao.AccountDAO;
import java.util.List;
import java.util.ArrayList;

/**
 * Professional Banking Controller
 * Handles all business logic and coordinates between GUI and data layers
 */
public class BankingController {
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
   
    public BankingController() {
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
    }

    // ==================== CUSTOMER MANAGEMENT ====================

    /**
     * Register a new customer with the bank
     */
    public String registerCustomer(String firstName, String surname, String address,
                                 String phoneNumber, String email) {
        try {
            // Validate input parameters
            if (firstName == null || firstName.trim().isEmpty()) {
                return "Error: First name is required";
            }
            if (surname == null || surname.trim().isEmpty()) {
                return "Error: Surname is required";
            }
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                return "Error: Phone number is required";
            }

            // Generate unique customer ID
            String customerId = generateCustomerId();
           
            // Create new customer object
            Customer customer = new Customer(customerId, firstName.trim(), surname.trim(),
                                           address != null ? address.trim() : "",
                                           phoneNumber.trim(),
                                           email != null ? email.trim() : "");
           
            // Save to database
            boolean success = customerDAO.saveCustomer(customer);
           
            if (success) {
                return String.format("""
                    ✅ Customer registered successfully!
                   
                    Customer Details:
                    • ID: %s
                    • Name: %s %s
                    • Phone: %s
                    • Email: %s
                    • Address: %s
                    """, customerId, firstName, surname, phoneNumber, email, address);
            } else {
                return "❌ Failed to register customer. Please try again.";
            }
           
        } catch (Exception e) {
            return "❌ Error registering customer: " + e.getMessage();
        }
    }

    /**
     * Retrieve all customers from the database
     */
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    /**
     * Get all accounts for display in GUI
     */
    public List<Account> getAllAccounts() {
        try {
            return accountDAO.getAllAccounts();
        } catch (Exception e) {
            System.err.println("Error retrieving accounts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Find customer by ID
     */
    public Customer findCustomerById(String customerId) {
        return customerDAO.findCustomerById(customerId);
    }

    /**
     * Update customer information
     */
    public String updateCustomer(String customerId, String firstName, String surname,
                               String address, String phoneNumber, String email) {
        try {
            Customer customer = customerDAO.findCustomerById(customerId);
            if (customer == null) {
                return "❌ Customer not found";
            }

            // Update customer details
            customer.setFirstName(firstName);
            customer.setSurname(surname);
            customer.setAddress(address);
            customer.setPhoneNumber(phoneNumber);
            customer.setEmail(email);

            boolean success = customerDAO.updateCustomer(customer);
            return success ? "✅ Customer updated successfully" : "❌ Failed to update customer";
           
        } catch (Exception e) {
            return "❌ Error updating customer: " + e.getMessage();
        }
    }

    // ==================== ACCOUNT MANAGEMENT ====================

    /**
     * Open a new savings account for a customer
     */
    public String openSavingsAccount(String customerId, double initialDeposit, String branch) {
        try {
            Customer customer = customerDAO.findCustomerById(customerId);
            if (customer == null) {
                return "❌ Customer not found";
            }

            if (initialDeposit < 0) {
                return "❌ Initial deposit cannot be negative";
            }

            String accountNumber = generateAccountNumber();
            SavingsAccount account = new SavingsAccount(accountNumber, initialDeposit, branch, customer);
           
            boolean success = accountDAO.saveAccount(account);
            if (success) {
                customer.openAccount(account);
                customerDAO.updateCustomer(customer);
               
                return String.format("""
                    ✅ Savings Account opened successfully!
                   
                    Account Details:
                    • Account Number: %s
                    • Customer: %s %s
                    • Initial Deposit: BWP %.2f
                    • Branch: %s
                    • Interest Rate: 0.05%% monthly
                    """, accountNumber, customer.getFirstName(), customer.getSurname(),
                    initialDeposit, branch);
            } else {
                return "❌ Failed to open savings account";
            }
           
        } catch (Exception e) {
            return "❌ Error opening savings account: " + e.getMessage();
        }
    }

    /**
     * Open a new investment account for a customer
     */
    public String openInvestmentAccount(String customerId, double initialDeposit, String branch) {
        try {
            Customer customer = customerDAO.findCustomerById(customerId);
            if (customer == null) {
                return "❌ Customer not found";
            }

            if (initialDeposit < 500.00) {
                return "❌ Investment account requires minimum BWP 500.00 opening balance";
            }

            String accountNumber = generateAccountNumber();
            InvestmentAccount account = new InvestmentAccount(accountNumber, initialDeposit, branch, customer);
           
            boolean success = accountDAO.saveAccount(account);
            if (success) {
                customer.openAccount(account);
                customerDAO.updateCustomer(customer);
               
                return String.format("""
                    ✅ Investment Account opened successfully!
                   
                    Account Details:
                    • Account Number: %s
                    • Customer: %s %s
                    • Initial Deposit: BWP %.2f
                    • Branch: %s
                    • Interest Rate: 5%% monthly
                    • Minimum Balance: BWP 500.00
                    """, accountNumber, customer.getFirstName(), customer.getSurname(),
                    initialDeposit, branch);
            } else {
                return "❌ Failed to open investment account";
            }
           
        } catch (Exception e) {
            return "❌ Error opening investment account: " + e.getMessage();
        }
    }

    /**
     * Open a new cheque account for a customer
     */
    public String openChequeAccount(String customerId, double initialDeposit, String branch,
                                  String employer, String employerAddress) {
        try {
            Customer customer = customerDAO.findCustomerById(customerId);
            if (customer == null) {
                return "❌ Customer not found";
            }

            if (employer == null || employer.trim().isEmpty()) {
                return "❌ Employer information is required for cheque accounts";
            }

            String accountNumber = generateAccountNumber();
            ChequeAccount account = new ChequeAccount(accountNumber, initialDeposit, branch,
                                                    customer, employer.trim(),
                                                    employerAddress != null ? employerAddress.trim() : "");
           
            boolean success = accountDAO.saveAccount(account);
            if (success) {
                customer.openAccount(account);
                customerDAO.updateCustomer(customer);
               
                return String.format("""
                    ✅ Cheque Account opened successfully!
                   
                    Account Details:
                    • Account Number: %s
                    • Customer: %s %s
                    • Initial Deposit: BWP %.2f
                    • Branch: %s
                    • Employer: %s
                    • No interest earned
                    """, accountNumber, customer.getFirstName(), customer.getSurname(),
                    initialDeposit, branch, employer);
            } else {
                return "❌ Failed to open cheque account";
            }
           
        } catch (Exception e) {
            return "❌ Error opening cheque account: " + e.getMessage();
        }
    }

    /**
     * Retrieve all accounts for a customer
     */
    public List<Account> getCustomerAccounts(String customerId) {
        return accountDAO.findAccountsByCustomerId(customerId);
    }

    /**
     * Get account by account number
     */
    public Account getAccountByNumber(String accountNumber) {
        return accountDAO.findAccountByNumber(accountNumber);
    }

    // ==================== TRANSACTION PROCESSING ====================

    /**
     * Process deposit transaction
     */
    public String processDeposit(String accountNumber, double amount) {
        try {
            if (amount <= 0) {
                return "❌ Deposit amount must be greater than zero";
            }

            Account account = accountDAO.findAccountByNumber(accountNumber);
            if (account == null) {
                return "❌ Account not found";
            }

            if (!account.canDeposit()) {
                return "❌ This account type does not allow deposits";
            }

            // Process deposit
            account.deposit(amount);
            boolean success = accountDAO.updateAccount(account);
           
            if (success) {
                return String.format("""
                    ✅ Deposit processed successfully!
                   
                    Transaction Details:
                    • Account: %s
                    • Amount: BWP %.2f
                    • New Balance: BWP %.2f
                    • Type: %s
                    """, accountNumber, amount, account.getBalance(),
                    account.getAccountType().getDescription());
            } else {
                return "❌ Failed to process deposit";
            }
           
        } catch (Exception e) {
            return "❌ Error processing deposit: " + e.getMessage();
        }
    }

    /**
     * Process withdrawal transaction
     */
    public String processWithdrawal(String accountNumber, double amount) {
        try {
            if (amount <= 0) {
                return "❌ Withdrawal amount must be greater than zero";
            }

            Account account = accountDAO.findAccountByNumber(accountNumber);
            if (account == null) {
                return "❌ Account not found";
            }

            if (!account.canWithdraw()) {
                return "❌ This account type does not allow withdrawals";
            }

            if (account.getBalance() < amount) {
                return "❌ Insufficient funds for withdrawal";
            }

            // Process withdrawal
            boolean withdrawalSuccess = account.withdraw(amount);
            if (!withdrawalSuccess) {
                return "❌ Withdrawal failed. Please check account restrictions.";
            }

            boolean updateSuccess = accountDAO.updateAccount(account);
           
            if (updateSuccess) {
                return String.format("""
                    ✅ Withdrawal processed successfully!
                   
                    Transaction Details:
                    • Account: %s
                    • Amount: BWP %.2f
                    • New Balance: BWP %.2f
                    • Type: %s
                    """, accountNumber, amount, account.getBalance(),
                    account.getAccountType().getDescription());
            } else {
                return "❌ Failed to process withdrawal";
            }
           
        } catch (Exception e) {
            return "❌ Error processing withdrawal: " + e.getMessage();
        }
    }

    /**
     * Transfer funds between accounts
     */
    public String transferFunds(String fromAccountNumber, String toAccountNumber, double amount) {
        try {
            if (amount <= 0) {
                return "❌ Transfer amount must be greater than zero";
            }

            Account fromAccount = accountDAO.findAccountByNumber(fromAccountNumber);
            Account toAccount = accountDAO.findAccountByNumber(toAccountNumber);

            if (fromAccount == null) {
                return "❌ Source account not found";
            }
            if (toAccount == null) {
                return "❌ Destination account not found";
            }
            if (fromAccount.getBalance() < amount) {
                return "❌ Insufficient funds for transfer";
            }
            if (!fromAccount.canWithdraw()) {
                return "❌ Source account does not allow withdrawals";
            }
            if (!toAccount.canDeposit()) {
                return "❌ Destination account does not allow deposits";
            }

            // Process transfer
            boolean withdrawSuccess = fromAccount.withdraw(amount);
            if (!withdrawSuccess) {
                return "❌ Withdrawal from source account failed";
            }

            toAccount.deposit(amount);

            boolean updateFromSuccess = accountDAO.updateAccount(fromAccount);
            boolean updateToSuccess = accountDAO.updateAccount(toAccount);

            if (updateFromSuccess && updateToSuccess) {
                return String.format("""
                    ✅ Transfer completed successfully!
                   
                    Transfer Details:
                    • From Account: %s
                    • To Account: %s
                    • Amount: BWP %.2f
                    • New Source Balance: BWP %.2f
                    • New Destination Balance: BWP %.2f
                    """, fromAccountNumber, toAccountNumber, amount,
                    fromAccount.getBalance(), toAccount.getBalance());
            } else {
                return "❌ Transfer completed but balance update failed";
            }
           
        } catch (Exception e) {
            return "❌ Error processing transfer: " + e.getMessage();
        }
    }

    // ==================== INTEREST CALCULATION ====================

    /**
     * Apply monthly interest to an account
     */
    public String applyMonthlyInterest(String accountNumber) {
        try {
            Account account = accountDAO.findAccountByNumber(accountNumber);
            if (account == null) {
                return "❌ Account not found";
            }

            double oldBalance = account.getBalance();
            account.applyMonthlyInterest();
            double interestEarned = account.getBalance() - oldBalance;

            boolean success = accountDAO.updateAccount(account);
           
            if (success) {
                return String.format("""
                    ✅ Interest applied successfully!
                   
                    Interest Details:
                    • Account: %s
                    • Type: %s
                    • Interest Earned: BWP %.2f
                    • New Balance: BWP %.2f
                    """, accountNumber, account.getAccountType().getDescription(),
                    interestEarned, account.getBalance());
            } else {
                return "❌ Failed to apply interest";
            }
           
        } catch (Exception e) {
            return "❌ Error applying interest: " + e.getMessage();
        }
    }

    /**
     * Apply monthly interest to all eligible accounts
     */
    public String applyInterestToAllAccounts() {
        try {
            List<Account> allAccounts = accountDAO.getAllAccounts();
            int processedCount = 0;
            double totalInterest = 0;

            for (Account account : allAccounts) {
                double oldBalance = account.getBalance();
                account.applyMonthlyInterest();
                double interestEarned = account.getBalance() - oldBalance;
               
                if (interestEarned > 0) {
                    accountDAO.updateAccount(account);
                    processedCount++;
                    totalInterest += interestEarned;
                }
            }

            return String.format("""
                ✅ Bulk interest processing completed!
               
                Processing Summary:
                • Accounts Processed: %d
                • Total Interest Applied: BWP %.2f
                • Total Accounts: %d
                """, processedCount, totalInterest, allAccounts.size());
           
        } catch (Exception e) {
            return "❌ Error processing bulk interest: " + e.getMessage();
        }
    }

    // ==================== ANALYTICS AND REPORTING ====================

    /**
     * Get banking system statistics
     */
    public SystemStatistics getSystemStatistics() {
        List<Customer> allCustomers = customerDAO.getAllCustomers();
        List<Account> allAccounts = accountDAO.getAllAccounts();
       
        long savingsCount = allAccounts.stream()
            .filter(acc -> acc.getAccountType() == AccountType.SAVINGS)
            .count();
           
        long investmentCount = allAccounts.stream()
            .filter(acc -> acc.getAccountType() == AccountType.INVESTMENT)
            .count();
           
        long chequeCount = allAccounts.stream()
            .filter(acc -> acc.getAccountType() == AccountType.CHEQUE)
            .count();
           
        double totalAssets = allAccounts.stream()
            .mapToDouble(Account::getBalance)
            .sum();

        return new SystemStatistics(
            allCustomers.size(),
            allAccounts.size(),
            savingsCount,
            investmentCount,
            chequeCount,
            totalAssets
        );
    }

    /**
     * Generate comprehensive financial report
     */
    public String generateFinancialReport() {
        SystemStatistics stats = getSystemStatistics();
       
        int totalAccounts = stats.getTotalAccounts();
        int totalCustomers = stats.getTotalCustomers();
        double totalAssets = stats.getTotalAssets();

        // Avoid divide-by-zero when there are no accounts or customers
        double savingsPct = totalAccounts > 0 ? (stats.getSavingsCount() * 100.0 / totalAccounts) : 0.0;
        double investmentPct = totalAccounts > 0 ? (stats.getInvestmentCount() * 100.0 / totalAccounts) : 0.0;
        double chequePct = totalAccounts > 0 ? (stats.getChequeCount() * 100.0 / totalAccounts) : 0.0;
        double avgBalance = totalAccounts > 0 ? (totalAssets / totalAccounts) : 0.0;
        double avgAccountsPerCustomer = totalCustomers > 0 ? ((double) totalAccounts / totalCustomers) : 0.0;

        return String.format("""
            ==================================================
                        SECURETRUST BANKING SYSTEM
                       FINANCIAL PERFORMANCE REPORT
                       Generated: %s
            ==================================================

            EXECUTIVE SUMMARY
            • Total Customers: %d
            • Total Accounts: %d
            • Total Assets: BWP %,.2f

            ACCOUNT DISTRIBUTION
            • Savings Accounts: %d (%.1f%%)
            • Investment Accounts: %d (%.1f%%)
            • Cheque Accounts: %d (%.1f%%)

            ASSET DISTRIBUTION
            • Average Balance per Account: BWP %,.2f
            • Average Accounts per Customer: %.1f

            RECOMMENDATIONS
            1. Focus on growing investment accounts for higher returns
            2. Consider promotional rates for savings accounts
            3. Monitor cheque account growth for business development

            ==================================================
            """,
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            stats.getTotalCustomers(),
            stats.getTotalAccounts(),
            totalAssets,
            stats.getSavingsCount(),
            savingsPct,
            stats.getInvestmentCount(),
            investmentPct,
            stats.getChequeCount(),
            chequePct,
            avgBalance,
            avgAccountsPerCustomer
        );
    }

    // ==================== UTILITY METHODS ====================

    private String generateCustomerId() {
        return "CUST" + System.currentTimeMillis();
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }

    // ==================== INNER STATISTICS CLASS ====================

    /**
     * Inner class for system statistics
     */
    public static class SystemStatistics {
        private final int totalCustomers;
        private final int totalAccounts;
        private final long savingsCount;
        private final long investmentCount;
        private final long chequeCount;
        private final double totalAssets;

        public SystemStatistics(int totalCustomers, int totalAccounts,
                              long savingsCount, long investmentCount,
                              long chequeCount, double totalAssets) {
            this.totalCustomers = totalCustomers;
            this.totalAccounts = totalAccounts;
            this.savingsCount = savingsCount;
            this.investmentCount = investmentCount;
            this.chequeCount = chequeCount;
            this.totalAssets = totalAssets;
        }

        // Getters
        public int getTotalCustomers() { return totalCustomers; }
        public int getTotalAccounts() { return totalAccounts; }
        public long getSavingsCount() { return savingsCount; }
        public long getInvestmentCount() { return investmentCount; }
        public long getChequeCount() { return chequeCount; }
        public double getTotalAssets() { return totalAssets; }
    }
}