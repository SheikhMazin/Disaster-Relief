package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Unit tests for Location.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class LocationTest {

    private Location       location;
    private DisasterVictim victim;
    private Supply         supply;

    @Before
    public void setUp() {
        location = new Location(1, "Shelter Alpha", "456 Elm Street");
        victim   = new DisasterVictim(1, "Alice", LocalDate.now());
        supply   = new Supply(1, "Blanket", 5);
    }

    // =========================================================================
    //  Constructor – valid args
    // =========================================================================

    @Test
    public void testConstructor_validArgs_setsLocationID() {
        assertEquals(1, location.getLocationID());
    }

    @Test
    public void testConstructor_validArgs_setsName() {
        assertEquals("Shelter Alpha", location.getName());
    }

    @Test
    public void testConstructor_validArgs_setsAddress() {
        assertEquals("456 Elm Street", location.getAddress());
    }

    @Test
    public void testConstructor_validArgs_occupantsListIsEmpty() {
        assertTrue(location.getOccupants().isEmpty());
    }

    @Test
    public void testConstructor_validArgs_suppliesListIsEmpty() {
        assertTrue(location.getSupplies().isEmpty());
    }

    // =========================================================================
    //  Constructor – invalid args
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_zeroLocationID_throwsIllegalArgumentException() {
        new Location(0, "Shelter", "123 St");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_negativeLocationID_throwsIllegalArgumentException() {
        new Location(-1, "Shelter", "123 St");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullName_throwsIllegalArgumentException() {
        new Location(1, null, "123 St");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankName_throwsIllegalArgumentException() {
        new Location(1, "  ", "123 St");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullAddress_throwsIllegalArgumentException() {
        new Location(1, "Shelter", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankAddress_throwsIllegalArgumentException() {
        new Location(1, "Shelter", "");
    }

    // =========================================================================
    //  setName
    // =========================================================================

    @Test
    public void testSetName_validValue_updatesName() {
        location.setName("Shelter Beta");
        assertEquals("Shelter Beta", location.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetName_nullValue_throwsIllegalArgumentException() {
        location.setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetName_blankValue_throwsIllegalArgumentException() {
        location.setName("  ");
    }

    // =========================================================================
    //  setAddress
    // =========================================================================

    @Test
    public void testSetAddress_validValue_updatesAddress() {
        location.setAddress("789 Oak Ave");
        assertEquals("789 Oak Ave", location.getAddress());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAddress_nullValue_throwsIllegalArgumentException() {
        location.setAddress(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAddress_blankValue_throwsIllegalArgumentException() {
        location.setAddress("   ");
    }

    // =========================================================================
    //  setOccupants
    // =========================================================================

    @Test
    public void testSetOccupants_validList_updatesOccupantsCount() {
        ArrayList<DisasterVictim> list = new ArrayList<>();
        list.add(victim);
        location.setOccupants(list);
        assertEquals(1, location.getOccupants().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOccupants_nullList_throwsIllegalArgumentException() {
        location.setOccupants(null);
    }

    // =========================================================================
    //  setSupplies
    // =========================================================================

    @Test
    public void testSetSupplies_validList_updatesSuppliesCount() {
        ArrayList<Supply> list = new ArrayList<>();
        list.add(supply);
        location.setSupplies(list);
        assertEquals(1, location.getSupplies().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSupplies_nullList_throwsIllegalArgumentException() {
        location.setSupplies(null);
    }

    // =========================================================================
    //  addOccupant / removeOccupant
    // =========================================================================

    @Test
    public void testAddOccupant_validVictim_appearsInOccupantsList() {
        location.addOccupant(victim);
        assertTrue(location.getOccupants().contains(victim));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOccupant_nullVictim_throwsIllegalArgumentException() {
        location.addOccupant(null);
    }

    @Test
    public void testRemoveOccupant_existingVictim_removedFromOccupantsList() {
        location.addOccupant(victim);
        location.removeOccupant(victim);
        assertFalse(location.getOccupants().contains(victim));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveOccupant_nullVictim_throwsIllegalArgumentException() {
        location.removeOccupant(null);
    }

    // =========================================================================
    //  addSupply / removeSupply
    // =========================================================================

    @Test
    public void testAddSupply_validSupply_appearsInSuppliesList() {
        location.addSupply(supply);
        assertTrue(location.getSupplies().contains(supply));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSupply_nullSupply_throwsIllegalArgumentException() {
        location.addSupply(null);
    }

    @Test
    public void testRemoveSupply_existingSupply_removedFromSuppliesList() {
        location.addSupply(supply);
        location.removeSupply(supply);
        assertFalse(location.getSupplies().contains(supply));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveSupply_nullSupply_throwsIllegalArgumentException() {
        location.removeSupply(null);
    }
}