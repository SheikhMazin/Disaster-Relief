package edu.ucalgary.oop;

/**
 * Skill
 *
 * Abstract base class representing a skill that a disaster victim can register.
 * All skill types share a skill ID, the associated victim ID, a category label
 * (set by each concrete subclass), and a proficiency level.
 * Concrete subclasses — MedicalSkill, LanguageSkill, and TradeSkill — add
 * category-specific validation and fields.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */
public abstract class Skill {

    private final int skillID;
    private final int victimID;
    private final String category;
    private String proficiencyLevel;

    /**
     * Constructs a Skill with the supplied identifiers, category, and proficiency.
     *
     * @param skillID         unique positive integer skill identifier
     * @param victimID        positive integer ID of the owning victim
     * @param category        non-null, non-empty category label (set by subclass)
     * @param proficiencyLevel non-null, non-empty level; expected values are
     *                        "beginner", "intermediate", or "advanced"
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Skill(int skillID, int victimID, String category, String proficiencyLevel) {
        if (skillID <= 0) {
            throw new IllegalArgumentException("skillID must be greater than 0.");
        }
        if (victimID <= 0) {
            throw new IllegalArgumentException("victimID must be greater than 0.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("category cannot be null or empty.");
        }
        if (proficiencyLevel == null || proficiencyLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("proficiencyLevel cannot be null or empty.");
        }

        this.skillID          = skillID;
        this.victimID         = victimID;
        this.category         = category;
        this.proficiencyLevel = proficiencyLevel;
    }

    // =========================================================================
    //  Getters
    // =========================================================================

    /**
     * Returns the unique identifier for this skill entry.
     * @return skillID
     */
    public int getSkillID() { return skillID; }

    /**
     * Returns the ID of the victim who owns this skill.
     * @return victimID
     */
    public int getVictimID() { return victimID; }

    /**
     * Returns the category label for this skill (e.g., "Medical", "Language", "Trade").
     * @return category
     */
    public String getCategory() { return category; }

    /**
     * Returns the proficiency level for this skill.
     * @return proficiencyLevel
     */
    public String getProficiencyLevel() { return proficiencyLevel; }

    // =========================================================================
    //  Setters
    // =========================================================================

    /**
     * Updates the proficiency level for this skill.
     *
     * @param proficiencyLevel non-null, non-empty proficiency string;
     *                         expected values are "beginner", "intermediate", "advanced"
     * @throws IllegalArgumentException if the value is null or blank
     */
    public void setProficiencyLevel(String proficiencyLevel) {
        if (proficiencyLevel == null || proficiencyLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("proficiencyLevel cannot be null or empty.");
        }
        this.proficiencyLevel = proficiencyLevel;
    }
}