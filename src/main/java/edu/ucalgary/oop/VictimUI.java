package edu.ucalgary.oop;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class VictimUI  {

    private ReliefController controller;
    private JPanel mainPanel;
    private JPanel contentPanel;


    public VictimUI(ReliefController controller){
        this.controller = controller;
    }

    /**
     * Builds the Info tab panel for the victim details dialog.
     * Displays all basic information about the given victim in a
     * two-column label/value grid layout including ID, name, gender,
     * age, entry date, comments, and active/soft-deleted status.
     *
     * @param victim the DisasterVictim whose information to display
     * @return a JPanel containing the victim's full info in a grid layout
     */
    public JPanel buildInfoPanel(DisasterVictim victim) {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font valueFont = new Font("Arial", Font.PLAIN, 14);

        int row = 0;

        // ── Heading ───────────────────────────────────────────────────────────────
        JLabel heading = new JLabel("Victim Information");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        infoPanel.add(heading, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        row++;

        // ── ID ────────────────────────────────────────────────────────────────────
        JLabel idLabel = new JLabel("Victim ID:");
        JLabel idValue = new JLabel(String.valueOf(victim.getVictimID()));
        idLabel.setFont(labelFont);
        idValue.setFont(valueFont);
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        infoPanel.add(idValue, gbc);
        row++;

        // ── First Name ────────────────────────────────────────────────────────────
        JLabel firstNameLabel = new JLabel("First Name:");
        JLabel firstNameValue = new JLabel(victim.getFirstName());
        firstNameLabel.setFont(labelFont);
        firstNameValue.setFont(valueFont);
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(firstNameLabel, gbc);
        gbc.gridx = 1;
        infoPanel.add(firstNameValue, gbc);
        row++;

        // ── Last Name ─────────────────────────────────────────────────────────────
        JLabel lastNameLabel = new JLabel("Last Name:");
        JLabel lastNameValue = new JLabel(victim.getLastName() != null
                ? victim.getLastName() : "N/A");
        lastNameLabel.setFont(labelFont);
        lastNameValue.setFont(valueFont);
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(lastNameLabel, gbc);
        gbc.gridx = 1;
        infoPanel.add(lastNameValue, gbc);
        row++;

        // ── Gender ────────────────────────────────────────────────────────────────
        JLabel genderLabel = new JLabel("Gender:");
        JLabel genderValue = new JLabel(victim.getGender() != null
                ? victim.getGender() : "N/A");
        genderLabel.setFont(labelFont);
        genderValue.setFont(valueFont);
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(genderLabel, gbc);
        gbc.gridx = 1;
        infoPanel.add(genderValue, gbc);
        row++;

        // ── Date of Birth / Approximate Age ──────────────────────────────────────
        JLabel ageLabel = new JLabel("Date of Birth / Age:");
        String ageInfo = victim.getDateOfBirth() != null
                ? victim.getDateOfBirth().toString()
                : victim.getApproximateAge() != null
                  ? "~" + victim.getApproximateAge() + " yrs"
                  : "N/A";
        JLabel ageValue = new JLabel(ageInfo);
        ageLabel.setFont(labelFont);
        ageValue.setFont(valueFont);
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(ageLabel, gbc);
        gbc.gridx = 1;
        infoPanel.add(ageValue, gbc);
        row++;

        // ── Entry Date ────────────────────────────────────────────────────────────
        JLabel entryLabel = new JLabel("Entry Date:");
        JLabel entryValue = new JLabel(victim.getEntryDate().toString());
        entryLabel.setFont(labelFont);
        entryValue.setFont(valueFont);
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(entryLabel, gbc);
        gbc.gridx = 1;
        infoPanel.add(entryValue, gbc);
        row++;

        // ── Comments ──────────────────────────────────────────────────────────────
        JLabel commentsLabel = new JLabel("Comments:");
        JLabel commentsValue = new JLabel(victim.getComments() != null
                ? victim.getComments() : "N/A");
        commentsLabel.setFont(labelFont);
        commentsValue.setFont(valueFont);
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(commentsLabel, gbc);
        gbc.gridx = 1;
        infoPanel.add(commentsValue, gbc);
        row++;

        // ── Soft Deleted status ───────────────────────────────────────────────────
        JLabel deletedLabel = new JLabel("Status:");
        JLabel deletedValue = new JLabel(victim.isSoftDeleted() ? "Soft Deleted" : "Active");
        deletedLabel.setFont(labelFont);
        deletedValue.setFont(valueFont);
        deletedValue.setForeground(victim.isSoftDeleted()
                ? new Color(180, 60, 60) : new Color(60, 150, 60));
        gbc.gridx = 0; gbc.gridy = row;
        infoPanel.add(deletedLabel, gbc);
        gbc.gridx = 1;
        infoPanel.add(deletedValue, gbc);

        return infoPanel;
    }


    /**
     * Builds the Medical Records tab panel for the victim details dialog.
     * Displays a table of all medical records for the given victim and
     * provides a button to add a new medical record with a location,
     * treatment details, and date. The tab refreshes immediately after
     * a successful add without closing the dialog.
     *
     * @param victim   the DisasterVictim whose medical records to display
     * @param tabs     the parent JTabbedPane used to refresh this tab on update
     * @param tabIndex the index of this tab within the JTabbedPane
     * @return a JPanel containing the medical records table and add button
     */
    public JPanel buildMedicalPanel(DisasterVictim victim, JTabbedPane tabs, int tabIndex) {
        ArrayList<MedicalRecord> medicalRecords = victim.getMedicalRecords();
        String[] columns = {"Location", "Treatment Details", "Date of Treatment"};
        Object[][] data  = new Object[medicalRecords.size()][3];

        for (int i = 0; i < medicalRecords.size(); i++) {
            MedicalRecord record = medicalRecords.get(i);
            data[i][0] = record.getLocation().getName();
            data[i][1] = record.getTreatmentDetails();
            data[i][2] = record.getDateOfTreatment();
        }

        JPanel medicalRecordPanel = new JPanel(new BorderLayout());
        JTable table = new JTable(data, columns);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        medicalRecordPanel.add(scrollPane, BorderLayout.CENTER);

        JButton addBtn = new JButton("Add Medical Record");

        addBtn.addActionListener(e -> {
            ArrayList<Location> locations = controller.getLocations();
            String[] locationNames = new String[locations.size()];
            for (int i = 0; i < locations.size(); i++) {
                locationNames[i] = locations.get(i).getName();
            }

            JComboBox<String> locationBox = new JComboBox<>(locationNames);
            JTextField treatmentField     = new JTextField();
            JTextField dateField          = new JTextField(LocalDate.now().toString());

            Object[] fields = {
                    "Location:",          locationBox,
                    "Treatment Details:", treatmentField,
                    "Date (YYYY-MM-DD):", dateField
            };

            int result = JOptionPane.showConfirmDialog(
                    null, fields, "Add Medical Record", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    Location selectedLocation = locations.get(locationBox.getSelectedIndex());
                    String treatment          = treatmentField.getText().trim();
                    LocalDate date            = LocalDate.parse(dateField.getText().trim());

                    MedicalRecord record = new MedicalRecord(
                            selectedLocation, treatment, date);
                    controller.addMedicalRecord(victim.getVictimID(), record);

                    // ── refresh the tab immediately ───────────────────────────
                    tabs.setComponentAt(tabIndex,
                            buildMedicalPanel(victim, tabs, tabIndex));
                    tabs.revalidate();
                    tabs.repaint();
                    // ─────────────────────────────────────────────────────────

                    JOptionPane.showMessageDialog(null,
                            "Medical record added successfully!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(addBtn);
        medicalRecordPanel.add(btnPanel, BorderLayout.SOUTH);

        return medicalRecordPanel;
    }

    /**
     * Builds the Family Connections tab panel for the victim details dialog.
     * Displays a table of all family relationships for the given victim and
     * provides a button to add a new family connection.
     *
     * @param victim   the DisasterVictim whose family connections to display
     * @param tabs     the parent JTabbedPane used to refresh this tab on update
     * @param tabIndex the index of this tab within the JTabbedPane
     * @return a JPanel containing the family connections table and add button
     */
    public JPanel buildFamilyPanel(DisasterVictim victim, JTabbedPane tabs, int tabIndex) {
        ArrayList<FamilyRelation> connections = victim.getFamilyConnections();
        String[] columns = {"Related Person", "Relationship"};
        Object[][] data  = new Object[connections.size()][2];

        for (int i = 0; i < connections.size(); i++) {
            FamilyRelation relation = connections.get(i);
            DisasterVictim related  = relation.getPersonTwo();
            data[i][0] = related.getFirstName() + " " +
                    (related.getLastName() != null ? related.getLastName() : "");
            data[i][1] = relation.getRelationshipTo();
        }

        JPanel familyPanel = new JPanel(new BorderLayout());
        JTable table       = new JTable(data, columns);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        familyPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton addBtn = new JButton("Add Family Connection");

        addBtn.addActionListener(e -> {
            ArrayList<DisasterVictim> allVictims = controller.getActiveVictims();
            // remove current victim from options
            allVictims.removeIf(v -> v.getVictimID() == victim.getVictimID());

            String[] victimNames = new String[allVictims.size()];
            for (int i = 0; i < allVictims.size(); i++) {
                victimNames[i] = allVictims.get(i).getFirstName() + " " +
                        (allVictims.get(i).getLastName() != null
                                ? allVictims.get(i).getLastName() : "");
            }

            JComboBox<String> victimBox      = new JComboBox<>(victimNames);
            JTextField relationshipField     = new JTextField();

            Object[] fields = {
                    "Related Person:",  victimBox,
                    "Relationship:",    relationshipField
            };

            int result = JOptionPane.showConfirmDialog(
                    null, fields, "Add Family Connection", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    DisasterVictim related = allVictims.get(victimBox.getSelectedIndex());
                    String relationship    = relationshipField.getText().trim();

                    FamilyRelation relation = new FamilyRelation(victim, relationship, related);
                    controller.addFamilyConnection(victim.getVictimID(), relation);

                    tabs.setComponentAt(tabIndex, buildFamilyPanel(victim, tabs, tabIndex));
                    tabs.revalidate();
                    tabs.repaint();

                    JOptionPane.showMessageDialog(null, "Family connection added successfully!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(addBtn);
        familyPanel.add(btnPanel, BorderLayout.SOUTH);

        return familyPanel;
    }

    /**
     * Builds the Skills tab panel for the victim details dialog.
     * Displays a table of all skills registered to the given victim and
     * provides buttons to add or remove skills.
     *
     * @param victim   the DisasterVictim whose skills to display
     * @param tabs     the parent JTabbedPane used to refresh this tab on update
     * @param tabIndex the index of this tab within the JTabbedPane
     * @return a JPanel containing the skills table and add/remove buttons
     */
    public JPanel buildSkillsPanel(DisasterVictim victim, JTabbedPane tabs, int tabIndex) {
        ArrayList<Skill> skills  = victim.getSkills();
        String[] columns         = {"ID", "Category", "Proficiency", "Details"};
        Object[][] data          = new Object[skills.size()][4];

        for (int i = 0; i < skills.size(); i++) {
            Skill skill  = skills.get(i);
            data[i][0]   = skill.getSkillID();
            data[i][1]   = skill.getCategory();
            data[i][2]   = skill.getProficiencyLevel();

            if (skill instanceof MedicalSkill ms) {
                data[i][3] = ms.getCertificationType() + " (exp: " +
                        ms.getCertificationExpiryDate() + ")";
            } else if (skill instanceof LanguageSkill ls) {
                data[i][3] = ls.getLanguageName() + " — " +
                        (ls.hasReadWrite() ? "read/write " : "") +
                        (ls.hasSpeakListen() ? "speak/listen" : "");
            } else if (skill instanceof TradeSkill ts) {
                data[i][3] = ts.getTradeType();
            }
        }

        JPanel skillsPanel = new JPanel(new BorderLayout());
        JTable table       = new JTable(data, columns);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        skillsPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton addBtn    = new JButton("Add Skill");
        JButton removeBtn = new JButton("Remove Skill");

        // ── Add skill action ──────────────────────────────────────────────────────
        addBtn.addActionListener(e -> {
            String[] categories      = {"Medical", "Language", "Trade"};
            String[] proficiencies   = {"beginner", "intermediate", "advanced"};
            String[] medTypes        = {"first-aid", "counseling", "nursing", "doctor"};
            String[] tradeTypes      = {"carpentry", "plumbing", "electricity"};

            JComboBox<String> categoryBox    = new JComboBox<>(categories);
            JComboBox<String> proficiencyBox = new JComboBox<>(proficiencies);
            JTextField detailField           = new JTextField();
            JTextField expiryField           = new JTextField("YYYY-MM-DD");
            JComboBox<String> medTypeBox     = new JComboBox<>(medTypes);
            JComboBox<String> tradeTypeBox   = new JComboBox<>(tradeTypes);
            JCheckBox readWriteBox           = new JCheckBox("Read/Write");
            JCheckBox speakListenBox         = new JCheckBox("Speak/Listen");

            Object[] fields = {
                    "Category:",    categoryBox,
                    "Proficiency:", proficiencyBox,
                    "Medical Type (if Medical):",  medTypeBox,
                    "Expiry Date (if Medical):",   expiryField,
                    "Language Name (if Language):", detailField,
                    readWriteBox, speakListenBox,
                    "Trade Type (if Trade):",      tradeTypeBox
            };

            int result = JOptionPane.showConfirmDialog(
                    null, fields, "Add Skill", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String category    = (String) categoryBox.getSelectedItem();
                    String proficiency = (String) proficiencyBox.getSelectedItem();
                    int skillID        = (int)(Math.random() * 10000) + 1;

                    Skill skill;
                    switch (category) {
                        case "Medical" -> {
                            String certType  = (String) medTypeBox.getSelectedItem();
                            LocalDate expiry = LocalDate.parse(expiryField.getText().trim());
                            skill = new MedicalSkill(skillID, victim.getVictimID(),
                                    proficiency, certType, expiry);
                        }
                        case "Language" -> {
                            String langName = detailField.getText().trim();
                            skill = new LanguageSkill(skillID, victim.getVictimID(),
                                    proficiency, langName,
                                    readWriteBox.isSelected(),
                                    speakListenBox.isSelected());
                        }
                        case "Trade" -> {
                            String tradeType = (String) tradeTypeBox.getSelectedItem();
                            skill = new TradeSkill(skillID, victim.getVictimID(),
                                    proficiency, tradeType);
                        }
                        default -> throw new IllegalArgumentException("Unknown category.");
                    }

                    controller.addSkill(victim.getVictimID(), skill);

                    tabs.setComponentAt(tabIndex, buildSkillsPanel(victim, tabs, tabIndex));
                    tabs.revalidate();
                    tabs.repaint();

                    JOptionPane.showMessageDialog(null, "Skill added successfully!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ── Remove skill action ───────────────────────────────────────────────────
        removeBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null,
                        "Please select a skill first.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int skillID = (int) table.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Remove this skill?", "Confirm", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.removeSkill(victim.getVictimID(), skillID);

                    tabs.setComponentAt(tabIndex, buildSkillsPanel(victim, tabs, tabIndex));
                    tabs.revalidate();
                    tabs.repaint();

                    JOptionPane.showMessageDialog(null, "Skill removed successfully!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(addBtn);
        btnPanel.add(removeBtn);
        skillsPanel.add(btnPanel, BorderLayout.SOUTH);

        return skillsPanel;
    }

    /**
     * Builds the Requirements tab panel for the victim details dialog.
     * Displays a table of all cultural and religious requirements for the
     * given victim and provides buttons to add or remove requirements.
     * Available requirement types and options are loaded from the
     * available_requirements1.ser file via the controller.
     *
     * @param victim   the DisasterVictim whose requirements to display
     * @param tabs     the parent JTabbedPane used to refresh this tab on update
     * @param tabIndex the index of this tab within the JTabbedPane
     * @return a JPanel containing the requirements table and add/remove buttons
     */
    public JPanel buildRequirementsPanel(DisasterVictim victim, JTabbedPane tabs, int tabIndex) {
        ArrayList<VictimRequirement> requirements = victim.getRequirements();
        String[] columns = {"Requirement Type", "Selected Option"};
        Object[][] data  = new Object[requirements.size()][2];

        for (int i = 0; i < requirements.size(); i++) {
            VictimRequirement req = requirements.get(i);
            data[i][0] = req.getRequirementType();
            data[i][1] = req.getSelectedOption();
        }

        JPanel requirementsPanel = new JPanel(new BorderLayout());
        JTable table             = new JTable(data, columns);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        requirementsPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton addBtn    = new JButton("Add Requirement");
        JButton removeBtn = new JButton("Remove Requirement");

        // ── Add requirement action ────────────────────────────────────────────────
        addBtn.addActionListener(e -> {
            // Get available types from the loaded .ser file via controller
            java.util.Set<String> types = controller.getAvailableRequirementTypes();

            if (types.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "No requirement types available.",
                        "Unavailable", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] typeArray          = types.toArray(new String[0]);
            JComboBox<String> typeBox   = new JComboBox<>(typeArray);
            JComboBox<String> optionBox = new JComboBox<>();

            // Populate options based on selected type
            java.util.Set<String> initialOptions = controller.getOptionsForRequirementType(typeArray[0]);
            for (String opt : initialOptions) {
                optionBox.addItem(opt);
            }

            // Update options when type changes
            typeBox.addActionListener(c -> {
                optionBox.removeAllItems();
                String selectedType = (String) typeBox.getSelectedItem();
                java.util.Set<String> options = controller.getOptionsForRequirementType(selectedType);
                for (String opt : options) {
                    optionBox.addItem(opt);
                }
            });

            Object[] fields = {
                    "Requirement Type:", typeBox,
                    "Option:",           optionBox
            };

            int result = JOptionPane.showConfirmDialog(
                    null, fields, "Add Requirement", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String selectedType   = (String) typeBox.getSelectedItem();
                    String selectedOption = (String) optionBox.getSelectedItem();

                    VictimRequirement req = new VictimRequirement(
                            victim.getVictimID(), selectedType, selectedOption);
                    controller.addRequirement(victim.getVictimID(), req);

                    // Refresh tab immediately
                    tabs.setComponentAt(tabIndex,
                            buildRequirementsPanel(victim, tabs, tabIndex));
                    tabs.revalidate();
                    tabs.repaint();

                    JOptionPane.showMessageDialog(null,
                            "Requirement added successfully!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ── Remove requirement action ─────────────────────────────────────────────
        removeBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null,
                        "Please select a requirement first.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String requirementType = (String) table.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Remove requirement: " + requirementType + "?",
                    "Confirm", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.removeRequirement(victim.getVictimID(), requirementType);

                    // Refresh tab immediately
                    tabs.setComponentAt(tabIndex,
                            buildRequirementsPanel(victim, tabs, tabIndex));
                    tabs.revalidate();
                    tabs.repaint();

                    JOptionPane.showMessageDialog(null,
                            "Requirement removed successfully!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(addBtn);
        btnPanel.add(removeBtn);
        requirementsPanel.add(btnPanel, BorderLayout.SOUTH);

        return requirementsPanel;
    }
}
