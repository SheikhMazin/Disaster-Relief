package edu.ucalgary.oop;

/**
 * DatabaseManager
 *
 * Singleton class responsible for managing the PostgreSQL database connection.
 * Reads connection credentials from a configuration file located at
 * src/main/resources/db.config to avoid hardcoding sensitive information.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static DatabaseManager instance;

    private final String url;
    private final String username;
    private final String password;
    private Connection connection;

    /** Path to the external database credentials config file. */
    private static final String CONFIG_FILE = "src/main/resources/db.config";


    /**
     * Private constructor — reads database credentials from the config file.
     * Format of db.config (one value per line): url, username, password.
     *
     * @throws RuntimeException if the config file cannot be read
     */
    private DatabaseManager() {
        String[] creds = readConfig();
        this.url      = creds[0];
        this.username = creds[1];
        this.password = creds[2];
    }

    /**
     * Returns the single shared instance of DatabaseManager, creating it if
     * it does not yet exist (lazy initialization).
     *
     * @return the singleton DatabaseManager instance
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Opens a JDBC connection to the PostgreSQL database using the credentials
     * loaded from the config file.
     *
     * @return the opened Connection, or null if the connection failed
     * @throws SQLException if a database access error occurs (caught internally
     *                      and printed to stderr; null is returned instead)
     */
    public Connection connect() {
        try {
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
        return this.connection;
    }

    /**
     * Closes the current database connection if one is open.
     *
     * @throws SQLException if closing the connection fails (caught internally
     *                      and printed to stderr)
     */
    public void disconnect() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Disconnect failed: " + e.getMessage());
        }
    }

    /**
     * Returns the current Connection object without opening a new one.
     *
     * @return the active Connection, or null if connect() has not been called
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Reads the database credentials from the config file.
     * Expected format — three lines in order: url, username, password.
     *
     * @return a String array of length 3: {url, username, password}
     * @throws RuntimeException if the file is missing or cannot be parsed
     */
    private String[] readConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            String url      = reader.readLine();
            String username = reader.readLine();
            String password = reader.readLine();

            if (url == null || username == null || password == null) {
                throw new RuntimeException("db.config is malformed — expected 3 lines: url, username, password.");
            }

            return new String[]{ url.trim(), username.trim(), password.trim() };

        } catch (IOException e) {
            throw new RuntimeException("Could not read database config file: " + CONFIG_FILE, e);
        }
    }
}