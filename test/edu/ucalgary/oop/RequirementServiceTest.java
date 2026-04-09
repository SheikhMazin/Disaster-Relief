package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for RequirementService.
 * Uses MockDataRepository — no database connection required.
 * Tests cover constructor validation and the in-memory query methods.
 * Loading from .ser file is not tested as it requires an external file.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class RequirementServiceTest {

    private RequirementService service;

    @Before
    public void setUp() {
        service = new RequirementService(new MockDataRepository());
    }

    // =========================================================================
    //  Constructor
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullRepository_throwsIllegalArgumentException() {
        new RequirementService(null);
    }

    // =========================================================================
    //  getAvailableRequirementTypes (before loading — should return empty set)
    // =========================================================================

    @Test
    public void testGetAvailableRequirementTypes_beforeLoading_returnsEmptySet() {
        assertTrue(service.getAvailableRequirementTypes().isEmpty());
    }

    // =========================================================================
    //  getOptionsForType
    // =========================================================================

    @Test
    public void testGetOptionsForType_unknownType_returnsEmptySet() {
        assertTrue(service.getOptionsForType("nonexistent type").isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionsForType_nullType_throwsIllegalArgumentException() {
        service.getOptionsForType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionsForType_blankType_throwsIllegalArgumentException() {
        service.getOptionsForType("  ");
    }

    // =========================================================================
    //  isValidOption
    // =========================================================================

    @Test
    public void testIsValidOption_unknownType_returnsFalse() {
        assertFalse(service.isValidOption("nonexistent type", "some option"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidOption_nullRequirementType_throwsIllegalArgumentException() {
        service.isValidOption(null, "halal");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidOption_blankRequirementType_throwsIllegalArgumentException() {
        service.isValidOption("  ", "halal");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidOption_nullSelectedOption_throwsIllegalArgumentException() {
        service.isValidOption("dietary restrictions", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsValidOption_blankSelectedOption_throwsIllegalArgumentException() {
        service.isValidOption("dietary restrictions", "  ");
    }

    // =========================================================================
    //  loadAvailableRequirements
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testLoadAvailableRequirements_nullFileName_throwsIllegalArgumentException()
            throws Exception {
        service.loadAvailableRequirements(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadAvailableRequirements_blankFileName_throwsIllegalArgumentException()
            throws Exception {
        service.loadAvailableRequirements("  ");
    }
}