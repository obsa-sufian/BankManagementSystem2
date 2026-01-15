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
import java.math.BigDecimal;

public class TransferFrame extends JFrame {
    private BankingService bankingService;
    private JTextField fromAccountField;
    private JTextField toAccountField;
    private JTextField amountField;
    private JTextArea descriptionArea;
    
    public TransferFrame() {
        this.bankingService = new BankingService();
        initComponents();
        setTitle("Transfer Funds");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("TRANSFER FUNDS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(155, 89, 182));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // From Account
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("From Account:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        fromAccountField = new JTextField(20);
        formPanel.add(fromAccountField, gbc);
        
        // To Account
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("To Account:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        toAccountField = new JTextField(20);
        formPanel.add(toAccountField, gbc);
        
        // Amount
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Amount:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        amountField = new JTextField(20);
        formPanel.add(amountField, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton submitButton = new JButton("Transfer Funds");
        submitButton.setBackground(new Color(155, 89, 182));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setPreferredSize(new Dimension(150, 35));
        submitButton.addActionListener(e -> processTransfer());
        
        JButton clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(52, 152, 219));
        clearButton.setForeground(Color.WHITE);
        clearButton.addActionListener(e -> clearForm());
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(closeButton);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void processTransfer() {
        String fromAccount = fromAccountField.getText().trim();
        String toAccount = toAccountField.getText().trim();
        String amountStr = amountField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        // Validation
        if (fromAccount.isEmpty() || toAccount.isEmpty() || amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (fromAccount.equals(toAccount)) {
            JOptionPane.showMessageDialog(this, 
                "Cannot transfer to the same account", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            BigDecimal amount = new BigDecimal(amountStr);
            
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Amount must be greater than zero", 
                    "Invalid Amount", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Process transfer
            boolean success = bankingService.transfer(fromAccount, toAccount, amount, description);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Transfer successful!\n" +
                    "From: " + fromAccount + "\n" +
                    "To: " + toAccount + "\n" +
                    "Amount: " + amount,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Transfer failed. Please check:\n" +
                    "1. Account numbers are valid\n" +
                    "2. Sufficient balance in source account\n" +
                    "3. Both accounts are active",
                    "Transfer Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid amount", 
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        fromAccountField.setText("");
        toAccountField.setText("");
        amountField.setText("");
        descriptionArea.setText("");
        fromAccountField.requestFocus();
    }
}