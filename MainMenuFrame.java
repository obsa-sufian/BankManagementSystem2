/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuFrame extends JFrame {
    private String loggedInUser;
    
    public MainMenuFrame(String username) {
        this.loggedInUser = username;
        initComponents();
        setTitle("Bank Management System - Main Menu");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("BANK MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Welcome, " + loggedInUser);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userLabel, BorderLayout.EAST);
        
        // Menu Panel
        JPanel menuPanel = new JPanel(new GridLayout(4, 2, 20, 20));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        menuPanel.setBackground(new Color(240, 248, 255));
        
        // Create menu buttons
        String[] menuItems = {
            "Customer Management", "Account Management",
            "Deposit Money", "Withdraw Money",
            "Transfer Funds", "Transaction History",
            "Reports", "System Settings"
        };
        
        Color[] buttonColors = {
            new Color(41, 128, 185), new Color(39, 174, 96),
            new Color(241, 196, 15), new Color(230, 126, 34),
            new Color(155, 89, 182), new Color(52, 152, 219),
            new Color(46, 204, 113), new Color(149, 165, 166)
        };
        
        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuItems[i], buttonColors[i]);
            final int index = i;
            menuButton.addActionListener(e -> handleMenuClick(index));
            menuPanel.add(menuButton);
        }
        
        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(240, 248, 255));
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
        
        bottomPanel.add(logoutButton);
        
        // Add all panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JButton createMenuButton(String text, Color color) {
        JButton button = new JButton("<html><center><b>" + text + "</b></center></html>");
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void handleMenuClick(int menuIndex) {
        switch (menuIndex) {
            case 0: // Customer Management
                SwingUtilities.invokeLater(() -> {
                    CustomerFrame customerFrame = new CustomerFrame();
                    customerFrame.setVisible(true);
                });
                break;
            case 1: // Account Management
                SwingUtilities.invokeLater(() -> {
                    AccountFrame accountFrame = new AccountFrame();
                    accountFrame.setVisible(true);
                });
                break;
            case 2: // Deposit Money
                SwingUtilities.invokeLater(() -> {
                    TransactionFrame depositFrame = new TransactionFrame("DEPOSIT");
                    depositFrame.setVisible(true);
                });
                break;
            case 3: // Withdraw Money
                SwingUtilities.invokeLater(() -> {
                    TransactionFrame withdrawFrame = new TransactionFrame("WITHDRAW");
                    withdrawFrame.setVisible(true);
                });
                break;
            case 4: // Transfer Funds
                SwingUtilities.invokeLater(() -> {
                    TransferFrame transferFrame = new TransferFrame();
                    transferFrame.setVisible(true);
                });
                break;
            case 5: // Transaction History
                SwingUtilities.invokeLater(() -> {
                    ReportFrame reportFrame = new ReportFrame();
                    reportFrame.setVisible(true);
                });
                break;
            case 6: // Reports
                SwingUtilities.invokeLater(() -> {
                    ReportFrame reportFrame = new ReportFrame();
                    reportFrame.setVisible(true);
                });
                break;
            case 7: // System Settings
                SwingUtilities.invokeLater(() -> {
                    AdminFrame adminFrame = new AdminFrame();
                    adminFrame.setVisible(true);
                });
                break;
        }
    }
}

