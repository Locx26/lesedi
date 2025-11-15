package com.bankingapp.service;

import com.bankingapp.model.Account;
import com.bankingapp.model.Transaction;
import com.bankingapp.enums.AccountType;
import com.bankingapp.enums.TransactionType;
import java.util.*;

/**
 * Account Service - Handles all account-related business logic
 * Account management, transactions, and financial operations
 */
public class AccountService {
    private Map<String, Account> accounts;
    private Map<String, List<Transaction>> accountTransactions;
    private UserService userService;

    public AccountService(UserService userService) {
        this.accounts = new HashMap<>();
        this.accountTransactions = new HashMap<>();
        this.userService = userService;
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Sample accounts are initialized in UserService
        // This would typically load from database
    }

    /**
     * Create new account for user
     */
    public Account createAccount(String userId, AccountType accountType, double initialDeposit) {
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative");
        }

        String accountId = "ACC" + String.format("%03d", accounts.size() + 1);
        String accountNumber = generateAccountNumber(accountType);
       
        Account newAccount = new Account(accountId, userId, accountNumber, accountType, initialDeposit);
        accounts.put(accountId, newAccount);
       
        // Add to user's account list
        List<Account> userAccounts = userService.getUserAccounts(userId);
        userAccounts.add(newAccount);
       
        // Initialize transactions list
        accountTransactions.put(accountId, new ArrayList<>());
       
        // Record initial deposit transaction
        if (initialDeposit > 0) {
            recordTransaction(accountId, TransactionType.DEPOSIT, initialDeposit, "Initial deposit");
        }
       
        return newAccount;
    }

    private String generateAccountNumber(AccountType accountType) {
        Random random = new Random();
        int randomNum = random.nextInt(9000) + 1000;
       
        switch (accountType) {
            case SAVINGS:
                return "SAV-" + randomNum;
            case CHECKING:
                return "CHK-" + randomNum;
            case BUSINESS:
                return "BUS-" + randomNum;
            default:
                return "ACC-" + randomNum;
        }
    }

    /**
     * Get account by ID
     */
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    /**
     * Get accounts for user
     */
    public List<Account> getAccountsForUser(String userId) {
        return userService.getUserAccounts(userId);
    }

    /**
     * Deposit money into account
     */
    public boolean deposit(String accountId, double amount, String description) {
        Account account = accounts.get(accountId);
        if (account != null && amount > 0) {
            boolean success = account.deposit(amount);
            if (success) {
                recordTransaction(accountId, TransactionType.DEPOSIT, amount, description);
                return true;
            }
        }
        return false;
    }

    /**
     * Withdraw money from account
     */
    public boolean withdraw(String accountId, double amount, String description) {
        Account account = accounts.get(accountId);
        if (account != null && amount > 0) {
            boolean success = account.withdraw(amount);
            if (success) {
                recordTransaction(accountId, TransactionType.WITHDRAWAL, amount, description);
                return true;
            }
        }
        return false;
    }

    /**
     * Transfer money between accounts
     */
    public boolean transfer(String fromAccountId, String toAccountId, double amount, String description) {
        Account fromAccount = accounts.get(fromAccountId);
        Account toAccount = accounts.get(toAccountId);
       
        if (fromAccount != null && toAccount != null && amount > 0) {
            // Check if sufficient funds
            if (fromAccount.getBalance() >= amount) {
                // Perform transfer
                fromAccount.withdraw(amount);
                toAccount.deposit(amount);
               
                // Record transactions
                recordTransaction(fromAccountId, TransactionType.TRANSFER, -amount,
                                 "Transfer to " + toAccount.getAccountNumber() + " - " + description);
                recordTransaction(toAccountId, TransactionType.TRANSFER, amount,
                                 "Transfer from " + fromAccount.getAccountNumber() + " - " + description);
               
                return true;
            }
        }
        return false;
    }

    /**
     * Get account balance
     */
    public double getAccountBalance(String accountId) {
        Account account = accounts.get(accountId);
        return account != null ? account.getBalance() : 0.0;
    }

    /**
     * Get transaction history for account
     */
    public List<Transaction> getTransactionHistory(String accountId) {
        return accountTransactions.getOrDefault(accountId, new ArrayList<>());
    }

    /**
     * Record a transaction
     */
    private void recordTransaction(String accountId, TransactionType type, double amount, String description) {
        Transaction transaction = new Transaction(
            "TXN" + String.format("%06d", accountTransactions.values().stream().mapToInt(List::size).sum() + 1),
            accountId,
            type,
            amount,
            description
        );
       
        accountTransactions.computeIfAbsent(accountId, k -> new ArrayList<>()).add(transaction);
    }

    /**
     * Close account
     */
    public boolean closeAccount(String accountId) {
        Account account = accounts.get(accountId);
        if (account != null && account.getBalance() == 0) {
            account.setActive(false);
            return true;
        }
        return false;
    }

    /**
     * Apply interest to all savings accounts
     */
    public void applyInterestToAllAccounts() {
        for (Account account : accounts.values()) {
            if (account.isActive() && account.getAccountType() == AccountType.SAVINGS) {
                account.applyInterest();
                recordTransaction(account.getAccountId(), TransactionType.INTEREST,
                                account.getBalance() * account.getInterestRate(), "Monthly interest");
            }
        }
    }
}
