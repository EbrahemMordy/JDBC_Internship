package org.intern.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Configuration Class
 * 
 * This class handles all database connection related operations.
 * It contains the database connection details and provides methods to:
 * 1. Establish connection to MySQL database
 * 2. Close database connections properly
 * 
 * JDBC Basics for Interns:
 * - JDBC (Java Database Connectivity) is an API for connecting Java applications to databases
 * - Connection object represents a database connection
 * - Always close connections to prevent memory leaks
 */
public class DatabaseConfig {
    
    // Database connection parameters - These should be provided via environment variables
    // or configuration files for security reasons
    private static final String DB_URL = System.getenv("DB_URL") != null ? 
        System.getenv("DB_URL") : 
        "jdbc:mysql://jdbc-sample-ebrahemmordyasr-c010.d.aivencloud.com:20820/defaultdb?ssl-mode=REQUIRED";
    private static final String DB_USERNAME = System.getenv("DB_USERNAME") != null ? 
        System.getenv("DB_USERNAME") : 
        "avnadmin";
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD") != null ? 
        System.getenv("DB_PASSWORD") : 
        "YOUR_PASSWORD_HERE"; // Replace with your actual password
    
    // JDBC Driver class name - MySQL driver
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    
    /**
     * Get Database Connection
     * 
     * This method establishes a connection to the MySQL database.
     * 
     * Steps:
     * 1. Load the JDBC driver (MySQL driver in this case)
     * 2. Use DriverManager to create connection with URL, username, and password
     * 3. Return the connection object
     * 
     * @return Connection object to interact with database
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Step 1: Load the JDBC driver
            // This registers the MySQL driver with DriverManager
            Class.forName(DRIVER_CLASS);
            
            // Step 2: Create and return database connection
            // DriverManager.getConnection() creates a connection to the specified database
            System.out.println("Connecting to database...");
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Database connection established successfully!");
            
            return connection;
            
        } catch (ClassNotFoundException e) {
            // This happens if MySQL driver is not found in classpath
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
            throw new SQLException("Database driver not found", e);
        } catch (SQLException e) {
            // This happens if connection fails (wrong credentials, network issues, etc.)
            System.err.println("Failed to connect to database!");
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Close Database Connection
     * 
     * This method properly closes a database connection.
     * Always close connections to free up database resources!
     * 
     * @param connection The connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed successfully!");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Test Database Connection
     * 
     * This method tests if we can successfully connect to the database.
     * Useful for debugging connection issues.
     * 
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection connection = getConnection();
            closeConnection(connection);
            return true;
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
