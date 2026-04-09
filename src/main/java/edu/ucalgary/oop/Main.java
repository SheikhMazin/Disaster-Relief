package edu.ucalgary.oop;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Main
 *
 * Entry point for the Disaster Relief System application.
 * Responsible for wiring together all dependencies and launching the GUI.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

public class Main {

    /**
     * Application entry point. Initialises the database connection, creates
     * all service and repository instances, starts the controller, and
     * launches the Swing GUI on the Event Dispatch Thread.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        try {
            // ── 1. Database connection ────────────────────────────────────────────
            DatabaseManager dbManager = DatabaseManager.getInstance();
            dbManager.connect();

            // ── 2. Repository (talks to DB) ───────────────────────────────────────
            DataRepository repository = new PostgreSQLDataRepository(dbManager);

            // ── 3. Shared logger ──────────────────────────────────────────────────
            ActionLogger logger = ActionLogger.getInstance();

            // ── 4. Services (business logic) ──────────────────────────────────────
            DisasterVictimService victimService = new DisasterVictimService(repository, logger);
            SupplyService supplyService = new SupplyService(repository, logger);
            InquiryService inquiryService = new InquiryService(repository, logger);
            RequirementService requirementService = new RequirementService(repository);
            SkillService skillService = new SkillService(repository, logger);

            // ── 5. Controller (coordinates everything) ────────────────────────────
            ReliefController controller = new ReliefController(
                    victimService,
                    supplyService,
                    inquiryService,
                    requirementService,
                    skillService
            );

            // ── 6. Load all data from DB + requirements file ──────────────────────
            controller.startApplication();

            // ── 7. Launch GUI on the Event Dispatch Thread ────────────────────────
            javax.swing.SwingUtilities.invokeLater(() -> {
                new MainFrame(controller);
            });
        } catch (Exception e) {
            logError(e);
            System.err.println("Fatal error: " + e.getMessage());
            System.err.println("The application cannot continue and will exit.");
            System.exit(1);
        }


    }
    private static void logError(Exception e) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("data/errorlog.txt", true))) {
            writer.write(String.format("[%s] FATAL ERROR: %s%n",
                    java.time.LocalDate.now(), e.getMessage()));
            for (StackTraceElement el : e.getStackTrace()) {
                writer.write("    at " + el.toString() + "%n");
            }
            writer.newLine();
        } catch (IOException ex) {
            System.err.println("Could not write to errorlog.txt: " + ex.getMessage());
        }
    }
}