package com.bankingapp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Customer domain model for SecureTrust Banking System.
 * - Keeps the same public API used by the rest of the codebase (constructor, openAccount, depositToAccount, withdrawFromAccount, getters/setters).
 * - Adds thread-safety for account list access, defensive null checks, and helpful toString/equals/hashCode implementations.
 */
public class Customer {
    private final String customerId;
    private String firstName;
    private String surname;
    private String address;
    private String phoneNumber;
    private String email;
    private final List<Account> accounts;

    public Customer(String customerId, String firstName, String surname, String address,
                    String phoneNumber, String email) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId is required");
        }
        this.customerId = customerId;
        this.firstName = firstName != null ? firstName : "";
        this.surname = surname != null ? surname : "";
        this.address = address != null ? address : "";
        this.phoneNumber = phoneNumber != null ? phoneNumber : "";
        this.email = email != null ? email : "";
        // Use a synchronized list to protect concurrent access from multiple UI/worker threads.
        this.accounts = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Open (attach) an account to this customer.
     * Keeps original signature so existing callers remain valid.
     */
    public void openAccount(Account account) {
        if (account == null) {
            System.err.println("❌ Attempted to open a null account for customer: " + customerId);
            return;
        }
        accounts.add(account);
        System.out.println("Opened " + (account.getAccountType() != null ? account.getAccountType().getDescription() : "Account")
                           + " account for customer: " + firstName + " " + surname);
    }

    /**
     * Deposit into an attached account. No-op with a warning if account not found.
     * Retains original void signature for compatibility.
     */
    public void depositToAccount(String accountNumber, double amount) {
        if (accountNumber == null || accountNumber.isBlank()) {
            System.err.println("❌ depositToAccount: invalid account number");
            return;
        }
        Optional<Account> opt;
        synchronized (accounts) {
            opt = accounts.stream().filter(a -> accountNumber.equals(a.getAccountNumber())).findFirst();
        }
        if (opt.isPresent()) {
            opt.get().deposit(amount);
        } else {
            System.out.println("Account not found: " + accountNumber);
        }
    }

    /**
     * Withdraw from an attached account, returns true if successful.
     */
    public boolean withdrawFromAccount(String accountNumber, double amount) {
        if (accountNumber == null || accountNumber.isBlank()) {
            System.err.println("❌ withdrawFromAccount: invalid account number");
            return false;
        }
        Optional<Account> opt;
        synchronized (accounts) {
            opt = accounts.stream().filter(a -> accountNumber.equals(a.getAccountNumber())).findFirst();
        }
        return opt.map(a -> a.withdraw(amount)).orElse(false);
    }

    /**
     * Find an attached account by its number, or null if not present.
     */
    public Account getAccountByNumber(String accountNumber) {
        if (accountNumber == null) return null;
        synchronized (accounts) {
            for (Account a : accounts) {
                if (accountNumber.equals(a.getAccountNumber())) return a;
            }
        }
        return null;
    }

    /**
     * Remove an attached account by account number. Returns true if removed.
     */
    public boolean removeAccount(String accountNumber) {
        if (accountNumber == null) return false;
        synchronized (accounts) {
            return accounts.removeIf(a -> accountNumber.equals(a.getAccountNumber()));
        }
    }

    // Getters and setters
    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    /**
     * Returns a copy of the accounts list to avoid exposing internal synchronized list.
     */
    public List<Account> getAccounts() {
        synchronized (accounts) {
            return new ArrayList<>(accounts);
        }
    }

    public void setFirstName(String firstName) { this.firstName = firstName != null ? firstName : ""; }
    public void setSurname(String surname) { this.surname = surname != null ? surname : ""; }
    public void setAddress(String address) { this.address = address != null ? address : ""; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber != null ? phoneNumber : ""; }
    public void setEmail(String email) { this.email = email != null ? email : ""; }

    @Override
    public String toString() {
        return "Customer{" +
               "customerId='" + customerId + '\'' +
               ", name='" + firstName + " " + surname + '\'' +
               ", phone='" + phoneNumber + '\'' +
               ", email='" + email + '\'' +
               ", accounts=" + accounts.size() +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer that = (Customer) o;
        return Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}