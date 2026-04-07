package edu.ucalgary.oop;

/**
 * SupplyService
 *
 * Service class responsible for managing supply inventory within the disaster
 * relief system. Handles adding, updating, and allocating supplies to victims,
 * as well as tracking perishable expiry. Expired supplies are excluded from
 * allocation. All changes are persisted through the DataRepository and logged
 * via ActionLogger.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import java.util.ArrayList;

public class SupplyService {

    private final DataRepository repository;
    private final ActionLogger logger;
    private ArrayList<Supply> supplies;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs a SupplyService backed by the given repository and logger.
     *
     * @param repository non-null DataRepository for persisting supply data
     * @param logger     non-null ActionLogger for logging supply changes
     * @throws IllegalArgumentException if repository or logger is null
     */
    public SupplyService(DataRepository repository, ActionLogger logger) {
        if (repository == null) {
            throw new IllegalArgumentException("repository cannot be null.");
        }
        if (logger == null) {
            throw new IllegalArgumentException("logger cannot be null.");
        }
        this.repository = repository;
        this.logger     = logger;
        this.supplies   = new ArrayList<>();
    }

    // =========================================================================
    //  Load
    // =========================================================================

    /**
     * Loads all supply records from the database into memory.
     * Should be called once at application startup.
     *
     * @return list of all Supply objects loaded from the database
     */
    public ArrayList<Supply> loadSupplies() {
        this.supplies = repository.loadSupplies();
        return this.supplies;
    }

    /**
     * Returns all supplies currently loaded in memory, including expired ones.
     * Used by the UI to display the complete inventory.
     *
     * @return full list of all Supply objects
     */
    public ArrayList<Supply> getAllSupplies() {
        return supplies;
    }


    // =========================================================================
    //  Add / Update
    // =========================================================================

    /**
     * Adds a new supply to the system and persists it to the database.
     *
     * @param supply non-null Supply to add
     * @throws IllegalArgumentException if supply is null
     */
    public void addSupply(Supply supply) {
        if (supply == null) {
            throw new IllegalArgumentException("supply cannot be null.");
        }
        supplies.add(supply);
        repository.saveSupply(supply);
        logger.logAdded("supply",
                String.format("ID: %d | Type: %s | Quantity: %d",
                        supply.getSupplyID(), supply.getType(), supply.getQuantity()));
    }

    /**
     * Updates an existing supply record in the system and database.
     *
     * @param supply non-null Supply with updated values
     * @throws IllegalArgumentException if supply is null
     */
    public void updateSupply(Supply supply) {
        if (supply == null) {
            throw new IllegalArgumentException("supply cannot be null.");
        }
        repository.updateSupply(supply);
        logger.logUpdated("supply",
                String.format("ID: %d | Type: %s | Quantity: %d",
                        supply.getSupplyID(), supply.getType(), supply.getQuantity()));
    }

    // =========================================================================
    //  Allocation
    // =========================================================================

    /**
     * Allocates a supply to a disaster victim. Expired supplies cannot be
     * allocated — the UI should call {@link #getAvailableSupplies()} to show
     * only valid supplies. Both the supply and victim objects are updated and
     * the change is persisted to the database.
     *
     * @param supply non-null Supply to allocate
     * @param victim non-null DisasterVictim to allocate the supply to
     * @throws IllegalArgumentException if supply or victim is null, or if the
     *                                  supply is expired
     */
    public void allocateSupply(Supply supply, DisasterVictim victim) {
        if (supply == null) {
            throw new IllegalArgumentException("supply cannot be null.");
        }
        if (victim == null) {
            throw new IllegalArgumentException("victim cannot be null.");
        }
        if (supply.isExpired()) {
            throw new IllegalArgumentException("Cannot allocate an expired supply.");
        }

        supply.allocateTo(victim);
        victim.addPersonalBelonging(supply);
        repository.updateSupply(supply);
        logger.logUpdated("supply",
                String.format("ID: %d | Type: %s -> allocated to victim %d (%s)",
                        supply.getSupplyID(), supply.getType(),
                        victim.getVictimID(), victim.getFirstName()));
    }

    // =========================================================================
    //  Query
    // =========================================================================

    /**
     * Returns all supplies that are not expired and available for allocation.
     * Expired perishable supplies are excluded.
     *
     * @return list of non-expired Supply objects
     */
    public ArrayList<Supply> getAvailableSupplies() {
        ArrayList<Supply> available = new ArrayList<>();
        for (Supply supply : supplies) {
            if (!supply.isExpired()) {
                available.add(supply);
            }
        }
        return available;
    }

    /**
     * Returns all supplies that have passed their expiry date.
     *
     * @return list of expired Supply objects
     */
    public ArrayList<Supply> getExpiredSupplies() {
        ArrayList<Supply> expired = new ArrayList<>();
        for (Supply supply : supplies) {
            if (supply.isExpired()) {
                expired.add(supply);
            }
        }
        return expired;
    }

    /**
     * Builds a warning message listing all currently expired supplies in
     * inventory. Returns an empty string if no expired supplies exist.
     * This warning should be shown to the user once each time they access
     * the supply allocation screen.
     *
     * @return formatted warning string listing expired supplies, or empty
     *         string if none are expired
     */
    public String warnExpiredSupplies() {
        ArrayList<Supply> expired = getExpiredSupplies();
        if (expired.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder("WARNING — Expired supplies in inventory:\n");
        for (Supply supply : expired) {
            sb.append(String.format("  - ID: %d | Type: %s | Expired: %s\n",
                    supply.getSupplyID(), supply.getType(), supply.getExpiryDate()));
        }
        return sb.toString();
    }

    /**
     * Returns the next available supply ID from the database.
     *
     * @return next available integer ID
     */
    public int getNextSupplyID() {
        return repository.getNextSupplyID();
    }
    
}