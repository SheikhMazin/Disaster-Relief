package edu.ucalgary.oop;

/**
 * VictimManagementUI
 *
 * Helper class responsible for building and displaying the Victim Management
 * screen within the application's content panel. Handles showing the victim
 * table, adding new victims, viewing victim details, soft deleting, and hard
 * deleting victims. All data operations are delegated to the ReliefController
 * — no business logic exists in this class.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class VictimManagementUI {

    private final ReliefController controller;
    private final VictimUI victimUI;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs a VictimManagementUI backed by the given controller and
     * victimUI helper.
     *
     * @param controller non-null ReliefController for all data operations
     * @param victimUI   non-null VictimUI for building victim detail tab panels
     * @throws IllegalArgumentException if controller or victimUI is null
     */
    public VictimManagementUI(ReliefController controller, VictimUI victimUI) {
        if (controller == null) {
            throw new IllegalArgumentException("controller cannot be null.");
        }
        if (victimUI == null) {
            throw new IllegalArgumentException("victimUI cannot be null.");
        }
        this.controller = controller;
        this.victimUI   = victimUI;
    }

    // =========================================================================
    //  Main screen
    // =========================================================================

    /**
     * Clears the given content panel and renders the Victim Management screen.
     * Displays a table of all active (non-soft-deleted) disaster victims and
     * provides buttons to add a new victim, view full details, soft delete,
     * or hard delete a selected victim.
     *
     * @param contentPanel the JPanel to render this screen into
     */
    public void show(JPanel contentPanel) {
        contentPanel.removeAll();

        // ── Get active victims from controller ────────────────────────────────
        ArrayList<DisasterVictim> victims = controller.getActiveVictims();

        // ── Build victims table ───────────────────────────────────────────────
        String[] columns = {"ID", "First Name", "Last Name", "Gender", "DOB / Age"};
        Object[][] data  = new Object[victims.size()][5];

        for (int i = 0; i < victims.size(); i++) {
            DisasterVictim v = victims.get(i);
            data[i][0] = v.getVictimID();
            data[i][1] = v.getFirstName();
            data[i][2] = v.getLastName();
            data[i][3] = v.getGender();
            data[i][4] = v.getDateOfBirth() != null
                    ? v.getDateOfBirth().toString()
                    : "~" + v.getApproximateAge() + " yrs";
        }

        JTable table = new JTable(data, columns);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);

        // ── Action buttons ────────────────────────────────────────────────────
        JButton addBtn        = new JButton("Add Victim");
        JButton softDeleteBtn = new JButton("Soft Delete");
        JButton hardDeleteBtn = new JButton("Hard Delete");
        JButton viewDetailsBtn = new JButton("View Details");

        hardDeleteBtn.setBackground(new Color(180, 60, 60));
        hardDeleteBtn.setForeground(Color.WHITE);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(addBtn);
        btnPanel.add(softDeleteBtn);
        btnPanel.add(viewDetailsBtn);
        btnPanel.add(hardDeleteBtn);

        // ── Add victim action ─────────────────────────────────────────────────
        addBtn.addActionListener(e -> {
            JTextField firstNameField    = new JTextField();
            JTextField lastNameField     = new JTextField();
            JTextField ageField          = new JTextField();
            JTextField dobField          = new JTextField("YYYY-MM-DD");
            JTextField customGenderField = new JTextField();
            customGenderField.setVisible(false);

            String[] genderOptions      = {"Man", "Woman", "Boy", "Girl",
                    "Non-binary person", "Please Specify"};
            JComboBox<String> genderBox = new JComboBox<>(genderOptions);

            // Show custom gender field only when Please Specify is selected
            genderBox.addActionListener(c -> {
                boolean isPlease = genderBox.getSelectedItem().equals("Please Specify");
                customGenderField.setVisible(isPlease);
            });

            Object[] fields = {
                    "First Name:",  firstNameField,
                    "Last Name:",   lastNameField,
                    "Gender:",      genderBox,
                    "Specify gender (if Please Specify):", customGenderField,
                    "Date of Birth (leave blank if unknown):", dobField,
                    "Approx Age (leave blank if DOB known):",  ageField
            };

            int result = JOptionPane.showConfirmDialog(
                    contentPanel, fields, "Add Victim", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String firstName = firstNameField.getText().trim();
                    String lastName  = lastNameField.getText().trim();
                    String dob       = dobField.getText().trim();
                    String age       = ageField.getText().trim();

                    int id = controller.getNextVictimID();

                    // Create victim with DOB, approximate age, or neither
                    DisasterVictim victim;
                    if (!dob.isEmpty() && !dob.equals("YYYY-MM-DD")) {
                        victim = new DisasterVictim(id, firstName,
                                LocalDate.now(), LocalDate.parse(dob));
                    } else if (!age.isEmpty()) {
                        victim = new DisasterVictim(id, firstName,
                                LocalDate.now(), Integer.parseInt(age));
                    } else {
                        victim = new DisasterVictim(id, firstName, LocalDate.now());
                    }

                    if (!lastName.isEmpty()) victim.setLastName(lastName);

                    // Handle Please Specify gender — two step process
                    if (genderBox.getSelectedItem().equals("Please Specify")) {
                        victim.setGender("Please Specify");
                        if (!customGenderField.getText().trim().isEmpty()) {
                            victim.setGender(customGenderField.getText().trim());
                        }
                    } else {
                        victim.setGender((String) genderBox.getSelectedItem());
                    }

                    controller.addVictim(victim);
                    show(contentPanel);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(contentPanel,
                            "Error adding victim: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ── Soft delete action ────────────────────────────────────────────────
        softDeleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(contentPanel,
                        "Please select a victim first.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int victimID = (int) table.getValueAt(selectedRow, 0);
            int confirm  = JOptionPane.showConfirmDialog(contentPanel,
                    "Soft delete victim ID " + victimID +
                            "?\nThey will be hidden from the UI but their data is kept.",
                    "Confirm Soft Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.softDeleteVictim(victimID);
                    show(contentPanel);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(contentPanel,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ── Hard delete action ────────────────────────────────────────────────
        hardDeleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(contentPanel,
                        "Please select a victim first.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int victimID = (int) table.getValueAt(selectedRow, 0);
            int confirm  = JOptionPane.showConfirmDialog(contentPanel,
                    "PERMANENTLY delete victim ID " + victimID +
                            "?\nAll associated records will be removed.\nThis cannot be undone.",
                    "Confirm Hard Delete", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.hardDeleteVictim(victimID);
                    show(contentPanel);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(contentPanel,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ── View details action ───────────────────────────────────────────────
        viewDetailsBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(contentPanel,
                        "Please select a victim first.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int victimID          = (int) table.getValueAt(selectedRow, 0);
            DisasterVictim victim = controller.getVictimByID(victimID);

            // Build tabbed detail dialog using VictimUI helpers
            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Info",            victimUI.buildInfoPanel(victim));
            tabs.addTab("Medical Records", victimUI.buildMedicalPanel(victim, tabs, 1));
            tabs.addTab("Family",          victimUI.buildFamilyPanel(victim, tabs, 2));
            tabs.addTab("Requirements",    victimUI.buildRequirementsPanel(victim, tabs, 3));
            tabs.addTab("Skills",          victimUI.buildSkillsPanel(victim, tabs, 4));

            JDialog dialog = new JDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(contentPanel),
                    "Victim Details - " + victim.getFirstName(), true);
            dialog.setSize(700, 500);
            dialog.setLocationRelativeTo(contentPanel);
            dialog.setLayout(new BorderLayout());
            dialog.add(tabs, BorderLayout.CENTER);
            dialog.setVisible(true);
        });

        // ── Assemble and display the victim management panel ──────────────────
        JPanel panel   = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Victim Management");
        heading.setFont(new Font("Arial", Font.BOLD, 18));
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        panel.add(heading,    BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel,   BorderLayout.SOUTH);

        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}