/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package network;

/**
 *
 * @author oscar
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class BankClient extends JFrame {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private JTextArea chatArea;
    private JTextField commandField;
    private JButton connectButton;
    private boolean connected = false;
    
    public BankClient() {
        initComponents();
        setTitle("Bank Client - Remote Banking");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Connection panel
        JPanel connectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel hostLabel = new JLabel("Host:");
        JTextField hostField = new JTextField("localhost", 15);
        
        JLabel portLabel = new JLabel("Port:");
        JTextField portField = new JTextField("8080", 8);
        
        connectButton = new JButton("Connect");
        connectButton.setBackground(new Color(39, 174, 96));
        connectButton.setForeground(Color.WHITE);
        connectButton.addActionListener(e -> connectToServer(
            hostField.getText(), 
            Integer.parseInt(portField.getText())
        ));
        
        JButton disconnectButton = new JButton("Disconnect");
        disconnectButton.setBackground(new Color(231, 76, 60));
        disconnectButton.setForeground(Color.WHITE);
        disconnectButton.addActionListener(e -> disconnect());
        
        connectionPanel.add(hostLabel);
        connectionPanel.add(hostField);
        connectionPanel.add(portLabel);
        connectionPanel.add(portField);
        connectionPanel.add(connectButton);
        connectionPanel.add(disconnectButton);
        
        // Chat area for server responses
        chatArea = new JTextArea(20, 50);
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        
        // Command input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        
        commandField = new JTextField();
        commandField.setEnabled(false);
        commandField.addActionListener(e -> sendCommand());
        
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendCommand());
        sendButton.setEnabled(false);
        
        inputPanel.add(new JLabel("Command: "), BorderLayout.WEST);
        inputPanel.add(commandField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        // Help panel
        JPanel helpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        helpPanel.setBorder(BorderFactory.createTitledBorder("Quick Commands"));
        
        String[] commands = {"HELP", "BALANCE ACC123", "DEPOSIT ACC123 500", 
                            "WITHDRAW ACC123 200", "TRANSFER ACC123 ACC456 300"};
        
        for (String cmd : commands) {
            JButton cmdButton = new JButton(cmd);
            cmdButton.addActionListener(e -> {
                if (connected) {
                    commandField.setText(cmd);
                    sendCommand();
                } else {
                    appendMessage("Please connect to server first");
                }
            });
            helpPanel.add(cmdButton);
        }
        
        // Add all panels
        mainPanel.add(connectionPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(helpPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void connectToServer(String host, int port) {
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            connected = true;
            connectButton.setEnabled(false);
            commandField.setEnabled(true);
            
            appendMessage("Connected to server at " + host + ":" + port);
            
            // Start a thread to listen for server messages
            new Thread(this::receiveMessages).start();
            
        } catch (IOException e) {
            appendMessage("Connection failed: " + e.getMessage());
        }
    }
    
    private void disconnect() {
        if (connected) {
            try {
                out.println("QUIT");
                socket.close();
                connected = false;
                connectButton.setEnabled(true);
                commandField.setEnabled(false);
                appendMessage("Disconnected from server");
            } catch (IOException e) {
                appendMessage("Error disconnecting: " + e.getMessage());
            }
        }
    }
    
    private void sendCommand() {
        if (!connected) {
            appendMessage("Not connected to server");
            return;
        }
        
        String command = commandField.getText().trim();
        if (!command.isEmpty()) {
            appendMessage("> " + command);
            out.println(command);
            commandField.setText("");
        }
    }
    
    private void receiveMessages() {
        try {
            String message;
            while (connected && (message = in.readLine()) != null) {
                appendMessage(message);
            }
        } catch (IOException e) {
            if (connected) {
                appendMessage("Connection lost: " + e.getMessage());
                connected = false;
                connectButton.setEnabled(true);
                commandField.setEnabled(false);
            }
        }
    }
    
    private void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankClient client = new BankClient();
            client.setVisible(true);
        });
    }
}
