package edu.ucalgary.oop;

/**
 * SupplyUI
 *
 * Helper class responsible for building and displaying the Supply Management
 * screen within the application's content panel. Handles showing the supply
 * table, expired supply warnings, adding new supplies, and allocating supplies
 * to disaster victims. All data operations are delegated to the
 * ReliefController — no business logic exists in this class.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class SupplyUI {

    private final ReliefController controller;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs a SupplyUI backed by the given controller.
     *
     * @param controller non-null ReliefController for all data operations
     * @throws IllegalArgumentException if controller is null
     */
    public SupplyUI(ReliefController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("controller cannot be null.");
        }
        this.controller = controller;
    }

    // =========================================================================
    //  Main screen
    // =========================================================================

    /**
     * Clears the given content panel and renders the Supply Management screen.
     * Displays a table of all supplies with expired rows highlighted in red,
     * shows a warning popup if any expired supplies exist, and provides
     * buttons to add a new supply or allocate an existing supply to a victim.
     *
     * @param contentPanel the JPanel to render this screen into
     */
    public void show(JPanel contentPanel) {
        contentPanel.removeAll();

        // ── Show expired supply warning if any exist ──────────────────────────
        String warning = controller.getExpiredSupplyWarning();
        if (!warning.isEmpty()) {
            JOptionPane.showMessageDialog(
                    contentPanel,
                    warning,
                    "Expired Supplies Warning",
                    JOptionPane.WARNING_MESSAGE
            );
        }

        // ── Get data from controller ──────────────────────────────────────────
        ArrayList<Supply> supplies = controller.getAllSupplies();

        // ── Build table ───────────────────────────────────────────────────────
        String[] columns = {"ID", "Type", "Quantity", "Expiry Date", "Allocated To"};
        Object[][] data  = new Object[supplies.size()][5];

        for (int i = 0; i < supplies.size(); i++) {
            Supply s = supplies.get(i);
            data[i][0] = s.getSupplyID();
            data[i][1] = s.getType();
            data[i][2] = s.getQuantity();
            data[i][3] = s.getExpiryDate() != null ? s.getExpiryDate().toString() : "N/A";
            data[i][4] = s.getAllocatedVictim() != null
                    ? s.getAllocatedVictim().getFirstName() + " " +
                      (s.getAllocatedVictim().getLastName() != null
                       ? s.getAllocatedVictim().getLastName() : "")
                    : "Unallocated";
        }

        // Highlight expired rows in light red
        JTable table = new JTable(data, columns) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                Object expiry = getValueAt(row, 3);
                if (expiry != null && !expiry.equals("N/A")) {
                    LocalDate expiryDate = LocalDate.parse(expiry.toString());
                    if (expiryDate.isBefore(LocalDate.now())) {
                        c.setBackground(new Color(255, 200, 200));
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

        // ── Buttons ───────────────────────────────────────────────────────────
        JButton addBtn      = new JButton("Add Supply");
        JButton allocateBtn = new JButton("Allocate Supply");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(addBtn);
        btnPanel.add(allocateBtn);

        // ── Add supply action ─────────────────────────────────────────────────
        addBtn.addActionListener(e -> {
            JTextField typeField            = new JTextField();
            String[] perishableOptions      = {"Yes", "No"};
            JComboBox<String> perishableBox = new JComboBox<>(perishableOptions);
            JTextField expiryDate           = new JTextField();
            expiryDate.setVisible(false);

            // Show/hide expiry date field based on perishable selection
            perishableBox.addActionListener(c -> {
                boolean isPerishable = perishableBox.getSelectedItem().equals("Yes");
                expiryDate.setVisible(isPerishable);
            });

            Object[] fields = {
                    "Type:",       typeField,
                    "Perishable?", perishableBox,
                    "Expiry Date (if perishable):", expiryDate
            };

            int result = JOptionPane.showConfirmDialog(
                    contentPanel, fields, "Add Supply", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String type          = typeField.getText().trim();
                    boolean isPerishable = perishableBox.getSelectedItem().equals("Yes");

                    // Validate input
                    if (type.isEmpty()) {
                        JOptionPane.showMessageDialog(contentPanel,
                                "Please enter a supply type.",
                                "Invalid Input", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (isPerishable && expiryDate.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(contentPanel,
                                "Please enter an expiry date for perishable supplies.",
                                "Invalid Input", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Create and save supply
                    Supply supply;
                    int id = controller.getNextSupplyID();

                    if (isPerishable) {
                        LocalDate expiry = LocalDate.parse(expiryDate.getText().trim());
                        supply = new Supply(id, type, 1, true, expiry);
                    } else {
                        supply = new Supply(id, type, 1);
                    }

                    controller.addSupply(supply);
                    show(contentPanel);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(contentPanel,
                            "Error adding supply: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ── Allocate supply action ────────────────────────────────────────────
        allocateBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(contentPanel,
                        "Please select an item first.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Find selected supply and validate before showing dialog
            int supplyID    = (int) table.getValueAt(selectedRow, 0);
            Supply selected = null;
            for (Supply s : controller.getAllSupplies()) {
                if (s.getSupplyID() == supplyID) {
                    selected = s;
                    break;
                }
            }

            if (selected == null) {
                JOptionPane.showMessageDialog(contentPanel,
                        "Supply not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (selected.isExpired()) {
                JOptionPane.showMessageDialog(contentPanel,
                        "Cannot allocate an expired supply.",
                        "Invalid", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (selected.getAllocatedVictim() != null) {
                JOptionPane.showMessageDialog(contentPanel,
                        "This supply is already allocated to " +
                                selected.getAllocatedVictim().getFirstName() + ".",
                        "Already Allocated", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Show victim selection dialog
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
                    contentPanel, fields, "Allocate Supply", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    Supply finalSelected              = selected;
                    DisasterVictim selectedVictim     = victims.get(victimBox.getSelectedIndex());
                    controller.allocateSupply(finalSelected, selectedVictim);
                    show(contentPanel);
                    JOptionPane.showMessageDialog(contentPanel,
                            "Supply allocated to " +
                                    selectedVictim.getFirstName() + " successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(contentPanel,
                            "Error allocating supply: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ── Assemble and display panel ────────────────────────────────────────
        JPanel panel   = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Supply Management");
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