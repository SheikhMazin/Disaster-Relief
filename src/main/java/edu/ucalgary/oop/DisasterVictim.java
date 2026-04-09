package edu.ucalgary.oop;

/**
 * DisasterVictim
 *
 * Represents a person registered in the disaster relief system.
 * Each victim has a unique ID, a mandatory first name, and an immutable
 * entry date recorded at registration. Age is tracked as either an exact
 * date of birth OR an approximate age — never both simultaneously.
 * A victim may be soft-deleted (hidden from the UI while data is retained)
 * or hard-deleted (fully removed) from the system.
 *
 * Valid gender options (case-insensitive): man, woman, boy, girl,
 * non-binary person, please specify. Child/adult terms are enforced
 * when a date of birth is known.
 *
 * @author Sheikh Muhammad Mazin
 * @version 2.0
 * @since 2026-01-01
 */

import java.time.LocalDate;
import java.util.ArrayList;

public class DisasterVictim {

    private final int victimID;
    private String firstName;
    private String lastName;
    private int locationID;

    /** Exact date of birth — mutually exclusive with approximateAge. */
    private LocalDate dateOfBirth;

    /** Approximate age in years — mutually exclusive with dateOfBirth. Null if unknown. */
    private Integer approximateAge;

    private ArrayList<FamilyRelation> familyConnections;
    private ArrayList<MedicalRecord> medicalRecords;
    private ArrayList<Supply> personalBelongings;
    private ArrayList<VictimRequirement> requirements;
    private ArrayList<Skill> skills;

    /** Immutable date this record was entered into the system. */
    private final LocalDate ENTRY_DATE;

    private String gender;
    private String comments;
    private boolean softDeleted;

    // =========================================================================
    //  Constructors
    // =========================================================================

    /**
     * Base constructor — creates a victim with no age information.
     *
     * @param victimID  unique positive integer identifier
     * @param firstName non-null, non-empty first name
     * @param entryDate non-null date of registration
     * @throws IllegalArgumentException if any argument is invalid
     */
    public DisasterVictim(int victimID, String firstName, LocalDate entryDate) {
        if (victimID <= 0) {
            throw new IllegalArgumentException("Victim ID must be greater than 0.");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("firstName cannot be null or empty.");
        }
        if (entryDate == null) {
            throw new IllegalArgumentException("entryDate cannot be null.");
        }

        this.victimID    = victimID;
        this.firstName   = firstName;
        this.ENTRY_DATE  = entryDate;
        this.softDeleted = false;

        this.familyConnections  = new ArrayList<>();
        this.medicalRecords     = new ArrayList<>();
        this.personalBelongings = new ArrayList<>();
        this.requirements       = new ArrayList<>();
        this.skills             = new ArrayList<>();
    }

