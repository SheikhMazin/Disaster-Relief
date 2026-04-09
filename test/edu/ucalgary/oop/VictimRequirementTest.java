package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for VictimRequirement.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class VictimRequirementTest {

    private VictimRequirement requirement;

    @Before
    public void setUp() {
        requirement = new VictimRequirement(1, "dietary restrictions", "halal");
    }

    @Test
    public void testConstructor_validArgs_setsVictimID() {
        assertEquals(1, requirement.getVictimID());
    }

    @Test
    public void testConstructor_validArgs_setsRequirementType() {
        assertEquals("dietary restrictions", requirement.getRequirementType());
    }

    @Test
    public void testConstructor_validArgs_setsSelectedOption() {
        assertEquals("halal", requirement.getSelectedOption());
    }

    @Test
    public void testConstructor_validArgs_tripsWhitespace() {
        VictimRequirement req = new VictimRequirement(1, "  dietary restrictions  ", "  halal  ");
        assertEquals("dietary restrictions", req.getRequirementType());
    }

    @Test
    public void testConstructor_validArgs_tripsOptionWhitespace() {
        VictimRequirement req = new VictimRequirement(1, "dietary restrictions", "  halal  ");
        assertEquals("halal", req.getSelectedOption());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_zeroVictimID_throwsIllegalArgumentException() {
        new VictimRequirement(0, "dietary restrictions", "halal");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_negativeVictimID_throwsIllegalArgumentException() {
        new VictimRequirement(-1, "dietary restrictions", "halal");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullRequirementType_throwsIllegalArgumentException() {
        new VictimRequirement(1, null, "halal");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankRequirementType_throwsIllegalArgumentException() {
        new VictimRequirement(1, "  ", "halal");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullSelectedOption_throwsIllegalArgumentException() {
        new VictimRequirement(1, "dietary restrictions", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankSelectedOption_throwsIllegalArgumentException() {
        new VictimRequirement(1, "dietary restrictions", "  ");
    }

    @Test
    public void testSetSelectedOption_validValue_updatesOption() {
        requirement.setSelectedOption("kosher");
        assertEquals("kosher", requirement.getSelectedOption());
    }

    @Test
    public void testSetSelectedOption_tripsWhitespace() {
        requirement.setSelectedOption("  kosher  ");
        assertEquals("kosher", requirement.getSelectedOption());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedOption_nullValue_throwsIllegalArgumentException() {
        requirement.setSelectedOption(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedOption_blankValue_throwsIllegalArgumentException() {
        requirement.setSelectedOption("  ");
    }
}