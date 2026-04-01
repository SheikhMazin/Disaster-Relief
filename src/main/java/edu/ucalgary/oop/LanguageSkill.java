package edu.ucalgary.oop;

/**
 * LanguageSkill
 *
 * Represents a language skill registered by a disaster victim.
 * In addition to the base Skill fields, a language skill tracks the specific
 * language name (no fixed options — any language is accepted) and the
 * capabilities the person has: read/write and/or speak/listen.
 * At least one capability must be selected.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */
public class LanguageSkill extends Skill {

    private String languageName;
    private boolean readWrite;
    private boolean speakListen;

    /**
     * Constructs a LanguageSkill with the given identifiers, proficiency,
     * language name, and capability flags.
     *
     * @param skillID        unique positive integer skill identifier
     * @param victimID       positive integer ID of the owning victim
     * @param proficiencyLevel non-null, non-empty proficiency level
     * @param languageName   non-null, non-empty name of the language
     * @param readWrite      true if the person can read and write the language
     * @param speakListen    true if the person can speak and listen in the language
     * @throws IllegalArgumentException if languageName is null or empty, or if
     *                                  neither readWrite nor speakListen is true
     */
    public LanguageSkill(int skillID, int victimID, String proficiencyLevel,
                         String languageName, boolean readWrite, boolean speakListen) {
        super(skillID, victimID, "Language", proficiencyLevel);

        if (languageName == null || languageName.trim().isEmpty()) {
            throw new IllegalArgumentException("languageName cannot be null or empty.");
        }
        if (!readWrite && !speakListen) {
            throw new IllegalArgumentException(
                    "At least one capability (readWrite or speakListen) must be true.");
        }

        this.languageName = languageName.trim();
        this.readWrite    = readWrite;
        this.speakListen  = speakListen;
    }

    // =========================================================================
    //  Getters
    // =========================================================================

    /**
     * Returns the name of the language for this skill entry.
     * @return languageName
     */
    public String getLanguageName() { return languageName; }

    /**
     * Returns whether the person can read and write in this language.
     * @return true if read/write capability is registered
     */
    public boolean hasReadWrite() { return readWrite; }

    /**
     * Returns whether the person can speak and listen in this language.
     * @return true if speak/listen capability is registered
     */
    public boolean hasSpeakListen() { return speakListen; }

    // =========================================================================
    //  Setters
    // =========================================================================

    /**
     * Updates the language name for this skill.
     *
     * @param languageName non-null, non-empty language name
     * @throws IllegalArgumentException if value is null or blank
     */
    public void setLanguageName(String languageName) {
        if (languageName == null || languageName.trim().isEmpty()) {
            throw new IllegalArgumentException("languageName cannot be null or empty.");
        }
        this.languageName = languageName.trim();
    }

    /**
     * Updates the read/write capability flag.
     *
     * @param readWrite new value for the read/write capability
     * @throws IllegalArgumentException if setting readWrite to false would leave
     *                                  the victim with no capabilities at all
     */
    public void setReadWrite(boolean readWrite) {
        if (!readWrite && !this.speakListen) {
            throw new IllegalArgumentException(
                    "At least one capability (readWrite or speakListen) must be true.");
        }
        this.readWrite = readWrite;
    }

    /**
     * Updates the speak/listen capability flag.
     *
     * @param speakListen new value for the speak/listen capability
     * @throws IllegalArgumentException if setting speakListen to false would leave
     *                                  the victim with no capabilities at all
     */
    public void setSpeakListen(boolean speakListen) {
        if (!speakListen && !this.readWrite) {
            throw new IllegalArgumentException(
                    "At least one capability (readWrite or speakListen) must be true.");
        }
        this.speakListen = speakListen;
    }
}