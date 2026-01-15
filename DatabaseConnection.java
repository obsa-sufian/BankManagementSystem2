/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author oscar
 */
import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseConnection {
    private static Connection connection = null;
    private static final Properties properties = new Properties();
    
    static {
        try {
            // Load configuration
            FileInputStream fis = new FileInputStream("resources/config.properties");
            properties.load(fis);
            
            // Register JDBC driver
            Class.forName(properties.getProperty("db.driver"));
        } catch (ClassNotFoundException | IOException e) {
        }
    }
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                String url = properties.getProperty("db.url");
                String username = properties.getProperty("db.username");
                String password = properties.getProperty("db.password");
                
                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Database connection established successfully!");
            } catch (SQLException e) {
                System.err.println("Failed to connect to database: " + e.getMessage());
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database connection test successful!");
            }
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
        }
    }
}


/**
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {

    private static final Properties properties = new Properties();

    static {
        try (InputStream is = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (is == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }

            properties.load(is);

            // Load JDBC driver
            Class.forName(properties.getProperty("db.driver"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ ALWAYS return a NEW connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );
    }
}
*/