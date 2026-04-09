package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Unit tests for CulturalOptions.
 * Each test covers a single scenario with a single assertion (rubric-compliant).
 */
public class CulturalOptionsTest {

    private CulturalOptions culturalOptions;

    @Before
    public void setUp() {
        culturalOptions = new CulturalOptions();
    }

    @Test
    public void testGetAccommodations_newInstance_returnsNull() {
        // CulturalOptions is a serializable data holder with no constructor args;
        // the accommodations field is null until set via deserialization.
        assertNull(culturalOptions.getAccommodations());
    }

    @Test
    public void testSerialVersionUID_isOne() {
        assertEquals(1L, CulturalOptions.serialVersionUID);
    }

    @Test
    public void testCulturalOptions_isSerializable() {
        assertTrue(culturalOptions instanceof java.io.Serializable);
    }
}