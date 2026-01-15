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
import dao.CustomerDAO;
import model.Customer;
import java.time.LocalDate;
import java.util.List;

public class CustomerFrame extends JFrame {
    private final CustomerDAO customerDAO;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    
    public CustomerFrame() {
        customerDAO = new CustomerDAO();
        initComponents();
        setTitle("Customer Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        loadCustomers();
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
        
        // Input fields
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Name:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField nameField = new JTextField(15);
        topPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        topPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        JTextField emailField = new JTextField(15);
        topPanel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        topPanel.add(new JLabel("Phone:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        JTextField phoneField = new JTextField(15);
        topPanel.add(phoneField, gbc);
        
        // Add button
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.gridheight = 3;
        JButton addButton = new JButton("Add Customer");
        addButton.setBackground(new Color(39, 174, 96));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> {
            addCustomer(nameField, emailField, phoneField);
        });
        topPanel.add(addButton, gbc);
        
        // Table for displaying customers
        String[] columns = {"ID", "Name", "Email", "Phone", "Address", "Date of Birth", "Created"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        customerTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        
        // Bottom panel for actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadCustomers());
        
        JButton deleteButton = new JButton("Delete Customer");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteCustomer());
        
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
        tableModel.setRowCount(0); // Clear table
        
        List<Customer> customers = customerDAO.getAllCustomers();
        for (Customer customer : customers) {
            Object[] row = {
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getDateOfBirth(),
                customer.getCreatedAt()
            };
            tableModel.addRow(row);
        }
    }
    
    private void addCustomer(JTextField nameField, JTextField emailField, JTextField phoneField) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all fields", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress("Not provided");
        customer.setDateOfBirth(LocalDate.now());
        
        if (customerDAO.addCustomer(customer)) {
            JOptionPane.showMessageDialog(this, 
                "Customer added successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            loadCustomers();
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to add customer. Email might already exist.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a customer to delete", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int customerId = (int) tableModel.getValueAt(selectedRow, 0);
        String customerName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete customer: " + customerName + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (customerDAO.deleteCustomer(customerId)) {
                JOptionPane.showMessageDialog(this, 
                    "Customer deleted successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadCustomers();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to delete customer", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
