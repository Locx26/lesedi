package com.bankingapp.model;

public class InvestmentAccount extends Account {
    private static final double MONTHLY_INTEREST_RATE = 0.05; // 5%
    private static final double MIN_OPENING_BALANCE = 500.00;

    public InvestmentAccount(String accountNumber, double balance, String branch, Customer customer) {
        super(accountNumber, balance, branch, customer);
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

    @Override
    public void applyMonthlyInterest() {
        double interest = balance * MONTHLY_INTEREST_RATE;
        balance += interest;
        System.out.println("Applied interest: " + interest + " to investment account: " + accountNumber);
    }

    // Method overloading
    public void applyMonthlyInterest(int months) {
        for (int i = 0; i < months; i++) {
            applyMonthlyInterest();
        }
    }
}