    /**
     * Constructor for a victim whose exact date of birth is known.
     *
     * @param victimID    unique positive integer identifier
     * @param firstName   non-null, non-empty first name
     * @param entryDate   non-null date of registration
     * @param dateOfBirth non-null date of birth, must not be after entryDate
     * @throws IllegalArgumentException if dateOfBirth is null or after entryDate
     */
    public DisasterVictim(int victimID, String firstName, LocalDate entryDate,
                          LocalDate dateOfBirth) {
        this(victimID, firstName, entryDate);

        if (dateOfBirth == null) {
            throw new IllegalArgumentException("dateOfBirth cannot be null.");
        }
        if (dateOfBirth.isAfter(entryDate)) {
            throw new IllegalArgumentException("dateOfBirth cannot be after entryDate.");
        }

        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Constructor for a victim whose exact date of birth is unknown — uses
     * an approximate age instead.
     *
     * @param victimID       unique positive integer identifier
     * @param firstName      non-null, non-empty first name
     * @param entryDate      non-null date of registration
     * @param approximateAge estimated age in years, must be between 1 and 150
     * @throws IllegalArgumentException if approximateAge is out of range
     */
    public DisasterVictim(int victimID, String firstName, LocalDate entryDate,
                          int approximateAge) {
        this(victimID, firstName, entryDate);

        if (approximateAge <= 0 || approximateAge > 150) {
            throw new IllegalArgumentException("Approximate age must be between 1 and 150.");
        }

        this.approximateAge = approximateAge;
    }

    // =========================================================================
    //  Getters
    // =========================================================================

    /**
     * Returns the unique identifier for this victim.
     * @return victimID
     */
    public int getVictimID() { return victimID; }

    /**
     * Returns the victim's first name.
     * @return firstName
     */
    public String getFirstName() { return firstName; }

    /**
     * Returns the victim's last name, or null if not set.
     * @return lastName
     */
    public String getLastName() { return lastName; }

    /**
     * Returns the ID of the location this victim is currently registered at.
     * Returns 0 if no location has been assigned.
     *
     * @return locationID, or 0 if not set
     */
    public int getLocationID() { return locationID; }

    /**
     * Returns the victim's exact date of birth, or null if only an
     * approximate age is recorded.
     * @return dateOfBirth, may be null
     */
    public LocalDate getDateOfBirth() { return dateOfBirth; }

    /**
     * Returns the victim's approximate age, or null if an exact date of birth
     * is recorded or age is unknown.
     * @return approximateAge, may be null
     */
    public Integer getApproximateAge() { return approximateAge; }

    /**
     * Returns the list of family relationships for this victim.
     * @return familyConnections
     */
    public ArrayList<FamilyRelation> getFamilyConnections() { return familyConnections; }

    /**
     * Returns the list of medical records for this victim.
     * @return medicalRecords
     */
    public ArrayList<MedicalRecord> getMedicalRecords() { return medicalRecords; }

    /**
     * Returns the list of supplies allocated as personal belongings.
     * @return personalBelongings
     */
    public ArrayList<Supply> getPersonalBelongings() { return personalBelongings; }

    /**
     * Returns the list of cultural/religious requirements for this victim.
     * @return requirements
     */
    public ArrayList<VictimRequirement> getRequirements() { return requirements; }

    /**
     * Returns the list of skills registered for this victim.
     * @return skills
     */
    public ArrayList<Skill> getSkills() { return skills; }

    /**
     * Returns the date this victim was entered into the system.
     * @return ENTRY_DATE (immutable)
     */
    public LocalDate getEntryDate() { return ENTRY_DATE; }

    /**
     * Returns any free-text comments attached to this victim's record.
     * @return comments, may be null
     */
    public String getComments() { return comments; }

    /**
     * Returns the victim's gender value.
     * @return gender, may be null if not set
     */
    public String getGender() { return gender; }

    /**
     * Returns whether this victim has been soft-deleted.
     * @return true if soft-deleted, false otherwise
     */
    public boolean isSoftDeleted() { return softDeleted; }

    // =========================================================================
    //  Setters
    // =========================================================================

    /**
     * Updates the victim's first name.
     *
     * @param newFirstName non-null, non-empty first name
     * @throws IllegalArgumentException if the value is null or blank
     */
    public void setFirstName(String newFirstName) {
        if (newFirstName == null || newFirstName.trim().isEmpty()) {
            throw new IllegalArgumentException("firstName cannot be null or empty.");
        }
        this.firstName = newFirstName;
    }

    /**
     * Updates the victim's last name.
     *
     * @param newLastName non-null, non-empty last name
     * @throws IllegalArgumentException if the value is null or blank
     */
    public void setLastName(String newLastName) {
        if (newLastName == null || newLastName.trim().isEmpty()) {
            throw new IllegalArgumentException("lastName cannot be null or empty.");
        }
        this.lastName = newLastName;
    }


    /**
     * Sets the location ID for this victim, indicating which relief location
     * they are currently registered at.
     *
     * @param locationID positive integer ID of the location
     * @throws IllegalArgumentException if locationID is not positive
     */
    public void setLocationID(int locationID) {
        if (locationID <= 0) {
            throw new IllegalArgumentException("locationID must be greater than 0.");
        }
        this.locationID = locationID;
    }

    /**
     * Sets the victim's exact date of birth, clearing any approximate age.
     * Per Feature 5, a date of birth can replace an approximate age, but an
     * approximate age can never replace a date of birth.
     *
     * @param newDateOfBirth non-null date of birth, must not be after ENTRY_DATE
     * @throws IllegalArgumentException if the value is null or after ENTRY_DATE
     */
    public void setDateOfBirth(LocalDate newDateOfBirth) {
        if (newDateOfBirth == null) {
            throw new IllegalArgumentException("dateOfBirth cannot be null.");
        }
        if (newDateOfBirth.isAfter(ENTRY_DATE)) {
            throw new IllegalArgumentException("dateOfBirth cannot be after entryDate.");
        }
        this.dateOfBirth    = newDateOfBirth;
        this.approximateAge = null;
    }

    /**
     * Sets the victim's approximate age.
     * Not permitted if an exact date of birth is already recorded (Feature 5).
     *
     * @param newApproxAge estimated age in years, must be between 1 and 150
     * @throws IllegalArgumentException if age is out of range or a date of birth
     *                                  is already set
     */
    public void setApproximateAge(int newApproxAge) {
        if (this.dateOfBirth != null) {
            throw new IllegalArgumentException(
                    "Cannot set approximate age when a date of birth is already recorded.");
        }
        if (newApproxAge <= 0 || newApproxAge > 150) {
            throw new IllegalArgumentException("Approximate age must be between 1 and 150.");
        }
        this.approximateAge = newApproxAge;
    }

    /**
     * Updates the free-text comments on this victim's record.
     *
     * @param newComment non-null, non-empty comment string
     * @throws IllegalArgumentException if the value is null or blank
     */
    public void setComments(String newComment) {
        if (newComment == null || newComment.trim().isEmpty()) {
            throw new IllegalArgumentException("comment cannot be null or empty.");
        }
        this.comments = newComment;
    }

    /**
     * Sets the victim's gender. Valid options (case-insensitive):
     * man, woman, boy, girl, non-binary person, please specify.
     * If a date of birth is known, child/adult terms are enforced:
     * under 18 must use boy/girl/non-binary person, 18+ must use
     * man/woman/non-binary person.
     * "Please specify" is always accepted and allows any follow-up value.
     *
     * @param gender non-null, non-empty gender string
     * @throws IllegalArgumentException if the value is null, empty, or not a
     *                                  valid option given the victim's age
     */
    public void setGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid gender option.");
        }

