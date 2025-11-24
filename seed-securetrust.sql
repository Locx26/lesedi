-- db/seed_securetrust.sql
-- SecureTrust Banking System — schema + sample data (10 customers, sample accounts)
-- Safe to run repeatedly: drops existing tables and recreates them, then inserts sample data.
-- Uses DECIMAL for money, enforces FK constraints and account_type check.

BEGIN;

-- Drop existing tables if present (order matters because of FK)
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS customers;

-- Customers table
CREATE TABLE customers (
    customer_id   VARCHAR(20) PRIMARY KEY,
    first_name    VARCHAR(100) NOT NULL,
    surname       VARCHAR(100) NOT NULL,
    address       VARCHAR(255),
    phone_number  VARCHAR(20),
    email         VARCHAR(100),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Accounts table
CREATE TABLE accounts (
    account_number    VARCHAR(20) PRIMARY KEY,
    customer_id       VARCHAR(20) NOT NULL,
    account_type      VARCHAR(20) NOT NULL CHECK (account_type IN ('SAVINGS','INVESTMENT','CHEQUE')),
    balance           DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    branch            VARCHAR(100),
    employer          VARCHAR(150),
    employer_address  VARCHAR(255),
    opened_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_accounts_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- Transactions table (optional audit/history)
CREATE TABLE transactions (
    transaction_id  VARCHAR(50) PRIMARY KEY,
    account_number  VARCHAR(20) NOT NULL,
    timestamp       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description     VARCHAR(255),
    amount          DECIMAL(19,2) NOT NULL,
    transaction_type VARCHAR(10), -- 'DEBIT' or 'CREDIT'
    category        VARCHAR(50),
    status          VARCHAR(20),
    CONSTRAINT fk_txn_account FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE
);

-- Clean existing data (if any) - redundant after DROP, but kept for safety in other flows
DELETE FROM transactions;
DELETE FROM accounts;
DELETE FROM customers;

-- Insert 10 sample customers
INSERT INTO customers (customer_id, first_name, surname, address, phone_number, email) VALUES
('CUST001', 'John',    'Doe',     '123 Main Street, Gaborone',            '71123456', 'john.doe@email.com'),
('CUST002', 'Jane',    'Smith',   '456 Broad Street, Francistown',        '72123456', 'jane.smith@email.com'),
('CUST003', 'Bob',     'Johnson', '789 Market Road, Maun',                '73123456', 'bob.johnson@email.com'),
('CUST004', 'Alice',   'Brown',   '321 Park Avenue, Palapye',             '74123456', 'alice.brown@email.com'),
('CUST005', 'Charlie', 'Wilson',  '654 Oak Lane, Serowe',                 '75123456', 'charlie.wilson@email.com'),
('CUST006', 'Diana',   'Davis',   '987 Pine Street, Molepolole',          '76123456', 'diana.davis@email.com'),
('CUST007', 'Edward',  'Miller',  '147 Elm Road, Kanye',                  '77123456', 'edward.miller@email.com'),
('CUST008', 'Fiona',   'Taylor',  '258 Maple Avenue, Mochudi',            '78123456', 'fiona.taylor@email.com'),
('CUST009', 'George',  'Anderson','369 Cedar Street, Mahalapye',        '79123456', 'george.anderson@email.com'),
('CUST010', 'Helen',   'Thomas',  '741 Birch Road, Lobatse',              '70123456', 'helen.thomas@email.com');

-- Insert sample accounts for the customers
INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES
-- Customer 1: John Doe (2 accounts)
('SAV001', 'CUST001', 'SAVINGS',    1500.00, 'Main Branch', NULL, NULL),
('INV001', 'CUST001', 'INVESTMENT', 5000.00, 'Main Branch', NULL, NULL),

-- Customer 2: Jane Smith (1 account)
('CHQ001', 'CUST002', 'CHEQUE',     2500.00, 'City Branch', 'Tech Solutions Ltd', 'Gaborone'),

-- Customer 3: Bob Johnson (3 accounts)
('SAV002', 'CUST003', 'SAVINGS',    800.00,  'Main Branch', NULL, NULL),
('INV002', 'CUST003', 'INVESTMENT', 3000.00, 'Main Branch', NULL, NULL),
('CHQ002', 'CUST003', 'CHEQUE',     1200.00, 'Main Branch', 'Construction Corp', 'Francistown'),

-- Customer 4: Alice Brown (2 accounts)
('SAV003', 'CUST004', 'SAVINGS',    2200.00, 'West Branch', NULL, NULL),
('INV003', 'CUST004', 'INVESTMENT', 7500.00, 'West Branch', NULL, NULL),

-- Customer 5: Charlie Wilson (1 account)
('CHQ003', 'CUST005', 'CHEQUE',     3200.00, 'South Branch', 'Finance Bank', 'Gaborone'),

-- Customer 6: Diana Davis (2 accounts)
('SAV004', 'CUST006', 'SAVINGS',    1200.00, 'North Branch', NULL, NULL),
('INV004', 'CUST006', 'INVESTMENT', 4500.00, 'North Branch', NULL, NULL),

-- Customer 7: Edward Miller (1 account)
('CHQ004', 'CUST007', 'CHEQUE',     1800.00, 'Main Branch', 'Education Department', 'Palapye'),

-- Customer 8: Fiona Taylor (2 accounts)
('SAV005', 'CUST008', 'SAVINGS',    950.00,  'East Branch', NULL, NULL),
('INV005', 'CUST008', 'INVESTMENT', 6000.00, 'East Branch', NULL, NULL),

-- Customer 9: George Anderson (1 account)
('CHQ005', 'CUST009', 'CHEQUE',     2750.00, 'Central Branch', 'Healthcare Inc', 'Mahalapye'),

-- Customer 10: Helen Thomas (2 accounts)
('SAV006', 'CUST010', 'SAVINGS',    1800.00, 'Main Branch', NULL, NULL),
('INV006', 'CUST010', 'INVESTMENT', 5200.00, 'Main Branch', NULL, NULL);

-- Optionally insert example transactions (few records) for visibility
INSERT INTO transactions (transaction_id, account_number, description, amount, transaction_type, category, status) VALUES
('TXN-1','SAV001','Initial deposit',1500.00,'CREDIT','Opening','COMPLETED'),
('TXN-2','INV001','Initial deposit',5000.00,'CREDIT','Opening','COMPLETED'),
('TXN-3','CHQ001','Salary credit',2500.00,'CREDIT','Salary','COMPLETED');

COMMIT;

-- Verification queries
SELECT '=== CUSTOMERS ===' AS info;
SELECT * FROM customers ORDER BY customer_id;

SELECT '=== ACCOUNTS ===' AS info;
SELECT a.account_number, a.account_type, c.first_name, c.surname, a.balance, a.branch
FROM accounts a
JOIN customers c ON a.customer_id = c.customer_id
ORDER BY a.account_number;

SELECT '=== SUMMARY ===' AS info;
SELECT
    COUNT(DISTINCT customer_id) AS total_customers,
    COUNT(*) AS total_accounts,
    SUM(balance) AS total_assets,
    ROUND(AVG(balance),2) AS average_balance
FROM accounts;