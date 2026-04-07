package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

/**
 * Unit tests for DisasterVictim.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class DisasterVictimTest {

    private static final int       VALID_ID   = 1;
    private static final String    FIRST_NAME = "Alice";
    private static final LocalDate ENTRY_DATE = LocalDate.of(2024, 1, 15);

    private DisasterVictim victim;
    private Location       location;

    @Before
    public void setUp() {
        victim   = new DisasterVictim(VALID_ID, FIRST_NAME, ENTRY_DATE);
        location = new Location(1, "Shelter A", "123 Main St");
    }

    // =========================================================================
    //  Constructor – base: valid args
    // =========================================================================

    @Test
    public void testBaseConstructor_validArgs_setsVictimID() {
        assertEquals(VALID_ID, victim.getVictimID());
    }

    @Test
    public void testBaseConstructor_validArgs_setsFirstName() {
        assertEquals(FIRST_NAME, victim.getFirstName());
    }

    @Test
    public void testBaseConstructor_validArgs_setsEntryDate() {
        assertEquals(ENTRY_DATE, victim.getEntryDate());
    }

    @Test
    public void testBaseConstructor_validArgs_notSoftDeletedByDefault() {
        assertFalse(victim.isSoftDeleted());
    }

    @Test
    public void testBaseConstructor_validArgs_personalBelongingsEmpty() {
        assertTrue(victim.getPersonalBelongings().isEmpty());
    }

    @Test
    public void testBaseConstructor_validArgs_medicalRecordsEmpty() {
        assertTrue(victim.getMedicalRecords().isEmpty());
    }

    @Test
    public void testBaseConstructor_validArgs_familyConnectionsEmpty() {
        assertTrue(victim.getFamilyConnections().isEmpty());
    }

    @Test
    public void testBaseConstructor_validArgs_requirementsEmpty() {
        assertTrue(victim.getRequirements().isEmpty());
    }

    @Test
    public void testBaseConstructor_validArgs_skillsEmpty() {
        assertTrue(victim.getSkills().isEmpty());
    }

    // =========================================================================
    //  Constructor – base: invalid args
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testBaseConstructor_zeroID_throwsIllegalArgumentException() {
        new DisasterVictim(0, FIRST_NAME, ENTRY_DATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseConstructor_negativeID_throwsIllegalArgumentException() {
        new DisasterVictim(-5, FIRST_NAME, ENTRY_DATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseConstructor_nullFirstName_throwsIllegalArgumentException() {
        new DisasterVictim(VALID_ID, null, ENTRY_DATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseConstructor_blankFirstName_throwsIllegalArgumentException() {
        new DisasterVictim(VALID_ID, "   ", ENTRY_DATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseConstructor_nullEntryDate_throwsIllegalArgumentException() {
        new DisasterVictim(VALID_ID, FIRST_NAME, null);
    }

    // =========================================================================
    //  Constructor – with date of birth
    // =========================================================================

    @Test
    public void testDobConstructor_validDob_setsDateOfBirth() {
        LocalDate dob = LocalDate.of(1990, 6, 1);
        DisasterVictim v = new DisasterVictim(2, "Bob", ENTRY_DATE, dob);
        assertEquals(dob, v.getDateOfBirth());
    }

    @Test
    public void testDobConstructor_validDob_approximateAgeIsNull() {
        LocalDate dob = LocalDate.of(1990, 6, 1);
        DisasterVictim v = new DisasterVictim(2, "Bob", ENTRY_DATE, dob);
        assertNull(v.getApproximateAge());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDobConstructor_dobAfterEntryDate_throwsIllegalArgumentException() {
        new DisasterVictim(2, "Bob", ENTRY_DATE, ENTRY_DATE.plusDays(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDobConstructor_nullDob_throwsIllegalArgumentException() {
        new DisasterVictim(2, "Bob", ENTRY_DATE, (LocalDate) null);
    }

    // =========================================================================
    //  Constructor – with approximate age
    // =========================================================================

    @Test
    public void testAgeConstructor_validAge_setsApproximateAge() {
        DisasterVictim v = new DisasterVictim(3, "Carol", ENTRY_DATE, 30);
        assertEquals(Integer.valueOf(30), v.getApproximateAge());
    }

    @Test
    public void testAgeConstructor_validAge_dateOfBirthIsNull() {
        DisasterVictim v = new DisasterVictim(3, "Carol", ENTRY_DATE, 30);
        assertNull(v.getDateOfBirth());
    }

    @Test
    public void testAgeConstructor_boundaryAge1_isValid() {
        DisasterVictim v = new DisasterVictim(4, "Dave", ENTRY_DATE, 1);
        assertEquals(Integer.valueOf(1), v.getApproximateAge());
    }

    @Test
    public void testAgeConstructor_boundaryAge150_isValid() {
        DisasterVictim v = new DisasterVictim(4, "Dave", ENTRY_DATE, 150);
        assertEquals(Integer.valueOf(150), v.getApproximateAge());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAgeConstructor_zeroAge_throwsIllegalArgumentException() {
        new DisasterVictim(3, "Carol", ENTRY_DATE, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAgeConstructor_negativeAge_throwsIllegalArgumentException() {
        new DisasterVictim(3, "Carol", ENTRY_DATE, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAgeConstructor_ageBeyond150_throwsIllegalArgumentException() {
        new DisasterVictim(3, "Carol", ENTRY_DATE, 151);
    }

    // =========================================================================
    //  Setters – basic fields
    // =========================================================================

    @Test
    public void testSetFirstName_validName_updatesFirstName() {
        victim.setFirstName("Alicia");
        assertEquals("Alicia", victim.getFirstName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFirstName_nullValue_throwsIllegalArgumentException() {
        victim.setFirstName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFirstName_blankValue_throwsIllegalArgumentException() {
        victim.setFirstName("  ");
    }

    @Test
    public void testSetLastName_validName_updatesLastName() {
        victim.setLastName("Smith");
        assertEquals("Smith", victim.getLastName());
    }

    @Test
    public void testSetComments_validText_updatesComments() {
        victim.setComments("Needs wheelchair access");
        assertEquals("Needs wheelchair access", victim.getComments());
    }

    // =========================================================================
    //  Gender – valid values
    // =========================================================================

    @Test
    public void testSetGender_man_normalisesToMan() {
        victim.setGender("man");
        assertEquals("Man", victim.getGender());
    }

    @Test
    public void testSetGender_womanUpperCase_normalisesToWoman() {
        victim.setGender("WOMAN");
        assertEquals("Woman", victim.getGender());
    }

    @Test
    public void testSetGender_nonBinaryPerson_normalisesCorrectly() {
        victim.setGender("non-binary person");
        assertEquals("Non-binary person", victim.getGender());
    }

    @Test
    public void testSetGender_pleaseSpecify_normalisesCorrectly() {
        victim.setGender("please specify");
        assertEquals("Please Specify", victim.getGender());
    }

    @Test
    public void testSetGender_boyWithNoDob_normalisesToBoy() {
        victim.setGender("boy");
        assertEquals("Boy", victim.getGender());
    }

    // =========================================================================
    //  Gender – invalid values
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testSetGender_unrecognisedValue_throwsIllegalArgumentException() {
        victim.setGender("helicopter");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGender_nullValue_throwsIllegalArgumentException() {
        victim.setGender(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGender_blankValue_throwsIllegalArgumentException() {
        victim.setGender("  ");
    }

    // =========================================================================
    //  Gender – DOB-based child / adult enforcement
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testSetGender_manOnChildByDob_throwsIllegalArgumentException() {
        DisasterVictim child = new DisasterVictim(10, "Tiny", ENTRY_DATE,
                LocalDate.of(2020, 1, 1));
        child.setGender("Man");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGender_womanOnChildByDob_throwsIllegalArgumentException() {
        DisasterVictim child = new DisasterVictim(11, "Tiny", ENTRY_DATE,
                LocalDate.of(2020, 1, 1));
        child.setGender("Woman");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGender_boyOnAdultByDob_throwsIllegalArgumentException() {
        DisasterVictim adult = new DisasterVictim(12, "Tom", ENTRY_DATE,
                LocalDate.of(2000, 1, 1));
        adult.setGender("Boy");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGender_girlOnAdultByDob_throwsIllegalArgumentException() {
        DisasterVictim adult = new DisasterVictim(13, "Tina", ENTRY_DATE,
                LocalDate.of(2000, 1, 1));
        adult.setGender("Girl");
    }

    @Test
    public void testSetGender_boyOnChildByDob_isAccepted() {
        DisasterVictim child = new DisasterVictim(14, "Tiny", ENTRY_DATE,
                LocalDate.of(2020, 1, 1));
        child.setGender("boy");
        assertEquals("Boy", child.getGender());
    }

    @Test
    public void testSetGender_manOnAdultByDob_isAccepted() {
        DisasterVictim adult = new DisasterVictim(15, "Tom", ENTRY_DATE,
                LocalDate.of(2000, 1, 1));
        adult.setGender("man");
        assertEquals("Man", adult.getGender());
    }

    // =========================================================================
    //  Personal belongings
    // =========================================================================

    @Test
    public void testAddPersonalBelonging_validSupply_appearsInList() {
        Supply s = new Supply(1, "Blanket", 1);
        victim.addPersonalBelonging(s);
        assertTrue(victim.getPersonalBelongings().contains(s));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPersonalBelonging_nullSupply_throwsIllegalArgumentException() {
        victim.addPersonalBelonging(null);
    }

    @Test
    public void testRemovePersonalBelonging_existingSupply_removedFromList() {
        Supply s = new Supply(2, "Towel", 1);
        victim.addPersonalBelonging(s);
        victim.removePersonalBelonging(s);
        assertFalse(victim.getPersonalBelongings().contains(s));
    }

    // =========================================================================
    //  Medical records
    // =========================================================================

    @Test
    public void testAddMedicalRecord_validRecord_appearsInList() {
        MedicalRecord rec = new MedicalRecord(location, "Bandage applied",
                LocalDate.of(2024, 1, 10));
        victim.addMedicalRecord(rec);
        assertTrue(victim.getMedicalRecords().contains(rec));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMedicalRecord_nullRecord_throwsIllegalArgumentException() {
        victim.addMedicalRecord(null);
    }

    // =========================================================================
    //  Family connections
    // =========================================================================

    @Test
    public void testAddFamilyConnection_validRelation_appearsInList() {
        DisasterVictim other = new DisasterVictim(2, "Bob", ENTRY_DATE);
        FamilyRelation rel   = new FamilyRelation(victim, "sibling", other);
        victim.addFamilyConnection(rel);
        assertTrue(victim.getFamilyConnections().contains(rel));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFamilyConnection_nullRelation_throwsIllegalArgumentException() {
        victim.addFamilyConnection(null);
    }

    @Test
    public void testRemoveFamilyConnection_existingRelation_removedFromList() {
        DisasterVictim other = new DisasterVictim(3, "Carol", ENTRY_DATE);
        FamilyRelation rel   = new FamilyRelation(victim, "parent", other);
        victim.addFamilyConnection(rel);
        victim.removeFamilyConnection(rel);
        assertFalse(victim.getFamilyConnections().contains(rel));
    }

    // =========================================================================
    //  Requirements
    // =========================================================================

    @Test
    public void testAddRequirement_validRequirement_appearsInList() {
        VictimRequirement req = new VictimRequirement(VALID_ID, "dietary restrictions", "halal");
        victim.addRequirement(req);
        assertTrue(victim.getRequirements().contains(req));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddRequirement_nullRequirement_throwsIllegalArgumentException() {
        victim.addRequirement(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddRequirement_duplicateType_throwsIllegalArgumentException() {
        victim.addRequirement(new VictimRequirement(VALID_ID, "dietary restrictions", "halal"));
        victim.addRequirement(new VictimRequirement(VALID_ID, "dietary restrictions", "kosher"));
    }

    @Test
    public void testRemoveRequirement_existingType_listIsEmpty() {
        victim.addRequirement(new VictimRequirement(VALID_ID, "dietary restrictions", "halal"));
        victim.removeRequirement("dietary restrictions");
        assertTrue(victim.getRequirements().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveRequirement_nullType_throwsIllegalArgumentException() {
        victim.removeRequirement(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveRequirement_blankType_throwsIllegalArgumentException() {
        victim.removeRequirement("  ");
    }

    // =========================================================================
    //  Skills – Language
    // =========================================================================

    @Test
    public void testAddSkill_validLanguageSkill_appearsInList() {
        LanguageSkill skill = new LanguageSkill(1, VALID_ID, "intermediate", "French", true, false);
        victim.addSkill(skill);
        assertTrue(victim.getSkills().contains(skill));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSkill_nullSkill_throwsIllegalArgumentException() {
        victim.addSkill(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSkill_duplicateLanguage_throwsIllegalArgumentException() {
        victim.addSkill(new LanguageSkill(1, VALID_ID, "beginner",     "French", true,  false));
        victim.addSkill(new LanguageSkill(2, VALID_ID, "intermediate", "French", false, true));
    }

    @Test
    public void testAddSkill_differentLanguages_skillCountIsTwo() {
        victim.addSkill(new LanguageSkill(1, VALID_ID, "beginner", "French",  true, false));
        victim.addSkill(new LanguageSkill(2, VALID_ID, "beginner", "Spanish", true, false));
        assertEquals(2, victim.getSkills().size());
    }

    @Test
    public void testRemoveSkill_byID_listIsEmpty() {
        LanguageSkill skill = new LanguageSkill(5, VALID_ID, "advanced", "Arabic", true, true);
        victim.addSkill(skill);
        victim.removeSkill(5);
        assertTrue(victim.getSkills().isEmpty());
    }

    // =========================================================================
    //  Skills – Medical
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testAddSkill_duplicateMedicalCertification_throwsIllegalArgumentException() {
        LocalDate expiry = LocalDate.now().plusYears(1);
        victim.addSkill(new MedicalSkill(1, VALID_ID, "beginner",     "first-aid", expiry));
        victim.addSkill(new MedicalSkill(2, VALID_ID, "intermediate", "first-aid", expiry));
    }

    @Test
    public void testAddSkill_differentMedicalCertifications_skillCountIsTwo() {
        LocalDate expiry = LocalDate.now().plusYears(1);
        victim.addSkill(new MedicalSkill(1, VALID_ID, "beginner", "first-aid", expiry));
        victim.addSkill(new MedicalSkill(2, VALID_ID, "advanced",  "nursing",   expiry));
        assertEquals(2, victim.getSkills().size());
    }

    // =========================================================================
    //  Skills – Trade
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testAddSkill_duplicateTradeType_throwsIllegalArgumentException() {
        victim.addSkill(new TradeSkill(1, VALID_ID, "beginner",     "carpentry"));
        victim.addSkill(new TradeSkill(2, VALID_ID, "intermediate", "carpentry"));
    }

    @Test
    public void testAddSkill_differentTradeTypes_skillCountIsTwo() {
        victim.addSkill(new TradeSkill(1, VALID_ID, "beginner", "carpentry"));
        victim.addSkill(new TradeSkill(2, VALID_ID, "beginner", "plumbing"));
        assertEquals(2, victim.getSkills().size());
    }

    @Test
    public void testAddSkill_languageAndTradeSkillsTogether_skillCountIsTwo() {
        victim.addSkill(new LanguageSkill(1, VALID_ID, "beginner", "French", true, false));
        victim.addSkill(new TradeSkill(2, VALID_ID, "intermediate", "electricity"));
        assertEquals(2, victim.getSkills().size());
    }

    // =========================================================================
    //  Soft delete
    // =========================================================================

    @Test
    public void testSoftDelete_beforeCalling_isSoftDeletedIsFalse() {
        assertFalse(victim.isSoftDeleted());
    }

    @Test
    public void testSoftDelete_afterCalling_isSoftDeletedIsTrue() {
        victim.softDelete();
        assertTrue(victim.isSoftDeleted());
    }
}