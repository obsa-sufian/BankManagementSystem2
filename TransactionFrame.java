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

public class TransactionFrame extends JFrame {
    private BankingService bankingService;
    private String transactionType;
    private JTextField accountField;
    private JTextField amountField;
    private JTextArea descriptionArea;
    
    public TransactionFrame(String type) {
        this.transactionType = type;
        this.bankingService = new BankingService();
        initComponents();
        setTitle(type + " Transaction");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel(transactionType + " MONEY", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(transactionType.equals("DEPOSIT") ? 
            new Color(39, 174, 96) : new Color(231, 76, 60));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Account Number
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Account Number:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        accountField = new JTextField(20);
        formPanel.add(accountField, gbc);
        
        // Amount
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Amount:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        amountField = new JTextField(20);
        formPanel.add(amountField, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton submitButton = new JButton("Submit " + transactionType);
        submitButton.setBackground(transactionType.equals("DEPOSIT") ? 
            new Color(39, 174, 96) : new Color(231, 76, 60));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setPreferredSize(new Dimension(150, 35));
        submitButton.addActionListener(e -> processTransaction());
        
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
    
    private void processTransaction() {
        String accountNo = accountField.getText().trim();
        String amountStr = amountField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        // Validation
        if (accountNo.isEmpty() || amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields", 
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
            
            boolean success = false;
            
            if (transactionType.equals("DEPOSIT")) {
                success = bankingService.deposit(accountNo, amount, description);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Deposit successful!\nAmount: " + amount + "\nTo Account: " + accountNo,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (transactionType.equals("WITHDRAW")) {
                success = bankingService.withdraw(accountNo, amount, description);
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Withdrawal successful!\nAmount: " + amount + "\nFrom Account: " + accountNo,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
            
            if (success) {
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this,
                    transactionType + " failed. Please check account details and balance.",
                    "Transaction Failed",
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
        accountField.setText("");
        amountField.setText("");
        descriptionArea.setText("");
        accountField.requestFocus();
    }
}

