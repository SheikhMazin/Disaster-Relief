package edu.ucalgary.oop;

/**
 * MedicalSkill
 *
 * Represents a medical skill registered by a disaster victim.
 * In addition to the base Skill fields, a medical skill tracks a certification
 * type (e.g., firstaid, counseling, nursing, doctor) and the date on which that
 * certification expires.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import java.time.LocalDate;

public class MedicalSkill extends Skill {

    /** Valid certification type options as specified by the feature requirements. */
    public static final String[] VALID_CERTIFICATION_TYPES =
            { "firstaid", "counseling", "nursing", "doctor" };

    private String certificationType;
    private LocalDate certificationExpiryDate;

    /**
     * Constructs a MedicalSkill with the given identifiers, proficiency,
     * certification type, and expiry date.
     *
     * @param skillID                 unique positive integer skill identifier
     * @param victimID                positive integer ID of the owning victim
     * @param proficiencyLevel        non-null, non-empty proficiency level
     * @param certificationType       non-null, non-empty certification type;
     *                                must be one of: firstaid, counseling,
     *                                nursing, doctor
     * @param certificationExpiryDate non-null expiry date for the certification
     * @throws IllegalArgumentException if certificationType is null, empty, or
     *                                  not one of the valid options; or if
     *                                  certificationExpiryDate is null
     */
    public MedicalSkill(int skillID, int victimID, String proficiencyLevel,
                        String certificationType, LocalDate certificationExpiryDate) {
        super(skillID, victimID, "Medical", proficiencyLevel);

        if (certificationType == null || certificationType.trim().isEmpty()) {
            throw new IllegalArgumentException("certificationType cannot be null or empty.");
        }
        if (!isValidCertificationType(certificationType)) {
            throw new IllegalArgumentException(
                    "Invalid certificationType. Must be one of: firstaid, counseling, nursing, doctor.");
        }
        if (certificationExpiryDate == null) {
            throw new IllegalArgumentException("certificationExpiryDate cannot be null.");
        }

        this.certificationType       = certificationType.trim().toLowerCase();
        this.certificationExpiryDate = certificationExpiryDate;
    }

    // =========================================================================
    //  Getters
    // =========================================================================

    /**
     * Returns the type of medical certification (e.g., "firstaid", "nursing").
     * @return certificationType
     */
    public String getCertificationType() { return certificationType; }

    /**
     * Returns the expiry date of the medical certification.
     * @return certificationExpiryDate
     */
    public LocalDate getCertificationExpiryDate() { return certificationExpiryDate; }

    // =========================================================================
    //  Setters
    // =========================================================================

    /**
     * Updates the certification type.
     *
     * @param certificationType non-null, non-empty type; must be one of:
     *                          firstaid, counseling, nursing, doctor
     * @throws IllegalArgumentException if value is null, empty, or invalid
     */
    public void setCertificationType(String certificationType) {
        if (certificationType == null || certificationType.trim().isEmpty()) {
            throw new IllegalArgumentException("certificationType cannot be null or empty.");
        }
        if (!isValidCertificationType(certificationType)) {
            throw new IllegalArgumentException(
                    "Invalid certificationType. Must be one of: firstaid, counseling, nursing, doctor.");
        }
        this.certificationType = certificationType.trim().toLowerCase();
    }

    /**
     * Updates the certification expiry date.
     *
     * @param certificationExpiryDate non-null expiry date
     * @throws IllegalArgumentException if value is null
     */
    public void setCertificationExpiryDate(LocalDate certificationExpiryDate) {
        if (certificationExpiryDate == null) {
            throw new IllegalArgumentException("certificationExpiryDate cannot be null.");
        }
        this.certificationExpiryDate = certificationExpiryDate;
    }

    // =========================================================================
    //  Helpers
    // =========================================================================

    /**
     * Checks whether the supplied certification type matches one of the allowed values.
     *
     * @param type the type string to validate
     * @return true if the type is a known valid certification type
     */
    private boolean isValidCertificationType(String type) {
        String lower = type.trim().toLowerCase();
        for (String valid : VALID_CERTIFICATION_TYPES) {
            if (valid.equals(lower)) {
                return true;
            }
        }
        return false;
    }
}