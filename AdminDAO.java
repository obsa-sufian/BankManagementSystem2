/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author oscar
 */
import model.Admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    
    public boolean authenticate(String username, String password) {
        String sql = "SELECT * FROM Admin WHERE username = ? AND password = ?";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password); // In real application, use hashed passwords
            
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            System.err.println("Error authenticating admin: " + e.getMessage());
            return false;
        }
    }
    
    public boolean addAdmin(Admin admin) {
        String sql = "INSERT INTO Admin (username, password) VALUES (?, ?)";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, admin.getUsername());
            pstmt.setString(2, admin.getPassword());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding admin: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteAdmin(int adminId) {
        String sql = "DELETE FROM Admin WHERE admin_id = ?";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, adminId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting admin: " + e.getMessage());
            return false;
        }
    }
    
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM Admin ORDER BY admin_id";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
                admin.setCreatedAt(rs.getString("created_at"));
                
                admins.add(admin);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all admins: " + e.getMessage());
        }
        return admins;
    }
    
    public boolean changePassword(String username, String newPassword) {
        String sql = "UPDATE Admin SET password = ? WHERE username = ?";
        
            Connection conn = DatabaseConnection.getConnection();
            try ( PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error changing password: " + e.getMessage());
            return false;
        }
    }
}
