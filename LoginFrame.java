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
import java.awt.*;
import java.awt.event.*;
import service.BankingService;
import util.FileLogger;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private BankingService bankingService;
    private int loginAttempts = 0;
    
    public LoginFrame() {
        bankingService = new BankingService();
        initComponents();
        setTitle("Bank Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initComponents() {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));
        
        // Header
        JLabel headerLabel = new JLabel("BANK MANAGEMENT SYSTEM", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Center panel for login form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);
        
        // Login button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 153, 76));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(120, 35));
        loginButton.addActionListener(new LoginAction());
        
        // Enter key listener for login
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });
        
        formPanel.add(loginButton, gbc);
        
        // Footer
        JLabel footerLabel = new JLabel("", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.GRAY);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                    "Please enter both username and password",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Authenticate
            if (bankingService.authenticateAdmin(username, password)) {
                FileLogger.logLogin(username, true);
                JOptionPane.showMessageDialog(LoginFrame.this,
                    "Login successful! Welcome " + username,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Open main menu
                SwingUtilities.invokeLater(() -> {
                    MainMenuFrame mainMenu = new MainMenuFrame(username);
                    mainMenu.setVisible(true);
                });
                
                dispose(); // Close login window
            } else {
                loginAttempts++;
                FileLogger.logLogin(username, false);
                
                if (loginAttempts >= 3) {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                        "Too many failed attempts. System will exit.",
                        "Security Alert",
                        JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                        "Invalid credentials. Attempts left: " + (3 - loginAttempts),
                        "Authentication Failed",
                        JOptionPane.ERROR_MESSAGE);
                    
                    // Clear password field
                    passwordField.setText("");
                }
            }
        }
    }
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
