package edu.ucalgary.oop;

/**
 * DataRepository
 *
 * Interface defining all persistence operations required by the application.
 * Concrete implementations (e.g., PostgreSQLDataRepository) provide the actual
 * database logic, while a mock implementation can be injected for testing.
 * This design follows the Dependency Inversion Principle so that business-logic
 * classes depend on this abstraction rather than a concrete database class.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import java.util.ArrayList;

public interface DataRepository {

    // ── Victim ────────────────────────────────────────────────────────────────

    /**
     * Loads all disaster victim records from the data store.
     * @return list of all DisasterVictim objects
     */
    ArrayList<DisasterVictim> loadVictims();

    /**
     * Loads all location records from the data store.
     * @return list of all Location objects
     */
    ArrayList<Location> loadLocations();

    /**
     * Loads all supply records from the data store.
     * @return list of all Supply objects
     */
    ArrayList<Supply> loadSupplies();

    /**
     * Loads all inquiry records from the data store.
     * @return list of all ReliefService (inquiry) objects
     */
    ArrayList<ReliefService> loadInquiries();

    /**
     * Persists a new disaster victim to the data store.
     * @param victim the DisasterVictim to save
     */
    void saveVictim(DisasterVictim victim);

    /**
     * Updates an existing disaster victim record in the data store.
     * @param victim the DisasterVictim with updated values
     */
    void updateVictim(DisasterVictim victim);

    /**
     * Marks a disaster victim as soft-deleted so they are hidden from the UI
     * but remain in the database.
     * @param victimID the ID of the victim to soft-delete
     */
    void softDeleteVictim(int victimID);

    /**
     * Permanently removes a disaster victim and all their associated data
     * (medical records, relationships, skills, inquiries) from the data store.
     * @param victimID the ID of the victim to hard-delete
     */
    void hardDeleteVictim(int victimID);

    // ── Supply ────────────────────────────────────────────────────────────────

    /**
     * Persists a new supply record to the data store.
     * @param supply the Supply to save
     */
    void saveSupply(Supply supply);

    /**
     * Updates an existing supply record in the data store.
     * @param supply the Supply with updated values
     */
    void updateSupply(Supply supply);

    // ── Inquiry ───────────────────────────────────────────────────────────────

    /**
     * Persists a new inquiry to the data store.
     * @param inquiry the ReliefService (inquiry) to save
     */
    void saveInquiry(ReliefService inquiry);

    /**
     * Updates an existing inquiry record in the data store.
     * @param inquiry the ReliefService with updated values
     */
    void updateInquiry(ReliefService inquiry);

    // ── Cultural/Religious Requirements ───────────────────────────────────────

    /**
     * Persists a cultural/religious requirement for a victim to the data store.
     * @param requirement the VictimRequirement to save
     */
    void saveRequirement(VictimRequirement requirement);

    /**
     * Removes a specific requirement type from a victim's record in the data store.
     * @param victimID        the ID of the victim
     * @param requirementType the type of requirement to remove
     */
    void deleteRequirement(int victimID, String requirementType);

    // ── Skills ────────────────────────────────────────────────────────────────

    /**
     * Persists a skill entry to the data store.
     * @param skill the Skill to save
     */
    void saveSkill(Skill skill);

    /**
     * Removes a skill from the data store by its ID.
     * @param skillID the ID of the skill to delete
     */
    void deleteSkill(int skillID);

    /**
     * Saves a new medical record to the data
     * @param record the medical record wanting to be stored
     * @param victimID the ID of the person that the medical record that will be stored
     */
    void saveMedicalRecord(MedicalRecord record, int victimID);

    /**
     * Inserts a family relationship record into the FamilyRelationship table.
     * @param relation the FamilyRelation to persist
     * @throws RuntimeException wrapping any SQLException
     */
    void saveFamilyConnection(FamilyRelation relation);
}