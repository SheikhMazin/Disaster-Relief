package edu.ucalgary.oop;

/**
 * Inquirer
 * Represents a person who contacts the relief system to inquire about a
 * missing disaster victim. All fields are immutable after construction
 * since an inquirer's identity and contact details should not change
 * within a single inquiry record.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */
public class Inquirer {

    private final int inquirerID;
    private final String FIRST_NAME;
    private final String LAST_NAME;
    private final String INFO;
    private final String SERVICES_PHONE;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs an Inquirer with the given identifying information.
     *
     * @param inquirerID     unique positive integer identifier for this inquirer
     * @param firstName      non-null, non-empty first name
     * @param lastName       non-null, non-empty last name
     * @param phone          non-null, non-empty services phone number
     * @param info           non-null, non-empty additional info about the inquirer
     * @throws IllegalArgumentException if inquirerID is not positive, or if any
     *                                  string argument is null or blank
     */
    public Inquirer(int inquirerID, String firstName, String lastName, String phone, String info) {
        if (inquirerID <= 0) {
            throw new IllegalArgumentException("inquirerID must be greater than 0.");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("firstName cannot be null or empty.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("lastName cannot be null or empty.");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("phone cannot be null or empty.");
        }
        if (info == null || info.trim().isEmpty()) {
            throw new IllegalArgumentException("info cannot be null or empty.");
        }

        this.inquirerID     = inquirerID;
        this.FIRST_NAME     = firstName;
        this.LAST_NAME      = lastName;
        this.SERVICES_PHONE = phone;
        this.INFO           = info;
    }

    // =========================================================================
    //  Getters
    // =========================================================================

    /**
     * Returns the unique identifier for this inquirer.
     * @return inquirerID
     */
    public int getInquirerID() { return inquirerID; }

    /**
     * Returns the first name of this inquirer.
     * @return FIRST_NAME
     */
    public String getFirstName() { return FIRST_NAME; }

    /**
     * Returns the last name of this inquirer.
     * @return LAST_NAME
     */
    public String getLastName() { return LAST_NAME; }

    /**
     * Returns the services phone number for this inquirer.
     * @return SERVICES_PHONE
     */
    public String getServicesPhoneNum() { return SERVICES_PHONE; }

    /**
     * Returns additional information about this inquirer.
     * @return INFO
     */
    public String getInfo() { return INFO; }
}