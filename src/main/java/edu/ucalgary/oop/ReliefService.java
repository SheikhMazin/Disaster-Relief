package edu.ucalgary.oop;

/**
 * ReliefService
 *
 * Represents an inquiry made by a person searching for a missing disaster
 * victim. Each inquiry records the inquirer, the missing person being searched
 * for, the date the inquiry was made, any information provided by the inquirer,
 * and the last known location of the missing person.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import java.time.LocalDate;

public class ReliefService {

    private final int inquiryID;
    private Inquirer inquirer;
    private DisasterVictim missingPerson;
    private LocalDate dateOfInquiry;
    private String infoProvided;
    private Location lastKnownLocation;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs a ReliefService inquiry with the given details.
     *
     * @param inquiryID          unique positive integer identifier for this inquiry
     * @param inquirer           non-null Inquirer who made this inquiry
     * @param missingPerson      non-null DisasterVictim being searched for
     * @param inquiryDate        non-null date the inquiry was made; must not be
     *                           in the future
     * @param infoProvided       non-null, non-empty information provided by the
     *                           inquirer about the missing person
     * @param lastKnownLocation  the last known location of the missing person;
     *                           may be null if unknown
     * @throws IllegalArgumentException if inquiryID is not positive, inquirer
     *                                  or missingPerson is null, inquiryDate is
     *                                  null or in the future, or infoProvided
     *                                  is null or blank
     */
    public ReliefService(int inquiryID, Inquirer inquirer, DisasterVictim missingPerson,
                         LocalDate inquiryDate, String infoProvided, Location lastKnownLocation) {
        if (inquiryID <= 0) {
            throw new IllegalArgumentException("inquiryID must be greater than 0.");
        }
        if (inquirer == null) {
            throw new IllegalArgumentException("inquirer cannot be null.");
        }
        if (missingPerson == null) {
            throw new IllegalArgumentException("missingPerson cannot be null.");
        }
        if (inquiryDate == null) {
            throw new IllegalArgumentException("inquiryDate cannot be null.");
        }
        if (inquiryDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("inquiryDate cannot be in the future.");
        }
        if (infoProvided == null || infoProvided.trim().isEmpty()) {
            throw new IllegalArgumentException("infoProvided cannot be null or empty.");
        }

        this.inquiryID          = inquiryID;
        this.inquirer           = inquirer;
        this.missingPerson      = missingPerson;
        this.dateOfInquiry      = inquiryDate;
        this.infoProvided       = infoProvided;
        this.lastKnownLocation  = lastKnownLocation;
    }

    // =========================================================================
    //  Getters
    // =========================================================================

    /**
     * Returns the unique identifier for this inquiry.
     * @return inquiryID
     */
    public int getInquiryID() { return inquiryID; }

    /**
     * Returns the inquirer who made this inquiry.
     * @return inquirer
     */
    public Inquirer getInquirer() { return inquirer; }

    /**
     * Returns the disaster victim being searched for.
     * @return missingPerson
     */
    public DisasterVictim getMissingPerson() { return missingPerson; }

    /**
     * Returns the date this inquiry was made.
     * @return dateOfInquiry
     */
    public LocalDate getDateOfInquiry() { return dateOfInquiry; }

    /**
     * Returns the information provided by the inquirer about the missing person.
     * @return infoProvided
     */
    public String getInfoProvided() { return infoProvided; }

    /**
     * Returns the last known location of the missing person, or null if unknown.
     * @return lastKnownLocation, may be null
     */
    public Location getLastKnownLocation() { return lastKnownLocation; }

    // =========================================================================
    //  Setters
    // =========================================================================

    /**
     * Updates the inquirer associated with this inquiry.
     *
     * @param inquirer non-null Inquirer
     * @throws IllegalArgumentException if inquirer is null
     */
    public void setInquirer(Inquirer inquirer) {
        if (inquirer == null) {
            throw new IllegalArgumentException("inquirer cannot be null.");
        }
        this.inquirer = inquirer;
    }

    /**
     * Updates the missing person associated with this inquiry.
     *
     * @param missingPerson non-null DisasterVictim
     * @throws IllegalArgumentException if missingPerson is null
     */
    public void setMissingPerson(DisasterVictim missingPerson) {
        if (missingPerson == null) {
            throw new IllegalArgumentException("missingPerson cannot be null.");
        }
        this.missingPerson = missingPerson;
    }

    /**
     * Updates the date this inquiry was made.
     *
     * @param inquiryDate non-null date; must not be in the future
     * @throws IllegalArgumentException if inquiryDate is null or in the future
     */
    public void setDateOfInquiry(LocalDate inquiryDate) {
        if (inquiryDate == null) {
            throw new IllegalArgumentException("inquiryDate cannot be null.");
        }
        if (inquiryDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("inquiryDate cannot be in the future.");
        }
        this.dateOfInquiry = inquiryDate;
    }

    /**
     * Updates the information provided by the inquirer.
     *
     * @param infoProvided non-null, non-empty information string
     * @throws IllegalArgumentException if infoProvided is null or blank
     */
    public void setInfoProvided(String infoProvided) {
        if (infoProvided == null || infoProvided.trim().isEmpty()) {
            throw new IllegalArgumentException("infoProvided cannot be null or empty.");
        }
        this.infoProvided = infoProvided;
    }

    /**
     * Updates the last known location of the missing person.
     * May be set to null if the location is unknown.
     *
     * @param lastKnownLocation Location of the missing person, or null if unknown
     */
    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    // =========================================================================
    //  Utility
    // =========================================================================

    /**
     * Returns a formatted string summarising this inquiry for logging purposes.
     * Includes the inquiry ID, inquirer name, missing person name, date, and
     * last known location if available.
     *
     * @return formatted log string for this inquiry
     */
    public String getLogDetails() {
        String location = (lastKnownLocation != null)
                ? lastKnownLocation.getName()
                : "Unknown";

        return String.format(
                "Inquiry #%d | Inquirer: %s %s | Missing: %s %s | Date: %s | Last known location: %s | Info: %s",
                inquiryID,
                inquirer.getFirstName(), inquirer.getLastName(),
                missingPerson.getFirstName(),
                missingPerson.getLastName() != null ? missingPerson.getLastName() : "",
                dateOfInquiry,
                location,
                infoProvided
        );
    }
}