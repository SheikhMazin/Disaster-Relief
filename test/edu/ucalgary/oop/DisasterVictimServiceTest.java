package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

/**
 * Unit tests for DisasterVictimService.
 * Uses MockDataRepository — no database connection required.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class DisasterVictimServiceTest {

    private DisasterVictimService service;
    private DisasterVictim        victim;

    @Before
    public void setUp() {
        service = new DisasterVictimService(new MockDataRepository(), ActionLogger.getInstance());
        victim  = new DisasterVictim(1, "Alice", LocalDate.now());
    }

    // =========================================================================
    //  Constructor
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullRepository_throwsIllegalArgumentException() {
        new DisasterVictimService(null, ActionLogger.getInstance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullLogger_throwsIllegalArgumentException() {
        new DisasterVictimService(new MockDataRepository(), null);
    }

    // =========================================================================
    //  addVictim
    // =========================================================================

    @Test
    public void testAddVictim_validVictim_victimAppearsInAllVictims() {
        service.addVictim(victim);
        assertTrue(service.getAllVictims().contains(victim));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVictim_nullVictim_throwsIllegalArgumentException() {
        service.addVictim(null);
    }

    // =========================================================================
    //  getVictimByID
    // =========================================================================

    @Test
    public void testGetVictimByID_existingID_returnsCorrectVictim() {
        service.addVictim(victim);
        assertEquals(victim, service.getVictimByID(1));
    }

    @Test
    public void testGetVictimByID_nonExistingID_returnsNull() {
        assertNull(service.getVictimByID(999));
    }

    // =========================================================================
    //  getActiveVictims
    // =========================================================================

    @Test
    public void testGetActiveVictims_noSoftDeleted_returnsAllVictims() {
        service.addVictim(victim);
        assertEquals(1, service.getActiveVictims().size());
    }

    @Test
    public void testGetActiveVictims_afterSoftDelete_excludesSoftDeletedVictim() {
        service.addVictim(victim);
        service.softDeleteVictim(1);
        assertTrue(service.getActiveVictims().isEmpty());
    }

    // =========================================================================
    //  softDeleteVictim
    // =========================================================================

    @Test
    public void testSoftDeleteVictim_existingVictim_marksVictimAsSoftDeleted() {
        service.addVictim(victim);
        service.softDeleteVictim(1);
        assertTrue(victim.isSoftDeleted());
    }

    @Test
    public void testSoftDeleteVictim_existingVictim_remainsInAllVictims() {
        service.addVictim(victim);
        service.softDeleteVictim(1);
        assertTrue(service.getAllVictims().contains(victim));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSoftDeleteVictim_nonExistingID_throwsIllegalArgumentException() {
        service.softDeleteVictim(999);
    }

    // =========================================================================
    //  hardDeleteVictim
    // =========================================================================

    @Test
    public void testHardDeleteVictim_existingVictim_removedFromAllVictims() {
        service.addVictim(victim);
        service.hardDeleteVictim(1);
        assertFalse(service.getAllVictims().contains(victim));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHardDeleteVictim_nonExistingID_throwsIllegalArgumentException() {
        service.hardDeleteVictim(999);
    }

    // =========================================================================
    //  addMedicalRecord
    // =========================================================================

    @Test
    public void testAddMedicalRecord_validRecord_appearsInVictimMedicalRecords() {
        service.addVictim(victim);
        Location loc = new Location(1, "Clinic", "123 St");
        MedicalRecord record = new MedicalRecord(loc, "Treatment", LocalDate.now().minusDays(1));
        service.addMedicalRecord(1, record);
        assertTrue(victim.getMedicalRecords().contains(record));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMedicalRecord_nullRecord_throwsIllegalArgumentException() {
        service.addVictim(victim);
        service.addMedicalRecord(1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMedicalRecord_nonExistingVictimID_throwsIllegalArgumentException() {
        Location loc = new Location(1, "Clinic", "123 St");
        MedicalRecord record = new MedicalRecord(loc, "Treatment", LocalDate.now().minusDays(1));
        service.addMedicalRecord(999, record);
    }

    // =========================================================================
    //  addFamilyConnection
    // =========================================================================

    @Test
    public void testAddFamilyConnection_validRelation_appearsInVictimConnections() {
        service.addVictim(victim);
        DisasterVictim other = new DisasterVictim(2, "Bob", LocalDate.now());
        FamilyRelation rel = new FamilyRelation(victim, "sibling", other);
        service.addFamilyConnection(1, rel);
        assertTrue(victim.getFamilyConnections().contains(rel));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFamilyConnection_nullRelation_throwsIllegalArgumentException() {
        service.addVictim(victim);
        service.addFamilyConnection(1, null);
    }

    // =========================================================================
    //  addRequirement / removeRequirement
    // =========================================================================

    @Test
    public void testAddRequirement_validRequirement_appearsInVictimRequirements() {
        service.addVictim(victim);
        VictimRequirement req = new VictimRequirement(1, "dietary restrictions", "halal");
        service.addRequirement(1, req);
        assertTrue(victim.getRequirements().contains(req));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddRequirement_nullRequirement_throwsIllegalArgumentException() {
        service.addVictim(victim);
        service.addRequirement(1, null);
    }

    @Test
    public void testRemoveRequirement_existingType_removedFromVictimRequirements() {
        service.addVictim(victim);
        VictimRequirement req = new VictimRequirement(1, "dietary restrictions", "halal");
        service.addRequirement(1, req);
        service.removeRequirement(1, "dietary restrictions");
        assertTrue(victim.getRequirements().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveRequirement_nullType_throwsIllegalArgumentException() {
        service.addVictim(victim);
        service.removeRequirement(1, null);
    }

    // =========================================================================
    //  addSkill / removeSkill
    // =========================================================================

    @Test
    public void testAddSkill_validSkill_appearsInVictimSkills() {
        service.addVictim(victim);
        LanguageSkill skill = new LanguageSkill(1, 1, "beginner", "French", true, false);
        service.addSkill(1, skill);
        assertTrue(victim.getSkills().contains(skill));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSkill_nullSkill_throwsIllegalArgumentException() {
        service.addVictim(victim);
        service.addSkill(1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSkill_nonExistingVictimID_throwsIllegalArgumentException() {
        LanguageSkill skill = new LanguageSkill(1, 999, "beginner", "French", true, false);
        service.addSkill(999, skill);
    }

    @Test
    public void testRemoveSkill_existingSkill_removedFromVictimSkills() {
        service.addVictim(victim);
        LanguageSkill skill = new LanguageSkill(5, 1, "advanced", "Arabic", true, true);
        service.addSkill(1, skill);
        service.removeSkill(1, 5);
        assertTrue(victim.getSkills().isEmpty());
    }
}