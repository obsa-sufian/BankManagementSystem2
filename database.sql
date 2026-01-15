-- Create database
--DROP DATABASE IF EXISTS BankManagement2DB;
CREATE DATABASE BankManagement2DB;
USE BankManagement2DB;

-- Admin table
CREATE TABLE Admin (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin (password: admin123)
INSERT IGNORE INTO Admin (username, password) VALUES 
('admin', 'admin123');

-- Customer table with login credentials

CREATE TABLE Customer (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address TEXT,
    date_of_birth DATE,
    security_question VARCHAR(255),
    security_answer VARCHAR(255),
    account_locked BOOLEAN DEFAULT FALSE,
    failed_login_attempts INT DEFAULT 0,
    last_login TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Account table
DROP TABLE IF EXISTS Account;
CREATE TABLE Account (
    account_no VARCHAR(20) PRIMARY KEY,
    customer_id INT NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    interest_rate DECIMAL(5,2) DEFAULT 2.5,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(10) DEFAULT 'ACTIVE',
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE
);

--ON BankTransaction table
CREATE TABLE BankTransaction (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    account_no VARCHAR(20) NOT NULL,
    transaction_type ENUM('DEPOSIT', 'WITHDRAW', 'TRANSFER', 'INTEREST'),
    amount DECIMAL(15,2) NOT NULL,
    recipient_account VARCHAR(20),
    description VARCHAR(255),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_no) REFERENCES Account(account_no)
);

-- Create indexes for better performance
-- Check if index exists before creating (MariaDB compatible)
DROP INDEX IF EXISTS idx_account_customer ON Account;
CREATE INDEX idx_account_customer ON Account(customer_id);

DROP INDEX IF EXISTS idx_transaction_account ON BankTransaction;
--CREATE INDEX idx_transaction_account ON Transaction(account_no);
        --fixed--
CREATE INDEX idx_transaction_account ON BankTransaction(account_no);

DROP INDEX IF EXISTS idx_transaction_date ON BankTransaction;
--CREATE INDEX idx_transaction_date ON Transaction(transaction_date);
CREATE INDEX idx_transaction_date ON BankTransaction(transaction_date);

DROP INDEX IF EXISTS idx_customer_username ON Customer;
CREATE INDEX idx_customer_username ON Customer(username);

-- Insert sample customers data for testing with passwords (password: customer123)
INSERT IGNORE INTO Customer (username, password, name, email, phone, address, date_of_birth, security_question, security_answer) VALUES
('obsa_sufian', 'obsa', 'Obsa Sufian', 'obsa@gmail.com', '0911111111', '123 Dire Dawa', '2005-01-15', 'What is your pet name?', 'Oscar'),
('umer_jemal', 'umer', 'Umer Jamal', 'umer@gmail.com', '0912121212', '456 Finfinnee', '2006-05-20', 'What city were you born in?', 'Arsi Asela'),
('lalise_abera', 'lalise', 'Lalise Abera', 'lalise@example.com', '0913131313', '789 Dire Dawa', '2007-11-30', 'What is your favorite color?', 'Blue');

-- Insert sample accounts
INSERT IGNORE INTO Account (account_no, customer_id, account_type, balance) VALUES
('ACC1001', 1, 'SAVINGS', 5000.00),
('ACC1002', 2, 'CURRENT', 2500.00),
('ACC1003', 3, 'FIXED_DEPOSIT', 10000.00),
('ACC1004', 1, 'CURRENT', 2000.00); -- Obsa has 2 accounts

-- Insert sample BankTransactions
INSERT IGNORE INTO BankTransaction (account_no, transaction_type, amount, description) VALUES
('ACC1001', 'DEPOSIT', 5000.00, 'Initial deposit'),
('ACC1002', 'DEPOSIT', 2500.00, 'Account opening'),
('ACC1003', 'DEPOSIT', 10000.00, 'Fixed deposit'),
('ACC1001', 'WITHDRAW', 500.00, 'ATM withdrawal'),
('ACC1002', 'TRANSFER', 300.00, 'Transfer to ACC1001');