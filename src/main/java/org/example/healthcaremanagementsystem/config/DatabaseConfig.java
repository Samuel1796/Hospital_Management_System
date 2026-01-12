package org.example.healthcaremanagementsystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database configuration and connection management class.
 * Follows Single Responsibility Principle by handling only database connectivity.
 * 
 * @author Healthcare Management System Team
 * @version 1.0
 */
public class DatabaseConfig {
    
    // Database connection parameters
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/healthcare_db";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "0050";
    
    // Instance for connection management
    private static DatabaseConfig instance;
    
    /**
     * Private constructor to enforce singleton pattern.
     */
    private DatabaseConfig() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Returns the singleton instance of DatabaseConfig.
     * Follows Singleton pattern for resource management.
     * 
     * @return DatabaseConfig instance
     */
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }
    
    // Flag to track if connection message has been shown
    private static boolean connectionMessageShown = false;
    
    /**
     * Establishes a connection to the PostgreSQL database.
     * Creates a new connection for each request (proper connection handling).
     * 
     * @return Connection object to the database
     * @throws SQLException if database access error occurs
     */
    public Connection getConnection() throws SQLException {
        try {
            // Load PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            
            // Set connection properties for optimal performance
            Properties props = new Properties();
            props.setProperty("user", DB_USER);
            props.setProperty("password", DB_PASSWORD);
            props.setProperty("ssl", "false");
            
            // Establish connection
            Connection conn = DriverManager.getConnection(DB_URL, props);
            
            // Enable auto-commit for transaction management
            conn.setAutoCommit(true);
            
            // Only show connection message once
            if (!connectionMessageShown) {
                System.out.println("Database connection established successfully.");
                connectionMessageShown = true;
            }
            
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found.", e);
        }
    }
    
    /**
     * Closes the database connection.
     * Properly releases database resources.
     * 
     * @param connection Connection to close
     * @throws SQLException if database access error occurs
     */
    public void closeConnection(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    
    /**
     * Tests the database connection.
     * Useful for connection validation during application startup.
     * 
     * @return true if connection is valid, false otherwise
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
            return false;
        }
    }
}

