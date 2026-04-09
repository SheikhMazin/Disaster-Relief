package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for TradeSkill.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class TradeSkillTest {

    private TradeSkill skill;

    @Before
    public void setUp() {
        skill = new TradeSkill(1, 1, "intermediate", "carpentry");
    }

    @Test
    public void testConstructor_validArgs_setsTradeType() {
        assertEquals("carpentry", skill.getTradeType());
    }

    @Test
    public void testConstructor_validArgs_categoryIsTrade() {
        assertEquals("Trade", skill.getCategory());
    }

    @Test
    public void testConstructor_caseInsensitiveType_normalisesToLowerCase() {
        TradeSkill s = new TradeSkill(2, 1, "beginner", "PLUMBING");
        assertEquals("plumbing", s.getTradeType());
    }

    @Test
    public void testConstructor_plumbingType_isAccepted() {
        TradeSkill s = new TradeSkill(2, 1, "beginner", "plumbing");
        assertEquals("plumbing", s.getTradeType());
    }

    @Test
    public void testConstructor_electricityType_isAccepted() {
        TradeSkill s = new TradeSkill(2, 1, "advanced", "electricity");
        assertEquals("electricity", s.getTradeType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullTradeType_throwsIllegalArgumentException() {
        new TradeSkill(1, 1, "beginner", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankTradeType_throwsIllegalArgumentException() {
        new TradeSkill(1, 1, "beginner", "  ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_invalidTradeType_throwsIllegalArgumentException() {
        new TradeSkill(1, 1, "beginner", "welding");
    }

    @Test
    public void testSetTradeType_validValue_updatesTradeType() {
        skill.setTradeType("electricity");
        assertEquals("electricity", skill.getTradeType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTradeType_invalidValue_throwsIllegalArgumentException() {
        skill.setTradeType("electrical");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTradeType_nullValue_throwsIllegalArgumentException() {
        skill.setTradeType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTradeType_blankValue_throwsIllegalArgumentException() {
        skill.setTradeType("  ");
    }
}