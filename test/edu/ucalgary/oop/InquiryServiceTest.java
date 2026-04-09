package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

/**
 * Unit tests for InquiryService.
 * Uses MockDataRepository — no database connection required.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class InquiryServiceTest {

    private InquiryService service;
    private ReliefService  inquiry;
    private Inquirer       inquirer;
    private DisasterVictim victim;

    @Before
    public void setUp() {
        service  = new InquiryService(new MockDataRepository(), ActionLogger.getInstance());
        inquirer = new Inquirer(1, "Jane", "Doe", "403-555-0100", "Searching for husband");
        victim   = new DisasterVictim(1, "Alice", LocalDate.now());
        inquiry  = new ReliefService(1, inquirer, victim,
                LocalDate.now().minusDays(1), "Last seen near river", null);
    }

    // =========================================================================
    //  Constructor
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullRepository_throwsIllegalArgumentException() {
        new InquiryService(null, ActionLogger.getInstance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullLogger_throwsIllegalArgumentException() {
        new InquiryService(new MockDataRepository(), null);
    }

    // =========================================================================
    //  addInquiry
    // =========================================================================

    @Test
    public void testAddInquiry_validInquiry_appearsInInquiries() {
        service.addInquiry(inquiry);
        assertTrue(service.getInquiries().contains(inquiry));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInquiry_nullInquiry_throwsIllegalArgumentException() {
        service.addInquiry(null);
    }

    // =========================================================================
    //  getInquiries
    // =========================================================================

    @Test
    public void testGetInquiries_emptyService_returnsEmptyList() {
        assertTrue(service.getInquiries().isEmpty());
    }

    @Test
    public void testGetInquiries_afterAddingOne_sizeIsOne() {
        service.addInquiry(inquiry);
        assertEquals(1, service.getInquiries().size());
    }

    // =========================================================================
    //  getInquiriesForVictim
    // =========================================================================

    @Test
    public void testGetInquiriesForVictim_matchingVictimID_returnsInquiry() {
        service.addInquiry(inquiry);
        assertTrue(service.getInquiriesForVictim(1).contains(inquiry));
    }

    @Test
    public void testGetInquiriesForVictim_nonMatchingVictimID_returnsEmptyList() {
        service.addInquiry(inquiry);
        assertTrue(service.getInquiriesForVictim(999).isEmpty());
    }

    @Test
    public void testGetInquiriesForVictim_noInquiries_returnsEmptyList() {
        assertTrue(service.getInquiriesForVictim(1).isEmpty());
    }

    // =========================================================================
    //  updateInquiry
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateInquiry_nullInquiry_throwsIllegalArgumentException() {
        service.updateInquiry(null);
    }
}