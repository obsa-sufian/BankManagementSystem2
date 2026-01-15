/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author oscar
 */


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger {
    private static final String LOG_FILE = "logs/transactions.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    static {
        // Create logs directory if it doesn't exist
        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
    }
    
    public static synchronized void log(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            String timestamp = LocalDateTime.now().format(formatter);
            out.println("[" + timestamp + "] " + message);
            
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
    
    public static synchronized void logTransaction(String accountNo, String type, double amount) {
        String message = String.format("Transaction - Account: %s, Type: %s, Amount: %.2f", 
            accountNo, type, amount);
        log(message);
    }
    
    public static synchronized void logError(String errorMessage) {
        String message = "ERROR: " + errorMessage;
        log(message);
    }
    
    public static synchronized void logLogin(String username, boolean success) {
        String status = success ? "SUCCESS" : "FAILED";
        String message = String.format("Login attempt - Username: %s, Status: %s", username, status);
        log(message);
    }
    
    public static String readLogFile() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading log file: " + e.getMessage());
        }
        return content.toString();
    }
    
    public static void clearLogFile() {
        try (PrintWriter writer = new PrintWriter(LOG_FILE)) {
            writer.print("");
        } catch (FileNotFoundException e) {
            System.err.println("Error clearing log file: " + e.getMessage());
        }
    }
}
