package com.bankingapp.model;
import com.bankingapp.enums.TransactionType;
import java.util.Date;

public class Transaction {
    private String transactionId;
    private Date date;
    private double amount;
    private TransactionType type;
    private String description;
    private double postBalance;

    public Transaction(String transactionId, double amount, TransactionType type,
                      String description, double postBalance) {
        this.transactionId = transactionId;
        this.date = new Date();
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.postBalance = postBalance;
    }

    public String getTransactionId() { return transactionId; }
    public Date getDate() { return date; }
    public double getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public String getDescription() { return description; }
    public double getPostBalance() { return postBalance; }

    @Override
    public String toString() {
        return String.format("Transaction[%s]: %s - %s - Amount: $%.2f - Balance: $%.2f",
                           transactionId, date, type, amount, postBalance);
    }
}
