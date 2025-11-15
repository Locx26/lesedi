package com.bankingapp.enums;

public enum AccountType {
    SAVINGS("Savings Account", 0.02),
    CHECKING("Checking Account", 0.01),
    BUSINESS("Business Account", 0.015);

    private final String displayName;
    private final double interestRate;

    AccountType(String displayName, double interestRate) {
        this.displayName = displayName;
        this.interestRate = interestRate;
    }

    public String getDisplayName() { return displayName; }
    public double getInterestRate() { return interestRate; }

    @Override
    public String toString() { return displayName; }
}
