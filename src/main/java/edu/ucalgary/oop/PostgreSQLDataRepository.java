package edu.ucalgary.oop;

/**
 * PostgreSQLDataRepository
 *
 * Concrete implementation of DataRepository that communicates with a
 * PostgreSQL database through the JDBC connection supplied by DatabaseManager.
 * All SQL operations are mediated here, keeping database logic isolated from
 * the rest of the application (Dependency Inversion Principle).
 *
 * The database uses the schema defined in project.sql with username "oop"
 * and password "ucalgary".  No tables or fields are modified.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
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
     * Loads all non-hard-deleted disaster victim records from the database,
     * populating their requirements and skills from the corresponding tables.
     *
     * @return list of DisasterVictim objects; empty list if none found
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public ArrayList<DisasterVictim> loadVictims() {
        ArrayList<DisasterVictim> victims = new ArrayList<>();
        String sql = "SELECT * FROM disaster_victims";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id           = rs.getInt("victim_id");
                String firstName = rs.getString("first_name");
                LocalDate entry  = rs.getDate("entry_date").toLocalDate();

                DisasterVictim victim;

                // Prefer exact DOB; fall back to approximate age
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
                if (gender != null) victim.setGender(gender);

                String comments = rs.getString("comments");
                if (comments != null) victim.setComments(comments);

                boolean softDeleted = rs.getBoolean("soft_deleted");
                if (softDeleted) victim.softDelete();

                victims.add(victim);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load victims: " + e.getMessage(), e);
        }

        return victims;
    }

    /**
     * Loads all location records from the database.
     *
     * @return list of Location objects; empty list if none found
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public ArrayList<Location> loadLocations() {
        ArrayList<Location> locations = new ArrayList<>();
        String sql = "SELECT * FROM locations";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id      = rs.getInt("location_id");
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
     * Loads all supply records from the database.
     *
     * @return list of Supply objects; empty list if none found
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public ArrayList<Supply> loadSupplies() {
        ArrayList<Supply> supplies = new ArrayList<>();
        String sql = "SELECT * FROM supplies";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id       = rs.getInt("supply_id");
                String type  = rs.getString("type");
                int qty      = rs.getInt("quantity");
                boolean peri = rs.getBoolean("perishable");

                java.sql.Date expSql = rs.getDate("expiry_date");
                LocalDate expiry     = (expSql != null) ? expSql.toLocalDate() : null;

                Supply supply = new Supply(id, type, qty, peri, expiry);
                supplies.add(supply);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load supplies: " + e.getMessage(), e);
        }

        return supplies;
    }

    /**
     * Loads all inquiry (ReliefService) records from the database.
     *
     * @return list of ReliefService objects; empty list if none found
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public ArrayList<ReliefService> loadInquiries() {
        // TODO: implement with full joins to Inquirer, DisasterVictim, Location
        return new ArrayList<>();
    }

    // =========================================================================
    //  Save / Update — Victim
    // =========================================================================

    /**
     * Inserts a new disaster victim record into the database.
     *
     * @param victim the DisasterVictim to persist
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void saveVictim(DisasterVictim victim) {
        String sql = "INSERT INTO disaster_victims "
                + "(victim_id, first_name, last_name, date_of_birth, approximate_age, "
                + "gender, comments, entry_date, soft_deleted) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, victim.getVictimID());
            stmt.setString(2, victim.getFirstName());
            stmt.setString(3, victim.getLastName());

            if (victim.getDateOfBirth() != null) {
                stmt.setDate(4, java.sql.Date.valueOf(victim.getDateOfBirth()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            if (victim.getApproximateAge() != null) {
                stmt.setInt(5, victim.getApproximateAge());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }

            stmt.setString(6, victim.getGender());
            stmt.setString(7, victim.getComments());
            stmt.setDate(8, java.sql.Date.valueOf(victim.getEntryDate()));
            stmt.setBoolean(9, victim.isSoftDeleted());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save victim: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing disaster victim record in the database.
     *
     * @param victim the DisasterVictim with updated field values
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void updateVictim(DisasterVictim victim) {
        String sql = "UPDATE disaster_victims SET "
                + "first_name=?, last_name=?, date_of_birth=?, approximate_age=?, "
                + "gender=?, comments=?, soft_deleted=? "
                + "WHERE victim_id=?";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, victim.getFirstName());
            stmt.setString(2, victim.getLastName());

            if (victim.getDateOfBirth() != null) {
                stmt.setDate(3, java.sql.Date.valueOf(victim.getDateOfBirth()));
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }

            if (victim.getApproximateAge() != null) {
                stmt.setInt(4, victim.getApproximateAge());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            stmt.setString(5, victim.getGender());
            stmt.setString(6, victim.getComments());
            stmt.setBoolean(7, victim.isSoftDeleted());
            stmt.setInt(8, victim.getVictimID());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update victim: " + e.getMessage(), e);
        }
    }

    /**
     * Sets the soft_deleted flag to true for the specified victim in the database.
     *
     * @param victimID the ID of the victim to soft-delete
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void softDeleteVictim(int victimID) {
        String sql = "UPDATE disaster_victims SET soft_deleted=TRUE WHERE victim_id=?";

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
     * Permanently removes the specified victim and all their associated records
     * (medical records, family relations, skills, requirements, inquiries) from
     * the database.  Cascading deletes are expected to be defined in the schema.
     *
     * @param victimID the ID of the victim to hard-delete
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void hardDeleteVictim(int victimID) {
        String sql = "DELETE FROM disaster_victims WHERE victim_id=?";

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
     * Inserts a new supply record into the database.
     *
     * @param supply the Supply to persist
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void saveSupply(Supply supply) {
        String sql = "INSERT INTO supplies (supply_id, type, quantity, perishable, expiry_date) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, supply.getSupplyID());
            stmt.setString(2, supply.getType());
            stmt.setInt(3, supply.getQuantity());
            stmt.setBoolean(4, supply.isPerishable());

            if (supply.getExpiryDate() != null) {
                stmt.setDate(5, java.sql.Date.valueOf(supply.getExpiryDate()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save supply: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing supply record in the database.
     *
     * @param supply the Supply with updated values
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void updateSupply(Supply supply) {
        String sql = "UPDATE supplies SET type=?, quantity=?, perishable=?, expiry_date=? "
                + "WHERE supply_id=?";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, supply.getType());
            stmt.setInt(2, supply.getQuantity());
            stmt.setBoolean(3, supply.isPerishable());

            if (supply.getExpiryDate() != null) {
                stmt.setDate(4, java.sql.Date.valueOf(supply.getExpiryDate()));
            } else {
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
     * Inserts a new inquiry record into the database.
     *
     * @param inquiry the ReliefService (inquiry) to persist
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void saveInquiry(ReliefService inquiry) {
        // TODO: implement with full field mapping to inquiry table
    }

    /**
     * Updates an existing inquiry record in the database.
     *
     * @param inquiry the ReliefService with updated values
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void updateInquiry(ReliefService inquiry) {
        // TODO: implement with full field mapping to inquiry table
    }

    // =========================================================================
    //  Requirements
    // =========================================================================

    /**
     * Inserts a cultural/religious requirement for a victim into the database.
     *
     * @param requirement the VictimRequirement to persist
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void saveRequirement(VictimRequirement requirement) {
        String sql = "INSERT INTO victim_requirements (victim_id, requirement_type, selected_option) "
                + "VALUES (?, ?, ?)";

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
     * Removes a specific requirement type for a victim from the database.
     *
     * @param victimID        ID of the victim
     * @param requirementType the type of requirement to remove
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void deleteRequirement(int victimID, String requirementType) {
        String sql = "DELETE FROM victim_requirements WHERE victim_id=? AND requirement_type=?";

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
     * Inserts a skill record into the database.  The concrete skill type is
     * determined at runtime and the appropriate type-specific fields are saved.
     *
     * @param skill the Skill to persist
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void saveSkill(Skill skill) {
        String sql = "INSERT INTO skills "
                + "(skill_id, victim_id, category, proficiency_level, "
                + "certification_type, certification_expiry, language_name, "
                + "read_write, speak_listen, trade_type) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, skill.getSkillID());
            stmt.setInt(2, skill.getVictimID());
            stmt.setString(3, skill.getCategory());
            stmt.setString(4, skill.getProficiencyLevel());

            if (skill instanceof MedicalSkill ms) {
                stmt.setString(5, ms.getCertificationType());
                stmt.setDate(6, java.sql.Date.valueOf(ms.getCertificationExpiryDate()));
                stmt.setNull(7, java.sql.Types.VARCHAR);
                stmt.setNull(8, java.sql.Types.BOOLEAN);
                stmt.setNull(9, java.sql.Types.BOOLEAN);
                stmt.setNull(10, java.sql.Types.VARCHAR);

            } else if (skill instanceof LanguageSkill ls) {
                stmt.setNull(5, java.sql.Types.VARCHAR);
                stmt.setNull(6, java.sql.Types.DATE);
                stmt.setString(7, ls.getLanguageName());
                stmt.setBoolean(8, ls.hasReadWrite());
                stmt.setBoolean(9, ls.hasSpeakListen());
                stmt.setNull(10, java.sql.Types.VARCHAR);

            } else if (skill instanceof TradeSkill ts) {
                stmt.setNull(5, java.sql.Types.VARCHAR);
                stmt.setNull(6, java.sql.Types.DATE);
                stmt.setNull(7, java.sql.Types.VARCHAR);
                stmt.setNull(8, java.sql.Types.BOOLEAN);
                stmt.setNull(9, java.sql.Types.BOOLEAN);
                stmt.setString(10, ts.getTradeType());
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save skill: " + e.getMessage(), e);
        }
    }

    /**
     * Removes a skill record from the database by its ID.
     *
     * @param skillID the ID of the Skill to delete
     * @throws RuntimeException wrapping any SQLException
     */
    @Override
    public void deleteSkill(int skillID) {
        String sql = "DELETE FROM skills WHERE skill_id=?";

        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, skillID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete skill: " + e.getMessage(), e);
        }
    }
}