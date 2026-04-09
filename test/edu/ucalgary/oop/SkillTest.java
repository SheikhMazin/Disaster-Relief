package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the abstract Skill base class, tested via LanguageSkill.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class SkillTest {

    private LanguageSkill skill;

    @Before
    public void setUp() {
        skill = new LanguageSkill(1, 1, "intermediate", "French", true, true);
    }

    @Test
    public void testConstructor_validArgs_setsSkillID() {
        assertEquals(1, skill.getSkillID());
    }

    @Test
    public void testConstructor_validArgs_setsVictimID() {
        assertEquals(1, skill.getVictimID());
    }

    @Test
    public void testConstructor_validArgs_setsProficiencyLevel() {
        assertEquals("intermediate", skill.getProficiencyLevel());
    }

    @Test
    public void testConstructor_validArgs_setsCategory() {
        assertEquals("Language", skill.getCategory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_zeroSkillID_throwsIllegalArgumentException() {
        new LanguageSkill(0, 1, "beginner", "French", true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_negativeSkillID_throwsIllegalArgumentException() {
        new LanguageSkill(-1, 1, "beginner", "French", true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_zeroVictimID_throwsIllegalArgumentException() {
        new LanguageSkill(1, 0, "beginner", "French", true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_negativeVictimID_throwsIllegalArgumentException() {
        new LanguageSkill(1, -1, "beginner", "French", true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullProficiencyLevel_throwsIllegalArgumentException() {
        new LanguageSkill(1, 1, null, "French", true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankProficiencyLevel_throwsIllegalArgumentException() {
        new LanguageSkill(1, 1, "  ", "French", true, true);
    }

    @Test
    public void testSetProficiencyLevel_validValue_updatesProficiencyLevel() {
        skill.setProficiencyLevel("advanced");
        assertEquals("advanced", skill.getProficiencyLevel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProficiencyLevel_nullValue_throwsIllegalArgumentException() {
        skill.setProficiencyLevel(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetProficiencyLevel_blankValue_throwsIllegalArgumentException() {
        skill.setProficiencyLevel("  ");
    }
}