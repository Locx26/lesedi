package com.bankingapp.model;

import java.util.Objects;

/**
 * Cheque (current) account implementation.
 * Keeps the same public API used across the app while adding light validation
 * and a helpful toString() for debugging.
 */
public class ChequeAccount extends Account {
    private String employer;
    private String employerAddress;

    public ChequeAccount(String accountNumber, double balance, String branch, Customer customer,
                        String employer, String employerAddress) {
        super(accountNumber, balance, branch, customer);
        // normalize employer fields to avoid NPEs elsewhere
        this.employer = employer != null ? employer.trim() : "";
        this.employerAddress = employerAddress != null ? employerAddress.trim() : "";
        this.accountType = AccountType.CHEQUE;
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
        // Cheque accounts don't earn interest
        System.out.println("No interest applied to cheque account: " + accountNumber);
    }

    // Getters and setters
    public String getEmployer() { return employer; }
    public String getEmployerAddress() { return employerAddress; }

    public void setEmployer(String employer) { this.employer = employer != null ? employer.trim() : ""; }
    public void setEmployerAddress(String employerAddress) { this.employerAddress = employerAddress != null ? employerAddress.trim() : ""; }

    @Override
    public String toString() {
        return "ChequeAccount{" +
               "accountNumber='" + accountNumber + '\'' +
               ", balance=" + String.format("%.2f", balance) +
               ", branch='" + branch + '\'' +
               ", employer='" + employer + '\'' +
               ", employerAddress='" + employerAddress + '\'' +
               ", customer=" + (customer != null ? customer.getCustomerId() : "null") +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChequeAccount)) return false;
        ChequeAccount that = (ChequeAccount) o;
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}