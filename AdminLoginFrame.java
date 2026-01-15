package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dao.AdminDAO;
import util.FileLogger;

public class AdminLoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AdminDAO adminDAO;
    private int loginAttempts = 0;
    
    public AdminLoginFrame() {
        adminDAO = new AdminDAO();
        initComponents();
        setTitle("Admin Login - Bank Management System");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));
        
        // Header
        JLabel headerLabel = new JLabel("ADMIN LOGIN", SwingConstants.CENTER);
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
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(41, 128, 185));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setPreferredSize(new Dimension(120, 35));
        loginButton.addActionListener(new LoginAction());
        
        // Enter key listener
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });
        
        formPanel.add(loginButton, gbc);
        
        // Back button
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton backButton = new JButton("← Back to Main Menu");
        backButton.setBackground(new Color(149, 165, 166));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            dispose();
            new WelcomeFrame().setVisible(true);
        });
        formPanel.add(backButton, gbc);
        
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
                JOptionPane.showMessageDialog(AdminLoginFrame.this,
                    "Please enter both username and password",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Authenticate
            if (adminDAO.authenticate(username, password)) {
                FileLogger.log("Admin login successful: " + username);
                JOptionPane.showMessageDialog(AdminLoginFrame.this,
                    "Login successful! Welcome " + username,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Open admin main menu
                SwingUtilities.invokeLater(() -> {
                    MainMenuFrame mainMenu = new MainMenuFrame(username);
                    mainMenu.setVisible(true);
                });
                
                dispose(); // Close login window
            } else {
                loginAttempts++;
                FileLogger.log("Admin login failed: " + username);
                
                if (loginAttempts >= 3) {
                    JOptionPane.showMessageDialog(AdminLoginFrame.this,
                        "Too many failed attempts. Returning to main menu.",
                        "Security Alert",
                        JOptionPane.ERROR_MESSAGE);
                    dispose();
                    new WelcomeFrame().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(AdminLoginFrame.this,
                        "Invalid credentials. Attempts left: " + (3 - loginAttempts),
                        "Authentication Failed",
                        JOptionPane.ERROR_MESSAGE);
                    
                    // Clear password field
                    passwordField.setText("");
                }
            }
        }
    }
}