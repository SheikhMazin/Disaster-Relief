package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

/**
 * Unit tests for MedicalRecord.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class MedicalRecordTest {

    private Location      location;
    private MedicalRecord record;
    private LocalDate     validDate;

    @Before
    public void setUp() {
        location  = new Location(1, "Medical Tent", "100 Relief Rd");
        validDate = LocalDate.now().minusDays(1);
        record    = new MedicalRecord(location, "Wound cleaned and dressed", validDate);
    }

    // =========================================================================
    //  Constructor – valid args
    // =========================================================================

    @Test
    public void testConstructor_validArgs_setsLocation() {
        assertEquals(location, record.getLocation());
    }

    @Test
    public void testConstructor_validArgs_setsTreatmentDetails() {
        assertEquals("Wound cleaned and dressed", record.getTreatmentDetails());
    }

    @Test
    public void testConstructor_validArgs_setsDateOfTreatment() {
        assertEquals(validDate, record.getDateOfTreatment());
    }

    // =========================================================================
    //  Constructor – invalid args
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullLocation_throwsIllegalArgumentException() {
        new MedicalRecord(null, "Some treatment", validDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullTreatmentDetails_throwsIllegalArgumentException() {
        new MedicalRecord(location, null, validDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankTreatmentDetails_throwsIllegalArgumentException() {
        new MedicalRecord(location, "  ", validDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullDateOfTreatment_throwsIllegalArgumentException() {
        new MedicalRecord(location, "Treatment", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_futureDateOfTreatment_throwsIllegalArgumentException() {
        new MedicalRecord(location, "Treatment", LocalDate.now().plusDays(1));
    }

    @Test
    public void testConstructor_todayAsDateOfTreatment_isAccepted() {
        MedicalRecord r = new MedicalRecord(location, "Treatment today", LocalDate.now());
        assertEquals(LocalDate.now(), r.getDateOfTreatment());
    }

    // =========================================================================
    //  setLocation
    // =========================================================================

    @Test
    public void testSetLocation_validLocation_updatesLocation() {
        Location newLoc = new Location(2, "Clinic B", "200 Health Ave");
        record.setLocation(newLoc);
        assertEquals(newLoc, record.getLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLocation_nullLocation_throwsIllegalArgumentException() {
        record.setLocation(null);
    }

    // =========================================================================
    //  setTreatmentDetails
    // =========================================================================

    @Test
    public void testSetTreatmentDetails_validText_updatesTreatmentDetails() {
        record.setTreatmentDetails("Stitches applied");
        assertEquals("Stitches applied", record.getTreatmentDetails());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTreatmentDetails_nullValue_throwsIllegalArgumentException() {
        record.setTreatmentDetails(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTreatmentDetails_blankValue_throwsIllegalArgumentException() {
        record.setTreatmentDetails("");
    }

    // =========================================================================
    //  setDateOfTreatment
    // =========================================================================

    @Test
    public void testSetDateOfTreatment_pastDate_updatesDateOfTreatment() {
        LocalDate newDate = LocalDate.now().minusDays(5);
        record.setDateOfTreatment(newDate);
        assertEquals(newDate, record.getDateOfTreatment());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDateOfTreatment_futureDate_throwsIllegalArgumentException() {
        record.setDateOfTreatment(LocalDate.now().plusDays(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDateOfTreatment_nullDate_throwsIllegalArgumentException() {
        record.setDateOfTreatment(null);
    }
}