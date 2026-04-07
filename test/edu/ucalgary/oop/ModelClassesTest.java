package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

/**
 * Unit tests for Inquirer, ReliefService, VictimRequirement,
 * LanguageSkill, MedicalSkill, and TradeSkill.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class ModelClassesTest {

    private Inquirer       inquirer;
    private DisasterVictim victim;
    private Location       location;
    private LocalDate      pastDate;

    @Before
    public void setUp() {
        pastDate = LocalDate.now().minusDays(1);
        inquirer = new Inquirer(1, "Jane", "Doe", "403-555-0100", "Searching for husband");
        victim   = new DisasterVictim(1, "Alice", LocalDate.now());
        location = new Location(1, "Shelter A", "123 Main St");
    }

    // =========================================================================
    //  Inquirer – constructor valid
    // =========================================================================

    @Test
    public void testInquirer_validArgs_setsInquirerID() {
        assertEquals(1, inquirer.getInquirerID());
    }

    @Test
    public void testInquirer_validArgs_setsFirstName() {
        assertEquals("Jane", inquirer.getFirstName());
    }

    @Test
    public void testInquirer_validArgs_setsLastName() {
        assertEquals("Doe", inquirer.getLastName());
    }

    @Test
    public void testInquirer_validArgs_setsServicesPhone() {
        assertEquals("403-555-0100", inquirer.getServicesPhoneNum());
    }

    @Test
    public void testInquirer_validArgs_setsInfo() {
        assertEquals("Searching for husband", inquirer.getInfo());
    }

    // =========================================================================
    //  Inquirer – constructor invalid
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testInquirer_zeroID_throwsIllegalArgumentException() {
        new Inquirer(0, "Jane", "Doe", "403-555-0100", "info");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInquirer_nullFirstName_throwsIllegalArgumentException() {
        new Inquirer(1, null, "Doe", "403-555-0100", "info");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInquirer_blankLastName_throwsIllegalArgumentException() {
        new Inquirer(1, "Jane", "  ", "403-555-0100", "info");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInquirer_nullPhone_throwsIllegalArgumentException() {
        new Inquirer(1, "Jane", "Doe", null, "info");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInquirer_blankInfo_throwsIllegalArgumentException() {
        new Inquirer(1, "Jane", "Doe", "403-555-0100", "");
    }

    // =========================================================================
    //  ReliefService – constructor valid
    // =========================================================================

    @Test
    public void testReliefService_validArgs_setsInquiryID() {
        ReliefService rs = new ReliefService(1, inquirer, victim, pastDate, "Last seen near bridge", location);
        assertEquals(1, rs.getInquiryID());
    }

    @Test
    public void testReliefService_validArgs_setsInquirer() {
        ReliefService rs = new ReliefService(1, inquirer, victim, pastDate, "info", location);
        assertEquals(inquirer, rs.getInquirer());
    }

    @Test
    public void testReliefService_validArgs_setsMissingPerson() {
        ReliefService rs = new ReliefService(1, inquirer, victim, pastDate, "info", location);
        assertEquals(victim, rs.getMissingPerson());
    }

    @Test
    public void testReliefService_validArgs_setsDateOfInquiry() {
        ReliefService rs = new ReliefService(1, inquirer, victim, pastDate, "info", location);
        assertEquals(pastDate, rs.getDateOfInquiry());
    }

    @Test
    public void testReliefService_validArgs_setsInfoProvided() {
        ReliefService rs = new ReliefService(1, inquirer, victim, pastDate, "Last seen near bridge", location);
        assertEquals("Last seen near bridge", rs.getInfoProvided());
    }

    @Test
    public void testReliefService_validArgs_setsLastKnownLocation() {
        ReliefService rs = new ReliefService(1, inquirer, victim, pastDate, "info", location);
        assertEquals(location, rs.getLastKnownLocation());
    }

    @Test
    public void testReliefService_nullLocation_lastKnownLocationIsNull() {
        ReliefService rs = new ReliefService(2, inquirer, victim, pastDate, "No location info", null);
        assertNull(rs.getLastKnownLocation());
    }

    // =========================================================================
    //  ReliefService – constructor invalid
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testReliefService_zeroInquiryID_throwsIllegalArgumentException() {
        new ReliefService(0, inquirer, victim, pastDate, "info", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReliefService_nullInquirer_throwsIllegalArgumentException() {
        new ReliefService(1, null, victim, pastDate, "info", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReliefService_nullMissingPerson_throwsIllegalArgumentException() {
        new ReliefService(1, inquirer, null, pastDate, "info", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReliefService_nullDateOfInquiry_throwsIllegalArgumentException() {
        new ReliefService(1, inquirer, victim, null, "info", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReliefService_futureDateOfInquiry_throwsIllegalArgumentException() {
        new ReliefService(1, inquirer, victim, LocalDate.now().plusDays(1), "info", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReliefService_blankInfoProvided_throwsIllegalArgumentException() {
        new ReliefService(1, inquirer, victim, pastDate, "  ", null);
    }

    // =========================================================================
    //  ReliefService – setters
    // =========================================================================

    @Test
    public void testReliefService_setInquirer_updatesInquirer() {
        ReliefService rs = new ReliefService(1, inquirer, victim, pastDate, "info", null);
        Inquirer newInquirer = new Inquirer(2, "John", "Smith", "403-555-0200", "extra info");
        rs.setInquirer(newInquirer);
        assertEquals(newInquirer, rs.getInquirer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReliefService_setNullInquirer_throwsIllegalArgumentException() {
        ReliefService rs = new ReliefService(1, inquirer, victim, pastDate, "info", null);
        rs.setInquirer(null);
    }

    @Test
    public void testReliefService_setInfoProvided_updatesInfoProvided() {
        ReliefService rs = new ReliefService(1, inquirer, victim, pastDate, "info", null);
        rs.setInfoProvided("Updated info");
        assertEquals("Updated info", rs.getInfoProvided());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReliefService_setBlankInfoProvided_throwsIllegalArgumentException() {
        ReliefService rs = new ReliefService(1, inquirer, victim, pastDate, "info", null);
        rs.setInfoProvided("  ");
    }

    @Test
    public void testReliefService_setLastKnownLocationToNull_returnsNull() {
        ReliefService rs = new ReliefService(1, inquirer, victim, pastDate, "info", location);
        rs.setLastKnownLocation(null);
        assertNull(rs.getLastKnownLocation());
    }

    // =========================================================================
    //  ReliefService – getLogDetails
    // =========================================================================

    @Test
    public void testReliefService_getLogDetails_containsInquirerName() {
        ReliefService rs = new ReliefService(1, inquirer, victim, pastDate, "Last seen near bridge", location);
        assertTrue(rs.getLogDetails().contains("Jane"));
    }

    @Test
    public void testReliefService_getLogDetails_containsMissingPersonName() {
        ReliefService rs = new ReliefService(1, inquirer, victim, pastDate, "Last seen near bridge", location);
        assertTrue(rs.getLogDetails().contains("Alice"));
    }

    @Test
    public void testReliefService_getLogDetails_containsLocationName() {
        ReliefService rs = new ReliefService(1, inquirer, victim, pastDate, "Last seen near bridge", location);
        assertTrue(rs.getLogDetails().contains("Shelter A"));
    }

    // =========================================================================
    //  VictimRequirement – constructor valid
    // =========================================================================

    @Test
    public void testVictimRequirement_validArgs_setsVictimID() {
        VictimRequirement req = new VictimRequirement(1, "dietary restrictions", "halal");
        assertEquals(1, req.getVictimID());
    }

    @Test
    public void testVictimRequirement_validArgs_setsRequirementType() {
        VictimRequirement req = new VictimRequirement(1, "dietary restrictions", "halal");
        assertEquals("dietary restrictions", req.getRequirementType());
    }

    @Test
    public void testVictimRequirement_validArgs_setsSelectedOption() {
        VictimRequirement req = new VictimRequirement(1, "dietary restrictions", "halal");
        assertEquals("halal", req.getSelectedOption());
    }

    // =========================================================================
    //  VictimRequirement – constructor invalid
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testVictimRequirement_zeroVictimID_throwsIllegalArgumentException() {
        new VictimRequirement(0, "dietary restrictions", "halal");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVictimRequirement_nullRequirementType_throwsIllegalArgumentException() {
        new VictimRequirement(1, null, "halal");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVictimRequirement_blankSelectedOption_throwsIllegalArgumentException() {
        new VictimRequirement(1, "dietary restrictions", "  ");
    }

    // =========================================================================
    //  VictimRequirement – setter
    // =========================================================================

    @Test
    public void testVictimRequirement_setSelectedOption_updatesOption() {
        VictimRequirement req = new VictimRequirement(1, "dietary restrictions", "halal");
        req.setSelectedOption("kosher");
        assertEquals("kosher", req.getSelectedOption());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVictimRequirement_setNullSelectedOption_throwsIllegalArgumentException() {
        VictimRequirement req = new VictimRequirement(1, "dietary restrictions", "halal");
        req.setSelectedOption(null);
    }

    // =========================================================================
    //  LanguageSkill – constructor valid
    // =========================================================================

    @Test
    public void testLanguageSkill_validArgs_setsSkillID() {
        LanguageSkill skill = new LanguageSkill(1, 1, "intermediate", "French", true, false);
        assertEquals(1, skill.getSkillID());
    }

    @Test
    public void testLanguageSkill_validArgs_setsCategory() {
        LanguageSkill skill = new LanguageSkill(1, 1, "intermediate", "French", true, false);
        assertEquals("Language", skill.getCategory());
    }

    @Test
    public void testLanguageSkill_validArgs_setsProficiencyLevel() {
        LanguageSkill skill = new LanguageSkill(1, 1, "intermediate", "French", true, false);
        assertEquals("intermediate", skill.getProficiencyLevel());
    }

    @Test
    public void testLanguageSkill_validArgs_setsLanguageName() {
        LanguageSkill skill = new LanguageSkill(1, 1, "intermediate", "French", true, false);
        assertEquals("French", skill.getLanguageName());
    }

    @Test
    public void testLanguageSkill_validArgs_setsReadWriteTrue() {
        LanguageSkill skill = new LanguageSkill(1, 1, "intermediate", "French", true, false);
        assertTrue(skill.hasReadWrite());
    }

    @Test
    public void testLanguageSkill_validArgs_setsSpeakListenFalse() {
        LanguageSkill skill = new LanguageSkill(1, 1, "intermediate", "French", true, false);
        assertFalse(skill.hasSpeakListen());
    }

    @Test
    public void testLanguageSkill_speakListenOnlyCapability_readWriteIsFalse() {
        LanguageSkill skill = new LanguageSkill(1, 1, "beginner", "Arabic", false, true);
        assertFalse(skill.hasReadWrite());
    }

    @Test
    public void testLanguageSkill_speakListenOnlyCapability_speakListenIsTrue() {
        LanguageSkill skill = new LanguageSkill(1, 1, "beginner", "Arabic", false, true);
        assertTrue(skill.hasSpeakListen());
    }

    // =========================================================================
    //  LanguageSkill – constructor invalid
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testLanguageSkill_nullLanguageName_throwsIllegalArgumentException() {
        new LanguageSkill(1, 1, "beginner", null, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguageSkill_bothCapabilitiesFalse_throwsIllegalArgumentException() {
        new LanguageSkill(1, 1, "beginner", "French", false, false);
    }

    // =========================================================================
    //  LanguageSkill – setters
    // =========================================================================

    @Test
    public void testLanguageSkill_setLanguageName_updatesLanguageName() {
        LanguageSkill skill = new LanguageSkill(1, 1, "beginner", "French", true, true);
        skill.setLanguageName("Spanish");
        assertEquals("Spanish", skill.getLanguageName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguageSkill_setNullLanguageName_throwsIllegalArgumentException() {
        LanguageSkill skill = new LanguageSkill(1, 1, "beginner", "French", true, true);
        skill.setLanguageName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguageSkill_setReadWriteFalseWhenSpeakListenAlreadyFalse_throwsIllegalArgumentException() {
        LanguageSkill skill = new LanguageSkill(1, 1, "beginner", "French", true, false);
        skill.setReadWrite(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguageSkill_setSpeakListenFalseWhenReadWriteAlreadyFalse_throwsIllegalArgumentException() {
        LanguageSkill skill = new LanguageSkill(1, 1, "beginner", "French", false, true);
        skill.setSpeakListen(false);
    }

    // =========================================================================
    //  Skill base – via LanguageSkill
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testSkill_zeroSkillID_throwsIllegalArgumentException() {
        new LanguageSkill(0, 1, "beginner", "French", true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkill_zeroVictimID_throwsIllegalArgumentException() {
        new LanguageSkill(1, 0, "beginner", "French", true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkill_nullProficiencyLevel_throwsIllegalArgumentException() {
        new LanguageSkill(1, 1, null, "French", true, true);
    }

    @Test
    public void testSkill_setProficiencyLevel_updatesProficiencyLevel() {
        LanguageSkill skill = new LanguageSkill(1, 1, "beginner", "French", true, true);
        skill.setProficiencyLevel("advanced");
        assertEquals("advanced", skill.getProficiencyLevel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkill_setBlankProficiencyLevel_throwsIllegalArgumentException() {
        LanguageSkill skill = new LanguageSkill(1, 1, "beginner", "French", true, true);
        skill.setProficiencyLevel("  ");
    }

    // =========================================================================
    //  MedicalSkill – constructor valid
    // =========================================================================

    @Test
    public void testMedicalSkill_validArgs_setsCategory() {
        MedicalSkill skill = new MedicalSkill(1, 1, "advanced", "first-aid", LocalDate.now().plusYears(2));
        assertEquals("Medical", skill.getCategory());
    }

    @Test
    public void testMedicalSkill_validArgs_setsCertificationType() {
        MedicalSkill skill = new MedicalSkill(1, 1, "advanced", "first-aid", LocalDate.now().plusYears(2));
        assertEquals("first-aid", skill.getCertificationType());
    }

    @Test
    public void testMedicalSkill_validArgs_setsCertificationExpiryDate() {
        LocalDate expiry = LocalDate.now().plusYears(2);
        MedicalSkill skill = new MedicalSkill(1, 1, "advanced", "first-aid", expiry);
        assertEquals(expiry, skill.getCertificationExpiryDate());
    }

    @Test
    public void testMedicalSkill_caseInsensitiveCertificationType_normalisesToLowerCase() {
        MedicalSkill skill = new MedicalSkill(1, 1, "beginner", "COUNSELING", LocalDate.now().plusYears(1));
        assertEquals("counseling", skill.getCertificationType());
    }

    // =========================================================================
    //  MedicalSkill – constructor invalid
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testMedicalSkill_nullCertificationType_throwsIllegalArgumentException() {
        new MedicalSkill(1, 1, "advanced", null, LocalDate.now().plusYears(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMedicalSkill_blankCertificationType_throwsIllegalArgumentException() {
        new MedicalSkill(1, 1, "advanced", "  ", LocalDate.now().plusYears(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMedicalSkill_unrecognisedCertificationType_throwsIllegalArgumentException() {
        new MedicalSkill(1, 1, "advanced", "CPR", LocalDate.now().plusYears(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMedicalSkill_nullExpiryDate_throwsIllegalArgumentException() {
        new MedicalSkill(1, 1, "advanced", "nursing", null);
    }

    // =========================================================================
    //  MedicalSkill – setters
    // =========================================================================

    @Test
    public void testMedicalSkill_setCertificationType_updatesType() {
        MedicalSkill skill = new MedicalSkill(1, 1, "intermediate", "first-aid", LocalDate.now().plusYears(1));
        skill.setCertificationType("nursing");
        assertEquals("nursing", skill.getCertificationType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMedicalSkill_setInvalidCertificationType_throwsIllegalArgumentException() {
        MedicalSkill skill = new MedicalSkill(1, 1, "intermediate", "first-aid", LocalDate.now().plusYears(1));
        skill.setCertificationType("EMT");
    }

    @Test
    public void testMedicalSkill_setCertificationExpiryDate_updatesDate() {
        LocalDate original = LocalDate.now().plusYears(1);
        MedicalSkill skill = new MedicalSkill(1, 1, "advanced", "doctor", original);
        LocalDate newExpiry = LocalDate.now().plusYears(3);
        skill.setCertificationExpiryDate(newExpiry);
        assertEquals(newExpiry, skill.getCertificationExpiryDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMedicalSkill_setNullExpiryDate_throwsIllegalArgumentException() {
        MedicalSkill skill = new MedicalSkill(1, 1, "advanced", "doctor", LocalDate.now().plusYears(1));
        skill.setCertificationExpiryDate(null);
    }

    // =========================================================================
    //  TradeSkill – constructor valid
    // =========================================================================

    @Test
    public void testTradeSkill_validArgs_setsCategory() {
        TradeSkill skill = new TradeSkill(1, 1, "beginner", "carpentry");
        assertEquals("Trade", skill.getCategory());
    }

    @Test
    public void testTradeSkill_validArgs_setsTradeType() {
        TradeSkill skill = new TradeSkill(1, 1, "beginner", "carpentry");
        assertEquals("carpentry", skill.getTradeType());
    }

    @Test
    public void testTradeSkill_caseInsensitiveTradeType_normalisesToLowerCase() {
        TradeSkill skill = new TradeSkill(1, 1, "intermediate", "PLUMBING");
        assertEquals("plumbing", skill.getTradeType());
    }

    // =========================================================================
    //  TradeSkill – constructor invalid
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testTradeSkill_nullTradeType_throwsIllegalArgumentException() {
        new TradeSkill(1, 1, "beginner", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTradeSkill_blankTradeType_throwsIllegalArgumentException() {
        new TradeSkill(1, 1, "beginner", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTradeSkill_unrecognisedTradeType_throwsIllegalArgumentException() {
        new TradeSkill(1, 1, "beginner", "welding");
    }

    // =========================================================================
    //  TradeSkill – setter
    // =========================================================================

    @Test
    public void testTradeSkill_setTradeType_updatesTradeType() {
        TradeSkill skill = new TradeSkill(1, 1, "intermediate", "plumbing");
        skill.setTradeType("electricity");
        assertEquals("electricity", skill.getTradeType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTradeSkill_setInvalidTradeType_throwsIllegalArgumentException() {
        TradeSkill skill = new TradeSkill(1, 1, "intermediate", "plumbing");
        skill.setTradeType("electrical");
    }
}