package com.bankingapp.model;
import com.bankingapp.enums.AccountStatus;
import com.bankingapp.enums.TransactionType;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected AccountStatus status;
    protected Customer customer;
    protected List<Transaction> transactions;

    public Account(String accountNumber, double initialBalance, String branch) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.branch = branch;
        this.status = AccountStatus.ACTIVE;
        this.transactions = new ArrayList<>();

        if (initialBalance > 0) {
            Transaction initialDeposit = new Transaction(
                "TXN_INIT_" + accountNumber,
                initialBalance,
                TransactionType.DEPOSIT,
                "Initial deposit",
                balance
            );
            transactions.add(initialDeposit);
        }
    }

    public boolean deposit(double amount) {
        if (amount > 0 && status == AccountStatus.ACTIVE) {
            balance += amount;
            Transaction transaction = new Transaction(
                generateTransactionId(),
                amount,
                TransactionType.DEPOSIT,
                "Deposit",
                balance
            );
            transactions.add(transaction);
            System.out.println("Deposited: $" + amount + " to account: " + accountNumber);
            return true;
        }
        System.out.println("Deposit failed for account: " + accountNumber);
        return false;
    }

    public abstract boolean withdraw(double amount);
    public abstract void calculateInterest();

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }

    public void printTransactionHistory() {
        System.out.println("Transaction History for Account: " + accountNumber);
        for (Transaction transaction : transactions) {
            System.out.println("  " + transaction);
        }
    }

    protected String generateTransactionId() {
        return "TXN_" + accountNumber + "_" + System.currentTimeMillis();
    }

    public void freezeAccount() {
        this.status = AccountStatus.FROZEN;
        System.out.println("Account " + accountNumber + " has been frozen");
    }

    public void activateAccount() {
        this.status = AccountStatus.ACTIVE;
        System.out.println("Account " + accountNumber + " has been activated");
    }

    public void closeAccount() {
        this.status = AccountStatus.CLOSED;
        System.out.println("Account " + accountNumber + " has been closed");
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public AccountStatus getStatus() { return status; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    @Override
    public String toString() {
        return String.format("Account[%s]: Balance: $%.2f, Status: %s, Branch: %s",
                           accountNumber, balance, status, branch);
    }
}