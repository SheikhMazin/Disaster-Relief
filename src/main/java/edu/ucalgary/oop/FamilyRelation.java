package edu.ucalgary.oop;

/**
 * FamilyRelation
 *
 * Represents a family relationship between two disaster victims.
 * Each relation stores the two victims involved and a string describing
 * the relationship from personOne's perspective (e.g., "parent", "sibling").
 * Both victims in the relationship must be distinct — a victim cannot be
 * related to themselves.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */
public class FamilyRelation {

    private DisasterVictim personOne;
    private String relationshipTo;
    private DisasterVictim personTwo;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs a FamilyRelation between two distinct disaster victims.
     *
     * @param personOne      non-null first victim in the relationship
     * @param relationshipTo non-null, non-empty string describing the relationship
     *                       from personOne's perspective (e.g., "parent of")
     * @param personTwo      non-null second victim in the relationship;
     *                       must be a different person than personOne
     * @throws IllegalArgumentException if any argument is null, relationshipTo
     *                                  is blank, or both persons are the same victim
     */
    public FamilyRelation(DisasterVictim personOne, String relationshipTo, DisasterVictim personTwo) {
        if (personOne == null) {
            throw new IllegalArgumentException("personOne cannot be null.");
        }
        if (relationshipTo == null || relationshipTo.trim().isEmpty()) {
            throw new IllegalArgumentException("relationshipTo cannot be null or empty.");
        }
        if (personTwo == null) {
            throw new IllegalArgumentException("personTwo cannot be null.");
        }
        if (personOne.getVictimID() == personTwo.getVictimID()) {
            throw new IllegalArgumentException("personOne and personTwo must be different victims.");
        }

        this.personOne      = personOne;
        this.relationshipTo = relationshipTo;
        this.personTwo      = personTwo;
    }

    // =========================================================================
    //  Getters
    // =========================================================================

    /**
     * Returns the first victim in this relationship.
     * @return personOne
     */
    public DisasterVictim getPersonOne() { return personOne; }

    /**
     * Returns the string describing the relationship from personOne's perspective.
     * @return relationshipTo
     */
    public String getRelationshipTo() { return relationshipTo; }

    /**
     * Returns the second victim in this relationship.
     * @return personTwo
     */
    public DisasterVictim getPersonTwo() { return personTwo; }

    // =========================================================================
    //  Setters
    // =========================================================================

    /**
     * Updates the first victim in this relationship.
     * personOne must be a different person than the current personTwo.
     *
     * @param personOne non-null DisasterVictim to set as personOne
     * @throws IllegalArgumentException if personOne is null or is the same
     *                                  victim as the current personTwo
     */
    public void setPersonOne(DisasterVictim personOne) {
        if (personOne == null) {
            throw new IllegalArgumentException("personOne cannot be null.");
        }
        if (personOne.getVictimID() == this.personTwo.getVictimID()) {
            throw new IllegalArgumentException("personOne and personTwo must be different victims.");
        }
        this.personOne = personOne;
    }

    /**
     * Updates the relationship description from personOne's perspective.
     *
     * @param relationshipTo non-null, non-empty relationship string
     * @throws IllegalArgumentException if relationshipTo is null or blank
     */
    public void setRelationshipTo(String relationshipTo) {
        if (relationshipTo == null || relationshipTo.trim().isEmpty()) {
            throw new IllegalArgumentException("relationshipTo cannot be null or empty.");
        }
        this.relationshipTo = relationshipTo;
    }

    /**
     * Updates the second victim in this relationship.
     * personTwo must be a different person than the current personOne.
     *
     * @param personTwo non-null DisasterVictim to set as personTwo
     * @throws IllegalArgumentException if personTwo is null or is the same
     *                                  victim as the current personOne
     */
    public void setPersonTwo(DisasterVictim personTwo) {
        if (personTwo == null) {
            throw new IllegalArgumentException("personTwo cannot be null.");
        }
        if (personTwo.getVictimID() == this.personOne.getVictimID()) {
            throw new IllegalArgumentException("personOne and personTwo must be different victims.");
        }
        this.personTwo = personTwo;
    }
}