package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;

public class DisasterVictim {
    private int victimID;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private int approximateAge;
    private ArrayList<FamilyRelation> familyConnections;
    private ArrayList<MedicalRecord> medicalRecords;
    private ArrayList<Supply> personalBelongings;
    private ArrayList<VictimRequirement> requirements;
    private ArrayList<Skill> skills;
    private LocalDate entryDate;
    private String gender;
    private String comments;
    private boolean softDeleted;

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

        this.victimID = victimID;
        this.firstName = firstName;
        this.entryDate = entryDate;
        this.softDeleted = false;

        // initialize arrays to avoid null issues
        this.familyConnections = new ArrayList<>();
        this.medicalRecords = new ArrayList<>();
        this.personalBelongings = new ArrayList<>();
        this.requirements = new ArrayList<>();
        this.skills = new ArrayList<>();
    }

    public DisasterVictim(int victimID, String firstName, LocalDate entryDate, LocalDate dateOfBirth) {
        this(victimID, firstName, entryDate);

        if (dateOfBirth == null) {
            throw new IllegalArgumentException("dateOfBirth cannot be null.");
        }
        if (dateOfBirth.isAfter(entryDate)) {
            throw new IllegalArgumentException("dateOfBirth cannot be after entryDate.");
        }

        this.dateOfBirth = dateOfBirth;
    }

    public DisasterVictim(int victimID, String firstName, LocalDate entryDate, int approximateAge) {
        this(victimID, firstName, entryDate);

        if (approximateAge <= 0 || approximateAge > 150) {
            throw new IllegalArgumentException("Approximate age must be between 1 and 150.");
        }

        this.approximateAge = approximateAge;
    }

    // ============ Getters =============

    public int getVictimID() { return victimID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public int getApproximateAge() { return approximateAge; }
    public ArrayList<FamilyRelation> getFamilyConnections() { return familyConnections; }
    public ArrayList<MedicalRecord> getMedicalRecords() { return medicalRecords; }
    public ArrayList<Supply> getPersonalBelongings() { return personalBelongings; }
    public ArrayList<VictimRequirement> getRequirements() { return requirements; }
    public ArrayList<Skill> getSkills() { return skills; }
    public LocalDate getEntryDate() { return entryDate; }
    public String getComments() { return comments; }
    public String getGender() { return gender; }
    public boolean isSoftDeleted() { return softDeleted; }

    // ============ Setters =============

    public void setFirstName(String newFirstName) {
        if (newFirstName == null || newFirstName.trim().isEmpty()) {
            throw new IllegalArgumentException("firstName cannot be null/empty.");
        }
        this.firstName = newFirstName;
    }

    public void setLastName(String newLastName) {
        if (newLastName == null || newLastName.trim().isEmpty()) {
            throw new IllegalArgumentException("lastName cannot be null/empty.");
        }
        this.lastName = newLastName;
    }

    public void setDateOfBirth(LocalDate newDateOfBirth) {
        if (newDateOfBirth == null) {
            throw new IllegalArgumentException("dateOfBirth cannot be null.");
        }
        if (entryDate != null && newDateOfBirth.isAfter(entryDate)) {
            throw new IllegalArgumentException("dateOfBirth cannot be after entryDate.");
        }
        this.dateOfBirth = newDateOfBirth;
    }

    public void setApproximateAge(int newApproxAge) {
        if (newApproxAge <= 0 || newApproxAge > 150) {
            throw new IllegalArgumentException("Approximate age must be between 1 and 150.");
        }
        this.approximateAge = newApproxAge;
    }

    public void setComments(String newComment) {
        if (newComment == null || newComment.trim().isEmpty()) {
            throw new IllegalArgumentException("comment cannot be null/empty");
        }
        this.comments = newComment;
    }

    public void setGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid gender option.");
        }

        String raw = gender.trim();

        // Step 1: detect "please specify" (case-insensitive, whitespace-insensitive)
        String normalizedKey = raw.toLowerCase().replaceAll("\\s+", " ");
        if (normalizedKey.equals("please specify")) {
            this.gender = "Please Specify";
            return;
        }

        // Step 2: if previously set to "Please Specify", allow any non-empty value
        if (this.gender != null && this.gender.equals("Please Specify")) {
            this.gender = raw;
            return;
        }

        // Step 3: otherwise enforce the restricted options
        String g = raw.toLowerCase();

        String normalized;
        if (g.equals("man")) {
            normalized = "Man";
        } else if (g.equals("woman")) {
            normalized = "Woman";
        } else if (g.equals("boy")) {
            normalized = "Boy";
        } else if (g.equals("girl")) {
            normalized = "Girl";
        } else {
            throw new IllegalArgumentException("Invalid gender option.");
        }

        // Step 4: enforce child/adult rules if DOB is known
        if (this.dateOfBirth != null) {
            LocalDate ref = (this.entryDate != null) ? this.entryDate : LocalDate.now();

            int age = ref.getYear() - this.dateOfBirth.getYear();
            if (ref.getMonthValue() < this.dateOfBirth.getMonthValue()
                    || (ref.getMonthValue() == this.dateOfBirth.getMonthValue()
                    && ref.getDayOfMonth() < this.dateOfBirth.getDayOfMonth())) {
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

    // ============ Add =============

    public void addPersonalBelonging(Supply belonging){
        if (belonging == null) {
            throw new IllegalArgumentException("belonging cannot be null.");
        }

        personalBelongings.add(belonging);
    }

    public void addFamilyConnection(FamilyRelation connection){
        if (connection == null){
            throw new IllegalArgumentException("connection cannot be null.");
        }

        familyConnections.add(connection);
    }

    public void addMedicalRecord(MedicalRecord record){
        if (record == null){
            throw new IllegalArgumentException("record cannot be null.");
        }

        medicalRecords.add(record);
    }

    public void addRequirement(VictimRequirement requirement){
        if (requirement == null){
            throw new IllegalArgumentException("requirement cannot be null.");
        }

        requirements.add(requirement);
    }

    public void addSkill(Skill skill){
        if (skill == null){
            throw new IllegalArgumentException("skill cannot be null");
        }

        skills.add(skill);
    }

    // ============ Remove =============

    public void removePersonalBelonging(Supply belonging){
        if (belonging == null) {
            throw new IllegalArgumentException("belonging cannot be null.");
        }

        personalBelongings.remove(belonging);
    }

    public void removeFamilyConnection(FamilyRelation connection){
        if (connection == null){
            throw new IllegalArgumentException("connection cannot be null.");
        }

        familyConnections.remove(connection);
    }

    public void removeRequirement(String requirementType){
        if (requirementType == null || requirementType.trim().isEmpty()){
            throw new IllegalArgumentException("requirementType cannot be null/empty.");
        }

        for (int i = 0; i < requirements.size(); i++) {
            VictimRequirement req = requirements.get(i);
            if (req != null && req.getType() != null &&
                    req.getType().equalsIgnoreCase(requirementType.trim())) {
                requirements.remove(i);
                return;
            }
        }
    }

    public void removeSkill(int skillID){
        for (int i = 0; i < skills.size(); i++) {
            Skill skill = skills.get(i);
            if (skill != null && skill.getSkillID() == skillID) {
                skills.remove(i);
                return;
            }
        }
    }

    public void softDelete(){
        this.softDeleted = true;
    }
}