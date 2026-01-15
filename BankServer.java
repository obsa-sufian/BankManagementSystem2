/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network;

/**
 *
 * @author oscar
 */

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import service.BankingService;

public class BankServer {
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private BankingService bankingService;
    private boolean running;
    
    public BankServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            threadPool = Executors.newFixedThreadPool(10);
            bankingService = new BankingService();
            running = true;
            
            System.out.println("Bank Server started on port " + port);
            System.out.println("Waiting for client connections...");
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }
    
    public void start() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                
                // Handle client in separate thread
                threadPool.submit(new ClientHandler(clientSocket, bankingService));
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
            }
        }
    }
    
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            threadPool.shutdown();
            bankingService.shutdown();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }
    
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BankingService bankingService;
        private BufferedReader in;
        private PrintWriter out;
        
        public ClientHandler(Socket socket, BankingService service) {
            this.clientSocket = socket;
            this.bankingService = service;
        }
        
        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                
                out.println("Welcome to Bank Management System Server");
                out.println("Enter command (HELP for available commands):");
                
                String command;
                while ((command = in.readLine()) != null) {
                    System.out.println("Received command: " + command);
                    String response = processCommand(command);
                    out.println(response);
                    
                    if ("QUIT".equalsIgnoreCase(command)) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    if (clientSocket != null) clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
        
        private String processCommand(String command) {
            String[] parts = command.split(" ");
            String action = parts[0].toUpperCase();
            
            switch (action) {
                case "HELP":
                    return "Available commands:\n" +
                           "HELP - Show this help\n" +
                           "BALANCE <account_no> - Check account balance\n" +
                           "TRANSFER <from> <to> <amount> - Transfer money\n" +
                           "DEPOSIT <account> <amount> - Deposit money\n" +
                           "WITHDRAW <account> <amount> - Withdraw money\n" +
                           "QUIT - Disconnect from server";
                
                case "BALANCE":
                    if (parts.length < 2) return "ERROR: Please provide account number";
                    // In real implementation, fetch balance from database
                    return "Balance for " + parts[1] + ": $1000.00 (Sample)";
                
                case "DEPOSIT":
                    if (parts.length < 3) return "ERROR: Please provide account and amount";
                    return "Deposit of " + parts[2] + " to " + parts[1] + " processed";
                
                case "WITHDRAW":
                    if (parts.length < 3) return "ERROR: Please provide account and amount";
                    return "Withdrawal of " + parts[2] + " from " + parts[1] + " processed";
                
                case "TRANSFER":
                    if (parts.length < 4) return "ERROR: Please provide from, to, and amount";
                    return "Transfer of " + parts[3] + " from " + parts[1] + " to " + parts[2] + " processed";
                
                case "QUIT":
                    return "Goodbye!";
                
                default:
                    return "ERROR: Unknown command. Type HELP for available commands.";
            }
        }
    }
    
    public static void main(String[] args) {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number. Using default port 8080.");
            }
        }
        
        BankServer server = new BankServer(port);
        
        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down server...");
            server.stop();
        }));
        
        // Start server
        server.start();
    }
}