        String raw           = gender.trim();
        String normalizedKey = raw.toLowerCase().replaceAll("\\s+", " ");

        // "Please specify" is always valid
        if (normalizedKey.equals("please specify")) {
            this.gender = "Please Specify";
            return;
        }

        // If previously set to "Please Specify", allow any non-empty value
        if (this.gender != null && this.gender.equals("Please Specify")) {
            this.gender = raw;
            return;
        }

        // Normalize to canonical form
        String normalized;
        switch (normalizedKey) {
            case "man"              -> normalized = "Man";
            case "woman"            -> normalized = "Woman";
            case "boy"              -> normalized = "Boy";
            case "girl"             -> normalized = "Girl";
            case "non-binary person"-> normalized = "Non-binary person";
            default -> throw new IllegalArgumentException("Invalid gender option.");
        }

        // Enforce child/adult rules only for gendered terms when DOB is known
        if (this.dateOfBirth != null && !normalized.equals("Non-binary person")) {
            int age = ENTRY_DATE.getYear() - this.dateOfBirth.getYear();
            if (ENTRY_DATE.getMonthValue() < this.dateOfBirth.getMonthValue()
                    || (ENTRY_DATE.getMonthValue() == this.dateOfBirth.getMonthValue()
                    && ENTRY_DATE.getDayOfMonth() < this.dateOfBirth.getDayOfMonth())) {
                age--;
            }

            boolean isChild = age < 18;
            if (isChild && (normalized.equals("Man") || normalized.equals("Woman"))) {
                throw new IllegalArgumentException("Invalid gender option.");
            }
            if (!isChild && (normalized.equals("Boy") || normalized.equals("Girl"))) {
                throw new IllegalArgumentException("Invalid gender option.");
            }
        }

