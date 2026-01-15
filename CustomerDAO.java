/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author oscar
 *//**
import model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    
    public boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO Customer (name, email, phone, address, date_of_birth) VALUES (?, ?, ?, ?, ?)";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getAddress());
            pstmt.setDate(5, Date.valueOf(customer.getDateOfBirth()));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
            return false;
        }
    }
    
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM Customer WHERE customer_id = ?";
        Customer customer = null;
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));
                customer.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                customer.setCreatedAt(rs.getString("created_at"));
                return customer;
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer: " + e.getMessage());
        }
        return null;
    }
    
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer ORDER BY customer_id DESC";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Customer customer = new Customer();
                
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));
                customer.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                customer.setCreatedAt(rs.getString("created_at"));
                
                customers.add(customer);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all customers: " + e.getMessage());
        }
        return customers;
    }
    
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE Customer SET name = ?, email = ?, phone = ?, address = ?, date_of_birth = ? WHERE customer_id = ?";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getAddress());
            pstmt.setDate(5, Date.valueOf(customer.getDateOfBirth()));
            pstmt.setInt(6, customer.getCustomerId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM Customer WHERE customer_id = ?";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }
    
    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM Customer WHERE email = ?";
        Customer customer = null;
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));
                customer.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                customer.setCreatedAt(rs.getString("created_at"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer by email: " + e.getMessage());
        }
        return customer;
    }
}
*/
import java.sql.*;
import java.util.*;
import model.Customer;

public class CustomerDAO {
    
    public boolean addCustomer(Customer customer) {
        String sql = "INSERT INTO Customer (username, password, name, email, phone, address, date_of_birth, security_question, security_answer) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, customer.getUsername());
            pstmt.setString(2, customer.getPassword());
            pstmt.setString(3, customer.getName());
            pstmt.setString(4, customer.getEmail());
            pstmt.setString(5, customer.getPhone());
            pstmt.setString(6, customer.getAddress());
            pstmt.setDate(7, java.sql.Date.valueOf(customer.getDateOfBirth()));
            pstmt.setString(8, customer.getSecurityQuestion());
            pstmt.setString(9, customer.getSecurityAnswer());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public Customer authenticateCustomer(String username, String password) {
        String sql = "SELECT * FROM Customer WHERE username = ? AND password = ? AND account_locked = FALSE";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setUsername(rs.getString("username"));
                customer.setPassword(rs.getString("password"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));
                java.sql.Date dob = rs.getDate("date_of_birth");
                if (dob != null) {
                    customer.setDateOfBirth(dob.toLocalDate());
                }
                customer.setSecurityQuestion(rs.getString("security_question"));
                customer.setSecurityAnswer(rs.getString("security_answer"));
                customer.setAccountLocked(rs.getBoolean("account_locked"));
                customer.setFailedLoginAttempts(rs.getInt("failed_login_attempts"));
                customer.setLastLogin(rs.getString("last_login"));
                customer.setCreatedAt(rs.getString("created_at"));
                
                // Update last login
                updateLastLogin(customer.getCustomerId());
                
                return customer;
            }
            
        } catch (SQLException e) {
            System.err.println("Error authenticating customer: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    private void updateLastLogin(int customerId) {
        String sql = "UPDATE Customer SET last_login = CURRENT_TIMESTAMP WHERE customer_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating last login: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void incrementFailedLoginAttempts(String username) {
        String sql = "UPDATE Customer SET failed_login_attempts = failed_login_attempts + 1 WHERE username = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            
            // Check if account should be locked (3 failed attempts)
            checkAndLockAccount(username);
        } catch (SQLException e) {
            System.err.println("Error incrementing failed login attempts: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void checkAndLockAccount(String username) {
        String sql = "UPDATE Customer SET account_locked = TRUE WHERE username = ? AND failed_login_attempts >= 3";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Account locked for user: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error checking account lock: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void resetFailedLoginAttempts(int customerId) {
        String sql = "UPDATE Customer SET failed_login_attempts = 0, account_locked = FALSE WHERE customer_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error resetting failed login attempts: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean resetPassword(String username, String securityQuestion, String securityAnswer, String newPassword) {
        String sql = "UPDATE Customer SET password = ?, failed_login_attempts = 0, account_locked = FALSE WHERE username = ? AND security_question = ? AND security_answer = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            pstmt.setString(3, securityQuestion);
            pstmt.setString(4, securityAnswer);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error resetting password: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean updateCustomerPassword(int customerId, String newPassword) {
        String sql = "UPDATE Customer SET password = ? WHERE customer_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, customerId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public Customer getCustomerByUsername(String username) {
        String sql = "SELECT * FROM Customer WHERE username = ?";
        Customer customer = null;
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setUsername(rs.getString("username"));
                customer.setPassword(rs.getString("password"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));
                java.sql.Date dob = rs.getDate("date_of_birth");
                if (dob != null) {
                    customer.setDateOfBirth(dob.toLocalDate());
                }
                customer.setSecurityQuestion(rs.getString("security_question"));
                customer.setSecurityAnswer(rs.getString("security_answer"));
                customer.setAccountLocked(rs.getBoolean("account_locked"));
                customer.setFailedLoginAttempts(rs.getInt("failed_login_attempts"));
                customer.setLastLogin(rs.getString("last_login"));
                customer.setCreatedAt(rs.getString("created_at"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer by username: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return customer;
    }
    
    public boolean isUsernameTaken(String username) {
        String sql = "SELECT 1 FROM Customer WHERE username = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.err.println("Error checking username: " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Keep existing methods like getAllCustomers(), getCustomerById(), etc.
    // Add customerId parameter to existing methods
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM Customer WHERE customer_id = ?";
        Customer customer = null;
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));
                customer.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                customer.setCreatedAt(rs.getString("created_at"));
                return customer;
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer: " + e.getMessage());
        }
        return null;
    }
    
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer ORDER BY customer_id DESC";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Customer customer = new Customer();
                
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));
                customer.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                customer.setCreatedAt(rs.getString("created_at"));
                
                customers.add(customer);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all customers: " + e.getMessage());
        }
        return customers;
    }
    
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE Customer SET name = ?, email = ?, phone = ?, address = ?, date_of_birth = ? WHERE customer_id = ?";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getAddress());
            pstmt.setDate(5, java.sql.Date.valueOf(customer.getDateOfBirth()));
            pstmt.setInt(6, customer.getCustomerId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM Customer WHERE customer_id = ?";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }
    
    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM Customer WHERE email = ?";
        Customer customer = null;
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setAddress(rs.getString("address"));
                customer.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                customer.setCreatedAt(rs.getString("created_at"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting customer by email: " + e.getMessage());
        }
        return customer;
    }
    
}