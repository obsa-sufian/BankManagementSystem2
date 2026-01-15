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
import dao.*;
import model.*;
import java.util.List;
import java.time.LocalDateTime;

public class ReportFrame extends JFrame {
    private TransactionDAO transactionDAO;
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> reportTypeCombo;
    
    public ReportFrame() {
        transactionDAO = new TransactionDAO();
        customerDAO = new CustomerDAO();
        accountDAO = new AccountDAO();
        initComponents();
        setTitle("Reports & Analytics");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        loadTransactionReport();
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        reportTypeCombo = new JComboBox<>(new String[]{
            "Transaction Report", "Customer Report", "Account Report", "Daily Summary"
        });
        reportTypeCombo.addActionListener(e -> loadSelectedReport());
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadSelectedReport());
        
        JButton printButton = new JButton("Print Report");
        printButton.setBackground(new Color(52, 152, 219));
        printButton.setForeground(Color.WHITE);
        
        JButton exportButton = new JButton("Export to PDF");
        exportButton.setBackground(new Color(39, 174, 96));
        exportButton.setForeground(Color.WHITE);
        
        controlPanel.add(new JLabel("Report Type:"));
        controlPanel.add(reportTypeCombo);
        controlPanel.add(refreshButton);
        controlPanel.add(printButton);
        controlPanel.add(exportButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        controlPanel.add(closeButton);        
        
        
        // Table for reports
        String[] columns = {"Date", "Account", "Customer", "Type", "Amount", "Description", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        reportTable = new JTable(tableModel);
        reportTable.setAutoCreateRowSorter(true);
        reportTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        reportTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(reportTable);
        
        // Bottom panel for summary
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
        summaryPanel.setBackground(new Color(240, 248, 255));
        
        JLabel totalLabel = createSummaryLabel("Total Transactions", "0");
        JLabel depositLabel = createSummaryLabel("Total Deposits", "0.00");
        JLabel withdrawalLabel = createSummaryLabel("Total Withdrawals", "0.00");
        JLabel balanceLabel = createSummaryLabel("Net Balance", "0.00");
        
        summaryPanel.add(totalLabel);
        summaryPanel.add(depositLabel);
        summaryPanel.add(withdrawalLabel);
        summaryPanel.add(balanceLabel);
        
        // Add all panels
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(summaryPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JLabel createSummaryLabel(String title, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        valueLabel.setForeground(new Color(0, 102, 204));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        
        return new JLabel() {
            public Component getComponent() {
                return panel;
            }
        };
    }
    
    private void loadSelectedReport() {
        String reportType = (String) reportTypeCombo.getSelectedItem();
        
        switch (reportType) {
            case "Transaction Report":
                loadTransactionReport();
                break;
            case "Customer Report":
                loadCustomerReport();
                break;
            case "Account Report":
                loadAccountReport();
                break;
            case "Daily Summary":
                loadDailySummary();
                break;
        }
    }
    
    private void loadTransactionReport() {
        tableModel.setRowCount(0);
        
        List<Transaction> transactions = transactionDAO.getAllTransactions();
        for (Transaction transaction : transactions) {
            Object[] row = {
                transaction.getTransactionDate(),
                transaction.getAccountNo(),
                "N/A", // Would need to join with customer table
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getDescription(),
                "Completed"
            };
            tableModel.addRow(row);
        }
        
        updateSummary(transactions);
    }
    
    private void loadCustomerReport() {
        tableModel.setRowCount(0);
        
        List<Customer> customers = customerDAO.getAllCustomers();
        for (Customer customer : customers) {
            Object[] row = {
                customer.getCreatedAt(),
                String.valueOf(customer.getCustomerId()),
                customer.getName(),
                "CUSTOMER",
                "N/A",
                customer.getEmail() + " | " + customer.getPhone(),
                "Active"
            };
            tableModel.addRow(row);
        }
    }
    
    private void loadAccountReport() {
        tableModel.setRowCount(0);
        
        List<Account> accounts = accountDAO.getAllAccounts();
        for (Account account : accounts) {
            Object[] row = {
                account.getCreatedAt(),
                account.getAccountNo(),
                "Account",
                account.getAccountType(),
                account.getBalance(),
                "Interest: " + account.getInterestRate() + "%",
                account.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void loadDailySummary() {
        tableModel.setRowCount(0);
        
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startOfDay = today.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = today.withHour(23).withMinute(59).withSecond(59);
        
        List<Transaction> todayTransactions = transactionDAO.getTransactionsByDateRange(startOfDay, endOfDay);
        
        for (Transaction transaction : todayTransactions) {
            Object[] row = {
                transaction.getTransactionDate(),
                transaction.getAccountNo(),
                "Transaction",
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getDescription(),
                "Today"
            };
            tableModel.addRow(row);
        }
    }
    
    private void updateSummary(List<Transaction> transactions) {
        // Calculate summary statistics
        double totalDeposits = transactions.stream()
            .filter(t -> "DEPOSIT".equals(t.getTransactionType()))
            .mapToDouble(t -> t.getAmount().doubleValue())
            .sum();
        
        double totalWithdrawals = transactions.stream()
            .filter(t -> "WITHDRAW".equals(t.getTransactionType()))
            .mapToDouble(t -> t.getAmount().doubleValue())
            .sum();
        
        double netBalance = totalDeposits - totalWithdrawals;
        
        // Update labels
        // This would update the summary panel labels
    }
            
}

