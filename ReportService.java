/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.time.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import model.Transaction;
import util.FileLogger;
import util.PDFGenerator;

/**
 *
 * @author oscar
 */

import dao.*;
import java.math.BigDecimal;
import model.*;
import java.util.List;
import java.time.LocalDateTime;

public class ReportService {
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    
    public ReportService() {
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }
    
    // Customer reports
    public String generateCustomerReport() {
        List<Customer> customers = customerDAO.getAllCustomers();
        StringBuilder report = new StringBuilder();
        
        report.append("========== CUSTOMER REPORT ==========\n");
        report.append(String.format("%-10s %-20s %-25s %-15s %-15s\n", 
            "ID", "Name", "Email", "Phone", "Joined Date"));
        report.append("--------------------------------------------------------\n");
        
        for (Customer customer : customers) {
            report.append(String.format("%-10d %-20s %-25s %-15s %-15s\n",
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCreatedAt()));
        }
        
        report.append("\nTotal Customers: ").append(customers.size()).append("\n");
        return report.toString();
    }
    
    // Account reports
    public String generateAccountReport() {
        List<Account> accounts = accountDAO.getAllAccounts();
        StringBuilder report = new StringBuilder();
        
        report.append("========== ACCOUNT REPORT ==========\n");
        report.append(String.format("%-15s %-12s %-15s %-12s %-10s\n", 
            "Account No", "Type", "Balance", "Interest", "Status"));
        report.append("----------------------------------------------------\n");
        
        BigDecimal totalBalance = new java.math.BigDecimal("0");
        
        for (Account account : accounts) {
            report.append(String.format("%-15s %-12s %-15s %-12s %-10s\n",
                account.getAccountNo(),
                account.getAccountType(),
                account.getBalance(),
                account.getInterestRate() + "%",
                account.getStatus()));
            
            totalBalance = totalBalance.add(account.getBalance());
        }
        
        report.append("\nTotal Accounts: ").append(accounts.size()).append("\n");
        report.append("Total Balance: ").append(totalBalance).append("\n");
        return report.toString();
    }
    
    // Transaction reports
    public String generateTransactionReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionDAO.getTransactionsByDateRange(startDate, endDate);
        StringBuilder report = new StringBuilder();
        
        report.append("========== TRANSACTION REPORT ==========\n");
        report.append(String.format("%-12s %-15s %-10s %-12s %-20s %-25s\n", 
            "ID", "Account", "Type", "Amount", "Recipient", "Date"));
        report.append("-----------------------------------------------------------------\n");
        
        BigDecimal totalDeposits = new java.math.BigDecimal("0");
        BigDecimal totalWithdrawals = new java.math.BigDecimal("0");
        
        for (Transaction transaction : transactions) {
            report.append(String.format("%-12d %-15s %-10s %-12s %-20s %-25s\n",
                transaction.getTransactionId(),
                transaction.getAccountNo(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getRecipientAccount() != null ? transaction.getRecipientAccount() : "N/A",
                transaction.getTransactionDate()));
            
            if ("DEPOSIT".equals(transaction.getTransactionType())) {
                totalDeposits = totalDeposits.add(transaction.getAmount());
            } else if ("WITHDRAW".equals(transaction.getTransactionType())) {
                totalWithdrawals = totalWithdrawals.add(transaction.getAmount());
            }
        }
        
        report.append("\nPeriod: ").append(startDate).append(" to ").append(endDate).append("\n");
        report.append("Total Transactions: ").append(transactions.size()).append("\n");
        report.append("Total Deposits: ").append(totalDeposits).append("\n");
        report.append("Total Withdrawals: ").append(totalWithdrawals).append("\n");
        report.append("Net Flow: ").append(totalDeposits.subtract(totalWithdrawals)).append("\n");
        
        return report.toString();
    }
    
    // Daily summary report
    public String generateDailySummary() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startOfDay = today.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = today.withHour(23).withMinute(59).withSecond(59);
        
        List<Transaction> todayTransactions = transactionDAO.getTransactionsByDateRange(startOfDay, endOfDay);
        
        StringBuilder summary = new StringBuilder();
        summary.append("========== DAILY SUMMARY ==========\n");
        summary.append("Date: ").append(today.toLocalDate()).append("\n");
        summary.append("Total Transactions: ").append(todayTransactions.size()).append("\n");
        
        // Count by type
        long depositCount = todayTransactions.stream()
            .filter(t -> "DEPOSIT".equals(t.getTransactionType()))
            .count();
        long withdrawCount = todayTransactions.stream()
            .filter(t -> "WITHDRAW".equals(t.getTransactionType()))
            .count();
        long transferCount = todayTransactions.stream()
            .filter(t -> "TRANSFER".equals(t.getTransactionType()))
            .count();
        
        summary.append("Deposits: ").append(depositCount).append("\n");
        summary.append("Withdrawals: ").append(withdrawCount).append("\n");
        summary.append("Transfers: ").append(transferCount).append("\n");
        
        return summary.toString();
    }
    
    // Generate PDF report (placeholder - would integrate with iText library)
    public boolean generatePDFReport(String reportContent, String filename) {
        try {
            // In a real implementation, use iText library to create PDF
            System.out.println("Generating PDF report: " + filename);
            System.out.println(reportContent);
            return true;
        } catch (Exception e) {
            System.err.println("Error generating PDF: " + e.getMessage());
            return false;
        }
    }
}
