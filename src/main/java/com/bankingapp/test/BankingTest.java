package com.bankingapp.test;
import com.bankingapp.model.*;
import com.bankingapp.enums.CustomerType;

public class BankingTest {
    public static void main(String[] args) {
        System.out.println("=== Banking System Core Model Test ===\n");

        // Test 1: Create Customers
        System.out.println("1. Creating Customers...");
        Customer individualCustomer = new Customer(
            "CUST001", "John", "Doe", "123 Main St",
            "555-0101", "john@email.com", CustomerType.INDIVIDUAL,
            "john123", "password123"
        );

        Customer companyCustomer = new Customer(
            "CUST002", "Tech Corp", "Ltd", "456 Business Ave",
            "555-0102", "info@techcorp.com", CustomerType.COMPANY,
            "techcorp", "secure456"
        );

        // Test 2: Create Bank Teller
        System.out.println("\n2. Creating Bank Teller...");
        BankTeller teller = new BankTeller(
            "EMP001", "Main Branch", "Senior Teller",
            "teller01", "tellerpass"
        );

        // Test 3: Create Accounts
        System.out.println("\n3. Creating Accounts...");
        SavingsAccount johnSavings = new SavingsAccount("SAV001", 1000.0, "Main Branch");
        InvestmentAccount johnInvestment = new InvestmentAccount("INV001", 600.0, "Main Branch");
        ChequeAccount techCorpCheque = new ChequeAccount("CHQ001", 5000.0, "Main Branch",
                                                       "Tech Corp", "456 Business Ave");

        // Test 4: Associate Accounts with Customers
        System.out.println("\n4. Associating Accounts with Customers...");
        individualCustomer.addAccount(johnSavings);
        individualCustomer.addAccount(johnInvestment);
        companyCustomer.addAccount(techCorpCheque);

        // Test 5: Test Login
        System.out.println("\n5. Testing Login...");
        individualCustomer.login("john123", "password123");
        teller.login("teller01", "tellerpass");

        // Test 6: Test Deposits
        System.out.println("\n6. Testing Deposits...");
        johnSavings.deposit(500.0);
        johnInvestment.deposit(200.0);
        techCorpCheque.deposit(1000.0);

        // Test 7: Test Withdrawals
        System.out.println("\n7. Testing Withdrawals...");
        johnSavings.withdraw(100.0);  // Should fail (Savings account)
        johnInvestment.withdraw(100.0); // Should succeed
        techCorpCheque.withdraw(500.0); // Should succeed

        // Test 8: Test Interest Calculation
        System.out.println("\n8. Testing Interest Calculation...");
        johnSavings.calculateInterest();  // Individual rate (2.5%)
        johnInvestment.calculateInterest(); // Investment rate (5%)
        techCorpCheque.calculateInterest(); // No interest for cheque accounts

        // Test 9: Test Monthly Fee for Cheque Account
        System.out.println("\n9. Testing Monthly Fee...");
        techCorpCheque.deductMonthlyFee();

        // Test 10: Display Final Balances
        System.out.println("\n10. Final Account Balances:");
        System.out.println("John's Savings: $" + johnSavings.getBalance());
        System.out.println("John's Investment: $" + johnInvestment.getBalance());
        System.out.println("Tech Corp Cheque: $" + techCorpCheque.getBalance());

        // Test 11: Transaction History
        System.out.println("\n11. Transaction History for John's Savings:");
        johnSavings.printTransactionHistory();

        System.out.println("\n=== Core Model Test Completed ===");
    }
}