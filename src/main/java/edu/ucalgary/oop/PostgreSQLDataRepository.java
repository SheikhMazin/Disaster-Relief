package edu.ucalgary.oop;

/**
 * PostgreSQLDataRepository
 *
 * Concrete implementation of DataRepository that communicates with the
 * ensf380project PostgreSQL database through the JDBC connection supplied
 * by DatabaseManager. All SQL operations are mediated here, keeping database
 * logic isolated from the rest of the application (Dependency Inversion
 * Principle).
 *
 * Schema overview:
 *   Person           — base table for all people (victims and inquirers)
 *   DisasterVictim   — extends Person with victim-specific fields
 *   Location         — relief locations
 *   Supply           — supplies (perishable if expiry_date is set)
 *   MedicalRecord    — treatment records per victim
 *   FamilyRelationship — relationships between persons
 *   Inquiry          — inquiries made by inquirers about missing persons
 *   CulturalRequirement — cultural/religious requirements per victim
 *   Skill            — master skill catalogue
 *   VictimSkill      — skills registered to a victim
 *
 * @author Sheikh Muhammad Mazin
 * @version 2.0
 * @since 2026-01-01
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PostgreSQLDataRepository implements DataRepository {

    private final DatabaseManager dbManager;

    /**
     * Constructs a PostgreSQLDataRepository backed by the given DatabaseManager.
     *
     * @param dbManager non-null DatabaseManager that provides the active connection
     * @throws IllegalArgumentException if dbManager is null
     */
    public PostgreSQLDataRepository(DatabaseManager dbManager) {
        if (dbManager == null) {
            throw new IllegalArgumentException("dbManager cannot be null.");
        }
        this.dbManager = dbManager;
    }

    // =========================================================================
    //  Load methods
    // =========================================================================

    /**
     * Loads all disaster victim records from the database by joining the
     * Person and DisasterVictim tables. Populates name, age, gender, entry
     * date, comments, and soft-delete status for each victim.
     *
     * @return list of DisasterVictim objects; empty list if none found
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public ArrayList<DisasterVictim> loadVictims() {
        ArrayList<DisasterVictim> victims = new ArrayList<>();

        String sql = "SELECT p.id, p.first_name, p.last_name, p.comments, " +
                "dv.date_of_birth, dv.approximate_age, dv.gender, " +
                "dv.entry_date, dv.is_soft_deleted " +
                "FROM Person p " +
                "JOIN DisasterVictim dv ON p.id = dv.person_id";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id           = rs.getInt("id");
                String firstName = rs.getString("first_name");
                LocalDate entry  = rs.getDate("entry_date").toLocalDate();

                DisasterVictim victim;

                java.sql.Date dobSql = rs.getDate("date_of_birth");
                if (dobSql != null) {
                    victim = new DisasterVictim(id, firstName, entry, dobSql.toLocalDate());
                } else {
                    int approxAge = rs.getInt("approximate_age");
                    if (!rs.wasNull() && approxAge > 0) {
                        victim = new DisasterVictim(id, firstName, entry, approxAge);
                    } else {
                        victim = new DisasterVictim(id, firstName, entry);
                    }
                }

                String lastName = rs.getString("last_name");
                if (lastName != null) victim.setLastName(lastName);

                String gender = rs.getString("gender");
                if (gender != null && !gender.trim().isEmpty()) {
                    try {
                        victim.setGender(gender);
                    } catch (IllegalArgumentException e) {
                        victim.setGender("Please Specify");
                        victim.setGender(gender);
                    }
                }

                String comments = rs.getString("comments");
                if (comments != null) victim.setComments(comments);

                boolean softDeleted = rs.getBoolean("is_soft_deleted");
                if (softDeleted) victim.softDelete();

                victims.add(victim);

                loadMedicalRecords(victim, conn);
                loadFamilyConnections(victim, conn);
                loadRequirements(victim, conn);
                loadSkills(victim, conn);

            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load victims: " + e.getMessage(), e);
        }

        return victims;
    }

    /**
     * Loads all medical records for the given victim from the MedicalRecord
     * table and adds them to the victim's in-memory record list.
     * Called internally by {@link #loadVictims()} after each victim is created.
     *
     * @param victim the DisasterVictim to populate with medical records
     * @param conn   the active database connection
     * @throws SQLException if a database access error occurs
     */
    private void loadMedicalRecords(DisasterVictim victim, Connection conn) throws SQLException {
        String sql = "SELECT mr.treatment_details, mr.treatment_date, " +
                "l.id, l.name, l.address " +
                "FROM MedicalRecord mr " +
                "JOIN Location l ON mr.location_id = l.id " +
                "WHERE mr.victim_id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, victim.getVictimID());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Location loc = new Location(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address")
            );
            MedicalRecord record = new MedicalRecord(
                    loc,
                    rs.getString("treatment_details"),
                    rs.getDate("treatment_date").toLocalDate()
            );
            victim.addMedicalRecord(record);
        }
    }

    /**
     * Loads all cultural and religious requirements for the given victim from
     * the CulturalRequirement table and adds them to the victim's in-memory
     * requirements list.
     * Called internally by {@link #loadVictims()} after each victim is created.
     *
     * @param victim the DisasterVictim to populate with requirements
     * @param conn   the active database connection
     * @throws SQLException if a database access error occurs
     */
    private void loadRequirements(DisasterVictim victim, Connection conn) throws SQLException {
        String sql = "SELECT requirement_category, requirement_option " +
                "FROM CulturalRequirement WHERE victim_id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, victim.getVictimID());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            VictimRequirement req = new VictimRequirement(
                    victim.getVictimID(),
                    rs.getString("requirement_category"),
                    rs.getString("requirement_option")
            );
            victim.addRequirement(req);
        }
    }

    /**
     * Loads all skills registered to the given victim from the VictimSkill
     * and Skill tables, constructs the appropriate Skill subclass for each
     * entry, and adds them to the victim's in-memory skills list.
     * Called internally by {@link #loadVictims()} after each victim is created.
     *
     * @param victim the DisasterVictim to populate with skills
     * @param conn   the active database connection
     * @throws SQLException if a database access error occurs
     */
    private void loadSkills(DisasterVictim victim, Connection conn) throws SQLException {
        String sql = "SELECT vs.id, vs.proficiency_level, vs.details, " +
                "vs.language_capabilities, vs.certification_expiry, " +
                "s.skill_name, s.category " +
                "FROM VictimSkill vs " +
                "JOIN Skill s ON vs.skill_id = s.id " +
                "WHERE vs.victim_id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, victim.getVictimID());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int skillID        = rs.getInt("id");
            String category    = rs.getString("category");
            String proficiency = rs.getString("proficiency_level");
            String skillName   = rs.getString("skill_name");

            Skill skill;

            switch (category.toLowerCase()) {
                case "medical" -> {
                    java.sql.Date expSql = rs.getDate("certification_expiry");
                    LocalDate expiry = expSql != null
                            ? expSql.toLocalDate()
                            : LocalDate.now().plusYears(1);
                    try {
                        skill = new MedicalSkill(skillID, victim.getVictimID(),
                                proficiency, skillName, expiry);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Warning: skipping invalid medical skill: "
                                + skillName + " — " + e.getMessage());
                        continue;
                    }
                }
                case "language" -> {
                    String caps  = rs.getString("language_capabilities");
                    boolean rw   = caps != null && caps.contains("read/write");
                    boolean sl   = caps != null && caps.contains("speak/listen");
                    if (!rw && !sl) sl = true;
                    try {
                        skill = new LanguageSkill(skillID, victim.getVictimID(),
                                proficiency, skillName, rw, sl);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Warning: skipping invalid language skill: "
                                + skillName + " — " + e.getMessage());
                        continue;
                    }
                }
                case "trade" -> {
                    try {
                        skill = new TradeSkill(skillID, victim.getVictimID(),
                                proficiency, skillName);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Warning: skipping invalid trade skill: "
                                + skillName + " — " + e.getMessage());
                        continue;
                    }
                }
                default -> { continue; }
            }

            victim.addSkill(skill);
        }
    }

    /**
     * Loads all family relationships where the given victim is person one,
     * from the FamilyRelationship table, and adds them to the victim's
     * in-memory family connections list.
     * Called internally by {@link #loadVictims()} after each victim is created.
     *
     * @param victim the DisasterVictim to populate with family connections
     * @param conn   the active database connection
     * @throws SQLException if a database access error occurs
     */
    private void loadFamilyConnections(DisasterVictim victim, Connection conn) throws SQLException {
        String sql = "SELECT fr.relationship_type, " +
                "p.id, p.first_name, p.last_name, dv.entry_date " +
                "FROM FamilyRelationship fr " +
                "JOIN Person p ON fr.person_two_id = p.id " +
                "JOIN DisasterVictim dv ON p.id = dv.person_id " +
                "WHERE fr.person_one_id = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, victim.getVictimID());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int relatedID    = rs.getInt("id");
            String firstName = rs.getString("first_name");
            LocalDate entry  = rs.getDate("entry_date").toLocalDate();
            String relType   = rs.getString("relationship_type");

            DisasterVictim related = new DisasterVictim(relatedID, firstName, entry);
            String lastName = rs.getString("last_name");
            if (lastName != null) related.setLastName(lastName);

            FamilyRelation relation = new FamilyRelation(victim, relType, related);
            victim.addFamilyConnection(relation);
        }
    }



    /**
     * Loads all location records from the Location table.
     *
     * @return list of Location objects; empty list if none found
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public ArrayList<Location> loadLocations() {
        ArrayList<Location> locations = new ArrayList<>();
        String sql = "SELECT id, name, address FROM Location";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id      = rs.getInt("id");
                String name = rs.getString("name");
                String addr = rs.getString("address");
                locations.add(new Location(id, name, addr));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load locations: " + e.getMessage(), e);
        }

        return locations;
    }

    /**
     * Loads all supply records from the Supply table.
     * A supply is treated as perishable if it has an expiry_date set.
     *
     * @return list of Supply objects; empty list if none found
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public ArrayList<Supply> loadSupplies() {
        ArrayList<Supply> supplies = new ArrayList<>();
        String sql = "SELECT id, supply_type, expiry_date FROM Supply";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id          = rs.getInt("id");
                String type     = rs.getString("supply_type");
                java.sql.Date expSql = rs.getDate("expiry_date");
                LocalDate expiry    = (expSql != null) ? expSql.toLocalDate() : null;

                // A supply is perishable if it has an expiry date
                boolean perishable = (expiry != null);
                Supply supply = new Supply(id, type, 1, perishable, expiry);
                supplies.add(supply);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load supplies: " + e.getMessage(), e);
        }

        return supplies;
    }

    /**
     * Loads all inquiry records from the database by joining the Inquiry,
     * Person (inquirer), and Person (subject) tables.
     *
     * @return list of ReliefService objects; empty list if none found
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public ArrayList<ReliefService> loadInquiries() {
        ArrayList<ReliefService> inquiries = new ArrayList<>();

        String sql = "SELECT i.id, i.details, i.inquiry_date, " +
                "p_inq.id AS inq_id, p_inq.first_name AS inq_first, " +
                "p_inq.last_name AS inq_last, p_inq.comments AS inq_info, " +
                "p_sub.id AS sub_id, p_sub.first_name AS sub_first, " +
                "p_sub.last_name AS sub_last, " +
                "dv.entry_date AS sub_entry " +
                "FROM Inquiry i " +
                "JOIN Person p_inq ON i.inquirer_id = p_inq.id " +
                "LEFT JOIN Person p_sub ON i.subject_person_id = p_sub.id " +
                "LEFT JOIN DisasterVictim dv ON p_sub.id = dv.person_id";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int inquiryID   = rs.getInt("id");
                String details  = rs.getString("details");
                LocalDate date  = rs.getTimestamp("inquiry_date")
                        .toLocalDateTime().toLocalDate();

                // Build Inquirer
                int inqID          = rs.getInt("inq_id");
                String inqFirst    = rs.getString("inq_first");
                String inqLast     = rs.getString("inq_last");
                String inqInfo     = rs.getString("inq_info");
                Inquirer inquirer  = new Inquirer(inqID, inqFirst,
                        inqLast != null ? inqLast : "Unknown",
                        "N/A",
                        inqInfo != null ? inqInfo : "No info");

                // Build missing DisasterVictim
                int subID         = rs.getInt("sub_id");
                String subFirst   = rs.getString("sub_first");
                java.sql.Date subEntryDate = rs.getDate("sub_entry");
                LocalDate subEntry = (subEntryDate != null)
                        ? subEntryDate.toLocalDate() : LocalDate.now();

                DisasterVictim missing = new DisasterVictim(subID, subFirst, subEntry);
                String subLast = rs.getString("sub_last");
                if (subLast != null) missing.setLastName(subLast);

                ReliefService inquiry = new ReliefService(
                        inquiryID, inquirer, missing, date, details, null);
                inquiries.add(inquiry);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load inquiries: " + e.getMessage(), e);
        }

        return inquiries;
    }

    // =========================================================================
    //  Save / Update — Victim
    // =========================================================================

    /**
     * Inserts a new disaster victim into the database by first inserting into
     * Person then into DisasterVictim.
     *
     * @param victim the DisasterVictim to persist
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void saveVictim(DisasterVictim victim) {
        String personSql = "INSERT INTO Person (id, first_name, last_name, comments) " +
                "VALUES (?, ?, ?, ?)";
        String victimSql = "INSERT INTO DisasterVictim " +
                "(person_id, date_of_birth, approximate_age, gender, " +
                "entry_date, is_soft_deleted) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = dbManager.getConnection();

            PreparedStatement pStmt = conn.prepareStatement(personSql);
            pStmt.setInt(1, victim.getVictimID());
            pStmt.setString(2, victim.getFirstName());
            pStmt.setString(3, victim.getLastName());
            pStmt.setString(4, victim.getComments());
            pStmt.executeUpdate();

            PreparedStatement vStmt = conn.prepareStatement(victimSql);
            vStmt.setInt(1, victim.getVictimID());

            if (victim.getDateOfBirth() != null) {
                vStmt.setDate(2, java.sql.Date.valueOf(victim.getDateOfBirth()));
            } else {
                vStmt.setNull(2, java.sql.Types.DATE);
            }

            if (victim.getApproximateAge() != null) {
                vStmt.setInt(3, victim.getApproximateAge());
            } else {
                vStmt.setNull(3, java.sql.Types.INTEGER);
            }

            vStmt.setString(4, victim.getGender());
            vStmt.setDate(5, java.sql.Date.valueOf(victim.getEntryDate()));
            vStmt.setBoolean(6, victim.isSoftDeleted());
            vStmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save victim: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing disaster victim record across the Person and
     * DisasterVictim tables.
     *
     * @param victim the DisasterVictim with updated field values
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void updateVictim(DisasterVictim victim) {
        String personSql = "UPDATE Person SET first_name=?, last_name=?, comments=? " +
                "WHERE id=?";
        String victimSql = "UPDATE DisasterVictim SET date_of_birth=?, " +
                "approximate_age=?, gender=?, is_soft_deleted=? " +
                "WHERE person_id=?";

        try {
            Connection conn = dbManager.getConnection();

            PreparedStatement pStmt = conn.prepareStatement(personSql);
            pStmt.setString(1, victim.getFirstName());
            pStmt.setString(2, victim.getLastName());
            pStmt.setString(3, victim.getComments());
            pStmt.setInt(4, victim.getVictimID());
            pStmt.executeUpdate();

            PreparedStatement vStmt = conn.prepareStatement(victimSql);

            if (victim.getDateOfBirth() != null) {
                vStmt.setDate(1, java.sql.Date.valueOf(victim.getDateOfBirth()));
            } else {
                vStmt.setNull(1, java.sql.Types.DATE);
            }

            if (victim.getApproximateAge() != null) {
                vStmt.setInt(2, victim.getApproximateAge());
            } else {
                vStmt.setNull(2, java.sql.Types.INTEGER);
            }

            vStmt.setString(3, victim.getGender());
            vStmt.setBoolean(4, victim.isSoftDeleted());
            vStmt.setInt(5, victim.getVictimID());
            vStmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update victim: " + e.getMessage(), e);
        }
    }

    /**
     * Sets the is_soft_deleted flag to true for the specified victim.
     *
     * @param victimID the ID of the victim to soft-delete
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void softDeleteVictim(int victimID) {
        String sql = "UPDATE DisasterVictim SET is_soft_deleted=TRUE WHERE person_id=?";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, victimID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to soft-delete victim: " + e.getMessage(), e);
        }
    }

    /**
     * Permanently removes the specified victim from the database.
     * Cascading deletes defined in the schema handle removal of associated
     * medical records, skills, requirements, and family relationships.
     *
     * @param victimID the ID of the victim to hard-delete
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void hardDeleteVictim(int victimID) {
        // Deleting from Person cascades to DisasterVictim and all related tables
        String sql = "DELETE FROM Person WHERE id=?";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, victimID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to hard-delete victim: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    //  Save / Update — Supply
    // =========================================================================

    /**
     * Inserts a new supply record into the Supply table.
     *
     * @param supply the Supply to persist
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void saveSupply(Supply supply) {
        String sql = "INSERT INTO Supply (id, supply_type, expiry_date) " +
                "VALUES (?, ?, ?)";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, supply.getSupplyID());
            stmt.setString(2, supply.getType());

            if (supply.getExpiryDate() != null) {
                stmt.setDate(3, java.sql.Date.valueOf(supply.getExpiryDate()));
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save supply: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing supply record in the Supply table.
     * Also updates the victim allocation if the supply has been allocated.
     *
     * @param supply the Supply with updated values
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void updateSupply(Supply supply) {
        String sql = "UPDATE Supply SET supply_type=?, expiry_date=?, " +
                "victim_id=?, allocation_date=? WHERE id=?";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, supply.getType());

            if (supply.getExpiryDate() != null) {
                stmt.setDate(2, java.sql.Date.valueOf(supply.getExpiryDate()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }

            if (supply.getAllocatedVictim() != null) {
                stmt.setInt(3, supply.getAllocatedVictim().getVictimID());
                stmt.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
                stmt.setNull(4, java.sql.Types.DATE);
            }

            stmt.setInt(5, supply.getSupplyID());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update supply: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    //  Save / Update — Inquiry
    // =========================================================================

    /**
     * Inserts a new inquiry record into the Inquiry table.
     *
     * @param inquiry the ReliefService inquiry to persist
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void saveInquiry(ReliefService inquiry) {
        String sql = "INSERT INTO Inquiry (id, inquirer_id, subject_person_id, " +
                "inquiry_date, details) VALUES (?, ?, ?, ?, ?)";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, inquiry.getInquiryID());
            stmt.setInt(2, inquiry.getInquirer().getInquirerID());
            stmt.setInt(3, inquiry.getMissingPerson().getVictimID());
            stmt.setDate(4, java.sql.Date.valueOf(inquiry.getDateOfInquiry()));
            stmt.setString(5, inquiry.getInfoProvided());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save inquiry: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing inquiry record in the Inquiry table.
     *
     * @param inquiry the ReliefService with updated values
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void updateInquiry(ReliefService inquiry) {
        String sql = "UPDATE Inquiry SET inquirer_id=?, subject_person_id=?, " +
                "inquiry_date=?, details=? WHERE id=?";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, inquiry.getInquirer().getInquirerID());
            stmt.setInt(2, inquiry.getMissingPerson().getVictimID());
            stmt.setDate(3, java.sql.Date.valueOf(inquiry.getDateOfInquiry()));
            stmt.setString(4, inquiry.getInfoProvided());
            stmt.setInt(5, inquiry.getInquiryID());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update inquiry: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    //  Requirements
    // =========================================================================

    /**
     * Inserts a cultural/religious requirement for a victim into the
     * CulturalRequirement table.
     *
     * @param requirement the VictimRequirement to persist
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void saveRequirement(VictimRequirement requirement) {
        String sql = "INSERT INTO CulturalRequirement " +
                "(victim_id, requirement_category, requirement_option) " +
                "VALUES (?, ?, ?)";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, requirement.getVictimID());
            stmt.setString(2, requirement.getRequirementType());
            stmt.setString(3, requirement.getSelectedOption());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save requirement: " + e.getMessage(), e);
        }
    }

    /**
     * Removes a specific requirement category for a victim from the
     * CulturalRequirement table.
     *
     * @param victimID        ID of the victim
     * @param requirementType the requirement_category value to remove
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void deleteRequirement(int victimID, String requirementType) {
        String sql = "DELETE FROM CulturalRequirement " +
                "WHERE victim_id=? AND requirement_category=?";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, victimID);
            stmt.setString(2, requirementType);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete requirement: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    //  Skills
    // =========================================================================

    /**
     * Inserts a skill record into the database. First ensures the skill exists
     * in the Skill catalogue table, then inserts a VictimSkill record linking
     * the victim to the skill with proficiency and type-specific details.
     *
     * @param skill the Skill to persist
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void saveSkill(Skill skill) {
        // Ensure the skill exists in the Skill catalogue
        String skillSql = "INSERT INTO Skill (skill_name, category) " +
                "VALUES (?, ?) ON CONFLICT (skill_name, category) DO NOTHING";

        // Insert into VictimSkill linking table
        String victimSkillSql = "INSERT INTO VictimSkill " +
                "(victim_id, skill_id, details, language_capabilities, " +
                "certification_expiry, proficiency_level) " +
                "VALUES (?, " +
                "(SELECT id FROM Skill WHERE skill_name=? AND category=?), " +
                "?, ?, ?, ?)";

        try {
            Connection conn = dbManager.getConnection();

            // Determine skill_name and category based on type
            String skillName;
            String category = skill.getCategory().toLowerCase();
            String details = null;
            String langCapabilities = null;
            java.sql.Date certExpiry = null;

            if (skill instanceof MedicalSkill ms) {
                skillName    = ms.getCertificationType();
                details      = ms.getCertificationType();
                certExpiry   = java.sql.Date.valueOf(ms.getCertificationExpiryDate());

            } else if (skill instanceof LanguageSkill ls) {
                skillName       = ls.getLanguageName();
                StringBuilder caps = new StringBuilder();
                if (ls.hasReadWrite())   caps.append("read/write");
                if (ls.hasSpeakListen()) {
                    if (caps.length() > 0) caps.append(", ");
                    caps.append("speak/listen");
                }
                langCapabilities = caps.toString();

            } else if (skill instanceof TradeSkill ts) {
                skillName = ts.getTradeType();
                details   = ts.getTradeType();

            } else {
                throw new IllegalArgumentException("Unknown skill type.");
            }

            // Insert into Skill catalogue
            PreparedStatement sStmt = conn.prepareStatement(skillSql);
            sStmt.setString(1, skillName);
            sStmt.setString(2, category);
            sStmt.executeUpdate();

            // Insert into VictimSkill
            PreparedStatement vsStmt = conn.prepareStatement(victimSkillSql);
            vsStmt.setInt(1, skill.getVictimID());
            vsStmt.setString(2, skillName);
            vsStmt.setString(3, category);
            vsStmt.setString(4, details);
            vsStmt.setString(5, langCapabilities);
            vsStmt.setDate(6, certExpiry);
            vsStmt.setString(7, skill.getProficiencyLevel());
            vsStmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save skill: " + e.getMessage(), e);
        }
    }

    /**
     * Removes a VictimSkill record from the database by its ID.
     *
     * @param skillID the ID of the VictimSkill entry to delete
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void deleteSkill(int skillID) {
        String sql = "DELETE FROM VictimSkill WHERE id=?";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, skillID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete skill: " + e.getMessage(), e);
        }
    }

    /**
     * Inserts a new medical record for a victim into the MedicalRecord table.
     *
     * @param record   the MedicalRecord to persist
     * @param victimID the ID of the victim this record belongs to
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void saveMedicalRecord(MedicalRecord record, int victimID) {
        String sql = "INSERT INTO MedicalRecord " +
                "(victim_id, treatment_details, treatment_date, location_id) " +
                "VALUES (?, ?, ?, ?)";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, victimID);
            stmt.setString(2, record.getTreatmentDetails());
            stmt.setDate(3, java.sql.Date.valueOf(record.getDateOfTreatment()));
            stmt.setInt(4, record.getLocation().getLocationID());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save medical record: " + e.getMessage(), e);
        }
    }

    /**
     * Inserts a family relationship record into the FamilyRelationship table.
     *
     * @param relation the FamilyRelation to persist
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void saveFamilyConnection(FamilyRelation relation) {
        String sql = "INSERT INTO FamilyRelationship " +
                "(person_one_id, person_two_id, relationship_type) " +
                "VALUES (?, ?, ?)";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, relation.getPersonOne().getVictimID());
            stmt.setInt(2, relation.getPersonTwo().getVictimID());
            stmt.setString(3, relation.getRelationshipTo());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save family connection: " + e.getMessage(), e);
        }
    }
}