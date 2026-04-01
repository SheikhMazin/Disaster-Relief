package edu.ucalgary.oop;

/**
 * SkillService
 *
 * Service class responsible for managing skill registrations for disaster
 * victims. Handles adding and removing skills from a victim's profile,
 * searching for victims by skill category, and retrieving all skills
 * belonging to a specific victim. All skill changes are persisted through
 * the DataRepository and logged via ActionLogger.
 *
 * Soft-deleted victims are excluded from skill search results per Feature 8.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import java.util.ArrayList;

public class SkillService {

    private final DataRepository repository;
    private final ActionLogger logger;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs a SkillService backed by the given repository and logger.
     *
     * @param repository non-null DataRepository for persisting skill data
     * @param logger     non-null ActionLogger for logging skill changes
     * @throws IllegalArgumentException if repository or logger is null
     */
    public SkillService(DataRepository repository, ActionLogger logger) {
        if (repository == null) {
            throw new IllegalArgumentException("repository cannot be null.");
        }
        if (logger == null) {
            throw new IllegalArgumentException("logger cannot be null.");
        }
        this.repository = repository;
        this.logger     = logger;
    }

    // =========================================================================
    //  Add / Remove
    // =========================================================================

    /**
     * Adds a skill to the specified victim's profile and persists it to the
     * database. The skill must not duplicate an existing skill type already
     * registered for that victim — this is enforced by
     * {@link DisasterVictim#addSkill(Skill)}.
     *
     * @param victimID the ID of the victim to add the skill to
     * @param victim   non-null DisasterVictim who owns the skill
     * @param skill    non-null Skill to register
     * @throws IllegalArgumentException if victim or skill is null, or if the
     *                                  victim already has a skill of this type
     */
    public void addSkill(DisasterVictim victim, Skill skill) {
        if (victim == null) {
            throw new IllegalArgumentException("victim cannot be null.");
        }
        if (skill == null) {
            throw new IllegalArgumentException("skill cannot be null.");
        }

        victim.addSkill(skill);
        repository.saveSkill(skill);
        logger.logAdded("skill",
                String.format("Victim %d (%s) — %s skill added (ID: %d)",
                        victim.getVictimID(), victim.getFirstName(),
                        skill.getCategory(), skill.getSkillID()));
    }

    /**
     * Removes a skill from the specified victim's profile and deletes it from
     * the database.
     *
     * @param victim  non-null DisasterVictim who owns the skill
     * @param skillID the ID of the Skill to remove
     * @throws IllegalArgumentException if victim is null
     */
    public void removeSkill(DisasterVictim victim, int skillID) {
        if (victim == null) {
            throw new IllegalArgumentException("victim cannot be null.");
        }

        victim.removeSkill(skillID);
        repository.deleteSkill(skillID);
        logger.logDeleted("skill",
                String.format("Victim %d (%s) — skill ID %d removed",
                        victim.getVictimID(), victim.getFirstName(), skillID));
    }

    // =========================================================================
    //  Search / Query
    // =========================================================================

    /**
     * Searches all loaded victims for those who have at least one skill in the
     * given category. Soft-deleted victims are excluded from results.
     *
     * @param victims  the full list of DisasterVictim objects to search through
     * @param category non-null, non-empty skill category to search for
     *                 (e.g., "Medical", "Language", "Trade")
     * @return list of Skills matching the given category from non-deleted victims;
     *         empty list if none found
     * @throws IllegalArgumentException if category is null or blank
     */
    public ArrayList<Skill> searchByCategory(ArrayList<DisasterVictim> victims, String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("category cannot be null or empty.");
        }

        ArrayList<Skill> results = new ArrayList<>();

        for (DisasterVictim victim : victims) {
            if (victim.isSoftDeleted()) {
                continue;
            }
            for (Skill skill : victim.getSkills()) {
                if (skill.getCategory().equalsIgnoreCase(category.trim())) {
                    results.add(skill);
                }
            }
        }

        return results;
    }

    /**
     * Returns all skills registered to the specified victim.
     *
     * @param victim non-null DisasterVictim whose skills to retrieve
     * @return list of Skills belonging to the victim; empty list if none
     * @throws IllegalArgumentException if victim is null
     */
    public ArrayList<Skill> getVictimSkills(DisasterVictim victim) {
        if (victim == null) {
            throw new IllegalArgumentException("victim cannot be null.");
        }
        return victim.getSkills();
    }
}