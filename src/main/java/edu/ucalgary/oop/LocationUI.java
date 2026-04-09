package edu.ucalgary.oop;

/**
 * LocationUI
 *
 * Helper class responsible for building and displaying the Location screen
 * within the application's content panel. Shows all locations in a table
 * and allows relief workers to search for victims by selecting a location.
 * All data operations are delegated to the ReliefController.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LocationUI {

    private final ReliefController controller;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs a LocationUI backed by the given controller.
     *
     * @param controller non-null ReliefController for all data operations
     * @throws IllegalArgumentException if controller is null
     */
    public LocationUI(ReliefController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("controller cannot be null.");
        }
        this.controller = controller;
    }

    // =========================================================================
    //  Main screen
    // =========================================================================

    /**
     * Clears the given content panel and renders the Location screen.
     * Displays a table of all locations and a dropdown to search for
     * victims registered at a selected location.
     *
     * @param contentPanel the JPanel to render this screen into
     */
    public void show(JPanel contentPanel) {
        contentPanel.removeAll();

        // ── Heading ───────────────────────────────────────────────────────────
        JLabel heading = new JLabel("Location Management");
        heading.setFont(new Font("Arial", Font.BOLD, 18));
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // ── Load locations ────────────────────────────────────────────────────
        ArrayList<Location> locations = controller.getLocations();

        // ── Locations table ───────────────────────────────────────────────────
        String[] locationColumns = {"Location ID", "Name", "Address"};
        Object[][] locationData  = new Object[locations.size()][3];
        for (int i = 0; i < locations.size(); i++) {
            Location loc      = locations.get(i);
            locationData[i][0] = loc.getLocationID();
            locationData[i][1] = loc.getName();
            locationData[i][2] = loc.getAddress();
        }

        JTable locationTable = new JTable(locationData, locationColumns);
        locationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        locationTable.setRowHeight(25);
        locationTable.setEnabled(false);
        JScrollPane locationScroll = new JScrollPane(locationTable);
        locationScroll.setPreferredSize(new Dimension(0, 200));

        JLabel locationTableLabel = new JLabel("All Locations:");
        locationTableLabel.setFont(new Font("Arial", Font.BOLD, 14));
        locationTableLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // ── Search by location ────────────────────────────────────────────────
        JLabel searchLabel = new JLabel("Search Victims by Location:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));

        // Build dropdown from location names
        String[] locationNames = new String[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            locationNames[i] = locations.get(i).getName();
        }

        JComboBox<String> locationBox = new JComboBox<>(locationNames);
        JButton searchBtn             = new JButton("Search");

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Location:"));
        searchPanel.add(locationBox);
        searchPanel.add(searchBtn);

        // ── Results table (starts empty) ──────────────────────────────────────
        String[] victimColumns = {"Victim ID", "First Name", "Last Name", "Gender", "Age"};
        Object[][] emptyData   = new Object[0][5];
        JTable victimTable     = new JTable(emptyData, victimColumns);
        victimTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        victimTable.setRowHeight(25);
        JScrollPane victimScroll = new JScrollPane(victimTable);

        JLabel resultsLabel = new JLabel("Victims at selected location:");
        resultsLabel.setFont(new Font("Arial", Font.BOLD, 13));
        resultsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // ── Search button action ──────────────────────────────────────────────
        searchBtn.addActionListener(e -> {
            if (locations.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "No locations available.",
                        "Unavailable", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Location selected = locations.get(locationBox.getSelectedIndex());

            // Search active victims whose location_id matches
            ArrayList<DisasterVictim> activeVictims = controller.getActiveVictims();
            ArrayList<DisasterVictim> results = new ArrayList<>();

            for (DisasterVictim v : activeVictims) {
                if (v.getLocationID() == selected.getLocationID()) {
                    results.add(v);
                }
            }

            // Build table data
            Object[][] data = new Object[results.size()][5];
            for (int i = 0; i < results.size(); i++) {
                DisasterVictim v = results.get(i);
                data[i][0] = v.getVictimID();
                data[i][1] = v.getFirstName();
                data[i][2] = v.getLastName() != null ? v.getLastName() : "";
                data[i][3] = v.getGender() != null ? v.getGender() : "";
                data[i][4] = v.getDateOfBirth() != null
                        ? v.getDateOfBirth().toString()
                        : (v.getApproximateAge() != null
                           ? "~" + v.getApproximateAge() + " yrs" : "Unknown");
            }

            JTable newTable = new JTable(data, victimColumns);
            newTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            newTable.setRowHeight(25);
            victimScroll.setViewportView(newTable);
            victimScroll.revalidate();
            victimScroll.repaint();

            resultsLabel.setText("Victims at: " + selected.getName()
                    + " (" + results.size() + " found)");
        });

        // ── Assemble panel ────────────────────────────────────────────────────
        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(heading);
        topPanel.add(locationTableLabel);
        topPanel.add(locationScroll);
        topPanel.add(searchLabel);
        topPanel.add(searchPanel);
        topPanel.add(resultsLabel);

        panel.add(topPanel,      BorderLayout.NORTH);
        panel.add(victimScroll,  BorderLayout.CENTER);

        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}