package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for LanguageSkill.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class LanguageSkillTest {

    private LanguageSkill skill;

    @Before
    public void setUp() {
        skill = new LanguageSkill(1, 1, "intermediate", "French", true, false);
    }

    @Test
    public void testConstructor_validArgs_setsLanguageName() {
        assertEquals("French", skill.getLanguageName());
    }

    @Test
    public void testConstructor_validArgs_setsReadWriteTrue() {
        assertTrue(skill.hasReadWrite());
    }

    @Test
    public void testConstructor_validArgs_setsSpeakListenFalse() {
        assertFalse(skill.hasSpeakListen());
    }

    @Test
    public void testConstructor_validArgs_categoryIsLanguage() {
        assertEquals("Language", skill.getCategory());
    }

    @Test
    public void testConstructor_speakListenOnly_readWriteIsFalse() {
        LanguageSkill s = new LanguageSkill(2, 1, "beginner", "Arabic", false, true);
        assertFalse(s.hasReadWrite());
    }

    @Test
    public void testConstructor_speakListenOnly_speakListenIsTrue() {
        LanguageSkill s = new LanguageSkill(2, 1, "beginner", "Arabic", false, true);
        assertTrue(s.hasSpeakListen());
    }

    @Test
    public void testConstructor_tripsLanguageNameWhitespace() {
        LanguageSkill s = new LanguageSkill(2, 1, "beginner", "  Spanish  ", true, true);
        assertEquals("Spanish", s.getLanguageName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullLanguageName_throwsIllegalArgumentException() {
        new LanguageSkill(1, 1, "beginner", null, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankLanguageName_throwsIllegalArgumentException() {
        new LanguageSkill(1, 1, "beginner", "  ", true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_bothCapabilitiesFalse_throwsIllegalArgumentException() {
        new LanguageSkill(1, 1, "beginner", "French", false, false);
    }

    @Test
    public void testSetLanguageName_validValue_updatesLanguageName() {
        skill.setLanguageName("Spanish");
        assertEquals("Spanish", skill.getLanguageName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLanguageName_nullValue_throwsIllegalArgumentException() {
        skill.setLanguageName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLanguageName_blankValue_throwsIllegalArgumentException() {
        skill.setLanguageName("  ");
    }

    @Test
    public void testSetReadWrite_trueValue_updatesReadWrite() {
        LanguageSkill s = new LanguageSkill(2, 1, "beginner", "Arabic", false, true);
        s.setReadWrite(true);
        assertTrue(s.hasReadWrite());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetReadWrite_falseWhenSpeakListenAlsoFalse_throwsIllegalArgumentException() {
        // skill has readWrite=true, speakListen=false — setting readWrite to false leaves no capability
        skill.setReadWrite(false);
    }

    @Test
    public void testSetSpeakListen_trueValue_updatesSpeakListen() {
        skill.setSpeakListen(true);
        assertTrue(skill.hasSpeakListen());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSpeakListen_falseWhenReadWriteAlsoFalse_throwsIllegalArgumentException() {
        LanguageSkill s = new LanguageSkill(2, 1, "beginner", "Arabic", false, true);
        s.setSpeakListen(false);
    }
}