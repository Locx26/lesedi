package com.bankingapp.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base Account class for SecureTrust Banking System.
 * Keeps the same API expected by DAOs and controller:
 *  - constructor: Account(String accountNumber, double balance, String branch, Customer customer)
 *  - deposit(double) : void
 *  - withdraw(double) : boolean
 *  - canDeposit(), canWithdraw(), applyMonthlyInterest() are abstract
 *
 * This version adds thread-safety to balance updates, defensive validation,
 * and pragmatic toString/equals/hashCode implementations while preserving the public API.
 */
public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected LocalDateTime dateOpened;
    protected Customer customer;
    protected AccountType accountType;

    public Account(String accountNumber, double balance, String branch, Customer customer) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("accountNumber is required");
        }
        if (customer == null) {
            throw new IllegalArgumentException("customer is required");
        }

        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch != null ? branch : "";
        this.customer = customer;
        this.dateOpened = LocalDateTime.now();
    }

    // Abstract methods - implemented by subclasses
    public abstract boolean canWithdraw();
    public abstract boolean canDeposit();
    public abstract void applyMonthlyInterest();

    /**
     * Deposit the specified amount into the account.
     * This method is synchronized to be thread-safe for concurrent access.
     * If the account type does not allow deposits or amount is non-positive, no change is made.
     */
    public synchronized void deposit(double amount) {
        if (amount <= 0) {
            System.err.println("❌ Deposit amount must be positive: " + amount);
            return;
        }
        if (!canDeposit()) {
            System.err.println("❌ Deposits not allowed for account: " + accountNumber);
            return;
        }
        this.balance += amount;
        System.out.println("Deposited: " + amount + " to account: " + accountNumber);
    }

    /**
     * Withdraw the specified amount from the account.
     * Returns true when the withdrawal succeeds; false otherwise.
     * This method is synchronized to be thread-safe.
     */
    public synchronized boolean withdraw(double amount) {
        if (amount <= 0) {
            System.err.println("❌ Withdrawal amount must be positive: " + amount);
            return false;
        }
        if (!canWithdraw()) {
            System.err.println("❌ Withdrawals not allowed for account: " + accountNumber);
            return false;
        }
        if (balance < amount) {
            System.err.println("❌ Insufficient funds for withdrawal from account: " + accountNumber);
            return false;
        }
        this.balance -= amount;
        System.out.println("Withdrawn: " + amount + " from account: " + accountNumber);
        return true;
    }

    // Getters and setters - demonstrating encapsulation
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public LocalDateTime getDateOpened() { return dateOpened; }
    public Customer getCustomer() { return customer; }
    public AccountType getAccountType() { return accountType; }

    public void setBalance(double balance) { this.balance = balance; }
    public void setBranch(String branch) { this.branch = branch; }

    @Override
    public String toString() {
        return "Account{" +
               "accountNumber='" + accountNumber + '\'' +
               ", accountType=" + (accountType != null ? accountType.name() : "UNKNOWN") +
               ", balance=" + String.format("%.2f", balance) +
               ", branch='" + branch + '\'' +
               ", customer=" + (customer != null ? customer.getCustomerId() : "null") +
               ", dateOpened=" + dateOpened +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account that = (Account) o;
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}