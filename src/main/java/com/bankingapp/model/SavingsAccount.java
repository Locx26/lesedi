package com.bankingapp.model;
import com.bankingapp.enums.TransactionType;

public class SavingsAccount extends Account {
    private static final double INDIVIDUAL_INTEREST_RATE = 0.025;
    private static final double COMPANY_INTEREST_RATE = 0.075;

    public SavingsAccount(String accountNumber, double initialBalance, String branch) {
        super(accountNumber, initialBalance, branch);
    }

    @Override
    public boolean withdraw(double amount) {
        System.out.println("Withdrawals are not allowed from Savings Account: " + accountNumber);
        return false;
    }

    @Override
    public void calculateInterest() {
        if (customer == null) {
            System.out.println("Cannot calculate interest - no customer associated with account");
            return;
        }

        double interestRate = (customer.getCustomerType() == com.bankingapp.enums.CustomerType.INDIVIDUAL)
            ? INDIVIDUAL_INTEREST_RATE : COMPANY_INTEREST_RATE;

        double interest = balance * interestRate;
        deposit(interest);

        Transaction interestTransaction = new Transaction(
            generateTransactionId(),
            interest,
            TransactionType.INTEREST,
            "Monthly interest",
            balance
        );

        if (!transactions.isEmpty()) {
            transactions.remove(transactions.size() - 1);
        }
        transactions.add(interestTransaction);

        System.out.println("Interest calculated and added: $" + interest +
                          " to Savings Account: " + accountNumber);
    }
}
