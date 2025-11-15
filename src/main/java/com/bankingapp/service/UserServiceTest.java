package com.bankingapp.service;

import com.bankingapp.enums.UserRole;
import com.bankingapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * User Service Test - Tests user-related business logic
 */
public class UserServiceTest {
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
    }

    @Test
    public void testAuthenticateValidCustomer() {
        boolean result = userService.authenticate("john_doe", "password123", UserRole.CUSTOMER);
        assertTrue(result, "Valid customer authentication should succeed");
       
        User currentUser = userService.getCurrentUser();
        assertNotNull(currentUser, "Current user should be set after authentication");
        assertEquals("John", currentUser.getFirstName(), "Authenticated user should have correct first name");
    }

    @Test
    public void testAuthenticateInvalidPassword() {
        boolean result = userService.authenticate("john_doe", "wrongpassword", UserRole.CUSTOMER);
        assertFalse(result, "Authentication with wrong password should fail");
    }

    @Test
    public void testAuthenticateWrongRole() {
        boolean result = userService.authenticate("john_doe", "password123", UserRole.BANK_TELLER);
        assertFalse(result, "Authentication with wrong role should fail");
    }

    @Test
    public void testRegisterNewCustomer() {
        User newUser = userService.registerCustomer("newuser", "password123",
                                                   "New", "User", "new@email.com", "555-1234");
       
        assertNotNull(newUser, "New user should be created successfully");
        assertEquals("New", newUser.getFirstName(), "New user should have correct first name");
        assertEquals(UserRole.CUSTOMER, newUser.getRole(), "New user should have CUSTOMER role");
       
        // Verify authentication works for new user
        boolean authResult = userService.authenticate("newuser", "password123", UserRole.CUSTOMER);
        assertTrue(authResult, "New user should be able to authenticate");
    }

    @Test
    public void testRegisterDuplicateUsername() {
        userService.registerCustomer("duplicate", "password123", "First", "User", "first@email.com", "555-1111");
       
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerCustomer("duplicate", "password123", "Second", "User", "second@email.com", "555-2222");
        });
       
        assertTrue(exception.getMessage().contains("already exists"),
                  "Should throw exception for duplicate username");
    }

    @Test
    public void testGetUserAccounts() {
        userService.authenticate("john_doe", "password123", UserRole.CUSTOMER);
        User user = userService.getCurrentUser();
       
        var accounts = userService.getUserAccounts(user.getUserId());
        assertFalse(accounts.isEmpty(), "User should have accounts");
        assertEquals(2, accounts.size(), "John Doe should have 2 accounts");
    }

    @Test
    public void testLogout() {
        userService.authenticate("john_doe", "password123", UserRole.CUSTOMER);
        assertNotNull(userService.getCurrentUser(), "User should be logged in");
       
        userService.logout();
        assertNull(userService.getCurrentUser(), "User should be logged out");
    }

    @Test
    public void testGetAllCustomers() {
        var customers = userService.getAllCustomers();
        assertFalse(customers.isEmpty(), "Should return some customers");
       
        // All returned users should be customers
        for (User customer : customers) {
            assertEquals(UserRole.CUSTOMER, customer.getRole(),
                        "All users in getAllCustomers should have CUSTOMER role");
        }
    }
}