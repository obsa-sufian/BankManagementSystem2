/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author oscar
 */
import java.math.BigDecimal;

public class Account {
    private String accountNo;
    private int customerId;
    private String accountType; // SAVINGS, CURRENT, FIXED_DEPOSIT
    private BigDecimal balance;
    private BigDecimal interestRate;
    private String createdAt;
    private String status; // ACTIVE, INACTIVE, BLOCKED
    
    public Account() {
        this.balance = BigDecimal.ZERO;
        this.interestRate = new BigDecimal("2.5");
        this.status = "ACTIVE";
    }
    
    public Account(String accountNo, int customerId, String accountType) {
        this();
        this.accountNo = accountNo;
        this.customerId = customerId;
        this.accountType = accountType;
    }
    
    // Getters and Setters
    public String getAccountNo() { return accountNo; }
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
    
    public void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }
    
    @Override
    public String toString() {
        return "Account [No: " + accountNo + ", Type: " + accountType + 
               ", Balance: " + balance + ", Status: " + status + "]";
    }
}
