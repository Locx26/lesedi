package com.bankingapp.model;

public enum AccountType {
    SAVINGS("Savings Account"),
    INVESTMENT("Investment Account"),
    CHEQUE("Cheque Account");

    private final String description;

    AccountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}