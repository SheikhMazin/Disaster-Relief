package edu.ucalgary.oop;

/**
 * DisasterVictimService
 *
 * Service class responsible for managing disaster victim records within the
 * relief system. Handles loading, adding, updating, and deleting victims,
 * as well as managing their medical records, family connections, cultural
 * requirements, and skills. All changes are persisted through the
 * DataRepository and logged via ActionLogger.
 *
 * Soft-deleted victims are retained in the database but hidden from the UI.
 * Hard-deleted victims and all their associated data are permanently removed.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import java.util.ArrayList;

public class DisasterVictimService {

    private final DataRepository repository;
    private final ActionLogger logger;
    private ArrayList<DisasterVictim> victims;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs a DisasterVictimService backed by the given repository and logger.
     *
     * @param repository non-null DataRepository for persisting victim data
     * @param logger     non-null ActionLogger for logging victim changes
     * @throws IllegalArgumentException if repository or logger is null
     */
    public DisasterVictimService(DataRepository repository, ActionLogger logger) {
        if (repository == null) {
            throw new IllegalArgumentException("repository cannot be null.");
        }
        if (logger == null) {
            throw new IllegalArgumentException("logger cannot be null.");
        }
        this.repository = repository;
        this.logger     = logger;
        this.victims    = new ArrayList<>();
    }

    // =========================================================================
    //  Load
    // =========================================================================

    /**
     * Loads all disaster victim records from the database into memory.
     * Should be called once at application startup.
     *
     * @return list of all DisasterVictim objects loaded from the database
     */
    public ArrayList<DisasterVictim> loadVictims() {
        this.victims = repository.loadVictims();
        return this.victims;
    }

    // =========================================================================
    //  Add / Update
    // =========================================================================

    /**
     * Adds a new disaster victim to the system and persists them to the database.
     *
     * @param victim non-null DisasterVictim to add
     * @throws IllegalArgumentException if victim is null
     */
    public void addVictim(DisasterVictim victim) {
        if (victim == null) {
            throw new IllegalArgumentException("victim cannot be null.");
        }
        victims.add(victim);
        repository.saveVictim(victim);
        logger.logAdded("disaster victim",
                String.format("ID: %d | Name: %s %s",
                        victim.getVictimID(), victim.getFirstName(),
                        victim.getLastName() != null ? victim.getLastName() : ""));
    }

    /**
     * Updates an existing disaster victim record in the system and database.
     *
     * @param victim non-null DisasterVictim with updated values
     * @throws IllegalArgumentException if victim is null
     */
    public void updateVictim(DisasterVictim victim) {
        if (victim == null) {
            throw new IllegalArgumentException("victim cannot be null.");
        }
        repository.updateVictim(victim);
        logger.logUpdated("disaster victim",
                String.format("ID: %d | Name: %s %s",
                        victim.getVictimID(), victim.getFirstName(),
                        victim.getLastName() != null ? victim.getLastName() : ""));
    }

    // =========================================================================
    //  Delete
    // =========================================================================

    /**
     * Soft-deletes a disaster victim by marking them as deleted in both the
     * in-memory list and the database. The victim's data is retained but they
     * are hidden throughout the UI and excluded from skill searches.
     *
     * @param victimID the ID of the victim to soft-delete
     * @throws IllegalArgumentException if no victim with the given ID is found
     */
    public void softDeleteVictim(int victimID) {
        DisasterVictim victim = getVictimByID(victimID);
        if (victim == null) {
            throw new IllegalArgumentException("No victim found with ID: " + victimID);
        }
        victim.softDelete();
        repository.softDeleteVictim(victimID);
        logger.logSoftDeleted("disaster victim",
                String.format("ID: %d | Name: %s %s",
                        victim.getVictimID(), victim.getFirstName(),
                        victim.getLastName() != null ? victim.getLastName() : ""));
    }

    /**
     * Hard-deletes a disaster victim, permanently removing them and all their
     * associated data (medical records, family connections, skills, requirements,
     * inquiries) from both the in-memory list and the database.
     *
     * @param victimID the ID of the victim to hard-delete
     * @throws IllegalArgumentException if no victim with the given ID is found
     */
    public void hardDeleteVictim(int victimID) {
        DisasterVictim victim = getVictimByID(victimID);
        if (victim == null) {
            throw new IllegalArgumentException("No victim found with ID: " + victimID);
        }
        String description = String.format("ID: %d | Name: %s %s",
                victim.getVictimID(), victim.getFirstName(),
                victim.getLastName() != null ? victim.getLastName() : "");

        victims.remove(victim);
        repository.hardDeleteVictim(victimID);
        logger.logDeleted("disaster victim", description);
    }

    // =========================================================================
    //  Medical Records
    // =========================================================================

    /**
     * Adds a medical record to the specified victim's history and updates the
     * victim record in the database.
     *
     * @param victimID the ID of the victim to add the medical record to
     * @param record   non-null MedicalRecord to add
     * @throws IllegalArgumentException if no victim is found with the given ID,
     *                                  or if record is null
     */
    public void addMedicalRecord(int victimID, MedicalRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("record cannot be null.");
        }
        DisasterVictim victim = getVictimByID(victimID);
        if (victim == null) {
            throw new IllegalArgumentException("No victim found with ID: " + victimID);
        }
        victim.addMedicalRecord(record);
        repository.saveMedicalRecord(record, victimID);
        repository.updateVictim(victim);
        logger.logAdded("medical record",
                String.format("Victim ID: %d | Treatment: %s | Date: %s",
                        victimID, record.getTreatmentDetails(), record.getDateOfTreatment()));
    }

    /**
     * Loads and returns all location records from the database.
     * Used to populate location selection dropdowns in the UI.
     *
     * @return list of all Location objects
     */
    public ArrayList<Location> getLocations() {
        return repository.loadLocations();
    }


    // =========================================================================
    //  Family Connections
    // =========================================================================

    /**
     * Adds a family relationship to the specified victim's record and updates
     * the victim in the database. The relationship is also added to the second
     * victim's record to keep both sides in sync.
     *
     * @param victimID the ID of the first victim in the relationship
     * @param relation non-null FamilyRelation to add
     * @throws IllegalArgumentException if no victim is found with the given ID,
     *                                  or if relation is null
     */
    public void addFamilyConnection(int victimID, FamilyRelation relation) {
        if (relation == null) {
            throw new IllegalArgumentException("relation cannot be null.");
        }
        DisasterVictim victim = getVictimByID(victimID);
        if (victim == null) {
            throw new IllegalArgumentException("No victim found with ID: " + victimID);
        }
        victim.addFamilyConnection(relation);
        repository.saveFamilyConnection(relation);
        repository.updateVictim(victim);
        logger.logAdded("family connection",
                String.format("Victim ID: %d | Relationship: %s | Related to victim ID: %d",
                        victimID, relation.getRelationshipTo(),
                        relation.getPersonTwo().getVictimID()));
    }

    // =========================================================================
    //  Requirements
    // =========================================================================

    /**
     * Adds a cultural or religious requirement to the specified victim's record
     * and persists it to the database.
     *
     * @param victimID    the ID of the victim to add the requirement to
     * @param requirement non-null VictimRequirement to add
     * @throws IllegalArgumentException if no victim is found with the given ID,
     *                                  requirement is null, or the victim already
     *                                  has a requirement of this type
     */
    public void addRequirement(int victimID, VictimRequirement requirement) {
        if (requirement == null) {
            throw new IllegalArgumentException("requirement cannot be null.");
        }
        DisasterVictim victim = getVictimByID(victimID);
        if (victim == null) {
            throw new IllegalArgumentException("No victim found with ID: " + victimID);
        }
        victim.addRequirement(requirement);
        repository.saveRequirement(requirement);
        logger.logAdded("requirement",
                String.format("Victim ID: %d | Type: %s | Option: %s",
                        victimID, requirement.getRequirementType(), requirement.getSelectedOption()));
    }

    /**
     * Removes a cultural or religious requirement from the specified victim's
     * record and deletes it from the database.
     *
     * @param victimID        the ID of the victim to remove the requirement from
     * @param requirementType non-null, non-empty type of requirement to remove
     * @throws IllegalArgumentException if no victim is found with the given ID,
     *                                  or requirementType is null or blank
     */
    public void removeRequirement(int victimID, String requirementType) {
        if (requirementType == null || requirementType.trim().isEmpty()) {
            throw new IllegalArgumentException("requirementType cannot be null or empty.");
        }
        DisasterVictim victim = getVictimByID(victimID);
        if (victim == null) {
            throw new IllegalArgumentException("No victim found with ID: " + victimID);
        }
        victim.removeRequirement(requirementType);
        repository.deleteRequirement(victimID, requirementType);
        logger.logDeleted("requirement",
                String.format("Victim ID: %d | Type: %s", victimID, requirementType));
    }

    // =========================================================================
    //  Skills
    // =========================================================================

    /**
     * Adds a skill to the specified victim's profile and persists it to the
     * database.
     *
     * @param victimID the ID of the victim to add the skill to
     * @param skill    non-null Skill to register
     * @throws IllegalArgumentException if no victim is found with the given ID,
     *                                  skill is null, or the victim already has
     *                                  a skill of this type
     */
    public void addSkill(int victimID, Skill skill) {
        if (skill == null) {
            throw new IllegalArgumentException("skill cannot be null.");
        }
        DisasterVictim victim = getVictimByID(victimID);
        if (victim == null) {
            throw new IllegalArgumentException("No victim found with ID: " + victimID);
        }
        victim.addSkill(skill);
        repository.saveSkill(skill);
        logger.logAdded("skill",
                String.format("Victim ID: %d | Category: %s | Skill ID: %d",
                        victimID, skill.getCategory(), skill.getSkillID()));
    }

    /**
     * Removes a skill from the specified victim's profile and deletes it from
     * the database.
     *
     * @param victimID the ID of the victim to remove the skill from
     * @param skillID  the ID of the Skill to remove
     * @throws IllegalArgumentException if no victim is found with the given ID
     */
    public void removeSkill(int victimID, int skillID) {
        DisasterVictim victim = getVictimByID(victimID);
        if (victim == null) {
            throw new IllegalArgumentException("No victim found with ID: " + victimID);
        }
        victim.removeSkill(skillID);
        repository.deleteSkill(skillID);
        logger.logDeleted("skill",
                String.format("Victim ID: %d | Skill ID: %d", victimID, skillID));
    }

    // =========================================================================
    //  Query
    // =========================================================================

    /**
     * Finds and returns the disaster victim with the given ID from the
     * in-memory list.
     *
     * @param victimID the ID to search for
     * @return the matching DisasterVictim, or null if not found
     */
    public DisasterVictim getVictimByID(int victimID) {
        for (DisasterVictim victim : victims) {
            if (victim.getVictimID() == victimID) {
                return victim;
            }
        }
        return null;
    }

    /**
     * Returns all victims that have not been soft-deleted.
     * Used to populate UI views where deleted victims should be hidden.
     *
     * @return list of active (non-soft-deleted) DisasterVictim objects
     */
    public ArrayList<DisasterVictim> getActiveVictims() {
        ArrayList<DisasterVictim> active = new ArrayList<>();
        for (DisasterVictim victim : victims) {
            if (!victim.isSoftDeleted()) {
                active.add(victim);
            }
        }
        return active;
    }

    /**
     * Returns the full list of victims including soft-deleted ones.
     * Used internally and for admin-level operations.
     *
     * @return full list of all DisasterVictim objects
     */
    public ArrayList<DisasterVictim> getAllVictims() {
        return victims;
    }

    /**
     * Returns the next available victim ID from the database.
     *
     * @return next available integer ID
     */
    public int getNextVictimID() {
        return repository.getNextVictimID();
    }
}