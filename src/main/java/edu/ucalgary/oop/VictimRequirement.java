package edu.ucalgary.oop;

public class VictimRequirement {
    private int victimID;
    private String requirementType;
    private String selectedOption;

    public VictimRequirement(int victimID, String requirementType, String selectedOption){
        if (victimID <= 0){
            throw new IllegalArgumentException("victimID must be greater than 0.");
        }
        if (requirementType == null || requirementType.trim().isEmpty()){
            throw new IllegalArgumentException("requirementType cannot be null or empty.");
        }
        if (selectedOption == null || selectedOption.trim().isEmpty()){
            throw new IllegalArgumentException("selectedOption cannot be null or empty.");
        }

        this.victimID = victimID;
        this.requirementType = requirementType;
        this.selectedOption = selectedOption;
    }

    public int getVictimID() { return victimID; }
    public String getRequirementType() { return requirementType; }
    public String getSelectedOption() { return selectedOption; }

    public void setSelectedOption(String selectedOption){
        if (selectedOption == null || selectedOption.trim().isEmpty()){
            throw new IllegalArgumentException("selectedOption cannot be null or empty.");
        }

        this.selectedOption = selectedOption;
    }
}
