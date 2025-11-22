package com.bankingapp.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerId;
    private String firstName;
    private String surname;
    private String address;
    private String phoneNumber;
    private String email;
    private List<Account> accounts;

    public Customer(String customerId, String firstName, String surname, String address,
                   String phoneNumber, String email) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.accounts = new ArrayList<>();
    }

    // Demonstrating polymorphism through interface-like behavior
    public void openAccount(Account account) {
        accounts.add(account);
        System.out.println("Opened " + account.getAccountType() + " account for customer: " + firstName + " " + surname);
    }

    public void depositToAccount(String accountNumber, double amount) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                account.deposit(amount);
                return;
            }
        }
        System.out.println("Account not found: " + accountNumber);
    }

    public boolean withdrawFromAccount(String accountNumber, double amount) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account.withdraw(amount);
            }
        }
        return false;
    }

    // Getters and setters
    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public List<Account> getAccounts() { return accounts; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
}