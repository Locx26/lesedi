package com.bankingapp.dao;

import com.bankingapp.model.Customer;
import com.bankingapp.model.Account;
import com.bankingapp.model.SavingsAccount;
import com.bankingapp.model.InvestmentAccount;
import com.bankingapp.model.ChequeAccount;
import com.bankingapp.model.AccountType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Professional Data Access Object for Customer operations
 * Implements CRUD operations with proper error handling
 */
public class CustomerDAO {
   
    /**
     * Save a new customer to the database
     */
    public boolean saveCustomer(Customer customer) {
        String sql = "INSERT INTO customers (customer_id, first_name, surname, address, phone_number, email) VALUES (?, ?, ?, ?, ?, ?)";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
            pstmt.setString(1, customer.getCustomerId());
            pstmt.setString(2, customer.getFirstName());
            pstmt.setString(3, customer.getSurname());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getPhoneNumber());
            pstmt.setString(6, customer.getEmail());
           
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
           
        } catch (SQLException e) {
            System.err.println("❌ Error saving customer: " + e.getMessage());
            return false;
        }
    }
   
    /**
     * Retrieve all customers from the database
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY first_name, surname";
       
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
           
            while (rs.next()) {
                Customer customer = extractCustomerFromResultSet(rs);
                if (customer != null) {
                    // Load customer accounts
                    loadCustomerAccounts(customer);
                    customers.add(customer);
                }
            }
           
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving customers: " + e.getMessage());
        }
       
        return customers;
    }
   
    /**
     * Find customer by ID
     */
    public Customer findCustomerById(String customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
           
            if (rs.next()) {
                Customer customer = extractCustomerFromResultSet(rs);
                if (customer != null) {
                    loadCustomerAccounts(customer);
                }
                return customer;
            }
           
        } catch (SQLException e) {
            System.err.println("❌ Error finding customer: " + e.getMessage());
        }
       
        return null;
    }
   
    /**
     * Update customer information
     */
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET first_name = ?, surname = ?, address = ?, phone_number = ?, email = ? WHERE customer_id = ?";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getSurname());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setString(5, customer.getEmail());
            pstmt.setString(6, customer.getCustomerId());
           
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
           
        } catch (SQLException e) {
            System.err.println("❌ Error updating customer: " + e.getMessage());
            return false;
        }
    }
   
    /**
     * Delete customer by ID
     */
    public boolean deleteCustomer(String customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
            pstmt.setString(1, customerId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
           
        } catch (SQLException e) {
            System.err.println("❌ Error deleting customer: " + e.getMessage());
            return false;
        }
    }
   
    /**
     * Search customers by name or phone number
     */
    public List<Customer> searchCustomers(String searchTerm) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE first_name LIKE ? OR surname LIKE ? OR phone_number LIKE ? ORDER BY first_name, surname";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
            String likeTerm = "%" + searchTerm + "%";
            pstmt.setString(1, likeTerm);
            pstmt.setString(2, likeTerm);
            pstmt.setString(3, likeTerm);
           
            ResultSet rs = pstmt.executeQuery();
           
            while (rs.next()) {
                Customer customer = extractCustomerFromResultSet(rs);
                if (customer != null) {
                    loadCustomerAccounts(customer);
                    customers.add(customer);
                }
            }
           
        } catch (SQLException e) {
            System.err.println("❌ Error searching customers: " + e.getMessage());
        }
       
        return customers;
    }
   
    /**
     * Get customer count for statistics
     */
    public int getCustomerCount() {
        String sql = "SELECT COUNT(*) FROM customers";
       
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
           
            if (rs.next()) {
                return rs.getInt(1);
            }
           
        } catch (SQLException e) {
            System.err.println("❌ Error getting customer count: " + e.getMessage());
        }
       
        return 0;
    }
   
    // ==================== PRIVATE HELPER METHODS ====================
   
    /**
     * Extract Customer object from ResultSet
     */
    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        try {
            return new Customer(
                rs.getString("customer_id"),
                rs.getString("first_name"),
                rs.getString("surname"),
                rs.getString("address"),
                rs.getString("phone_number"),
                rs.getString("email")
            );
        } catch (SQLException e) {
            System.err.println("❌ Error extracting customer from result set: " + e.getMessage());
            throw e;
        }
    }
   
    /**
     * Load all accounts for a customer
     */
    private void loadCustomerAccounts(Customer customer) {
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
           
            pstmt.setString(1, customer.getCustomerId());
            ResultSet rs = pstmt.executeQuery();
           
            while (rs.next()) {
                Account account = extractAccountFromResultSet(rs, customer);
                if (account != null) {
                    customer.openAccount(account);
                }
            }
           
        } catch (SQLException e) {
            System.err.println("❌ Error loading customer accounts: " + e.getMessage());
        }
    }
   
    /**
     * Extract Account object from ResultSet
     */
    private Account extractAccountFromResultSet(ResultSet rs, Customer customer) throws SQLException {
        try {
            String accountType = rs.getString("account_type");
            String accountNumber = rs.getString("account_number");
            double balance = rs.getDouble("balance");
            String branch = rs.getString("branch");
           
            switch (accountType.toUpperCase()) {
                case "SAVINGS":
                    return new SavingsAccount(accountNumber, balance, branch, customer);
                   
                case "INVESTMENT":
                    return new InvestmentAccount(accountNumber, balance, branch, customer);
                   
                case "CHEQUE":
                    String employer = rs.getString("employer");
                    String employerAddress = rs.getString("employer_address");
                    return new ChequeAccount(accountNumber, balance, branch, customer, employer, employerAddress);
                   
                default:
                    System.err.println("❌ Unknown account type: " + accountType);
                    return null;
            }
           
        } catch (SQLException e) {
            System.err.println("❌ Error extracting account from result set: " + e.getMessage());
            throw e;
        }
    }
}