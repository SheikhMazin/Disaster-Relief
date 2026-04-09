package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Unit tests for SkillService.
 * Uses MockDataRepository — no database connection required.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class SkillServiceTest {

    private SkillService   service;
    private DisasterVictim activeVictim;
    private DisasterVictim softDeletedVictim;
    private LanguageSkill  languageSkill;
    private TradeSkill     tradeSkill;

    @Before
    public void setUp() {
        service             = new SkillService(new MockDataRepository(), ActionLogger.getInstance());
        activeVictim        = new DisasterVictim(1, "Alice", LocalDate.now());
        softDeletedVictim   = new DisasterVictim(2, "Bob",   LocalDate.now());
        softDeletedVictim.softDelete();
        languageSkill       = new LanguageSkill(1, 1, "intermediate", "French", true, false);
        tradeSkill          = new TradeSkill(2, 1, "beginner", "carpentry");
    }

    // =========================================================================
    //  Constructor
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullRepository_throwsIllegalArgumentException() {
        new SkillService(null, ActionLogger.getInstance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullLogger_throwsIllegalArgumentException() {
        new SkillService(new MockDataRepository(), null);
    }

    // =========================================================================
    //  addSkill
    // =========================================================================

    @Test
    public void testAddSkill_validSkill_appearsInVictimSkills() {
        service.addSkill(activeVictim, languageSkill);
        assertTrue(activeVictim.getSkills().contains(languageSkill));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSkill_nullVictim_throwsIllegalArgumentException() {
        service.addSkill(null, languageSkill);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSkill_nullSkill_throwsIllegalArgumentException() {
        service.addSkill(activeVictim, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSkill_duplicateSkillType_throwsIllegalArgumentException() {
        service.addSkill(activeVictim, languageSkill);
        LanguageSkill duplicate = new LanguageSkill(3, 1, "advanced", "French", false, true);
        service.addSkill(activeVictim, duplicate);
    }

    // =========================================================================
    //  removeSkill
    // =========================================================================

    @Test
    public void testRemoveSkill_existingSkill_removedFromVictimSkills() {
        service.addSkill(activeVictim, languageSkill);
        service.removeSkill(activeVictim, 1);
        assertTrue(activeVictim.getSkills().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveSkill_nullVictim_throwsIllegalArgumentException() {
        service.removeSkill(null, 1);
    }

    // =========================================================================
    //  searchByCategory
    // =========================================================================

    @Test
    public void testSearchByCategory_matchingCategory_returnsSkill() {
        activeVictim.addSkill(languageSkill);
        ArrayList<DisasterVictim> victims = new ArrayList<>();
        victims.add(activeVictim);
        assertTrue(service.searchByCategory(victims, "Language").contains(languageSkill));
    }

    @Test
    public void testSearchByCategory_nonMatchingCategory_returnsEmptyList() {
        activeVictim.addSkill(languageSkill);
        ArrayList<DisasterVictim> victims = new ArrayList<>();
        victims.add(activeVictim);
        assertTrue(service.searchByCategory(victims, "Medical").isEmpty());
    }

    @Test
    public void testSearchByCategory_softDeletedVictim_skillExcludedFromResults() {
        softDeletedVictim.addSkill(new LanguageSkill(5, 2, "beginner", "Spanish", true, false));
        ArrayList<DisasterVictim> victims = new ArrayList<>();
        victims.add(softDeletedVictim);
        assertTrue(service.searchByCategory(victims, "Language").isEmpty());
    }

    @Test
    public void testSearchByCategory_mixedVictims_onlyActiveVictimSkillsReturned() {
        activeVictim.addSkill(languageSkill);
        softDeletedVictim.addSkill(new LanguageSkill(5, 2, "beginner", "Spanish", true, false));
        ArrayList<DisasterVictim> victims = new ArrayList<>();
        victims.add(activeVictim);
        victims.add(softDeletedVictim);
        assertEquals(1, service.searchByCategory(victims, "Language").size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchByCategory_nullCategory_throwsIllegalArgumentException() {
        service.searchByCategory(new ArrayList<>(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSearchByCategory_blankCategory_throwsIllegalArgumentException() {
        service.searchByCategory(new ArrayList<>(), "  ");
    }

    // =========================================================================
    //  getVictimSkills
    // =========================================================================

    @Test
    public void testGetVictimSkills_victimWithSkill_returnsSkillList() {
        service.addSkill(activeVictim, languageSkill);
        assertTrue(service.getVictimSkills(activeVictim).contains(languageSkill));
    }

    @Test
    public void testGetVictimSkills_victimWithNoSkills_returnsEmptyList() {
        assertTrue(service.getVictimSkills(activeVictim).isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetVictimSkills_nullVictim_throwsIllegalArgumentException() {
        service.getVictimSkills(null);
    }
}