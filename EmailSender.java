/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author oscar
 */
import java.util.Properties;
//import javax.mail.*;
//import javax.mail.internet.*;

public class EmailSender {
    private static final String FROM_EMAIL = "noreply@bankmanagement.com";
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    
    // In production, these should be in configuration file
    private static final String USERNAME = "saabirsufiyan@gmail.com";
    private static final String PASSWORD = "your-password";
    
    public static boolean sendEmail(String toEmail, String subject, String body) {
        // For demonstration, we'll just print the email
        System.out.println("=========================================");
        System.out.println("TO: " + toEmail);
        System.out.println("SUBJECT: " + subject);
        System.out.println("BODY:\n" + body);
        System.out.println("=========================================");
        
        // Uncomment the following code for real email sending
        
        /*
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);
            
            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
            return true;
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
            return false;
        }
        */
        
        return true; // For demo purposes
    }
    
    public static void sendWelcomeEmail(String toEmail, String customerName, String accountNumber) {
        String subject = "Welcome to Bank Management System";
        String body = String.format(
            "Dear %s,\n\n" +
            "Welcome to our Bank Management System!\n\n" +
            "Your account has been successfully created.\n" +
            "Account Number: %s\n\n" +
            "You can now access your account and perform transactions.\n\n" +
            "Thank you for choosing our services.\n\n" +
            "Best regards,\n" +
            "Bank Management Team",
            customerName, accountNumber
        );
        
        sendEmail(toEmail, subject, body);
    }
    
    public static void sendTransactionAlert(String toEmail, String accountNumber, 
                                           String transactionType, double amount, double balance) {
        String subject = "Transaction Alert - " + transactionType;
        String body = String.format(
            "Dear Customer,\n\n" +
            "A %s transaction has been made on your account.\n\n" +
            "Account Number: %s\n" +
            "Transaction Type: %s\n" +
            "Amount: %.2f\n" +
            "Current Balance: %.2f\n\n" +
            "If you did not perform this transaction, please contact us immediately.\n\n" +
            "Thank you,\n" +
            "Bank Management Team",
            transactionType, accountNumber, transactionType, amount, balance
        );
        
        sendEmail(toEmail, subject, body);
    }
    
    public static void sendMonthlyStatement(String toEmail, String customerName, 
                                           String accountNumber, String statementPeriod) {
        String subject = "Monthly Account Statement - " + statementPeriod;
        String body = String.format(
            "Dear %s,\n\n" +
            "Please find your monthly account statement attached.\n\n" +
            "Account Number: %s\n" +
            "Statement Period: %s\n\n" +
            "If you have any questions, please contact our customer service.\n\n" +
            "Best regards,\n" +
            "Bank Management Team",
            customerName, accountNumber, statementPeriod
        );
        
        sendEmail(toEmail, subject, body);
    }
}