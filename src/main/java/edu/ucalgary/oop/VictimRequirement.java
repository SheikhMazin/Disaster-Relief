package edu.ucalgary.oop;

/**
 * VictimRequirement
 *
 * Represents a single cultural or religious requirement registered for a
 * disaster victim (Feature 7).  Requirements are loaded from the serialized
 * available_requirements.ser file at startup; only types and options listed
 * in that file are valid.
 *
 * Each victim may hold at most one option per requirement type — for example,
 * a victim can have either a halal or a vegetarian dietary requirement, not both.
 * This constraint is enforced by DisasterVictim.addRequirement().
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */
public class VictimRequirement {

    private final int victimID;
    private final String requirementType;
    private String selectedOption;

    /**
     * Constructs a VictimRequirement for a given victim, type, and chosen option.
     *
     * @param victimID        positive integer ID of the owning victim
     * @param requirementType non-null, non-empty type label (e.g., "dietary restrictions")
     * @param selectedOption  non-null, non-empty option value (e.g., "halal")
     * @throws IllegalArgumentException if any argument is invalid
     */
    public VictimRequirement(int victimID, String requirementType, String selectedOption) {
        if (victimID <= 0) {
            throw new IllegalArgumentException("victimID must be greater than 0.");
        }
        if (requirementType == null || requirementType.trim().isEmpty()) {
            throw new IllegalArgumentException("requirementType cannot be null or empty.");
        }
        if (selectedOption == null || selectedOption.trim().isEmpty()) {
            throw new IllegalArgumentException("selectedOption cannot be null or empty.");
        }

        this.victimID        = victimID;
        this.requirementType = requirementType.trim();
        this.selectedOption  = selectedOption.trim();
    }

    // =========================================================================
    //  Getters
    // =========================================================================

    /**
     * Returns the ID of the victim this requirement belongs to.
     * @return victimID
     */
    public int getVictimID() { return victimID; }

    /**
     * Returns the type of requirement (e.g., "dietary restrictions").
     * @return requirementType
     */
    public String getRequirementType() { return requirementType; }

    /**
     * Returns the currently selected option for this requirement (e.g., "halal").
     * @return selectedOption
     */
    public String getSelectedOption() { return selectedOption; }

    // =========================================================================
    //  Setters
    // =========================================================================

    /**
     * Updates the selected option for this requirement.
     * The new option should be validated against the available options loaded from
     * available_requirements.ser before calling this method.
     *
     * @param selectedOption non-null, non-empty option value
     * @throws IllegalArgumentException if value is null or blank
     */
    public void setSelectedOption(String selectedOption) {
        if (selectedOption == null || selectedOption.trim().isEmpty()) {
            throw new IllegalArgumentException("selectedOption cannot be null or empty.");
        }
        this.selectedOption = selectedOption.trim();
    }
}