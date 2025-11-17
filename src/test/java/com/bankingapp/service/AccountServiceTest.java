package com.bankingapp.service;

import com.bankingapp.enums.AccountType;
import com.bankingapp.enums.TransactionType;
import com.bankingapp.model.Account;
import com.bankingapp.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Account Service Test - Tests account-related business logic
 */
public class AccountServiceTest {
    private UserService userService;
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
        accountService = new AccountService(userService);
       
        // Authenticate as a customer to get user ID
        userService.authenticate("john_doe", "password123", com.bankingapp.enums.UserRole.CUSTOMER);
    }

    @Test
    public void testCreateAccount() {
        String userId = userService.getCurrentUser().getUserId();
        Account newAccount = accountService.createAccount(userId, AccountType.SAVINGS, 100.0);
       
        assertNotNull(newAccount, "New account should be created successfully");
        assertEquals(AccountType.SAVINGS, newAccount.getAccountType(), "Account should have correct type");
        assertEquals(100.0, newAccount.getBalance(), 0.001, "Account should have correct initial balance");
        assertTrue(newAccount.getAccountNumber().startsWith("SAV-"), "Savings account number should start with SAV-");
    }

    @Test
    public void testCreateAccountWithNegativeDeposit() {
        String userId = userService.getCurrentUser().getUserId();
       
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount(userId, AccountType.SAVINGS, -50.0);
        });
       
        assertTrue(exception.getMessage().contains("cannot be negative"),
                  "Should throw exception for negative initial deposit");
    }

    @Test
    public void testDeposit() {
        String userId = userService.getCurrentUser().getUserId();
        Account account = accountService.createAccount(userId, AccountType.CHECKING, 100.0);
       
        boolean result = accountService.deposit(account.getAccountId(), 50.0, "Test deposit");
        assertTrue(result, "Deposit should succeed");
       
        double newBalance = accountService.getAccountBalance(account.getAccountId());
        assertEquals(150.0, newBalance, 0.001, "Balance should be updated after deposit");
    }

    @Test
    public void testWithdraw() {
        String userId = userService.getCurrentUser().getUserId();
        Account account = accountService.createAccount(userId, AccountType.CHECKING, 100.0);
       
        boolean result = accountService.withdraw(account.getAccountId(), 30.0, "Test withdrawal");
        assertTrue(result, "Withdrawal should succeed with sufficient funds");
       
        double newBalance = accountService.getAccountBalance(account.getAccountId());
        assertEquals(70.0, newBalance, 0.001, "Balance should be updated after withdrawal");
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        String userId = userService.getCurrentUser().getUserId();
        Account account = accountService.createAccount(userId, AccountType.CHECKING, 50.0);
       
        boolean result = accountService.withdraw(account.getAccountId(), 100.0, "Test withdrawal");
        assertFalse(result, "Withdrawal should fail with insufficient funds");
       
        double balance = accountService.getAccountBalance(account.getAccountId());
        assertEquals(50.0, balance, 0.001, "Balance should remain unchanged after failed withdrawal");
    }

    @Test
    public void testTransfer() {
        String userId = userService.getCurrentUser().getUserId();
        Account fromAccount = accountService.createAccount(userId, AccountType.CHECKING, 200.0);
        Account toAccount = accountService.createAccount(userId, AccountType.SAVINGS, 50.0);
       
        boolean result = accountService.transfer(fromAccount.getAccountId(), toAccount.getAccountId(), 75.0, "Test transfer");
        assertTrue(result, "Transfer should succeed with sufficient funds");
       
        double fromBalance = accountService.getAccountBalance(fromAccount.getAccountId());
        double toBalance = accountService.getAccountBalance(toAccount.getAccountId());
       
        assertEquals(125.0, fromBalance, 0.001, "From account balance should be decreased");
        assertEquals(125.0, toBalance, 0.001, "To account balance should be increased");
    }

    @Test
    public void testGetTransactionHistory() {
        String userId = userService.getCurrentUser().getUserId();
        Account account = accountService.createAccount(userId, AccountType.CHECKING, 100.0);
       
        // Perform some transactions
        accountService.deposit(account.getAccountId(), 50.0, "Deposit 1");
        accountService.withdraw(account.getAccountId(), 25.0, "Withdrawal 1");
       
        List<Transaction> transactions = accountService.getTransactionHistory(account.getAccountId());
        assertEquals(3, transactions.size(), "Should have 3 transactions (initial + deposit + withdrawal)");
       
        // Check transaction types
        assertEquals(TransactionType.DEPOSIT, transactions.get(1).getType(), "Second transaction should be deposit");
        assertEquals(TransactionType.WITHDRAWAL, transactions.get(2).getType(), "Third transaction should be withdrawal");
    }

    @Test
    public void testCloseAccount() {
        String userId = userService.getCurrentUser().getUserId();
        Account account = accountService.createAccount(userId, AccountType.CHECKING, 0.0);
       
        boolean result = accountService.closeAccount(account.getAccountId());
        assertTrue(result, "Should be able to close account with zero balance");
        assertFalse(account.isActive(), "Account should be marked as inactive");
    }

    @Test
    public void testCloseAccountWithBalance() {
        String userId = userService.getCurrentUser().getUserId();
        Account account = accountService.createAccount(userId, AccountType.CHECKING, 50.0);
       
        boolean result = accountService.closeAccount(account.getAccountId());
        assertFalse(result, "Should not be able to close account with positive balance");
        assertTrue(account.isActive(), "Account should remain active");
    }
}