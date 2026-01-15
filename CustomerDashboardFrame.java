package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import dao.*;
import model.*;
import service.BankingService;
import java.util.List;
import java.math.BigDecimal;

public class CustomerDashboardFrame extends JFrame {
    private Customer customer;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private BankingService bankingService;
    private JTable accountTable;
    private DefaultTableModel accountTableModel;
    private JLabel welcomeLabel;
    private JLabel totalBalanceLabel;
    
    public CustomerDashboardFrame(Customer customer) {
        this.customer = customer;
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.bankingService = new BankingService();
        initComponents();
        setTitle("Customer Dashboard - Bank Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        loadCustomerAccounts();
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        welcomeLabel = new JLabel("Welcome, " + customer.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        
        JLabel customerInfo = new JLabel("Customer ID: " + customer.getCustomerId() + " | Username: " + customer.getUsername());
        customerInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        customerInfo.setForeground(Color.WHITE);
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);
        infoPanel.add(welcomeLabel);
        infoPanel.add(customerInfo);
        
        totalBalanceLabel = new JLabel("Total Balance: $0.00");
        totalBalanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalBalanceLabel.setForeground(Color.WHITE);
        
        headerPanel.add(infoPanel, BorderLayout.WEST);
        headerPanel.add(totalBalanceLabel, BorderLayout.EAST);
        
        // Quick actions panel
        JPanel quickActionsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        quickActionsPanel.setBorder(BorderFactory.createTitledBorder("Quick Actions"));
        quickActionsPanel.setBackground(new Color(240, 248, 255));
        
        String[] actionNames = {"View Balance", "Deposit", "Withdraw", "Transfer"};
        Color[] actionColors = {
            new Color(52, 152, 219),
            new Color(46, 204, 113),
            new Color(230, 126, 34),
            new Color(155, 89, 182)
        };
        
        for (int i = 0; i < actionNames.length; i++) {
            JButton actionButton = createActionButton(actionNames[i], actionColors[i]);
            final int index = i;
            actionButton.addActionListener(e -> handleQuickAction(index));
            quickActionsPanel.add(actionButton);
        }
        
        // Accounts panel
        JPanel accountsPanel = new JPanel(new BorderLayout());
        accountsPanel.setBorder(BorderFactory.createTitledBorder("Your Accounts"));
        
        String[] accountColumns = {"Account No", "Type", "Balance", "Interest Rate", "Status", "Created"};
        accountTableModel = new DefaultTableModel(accountColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        accountTable = new JTable(accountTableModel);
        accountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        accountTable.setRowHeight(25);
        
        JScrollPane accountScrollPane = new JScrollPane(accountTable);
        
        // Transaction history panel
        JPanel transactionPanel = new JPanel(new BorderLayout());
        transactionPanel.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));
        
        String[] transactionColumns = {"Date", "Type", "Amount", "Description", "Recipient"};
        DefaultTableModel transactionModel = new DefaultTableModel(transactionColumns, 0);
        JTable transactionTable = new JTable(transactionModel);
        JScrollPane transactionScrollPane = new JScrollPane(transactionTable);
        
        // Load transaction history
        loadTransactionHistory(transactionModel);
        
        transactionPanel.add(transactionScrollPane, BorderLayout.CENTER);
        
        // Bottom panel with logout button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshDashboard(transactionModel));
        
        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.setBackground(new Color(241, 196, 15));
        changePasswordButton.addActionListener(e -> changePassword());
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> {
            dispose();
            new WelcomeFrame().setVisible(true);
        });
        
        bottomPanel.add(refreshButton);
        bottomPanel.add(changePasswordButton);
        bottomPanel.add(logoutButton);
        
        // Add all panels
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(quickActionsPanel, BorderLayout.NORTH);
        centerPanel.add(accountsPanel, BorderLayout.CENTER);
        centerPanel.add(transactionPanel, BorderLayout.SOUTH);
        
