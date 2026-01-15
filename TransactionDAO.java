/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author oscar
 */
import model.Transaction;
import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    
    public boolean recordTransaction(Transaction transaction) {
        String sql = "INSERT INTO BankTransaction (account_no, transaction_type, amount, recipient_account, description) VALUES (?, ?, ?, ?, ?)";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, transaction.getAccountNo());
            pstmt.setString(2, transaction.getTransactionType());
            pstmt.setBigDecimal(3, transaction.getAmount());
            pstmt.setString(4, transaction.getRecipientAccount());
            pstmt.setString(5, transaction.getDescription());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error recording transaction: " + e.getMessage());
            return false;
        }
    }
    
    public List<Transaction> getTransactionsByAccount(String accountNo) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM BankTransaction WHERE account_no = ? ORDER BY transaction_date DESC";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNo);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setAccountNo(rs.getString("account_no"));
                transaction.setTransactionType(rs.getString("transaction_type"));
                transaction.setAmount(rs.getBigDecimal("amount"));
                transaction.setRecipientAccount(rs.getString("recipient_account"));
                transaction.setDescription(rs.getString("description"));
                transaction.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
                
                transactions.add(transaction);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting transactions: " + e.getMessage());
        }
        return transactions;
    }
    
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, a.account_type, c.name as customer_name " +
                     "FROM BankTransaction t " +
                     "JOIN Account a ON t.account_no = a.account_no " +
                     "JOIN Customer c ON a.customer_id = c.customer_id " +
                     "ORDER BY t.transaction_date DESC";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setAccountNo(rs.getString("account_no"));
                transaction.setTransactionType(rs.getString("transaction_type"));
                transaction.setAmount(rs.getBigDecimal("amount"));
                transaction.setRecipientAccount(rs.getString("recipient_account"));
                transaction.setDescription(rs.getString("description"));
                transaction.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
                
                transactions.add(transaction);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all transactions: " + e.getMessage());
        }
        return transactions;
    }
    
    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM BankTransaction WHERE transaction_date BETWEEN ? AND ? ORDER BY transaction_date DESC";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(startDate));
            pstmt.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setAccountNo(rs.getString("account_no"));
                transaction.setTransactionType(rs.getString("transaction_type"));
                transaction.setAmount(rs.getBigDecimal("amount"));
                transaction.setRecipientAccount(rs.getString("recipient_account"));
                transaction.setDescription(rs.getString("description"));
                transaction.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
                
                transactions.add(transaction);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting transactions by date range: " + e.getMessage());
        }
        return transactions;
    }
    
    public BigDecimal getTotalDeposits(String accountNo) {
        String sql = "SELECT SUM(amount) as total FROM BankTransaction WHERE account_no = ? AND transaction_type = 'DEPOSIT'";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total deposits: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
    
    public BigDecimal getTotalWithdrawals(String accountNo) {
        String sql = "SELECT SUM(amount) as total FROM BankTransaction WHERE account_no = ? AND transaction_type = 'WITHDRAW'";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting total withdrawals: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
}
