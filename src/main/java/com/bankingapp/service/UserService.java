package com.bankingapp.service;

import com.bankingapp.enums.UserRole;
import com.bankingapp.model.User;
import com.bankingapp.model.Account;
import java.util.*;

/**
 * User Service - Handles all user-related business logic
 * Authentication, registration, and user management
 */
public class UserService {
    private Map<String, User> users;
    private Map<String, List<Account>> userAccounts;
    private User currentUser;

    public UserService() {
        this.users = new HashMap<>();
        this.userAccounts = new HashMap<>();
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Sample customers
        User customer1 = new User("CUST001", "john_doe", "password123",
                                 "John", "Doe", "john.doe@email.com", "555-0101", UserRole.CUSTOMER);
        User customer2 = new User("CUST002", "jane_smith", "password123",
                                 "Jane", "Smith", "jane.smith@email.com", "555-0102", UserRole.CUSTOMER);
       
        // Sample bank teller
        User teller1 = new User("TELL001", "teller1", "teller123",
                               "Sarah", "Johnson", "sarah.johnson@bank.com", "555-0201", UserRole.BANK_TELLER);
       
        // Sample admin
        User admin1 = new User("ADMIN001", "admin", "admin123",
                              "Michael", "Brown", "michael.brown@bank.com", "555-0301", UserRole.ADMIN);

        // Add users to system
        users.put(customer1.getUsername(), customer1);
        users.put(customer2.getUsername(), customer2);
        users.put(teller1.getUsername(), teller1);
        users.put(admin1.getUsername(), admin1);

        // Initialize sample accounts
        initializeSampleAccounts();
    }

    private void initializeSampleAccounts() {
        // Sample accounts for John Doe
        List<Account> johnAccounts = Arrays.asList(
            new Account("ACC001", "CUST001", "SAV-1234", com.bankingapp.enums.AccountType.SAVINGS, 5250.75),
            new Account("ACC002", "CUST001", "CHK-5678", com.bankingapp.enums.AccountType.CHECKING, 2150.30)
        );
        userAccounts.put("CUST001", johnAccounts);

        // Sample accounts for Jane Smith
        List<Account> janeAccounts = Arrays.asList(
            new Account("ACC003", "CUST002", "SAV-9012", com.bankingapp.enums.AccountType.SAVINGS, 12500.00),
            new Account("ACC004", "CUST002", "BUS-3456", com.bankingapp.enums.AccountType.BUSINESS, 50000.00)
        );
        userAccounts.put("CUST002", janeAccounts);
    }

    /**
     * Authenticate user with username and password
     */
    public boolean authenticate(String username, String password, UserRole expectedRole) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password) && user.getRole() == expectedRole) {
            currentUser = user;
            return true;
        }
        return false;
    }

    /**
     * Register new customer
     */
    public User registerCustomer(String username, String password, String firstName,
                               String lastName, String email, String phone) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        String userId = "CUST" + String.format("%03d", users.size() + 1);
        User newUser = new User(userId, username, password, firstName, lastName, email, phone, UserRole.CUSTOMER);
       
        users.put(username, newUser);
        userAccounts.put(userId, new ArrayList<>());
       
        return newUser;
    }

    /**
     * Get current logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Logout current user
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Get user accounts
     */
    public List<Account> getUserAccounts(String userId) {
        return userAccounts.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        return users.get(username);
    }

    /**
     * Get all customers (for teller/admin use)
     */
    public List<User> getAllCustomers() {
        return users.values().stream()
                .filter(user -> user.getRole() == UserRole.CUSTOMER)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Update user profile
     */
    public boolean updateUserProfile(String userId, String email, String phone) {
        User user = users.values().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
       
        if (user != null) {
            user.setEmail(email);
            user.setPhone(phone);
            return true;
        }
        return false;
    }
}