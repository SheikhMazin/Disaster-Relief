package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

/**
 * Unit tests for Supply.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class SupplyTest {

    private Supply    nonPerishable;
    private Supply    perishable;
    private LocalDate futureDate;
    private LocalDate pastDate;

    @Before
    public void setUp() {
        futureDate    = LocalDate.now().plusDays(30);
        pastDate      = LocalDate.now().minusDays(1);
        nonPerishable = new Supply(1, "Blanket", 10);
        perishable    = new Supply(2, "Water bottle", 5, true, futureDate);
    }

    // =========================================================================
    //  Constructor – non-perishable
    // =========================================================================

    @Test
    public void testNonPerishableConstructor_validArgs_setsSupplyID() {
        assertEquals(1, nonPerishable.getSupplyID());
    }

    @Test
    public void testNonPerishableConstructor_validArgs_setsType() {
        assertEquals("Blanket", nonPerishable.getType());
    }

    @Test
    public void testNonPerishableConstructor_validArgs_setsQuantity() {
        assertEquals(10, nonPerishable.getQuantity());
    }

    @Test
    public void testNonPerishableConstructor_validArgs_isPerishableFalse() {
        assertFalse(nonPerishable.isPerishable());
    }

    @Test
    public void testNonPerishableConstructor_validArgs_expiryDateIsNull() {
        assertNull(nonPerishable.getExpiryDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonPerishableConstructor_zeroSupplyID_throwsIllegalArgumentException() {
        new Supply(0, "Blanket", 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonPerishableConstructor_negativeSupplyID_throwsIllegalArgumentException() {
        new Supply(-1, "Blanket", 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonPerishableConstructor_nullType_throwsIllegalArgumentException() {
        new Supply(1, null, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonPerishableConstructor_blankType_throwsIllegalArgumentException() {
        new Supply(1, "  ", 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonPerishableConstructor_negativeQuantity_throwsIllegalArgumentException() {
        new Supply(1, "Blanket", -1);
    }

    @Test
    public void testNonPerishableConstructor_zeroQuantity_isAccepted() {
        Supply s = new Supply(3, "Blanket", 0);
        assertEquals(0, s.getQuantity());
    }

    // =========================================================================
    //  Constructor – perishable
    // =========================================================================

    @Test
    public void testPerishableConstructor_validArgs_isPerishableTrue() {
        assertTrue(perishable.isPerishable());
    }

    @Test
    public void testPerishableConstructor_validArgs_setsExpiryDate() {
        assertEquals(futureDate, perishable.getExpiryDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerishableConstructor_perishableWithNullExpiryDate_throwsIllegalArgumentException() {
        new Supply(3, "Milk", 2, true, null);
    }

    @Test
    public void testPerishableConstructor_pastExpiryDateAllowedForDatabaseLoading_setsExpiryDate() {
        // Constructor allows past dates to support loading existing DB records
        Supply s = new Supply(4, "Old food", 1, true, pastDate);
        assertEquals(pastDate, s.getExpiryDate());
    }

    // =========================================================================
    //  isExpired
    // =========================================================================

    @Test
    public void testIsExpired_futureDatedPerishable_returnsFalse() {
        assertFalse(perishable.isExpired());
    }

    @Test
    public void testIsExpired_pastDatedPerishable_returnsTrue() {
        Supply expired = new Supply(5, "Old food", 1, true, pastDate);
        assertTrue(expired.isExpired());
    }

    @Test
    public void testIsExpired_nonPerishableSupply_returnsFalse() {
        assertFalse(nonPerishable.isExpired());
    }

    // =========================================================================
    //  setType
    // =========================================================================

    @Test
    public void testSetType_validValue_updatesType() {
        nonPerishable.setType("Sleeping bag");
        assertEquals("Sleeping bag", nonPerishable.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetType_nullValue_throwsIllegalArgumentException() {
        nonPerishable.setType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetType_blankValue_throwsIllegalArgumentException() {
        nonPerishable.setType("");
    }

    // =========================================================================
    //  setQuantity
    // =========================================================================

    @Test
    public void testSetQuantity_positiveValue_updatesQuantity() {
        nonPerishable.setQuantity(20);
        assertEquals(20, nonPerishable.getQuantity());
    }

    @Test
    public void testSetQuantity_zeroValue_isAccepted() {
        nonPerishable.setQuantity(0);
        assertEquals(0, nonPerishable.getQuantity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetQuantity_negativeValue_throwsIllegalArgumentException() {
        nonPerishable.setQuantity(-1);
    }

    // =========================================================================
    //  setExpiryDate (UI-facing — rejects past dates)
    // =========================================================================

    @Test
    public void testSetExpiryDate_futureDate_updatesExpiryDate() {
        LocalDate newDate = LocalDate.now().plusDays(60);
        perishable.setExpiryDate(newDate);
        assertEquals(newDate, perishable.getExpiryDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExpiryDate_pastDate_throwsIllegalArgumentException() {
        perishable.setExpiryDate(pastDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetExpiryDate_nullDate_throwsIllegalArgumentException() {
        perishable.setExpiryDate(null);
    }

    // =========================================================================
    //  allocateTo
    // =========================================================================

    @Test
    public void testAllocateTo_validVictim_setsAllocatedVictim() {
        DisasterVictim victim = new DisasterVictim(1, "Alice", LocalDate.now());
        nonPerishable.allocateTo(victim);
        assertEquals(victim, nonPerishable.getAllocatedVictim());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllocateTo_nullVictim_throwsIllegalArgumentException() {
        nonPerishable.allocateTo(null);
    }

    @Test
    public void testGetAllocatedVictim_notYetAllocated_returnsNull() {
        assertNull(nonPerishable.getAllocatedVictim());
    }
}