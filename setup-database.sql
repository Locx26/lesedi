-- SecureTrust Banking System Database Setup
-- Complete sample data with 10 customers as required

-- Clean existing data (if any)
DELETE FROM accounts;
DELETE FROM customers;

-- Insert 10 sample customers as required by assignment
INSERT INTO customers (customer_id, first_name, surname, address, phone_number, email) VALUES
('CUST001', 'John', 'Doe', '123 Main Street, Gaborone', '71123456', 'john.doe@email.com'),
('CUST002', 'Jane', 'Smith', '456 Broad Street, Francistown', '72123456', 'jane.smith@email.com'),
('CUST003', 'Bob', 'Johnson', '789 Market Road, Maun', '73123456', 'bob.johnson@email.com'),
('CUST004', 'Alice', 'Brown', '321 Park Avenue, Palapye', '74123456', 'alice.brown@email.com'),
('CUST005', 'Charlie', 'Wilson', '654 Oak Lane, Serowe', '75123456', 'charlie.wilson@email.com'),
('CUST006', 'Diana', 'Davis', '987 Pine Street, Molepolole', '76123456', 'diana.davis@email.com'),
('CUST007', 'Edward', 'Miller', '147 Elm Road, Kanye', '77123456', 'edward.miller@email.com'),
('CUST008', 'Fiona', 'Taylor', '258 Maple Avenue, Mochudi', '78123456', 'fiona.taylor@email.com'),
('CUST009', 'George', 'Anderson', '369 Cedar Street, Mahalapye', '79123456', 'george.anderson@email.com'),
('CUST010', 'Helen', 'Thomas', '741 Birch Road, Lobatse', '70123456', 'helen.thomas@email.com');

-- Insert sample accounts for the customers
-- Customer 1: John Doe (2 accounts)
INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES
('SAV001', 'CUST001', 'SAVINGS', 1500.00, 'Main Branch', NULL, NULL),
('INV001', 'CUST001', 'INVESTMENT', 5000.00, 'Main Branch', NULL, NULL);

-- Customer 2: Jane Smith (1 account)
INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES
('CHQ001', 'CUST002', 'CHEQUE', 2500.00, 'City Branch', 'Tech Solutions Ltd', 'Gaborone');

-- Customer 3: Bob Johnson (3 accounts)
INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES
('SAV002', 'CUST003', 'SAVINGS', 800.00, 'Main Branch', NULL, NULL),
('INV002', 'CUST003', 'INVESTMENT', 3000.00, 'Main Branch', NULL, NULL),
('CHQ002', 'CUST003', 'CHEQUE', 1200.00, 'Main Branch', 'Construction Corp', 'Francistown');

-- Customer 4: Alice Brown (2 accounts)
INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES
('SAV003', 'CUST004', 'SAVINGS', 2200.00, 'West Branch', NULL, NULL),
('INV003', 'CUST004', 'INVESTMENT', 7500.00, 'West Branch', NULL, NULL);

-- Customer 5: Charlie Wilson (1 account)
INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES
('CHQ003', 'CUST005', 'CHEQUE', 3200.00, 'South Branch', 'Finance Bank', 'Gaborone');

-- Customer 6: Diana Davis (2 accounts)
INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES
('SAV004', 'CUST006', 'SAVINGS', 1200.00, 'North Branch', NULL, NULL),
('INV004', 'CUST006', 'INVESTMENT', 4500.00, 'North Branch', NULL, NULL);

-- Customer 7: Edward Miller (1 account)
INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES
('CHQ004', 'CUST007', 'CHEQUE', 1800.00, 'Main Branch', 'Education Department', 'Palapye');

-- Customer 8: Fiona Taylor (2 accounts)
INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES
('SAV005', 'CUST008', 'SAVINGS', 950.00, 'East Branch', NULL, NULL),
('INV005', 'CUST008', 'INVESTMENT', 6000.00, 'East Branch', NULL, NULL);

-- Customer 9: George Anderson (1 account)
INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES
('CHQ005', 'CUST009', 'CHEQUE', 2750.00, 'Central Branch', 'Healthcare Inc', 'Mahalapye');

-- Customer 10: Helen Thomas (2 accounts)
INSERT INTO accounts (account_number, customer_id, account_type, balance, branch, employer, employer_address) VALUES
('SAV006', 'CUST010', 'SAVINGS', 1800.00, 'Main Branch', NULL, NULL),
('INV006', 'CUST010', 'INVESTMENT', 5200.00, 'Main Branch', NULL, NULL);

-- Display inserted data for verification
SELECT '=== CUSTOMERS ===' as '';
SELECT * FROM customers;

SELECT '=== ACCOUNTS ===' as '';
SELECT a.account_number, a.account_type, c.first_name, c.surname, a.balance, a.branch
FROM accounts a
JOIN customers c ON a.customer_id = c.customer_id
ORDER BY a.account_number;

SELECT '=== SUMMARY ===' as '';
SELECT
    COUNT(DISTINCT customer_id) as total_customers,
    COUNT(*) as total_accounts,
    SUM(balance) as total_assets,
    AVG(balance) as average_balance
FROM accounts;