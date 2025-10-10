package com.bankingapp.model;
import com.bankingapp.enums.CustomerType;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private String customerId;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String email;
    private CustomerType customerType;
    private List<Account> accounts;

    public Customer(String customerId, String firstName, String lastName,
                   String address, String phoneNumber, String email,
                   CustomerType customerType, String userId, String password) {
        super(userId, password);
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.customerType = customerType;
        this.accounts = new ArrayList<>();
    }

    @Override
    public boolean login(String username, String password) {
        if (this.userId.equals(username) && verifyPassword(password, this.passwordHash)) {
            System.out.println("Customer " + firstName + " logged in successfully");
            return true;
        }
        System.out.println("Login failed for customer: " + username);
        return false;
    }

    @Override
    public void logout() {
        System.out.println("Customer " + firstName + " logged out");
    }

    public void addAccount(Account account) {
        if (account != null) {
            accounts.add(account);
            account.setCustomer(this);
            System.out.println("Account " + account.getAccountNumber() + " added to customer " + firstName);
        }
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public Account findAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public CustomerType getCustomerType() { return customerType; }

    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
}