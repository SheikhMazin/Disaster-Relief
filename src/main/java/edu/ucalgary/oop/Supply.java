package edu.ucalgary.oop;

/**
 * Supply
 *
 * Represents a supply item managed within the disaster relief system.
 * Supplies may be perishable (e.g., bottled water, food items) or non-perishable
 * (e.g., blankets, teddy bears). Perishable supplies must have an expiry date
 * and cannot be allocated to victims once expired. Supplies may be allocated
 * to a specific disaster victim as personal belongings.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import java.time.LocalDate;

public class Supply {

    private int supplyID;
    private String type;
    private int quantity;
    private boolean perishable;
    private LocalDate expiryDate;
    private DisasterVictim allocatedVictim;

    // =========================================================================
    //  Constructors
    // =========================================================================

    /**
     * Constructs a non-perishable Supply with no expiry date.
     *
     * @param supplyID unique positive integer identifier for this supply
     * @param type     non-null, non-empty string describing the supply type
     * @param quantity non-negative integer quantity of this supply
     * @throws IllegalArgumentException if supplyID is not positive, type is
     *                                  null or empty, or quantity is negative
     */
    public Supply(int supplyID, String type, int quantity) {
        if (supplyID <= 0) {
            throw new IllegalArgumentException("supplyID must be greater than 0.");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("type cannot be null or empty.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity cannot be less than 0.");
        }

        this.supplyID = supplyID;
        this.type     = type;
        this.quantity = quantity;
    }

    /**
     * Constructs a Supply with perishability and expiry date information.
     * If the supply is marked as perishable, an expiry date must be provided.
     *
     * @param supplyID   unique positive integer identifier for this supply
     * @param type       non-null, non-empty string describing the supply type
     * @param quantity   non-negative integer quantity of this supply
     * @param perishable true if this supply type can expire
     * @param expiryDate the date this supply expires; must not be null if
     *                   perishable is true, and must not be in the past
     * @throws IllegalArgumentException if supplyID is not positive, type is
     *                                  null or empty, quantity is negative,
     *                                  perishable is true but expiryDate is
     *                                  null, or expiryDate is in the past
     */
    public Supply(int supplyID, String type, int quantity, boolean perishable, LocalDate expiryDate) {
        this(supplyID, type, quantity);

        if (perishable && expiryDate == null) {
            throw new IllegalArgumentException("Perishable supplies must have an expiryDate.");
        }
        if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("expiryDate cannot be in the past.");
        }

        this.perishable = perishable;
        this.expiryDate = expiryDate;
    }

    // =========================================================================
    //  Getters
    // =========================================================================

    /**
     * Returns the unique identifier for this supply.
     * @return supplyID
     */
    public int getSupplyID() { return supplyID; }

    /**
     * Returns the type description of this supply (e.g., "bottled water").
     * @return type
     */
    public String getType() { return type; }

    /**
     * Returns the current quantity of this supply.
     * @return quantity
     */
    public int getQuantity() { return quantity; }

    /**
     * Returns whether this supply is perishable.
     * @return true if perishable, false otherwise
     */
    public boolean isPerishable() { return perishable; }

    /**
     * Returns the expiry date of this supply, or null if not applicable.
     * @return expiryDate, may be null for non-perishable supplies
     */
    public LocalDate getExpiryDate() { return expiryDate; }

    /**
     * Returns whether this supply has expired based on today's date.
     * Non-perishable supplies (no expiry date) never expire.
     *
     * @return true if the expiry date is set and is before today, false otherwise
     */
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }

    /**
     * Returns the disaster victim this supply is currently allocated to,
     * or null if it has not been allocated.
     *
     * @return allocatedVictim, may be null
     */
    public DisasterVictim getAllocatedVictim() { return allocatedVictim; }

    // =========================================================================
    //  Setters
    // =========================================================================

    /**
     * Updates the type description of this supply.
     *
     * @param type non-null, non-empty string describing the supply type
     * @throws IllegalArgumentException if type is null or blank
     */
    public void setType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("type cannot be null or empty.");
        }
        this.type = type;
    }

    /**
     * Updates the quantity of this supply.
     *
     * @param quantity non-negative integer quantity
     * @throws IllegalArgumentException if quantity is negative
     */
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity cannot be less than 0.");
        }
        this.quantity = quantity;
    }

    /**
     * Sets whether this supply is perishable.
     * Note: if setting to true, ensure an expiry date is also set via
     * {@link #setExpiryDate(LocalDate)}.
     *
     * @param perishable true if this supply can expire
     */
    public void setPerishable(boolean perishable) {
        this.perishable = perishable;
    }

    /**
     * Updates the expiry date of this supply.
     *
     * @param expiryDate non-null expiry date; must not be in the past
     * @throws IllegalArgumentException if expiryDate is null or before today
     */
    public void setExpiryDate(LocalDate expiryDate) {
        if (expiryDate == null) {
            throw new IllegalArgumentException("expiryDate cannot be null.");
        }
        if (expiryDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("expiryDate cannot be in the past.");
        }
        this.expiryDate = expiryDate;
    }

    /**
     * Allocates this supply to the specified disaster victim.
     *
     * @param allocatedVictim non-null DisasterVictim to allocate this supply to
     * @throws IllegalArgumentException if allocatedVictim is null
     */
    public void allocateTo(DisasterVictim allocatedVictim) {
        if (allocatedVictim == null) {
            throw new IllegalArgumentException("allocatedVictim cannot be null.");
        }
        this.allocatedVictim = allocatedVictim;
    }
}