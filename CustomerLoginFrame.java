package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dao.CustomerDAO;
import model.Customer;
import util.FileLogger;

public class CustomerLoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private CustomerDAO customerDAO;
    private int loginAttempts = 0;
    
    public CustomerLoginFrame() {
        customerDAO = new CustomerDAO();
        initComponents();
        setTitle("Customer Login - Bank Management System");
        setSize(450, 400);
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
        JLabel headerLabel = new JLabel("CUSTOMER LOGIN", SwingConstants.CENTER);
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
        
        // Forgot password link
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel forgotPasswordLabel = new JLabel("<html><u>Forgot Password?</u></html>");
        forgotPasswordLabel.setForeground(new Color(41, 128, 185));
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showForgotPasswordDialog();
            }
        });
        formPanel.add(forgotPasswordLabel, gbc);
        
        // Login button
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(39, 174, 96));
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
        gbc.gridx = 0; gbc.gridy = 4;
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
        JLabel footerLabel = new JLabel("New customer? Click 'Register' on main menu", SwingConstants.CENTER);
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
                JOptionPane.showMessageDialog(CustomerLoginFrame.this,
                    "Please enter both username and password",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Authenticate customer
            Customer customer = customerDAO.authenticateCustomer(username, password);
            
            if (customer != null) {
                // Check if account is locked
                if (customer.isAccountLocked()) {
                    JOptionPane.showMessageDialog(CustomerLoginFrame.this,
                        "Your account is locked. Please contact customer support.",
                        "Account Locked",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Reset failed login attempts
                customerDAO.resetFailedLoginAttempts(customer.getCustomerId());
                
                FileLogger.log("Customer login successful: " + username);
                JOptionPane.showMessageDialog(CustomerLoginFrame.this,
                    "Login successful! Welcome " + customer.getName(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Open customer dashboard
                SwingUtilities.invokeLater(() -> {
                    CustomerDashboardFrame dashboard = new CustomerDashboardFrame(customer);
                    dashboard.setVisible(true);
                });
                
                dispose(); // Close login window
            } else {
                loginAttempts++;
                FileLogger.log("Customer login failed: " + username);
                
                // Increment failed login attempts
                customerDAO.incrementFailedLoginAttempts(username);
                
                if (loginAttempts >= 3) {
                    JOptionPane.showMessageDialog(CustomerLoginFrame.this,
                        "Too many failed attempts. Your account may be locked.",
                        "Security Alert",
                        JOptionPane.ERROR_MESSAGE);
                    dispose();
                    new WelcomeFrame().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(CustomerLoginFrame.this,
                        "Invalid credentials. Attempts left: " + (3 - loginAttempts),
                        "Authentication Failed",
                        JOptionPane.ERROR_MESSAGE);
                    
                    // Clear password field
                    passwordField.setText("");
                }
            }
        }
    }
    
    private void showForgotPasswordDialog() {
        JTextField usernameField = new JTextField();
        JComboBox<String> securityQuestions = new JComboBox<>(new String[]{
            "What is your pet name?",
            "What city were you born in?",
            "What is your favorite color?",
            "What is your mother's maiden name?",
            "What was your first school?"
        });
        JTextField answerField = new JTextField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        
        Object[] message = {
            "Username:", usernameField,
            "Security Question:", securityQuestions,
            "Security Answer:", answerField,
            "New Password:", newPasswordField,
            "Confirm Password:", confirmPasswordField
        };
        
        int option = JOptionPane.showConfirmDialog(this,
            message,
            "Forgot Password - Reset",
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String securityQuestion = (String) securityQuestions.getSelectedItem();
            String securityAnswer = answerField.getText().trim();
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            if (username.isEmpty() || securityAnswer.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please fill all fields",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                    "Passwords do not match",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = customerDAO.resetPassword(username, securityQuestion, securityAnswer, newPassword);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Password reset successful! You can now login with your new password.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Password reset failed. Please check your details and try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}