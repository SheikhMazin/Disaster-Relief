package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

/**
 * Unit tests for FamilyRelation.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class FamilyRelationTest {

    private DisasterVictim personOne;
    private DisasterVictim personTwo;
    private DisasterVictim personThree;
    private FamilyRelation relation;

    @Before
    public void setUp() {
        LocalDate entry = LocalDate.of(2024, 1, 1);
        personOne   = new DisasterVictim(1, "Alice", entry);
        personTwo   = new DisasterVictim(2, "Bob",   entry);
        personThree = new DisasterVictim(3, "Carol", entry);
        relation    = new FamilyRelation(personOne, "sibling", personTwo);
    }

    // =========================================================================
    //  Constructor – valid args
    // =========================================================================

    @Test
    public void testConstructor_validArgs_setsPersonOne() {
        assertEquals(personOne, relation.getPersonOne());
    }

    @Test
    public void testConstructor_validArgs_setsRelationshipTo() {
        assertEquals("sibling", relation.getRelationshipTo());
    }

    @Test
    public void testConstructor_validArgs_setsPersonTwo() {
        assertEquals(personTwo, relation.getPersonTwo());
    }

    // =========================================================================
    //  Constructor – invalid args
    // =========================================================================

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullPersonOne_throwsIllegalArgumentException() {
        new FamilyRelation(null, "sibling", personTwo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullRelationshipTo_throwsIllegalArgumentException() {
        new FamilyRelation(personOne, null, personTwo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankRelationshipTo_throwsIllegalArgumentException() {
        new FamilyRelation(personOne, "  ", personTwo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullPersonTwo_throwsIllegalArgumentException() {
        new FamilyRelation(personOne, "sibling", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_samePersonForBothRoles_throwsIllegalArgumentException() {
        new FamilyRelation(personOne, "sibling", personOne);
    }

    // =========================================================================
    //  setPersonOne
    // =========================================================================

    @Test
    public void testSetPersonOne_differentPerson_updatesPersonOne() {
        relation.setPersonOne(personThree);
        assertEquals(personThree, relation.getPersonOne());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPersonOne_nullValue_throwsIllegalArgumentException() {
        relation.setPersonOne(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPersonOne_sameAsPersonTwo_throwsIllegalArgumentException() {
        relation.setPersonOne(personTwo);
    }

    // =========================================================================
    //  setRelationshipTo
    // =========================================================================

    @Test
    public void testSetRelationshipTo_validValue_updatesRelationship() {
        relation.setRelationshipTo("parent");
        assertEquals("parent", relation.getRelationshipTo());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRelationshipTo_nullValue_throwsIllegalArgumentException() {
        relation.setRelationshipTo(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRelationshipTo_blankValue_throwsIllegalArgumentException() {
        relation.setRelationshipTo("");
    }

    // =========================================================================
    //  setPersonTwo
    // =========================================================================

    @Test
    public void testSetPersonTwo_differentPerson_updatesPersonTwo() {
        relation.setPersonTwo(personThree);
        assertEquals(personThree, relation.getPersonTwo());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPersonTwo_nullValue_throwsIllegalArgumentException() {
        relation.setPersonTwo(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPersonTwo_sameAsPersonOne_throwsIllegalArgumentException() {
        relation.setPersonTwo(personOne);
    }
}