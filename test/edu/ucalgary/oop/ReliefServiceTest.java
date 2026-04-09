package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

/**
 * Unit tests for ReliefService.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class ReliefServiceTest {

    private Inquirer       inquirer;
    private DisasterVictim victim;
    private Location       location;
    private LocalDate      pastDate;
    private ReliefService  inquiry;

    @Before
    public void setUp() {
        pastDate = LocalDate.now().minusDays(1);
        inquirer = new Inquirer(1, "Jane", "Doe", "403-555-0100", "Searching for husband");
        victim   = new DisasterVictim(1, "Alice", LocalDate.now());
        location = new Location(1, "Shelter A", "123 Main St");
        inquiry  = new ReliefService(1, inquirer, victim, pastDate, "Last seen near bridge", location);
    }

    @Test
    public void testConstructor_validArgs_setsInquiryID() {
        assertEquals(1, inquiry.getInquiryID());
    }

    @Test
    public void testConstructor_validArgs_setsInquirer() {
        assertEquals(inquirer, inquiry.getInquirer());
    }

    @Test
    public void testConstructor_validArgs_setsMissingPerson() {
        assertEquals(victim, inquiry.getMissingPerson());
    }

    @Test
    public void testConstructor_validArgs_setsDateOfInquiry() {
        assertEquals(pastDate, inquiry.getDateOfInquiry());
    }

    @Test
    public void testConstructor_validArgs_setsInfoProvided() {
        assertEquals("Last seen near bridge", inquiry.getInfoProvided());
    }

    @Test
    public void testConstructor_validArgs_setsLastKnownLocation() {
        assertEquals(location, inquiry.getLastKnownLocation());
    }

    @Test
    public void testConstructor_nullLocation_lastKnownLocationIsNull() {
        ReliefService rs = new ReliefService(2, inquirer, victim, pastDate, "info", null);
        assertNull(rs.getLastKnownLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_zeroInquiryID_throwsIllegalArgumentException() {
        new ReliefService(0, inquirer, victim, pastDate, "info", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullInquirer_throwsIllegalArgumentException() {
        new ReliefService(1, null, victim, pastDate, "info", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullMissingPerson_throwsIllegalArgumentException() {
        new ReliefService(1, inquirer, null, pastDate, "info", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullDateOfInquiry_throwsIllegalArgumentException() {
        new ReliefService(1, inquirer, victim, null, "info", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_futureDateOfInquiry_throwsIllegalArgumentException() {
        new ReliefService(1, inquirer, victim, LocalDate.now().plusDays(1), "info", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullInfoProvided_throwsIllegalArgumentException() {
        new ReliefService(1, inquirer, victim, pastDate, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankInfoProvided_throwsIllegalArgumentException() {
        new ReliefService(1, inquirer, victim, pastDate, "  ", null);
    }

    @Test
    public void testSetInquirer_validInquirer_updatesInquirer() {
        Inquirer newInquirer = new Inquirer(2, "John", "Smith", "403-555-0200", "extra info");
        inquiry.setInquirer(newInquirer);
        assertEquals(newInquirer, inquiry.getInquirer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInquirer_nullValue_throwsIllegalArgumentException() {
        inquiry.setInquirer(null);
    }

    @Test
    public void testSetMissingPerson_validVictim_updatesMissingPerson() {
        DisasterVictim newVictim = new DisasterVictim(2, "Bob", LocalDate.now());
        inquiry.setMissingPerson(newVictim);
        assertEquals(newVictim, inquiry.getMissingPerson());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMissingPerson_nullValue_throwsIllegalArgumentException() {
        inquiry.setMissingPerson(null);
    }

    @Test
    public void testSetDateOfInquiry_pastDate_updatesDate() {
        LocalDate newDate = LocalDate.now().minusDays(5);
        inquiry.setDateOfInquiry(newDate);
        assertEquals(newDate, inquiry.getDateOfInquiry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDateOfInquiry_futureDate_throwsIllegalArgumentException() {
        inquiry.setDateOfInquiry(LocalDate.now().plusDays(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDateOfInquiry_nullValue_throwsIllegalArgumentException() {
        inquiry.setDateOfInquiry(null);
    }

    @Test
    public void testSetInfoProvided_validText_updatesInfoProvided() {
        inquiry.setInfoProvided("Updated information");
        assertEquals("Updated information", inquiry.getInfoProvided());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInfoProvided_blankValue_throwsIllegalArgumentException() {
        inquiry.setInfoProvided("  ");
    }

    @Test
    public void testSetLastKnownLocation_nullValue_setsLocationToNull() {
        inquiry.setLastKnownLocation(null);
        assertNull(inquiry.getLastKnownLocation());
    }

    @Test
    public void testGetLogDetails_containsInquirerName() {
        assertTrue(inquiry.getLogDetails().contains("Jane"));
    }

    @Test
    public void testGetLogDetails_containsMissingPersonName() {
        assertTrue(inquiry.getLogDetails().contains("Alice"));
    }

    @Test
    public void testGetLogDetails_containsLocationName() {
        assertTrue(inquiry.getLogDetails().contains("Shelter A"));
    }

    @Test
    public void testGetLogDetails_noKnownLocation_containsUnknown() {
        ReliefService rs = new ReliefService(2, inquirer, victim, pastDate, "info", null);
        assertTrue(rs.getLogDetails().contains("Unknown"));
    }
}