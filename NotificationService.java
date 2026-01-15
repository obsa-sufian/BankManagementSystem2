/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;


import java.util.Properties;
//import javax.mail.*;
//import javax.mail.internet.*;
import java.io.FileInputStream;
import java.io.IOException;

public class NotificationService {
    private Properties emailProperties;
    
    public NotificationService() {
        emailProperties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("resources/config.properties");
            emailProperties.load(fis);
        } catch (IOException e) {
            System.err.println("Error loading email configuration: " + e.getMessage());
        }
    }
    
    public void sendEmail(String toEmail, String subject, String body) {
        // This is a simplified version. In real application, configure properly
        System.out.println("Sending email to: " + toEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        
        /*
        // Real email sending code (commented out for safety)
        Properties props = new Properties();
        props.put("mail.smtp.auth", emailProperties.getProperty("smtp.auth"));
        props.put("mail.smtp.starttls.enable", emailProperties.getProperty("smtp.starttls.enable"));
        props.put("mail.smtp.host", emailProperties.getProperty("smtp.host"));
        props.put("mail.smtp.port", emailProperties.getProperty("smtp.port"));
        
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                    emailProperties.getProperty("smtp.username"),
                    emailProperties.getProperty("smtp.password")
                );
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailProperties.getProperty("smtp.username")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);
            
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
        */
    }
    
    public void sendTransactionNotification(String email, String accountNo, String transactionType, String amount) {
        String subject = "Transaction Notification - Bank Management System";
        String body = String.format(
            "Dear Customer,\n\n" +
            "A %s transaction has been made on your account %s.\n" +
            "Amount: %s\n\n" +
            "If you did not initiate this transaction, please contact our customer support immediately.\n\n" +
            "Thank you,\n" +
            "Bank Management System",
            transactionType, accountNo, amount
        );
        
        sendEmail(email, subject, body);
    }
    
    public void sendAccountCreationNotification(String email, String customerName, String accountNo) {
        String subject = "Account Created Successfully";
        String body = String.format(
            "Dear %s,\n\n" +
            "Your account has been successfully created!\n" +
            "Account Number: %s\n\n" +
            "Welcome to our banking services.\n\n" +
            "Thank you,\n" +
            "Bank Management System",
            customerName, accountNo
        );
        
        sendEmail(email, subject, body);
    }
    
    public void sendLowBalanceAlert(String email, String accountNo, String currentBalance) {
        String subject = "Low Balance Alert";
        String body = String.format(
            "Dear Customer,\n\n" +
            "Your account %s has a low balance.\n" +
            "Current Balance: %s\n\n" +
            "Please consider depositing funds to avoid service charges.\n\n" +
            "Thank you,\n" +
            "Bank Management System",
            accountNo, currentBalance
        );
        
        sendEmail(email, subject, body);
    }
}

