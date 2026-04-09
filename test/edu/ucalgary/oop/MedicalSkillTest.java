package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

/**
 * Unit tests for MedicalSkill.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class MedicalSkillTest {

    private MedicalSkill skill;
    private LocalDate    expiry;

    @Before
    public void setUp() {
        expiry = LocalDate.now().plusYears(2);
        skill  = new MedicalSkill(1, 1, "advanced", "first-aid", expiry);
    }

    @Test
    public void testConstructor_validArgs_setsCertificationType() {
        assertEquals("first-aid", skill.getCertificationType());
    }

    @Test
    public void testConstructor_validArgs_setsCertificationExpiryDate() {
        assertEquals(expiry, skill.getCertificationExpiryDate());
    }

    @Test
    public void testConstructor_validArgs_categoryIsMedical() {
        assertEquals("Medical", skill.getCategory());
    }

    @Test
    public void testConstructor_caseInsensitiveType_normalisesToLowerCase() {
        MedicalSkill s = new MedicalSkill(2, 1, "beginner", "COUNSELING", expiry);
        assertEquals("counseling", s.getCertificationType());
    }

    @Test
    public void testConstructor_nursingType_isAccepted() {
        MedicalSkill s = new MedicalSkill(2, 1, "intermediate", "nursing", expiry);
        assertEquals("nursing", s.getCertificationType());
    }

    @Test
    public void testConstructor_doctorType_isAccepted() {
        MedicalSkill s = new MedicalSkill(2, 1, "advanced", "doctor", expiry);
        assertEquals("doctor", s.getCertificationType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullCertificationType_throwsIllegalArgumentException() {
        new MedicalSkill(1, 1, "advanced", null, expiry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankCertificationType_throwsIllegalArgumentException() {
        new MedicalSkill(1, 1, "advanced", "  ", expiry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_invalidCertificationType_throwsIllegalArgumentException() {
        new MedicalSkill(1, 1, "advanced", "CPR", expiry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullExpiryDate_throwsIllegalArgumentException() {
        new MedicalSkill(1, 1, "advanced", "first-aid", null);
    }

    @Test
    public void testSetCertificationType_validType_updatesType() {
        skill.setCertificationType("nursing");
        assertEquals("nursing", skill.getCertificationType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCertificationType_invalidType_throwsIllegalArgumentException() {
        skill.setCertificationType("EMT");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCertificationType_nullValue_throwsIllegalArgumentException() {
        skill.setCertificationType(null);
    }

    @Test
    public void testSetCertificationExpiryDate_validDate_updatesDate() {
        LocalDate newDate = LocalDate.now().plusYears(5);
        skill.setCertificationExpiryDate(newDate);
        assertEquals(newDate, skill.getCertificationExpiryDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCertificationExpiryDate_nullValue_throwsIllegalArgumentException() {
        skill.setCertificationExpiryDate(null);
    }
}