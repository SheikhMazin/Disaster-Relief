package edu.ucalgary.oop;

/**
 * ReliefController
 *
 * MVC Controller class that acts as the central coordinator between the
 * user interface (MainFrame) and the application's service layer. All user
 * interactions from the UI are routed through this controller, which
 * delegates to the appropriate service class and returns results back to
 * the view. This keeps business logic out of the UI and the UI out of the
 * service classes.
 *
 * The controller is also responsible for application startup — loading all
 * data from the database and initialising the available requirements from
 * the serialized options file.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class ReliefController {

    private final DisasterVictimService victimService;
    private final SupplyService supplyService;
    private final InquiryService inquiryService;
    private final RequirementService requirementService;
    private final SkillService skillService;

    private static final String REQUIREMENTS_FILE =
            "src/main/resources/available_requirements.ser";

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs a ReliefController with all required service dependencies.
     *
     * @param victimService      non-null DisasterVictimService
     * @param supplyService      non-null SupplyService
     * @param inquiryService     non-null InquiryService
     * @param requirementService non-null RequirementService
     * @param skillService       non-null SkillService
     * @throws IllegalArgumentException if any service argument is null
     */
    public ReliefController(DisasterVictimService victimService,
                            SupplyService supplyService,
                            InquiryService inquiryService,
                            RequirementService requirementService,
                            SkillService skillService) {
        if (victimService == null)      throw new IllegalArgumentException("victimService cannot be null.");
        if (supplyService == null)      throw new IllegalArgumentException("supplyService cannot be null.");
        if (inquiryService == null)     throw new IllegalArgumentException("inquiryService cannot be null.");
        if (requirementService == null) throw new IllegalArgumentException("requirementService cannot be null.");
        if (skillService == null)       throw new IllegalArgumentException("skillService cannot be null.");

        this.victimService      = victimService;
        this.supplyService      = supplyService;
        this.inquiryService     = inquiryService;
        this.requirementService = requirementService;
        this.skillService       = skillService;
    }

    // =========================================================================
    //  Startup
    // =========================================================================

    /**
     * Initialises the application by loading all data from the database and
     * reading the available requirements from the serialized options file.
     * If the requirements file cannot be read the application prints an error
     * and exits, as per Feature 7 requirements.
     */
    public void startApplication() {
        victimService.loadVictims();
        supplyService.loadSupplies();
        inquiryService.loadInquiries();

        try {
            requirementService.loadAvailableRequirements(REQUIREMENTS_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Fatal: could not load requirements file. " + e.getMessage());
            System.exit(1);
        }
    }

    // =========================================================================
    //  Victim management
    // =========================================================================

    /**
     * Returns all active (non-soft-deleted) disaster victims.
     *
     * @return list of active DisasterVictim objects
     */
    public ArrayList<DisasterVictim> getActiveVictims() {
        return victimService.getActiveVictims();
    }

    /**
     * Returns the disaster victim with the given ID, or null if not found.
     *
     * @param victimID the ID to look up
     * @return matching DisasterVictim, or null
     */
    public DisasterVictim getVictimByID(int victimID) {
        return victimService.getVictimByID(victimID);
    }

    /**
     * Adds a new disaster victim to the system.
     *
     * @param victim non-null DisasterVictim to add
     * @throws IllegalArgumentException if victim is null
     */
    public void addVictim(DisasterVictim victim) {
        victimService.addVictim(victim);
    }

    /**
     * Updates an existing disaster victim record.
     *
     * @param victim non-null DisasterVictim with updated values
     * @throws IllegalArgumentException if victim is null
     */
    public void updateVictim(DisasterVictim victim) {
        victimService.updateVictim(victim);
    }

    /**
     * Soft-deletes a disaster victim — hides them from the UI while retaining
     * their data in the database.
     *
     * @param victimID the ID of the victim to soft-delete
     * @throws IllegalArgumentException if no victim is found with the given ID
     */
    public void softDeleteVictim(int victimID) {
        victimService.softDeleteVictim(victimID);
    }

    /**
     * Hard-deletes a disaster victim and all their associated data permanently.
     *
     * @param victimID the ID of the victim to hard-delete
     * @throws IllegalArgumentException if no victim is found with the given ID
     */
    public void hardDeleteVictim(int victimID) {
        victimService.hardDeleteVictim(victimID);
    }

    /**
     * Adds a medical record to the specified victim.
     *
     * @param victimID the ID of the victim
     * @param record   non-null MedicalRecord to add
     * @throws IllegalArgumentException if victim not found or record is null
     */
    public void addMedicalRecord(int victimID, MedicalRecord record) {
        victimService.addMedicalRecord(victimID, record);
    }

    /**
     * Adds a family relationship to the specified victim.
     *
     * @param victimID the ID of the victim
     * @param relation non-null FamilyRelation to add
     * @throws IllegalArgumentException if victim not found or relation is null
     */
    public void addFamilyConnection(int victimID, FamilyRelation relation) {
        victimService.addFamilyConnection(victimID, relation);
    }

    /**
     * Returns all locations currently loaded from the database.
     * Used to populate location dropdowns in the user interface.
     *
     * @return list of all Location objects
     */
    public ArrayList<Location> getLocations() {
        return victimService.getLocations();
    }


    // =========================================================================
    //  Requirements management
    // =========================================================================

    /**
     * Returns all available requirement types loaded from the options file.
     *
     * @return unmodifiable set of requirement type strings
     */
    public Set<String> getAvailableRequirementTypes() {
        return requirementService.getAvailableRequirementTypes();
    }

    /**
     * Returns the valid options for a given requirement type.
     *
     * @param requirementType non-null, non-empty requirement type string
     * @return unmodifiable set of valid option strings
     * @throws IllegalArgumentException if requirementType is null or blank
     */
    public Set<String> getOptionsForRequirementType(String requirementType) {
        return requirementService.getOptionsForType(requirementType);
    }

    /**
     * Adds a cultural or religious requirement to the specified victim.
     *
     * @param victimID    the ID of the victim
     * @param requirement non-null VictimRequirement to add
     * @throws IllegalArgumentException if victim not found, requirement is null,
     *                                  or the victim already has this requirement type
     */
    public void addRequirement(int victimID, VictimRequirement requirement) {
        victimService.addRequirement(victimID, requirement);
    }

    /**
     * Removes a cultural or religious requirement from the specified victim.
     *
     * @param victimID        the ID of the victim
     * @param requirementType non-null, non-empty type to remove
     * @throws IllegalArgumentException if victim not found or requirementType
     *                                  is null or blank
     */
    public void removeRequirement(int victimID, String requirementType) {
        victimService.removeRequirement(victimID, requirementType);
    }

    // =========================================================================
    //  Supply management
    // =========================================================================

    /**
     * Returns all supplies available for allocation (non-expired).
     *
     * @return list of available Supply objects
     */
    public ArrayList<Supply> getAvailableSupplies() {
        return supplyService.getAvailableSupplies();
    }


    /**
     * Returns all supplies including expired ones.
     * Used to display the full inventory in the supply management screen.
     *
     * @return list of all Supply objects
     */
    public ArrayList<Supply> getAllSupplies() {
        return supplyService.getAllSupplies();
    }


    /**
     * Returns a warning string listing all expired supplies, or empty string
     * if none are expired.
     *
     * @return formatted warning string
     */
    public String getExpiredSupplyWarning() {
        return supplyService.warnExpiredSupplies();
    }

    /**
     * Adds a new supply to the system.
     *
     * @param supply non-null Supply to add
     * @throws IllegalArgumentException if supply is null
     */
    public void addSupply(Supply supply) {
        supplyService.addSupply(supply);
    }

    /**
     * Updates an existing supply record.
     *
     * @param supply non-null Supply with updated values
     * @throws IllegalArgumentException if supply is null
     */
    public void updateSupply(Supply supply) {
        supplyService.updateSupply(supply);
    }

    /**
     * Allocates a supply to a disaster victim.
     *
     * @param supply non-null Supply to allocate
     * @param victim non-null DisasterVictim to allocate to
     * @throws IllegalArgumentException if supply or victim is null, or supply
     *                                  is expired
     */
    public void allocateSupply(Supply supply, DisasterVictim victim) {
        supplyService.allocateSupply(supply, victim);
    }

    // =========================================================================
    //  Inquiry management
    // =========================================================================

    /**
     * Returns all inquiries currently in the system.
     *
     * @return list of all ReliefService inquiries
     */
    public ArrayList<ReliefService> getInquiries() {
        return inquiryService.getInquiries();
    }

    /**
     * Adds a new inquiry to the system.
     *
     * @param inquiry non-null ReliefService inquiry to add
     * @throws IllegalArgumentException if inquiry is null
     */
    public void addInquiry(ReliefService inquiry) {
        inquiryService.addInquiry(inquiry);
    }

    /**
     * Updates an existing inquiry record.
     *
     * @param inquiry non-null ReliefService with updated values
     * @throws IllegalArgumentException if inquiry is null
     */
    public void updateInquiry(ReliefService inquiry) {
        inquiryService.updateInquiry(inquiry);
    }

    // =========================================================================
    //  Skill management
    // =========================================================================

    /**
     * Adds a skill to the specified victim's profile.
     *
     * @param victimID the ID of the victim
     * @param skill    non-null Skill to add
     * @throws IllegalArgumentException if victim not found, skill is null, or
     *                                  the victim already has this skill type
     */
    public void addSkill(int victimID, Skill skill) {
        victimService.addSkill(victimID, skill);
    }

    /**
     * Removes a skill from the specified victim's profile.
     *
     * @param victimID the ID of the victim
     * @param skillID  the ID of the skill to remove
     * @throws IllegalArgumentException if victim not found
     */
    public void removeSkill(int victimID, int skillID) {
        victimService.removeSkill(victimID, skillID);
    }

    /**
     * Searches all active victims for skills in the given category.
     *
     * @param category non-null, non-empty skill category
     *                 (e.g., "Medical", "Language", "Trade")
     * @return list of matching Skills from non-soft-deleted victims
     * @throws IllegalArgumentException if category is null or blank
     */
    public ArrayList<Skill> searchSkillsByCategory(String category) {
        return skillService.searchByCategory(victimService.getAllVictims(), category);
    }

    /**
     * Returns the next available victim ID from the database.
     *
     * @return next available integer ID
     */
    public int getNextVictimID() {
        return victimService.getNextVictimID();
    }

    /**
     * Returns the next available supply ID from the database.
     *
     * @return next available integer ID
     */
    public int getNextSupplyID() {
        return supplyService.getNextSupplyID();
    }

    /**
     * Returns the next available skill ID from the database.
     *
     * @return next available integer ID
     */
    public int getNextSkillID() {
        return skillService.getNextSkillID();
    }


    /**
     * Saves a new inquirer to the database and returns their generated ID.
     *
     * @param inquirer non-null Inquirer to persist
     * @return the database-generated ID for the inquirer
     */
    public int saveInquirer(Inquirer inquirer) {
        return inquiryService.saveInquirer(inquirer);
    }

    /**
     * Returns the next available inquiry ID from the database.
     *
     * @return next available integer ID
     */
    public int getNextInquiryID() {
        return inquiryService.getNextInquiryID();
    }
    
}