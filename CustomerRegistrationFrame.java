
package ui;

import javax.swing.*;
import java.awt.*;
import dao.CustomerDAO;
import model.Customer;
import java.time.LocalDate;

public class CustomerRegistrationFrame extends JFrame {
    private CustomerDAO customerDAO;
    
    public CustomerRegistrationFrame() {
        customerDAO = new CustomerDAO();
        initComponents();
        setTitle("New Customer Registration");
        setSize(600, 700);
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
        JLabel headerLabel = new JLabel("NEW CUSTOMER REGISTRATION", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Registration form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Personal Information Section
        JLabel personalInfoLabel = new JLabel("Personal Information");
        personalInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        personalInfoLabel.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(personalInfoLabel, gbc);
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Full Name:*"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        JTextField nameField = new JTextField(20);
        formPanel.add(nameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email Address:*"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        JTextField emailField = new JTextField(20);
        formPanel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Phone Number:*"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        JTextField phoneField = new JTextField(20);
        formPanel.add(phoneField, gbc);
        
        // Date of Birth
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Date of Birth:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 4;
        JTextField dobField = new JTextField(20);
        dobField.setText("YYYY-MM-DD");
        formPanel.add(dobField, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Address:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 5;
        JTextArea addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        formPanel.add(addressScroll, gbc);
        
        // Login Credentials Section
        JLabel loginLabel = new JLabel("Login Credentials");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 16));
        loginLabel.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        formPanel.add(loginLabel, gbc);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Username:*"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 7;
        JTextField usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(new JLabel("Password:*"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 8;
        JPasswordField passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);
        
        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 9;
        formPanel.add(new JLabel("Confirm Password:*"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 9;
        JPasswordField confirmPasswordField = new JPasswordField(20);
        formPanel.add(confirmPasswordField, gbc);
        
        // Security Section
        JLabel securityLabel = new JLabel("Security Questions");
        securityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        securityLabel.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 2;
        formPanel.add(securityLabel, gbc);
        
        // Security Question
        gbc.gridx = 0; gbc.gridy = 11; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Security Question:*"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 11;
        JComboBox<String> securityQuestionCombo = new JComboBox<>(new String[]{
            "What is your pet name?",
            "What city were you born in?",
            "What is your favorite color?",
            "What is your mother's maiden name?",
            "What was your first school?"
        });
        formPanel.add(securityQuestionCombo, gbc);
        
        // Security Answer
        gbc.gridx = 0; gbc.gridy = 12;
        formPanel.add(new JLabel("Security Answer:*"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 12;
        JTextField securityAnswerField = new JTextField(20);
        formPanel.add(securityAnswerField, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 13; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(39, 174, 96));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setPreferredSize(new Dimension(120, 35));
        registerButton.addActionListener(e -> {
            registerCustomer(
                nameField, emailField, phoneField, dobField, addressArea,
                usernameField, passwordField, confirmPasswordField,
                securityQuestionCombo, securityAnswerField
            );
        });
        
        JButton clearButton = new JButton("Clear Form");
        clearButton.setBackground(new Color(52, 152, 219));
        clearButton.setForeground(Color.WHITE);
        clearButton.addActionListener(e -> {
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
            dobField.setText("YYYY-MM-DD");
            addressArea.setText("");
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            securityAnswerField.setText("");
        });
        
        JButton backButton = new JButton("← Back to Login");
        backButton.setBackground(new Color(149, 165, 166));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            dispose();
            new WelcomeFrame().setVisible(true);
        });
        
        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);
        
        formPanel.add(buttonPanel, gbc);
        
        // Footer
        JLabel footerLabel = new JLabel("Fields marked with * are required", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.GRAY);
        
        // Add panels
        mainPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void registerCustomer(
        JTextField nameField, JTextField emailField, JTextField phoneField,
        JTextField dobField, JTextArea addressArea, JTextField usernameField,
        JPasswordField passwordField, JPasswordField confirmPasswordField,
        JComboBox<String> securityQuestionCombo, JTextField securityAnswerField) {
        
        // Collect data
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String dobStr = dobField.getText().trim();
        String address = addressArea.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String securityQuestion = (String) securityQuestionCombo.getSelectedItem();
        String securityAnswer = securityAnswerField.getText().trim();
        
        // Validation
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || 
            username.isEmpty() || password.isEmpty() || securityAnswer.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill all required fields",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this,
                "Password must be at least 4 characters",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check if username is taken
        if (customerDAO.isUsernameTaken(username)) {
            JOptionPane.showMessageDialog(this,
                "Username already taken. Please choose another.",
                "Username Unavailable",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Parse date of birth
        LocalDate dateOfBirth;
        try {
            dateOfBirth = dobStr.equals("YYYY-MM-DD") ? LocalDate.now() : LocalDate.parse(dobStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Invalid date format. Please use YYYY-MM-DD format.",
                "Invalid Date",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create customer object
        Customer customer = new Customer(username, password, name, email, phone);
        customer.setAddress(address.isEmpty() ? "Not provided" : address);
        customer.setDateOfBirth(dateOfBirth);
        customer.setSecurityQuestion(securityQuestion);
        customer.setSecurityAnswer(securityAnswer);
        
        // Save to database
        if (customerDAO.addCustomer(customer)) {
            JOptionPane.showMessageDialog(this,
                "Registration successful!\n\n" +
                "Your account has been created.\n" +
                "Username: " + username + "\n" +
                "Please login to access your account.",
                "Registration Successful",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Redirect to login
            dispose();
            new CustomerLoginFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Registration failed. Email or username might already exist.",
                "Registration Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}