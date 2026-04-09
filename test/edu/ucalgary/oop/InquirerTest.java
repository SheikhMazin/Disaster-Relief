package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for Inquirer.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class InquirerTest {

    private Inquirer inquirer;

    @Before
    public void setUp() {
        inquirer = new Inquirer(1, "Jane", "Doe", "403-555-0100", "Searching for husband");
    }

    @Test
    public void testConstructor_validArgs_setsInquirerID() {
        assertEquals(1, inquirer.getInquirerID());
    }

    @Test
    public void testConstructor_validArgs_setsFirstName() {
        assertEquals("Jane", inquirer.getFirstName());
    }

    @Test
    public void testConstructor_validArgs_setsLastName() {
        assertEquals("Doe", inquirer.getLastName());
    }

    @Test
    public void testConstructor_validArgs_setsServicesPhone() {
        assertEquals("403-555-0100", inquirer.getServicesPhoneNum());
    }

    @Test
    public void testConstructor_validArgs_setsInfo() {
        assertEquals("Searching for husband", inquirer.getInfo());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_zeroID_throwsIllegalArgumentException() {
        new Inquirer(0, "Jane", "Doe", "403-555-0100", "info");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_negativeID_throwsIllegalArgumentException() {
        new Inquirer(-1, "Jane", "Doe", "403-555-0100", "info");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullFirstName_throwsIllegalArgumentException() {
        new Inquirer(1, null, "Doe", "403-555-0100", "info");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankFirstName_throwsIllegalArgumentException() {
        new Inquirer(1, "  ", "Doe", "403-555-0100", "info");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullLastName_throwsIllegalArgumentException() {
        new Inquirer(1, "Jane", null, "403-555-0100", "info");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankLastName_throwsIllegalArgumentException() {
        new Inquirer(1, "Jane", "  ", "403-555-0100", "info");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullPhone_throwsIllegalArgumentException() {
        new Inquirer(1, "Jane", "Doe", null, "info");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankPhone_throwsIllegalArgumentException() {
        new Inquirer(1, "Jane", "Doe", "  ", "info");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nullInfo_throwsIllegalArgumentException() {
        new Inquirer(1, "Jane", "Doe", "403-555-0100", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_blankInfo_throwsIllegalArgumentException() {
        new Inquirer(1, "Jane", "Doe", "403-555-0100", "");
    }
}