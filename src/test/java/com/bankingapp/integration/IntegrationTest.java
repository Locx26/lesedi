package com.bankingapp.integration;

import com.bankingapp.controller.BankingController;
import com.bankingapp.model.*;
import com.bankingapp.dao.DatabaseConnection;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Comprehensive Integration Test Suite
 * Tests the complete banking system workflow from UI to Database
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTest {
   
    private BankingController controller;
    private String testCustomerId;
    private String testSavingsAccount;
    private String testInvestmentAccount;
    private String testChequeAccount;
   
    @BeforeAll
    void setup() {
        System.out.println("🚀 Starting Integration Test Suite");
        System.out.println("==================================");
       
        // Initialize controller and database
        controller = new BankingController();
       
        // Verify database connection
        assertTrue(DatabaseConnection.testConnection(), "Database connection should be established");
       
        System.out.println("✅ Test environment initialized");
    }
   
    @AfterAll
    void tearDown() {
        System.out.println("==================================");
        System.out.println("🏁 Integration Test Suite Completed");
    }
   
    @Test
    @Order(1)
    @DisplayName("Test Customer Registration")
    void testCustomerRegistration() {
        System.out.println("\n📝 Testing Customer Registration...");
       
        // Test successful customer registration
        String result = controller.registerCustomer(
            "Test", "User", "123 Test Street", "76123456", "test.user@email.com"
        );
       
        assertTrue(result.contains("✅"), "Customer registration should succeed");
        assertTrue(result.contains("Customer registered successfully"),
                  "Should contain success message");
       
        // Extract customer ID from result
        String[] lines = result.split("\n");
        for (String line : lines) {
            if (line.contains("ID:")) {
                testCustomerId = line.split(":")[1].trim();
                break;
            }
        }
       
        assertNotNull(testCustomerId, "Customer ID should be generated");
        assertTrue(testCustomerId.startsWith("CUST"), "Customer ID should start with CUST");
       
        System.out.println("✅ Customer registered: " + testCustomerId);
    }
   
    @Test
    @Order(2)
    @DisplayName("Test Customer Retrieval")
    void testCustomerRetrieval() {
        System.out.println("\n🔍 Testing Customer Retrieval...");
       
        assertNotNull(testCustomerId, "Customer ID should be available");
       
        Customer customer = controller.findCustomerById(testCustomerId);
        assertNotNull(customer, "Customer should be retrievable from database");
        assertEquals("Test", customer.getFirstName(), "First name should match");
        assertEquals("User", customer.getSurname(), "Surname should match");
        assertEquals("76123456", customer.getPhoneNumber(), "Phone number should match");
       
        System.out.println("✅ Customer retrieved successfully: " + customer.getFirstName() + " " + customer.getSurname());
    }
   
    @Test
    @Order(3)
    @DisplayName("Test Savings Account Opening")
    void testSavingsAccountOpening() {
        System.out.println("\n💰 Testing Savings Account Opening...");
       
        assertNotNull(testCustomerId, "Customer ID should be available");
       
        String result = controller.openSavingsAccount(testCustomerId, 1000.00, "Test Branch");
       
        assertTrue(result.contains("✅"), "Savings account opening should succeed");
        assertTrue(result.contains("Savings Account opened successfully"),
                  "Should contain success message");
       
        // Extract account number
        String[] lines = result.split("\n");
        for (String line : lines) {
            if (line.contains("Account Number:")) {
                testSavingsAccount = line.split(":")[1].trim();
                break;
            }
        }
       
        assertNotNull(testSavingsAccount, "Account number should be generated");
        assertTrue(testSavingsAccount.startsWith("ACC"), "Account number should start with ACC");
       
        System.out.println("✅ Savings account opened: " + testSavingsAccount);
    }
   
    @Test
    @Order(4)
    @DisplayName("Test Investment Account Opening")
    void testInvestmentAccountOpening() {
        System.out.println("\n📈 Testing Investment Account Opening...");
       
        assertNotNull(testCustomerId, "Customer ID should be available");
       
        // Test with insufficient funds (should fail)
        String failResult = controller.openInvestmentAccount(testCustomerId, 400.00, "Test Branch");
        assertTrue(failResult.contains("❌"), "Should fail with insufficient funds");
        assertTrue(failResult.contains("minimum BWP 500.00"),
                  "Should mention minimum balance requirement");
       
        // Test with sufficient funds (should succeed)
        String successResult = controller.openInvestmentAccount(testCustomerId, 1000.00, "Test Branch");
        assertTrue(successResult.contains("✅"), "Investment account opening should succeed");
       
        // Extract account number
        String[] lines = successResult.split("\n");
        for (String line : lines) {
            if (line.contains("Account Number:")) {
                testInvestmentAccount = line.split(":")[1].trim();
                break;
            }
        }
       
        assertNotNull(testInvestmentAccount, "Account number should be generated");
       
        System.out.println("✅ Investment account opened: " + testInvestmentAccount);
    }
   
    @Test
    @Order(5)
    @DisplayName("Test Cheque Account Opening")
    void testChequeAccountOpening() {
        System.out.println("\n💳 Testing Cheque Account Opening...");
       
        assertNotNull(testCustomerId, "Customer ID should be available");
       
        String result = controller.openChequeAccount(testCustomerId, 500.00, "Test Branch",
                                                   "Test Company", "Test Address");
       
        assertTrue(result.contains("✅"), "Cheque account opening should succeed");
       
        // Extract account number
        String[] lines = result.split("\n");
        for (String line : lines) {
            if (line.contains("Account Number:")) {
                testChequeAccount = line.split(":")[1].trim();
                break;
            }
        }
       
        assertNotNull(testChequeAccount, "Account number should be generated");
       
        System.out.println("✅ Cheque account opened: " + testChequeAccount);
    }
   
    @Test
    @Order(6)
    @DisplayName("Test Deposit Operations")
    void testDepositOperations() {
        System.out.println("\n💵 Testing Deposit Operations...");
       
        assertNotNull(testSavingsAccount, "Savings account should be available");
       
        // Test deposit to savings account
        String result = controller.processDeposit(testSavingsAccount, 500.00);
        assertTrue(result.contains("✅"), "Deposit should succeed");
        assertTrue(result.contains("Deposit processed successfully"),
                  "Should contain success message");
       
        // Verify balance was updated
        Account account = controller.getAccountByNumber(testSavingsAccount);
        assertNotNull(account, "Account should be retrievable");
        assertEquals(1500.00, account.getBalance(), 0.001,
                    "Account balance should be updated after deposit");
       
        System.out.println("✅ Deposit processed: BWP 500.00 to " + testSavingsAccount);
    }
   
    @Test
    @Order(7)
    @DisplayName("Test Withdrawal Operations")
    void testWithdrawalOperations() {
        System.out.println("\n💸 Testing Withdrawal Operations...");
       
        assertNotNull(testInvestmentAccount, "Investment account should be available");
       
        // Test withdrawal from investment account
        String result = controller.processWithdrawal(testInvestmentAccount, 200.00);
        assertTrue(result.contains("✅"), "Withdrawal should succeed");
       
        // Verify balance was updated
        Account account = controller.getAccountByNumber(testInvestmentAccount);
        assertNotNull(account, "Account should be retrievable");
        assertEquals(800.00, account.getBalance(), 0.001,
                    "Account balance should be updated after withdrawal");
       
        System.out.println("✅ Withdrawal processed: BWP 200.00 from " + testInvestmentAccount);
    }
   
    @Test
    @Order(8)
    @DisplayName("Test Transfer Operations")
    void testTransferOperations() {
        System.out.println("\n🔄 Testing Transfer Operations...");
       
        assertNotNull(testInvestmentAccount, "Investment account should be available");
        assertNotNull(testSavingsAccount, "Savings account should be available");
       
        // Get initial balances
        Account fromAccount = controller.getAccountByNumber(testInvestmentAccount);
        Account toAccount = controller.getAccountByNumber(testSavingsAccount);
        double initialFromBalance = fromAccount.getBalance();
        double initialToBalance = toAccount.getBalance();
       
        // Test transfer
        String result = controller.transferFunds(testInvestmentAccount, testSavingsAccount, 300.00);
        assertTrue(result.contains("✅"), "Transfer should succeed");
       
        // Verify balances were updated
        fromAccount = controller.getAccountByNumber(testInvestmentAccount);
        toAccount = controller.getAccountByNumber(testSavingsAccount);
       
        assertEquals(initialFromBalance - 300.00, fromAccount.getBalance(), 0.001,
                    "Source account balance should decrease by transfer amount");
        assertEquals(initialToBalance + 300.00, toAccount.getBalance(), 0.001,
                    "Destination account balance should increase by transfer amount");
       
        System.out.println("✅ Transfer processed: BWP 300.00 from " + testInvestmentAccount + " to " + testSavingsAccount);
    }
   
    @Test
    @Order(9)
    @DisplayName("Test Interest Calculation")
    void testInterestCalculation() {
        System.out.println("\n📊 Testing Interest Calculation...");
       
        assertNotNull(testSavingsAccount, "Savings account should be available");
        assertNotNull(testInvestmentAccount, "Investment account should be available");
       
        // Get initial balances
        Account savingsAccount = controller.getAccountByNumber(testSavingsAccount);
        Account investmentAccount = controller.getAccountByNumber(testInvestmentAccount);
        double initialSavingsBalance = savingsAccount.getBalance();
        double initialInvestmentBalance = investmentAccount.getBalance();
       
        // Apply interest to savings account
        String savingsResult = controller.applyMonthlyInterest(testSavingsAccount);
        assertTrue(savingsResult.contains("✅"), "Savings interest should be applied");
       
        // Apply interest to investment account
        String investmentResult = controller.applyMonthlyInterest(testInvestmentAccount);
        assertTrue(investmentResult.contains("✅"), "Investment interest should be applied");
       
        // Verify interest was applied correctly
        savingsAccount = controller.getAccountByNumber(testSavingsAccount);
        investmentAccount = controller.getAccountByNumber(testInvestmentAccount);
       
        // Savings interest: 0.05% monthly
        double expectedSavingsInterest = initialSavingsBalance * 0.0005;
        assertEquals(initialSavingsBalance + expectedSavingsInterest, savingsAccount.getBalance(), 0.01,
                    "Savings account should earn 0.05% interest");
       
        // Investment interest: 5% monthly
        double expectedInvestmentInterest = initialInvestmentBalance * 0.05;
        assertEquals(initialInvestmentBalance + expectedInvestmentInterest, investmentAccount.getBalance(), 0.01,
                    "Investment account should earn 5% interest");
       
        System.out.println("✅ Interest applied: Savings +0.05%, Investment +5%");
    }
   
    @Test
    @Order(10)
    @DisplayName("Test Account Restrictions")
    void testAccountRestrictions() {
        System.out.println("\n🚫 Testing Account Restrictions...");
       
        assertNotNull(testSavingsAccount, "Savings account should be available");
       
        // Test withdrawal from savings account (should fail)
        String result = controller.processWithdrawal(testSavingsAccount, 100.00);
        assertTrue(result.contains("❌"), "Withdrawal from savings should fail");
        assertTrue(result.contains("does not allow withdrawals"),
                  "Should mention withdrawal restriction");
       
        System.out.println("✅ Account restrictions enforced correctly");
    }
   
    @Test
    @Order(11)
    @DisplayName("Test System Statistics")
    void testSystemStatistics() {
        System.out.println("\n📈 Testing System Statistics...");
       
        BankingController.SystemStatistics stats = controller.getSystemStatistics();
       
        assertTrue(stats.getTotalCustomers() >= 1, "Should have at least 1 customer");
        assertTrue(stats.getTotalAccounts() >= 3, "Should have at least 3 accounts");
        assertTrue(stats.getTotalAssets() > 0, "Total assets should be positive");
       
        System.out.println("✅ System statistics:");
        System.out.println("   • Customers: " + stats.getTotalCustomers());
        System.out.println("   • Accounts: " + stats.getTotalAccounts());
        System.out.println("   • Total Assets: BWP " + String.format("%,.2f", stats.getTotalAssets()));
    }
   
    @Test
    @Order(12)
    @DisplayName("Test Financial Reporting")
    void testFinancialReporting() {
        System.out.println("\n📋 Testing Financial Reporting...");
       
        String report = controller.generateFinancialReport();
       
        assertNotNull(report, "Report should be generated");
        assertTrue(report.contains("SECURETRUST BANKING SYSTEM"),
                  "Report should contain bank name");
        assertTrue(report.contains("FINANCIAL PERFORMANCE REPORT"),
                  "Report should contain title");
        assertTrue(report.contains("EXECUTIVE SUMMARY"),
                  "Report should contain executive summary");
        assertTrue(report.contains("ACCOUNT DISTRIBUTION"),
                  "Report should contain account distribution");
        assertTrue(report.contains("RECOMMENDATIONS"),
                  "Report should contain recommendations");
       
        System.out.println("✅ Financial report generated successfully");
        System.out.println("Report length: " + report.length() + " characters");
    }
   
    @Test
    @Order(13)
    @DisplayName("Test Bulk Operations")
    void testBulkOperations() {
        System.out.println("\n⚡ Testing Bulk Operations...");
       
        // Test bulk interest application
        String result = controller.applyInterestToAllAccounts();
        assertTrue(result.contains("✅"), "Bulk interest should be processed");
        assertTrue(result.contains("Bulk interest processing completed"),
                  "Should indicate bulk processing completion");
       
        System.out.println("✅ Bulk operations completed successfully");
    }
   
    @Test
    @Order(14)
    @DisplayName("Test Error Handling")
    void testErrorHandling() {
        System.out.println("\n🛡️ Testing Error Handling...");
       
        // Test with invalid account number
        String depositResult = controller.processDeposit("INVALID_ACCOUNT", 100.00);
        assertTrue(depositResult.contains("❌"), "Should fail with invalid account");
        assertTrue(depositResult.contains("Account not found"),
                  "Should indicate account not found");
       
        // Test with negative amount
        String negativeResult = controller.processDeposit(testSavingsAccount, -50.00);
        assertTrue(negativeResult.contains("❌"), "Should fail with negative amount");
        assertTrue(negativeResult.contains("must be greater than zero"),
                  "Should indicate amount must be positive");
       
        // Test with insufficient funds
        String insufficientResult = controller.processWithdrawal(testInvestmentAccount, 100000.00);
        assertTrue(insufficientResult.contains("❌"), "Should fail with insufficient funds");
        assertTrue(insufficientResult.contains("Insufficient funds"),
                  "Should indicate insufficient funds");
       
        System.out.println("✅ Error handling working correctly");
    }
   
    @Test
    @Order(15)
    @DisplayName("Complete Workflow Test")
    void testCompleteWorkflow() {
        System.out.println("\n🎯 Testing Complete Banking Workflow...");
       
        // 1. Register a new customer
        String customerResult = controller.registerCustomer(
            "Workflow", "Test", "456 Workflow Ave", "78123456", "workflow.test@email.com"
        );
        assertTrue(customerResult.contains("✅"), "Customer registration should succeed");
       
        // Extract customer ID
        String workflowCustomerId = null;
        String[] lines = customerResult.split("\n");
        for (String line : lines) {
            if (line.contains("ID:")) {
                workflowCustomerId = line.split(":")[1].trim();
                break;
            }
        }
       
        // 2. Open multiple accounts
        String savingsResult = controller.openSavingsAccount(workflowCustomerId, 2000.00, "Main Branch");
        String investmentResult = controller.openInvestmentAccount(workflowCustomerId, 5000.00, "Main Branch");
       
        assertTrue(savingsResult.contains("✅"), "Savings account should be opened");
        assertTrue(investmentResult.contains("✅"), "Investment account should be opened");
       
        // Extract account numbers
        String workflowSavingsAccount = extractAccountNumber(savingsResult);
        String workflowInvestmentAccount = extractAccountNumber(investmentResult);
       
        // 3. Perform transactions
        String depositResult = controller.processDeposit(workflowSavingsAccount, 1000.00);
        String withdrawalResult = controller.processWithdrawal(workflowInvestmentAccount, 1000.00);
        String transferResult = controller.transferFunds(workflowInvestmentAccount, workflowSavingsAccount, 500.00);
       
        assertTrue(depositResult.contains("✅"), "Deposit should succeed");
        assertTrue(withdrawalResult.contains("✅"), "Withdrawal should succeed");
        assertTrue(transferResult.contains("✅"), "Transfer should succeed");
       
        // 4. Apply interest
        String interestResult = controller.applyMonthlyInterest(workflowSavingsAccount);
        assertTrue(interestResult.contains("✅"), "Interest should be applied");
       
        // 5. Generate report
        String report = controller.generateFinancialReport();
        assertNotNull(report, "Report should be generated");
       
        System.out.println("✅ Complete banking workflow tested successfully");
        System.out.println("   • Customer registered: " + workflowCustomerId);
        System.out.println("   • Accounts opened: 2");
        System.out.println("   • Transactions processed: 3");
        System.out.println("   • Interest applied: Yes");
        System.out.println("   • Report generated: Yes");
    }
   
    private String extractAccountNumber(String result) {
        String[] lines = result.split("\n");
        for (String line : lines) {
            if (line.contains("Account Number:")) {
                return line.split(":")[1].trim();
            }
        }
        return null;
    }
}
