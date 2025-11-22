package com.bankingapp.dao;

import com.bankingapp.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Professional Data Access Object for Account operations
 * Handles all database interactions for accounts and transactions
 */
public class AccountDAO {
   
    /**
     * Save a new account to the database
     */
    public boolean saveAccount(Account account) {
        String sql = "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES (?, ?, ?, ?, ?, ?, ?)";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setString(2, account.getCustomer().getCustomerId());
            pstmt.setString(3, account.getAccountType().name());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setString(5, account.getBranch());
           
            // Handle employer information for cheque accounts
            if (account instanceof ChequeAccount) {
                ChequeAccount chequeAccount = (ChequeAccount) account;
                pstmt.setString(6, chequeAccount.getEmployer());
                pstmt.setString(7, chequeAccount.getEmployerAddress());
            } else {
                pstmt.setNull(6, Types.VARCHAR);
                pstmt.setNull(7, Types.VARCHAR);
            }
           
            int rowsAffected = pstmt.executeUpdate();
           
            // Record initial transaction
            if (rowsAffected > 0 && account.getBalance() > 0) {
                recordTransaction(account.getAccountNumber(), "DEPOSIT", account.getBalance(),
                                account.getBalance(), "Initial account opening deposit");
            }
           
            return rowsAffected > 0;
           
        } catch (SQLException e) {
            System.err.println("❌ Error saving account: " + e.getMessage());
            return false;
        }
    }
   
    /**
     * Update account information (primarily balance)
     */
    public boolean updateAccount(Account account) {
        String sql = "UPDATE accounts SET balance = ?, branch = ?, employer = ?, employer_address = ? WHERE account_number = ?";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
            pstmt.setDouble(1, account.getBalance());
            pstmt.setString(2, account.getBranch());
           
            // Handle employer information for cheque accounts
            if (account instanceof ChequeAccount) {
                ChequeAccount chequeAccount = (ChequeAccount) account;
                pstmt.setString(3, chequeAccount.getEmployer());
                pstmt.setString(4, chequeAccount.getEmployerAddress());
            } else {
                pstmt.setNull(3, Types.VARCHAR);
                pstmt.setNull(4, Types.VARCHAR);
            }
           
            pstmt.setString(5, account.getAccountNumber());
           
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
           
        } catch (SQLException e) {
            System.err.println("❌ Error updating account: " + e.getMessage());
            return false;
        }
    }
   
    /**
     * Find account by account number
     */
    public Account findAccountByNumber(String accountNumber) {
        String sql = "SELECT a.*, c.customer_id, c.first_name, c.surname, c.address, c.phone_number, c.email " +
                    "FROM accounts a JOIN customers c ON a.customer_id = c.customer_id " +
                    "WHERE a.account_number = ?";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
           
            if (rs.next()) {
                return extractAccountWithCustomerFromResultSet(rs);
            }
           
        } catch (SQLException e) {
            System.err.println("❌ Error finding account: " + e.getMessage());
        }
       
        return null;
    }
   
    /**
     * Find all accounts for a specific customer
     */
    public List<Account> findAccountsByCustomerId(String customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT a.*, c.customer_id, c.first_name, c.surname, c.address, c.phone_number, c.email " +
                    "FROM accounts a JOIN customers c ON a.customer_id = c.customer_id " +
                    "WHERE a.customer_id = ? ORDER BY a.date_opened";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
           
            while (rs.next()) {
                Account account = extractAccountWithCustomerFromResultSet(rs);
                if (account != null) {
                    accounts.add(account);
                }
            }
           
        } catch (SQLException e) {
            System.err.println("❌ Error finding customer accounts: " + e.getMessage());
        }
       
        return accounts;
    }
   
    /**
     * Retrieve all accounts from the database
     */
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT a.*, c.customer_id, c.first_name, c.surname, c.address, c.phone_number, c.email " +
                    "FROM accounts a JOIN customers c ON a.customer_id = c.customer_id " +
                    "ORDER BY a.account_number";
       
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
           
            while (rs.next()) {
                Account account = extractAccountWithCustomerFromResultSet(rs);
                if (account != null) {
                    accounts.add(account);
                }
            }
           
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving all accounts: " + e.getMessage());
        }
       
        return accounts;
    }
   
    /**
     * Close account (set balance to 0 and mark as inactive in a real system)
     */
    public boolean closeAccount(String accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
            pstmt.setString(1, accountNumber);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
           
        } catch (SQLException e) {
            System.err.println("❌ Error closing account: " + e.getMessage());
            return false;
        }
    }
   
    /**
     * Get account count by type for statistics
     */
    public AccountTypeStatistics getAccountTypeStatistics() {
        String sql = "SELECT account_type, COUNT(*) as count, SUM(balance) as total_balance FROM accounts GROUP BY account_type";
       
        int savingsCount = 0, investmentCount = 0, chequeCount = 0;
        double savingsBalance = 0, investmentBalance = 0, chequeBalance = 0;
       
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
           
            while (rs.next()) {
                String accountType = rs.getString("account_type");
                int count = rs.getInt("count");
                double balance = rs.getDouble("total_balance");
               
                switch (accountType.toUpperCase()) {
                    case "SAVINGS":
                        savingsCount = count;
                        savingsBalance = balance;
                        break;
                    case "INVESTMENT":
                        investmentCount = count;
                        investmentBalance = balance;
                        break;
                    case "CHEQUE":
                        chequeCount = count;
                        chequeBalance = balance;
                        break;
                }
            }
           
        } catch (SQLException e) {
            System.err.println("❌ Error getting account statistics: " + e.getMessage());
        }
       
        return new AccountTypeStatistics(savingsCount, investmentCount, chequeCount,
                                       savingsBalance, investmentBalance, chequeBalance);
    }
   
    /**
     * Record a transaction for audit trail
     */
    public void recordTransaction(String accountNumber, String transactionType,
                                double amount, double balanceAfter, String description) {
        String sql = "INSERT INTO transactions (account_number, transaction_type, amount, balance_after, description) VALUES (?, ?, ?, ?, ?)";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
            pstmt.setString(1, accountNumber);
            pstmt.setString(2, transactionType);
            pstmt.setDouble(3, amount);
            pstmt.setDouble(4, balanceAfter);
            pstmt.setString(5, description);
           
            pstmt.executeUpdate();
           
        } catch (SQLException e) {
            System.err.println("❌ Error recording transaction: " + e.getMessage());
        }
    }
   
    /**
     * Get transaction history for an account
     */
    public List<TransactionRecord> getTransactionHistory(String accountNumber) {
        List<TransactionRecord> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();
           
            while (rs.next()) {
                TransactionRecord transaction = new TransactionRecord(
                    rs.getLong("transaction_id"),
                    rs.getString("account_number"),
                    rs.getString("transaction_type"),
                    rs.getDouble("amount"),
                    rs.getDouble("balance_after"),
                    rs.getTimestamp("transaction_date").toLocalDateTime(),
                    rs.getString("description")
                );
                transactions.add(transaction);
            }
           
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving transaction history: " + e.getMessage());
        }
       
        return transactions;
    }
   
    // ==================== PRIVATE HELPER METHODS ====================
   
    /**
     * Extract Account object with Customer from ResultSet
     */
    private Account extractAccountWithCustomerFromResultSet(ResultSet rs) throws SQLException {
        try {
            // First extract customer
            Customer customer = new Customer(
                rs.getString("customer_id"),
                rs.getString("first_name"),
                rs.getString("surname"),
                rs.getString("address"),
                rs.getString("phone_number"),
                rs.getString("email")
            );
           
            // Then extract account based on type
            String accountType = rs.getString("account_type");
            String accountNumber = rs.getString("account_number");
            double balance = rs.getDouble("balance");
            String branch = rs.getString("branch");
           
            switch (accountType.toUpperCase()) {
                case "SAVINGS":
                    return new SavingsAccount(accountNumber, balance, branch, customer);
                   
                case "INVESTMENT":
                    return new InvestmentAccount(accountNumber, balance, branch, customer);
                   
                case "CHEQUE":
                    String employer = rs.getString("employer");
                    String employerAddress = rs.getString("employer_address");
                    return new ChequeAccount(accountNumber, balance, branch, customer, employer, employerAddress);
                   
                default:
                    System.err.println("❌ Unknown account type: " + accountType);
                    return null;
            }
           
        } catch (SQLException e) {
            System.err.println("❌ Error extracting account with customer from result set: " + e.getMessage());
            throw e;
        }
    }
   
    // ==================== INNER CLASSES ====================
   
    /**
     * Inner class for account type statistics
     */
    public static class AccountTypeStatistics {
        private final int savingsCount;
        private final int investmentCount;
        private final int chequeCount;
        private final double savingsBalance;
        private final double investmentBalance;
        private final double chequeBalance;
       
        public AccountTypeStatistics(int savingsCount, int investmentCount, int chequeCount,
                                   double savingsBalance, double investmentBalance, double chequeBalance) {
            this.savingsCount = savingsCount;
            this.investmentCount = investmentCount;
            this.chequeCount = chequeCount;
            this.savingsBalance = savingsBalance;
            this.investmentBalance = investmentBalance;
            this.chequeBalance = chequeBalance;
        }
       
        // Getters
        public int getSavingsCount() { return savingsCount; }
        public int getInvestmentCount() { return investmentCount; }
        public int getChequeCount() { return chequeCount; }
        public double getSavingsBalance() { return savingsBalance; }
        public double getInvestmentBalance() { return investmentBalance; }
        public double getChequeBalance() { return chequeBalance; }
        public int getTotalCount() { return savingsCount + investmentCount + chequeCount; }
        public double getTotalBalance() { return savingsBalance + investmentBalance + chequeBalance; }
    }
   
    /**
     * Inner class for transaction records
     */
    public static class TransactionRecord {
        private final long transactionId;
        private final String accountNumber;
        private final String transactionType;
        private final double amount;
        private final double balanceAfter;
        private final java.time.LocalDateTime transactionDate;
        private final String description;
       
        public TransactionRecord(long transactionId, String accountNumber, String transactionType,
                               double amount, double balanceAfter, java.time.LocalDateTime transactionDate,
                               String description) {
            this.transactionId = transactionId;
            this.accountNumber = accountNumber;
            this.transactionType = transactionType;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
            this.transactionDate = transactionDate;
            this.description = description;
        }
       
        // Getters
        public long getTransactionId() { return transactionId; }
        public String getAccountNumber() { return accountNumber; }
        public String getTransactionType() { return transactionType; }
        public double getAmount() { return amount; }
        public double getBalanceAfter() { return balanceAfter; }
        public java.time.LocalDateTime getTransactionDate() { return transactionDate; }
        public String getDescription() { return description; }
    }
}