package com.bankingapp.model;

public class ChequeAccount extends Account {
    private String employer;
    private String employerAddress;

    public ChequeAccount(String accountNumber, double balance, String branch, Customer customer,
                        String employer, String employerAddress) {
        super(accountNumber, balance, branch, customer);
        this.employer = employer;
        this.employerAddress = employerAddress;
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
   
    public void setEmployer(String employer) { this.employer = employer; }
    public void setEmployerAddress(String employerAddress) { this.employerAddress = employerAddress; }
}