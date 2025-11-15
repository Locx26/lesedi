package com.bankingapp.model;

import com.bankingapp.enums.TransactionType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Transaction Model - Represents financial transactions
 */
public class Transaction {
    private String transactionId;
    private String accountId;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime timestamp;
    private double balanceAfterTransaction;

    public Transaction(String transactionId, String accountId, TransactionType type,
                      double amount, String description) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
        this.balanceAfterTransaction = 0.0; // Would be calculated during transaction processing
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public String getAccountId() { return accountId; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getBalanceAfterTransaction() { return balanceAfterTransaction; }

    // Setters
    public void setBalanceAfterTransaction(double balanceAfterTransaction) {
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }

    public String getFormattedAmount() {
        String sign = amount >= 0 ? "+" : "";
        return String.format("%s$%.2f", sign, Math.abs(amount));
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s",
            getFormattedTimestamp(),
            type.getDisplayName(),
            getFormattedAmount(),
            description);
    }

    public String toDetailedString() {
        return String.format("Transaction ID: %s\nAccount: %s\nType: %s\nAmount: %s\nDescription: %s\nTime: %s\nBalance After: $%.2f",
            transactionId, accountId, type.getDisplayName(), getFormattedAmount(),
            description, getFormattedTimestamp(), balanceAfterTransaction);
    }
}