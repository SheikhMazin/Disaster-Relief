package edu.ucalgary.oop;

/**
 * ActionLogger
 *
 * Singleton class responsible for writing all user-driven action log entries
 * to a plain text file (action_log.txt) stored in the data/ directory.
 * Using the Singleton pattern ensures log entries are written in the correct
 * order, that only one part of the program has the file open at any time,
 * and that any class can access the logger without creating dependencies on
 * file paths or output streams.
 *
 * Each log entry records the action type (Added, Updated, Deleted, Soft Deleted),
 * the date the action occurred, and a description of the change. Only
 * successfully completed user-driven actions are logged — application errors,
 * failed operations, and system events must not appear in the log.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class ActionLogger {

    private static ActionLogger instance;
    private static final String LOG_FILE = "data/action_log.txt";

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Private constructor — prevents external instantiation.
     * Use {@link #getInstance()} to obtain the shared instance.
     */
    private ActionLogger() {}

    // =========================================================================
    //  Singleton accessor
    // =========================================================================

    /**
     * Returns the single shared instance of ActionLogger, creating it if
     * it does not yet exist (lazy initialization).
     *
     * @return the singleton ActionLogger instance
     */
    public static ActionLogger getInstance() {
        if (instance == null) {
            instance = new ActionLogger();
        }
        return instance;
    }

    // =========================================================================
    //  Public logging methods
    // =========================================================================

    /**
     * Logs that a new entity was successfully added to the system.
     * Entry format: [date] ADDED entityType | description
     *
     * @param entityType  non-null, non-empty label for the type of entity
     *                    (e.g., "disaster victim", "supply")
     * @param description non-null, non-empty description of what was added
     * @throws IllegalArgumentException if entityType or description is null or blank
     */
    public void logAdded(String entityType, String description) {
        validateArgs(entityType, description);
        writeEntry(String.format("[%s] ADDED %s | %s", LocalDate.now(), entityType, description));
    }

    /**
     * Logs that an existing entity was successfully updated in the system.
     * Entry format: [date] UPDATED entityType | description
     *
     * @param entityType  non-null, non-empty label for the type of entity
     * @param description non-null, non-empty description of what changed
     * @throws IllegalArgumentException if entityType or description is null or blank
     */
    public void logUpdated(String entityType, String description) {
        validateArgs(entityType, description);
        writeEntry(String.format("[%s] UPDATED %s | %s", LocalDate.now(), entityType, description));
    }

    /**
     * Logs that an entity was successfully hard-deleted from the system.
     * Entry format: [date] DELETED entityType | description
     *
     * @param entityType  non-null, non-empty label for the type of entity
     * @param description non-null, non-empty description of what was deleted
     * @throws IllegalArgumentException if entityType or description is null or blank
     */
    public void logDeleted(String entityType, String description) {
        validateArgs(entityType, description);
        writeEntry(String.format("[%s] DELETED %s | %s", LocalDate.now(), entityType, description));
    }

    /**
     * Logs that a disaster victim was successfully soft-deleted from the system.
     * The victim's data remains in the database but is hidden in the UI.
     * Entry format: [date] SOFT DELETED entityType | description
     *
     * @param entityType  non-null, non-empty label for the type of entity
     * @param description non-null, non-empty description of who was soft-deleted
     * @throws IllegalArgumentException if entityType or description is null or blank
     */
    public void logSoftDeleted(String entityType, String description) {
        validateArgs(entityType, description);
        writeEntry(String.format("[%s] SOFT DELETED %s | %s", LocalDate.now(), entityType, description));
    }

    // =========================================================================
    //  Private helpers
    // =========================================================================

    /**
     * Appends a single log entry to the log file, followed by a newline.
     * If the file does not exist it is created. If writing fails, the error
     * is printed to stderr but does not propagate — a logging failure should
     * never crash the application.
     *
     * @param entry the fully formatted log entry string to write
     */
    private void writeEntry(String entry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("ActionLogger: failed to write log entry — " + e.getMessage());
        }
    }

    /**
     * Validates that the entity type and description arguments are non-null
     * and non-blank before writing a log entry.
     *
     * @param entityType  the entity type string to validate
     * @param description the description string to validate
     * @throws IllegalArgumentException if either argument is null or blank
     */
    private void validateArgs(String entityType, String description) {
        if (entityType == null || entityType.trim().isEmpty()) {
            throw new IllegalArgumentException("entityType cannot be null or empty.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("description cannot be null or empty.");
        }
    }
}