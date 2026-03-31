package edu.ucalgary.oop;

public abstract class Skill {
    private int skillID;
    private int victimID;
    private String category;
    private String proficiencyLevel;

    public Skill(int skillID, int victimID, String category, String proficiencyLevel){
        if (skillID <= 0) {
            throw new IllegalArgumentException("skillId must be greater than 0.");
        }
        if (victimID <= 0){
            throw new IllegalArgumentException("victimId must be greater than 0.");
        }
        if (category == null || category.trim().isEmpty()){
            throw new IllegalArgumentException("category cannot be null or empty.");
        }
        if (proficiencyLevel == null || proficiencyLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("proficiencyLevel cannot be null or empty.");
        }

        this.skillID = skillID;
        this.victimID = victimID;
        this.category = category;
        this.proficiencyLevel = proficiencyLevel;
    }

    public int getSkillID() { return skillID; }
    public int getVictimID() { return victimID; }
    public String getCategory() { return category; }
    public String getProficiencyLevel() { return proficiencyLevel; }

    public void setProficiencyLevel(String proficiencyLevel){
        if (proficiencyLevel == null || proficiencyLevel.trim().isEmpty()){
            throw new IllegalArgumentException("proficiencyLevel cannot be null or empty.");
        }
        this.proficiencyLevel = proficiencyLevel;
    }
}
