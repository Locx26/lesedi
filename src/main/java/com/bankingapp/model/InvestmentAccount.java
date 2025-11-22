package com.bankingapp.model;

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

    @Override
    public void applyMonthlyInterest() {
        double interest = balance * MONTHLY_INTEREST_RATE;
        balance += interest;
        System.out.println("Applied interest: " + interest + " to savings account: " + accountNumber);
    }

    // Method overloading - demonstrating polymorphism
    public void applyMonthlyInterest(double customRate) {
        double interest = balance * customRate;
        balance += interest;
        System.out.println("Applied custom interest: " + interest + " to savings account: " + accountNumber);
    }
}