package com.bankingapp.model;

/**
 * Account type enumerations for SecureTrust Banking System.
 * Includes a helper to parse values coming from DB/UI (case-insensitive,
 * accepts either enum name or the human-friendly description).
 */
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

    /**
     * Parse an account type from a string. Accepts the enum name (e.g. "SAVINGS"),
     * the description (e.g. "Savings Account"), or common variants (case-insensitive).
     *
     * Returns the matching AccountType or throws IllegalArgumentException if unknown.
     */
    public static AccountType fromString(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Account type string is null");
        }

        String normalized = s.trim();

        // 1) Try direct enum name match
        for (AccountType t : values()) {
            if (t.name().equalsIgnoreCase(normalized)) {
                return t;
            }
        }

        // 2) Try human-friendly description match
        for (AccountType t : values()) {
            if (t.getDescription().equalsIgnoreCase(normalized)) {
                return t;
            }
            // allow matching when input contains the key word, e.g. "savings", "Savings Account"
            if (normalized.toUpperCase().contains(t.name())) {
                return t;
            }
            if (normalized.toUpperCase().contains(t.getDescription().toUpperCase().replace(" ", ""))) {
                return t;
            }
        }

        throw new IllegalArgumentException("Unknown AccountType: " + s);
    }
}