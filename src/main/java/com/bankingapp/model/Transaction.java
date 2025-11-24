package com.bankingapp.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain model representing a transaction/audit record.
 * - Keeps the original public API (String fields for type/status) for compatibility with existing DAOs/controllers
 * - Adds defensive validation, convenience constructors/factories, enums for common values and helpful toString/equals/hashCode
 */
public class Transaction {
    private String transactionId;
    private String accountId;
    private LocalDateTime timestamp;
    private String description;
    private double amount;
    private String transactionType; // DEBIT or CREDIT (kept as String for compatibility)
    private String category;
    private String status; // e.g. PENDING, COMPLETED, FAILED (kept as String for compatibility)

    public Transaction() {
        this.transactionId = generateId();
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.PENDING.name();
    }

    public Transaction(String transactionId, String accountId, LocalDateTime timestamp,
                       String description, double amount, String transactionType,
                       String category, String status) {
        this.transactionId = transactionId != null && !transactionId.isBlank() ? transactionId : generateId();
        this.accountId = accountId;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        this.description = description != null ? description : "";
        this.amount = amount;
        this.transactionType = normalizeType(transactionType);
        this.category = category != null ? category : "";
        this.status = normalizeStatus(status);
    }

    // Convenience factory for debit/credit
    public static Transaction createDebit(String accountId, double amount, String description, String category) {
        return new Transaction(null, accountId, LocalDateTime.now(), description, amount, TransactionType.DEBIT.name(), category, TransactionStatus.COMPLETED.name());
    }

    public static Transaction createCredit(String accountId, double amount, String description, String category) {
        return new Transaction(null, accountId, LocalDateTime.now(), description, amount, TransactionType.CREDIT.name(), category, TransactionStatus.COMPLETED.name());
    }

    // Helper: generate unique transaction id
    private static String generateId() {
        return "TXN-" + UUID.randomUUID().toString();
    }

    // Normalize incoming type/status strings (null-safe)
    private static String normalizeType(String type) {
        if (type == null) return null;
        try {
            return TransactionType.valueOf(type.trim().toUpperCase()).name();
        } catch (IllegalArgumentException ex) {
            // allow arbitrary strings but normalize whitespace
            return type.trim().toUpperCase();
        }
    }

    private static String normalizeStatus(String status) {
        if (status == null) return TransactionStatus.PENDING.name();
        try {
            return TransactionStatus.valueOf(status.trim().toUpperCase()).name();
        } catch (IllegalArgumentException ex) {
            return status.trim().toUpperCase();
        }
    }

    // Getters and setters (defensive)
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId != null ? transactionId : this.transactionId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp != null ? timestamp : LocalDateTime.now(); }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description != null ? description : ""; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw new IllegalArgumentException("Invalid amount");
        }
        this.amount = amount;
    }

    // Keep original String-based API for compatibility
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = normalizeType(transactionType); }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category != null ? category : ""; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = normalizeStatus(status); }

    // Convenience enum-aware getters/setters
    public TransactionType getTransactionTypeEnum() {
        try {
            return transactionType != null ? TransactionType.valueOf(transactionType) : null;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public void setTransactionTypeEnum(TransactionType type) {
        this.transactionType = type != null ? type.name() : null;
    }

    public TransactionStatus getTransactionStatusEnum() {
        try {
            return status != null ? TransactionStatus.valueOf(status) : null;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public void setTransactionStatusEnum(TransactionStatus s) {
        this.status = s != null ? s.name() : null;
    }

    @Override
    public String toString() {
        return String.format("Transaction[%s: %s %.2f (%s) @ %s]",
            transactionId,
            description == null || description.isBlank() ? "" : description,
            amount,
            transactionType != null ? transactionType : "UNKNOWN",
            timestamp != null ? timestamp.toString() : "N/A");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    // Common transaction types/statuses as enums for convenience
    public enum TransactionType {
        DEBIT,
        CREDIT
    }

    public enum TransactionStatus {
        PENDING,
        COMPLETED,
        FAILED,
        REVERSED
    }
}