package edu.ucalgary.oop;

public class LanguageSkill extends Skill{
    private String languageName;
    private boolean readWrite;
    private boolean speakListen;

    public LanguageSkill(int skillID, int victimID, String proficiencyLevel, String languageName, boolean readWrite, boolean speakListen){
        super(skillID, victimID, "Language", proficiencyLevel);

        if (languageName == null || languageName.trim().isEmpty()){
            throw new IllegalArgumentException("languageName cannot be null or empty.");
        }

        this.languageName = languageName;
        this.readWrite = readWrite;
        this.speakListen = speakListen;
    }

    public String getLanguageName() { return languageName; }
    public boolean isReadWrite() { return readWrite; }
    public boolean isSpeakListen() { return speakListen; }

    public void setLanguageName(String languageName){
        if (languageName == null || languageName.trim().isEmpty()){
            throw new IllegalArgumentException("languageName cannot be null or empty.");
        }

        this.languageName = languageName;
    }

}
