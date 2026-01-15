/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author oscar
 */

import model.Account;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AccountDAO {
    
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO Account (account_no, customer_id, account_type, balance, interest_rate) VALUES (?, ?, ?, ?, ?)";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Generate account number if not provided
            if (account.getAccountNo() == null || account.getAccountNo().isEmpty()) {
                account.setAccountNo(generateAccountNumber());
            }
            
            pstmt.setString(1, account.getAccountNo());
            pstmt.setInt(2, account.getCustomerId());
            pstmt.setString(3, account.getAccountType());
            pstmt.setBigDecimal(4, account.getBalance());
            pstmt.setBigDecimal(5, account.getInterestRate());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
            return false;
        }
    }
    
    private String generateAccountNumber() {
        Random rand = new Random();
        //long accountNumber = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        long accountNumber = 100L + (long)(rand.nextDouble() * 900L);
        return "ACC" + accountNumber;
    }
    
    public Account getAccountByNumber(String accountNo) {
        String sql = "SELECT * FROM Account WHERE account_no = ?";
        Account account = null;
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                account = new Account();
                account.setAccountNo(rs.getString("account_no"));
                account.setCustomerId(rs.getInt("customer_id"));
                account.setAccountType(rs.getString("account_type"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setInterestRate(rs.getBigDecimal("interest_rate"));
                account.setCreatedAt(rs.getString("created_at"));
                account.setStatus(rs.getString("status"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting account: " + e.getMessage());
        }
        return account;
    }
    
    public List<Account> getAccountsByCustomerId(int customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM Account WHERE customer_id = ? ORDER BY created_at DESC";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Account account = new Account();
                account.setAccountNo(rs.getString("account_no"));
                account.setCustomerId(rs.getInt("customer_id"));
                account.setAccountType(rs.getString("account_type"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setInterestRate(rs.getBigDecimal("interest_rate"));
                account.setCreatedAt(rs.getString("created_at"));
                account.setStatus(rs.getString("status"));
                
                accounts.add(account);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer accounts: " + e.getMessage());
        }
        return accounts;
    }
    
    public boolean updateAccountBalance(String accountNo, BigDecimal newBalance) {
        String sql = "UPDATE Account SET balance = ? WHERE account_no = ?";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBigDecimal(1, newBalance);
            pstmt.setString(2, accountNo);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating account balance: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateAccountStatus(String accountNo, String status) {
        String sql = "UPDATE Account SET status = ? WHERE account_no = ?";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, accountNo);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating account status: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteAccount(String accountNo) {
        String sql = "DELETE FROM Account WHERE account_no = ?";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNo);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting account: " + e.getMessage());
            return false;
        }
    }
    
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT a.*, c.name as customer_name FROM Account a JOIN Customer c ON a.customer_id = c.customer_id ORDER BY a.created_at DESC";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Account account = new Account();
                account.setAccountNo(rs.getString("account_no"));
                account.setCustomerId(rs.getInt("customer_id"));
                account.setAccountType(rs.getString("account_type"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setInterestRate(rs.getBigDecimal("interest_rate"));
                account.setCreatedAt(rs.getString("created_at"));
                account.setStatus(rs.getString("status"));
                
                accounts.add(account);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all accounts: " + e.getMessage());
        }
        return accounts;
    }
    
    public boolean accountExists(String accountNo) {
        String sql = "SELECT 1 FROM Account WHERE account_no = ?";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNo);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.err.println("Error checking account existence: " + e.getMessage());
            return false;
        }
    }
}
