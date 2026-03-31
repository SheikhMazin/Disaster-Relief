package edu.ucalgary.oop;

public class TradeSkill extends Skill {
    private String tradeType;

    public TradeSkill(int skillID, int victimID, String proficiencyLevel, String tradeType){
        super(skillID, victimID, "Trade", proficiencyLevel);

        if (tradeType == null || tradeType.trim().isEmpty()){
            throw new IllegalArgumentException("tradeType cannot be null or empty.");
        }

        this.tradeType = tradeType;
    }

    public String getTradeType() { return tradeType; }

    public void setTradeType(String tradeType) {
        if (tradeType == null || tradeType.trim().isEmpty()){
            throw new IllegalArgumentException("tradeType cannot be null or empty.");
        }

        this.tradeType = tradeType;
    }

}
