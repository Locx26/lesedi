package com.bankingapp.model;

import java.util.Objects;

/**
 * Investment account implementation.
 * Preserves existing public API but adds defensive checks, normalization, and thread-safety.
 */
public class InvestmentAccount extends Account {
    private static final double MONTHLY_INTEREST_RATE = 0.05; // 5%
    private static final double MIN_OPENING_BALANCE = 500.00;

    public InvestmentAccount(String accountNumber, double balance, String branch, Customer customer) {
        super(accountNumber, balance, branch, customer);

        if (Double.isNaN(balance) || Double.isInfinite(balance)) {
            throw new IllegalArgumentException("Invalid opening balance");
        }

        if (balance < MIN_OPENING_BALANCE) {
            throw new IllegalArgumentException("Investment account requires minimum BWP 500.00 opening balance");
        }

        this.accountType = AccountType.INVESTMENT;
    }

    @Override
    public boolean canWithdraw() {
        return true;
    }

    @Override
    public boolean canDeposit() {
        return true;
    }

    /**
     * Apply monthly interest once. Synchronized for thread-safety when updating balance.
     */
    @Override
    public synchronized void applyMonthlyInterest() {
        double interest = this.balance * MONTHLY_INTEREST_RATE;
        this.balance += interest;
        System.out.println("Applied interest: " + String.format("%.2f", interest) + " to investment account: " + accountNumber);
    }

    /**
     * Apply monthly interest repeatedly for the given number of months.
     * Uses the single-month method to keep behavior consistent.
     */
    public void applyMonthlyInterest(int months) {
        if (months <= 0) return;
        for (int i = 0; i < months; i++) {
            applyMonthlyInterest();
        }
    }

    @Override
    public String toString() {
        return "InvestmentAccount{" +
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
        if (!(o instanceof InvestmentAccount)) return false;
        InvestmentAccount that = (InvestmentAccount) o;
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}