package com.bankingapp.model;
import com.bankingapp.enums.TransactionType;
import com.bankingapp.enums.AccountStatus;

public class ChequeAccount extends Account {
    private String employerName;
    private String employerAddress;
    private static final double MONTHLY_FEE = 10.0;

    public ChequeAccount(String accountNumber, double initialBalance, String branch,
                       String employerName, String employerAddress) {
        super(accountNumber, initialBalance, branch);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
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
            System.out.println("Withdrawn: $" + amount + " from Cheque Account: " + accountNumber);
            return true;
        }
        System.out.println("Withdrawal failed from Cheque Account: " + accountNumber);
        return false;
    }

    @Override
    public void calculateInterest() {
        System.out.println("Interest calculation not applicable for Cheque Account: " + accountNumber);
    }

    public void deductMonthlyFee() {
        if (balance >= MONTHLY_FEE && status == AccountStatus.ACTIVE) {
            balance -= MONTHLY_FEE;
            Transaction feeTransaction = new Transaction(
                generateTransactionId(),
                MONTHLY_FEE,
                TransactionType.FEE,
                "Monthly account fee",
                balance
            );
            transactions.add(feeTransaction);
            System.out.println("Monthly fee of $" + MONTHLY_FEE +
                              " deducted from Cheque Account: " + accountNumber);
        } else {
            System.out.println("Insufficient funds to deduct monthly fee from Cheque Account: " + accountNumber);
        }
    }

    public String getEmployerName() { return employerName; }
    public String getEmployerAddress() { return employerAddress; }
    public static double getMonthlyFee() { return MONTHLY_FEE; }
}
