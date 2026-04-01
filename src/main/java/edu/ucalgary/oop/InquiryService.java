package edu.ucalgary.oop;

/**
 * InquiryService
 *
 * Service class responsible for managing inquiries made by people searching
 * for missing disaster victims. Handles loading, adding, and updating inquiry
 * records. All changes are persisted through the DataRepository and logged
 * via ActionLogger.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import java.util.ArrayList;

public class InquiryService {

    private final DataRepository repository;
    private final ActionLogger logger;
    private ArrayList<ReliefService> inquiries;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs an InquiryService backed by the given repository and logger.
     *
     * @param repository non-null DataRepository for persisting inquiry data
     * @param logger     non-null ActionLogger for logging inquiry changes
     * @throws IllegalArgumentException if repository or logger is null
     */
    public InquiryService(DataRepository repository, ActionLogger logger) {
        if (repository == null) {
            throw new IllegalArgumentException("repository cannot be null.");
        }
        if (logger == null) {
            throw new IllegalArgumentException("logger cannot be null.");
        }
        this.repository = repository;
        this.logger     = logger;
        this.inquiries  = new ArrayList<>();
    }

    // =========================================================================
    //  Load
    // =========================================================================

    /**
     * Loads all inquiry records from the database into memory.
     * Should be called once at application startup.
     *
     * @return list of all ReliefService (inquiry) objects loaded from the database
     */
    public ArrayList<ReliefService> loadInquiries() {
        this.inquiries = repository.loadInquiries();
        return this.inquiries;
    }

    // =========================================================================
    //  Add / Update
    // =========================================================================

    /**
     * Adds a new inquiry to the system and persists it to the database.
     *
     * @param inquiry non-null ReliefService inquiry to add
     * @throws IllegalArgumentException if inquiry is null
     */
    public void addInquiry(ReliefService inquiry) {
        if (inquiry == null) {
            throw new IllegalArgumentException("inquiry cannot be null.");
        }
        inquiries.add(inquiry);
        repository.saveInquiry(inquiry);
        logger.logAdded("inquiry",
                String.format("ID: %d | Inquirer: %s %s | Missing person: %s %s",
                        inquiry.getInquiryID(),
                        inquiry.getInquirer().getFirstName(),
                        inquiry.getInquirer().getLastName(),
                        inquiry.getMissingPerson().getFirstName(),
                        inquiry.getMissingPerson().getLastName() != null
                                ? inquiry.getMissingPerson().getLastName() : ""));
    }

    /**
     * Updates an existing inquiry record in the system and database.
     *
     * @param inquiry non-null ReliefService inquiry with updated values
     * @throws IllegalArgumentException if inquiry is null
     */
    public void updateInquiry(ReliefService inquiry) {
        if (inquiry == null) {
            throw new IllegalArgumentException("inquiry cannot be null.");
        }
        repository.updateInquiry(inquiry);
        logger.logUpdated("inquiry",
                String.format("ID: %d | %s", inquiry.getInquiryID(), inquiry.getLogDetails()));
    }

    // =========================================================================
    //  Query
    // =========================================================================

    /**
     * Returns the full list of inquiries currently loaded in memory.
     *
     * @return list of all ReliefService inquiries
     */
    public ArrayList<ReliefService> getInquiries() {
        return inquiries;
    }

    /**
     * Searches for all inquiries related to a specific missing person by their
     * victim ID.
     *
     * @param victimID the ID of the missing person to search for
     * @return list of ReliefService inquiries for the given victim; empty if none
     */
    public ArrayList<ReliefService> getInquiriesForVictim(int victimID) {
        ArrayList<ReliefService> results = new ArrayList<>();
        for (ReliefService inquiry : inquiries) {
            if (inquiry.getMissingPerson().getVictimID() == victimID) {
                results.add(inquiry);
            }
        }
        return results;
    }
}