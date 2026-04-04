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
            tabs.addTab("Medical Records", buildMedicalPanel(victim));
            tabs.addTab("Family", familyPanel);
            tabs.addTab("Skills", skillsPanel);

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

    private JPanel buildMedicalPanel(DisasterVictim victim){
        // ── Build table ───────────────────────────────────────────────────────────
        ArrayList<MedicalRecord> medicalRecords = victim.getMedicalRecords();

        String[] columns = {"Location", "Treatment Details", "Date of Treatment"};
        Object[][] data  = new Object[medicalRecords.size()][3];

        for (int i = 0; i < medicalRecords.size(); i++){
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

        return medicalRecordPanel;
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
