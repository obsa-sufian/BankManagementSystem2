package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WelcomeFrame extends JFrame {
    
    public WelcomeFrame() {
        initComponents();
        setTitle("Bank Management System");
        setSize(700, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        JLabel titleLabel = new JLabel("🏦 BANK MANAGEMENT SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Center panel with buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to Online Banking");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(0, 102, 204));
        centerPanel.add(welcomeLabel, gbc);
        
        JLabel descLabel = new JLabel("Please select your login type:");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        descLabel.setForeground(Color.DARK_GRAY);
        centerPanel.add(descLabel, gbc);
        
        // Buttons
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipady = 40;
        gbc.ipadx = 100;
        
        // Admin Login Button
        JButton adminButton = new JButton("ADMIN LOGIN");
        adminButton.setBackground(new Color(41, 128, 185));
        adminButton.setForeground(Color.WHITE);
        adminButton.setFont(new Font("Arial", Font.BOLD, 16));
        adminButton.setFocusPainted(false);
        adminButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminButton.addActionListener(e -> {
            dispose();
            new AdminLoginFrame().setVisible(true);
        });
        centerPanel.add(adminButton, gbc);
        
        // Customer Login Button
        JButton customerButton = new JButton("CUSTOMER LOGIN");
        customerButton.setBackground(new Color(39, 174, 96));
        customerButton.setForeground(Color.WHITE);
        customerButton.setFont(new Font("Arial", Font.BOLD, 16));
        customerButton.setFocusPainted(false);
        customerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customerButton.addActionListener(e -> {
            dispose();
            new CustomerLoginFrame().setVisible(true);
        });
        centerPanel.add(customerButton, gbc);
        
        // Register Button
        JButton registerButton = new JButton("NEW CUSTOMER REGISTRATION");
        registerButton.setBackground(new Color(150, 89, 182));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> {
            dispose();
            new CustomerRegistrationFrame().setVisible(true);
        });
        centerPanel.add(registerButton, gbc);
        
        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 248, 255));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel footerLabel = new JLabel("© 2026 Bank Management System. All rights reserved.");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(Color.GRAY);
        footerPanel.add(footerLabel);
        
        // Add panels
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }
        
        SwingUtilities.invokeLater(() -> {
            WelcomeFrame welcomeFrame = new WelcomeFrame();
            welcomeFrame.setVisible(true);
        });
    }
}