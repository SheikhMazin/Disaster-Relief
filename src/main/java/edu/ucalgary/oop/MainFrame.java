package edu.ucalgary.oop;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class MainFrame extends JFrame{
    private ReliefController controller;
    private JPanel mainPanel;
    private JPanel contentPanel;
    private VictimUI victimUI;

   public MainFrame(ReliefController controller){
       this.controller = controller;
       this.victimUI = new VictimUI(controller);
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

            tabs.addTab("Info",             victimUI.buildInfoPanel(victim));
            tabs.addTab("Medical Records",  victimUI.buildMedicalPanel(victim, tabs, 1));
            tabs.addTab("Family",           victimUI.buildFamilyPanel(victim, tabs, 2));
            tabs.addTab("Requirements",     victimUI.buildRequirementsPanel(victim, tabs, 3));
            tabs.addTab("Skills",           victimUI.buildSkillsPanel(victim, tabs, 4));

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


    public void showSupplyManagement() {
       contentPanel.removeAll();

        String warning = controller.getExpiredSupplyWarning();
        if (!warning.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    warning,
                    "Expired Supplies Warning",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        // ── Get data from controller ──────────────────────────────────────────────
        ArrayList<Supply> supplies = controller.getAllSupplies();

        // ── Build table ───────────────────────────────────────────────────────────
        String[] columns = {"ID", "Type", "Quantity", "Expiry Date", "Allocated To"};
        Object[][] data = new Object[supplies.size()][5];

        for (int i = 0; i < supplies.size(); i++){
            Supply s = supplies.get(i);
            data[i][0] = s.getSupplyID();
            data[i][1] = s.getType();
            data[i][2] = s.getQuantity();
            data[i][3] = s.getExpiryDate();
            data[i][4] = s.getAllocatedVictim() != null
                    ? s.getAllocatedVictim().getFirstName() + " " +
                      (s.getAllocatedVictim().getLastName() != null
                       ? s.getAllocatedVictim().getLastName() : "")
                    : "Unallocated";
        }

        JTable table = new JTable(data, columns) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                // check if expiry date column is in the past
                Object expiry = getValueAt(row, 3);
                if (expiry != null && !expiry.equals("N/A")) {
                    LocalDate expiryDate = LocalDate.parse(expiry.toString());
                    if (expiryDate.isBefore(LocalDate.now())) {
                        c.setBackground(new Color(255, 200, 200)); // light red
                        return c;
                    }
                }
                c.setBackground(Color.WHITE);
                return c;
            }
        };

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);

        // ── Buttons ───────────────────────────────────────────────────────────────
        JButton addBtn      = new JButton("Add Supply");
        JButton allocateBtn = new JButton("Allocate Supply");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(addBtn);
        btnPanel.add(allocateBtn);

        // ── Add Supply action ─────────────────────────────────────────────────────
        addBtn.addActionListener(e -> {
            JTextField typeField                = new JTextField();
            String[] perishableOptions = {"Yes", "No"};
            JComboBox<String> perishableBox = new JComboBox<>(perishableOptions);
            JTextField expiryDate               = new JTextField();
            expiryDate.setVisible(false); // hidden by default

            // Show/Hide expiry date field based on selection
            perishableBox.addActionListener(c -> {
                boolean isPerishable = perishableBox.getSelectedItem().equals("Yes");
                expiryDate.setVisible(isPerishable);
            });

            Object[] fields = {
                    "Type",        typeField,
                    "Perishable?", perishableBox,
                    "Expiry Date (if perishable):", expiryDate
            };

            int result = JOptionPane.showConfirmDialog(
                    this, fields, "Add Supply", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String type          = typeField.getText().trim();
                    boolean isPerishable = perishableBox.getSelectedItem().equals("Yes");

                    // ── Validate input ────────────────────────────────────────────────
                    if (type.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "Please enter a supply type.",
                                "Invalid Input", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (isPerishable && expiryDate.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "Please enter an expiry date for perishable supplies.",
                                "Invalid Input", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // ── Create supply ─────────────────────────────────────────────────
                    Supply supply;
                    if (isPerishable) {
                        LocalDate expiry = LocalDate.parse(expiryDate.getText().trim());
                        supply = new Supply(
                                (int)(Math.random() * 10000) + 1,
                                type, 1, true, expiry);
                    } else {
                        supply = new Supply(
                                (int)(Math.random() * 10000) + 1,
                                type, 1);
                    }

                    controller.addSupply(supply);
                    showSupplyManagement();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error adding supply: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ── Allocate Supply action ────────────────────────────────────────────────
        allocateBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select an item first.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ── Check supply before even showing dialog ───────────────────────────
            int supplyID    = (int) table.getValueAt(selectedRow, 0);
            Supply selected = null;
            for (Supply s : controller.getAllSupplies()) {
                if (s.getSupplyID() == supplyID) {
                    selected = s;
                    break;
                }
            }

            if (selected == null) {
                JOptionPane.showMessageDialog(this,
                        "Supply not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (selected.isExpired()) {
                JOptionPane.showMessageDialog(this,
                        "Cannot allocate an expired supply.",
                        "Invalid", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (selected.getAllocatedVictim() != null) {
                JOptionPane.showMessageDialog(this,
                        "This supply is already allocated to " +
                                selected.getAllocatedVictim().getFirstName() + ".",
                        "Already Allocated", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ── Show victim selection dialog ──────────────────────────────────────
            ArrayList<DisasterVictim> victims = controller.getActiveVictims();
            String[] victimNames = new String[victims.size()];
            for (int i = 0; i < victims.size(); i++) {
                victimNames[i] = victims.get(i).getVictimID() + " - " +
                        victims.get(i).getFirstName() + " " +
                        (victims.get(i).getLastName() != null
                                ? victims.get(i).getLastName() : "");
            }

            JComboBox<String> victimBox = new JComboBox<>(victimNames);
            Object[] fields = {"Victim:", victimBox};

            int result = JOptionPane.showConfirmDialog(
                    this, fields, "Allocate Supply", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    DisasterVictim selectedVictim = victims.get(victimBox.getSelectedIndex());
                    controller.allocateSupply(selected, selectedVictim);
                    showSupplyManagement();
                    JOptionPane.showMessageDialog(this,
                            "Supply allocated to " +
                                    selectedVictim.getFirstName() + " successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error allocating supply: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ── Assemble panel ────────────────────────────────────────────────────────
        JPanel panel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Supply Management");
        heading.setFont(new Font("Arial", Font.BOLD, 18));
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        panel.add(heading, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        contentPanel.add(panel);
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
