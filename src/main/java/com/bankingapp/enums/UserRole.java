package com.bankingapp.enums;

public enum UserRole {
    CUSTOMER("Customer"),
    BANK_TELLER("Bank Teller"),
    ADMIN("Administrator");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return displayName; }
}