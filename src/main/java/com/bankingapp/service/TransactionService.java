package com.bankingapp.service;

import com.bankingapp.model.Transaction;
import com.bankingapp.enums.TransactionType;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Transaction Service - Handles transaction-related business logic
 * Transaction history, reporting, and analytics
 */
public class TransactionService {
    private Map<String, List<Transaction>> accountTransactions;
    private AccountService accountService;

    public TransactionService(AccountService accountService) {
        this.accountTransactions = new HashMap<>();
        this.accountService = accountService;
    }

    /**
     * Get transactions for account within date range
     */
    public List<Transaction> getTransactionsByDateRange(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = accountService.getTransactionHistory(accountId);
       
        return transactions.stream()
                .filter(t -> !t.getTimestamp().isBefore(startDate) && !t.getTimestamp().isAfter(endDate))
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()))
                .collect(Collectors.toList());
    }

    /**
     * Get recent transactions for account
     */
    public List<Transaction> getRecentTransactions(String accountId, int limit) {
        List<Transaction> transactions = accountService.getTransactionHistory(accountId);
       
        return transactions.stream()
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get transactions by type
     */
    public List<Transaction> getTransactionsByType(String accountId, TransactionType type) {
        List<Transaction> transactions = accountService.getTransactionHistory(accountId);
       
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()))
                .collect(Collectors.toList());
    }

    /**
     * Calculate total deposits for account
     */
    public double getTotalDeposits(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = getTransactionsByDateRange(accountId, startDate, endDate);
       
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.DEPOSIT || t.getType() == TransactionType.TRANSFER)
                .filter(t -> t.getAmount() > 0)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Calculate total withdrawals for account
     */
    public double getTotalWithdrawals(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = getTransactionsByDateRange(accountId, startDate, endDate);
       
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.WITHDRAWAL || t.getType() == TransactionType.TRANSFER)
                .filter(t -> t.getAmount() < 0)
                .mapToDouble(t -> Math.abs(t.getAmount()))
                .sum();
    }

    /**
     * Generate transaction report
     */
    public String generateTransactionReport(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = getTransactionsByDateRange(accountId, startDate, endDate);
        double totalDeposits = getTotalDeposits(accountId, startDate, endDate);
        double totalWithdrawals = getTotalWithdrawals(accountId, startDate, endDate);
        double netFlow = totalDeposits - totalWithdrawals;

        StringBuilder report = new StringBuilder();
        report.append("=== TRANSACTION REPORT ===\n");
        report.append("Period: ").append(startDate.toLocalDate()).append(" to ").append(endDate.toLocalDate()).append("\n");
        report.append("Total Transactions: ").append(transactions.size()).append("\n");
        report.append("Total Deposits: $").append(String.format("%.2f", totalDeposits)).append("\n");
        report.append("Total Withdrawals: $").append(String.format("%.2f", totalWithdrawals)).append("\n");
        report.append("Net Cash Flow: $").append(String.format("%.2f", netFlow)).append("\n\n");
        report.append("Recent Transactions:\n");

        if (transactions.isEmpty()) {
            report.append("No transactions in this period.\n");
        } else {
            transactions.stream()
                    .limit(10)
                    .forEach(t -> report.append(t).append("\n"));
        }

        return report.toString();
    }

    /**
     * Search transactions by description
     */
    public List<Transaction> searchTransactions(String accountId, String searchTerm) {
        List<Transaction> transactions = accountService.getTransactionHistory(accountId);
       
        return transactions.stream()
                .filter(t -> t.getDescription().toLowerCase().contains(searchTerm.toLowerCase()))
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()))
                .collect(Collectors.toList());
    }
}