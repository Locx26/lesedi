package com.bankingapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Professional Database Connection Manager
 * Handles database connections with connection pooling and proper resource management
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:h2:mem:banking;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
   
    private static boolean initialized = false;

    /**
     * Get database connection with auto-initialization
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
       
        if (!initialized) {
            initializeDatabase(conn);
            initialized = true;
        }
       
        return conn;
    }

    /**
     * Initialize database schema and sample data
     */
    private static void initializeDatabase(Connection conn) throws SQLException {
        System.out.println("🔧 Initializing database schema...");
       
        // Create customers table
        String createCustomersTable = """
            CREATE TABLE IF NOT EXISTS customers (
                customer_id VARCHAR(50) PRIMARY KEY,
                first_name VARCHAR(100) NOT NULL,
                surname VARCHAR(100) NOT NULL,
                address VARCHAR(255),
                phone_number VARCHAR(20) NOT NULL,
                email VARCHAR(100),
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
       
        // Create accounts table
        String createAccountsTable = """
            CREATE TABLE IF NOT EXISTS accounts (
                account_number VARCHAR(50) PRIMARY KEY,
                customer_id VARCHAR(50) NOT NULL,
                account_type VARCHAR(20) NOT NULL,
                balance DECIMAL(15,2) DEFAULT 0.00,
                branch VARCHAR(100),
                date_opened TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                employer VARCHAR(100),
                employer_address VARCHAR(255),
                FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
            )
        """;
       
        // Create transactions table for audit trail
        String createTransactionsTable = """
            CREATE TABLE IF NOT EXISTS transactions (
                transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                account_number VARCHAR(50) NOT NULL,
                transaction_type VARCHAR(20) NOT NULL,
                amount DECIMAL(15,2) NOT NULL,
                balance_after DECIMAL(15,2) NOT NULL,
                transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                description VARCHAR(255),
                FOREIGN KEY (account_number) REFERENCES accounts(account_number)
            )
        """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createCustomersTable);
            stmt.execute(createAccountsTable);
            stmt.execute(createTransactionsTable);
           
            // Insert sample data
            insertSampleData(stmt);
           
            System.out.println("✅ Database initialized successfully");
        } catch (SQLException e) {
            System.err.println("❌ Database initialization failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Insert comprehensive sample data for testing and demonstration
     */
    private static void insertSampleData(Statement stmt) throws SQLException {
        System.out.println("📊 Inserting sample data...");
       
        // Clear any existing data
        stmt.execute("DELETE FROM transactions");
        stmt.execute("DELETE FROM accounts");
        stmt.execute("DELETE FROM customers");

        // Insert 10 sample customers as required by assignment
        String[] customerInserts = {
            "INSERT INTO customers (customer_id, first_name, surname, address, phone_number, email) VALUES " +
            "('CUST001', 'John', 'Doe', '123 Main Street, Gaborone', '71123456', 'john.doe@email.com')",
           
            "INSERT INTO customers (customer_id, first_name, surname, address, phone_number, email) VALUES " +
            "('CUST002', 'Jane', 'Smith', '456 Broad Street, Francistown', '72123456', 'jane.smith@email.com')",
           
            "INSERT INTO customers (customer_id, first_name, surname, address, phone_number, email) VALUES " +
            "('CUST003', 'Bob', 'Johnson', '789 Market Road, Maun', '73123456', 'bob.johnson@email.com')",
           
            "INSERT INTO customers (customer_id, first_name, surname, address, phone_number, email) VALUES " +
            "('CUST004', 'Alice', 'Brown', '321 Park Avenue, Palapye', '74123456', 'alice.brown@email.com')",
           
            "INSERT INTO customers (customer_id, first_name, surname, address, phone_number, email) VALUES " +
            "('CUST005', 'Charlie', 'Wilson', '654 Oak Lane, Serowe', '75123456', 'charlie.wilson@email.com')",
           
            "INSERT INTO customers (customer_id, first_name, surname, address, phone_number, email) VALUES " +
            "('CUST006', 'Diana', 'Davis', '987 Pine Street, Molepolole', '76123456', 'diana.davis@email.com')",
           
            "INSERT INTO customers (customer_id, first_name, surname, address, phone_number, email) VALUES " +
            "('CUST007', 'Edward', 'Miller', '147 Elm Road, Kanye', '77123456', 'edward.miller@email.com')",
           
            "INSERT INTO customers (customer_id, first_name, surname, address, phone_number, email) VALUES " +
            "('CUST008', 'Fiona', 'Taylor', '258 Maple Avenue, Mochudi', '78123456', 'fiona.taylor@email.com')",
           
            "INSERT INTO customers (customer_id, first_name, surname, address, phone_number, email) VALUES " +
            "('CUST009', 'George', 'Anderson', '369 Cedar Street, Mahalapye', '79123456', 'george.anderson@email.com')",
           
            "INSERT INTO customers (customer_id, first_name, surname, address, phone_number, email) VALUES " +
            "('CUST010', 'Helen', 'Thomas', '741 Birch Road, Lobatse', '70123456', 'helen.thomas@email.com')"
        };

        // Insert sample accounts
        String[] accountInserts = {
            // Customer 1: John Doe (2 accounts)
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch) VALUES " +
            "('SAV001', 'CUST001', 'SAVINGS', 1500.00, 'Main Branch')",
           
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch) VALUES " +
            "('INV001', 'CUST001', 'INVESTMENT', 5000.00, 'Main Branch')",
           
            // Customer 2: Jane Smith (1 account)
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES " +
            "('CHQ001', 'CUST002', 'CHEQUE', 2500.00, 'City Branch', 'Tech Solutions Ltd', 'Gaborone')",
           
            // Customer 3: Bob Johnson (3 accounts)
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch) VALUES " +
            "('SAV002', 'CUST003', 'SAVINGS', 800.00, 'Main Branch')",
           
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch) VALUES " +
            "('INV002', 'CUST003', 'INVESTMENT', 3000.00, 'Main Branch')",
           
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES " +
            "('CHQ002', 'CUST003', 'CHEQUE', 1200.00, 'Main Branch', 'Construction Corp', 'Francistown')",
           
            // Customer 4: Alice Brown (2 accounts)
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch) VALUES " +
            "('SAV003', 'CUST004', 'SAVINGS', 2200.00, 'West Branch')",
           
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch) VALUES " +
            "('INV003', 'CUST004', 'INVESTMENT', 7500.00, 'West Branch')",
           
            // Customer 5: Charlie Wilson (1 account)
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES " +
            "('CHQ003', 'CUST005', 'CHEQUE', 3200.00, 'South Branch', 'Finance Bank', 'Gaborone')",
           
            // Customer 6: Diana Davis (2 accounts)
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch) VALUES " +
            "('SAV004', 'CUST006', 'SAVINGS', 1200.00, 'North Branch')",
           
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch) VALUES " +
            "('INV004', 'CUST006', 'INVESTMENT', 4500.00, 'North Branch')",
           
            // Customer 7: Edward Miller (1 account)
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES " +
            "('CHQ004', 'CUST007', 'CHEQUE', 1800.00, 'Main Branch', 'Education Department', 'Palapye')",
           
            // Customer 8: Fiona Taylor (2 accounts)
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch) VALUES " +
            "('SAV005', 'CUST008', 'SAVINGS', 950.00, 'East Branch')",
           
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch) VALUES " +
            "('INV005', 'CUST008', 'INVESTMENT', 6000.00, 'East Branch')",
           
            // Customer 9: George Anderson (1 account)
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES " +
            "('CHQ005', 'CUST009', 'CHEQUE', 2750.00, 'Central Branch', 'Healthcare Inc', 'Mahalapye')",
           
            // Customer 10: Helen Thomas (2 accounts)
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch) VALUES " +
            "('SAV006', 'CUST010', 'SAVINGS', 1800.00, 'Main Branch')",
           
            "INSERT INTO accounts (account_number, customer_id, account_type, balance, branch) VALUES " +
            "('INV006', 'CUST010', 'INVESTMENT', 5200.00, 'Main Branch')"
        };

        // Insert sample transactions
        String[] transactionInserts = {
            "INSERT INTO transactions (account_number, transaction_type, amount, balance_after, description) VALUES " +
            "('SAV001', 'DEPOSIT', 1500.00, 1500.00, 'Initial account opening deposit')",
           
            "INSERT INTO transactions (account_number, transaction_type, amount, balance_after, description) VALUES " +
            "('INV001', 'DEPOSIT', 5000.00, 5000.00, 'Investment account initial deposit')",
           
            "INSERT INTO transactions (account_number, transaction_type, amount, balance_after, description) VALUES " +
            "('CHQ001', 'DEPOSIT', 2500.00, 2500.00, 'Salary account opening')",
           
            "INSERT INTO transactions (account_number, transaction_type, amount, balance_after, description) VALUES " +
            "('SAV002', 'DEPOSIT', 800.00, 800.00, 'Savings account deposit')"
        };

        // Execute all inserts
        try {
            for (String sql : customerInserts) {
                stmt.execute(sql);
            }
           
            for (String sql : accountInserts) {
                stmt.execute(sql);
            }
           
            for (String sql : transactionInserts) {
                stmt.execute(sql);
            }
           
            System.out.println("✅ Sample data inserted: 10 customers, " + accountInserts.length + " accounts");
           
        } catch (SQLException e) {
            System.err.println("❌ Error inserting sample data: " + e.getMessage());
            // Don't throw - we can continue with existing data
        }
    }

    /**
     * Test database connection and return status
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("❌ Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get database statistics
     */
    public static String getDatabaseStats() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
           
            int customerCount = 0;
            int accountCount = 0;
            double totalBalance = 0;
           
            var rs1 = stmt.executeQuery("SELECT COUNT(*) FROM customers");
            if (rs1.next()) customerCount = rs1.getInt(1);
           
            var rs2 = stmt.executeQuery("SELECT COUNT(*), SUM(balance) FROM accounts");
            if (rs2.next()) {
                accountCount = rs2.getInt(1);
                totalBalance = rs2.getDouble(2);
            }
           
            return String.format("""
                📊 Database Statistics:
                • Customers: %d
                • Accounts: %d
                • Total Assets: BWP %,.2f
                • Average Balance: BWP %,.2f
                """, customerCount, accountCount, totalBalance, totalBalance / accountCount);
           
        } catch (SQLException e) {
            return "❌ Unable to retrieve database statistics: " + e.getMessage();
        }
    }
}