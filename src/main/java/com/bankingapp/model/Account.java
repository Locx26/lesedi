package com.bankingapp.model;

import com.bankingapp.enums.AccountType;
import java.time.LocalDateTime;

public class Account {
    private String accountId;
    private String userId;
    private String accountNumber;
    private AccountType accountType;
    private double balance;
    private double interestRate;
    private LocalDateTime openedDate;
    private boolean active;

    public Account(String accountId, String userId, String accountNumber,
                   AccountType accountType, double initialBalance) {
        this.accountId = accountId;
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = initialBalance;
        this.interestRate = accountType.getInterestRate();
        this.openedDate = LocalDateTime.now();
        this.active = true;
    }

    // Getters
    public String getAccountId() { return accountId; }
    public String getUserId() { return userId; }
    public String getAccountNumber() { return accountNumber; }
    public AccountType getAccountType() { return accountType; }
    public double getBalance() { return balance; }
    public double getInterestRate() { return interestRate; }
    public LocalDateTime getOpenedDate() { return openedDate; }
    public boolean isActive() { return active; }

    // Business methods (no UI logic - pure business logic)
    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void applyInterest() {
        balance += balance * interestRate;
    }

    public String getFormattedBalance() {
        return String.format("$%.2f", balance);
    }

    @Override
    public String toString() {
        return String.format("Account{number=%s, type=%s, balance=%.2f}",
            accountNumber, accountType, balance);
    }
}