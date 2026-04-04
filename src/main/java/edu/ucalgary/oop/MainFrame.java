package edu.ucalgary.oop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class MainFrame extends JFrame{
    private ReliefController controller;
    private JPanel mainPanel;
    private JPanel contentPanel;

   public MainFrame(ReliefController controller){
       this.controller = controller;
       setTitle("Disaster Relief System");
       setSize(1024, 768);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       initComponents();
       setVisible(true);
   }

    private void initComponents() {
        setLayout(new BorderLayout());

        // ── NORTH: Title + Nav combined into one top panel ────────────────────────
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(47, 79, 105)); // dark blue

        // Title
        JLabel title = new JLabel("Disaster Relief System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
        topPanel.add(title);

        // Nav bar
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        navPanel.setBackground(new Color(47, 79, 105)); // same dark blue

        JButton victimsBtn   = new JButton("👤 Victims");
        JButton suppliesBtn  = new JButton("📦 Supplies");
        JButton inquiriesBtn = new JButton("🔍 Inquiries");
        JButton skillsBtn    = new JButton("⚡ Skills");
        JButton exitBtn      = new JButton("✖ Exit");

        // Style all buttons
        Dimension btnSize = new Dimension(140, 40);
        for (JButton btn : new JButton[]{victimsBtn, suppliesBtn,
                inquiriesBtn, skillsBtn, exitBtn}) {
            btn.setPreferredSize(btnSize);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setForeground(Color.WHITE);
            navPanel.add(btn);
        }

        // Set colors AFTER loop so each button keeps its own color
        victimsBtn.setBackground(new Color(70, 130, 180));
        suppliesBtn.setBackground(new Color(70, 130, 180));
        inquiriesBtn.setBackground(new Color(70, 130, 180));
        skillsBtn.setBackground(new Color(70, 130, 180));
        exitBtn.setBackground(new Color(180, 60, 60)); // red

        // Add hover effect AFTER colors are set
        for (JButton btn : new JButton[]{victimsBtn, suppliesBtn,
                inquiriesBtn, skillsBtn, exitBtn}) {
            Color original = btn.getBackground(); // captures each button's own color
            Color hover    = original.brighter();
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    btn.setBackground(hover);
                }
                public void mouseExited(MouseEvent evt) {
                    btn.setBackground(original);
                }
            });
        }

        // make exit button red
        exitBtn.setBackground(new Color(180, 60, 60));

        topPanel.add(navPanel);
        add(topPanel, BorderLayout.NORTH);

        // ── CENTER: Content area ──────────────────────────────────────────────────
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 240, 240)); // light gray
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(contentPanel, BorderLayout.CENTER);

        // ── Button actions ────────────────────────────────────────────────────────
        victimsBtn.addActionListener(e -> showVictimManagement());
        suppliesBtn.addActionListener(e -> showSupplyManagement());
        inquiriesBtn.addActionListener(e -> showInquiryManagement());
        skillsBtn.addActionListener(e -> showSkillManagement());
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }


    public void showVictimManagement() {
        contentPanel.removeAll();

        // ── Get data from controller ──────────────────────────────────────────────
        ArrayList<DisasterVictim> victims = controller.getActiveVictims();

        // ── Build table ───────────────────────────────────────────────────────────
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

        // ── Buttons ───────────────────────────────────────────────────────────────
        JButton addBtn         = new JButton("Add Victim");
        JButton softDeleteBtn  = new JButton("Soft Delete");
        JButton hardDeleteBtn  = new JButton("Hard Delete");
        JButton viewDetailsBtn    = new JButton("View Details");

        hardDeleteBtn.setBackground(new Color(180, 60, 60));
        hardDeleteBtn.setForeground(Color.WHITE);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(addBtn);
        btnPanel.add(softDeleteBtn);
        btnPanel.add(viewDetailsBtn);
        btnPanel.add(hardDeleteBtn);

        // ── Add victim action ─────────────────────────────────────────────────────
        addBtn.addActionListener(e -> {
            JTextField firstNameField       = new JTextField();
            JTextField lastNameField        = new JTextField();
            JTextField ageField             = new JTextField();
            JTextField dobField             = new JTextField("YYYY-MM-DD");
            JTextField customGenderField    = new JTextField();
            customGenderField.setVisible(false); // hidden by default

            String[] genderOptions          = {"Man", "Woman", "Boy", "Girl", "Non-binary person", "Please Specify"};
            JComboBox<String> genderBox     = new JComboBox<>(genderOptions);


            // Show/Hide custom gender field based on selection
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
                    this, fields, "Add Victim", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String firstName = firstNameField.getText().trim();
                    String lastName  = lastNameField.getText().trim();
                    String gender;
                    String dob       = dobField.getText().trim();
                    String age       = ageField.getText().trim();


                    DisasterVictim victim;

                    if (!dob.isEmpty() && !dob.equals("YYYY-MM-DD")) {
                        victim = new DisasterVictim(
                                (int)(Math.random() * 10000) + 1,
                                firstName,
                                LocalDate.now(),
                                LocalDate.parse(dob));
                    } else if (!age.isEmpty()) {
                        victim = new DisasterVictim(
                                (int)(Math.random() * 10000) + 1,
                                firstName,
                                LocalDate.now(),
                                Integer.parseInt(age));
                    } else {
                        victim = new DisasterVictim(
                                (int)(Math.random() * 10000) + 1,
                                firstName,
                                LocalDate.now());
                    }

                    if (!lastName.isEmpty()) victim.setLastName(lastName);

                    if (genderBox.getSelectedItem().equals("Please Specify")) {
                        if (!customGenderField.getText().trim().isEmpty()) {
                            victim.setGender("Please Specify");
                            victim.setGender(customGenderField.getText().trim());
                        } else {
                            // nothing typed - just set Please Specify
                            victim.setGender("Please Specify");
                        }
                    }
                    controller.addVictim(victim);
                    showVictimManagement(); // refresh

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error adding victim: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ── Soft delete action ────────────────────────────────────────────────────
        softDeleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select a victim first.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int victimID = (int) table.getValueAt(selectedRow, 0);
            int confirm  = JOptionPane.showConfirmDialog(this,
                    "Soft delete victim ID " + victimID + "? They will be hidden but data is kept.",
                    "Confirm Soft Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.softDeleteVictim(victimID);
                    showVictimManagement(); // refresh
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // ── View Details action ─────────────────────────────────────────────────────
        viewDetailsBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select a victim first.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int victimID = (int) table.getValueAt(selectedRow, 0);
            DisasterVictim victim = controller.getVictimByID(victimID);

            JTabbedPane tabs = new JTabbedPane();

            JPanel medicalPanel = new JPanel();
            JPanel familyPanel = new JPanel();
            JPanel skillsPanel = new JPanel();

            tabs.addTab("Info", buildInfoPanel(victim));
            tabs.addTab("Medical Records", buildMedicalPanel(victim, tabs, 1));
            tabs.addTab("Family", buildFamilyPanel(victim, tabs, 2));
            tabs.addTab("Skills", buildSkillsPanel(victim, tabs, 3));

            JDialog dialog = new JDialog(this, "Victim Details - " + victim.getFirstName(), true);
            dialog.setSize(700, 500);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());
            dialog.add(tabs, BorderLayout.CENTER);
            dialog.setVisible(true);
        });


        // ── Hard delete action ────────────────────────────────────────────────────
        hardDeleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select a victim first.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int victimID = (int) table.getValueAt(selectedRow, 0);
            int confirm  = JOptionPane.showConfirmDialog(this,
                    "PERMANENTLY delete victim ID " + victimID + "?\nThis cannot be undone.",
                    "Confirm Hard Delete", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.hardDeleteVictim(victimID);
                    showVictimManagement(); // refresh
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        // ── Assemble panel ────────────────────────────────────────────────────────
        JPanel panel = new JPanel(new BorderLayout());
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


    /**
     * Builds the Info tab panel for the victim details dialog.
     * Displays all basic information about the given victim in a
     * two-column label/value grid layout including ID, name, gender,
     * age, entry date, comments, and active/soft-deleted status.
     *
     * @param victim the DisasterVictim whose information to display
     * @return a JPanel containing the victim's full info in a grid layout
     */
    private JPanel buildInfoPanel(DisasterVictim victim) {
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
    private JPanel buildMedicalPanel(DisasterVictim victim, JTabbedPane tabs, int tabIndex) {
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
    private JPanel buildFamilyPanel(DisasterVictim victim, JTabbedPane tabs, int tabIndex) {
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
    private JPanel buildSkillsPanel(DisasterVictim victim, JTabbedPane tabs, int tabIndex) {
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

    public void showSupplyManagement() {
       contentPanel.removeAll();
       contentPanel.add(new JLabel("Supplies"), BorderLayout.NORTH);

       contentPanel.revalidate();
       contentPanel.repaint();
    }

    public void showInquiryManagement() {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Inquirers"), BorderLayout.NORTH);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void showSkillManagement() {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Skills"), BorderLayout.NORTH);

        contentPanel.revalidate();
        contentPanel.repaint();
    }



}
