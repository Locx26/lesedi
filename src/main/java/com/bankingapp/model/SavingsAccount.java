package com.bankingapp.model;

import java.util.Objects;

/**
 * Savings account implementation.
 * - Preserves the existing public API used across the project.
 * - Adds defensive checks and synchronizes interest application to be thread-safe.
 */
public class SavingsAccount extends Account {
    private static final double MONTHLY_INTEREST_RATE = 0.0005; // 0.05%

    public SavingsAccount(String accountNumber, double balance, String branch, Customer customer) {
        super(accountNumber, balance, branch, customer);
        this.accountType = AccountType.SAVINGS;
    }

    @Override
    public boolean canWithdraw() {
        return false; // Savings account doesn't allow withdrawals
    }

    @Override
    public boolean canDeposit() {
        return true;
    }

    /**
     * Apply the standard monthly interest (synchronized for thread-safety).
     */
    @Override
    public synchronized void applyMonthlyInterest() {
        if (Double.isNaN(balance) || Double.isInfinite(balance)) return;
        double interest = this.balance * MONTHLY_INTEREST_RATE;
        this.balance += interest;
        System.out.println("Applied interest: " + String.format("%.2f", interest) + " to savings account: " + accountNumber);
    }

    /**
     * Apply a custom interest rate once. Synchronized to match applyMonthlyInterest().
     */
    public synchronized void applyMonthlyInterest(double customRate) {
        if (Double.isNaN(balance) || Double.isInfinite(balance)) return;
        if (Double.isNaN(customRate) || Double.isInfinite(customRate) || customRate < 0) return;
        double interest = this.balance * customRate;
        this.balance += interest;
        System.out.println("Applied custom interest: " + String.format("%.2f", interest) + " to savings account: " + accountNumber);
    }

    @Override
    public String toString() {
        return "SavingsAccount{" +
               "accountNumber='" + accountNumber + '\'' +
               ", balance=" + String.format("%.2f", balance) +
               ", branch='" + branch + '\'' +
               ", accountType=" + (accountType != null ? accountType.name() : "UNKNOWN") +
               ", customer=" + (customer != null ? customer.getCustomerId() : "null") +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SavingsAccount)) return false;
        SavingsAccount that = (SavingsAccount) o;
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}