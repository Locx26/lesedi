package com.bankingapp.model;
import com.bankingapp.enums.TransactionType;
import com.bankingapp.enums.AccountStatus;

public class InvestmentAccount extends Account {
    private static final double INTEREST_RATE = 0.05;
    private static final double MIN_INITIAL_DEPOSIT = 500.0;

    public InvestmentAccount(String accountNumber, double initialBalance, String branch) {
        super(accountNumber, initialBalance, branch);

        if (initialBalance < MIN_INITIAL_DEPOSIT) {
            System.out.println("Warning: Investment account requires minimum initial deposit of $" +
                             MIN_INITIAL_DEPOSIT);
        }
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance && status == AccountStatus.ACTIVE) {
            balance -= amount;
            Transaction transaction = new Transaction(
                generateTransactionId(),
                amount,
                TransactionType.WITHDRAWAL,
                "Withdrawal",
                balance
            );
            transactions.add(transaction);
            System.out.println("Withdrawn: $" + amount + " from Investment Account: " + accountNumber);
            return true;
        }
        System.out.println("Withdrawal failed from Investment Account: " + accountNumber);
        return false;
    }

    @Override
    public void calculateInterest() {
        double interest = balance * INTEREST_RATE;
        deposit(interest);

        System.out.println("Interest calculated and added: $" + interest +
                          " to Investment Account: " + accountNumber);
    }

    public static double getMinInitialDeposit() {
        return MIN_INITIAL_DEPOSIT;
    }
}