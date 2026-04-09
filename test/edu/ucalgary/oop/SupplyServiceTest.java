package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

/**
 * Unit tests for SupplyService.
 * Uses MockDataRepository — no database connection required.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class SupplyServiceTest {

    private SupplyService  service;
    private Supply         nonPerishable;
    private Supply         perishable;
    private Supply         expired;
    private DisasterVictim victim;

    @Before
    public void setUp() {
        service       = new SupplyService(new MockDataRepository(), ActionLogger.getInstance());
        nonPerishable = new Supply(1, "Blanket", 5);
        perishable    = new Supply(2, "Water", 10, true, LocalDate.now().plusDays(30));
        expired       = new Supply(3, "Old food", 2, true, LocalDate.now().minusDays(1));
        victim        = new DisasterVictim(1, "Alice", LocalDate.now());
    }

    // =========================================================================
    //  Constructor
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullRepository_throwsIllegalArgumentException() {
        new SupplyService(null, ActionLogger.getInstance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullLogger_throwsIllegalArgumentException() {
        new SupplyService(new MockDataRepository(), null);
    }

    // =========================================================================
    //  addSupply
    // =========================================================================

    @Test
    public void testAddSupply_validSupply_appearsInAllSupplies() {
        service.addSupply(nonPerishable);
        assertTrue(service.getAllSupplies().contains(nonPerishable));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSupply_nullSupply_throwsIllegalArgumentException() {
        service.addSupply(null);
    }

    // =========================================================================
    //  getAvailableSupplies
    // =========================================================================

    @Test
    public void testGetAvailableSupplies_nonExpiredSupply_included() {
        service.addSupply(nonPerishable);
        assertTrue(service.getAvailableSupplies().contains(nonPerishable));
    }

    @Test
    public void testGetAvailableSupplies_expiredSupply_excluded() {
        service.addSupply(expired);
        assertFalse(service.getAvailableSupplies().contains(expired));
    }

    @Test
    public void testGetAvailableSupplies_freshPerishable_included() {
        service.addSupply(perishable);
        assertTrue(service.getAvailableSupplies().contains(perishable));
    }

    // =========================================================================
    //  getExpiredSupplies
    // =========================================================================

    @Test
    public void testGetExpiredSupplies_expiredSupply_included() {
        service.addSupply(expired);
        assertTrue(service.getExpiredSupplies().contains(expired));
    }

    @Test
    public void testGetExpiredSupplies_nonExpiredSupply_excluded() {
        service.addSupply(nonPerishable);
        assertFalse(service.getExpiredSupplies().contains(nonPerishable));
    }

    // =========================================================================
    //  warnExpiredSupplies
    // =========================================================================

    @Test
    public void testWarnExpiredSupplies_noExpiredSupplies_returnsEmptyString() {
        service.addSupply(nonPerishable);
        assertEquals("", service.warnExpiredSupplies());
    }

    @Test
    public void testWarnExpiredSupplies_withExpiredSupply_returnsNonEmptyString() {
        service.addSupply(expired);
        assertFalse(service.warnExpiredSupplies().isEmpty());
    }

    @Test
    public void testWarnExpiredSupplies_withExpiredSupply_containsSupplyType() {
        service.addSupply(expired);
        assertTrue(service.warnExpiredSupplies().contains("Old food"));
    }

    // =========================================================================
    //  allocateSupply
    // =========================================================================

    @Test
    public void testAllocateSupply_validSupplyAndVictim_supplyAllocatedToVictim() {
        service.addSupply(nonPerishable);
        service.allocateSupply(nonPerishable, victim);
        assertEquals(victim, nonPerishable.getAllocatedVictim());
    }

    @Test
    public void testAllocateSupply_validSupplyAndVictim_supplyAppearsInVictimBelongings() {
        service.addSupply(nonPerishable);
        service.allocateSupply(nonPerishable, victim);
        assertTrue(victim.getPersonalBelongings().contains(nonPerishable));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllocateSupply_expiredSupply_throwsIllegalArgumentException() {
        service.addSupply(expired);
        service.allocateSupply(expired, victim);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllocateSupply_nullSupply_throwsIllegalArgumentException() {
        service.allocateSupply(null, victim);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllocateSupply_nullVictim_throwsIllegalArgumentException() {
        service.allocateSupply(nonPerishable, null);
    }

    // =========================================================================
    //  updateSupply
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateSupply_nullSupply_throwsIllegalArgumentException() {
        service.updateSupply(null);
    }
}