        this.gender = normalized;
    }

    // =========================================================================
    //  Add methods
    // =========================================================================

    /**
     * Adds a supply item to this victim's personal belongings.
     *
     * @param belonging non-null Supply to add
     * @throws IllegalArgumentException if belonging is null
     */
    public void addPersonalBelonging(Supply belonging) {
        if (belonging == null) {
            throw new IllegalArgumentException("belonging cannot be null.");
        }
        personalBelongings.add(belonging);
    }

    /**
     * Adds a family relationship to this victim's record.
     *
     * @param connection non-null FamilyRelation to add
     * @throws IllegalArgumentException if connection is null
     */
    public void addFamilyConnection(FamilyRelation connection) {
        if (connection == null) {
            throw new IllegalArgumentException("connection cannot be null.");
        }
        familyConnections.add(connection);
    }

    /**
     * Adds a medical record to this victim's history.
     *
     * @param record non-null MedicalRecord to add
     * @throws IllegalArgumentException if record is null
     */
    public void addMedicalRecord(MedicalRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("record cannot be null.");
        }
        medicalRecords.add(record);
    }

    /**
     * Adds a cultural or religious requirement for this victim.
     * Each requirement type may only appear once per victim.
     *
     * @param requirement non-null VictimRequirement to add
     * @throws IllegalArgumentException if requirement is null or if the victim
     *                                  already has a requirement of this type
     */
    public void addRequirement(VictimRequirement requirement) {
        if (requirement == null) {
            throw new IllegalArgumentException("requirement cannot be null.");
        }
        for (VictimRequirement existing : requirements) {
            if (existing.getRequirementType()
                    .equalsIgnoreCase(requirement.getRequirementType())) {
                throw new IllegalArgumentException(
                        "Victim already has a requirement of type: "
                                + requirement.getRequirementType());
            }
        }
        requirements.add(requirement);
    }

    /**
     * Registers a skill for this victim. A victim may not register the exact
     * same skill type more than once (e.g., cannot register intermediate French
     * AND advanced French, but may register intermediate French and advanced Arabic).
     *
     * @param skill non-null Skill to add
     * @throws IllegalArgumentException if skill is null or a duplicate skill
     *                                  type already exists
     */
    public void addSkill(Skill skill) {
        if (skill == null) {
            throw new IllegalArgumentException("skill cannot be null.");
        }
        for (Skill existing : skills) {
            if (isDuplicateSkill(existing, skill)) {
                throw new IllegalArgumentException(
                        "Victim already has a skill of this type registered.");
            }
        }
        skills.add(skill);
    }

    /**
     * Determines whether two skills represent the same specific skill type.
     * For language skills the language name is compared; for medical skills
     * the certification type; for trade skills the trade type.
     *
     * @param existing the skill already registered
     * @param incoming the skill being added
     * @return true if they represent the same specific skill type
     */
    private boolean isDuplicateSkill(Skill existing, Skill incoming) {
        if (!existing.getCategory().equalsIgnoreCase(incoming.getCategory())) {
            return false;
        }
        if (existing instanceof LanguageSkill && incoming instanceof LanguageSkill) {
            return ((LanguageSkill) existing).getLanguageName()
                    .equalsIgnoreCase(((LanguageSkill) incoming).getLanguageName());
        }
        if (existing instanceof MedicalSkill && incoming instanceof MedicalSkill) {
            return ((MedicalSkill) existing).getCertificationType()
                    .equalsIgnoreCase(((MedicalSkill) incoming).getCertificationType());
        }
        if (existing instanceof TradeSkill && incoming instanceof TradeSkill) {
            return ((TradeSkill) existing).getTradeType()
                    .equalsIgnoreCase(((TradeSkill) incoming).getTradeType());
        }
        return false;
    }

    // =========================================================================
    //  Remove methods
    // =========================================================================

    /**
     * Removes a supply item from this victim's personal belongings.
     *
     * @param belonging non-null Supply to remove
     * @throws IllegalArgumentException if belonging is null
     */
    public void removePersonalBelonging(Supply belonging) {
        if (belonging == null) {
            throw new IllegalArgumentException("belonging cannot be null.");
        }
        personalBelongings.remove(belonging);
    }

    /**
     * Removes a family relationship from this victim's record.
     *
     * @param connection non-null FamilyRelation to remove
     * @throws IllegalArgumentException if connection is null
     */
    public void removeFamilyConnection(FamilyRelation connection) {
        if (connection == null) {
            throw new IllegalArgumentException("connection cannot be null.");
        }
        familyConnections.remove(connection);
    }

    /**
     * Removes the requirement of the given type from this victim's record.
     *
     * @param requirementType non-null, non-empty type string to match
     * @throws IllegalArgumentException if requirementType is null or blank
     */
    public void removeRequirement(String requirementType) {
        if (requirementType == null || requirementType.trim().isEmpty()) {
            throw new IllegalArgumentException("requirementType cannot be null or empty.");
        }
        for (int i = 0; i < requirements.size(); i++) {
            VictimRequirement req = requirements.get(i);
            if (req != null && req.getRequirementType() != null
                    && req.getRequirementType()
                    .equalsIgnoreCase(requirementType.trim())) {
                requirements.remove(i);
                return;
            }
        }
    }

    /**
     * Removes the skill with the given ID from this victim's skill list.
     *
     * @param skillID the ID of the Skill to remove
     */
    public void removeSkill(int skillID) {
        for (int i = 0; i < skills.size(); i++) {
            Skill skill = skills.get(i);
            if (skill != null && skill.getSkillID() == skillID) {
                skills.remove(i);
                return;
            }
        }
    }

    // =========================================================================
    //  Soft delete
    // =========================================================================

    /**
     * Marks this victim as soft-deleted. Their data persists in the database
     * but they are hidden throughout the user interface and excluded from
     * skill searches.
     */
    public void softDelete() {
        this.softDeleted = true;
    }
}