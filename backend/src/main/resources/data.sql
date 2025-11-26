MERGE INTO customer (name, email, phone) KEY (email) VALUES
('John Doe', 'john@securetrust.com', '+1234567890'),
('Jane Smith', 'jane@securetrust.com', '+0987654321');

MERGE INTO account (customer_id, account_type, account_number, balance) KEY (account_number) VALUES
(1, 'SAVINGS', 'SB1001', 12500.00),
(1, 'CHECKING', 'CK1001', 8500.50),
(2, 'SAVINGS', 'SB1002', 34200.75);

