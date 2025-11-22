package com.bankingapp.model;

import java.time.LocalDateTime;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected LocalDateTime dateOpened;
    protected Customer customer;
    protected AccountType accountType;

    public Account(String accountNumber, double balance, String branch, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.customer = customer;
        this.dateOpened = LocalDateTime.now();
    }

    // Abstract methods - demonstrating abstraction
    public abstract boolean canWithdraw();
    public abstract boolean canDeposit();
    public abstract void applyMonthlyInterest();
   
    // Concrete methods - demonstrating inheritance
    public void deposit(double amount) {
        if (amount > 0 && canDeposit()) {
            this.balance += amount;
            System.out.println("Deposited: " + amount + " to account: " + accountNumber);
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && canWithdraw() && balance >= amount) {
            this.balance -= amount;
            System.out.println("Withdrawn: " + amount + " from account: " + accountNumber);
            return true;
        }
        return false;
    }

    // Getters and setters - demonstrating encapsulation
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public LocalDateTime getDateOpened() { return dateOpened; }
    public Customer getCustomer() { return customer; }
    public AccountType getAccountType() { return accountType; }

    public void setBalance(double balance) { this.balance = balance; }
}