package com.securetrust.model;

/**
 * Represents the type of customer - Individual or Company.
 * This affects interest rates for Savings accounts:
 * - Individual: 2.5% monthly interest
 * - Company: 7.5% monthly interest
 */
public enum CustomerType {
    INDIVIDUAL("Individual"),
    COMPANY("Company");

    private final String displayName;

    CustomerType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
