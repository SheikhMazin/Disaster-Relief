package edu.ucalgary.oop;

/**
 * InquiryUI
 *
 * Helper class responsible for building and displaying the Inquiry Management
 * screen within the application's content panel. Handles showing the inquiry
 * table, adding new inquiries, and updating existing inquiries. All data
 * operations are delegated to the ReliefController — no business logic
 * exists in this class.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class InquiryUI {

    private final ReliefController controller;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs an InquiryUI backed by the given controller.
     *
     * @param controller non-null ReliefController for all data operations
     * @throws IllegalArgumentException if controller is null
     */
    public InquiryUI(ReliefController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("controller cannot be null.");
        }
        this.controller = controller;
    }

    // =========================================================================
    //  Main screen
    // =========================================================================

    /**
     * Clears the given content panel and renders the Inquiry Management screen.
     * Displays a table of all inquiries loaded from the database and provides
     * buttons to add a new inquiry or update an existing one.
     *
     * @param contentPanel the JPanel to render this screen into
     */
    public void show(JPanel contentPanel) {
        contentPanel.removeAll();

        // ── Get data from controller ──────────────────────────────────────────
        ArrayList<ReliefService> inquiries = controller.getInquiries();

        // ── Build table ──────────────────────────────────────────
        String[] columns = {"ID", "Inquirer", "Missing Persson", "Date", "Details"};
        Object[][] data = new Object[inquiries.size()][5];

        for (int i = 0; i < inquiries.size(); i++) {
            ReliefService inquiry = inquiries.get(i);
            data[i][0] = inquiry.getInquiryID();
            data[i][1] = inquiry.getInquirer().getFirstName() + " " +
                    inquiry.getInquirer().getLastName();
            data[i][2] = inquiry.getMissingPerson().getFirstName() + " " +
                    (inquiry.getMissingPerson().getLastName() != null
                            ? inquiry.getMissingPerson().getLastName() : "");
            data[i][3] = inquiry.getDateOfInquiry();
            data[i][4] = inquiry.getInfoProvided();
        }

        JTable table = new JTable(data, columns);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);


        // ── Buttons ───────────────────────────────────────────────────────────
        JButton addBtn = new JButton("Add Inquiry");
        JButton updateBtn = new JButton("Update Inquiry");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);


        // ── Add Inquiry action ───────────────────────────────────────────────────────────
        addBtn.addActionListener(e -> {
            JTextField firstNameField = new JTextField();
            JTextField lastNameField  = new JTextField();
            JTextField phoneField     = new JTextField();
            JTextField infoField      = new JTextField();
            JTextField detailsField   = new JTextField();
            JTextField dateField      = new JTextField(LocalDate.now().toString());

            ArrayList<DisasterVictim> victims = controller.getActiveVictims();
            String[] victimNames = new String[victims.size()];
            for (int i = 0; i < victims.size(); i++) {
                victimNames[i] = victims.get(i).getVictimID() + " - " +
                        victims.get(i).getFirstName() + " " +
                        (victims.get(i).getLastName() != null
                                ? victims.get(i).getLastName() : "");
            }

            JComboBox<String> victimBox = new JComboBox<>(victimNames);

            Object[] fields = {
                    "First Name:",    firstNameField,
                    "Last Name:",     lastNameField,
                    "Phone Number:",  phoneField,
                    "Info:",          infoField,
                    "Active Victims:", victimBox,
                    "Details:",       detailsField,
                    "Date:",          dateField
            };

            int result = JOptionPane.showConfirmDialog(
                    contentPanel, fields, "Add Inquiry", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String firstName   = firstNameField.getText().trim();
                    String lastName    = lastNameField.getText().trim();
                    String phoneNumber = phoneField.getText().trim();
                    String info        = infoField.getText().trim();
                    String details     = detailsField.getText().trim();
                    LocalDate date     = LocalDate.parse(dateField.getText().trim());

                    // ── Validate ──────────────────────────────────────────────────────
                    if (firstName.isEmpty()) {
                        JOptionPane.showMessageDialog(contentPanel,
                                "Please enter the inquirer's first name.",
                                "Invalid Input", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (details.isEmpty()) {
                        JOptionPane.showMessageDialog(contentPanel,
                                "Please enter inquiry details.",
                                "Invalid Input", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // ── Create temp inquirer to save to DB ────────────────────────────
                    Inquirer tempInquirer = new Inquirer(1, firstName,
                            lastName.isEmpty() ? "Unknown" : lastName,
                            phoneNumber.isEmpty() ? "N/A" : phoneNumber,
                            info.isEmpty() ? "N/A" : info);

                    // ── Save inquirer to DB and get generated ID ──────────────────────
                    int inquirerID = controller.saveInquirer(tempInquirer);
                    Inquirer inquirer = new Inquirer(inquirerID, firstName,
                            lastName.isEmpty() ? "Unknown" : lastName,
                            phoneNumber.isEmpty() ? "N/A" : phoneNumber,
                            info.isEmpty() ? "N/A" : info);

                    // ── Get selected missing person ────────────────────────────────────
                    DisasterVictim missingPerson = victims.get(victimBox.getSelectedIndex());

                    // ── Create and save inquiry ───────────────────────────────────────
                    int inquiryID = controller.getNextInquiryID();
                    ReliefService inquiry = new ReliefService(
                            inquiryID, inquirer, missingPerson, date, details, null);
                    controller.addInquiry(inquiry);

                    show(contentPanel);
                    JOptionPane.showMessageDialog(contentPanel, "Inquiry added successfully!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(contentPanel,
                            "Error adding inquiry: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ── Update Inquiry action ───────────────────────────────────────────────────────────
        updateBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(contentPanel,
                        "Please select an inquiry first.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Get selected inquiry
            int inquiryID = (int) table.getValueAt(selectedRow, 0);
            ReliefService selected = null;
            for (ReliefService inq : controller.getInquiries()) {
                if (inq.getInquiryID() == inquiryID) {
                    selected = inq;
                    break;
                }
            }

            if (selected == null) {
                JOptionPane.showMessageDialog(contentPanel,
                        "Inquiry not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Pre-populate with existing details
            JTextField detailsField = new JTextField(selected.getInfoProvided());

            Object[] fields = {
                    "Details:", detailsField
            };

            int result = JOptionPane.showConfirmDialog(
                    contentPanel, fields, "Update Inquiry", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String newDetails = detailsField.getText().trim();
                    if (newDetails.isEmpty()) {
                        JOptionPane.showMessageDialog(contentPanel,
                                "Details cannot be empty.",
                                "Invalid Input", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    selected.setInfoProvided(newDetails);
                    controller.updateInquiry(selected);
                    show(contentPanel);
                    JOptionPane.showMessageDialog(contentPanel,
                            "Inquiry updated successfully!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(contentPanel,
                            "Error updating inquiry: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // ── Assemble and display panel ────────────────────────────────────────
        JPanel panel = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Inquiry Management");
        heading.setFont(new Font("Arial", Font.BOLD, 10));
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        panel.add(heading,          BorderLayout.NORTH);
        panel.add(scrollPane,       BorderLayout.CENTER);
        panel.add(btnPanel,         BorderLayout.SOUTH);


        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}