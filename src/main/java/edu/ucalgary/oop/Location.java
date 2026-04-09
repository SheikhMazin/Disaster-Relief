package edu.ucalgary.oop;

/**
 * Location
 * Represents a physical relief location (e.g., a shelter or medical centre)
 * within the disaster relief system. Each location has a unique ID, a name,
 * and an address. A location tracks the disaster victims currently occupying
 * it as well as the supplies stored there.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import java.util.ArrayList;

public class Location {

    private final int locationID;
    private String name;
    private String address;
    private ArrayList<DisasterVictim> occupants;
    private ArrayList<Supply> supplies;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs a Location with the given ID, name, and address.
     *
     * @param locationID unique positive integer identifier for this location
     * @param name       non-null, non-empty name of the location
     * @param address    non-null, non-empty street address of the location
     * @throws IllegalArgumentException if locationID is not positive, or if
     *                                  name or address is null or blank
     */
    public Location(int locationID, String name, String address) {
        if (locationID <= 0) {
            throw new IllegalArgumentException("locationID must be greater than 0.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty.");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("address cannot be null or empty.");
        }

        this.locationID = locationID;
        this.name       = name;
        this.address    = address;
        this.occupants  = new ArrayList<>();
        this.supplies   = new ArrayList<>();
    }

    // =========================================================================
    //  Getters
    // =========================================================================

    /**
     * Returns the unique identifier for this location.
     * @return locationID
     */
    public int getLocationID() { return locationID; }

    /**
     * Returns the name of this location.
     * @return name
     */
    public String getName() { return name; }

    /**
     * Returns the street address of this location.
     * @return address
     */
    public String getAddress() { return address; }

    /**
     * Returns the list of disaster victims currently occupying this location.
     * @return occupants
     */
    public ArrayList<DisasterVictim> getOccupants() { return occupants; }

    /**
     * Returns the list of supplies currently stored at this location.
     * @return supplies
     */
    public ArrayList<Supply> getSupplies() { return supplies; }

    // =========================================================================
    //  Setters
    // =========================================================================

    /**
     * Updates the name of this location.
     *
     * @param name non-null, non-empty location name
     * @throws IllegalArgumentException if name is null or blank
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty.");
        }
        this.name = name;
    }

    /**
     * Updates the street address of this location.
     *
     * @param address non-null, non-empty street address
     * @throws IllegalArgumentException if address is null or blank
     */
    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("address cannot be null or empty.");
        }
        this.address = address;
    }

    /**
     * Replaces the entire occupants list with the provided list.
     * Use {@link #addOccupant(DisasterVictim)} to add individual victims.
     *
     * @param occupants non-null list of DisasterVictim objects
     * @throws IllegalArgumentException if occupants is null
     */
    public void setOccupants(ArrayList<DisasterVictim> occupants) {
        if (occupants == null) {
            throw new IllegalArgumentException("occupants cannot be null.");
        }
        this.occupants = occupants;
    }

    /**
     * Replaces the entire supplies list with the provided list.
     * Use {@link #addSupply(Supply)} to add individual supplies.
     *
     * @param supplies non-null list of Supply objects
     * @throws IllegalArgumentException if supplies is null
     */
    public void setSupplies(ArrayList<Supply> supplies) {
        if (supplies == null) {
            throw new IllegalArgumentException("supplies cannot be null.");
        }
        this.supplies = supplies;
    }

    // =========================================================================
    //  Add / Remove
    // =========================================================================

    /**
     * Adds a disaster victim to this location's occupant list.
     *
     * @param occupant non-null DisasterVictim to add
     * @throws IllegalArgumentException if occupant is null
     */
    public void addOccupant(DisasterVictim occupant) {
        if (occupant == null) {
            throw new IllegalArgumentException("occupant cannot be null.");
        }
        occupants.add(occupant);
    }

    /**
     * Removes a disaster victim from this location's occupant list.
     * If the victim is not in the list, the call is silently ignored.
     *
     * @param occupant non-null DisasterVictim to remove
     * @throws IllegalArgumentException if occupant is null
     */
    public void removeOccupant(DisasterVictim occupant) {
        if (occupant == null) {
            throw new IllegalArgumentException("occupant cannot be null.");
        }
        occupants.remove(occupant);
    }

    /**
     * Adds a supply item to this location's inventory.
     *
     * @param inventory non-null Supply to add
     * @throws IllegalArgumentException if inventory is null
     */
    public void addSupply(Supply inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("inventory cannot be null.");
        }
        supplies.add(inventory);
    }

    /**
     * Removes a supply item from this location's inventory.
     * If the supply is not in the list, the call is silently ignored.
     *
     * @param inventory non-null Supply to remove
     * @throws IllegalArgumentException if inventory is null
     */
    public void removeSupply(Supply inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("inventory cannot be null.");
        }
        supplies.remove(inventory);
    }
}