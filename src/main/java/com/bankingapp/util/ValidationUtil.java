package com.bankingapp.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Professional Validation Utility Class
 * Provides comprehensive input validation for banking operations
 *
 * Improvements:
 * - More robust regexes (Unicode name support, safer email check)
 * - Numeric amount validation using BigDecimal to strictly enforce scale (2 decimals)
 * - Case-insensitive account/customer id checks and trimmed inputs
 * - Defensive null/blank handling and clearer error messages
 */
public class ValidationUtil {

    // Regular expressions for validation
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^\\d{8}$"); // 8-digit phone numbers

    // Allow Unicode letters for international names
    private static final Pattern NAME_PATTERN =
        Pattern.compile("^[\\p{L}\\s'\\-\\.]{2,50}$");

    // Three letters followed by digits (case-insensitive in matching logic)
    private static final Pattern ACCOUNT_NUMBER_PATTERN =
        Pattern.compile("^[A-Za-z]{3}\\d+$");

    /**
     * Validate email address format
     */
    public static ValidationResult validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return new ValidationResult(false, "Email address is required");
        }

        String trimmed = email.trim();
        if (trimmed.length() > 100) {
            return new ValidationResult(false, "Email address too long (max 100 characters)");
        }

        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            return new ValidationResult(false, "Invalid email address format");
        }

        return new ValidationResult(true, "Valid email address");
    }

    /**
     * Validate phone number format (expects 8 digits after cleaning).
     */
    public static ValidationResult validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return new ValidationResult(false, "Phone number is required");
        }

        // Remove any spaces, dashes, parentheses or plus signs
        String cleaned = phoneNumber.replaceAll("[\\s\\-\\(\\)\\+]", "");

        if (!PHONE_PATTERN.matcher(cleaned).matches()) {
            return new ValidationResult(false, "Phone number must be 8 digits");
        }

        return new ValidationResult(true, "Valid phone number");
    }

    /**
     * Validate person name (first name or surname)
     */
    public static ValidationResult validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            return new ValidationResult(false, fieldName + " is required");
        }

        String trimmed = name.trim();

        if (!NAME_PATTERN.matcher(trimmed).matches()) {
            return new ValidationResult(false,
                fieldName + " can only contain letters, spaces, hyphens, apostrophes and periods (2-50 characters)");
        }

        return new ValidationResult(true, "Valid " + fieldName.toLowerCase());
    }

    /**
     * Validate monetary amount (double overload)
     */
    public static ValidationResult validateAmount(double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            return new ValidationResult(false, "Invalid amount");
        }
        if (amount < 0) {
            return new ValidationResult(false, "Amount cannot be negative");
        }
        if (amount > 1_000_000_000) {
            return new ValidationResult(false, "Amount exceeds maximum limit");
        }
        // Check scale by converting to BigDecimal
        BigDecimal bd = BigDecimal.valueOf(amount);
        if (bd.scale() > 2) {
            return new ValidationResult(false, "Amount must have up to 2 decimal places");
        }
        return new ValidationResult(true, "Valid amount");
    }

    /**
     * Validate monetary amount from string (strict: decimal format with up to 2 decimals)
     */
    public static ValidationResult validateAmount(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return new ValidationResult(false, "Amount is required");
        }

        String trimmed = amountStr.trim();

        try {
            BigDecimal bd = new BigDecimal(trimmed);
            if (bd.scale() < 0) {
                // defensive: treat negative scale as invalid
                return new ValidationResult(false, "Invalid amount format");
            }
            if (bd.compareTo(BigDecimal.ZERO) < 0) {
                return new ValidationResult(false, "Amount cannot be negative");
            }
            if (bd.compareTo(BigDecimal.valueOf(1_000_000_000L)) > 0) {
                return new ValidationResult(false, "Amount exceeds maximum limit");
            }
            if (bd.scale() > 2) {
                return new ValidationResult(false, "Amount must have up to 2 decimal places");
            }
            return new ValidationResult(true, "Valid amount");
        } catch (NumberFormatException ex) {
            return new ValidationResult(false, "Invalid amount format");
        }
    }

    /**
     * Validate account number format: 3 letters followed by digits, case-insensitive.
     */
    public static ValidationResult validateAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return new ValidationResult(false, "Account number is required");
        }

        String trimmed = accountNumber.trim();
        if (trimmed.length() > 20) {
            return new ValidationResult(false, "Account number too long (max 20 characters)");
        }

        if (!ACCOUNT_NUMBER_PATTERN.matcher(trimmed).matches()) {
            return new ValidationResult(false,
                "Account number must start with 3 letters followed by numbers (e.g., ABC12345)");
        }

        return new ValidationResult(true, "Valid account number");
    }

    /**
     * Validate customer ID format (case-insensitive prefix 'CUST')
     */
    public static ValidationResult validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            return new ValidationResult(false, "Customer ID is required");
        }

        String trimmed = customerId.trim().toUpperCase();
        if (!trimmed.startsWith("CUST")) {
            return new ValidationResult(false, "Customer ID must start with 'CUST'");
        }

        if (trimmed.length() > 20) {
            return new ValidationResult(false, "Customer ID too long (max 20 characters)");
        }

        return new ValidationResult(true, "Valid customer ID");
    }

    /**
     * Validate branch name
     */
    public static ValidationResult validateBranch(String branch) {
        if (branch == null || branch.trim().isEmpty()) {
            return new ValidationResult(false, "Branch name is required");
        }

        String trimmed = branch.trim();
        if (trimmed.length() < 2 || trimmed.length() > 100) {
            return new ValidationResult(false, "Branch name must be between 2 and 100 characters");
        }

        return new ValidationResult(true, "Valid branch name");
    }

    /**
     * Validate address
     */
    public static ValidationResult validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return new ValidationResult(false, "Address is required");
        }

        String trimmed = address.trim();
        if (trimmed.length() < 5 || trimmed.length() > 255) {
            return new ValidationResult(false, "Address must be between 5 and 255 characters");
        }

        return new ValidationResult(true, "Valid address");
    }

    /**
     * Validate employer information for cheque accounts
     */
    public static ValidationResult validateEmployer(String employer) {
        if (employer == null || employer.trim().isEmpty()) {
            return new ValidationResult(false, "Employer name is required for cheque accounts");
        }

        String trimmed = employer.trim();
        if (trimmed.length() < 2 || trimmed.length() > 100) {
            return new ValidationResult(false, "Employer name must be between 2 and 100 characters");
        }

        return new ValidationResult(true, "Valid employer name");
    }

    /**
     * Comprehensive customer validation
     */
    public static ValidationResult validateCustomer(String firstName, String surname,
                                                   String address, String phone, String email) {
        // Validate first name
        ValidationResult firstNameResult = validateName(firstName, "First name");
        if (!firstNameResult.isValid()) {
            return firstNameResult;
        }

        // Validate surname
        ValidationResult surnameResult = validateName(surname, "Surname");
        if (!surnameResult.isValid()) {
            return surnameResult;
        }

        // Validate address
        ValidationResult addressResult = validateAddress(address);
        if (!addressResult.isValid()) {
            return addressResult;
        }

        // Validate phone
        ValidationResult phoneResult = validatePhoneNumber(phone);
        if (!phoneResult.isValid()) {
            return phoneResult;
        }

        // Validate email (optional)
        if (email != null && !email.trim().isEmpty()) {
            ValidationResult emailResult = validateEmail(email);
            if (!emailResult.isValid()) {
                return emailResult;
            }
        }

        return new ValidationResult(true, "All customer fields are valid");
    }

    /**
     * Comprehensive transaction validation
     */
    public static ValidationResult validateTransaction(String accountNumber, double amount) {
        // Validate account number
        ValidationResult accountResult = validateAccountNumber(accountNumber);
        if (!accountResult.isValid()) {
            return accountResult;
        }

        // Validate amount
        ValidationResult amountResult = validateAmount(amount);
        if (!amountResult.isValid()) {
            return amountResult;
        }

        return new ValidationResult(true, "Transaction validation passed");
    }

    // Inner class for validation results
    public static class ValidationResult {
        private final boolean valid;
        private final String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public boolean isValid() { return valid; }
        public String getMessage() { return message; }

        @Override
        public String toString() {
            return String.format("%s: %s", valid ? "✅" : "❌", message);
        }
    }
}