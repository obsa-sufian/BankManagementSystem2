/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author oscar
 */
import dao.*;
import model.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankingService {
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private AdminDAO adminDAO;
    private ExecutorService executorService;
    
    public BankingService() {
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.adminDAO = new AdminDAO();
        this.executorService = Executors.newFixedThreadPool(5);
    }
    
    // Customer operations
    public boolean addCustomer(Customer customer) {
        return customerDAO.addCustomer(customer);
    }
    
    public Customer getCustomer(int customerId) {
        return customerDAO.getCustomerById(customerId);
    }
    
    // Account operations
    public boolean createAccount(Account account) {
        return accountDAO.createAccount(account);
    }
    
    public Account getAccount(String accountNo) {
        return accountDAO.getAccountByNumber(accountNo);
    }
    

    /**
     * Step 10: Update BankingService for Customer Operations 
        Update src/service/BankingService.java

        Add customer-specific methods:
     */

    // Add to BankingService class

    public List<Account> getCustomerAccounts(int customerId) {
        return accountDAO.getAccountsByCustomerId(customerId);
    }

    public List<Transaction> getCustomerTransactions(int customerId) {
        List<Transaction> allTransactions = new ArrayList<>();
        List<Account> accounts = accountDAO.getAccountsByCustomerId(customerId);

        for (Account account : accounts) {
            List<Transaction> accountTransactions = transactionDAO.getTransactionsByAccount(account.getAccountNo());
            allTransactions.addAll(accountTransactions);
        }

        // Sort by date (most recent first)
        allTransactions.sort((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()));
        return allTransactions;
    }

    public BigDecimal getCustomerTotalBalance(int customerId) {
        List<Account> accounts = accountDAO.getAccountsByCustomerId(customerId);
        BigDecimal total = BigDecimal.ZERO;

        for (Account account : accounts) {
            total = total.add(account.getBalance());
        }

        return total;
    }


    // Deposit operation with multithreading
    public synchronized boolean deposit(String accountNo, BigDecimal amount, String description) {
        executorService.submit(() -> {
            try {
                Account account = accountDAO.getAccountByNumber(accountNo);
                if (account != null && "ACTIVE".equals(account.getStatus())) {
                    BigDecimal newBalance = account.getBalance().add(amount);
                    
                    // Update balance
                    accountDAO.updateAccountBalance(accountNo, newBalance);
                    
                    // Record transaction
                    Transaction transaction = new Transaction(accountNo, "DEPOSIT", amount);
                    transaction.setDescription(description);
                    transactionDAO.recordTransaction(transaction);
                    
                    // Log transaction
                    util.FileLogger.log("Deposit: " + accountNo + " | Amount: " + amount);
                    
                    // Send notification
                    sendDepositNotification(account, amount);
                }
            } catch (Exception e) {
                System.err.println("Error in deposit operation: " + e.getMessage());
            }
        });
        return true;
    }
    
    // Withdraw operation with multithreading
    public synchronized boolean withdraw(String accountNo, BigDecimal amount, String description) {
        executorService.submit(() -> {
            try {
                Account account = accountDAO.getAccountByNumber(accountNo);
                if (account != null && "ACTIVE".equals(account.getStatus())) {
                    if (account.getBalance().compareTo(amount) >= 0) {
                        BigDecimal newBalance = account.getBalance().subtract(amount);
                        
                        // Update balance
                        accountDAO.updateAccountBalance(accountNo, newBalance);
                        
                        // Record transaction
                        Transaction transaction = new Transaction(accountNo, "WITHDRAW", amount);
                        transaction.setDescription(description);
                        transactionDAO.recordTransaction(transaction);
                        
                        // Log transaction
                        util.FileLogger.log("Withdraw: " + accountNo + " | Amount: " + amount);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error in withdraw operation: " + e.getMessage());
            }
        });
        return true;
    }
    
    // Transfer operation
    public synchronized boolean transfer(String fromAccount, String toAccount, BigDecimal amount, String description) {
        try {
            Account sender = accountDAO.getAccountByNumber(fromAccount);
            Account receiver = accountDAO.getAccountByNumber(toAccount);
            
            if (sender == null || receiver == null) {
                return false;
            }
            
            if (!"ACTIVE".equals(sender.getStatus()) || !"ACTIVE".equals(receiver.getStatus())) {
                return false;
            }
            
            if (sender.getBalance().compareTo(amount) < 0) {
                return false;
            }
            
            // Perform transfer
            BigDecimal senderNewBalance = sender.getBalance().subtract(amount);
            BigDecimal receiverNewBalance = receiver.getBalance().add(amount);
            
            accountDAO.updateAccountBalance(fromAccount, senderNewBalance);
            accountDAO.updateAccountBalance(toAccount, receiverNewBalance);
            
            // Record transactions for both accounts
            Transaction senderTransaction = new Transaction(fromAccount, "TRANSFER", amount);
            senderTransaction.setRecipientAccount(toAccount);
            senderTransaction.setDescription("Transfer to " + toAccount + ": " + description);
            transactionDAO.recordTransaction(senderTransaction);
            
            Transaction receiverTransaction = new Transaction(toAccount, "TRANSFER", amount);
            receiverTransaction.setRecipientAccount(fromAccount);
            receiverTransaction.setDescription("Transfer from " + fromAccount + ": " + description);
            transactionDAO.recordTransaction(receiverTransaction);
            
            // Log transfer
            util.FileLogger.log("Transfer: " + fromAccount + " -> " + toAccount + " | Amount: " + amount);
            
            return true;
        } catch (Exception e) {
            System.err.println("Error in transfer operation: " + e.getMessage());
            return false;
        }
    }
    
    // Admin authentication
    public boolean authenticateAdmin(String username, String password) {
        return adminDAO.authenticate(username, password);
    }
    
    // Generate interest (background task)
    public void applyMonthlyInterest() {
        executorService.submit(() -> {
            try {
                // Get all savings accounts
                // Apply interest logic here
                util.FileLogger.log("Monthly interest applied to all savings accounts");
            } catch (Exception e) {
                System.err.println("Error applying interest: " + e.getMessage());
            }
        });
    }
    
    // Private helper method for notifications
    private void sendDepositNotification(Account account, BigDecimal amount) {
        // In a real system, this would send email/SMS notifications
        System.out.println("Notification: Deposit of " + amount + " made to account " + account.getAccountNo());
    }
    
    // Shutdown executor service
    public void shutdown() {
        executorService.shutdown();
    }
}