        accountsPanel.add(accountScrollPane, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(color);
            }
        });
        return button;
    }
    
    private void loadCustomerAccounts() {
        accountTableModel.setRowCount(0);
        
        List<Account> accounts = accountDAO.getAccountsByCustomerId(customer.getCustomerId());
        BigDecimal totalBalance = BigDecimal.ZERO;
        
        for (Account account : accounts) {
            Object[] row = {
                account.getAccountNo(),
                account.getAccountType(),
                "$" + account.getBalance(),
                account.getInterestRate() + "%",
                account.getStatus(),
                account.getCreatedAt()
            };
            accountTableModel.addRow(row);
            totalBalance = totalBalance.add(account.getBalance());
        }
        
        totalBalanceLabel.setText("Total Balance: $" + totalBalance);
    }
    
    private void loadTransactionHistory(DefaultTableModel transactionModel) {
        transactionModel.setRowCount(0);
        
        List<Account> accounts = accountDAO.getAccountsByCustomerId(customer.getCustomerId());
        
        // Get transactions for all accounts
        for (Account account : accounts) {
            List<Transaction> transactions = transactionDAO.getTransactionsByAccount(account.getAccountNo());
            
            // Show only last 10 transactions per account
            int count = 0;
            for (Transaction transaction : transactions) {
                if (count >= 10) break;
                
                Object[] row = {
                    transaction.getTransactionDate(),
                    transaction.getTransactionType(),
                    "$" + transaction.getAmount(),
                    transaction.getDescription(),
                    transaction.getRecipientAccount() != null ? transaction.getRecipientAccount() : "N/A"
                };
                transactionModel.addRow(row);
                count++;
            }
        }
    }
    
    private void handleQuickAction(int actionIndex) {
        switch (actionIndex) {
            case 0: // View Balance
                viewSelectedAccountBalance();
                break;
            case 1: // Deposit
                openDepositWindow();
                break;
            case 2: // Withdraw
                openWithdrawWindow();
                break;
            case 3: // Transfer
                openTransferWindow();
                break;
        }
    }
    
    private void viewSelectedAccountBalance() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an account first",
                "No Account Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String accountNo = (String) accountTableModel.getValueAt(selectedRow, 0);
        String balance = (String) accountTableModel.getValueAt(selectedRow, 2);
        String accountType = (String) accountTableModel.getValueAt(selectedRow, 1);
        
        JOptionPane.showMessageDialog(this,
            "Account Details:\n\n" +
            "Account Number: " + accountNo + "\n" +
            "Account Type: " + accountType + "\n" +
            "Current Balance: " + balance + "\n" +
            "Status: " + accountTableModel.getValueAt(selectedRow, 4),
            "Account Balance",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void openDepositWindow() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an account for deposit",
                "No Account Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String accountNo = (String) accountTableModel.getValueAt(selectedRow, 0);
        
        JTextField amountField = new JTextField();
        JTextArea descriptionField = new JTextArea(3, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        
        Object[] message = {
            "Account: " + accountNo,
            "Deposit Amount ($):", amountField,
            "Description:", new JScrollPane(descriptionField)
        };
        
        int option = JOptionPane.showConfirmDialog(this,
            message,
            "Deposit Money",
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                BigDecimal amount = new BigDecimal(amountField.getText().trim());
                String description = descriptionField.getText().trim();
                
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this,
                        "Amount must be greater than zero",
                        "Invalid Amount",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                boolean success = bankingService.deposit(accountNo, amount, description);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Deposit successful!\nAmount: $" + amount + "\nTo Account: " + accountNo,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshDashboard(null);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Deposit failed. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid amount",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void openWithdrawWindow() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an account for withdrawal",
                "No Account Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String accountNo = (String) accountTableModel.getValueAt(selectedRow, 0);
        BigDecimal currentBalance = new BigDecimal(((String) accountTableModel.getValueAt(selectedRow, 2)).replace("$", ""));
        
        JTextField amountField = new JTextField();
        JTextArea descriptionField = new JTextArea(3, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        
        Object[] message = {
            "Account: " + accountNo,
            "Current Balance: $" + currentBalance,
            "Withdrawal Amount ($):", amountField,
            "Description:", new JScrollPane(descriptionField)
        };
        
        int option = JOptionPane.showConfirmDialog(this,
            message,
            "Withdraw Money",
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                BigDecimal amount = new BigDecimal(amountField.getText().trim());
                String description = descriptionField.getText().trim();
                
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this,
                        "Amount must be greater than zero",
                        "Invalid Amount",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (amount.compareTo(currentBalance) > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Insufficient balance. Available: $" + currentBalance,
                        "Insufficient Funds",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                boolean success = bankingService.withdraw(accountNo, amount, description);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Withdrawal successful!\nAmount: $" + amount + "\nFrom Account: " + accountNo,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshDashboard(null);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Withdrawal failed. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid amount",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void openTransferWindow() {
        int selectedRow = accountTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a source account",
                "No Account Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String fromAccount = (String) accountTableModel.getValueAt(selectedRow, 0);
        BigDecimal currentBalance = new BigDecimal(((String) accountTableModel.getValueAt(selectedRow, 2)).replace("$", ""));
        
        JTextField toAccountField = new JTextField();
        JTextField amountField = new JTextField();
        JTextArea descriptionField = new JTextArea(3, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        
        Object[] message = {
            "From Account: " + fromAccount,
            "Current Balance: $" + currentBalance,
            "To Account Number:", toAccountField,
            "Transfer Amount ($):", amountField,
            "Description:", new JScrollPane(descriptionField)
        };
        
        int option = JOptionPane.showConfirmDialog(this,
            message,
            "Transfer Funds",
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                String toAccount = toAccountField.getText().trim();
                BigDecimal amount = new BigDecimal(amountField.getText().trim());
                String description = descriptionField.getText().trim();
                
                if (toAccount.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Please enter recipient account number",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (toAccount.equals(fromAccount)) {
                    JOptionPane.showMessageDialog(this,
                        "Cannot transfer to the same account",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this,
                        "Amount must be greater than zero",
                        "Invalid Amount",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (amount.compareTo(currentBalance) > 0) {
                    JOptionPane.showMessageDialog(this,
                        "Insufficient balance. Available: $" + currentBalance,
                        "Insufficient Funds",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Check if recipient account exists
                Account recipientAccount = accountDAO.getAccountByNumber(toAccount);
                if (recipientAccount == null) {
                    JOptionPane.showMessageDialog(this,
                        "Recipient account does not exist",
                        "Invalid Account",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                boolean success = bankingService.transfer(fromAccount, toAccount, amount, description);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Transfer successful!\n" +
                        "From: " + fromAccount + "\n" +
                        "To: " + toAccount + "\n" +
                        "Amount: $" + amount,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshDashboard(null);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Transfer failed. Please check account details and try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid amount",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void refreshDashboard(DefaultTableModel transactionModel) {
        loadCustomerAccounts();
        if (transactionModel != null) {
            loadTransactionHistory(transactionModel);
        }
    }
    
    private void changePassword() {
        JPasswordField currentPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        
        Object[] message = {
            "Current Password:", currentPasswordField,
            "New Password:", newPasswordField,
            "Confirm New Password:", confirmPasswordField
        };
        
        int option = JOptionPane.showConfirmDialog(this,
            message,
            "Change Password",
            JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            // Verify current password
            Customer verifiedCustomer = customerDAO.authenticateCustomer(customer.getUsername(), currentPassword);
            if (verifiedCustomer == null) {
                JOptionPane.showMessageDialog(this,
                    "Current password is incorrect",
                    "Authentication Failed",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                    "New passwords do not match",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (newPassword.length() < 4) {
                JOptionPane.showMessageDialog(this,
                    "Password must be at least 4 characters",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            boolean success = customerDAO.updateCustomerPassword(customer.getCustomerId(), newPassword);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Password changed successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to change password",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}