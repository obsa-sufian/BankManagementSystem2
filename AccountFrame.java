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
import dao.*;
import model.*;
import java.util.List;

public class AccountFrame extends JFrame {
    private AccountDAO accountDAO;
    private CustomerDAO customerDAO;
    private JTable accountTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> customerCombo;
    
    public AccountFrame() {
        accountDAO = new AccountDAO();
        customerDAO = new CustomerDAO();
        initComponents();
        setTitle("Account Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        loadCustomers();
        loadAccounts();
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel for controls
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Customer selection
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Select Customer:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        customerCombo = new JComboBox<>();
        customerCombo.setPreferredSize(new Dimension(200, 25));
        topPanel.add(customerCombo, gbc);
        
        // Account type
        gbc.gridx = 0; gbc.gridy = 1;
        topPanel.add(new JLabel("Account Type:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"SAVINGS", "CURRENT", "FIXED_DEPOSIT"});
        topPanel.add(typeCombo, gbc);
        
        // Create account button
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.gridheight = 2;
        JButton createButton = new JButton("Create Account");
        createButton.setBackground(new Color(39, 174, 96));
        createButton.setForeground(Color.WHITE);
        createButton.addActionListener(e -> createAccount(typeCombo));
        topPanel.add(createButton, gbc);
        
        // Table for displaying accounts
        String[] columns = {"Account No", "Customer ID", "Type", "Balance", "Interest Rate", "Status", "Created"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        accountTable = new JTable(tableModel);
        accountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        accountTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(accountTable);
        
        // Bottom panel for actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadAccounts());
        
        JButton deleteButton = new JButton("Delete Account");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteAccount());
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        bottomPanel.add(refreshButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(closeButton);
        
        // Add panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadCustomers() {
        customerCombo.removeAllItems();
        customerCombo.addItem("Select Customer");
        
        List<Customer> customers = customerDAO.getAllCustomers();
        for (Customer customer : customers) {
            customerCombo.addItem(customer.getCustomerId() + " - " + customer.getName());
        }
    }
    
    private void loadAccounts() {
        tableModel.setRowCount(0); // Clear table
        
        List<Account> accounts = accountDAO.getAllAccounts();
        for (Account account : accounts) {
            Object[] row = {
                account.getAccountNo(),
                account.getCustomerId(),
                account.getAccountType(),
                account.getBalance(),
                account.getInterestRate() + "%",
                account.getStatus(),
                account.getCreatedAt()
            };
            tableModel.addRow(row);
        }
    }
    
    private void createAccount(JComboBox<String> typeCombo) {
        String selectedCustomer = (String) customerCombo.getSelectedItem();
        if (selectedCustomer == null || selectedCustomer.equals("Select Customer")) {
            JOptionPane.showMessageDialog(this, "Please select a customer", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Extract customer ID
        int customerId = Integer.parseInt(selectedCustomer.split(" - ")[0]);
        String accountType = (String) typeCombo.getSelectedItem();
        
        Account account = new Account(null, customerId, accountType);
        
        if (accountDAO.createAccount(account)) {
            JOptionPane.showMessageDialog(this, 
                "Account created successfully!\nAccount Number: " + account.getAccountNo(),
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            loadAccounts();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create account", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteAccount() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an account to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String accountNo = (String) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete account " + accountNo + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (accountDAO.deleteAccount(accountNo)) {
                JOptionPane.showMessageDialog(this, "Account deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAccounts();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete account", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
