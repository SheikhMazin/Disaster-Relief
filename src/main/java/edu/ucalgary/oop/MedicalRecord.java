package edu.ucalgary.oop;

/**
 * MedicalRecord
 *
 * Represents a medical treatment record for a disaster victim.
 * Each record stores the location where treatment was administered,
 * a description of the treatment, and the date it occurred.
 * The date of treatment cannot be in the future.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import java.time.LocalDate;

public class MedicalRecord {

    private Location location;
    private String treatmentDetails;
    private LocalDate dateOfTreatment;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs a MedicalRecord with the given location, treatment details,
     * and date of treatment.
     *
     * @param location         non-null Location where treatment was administered
     * @param treatmentDetails non-null, non-empty description of the treatment
     * @param dateOfTreatment  non-null date of treatment; must not be in the future
     * @throws IllegalArgumentException if any argument is null, treatmentDetails
     *                                  is blank, or dateOfTreatment is in the future
     */
    public MedicalRecord(Location location, String treatmentDetails, LocalDate dateOfTreatment) {
        if (location == null) {
            throw new IllegalArgumentException("location cannot be null.");
        }
        if (treatmentDetails == null || treatmentDetails.trim().isEmpty()) {
            throw new IllegalArgumentException("treatmentDetails cannot be null or empty.");
        }
        if (dateOfTreatment == null) {
            throw new IllegalArgumentException("dateOfTreatment cannot be null.");
        }
        if (dateOfTreatment.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("dateOfTreatment cannot be in the future.");
        }

        this.location         = location;
        this.treatmentDetails = treatmentDetails;
        this.dateOfTreatment  = dateOfTreatment;
    }

    // =========================================================================
    //  Getters
    // =========================================================================

    /**
     * Returns the location where treatment was administered.
     * @return location
     */
    public Location getLocation() { return location; }

    /**
     * Returns the description of the treatment administered.
     * @return treatmentDetails
     */
    public String getTreatmentDetails() { return treatmentDetails; }

    /**
     * Returns the date on which treatment was administered.
     * @return dateOfTreatment
     */
    public LocalDate getDateOfTreatment() { return dateOfTreatment; }

    // =========================================================================
    //  Setters
    // =========================================================================

    /**
     * Updates the location where treatment was administered.
     *
     * @param location non-null Location object
     * @throws IllegalArgumentException if location is null
     */
    public void setLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("location cannot be null.");
        }
        this.location = location;
    }

    /**
     * Updates the treatment details description.
     *
     * @param treatmentDetails non-null, non-empty treatment description
     * @throws IllegalArgumentException if treatmentDetails is null or blank
     */
    public void setTreatmentDetails(String treatmentDetails) {
        if (treatmentDetails == null || treatmentDetails.trim().isEmpty()) {
            throw new IllegalArgumentException("treatmentDetails cannot be null or empty.");
        }
        this.treatmentDetails = treatmentDetails;
    }

    /**
     * Updates the date of treatment.
     *
     * @param dateOfTreatment non-null date; must not be in the future
     * @throws IllegalArgumentException if dateOfTreatment is null or in the future
     */
    public void setDateOfTreatment(LocalDate dateOfTreatment) {
        if (dateOfTreatment == null) {
            throw new IllegalArgumentException("dateOfTreatment cannot be null.");
        }
        if (dateOfTreatment.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("dateOfTreatment cannot be in the future.");
        }
        this.dateOfTreatment = dateOfTreatment;
    }
}