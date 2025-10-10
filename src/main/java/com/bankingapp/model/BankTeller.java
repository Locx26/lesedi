package com.bankingapp.model;

import com.bankingapp.enums.CustomerType;

public class BankTeller extends User {

    private String employeeId;
    private String branch;
    private String position;

    public BankTeller(String employeeId, String branch, String position,
                     String userId, String password) {
        super(userId, password);
        this.employeeId = employeeId;
        this.branch = branch;
        this.position = position;
    }

    @Override
    public boolean login(String username, String password) {
        if (this.userId.equals(username) && verifyPassword(password, this.passwordHash)) {
            System.out.println("Bank Teller " + employeeId + " logged in successfully");
            return true;
        }
        System.out.println("Login failed for teller: " + username);
        return false;
    }

    @Override
    public void logout() {
        System.out.println("Bank Teller " + employeeId + " logged out");
    }

    public Customer onboardCustomer(String customerId, String firstName, String lastName,
                                   String address, String phoneNumber, String email,
                                   CustomerType customerType, String username, String password) {
        Customer newCustomer = new Customer(customerId, firstName, lastName, address,
                                          phoneNumber, email, customerType, username, password);
        System.out.println("New customer onboarded: " + firstName + " " + lastName);
        return newCustomer;
    }

    public String getEmployeeId() { return employeeId; }
    public String getBranch() { return branch; }
    public String getPosition() { return position; }
}
