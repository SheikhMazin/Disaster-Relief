package edu.ucalgary.oop;

/**
 * DataRepository
 *
 * Interface defining all persistence operations required by the application.
 * Concrete implementations (e.g., {@link PostgreSQLDataRepository}) provide
 * the actual database logic, while a mock implementation can be injected for
 * testing. This design follows the Dependency Inversion Principle so that
 * business-logic classes depend on this abstraction rather than a concrete
 * database class.
 *
 * Methods are grouped by domain entity:
 *   - Disaster Victims
 *   - Locations
 *   - Supplies
 *   - Inquiries and Inquirers
 *   - Cultural / Religious Requirements
 *   - Skills
 *   - Medical Records
 *   - Family Connections
 *
 * @author Sheikh Muhammad Mazin
 * @version 2.0
 * @since 2026-01-01
 */

import java.util.ArrayList;

public interface DataRepository {

    // =========================================================================
    //  Disaster Victims
    // =========================================================================

    /**
     * Loads all non-deleted and soft-deleted disaster victim records from the
     * data store, including their associated medical records, family
     * connections, requirements, and skills.
     *
     * @return list of all {@link DisasterVictim} objects; empty list if none found
     */
    ArrayList<DisasterVictim> loadVictims();

    /**
     * Persists a new disaster victim to the data store.
     * The victim's associated data (medical records, skills, etc.) are saved
     * separately via their respective save methods.
     *
     * @param victim the non-null {@link DisasterVictim} to save
     */
    void saveVictim(DisasterVictim victim);

    /**
     * Updates an existing disaster victim's core fields (name, age, gender,
     * comments, soft-delete status) in the data store.
     *
     * @param victim the non-null {@link DisasterVictim} with updated values
     */
    void updateVictim(DisasterVictim victim);

    /**
     * Marks a disaster victim as soft-deleted. The victim is hidden from the
     * UI but their record and all associated data remain in the database.
     *
     * @param victimID the ID of the victim to soft-delete
     */
    void softDeleteVictim(int victimID);

    /**
     * Permanently removes a disaster victim and all their associated data
     * (medical records, family relationships, skills, requirements, inquiries)
     * from the data store. This action cannot be undone.
     *
     * @param victimID the ID of the victim to hard-delete
     */
    void hardDeleteVictim(int victimID);

    /**
     * Returns the next available integer ID for a new disaster victim record.
     * Computed as MAX(id) + 1 from the Person table.
     *
     * @return next available victim ID
     */
    int getNextVictimID();

    // =========================================================================
    //  Locations
    // =========================================================================

    /**
     * Loads all location records from the data store.
     *
     * @return list of all {@link Location} objects; empty list if none found
     */
    ArrayList<Location> loadLocations();

    // =========================================================================
    //  Supplies
    // =========================================================================

    /**
     * Loads all supply records from the data store, including perishability
     * and victim allocation status.
     *
     * @return list of all {@link Supply} objects; empty list if none found
     */
    ArrayList<Supply> loadSupplies();

    /**
     * Persists a new supply record to the data store.
     *
     * @param supply the non-null {@link Supply} to save
     */
    void saveSupply(Supply supply);

    /**
     * Updates an existing supply record in the data store, including its
     * type, expiry date, and allocated victim.
     *
     * @param supply the non-null {@link Supply} with updated values
     */
    void updateSupply(Supply supply);

    /**
     * Returns the next available integer ID for a new supply record.
     * Computed as MAX(id) + 1 from the Supply table.
     *
     * @return next available supply ID
     */
    int getNextSupplyID();

    // =========================================================================
    //  Inquiries and Inquirers
    // =========================================================================

    /**
     * Loads all inquiry records from the data store, including the associated
     * inquirer and missing person details.
     *
     * @return list of all {@link ReliefService} (inquiry) objects; empty list if none found
     */
    ArrayList<ReliefService> loadInquiries();

    /**
     * Persists a new inquiry record to the data store.
     *
     * @param inquiry the non-null {@link ReliefService} inquiry to save
     */
    void saveInquiry(ReliefService inquiry);

    /**
     * Updates an existing inquiry record in the data store.
     *
     * @param inquiry the non-null {@link ReliefService} with updated values
     */
    void updateInquiry(ReliefService inquiry);

    /**
     * Persists a new inquirer to the Person table and returns the
     * database-generated ID for the new record.
     *
     * @param inquirer the non-null {@link Inquirer} to save
     * @return the generated integer ID assigned to the new inquirer
     */
    int saveInquirer(Inquirer inquirer);

    /**
     * Returns the next available integer ID for a new inquiry record.
     * Computed as MAX(id) + 1 from the Inquiry table.
     *
     * @return next available inquiry ID
     */
    int getNextInquiryID();

    // =========================================================================
    //  Cultural / Religious Requirements
    // =========================================================================

    /**
     * Persists a cultural or religious requirement for a victim to the data
     * store. Each victim may hold at most one option per requirement type;
     * this constraint is enforced at the service layer before calling this
     * method.
     *
     * @param requirement the non-null {@link VictimRequirement} to save
     */
    void saveRequirement(VictimRequirement requirement);

    /**
     * Removes a specific requirement type from a victim's record in the data
     * store.
     *
     * @param victimID        the ID of the victim
     * @param requirementType the category of requirement to remove
     *                        (e.g., "dietary restrictions")
     */
    void deleteRequirement(int victimID, String requirementType);

    // =========================================================================
    //  Skills
    // =========================================================================

    /**
     * Persists a skill entry to the data store. First ensures the skill exists
     * in the Skill catalogue table, then creates a VictimSkill record linking
     * the victim to the skill with proficiency and type-specific details.
     *
     * @param skill the non-null {@link Skill} (MedicalSkill, LanguageSkill,
     *              or TradeSkill) to save
     */
    void saveSkill(Skill skill);

    /**
     * Removes a skill from the data store by its VictimSkill record ID.
     *
     * @param skillID the ID of the VictimSkill entry to delete
     */
    void deleteSkill(int skillID);

    /**
     * Returns the next available integer ID for a new VictimSkill record.
     * Computed as MAX(id) + 1 from the VictimSkill table.
     *
     * @return next available skill ID
     */
    int getNextSkillID();

    // =========================================================================
    //  Medical Records
    // =========================================================================

    /**
     * Persists a new medical record for a victim to the data store.
     *
     * @param record   the non-null {@link MedicalRecord} to save
     * @param victimID the ID of the victim this record belongs to
     */
    void saveMedicalRecord(MedicalRecord record, int victimID);

    // =========================================================================
    //  Family Connections
    // =========================================================================

    /**
     * Inserts a family relationship record into the FamilyRelationship table,
     * linking two disaster victims with a named relationship type.
     *
     * @param relation the non-null {@link FamilyRelation} to persist
     */
    void saveFamilyConnection(FamilyRelation relation);
}