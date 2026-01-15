/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author oscar
 */
/**
import ui.LoginFrame;
import network.BankServer;

public class MainApplication {
    public static void main(String[] args) {
        // Start GUI Application
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
        
        // Uncomment to start server in separate thread
        
        Thread serverThread = new Thread(() -> {
            BankServer server = new BankServer(8080);
            server.start();
        });
        serverThread.start();
        
    }
}
*/
import dao.DatabaseConnection;
import ui.WelcomeFrame;

public class MainApplication {
    public static void main(String[] args) {
        // Add shutdown hook to close database connection
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down application...");
            DatabaseConnection.closeConnection();
        }));
        
        // Start with Welcome Screen
        javax.swing.SwingUtilities.invokeLater(() -> {
            WelcomeFrame welcomeFrame = new WelcomeFrame();
            welcomeFrame.setVisible(true);
        });
    }
}