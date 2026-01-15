/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author oscar
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import dao.AdminDAO;
import model.Admin;
import java.util.List;

public class AdminFrame extends JFrame {
    private AdminDAO adminDAO;
    private JTable adminTable;
    private DefaultTableModel tableModel;
    
    public AdminFrame() {
        adminDAO = new AdminDAO();
        initComponents();
        setTitle("Admin Management");
        setSize(700, 500);
        setLocationRelativeTo(null);
        loadAdmins();
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel for add admin
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Input fields
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField usernameField = new JTextField(15);
        topPanel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        topPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        JPasswordField passwordField = new JPasswordField(15);
        topPanel.add(passwordField, gbc);
        
        // Add button
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.gridheight = 2;
        JButton addButton = new JButton("Add Admin");
        addButton.setBackground(new Color(39, 174, 96));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> {
            addAdmin(usernameField, passwordField);
        });
        topPanel.add(addButton, gbc);
        
        // Table for displaying admins
        String[] columns = {"ID", "Username", "Created"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        adminTable = new JTable(tableModel);
        adminTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        adminTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        adminTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(adminTable);
        
        // Bottom panel for actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadAdmins());
        
        JButton deleteButton = new JButton("Delete Admin");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteAdmin());
        
        JButton changePassButton = new JButton("Change Password");
        changePassButton.setBackground(new Color(241, 196, 15));
        changePassButton.addActionListener(e -> changePassword());
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        bottomPanel.add(refreshButton);
        bottomPanel.add(changePassButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(closeButton);
        
        // Add panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadAdmins() {
        tableModel.setRowCount(0); // Clear table
        
        List<Admin> admins = adminDAO.getAllAdmins();
        for (Admin admin : admins) {
            Object[] row = {
                admin.getAdminId(),
                admin.getUsername(),
                admin.getCreatedAt()
            };
            tableModel.addRow(row);
        }
    }
    
    private void addAdmin(JTextField usernameField, JPasswordField passwordField) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all fields", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (username.equals("admin")) {
            JOptionPane.showMessageDialog(this, 
                "'admin' username is reserved", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Admin admin = new Admin(username, password);
        
        if (adminDAO.addAdmin(admin)) {
            JOptionPane.showMessageDialog(this, 
                "Admin added successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            loadAdmins();
            usernameField.setText("");
            passwordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to add admin. Username might already exist.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteAdmin() {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select an admin to delete", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int adminId = (int) tableModel.getValueAt(selectedRow, 0);
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        
        if (username.equals("admin")) {
            JOptionPane.showMessageDialog(this, 
                "Cannot delete the default admin account", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete admin: " + username + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (adminDAO.deleteAdmin(adminId)) {
                JOptionPane.showMessageDialog(this, 
                    "Admin deleted successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadAdmins();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to delete admin", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void changePassword() {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select an admin to change password", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String username = (String) tableModel.getValueAt(selectedRow, 1);
        
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        
        Object[] message = {
            "New Password for " + username + ":", newPasswordField,
            "Confirm Password:", confirmPasswordField
        };
        
        int option = JOptionPane.showConfirmDialog(this,
            message,
            "Change Password",
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, 
                    "Passwords do not match", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (newPassword.length() < 4) {
                JOptionPane.showMessageDialog(this, 
                    "Password must be at least 4 characters", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (adminDAO.changePassword(username, newPassword)) {
                JOptionPane.showMessageDialog(this, 
                    "Password changed successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to change password", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
