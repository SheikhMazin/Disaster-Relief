package edu.ucalgary.oop;

/**
 * TradeSkill
 *
 * Represents a trade skill registered by a disaster victim.
 * In addition to the base Skill fields, a trade skill stores the specific
 * trade type.  Valid trade types are fixed by the feature requirements:
 * carpentry, plumbing, and electricity.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */
public class TradeSkill extends Skill {

    /** Valid trade type options as specified by the feature requirements. */
    public static final String[] VALID_TRADE_TYPES = { "carpentry", "plumbing", "electricity" };

    private String tradeType;

    /**
     * Constructs a TradeSkill with the given identifiers, proficiency, and trade type.
     *
     * @param skillID        unique positive integer skill identifier
     * @param victimID       positive integer ID of the owning victim
     * @param proficiencyLevel non-null, non-empty proficiency level
     * @param tradeType      non-null, non-empty trade type; must be one of:
     *                       carpentry, plumbing, electricity
     * @throws IllegalArgumentException if tradeType is null, empty, or not a
     *                                  valid option
     */
    public TradeSkill(int skillID, int victimID, String proficiencyLevel, String tradeType) {
        super(skillID, victimID, "Trade", proficiencyLevel);

        if (tradeType == null || tradeType.trim().isEmpty()) {
            throw new IllegalArgumentException("tradeType cannot be null or empty.");
        }
        if (!isValidTradeType(tradeType)) {
            throw new IllegalArgumentException(
                    "Invalid tradeType. Must be one of: carpentry, plumbing, electricity.");
        }

        this.tradeType = tradeType.trim().toLowerCase();
    }

    // =========================================================================
    //  Getters
    // =========================================================================

    /**
     * Returns the trade type for this skill (e.g., "carpentry").
     * @return tradeType
     */
    public String getTradeType() { return tradeType; }

    // =========================================================================
    //  Setters
    // =========================================================================

    /**
     * Updates the trade type for this skill.
     *
     * @param tradeType non-null, non-empty type; must be one of:
     *                  carpentry, plumbing, electricity
     * @throws IllegalArgumentException if value is null, empty, or invalid
     */
    public void setTradeType(String tradeType) {
        if (tradeType == null || tradeType.trim().isEmpty()) {
            throw new IllegalArgumentException("tradeType cannot be null or empty.");
        }
        if (!isValidTradeType(tradeType)) {
            throw new IllegalArgumentException(
                    "Invalid tradeType. Must be one of: carpentry, plumbing, electricity.");
        }
        this.tradeType = tradeType.trim().toLowerCase();
    }

    // =========================================================================
    //  Helpers
    // =========================================================================

    /**
     * Checks whether the supplied trade type matches one of the allowed values.
     *
     * @param type the type string to validate (case-insensitive)
     * @return true if the type is a known valid trade type
     */
    private boolean isValidTradeType(String type) {
        String lower = type.trim().toLowerCase();
        for (String valid : VALID_TRADE_TYPES) {
            if (valid.equals(lower)) {
                return true;
            }
        }
        return false;
    }
}