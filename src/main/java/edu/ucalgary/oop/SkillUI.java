package edu.ucalgary.oop;

/**
 * SkillUI
 *
 * Helper class responsible for building and displaying the Skill Search
 * screen within the application's content panel. Allows relief workers to
 * search for disaster victims by skill category (Medical, Language, or Trade)
 * and view matching results. Soft-deleted victims are excluded from all
 * search results. All data operations are delegated to the ReliefController
 * — no business logic exists in this class.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SkillUI {

    private final ReliefController controller;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs a SkillUI backed by the given controller.
     *
     * @param controller non-null ReliefController for all data operations
     * @throws IllegalArgumentException if controller is null
     */
    public SkillUI(ReliefController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("controller cannot be null.");
        }
        this.controller = controller;
    }

    // =========================================================================
    //  Main screen
    // =========================================================================

    /**
     * Clears the given content panel and renders the Skill Search screen.
     * Displays a category dropdown and a search button. Results are shown
     * in a table listing the victim name, skill category, proficiency level,
     * and skill-specific details. Soft-deleted victims are excluded.
     *
     * @param contentPanel the JPanel to render this screen into
     */
    public void show(JPanel contentPanel) {
        contentPanel.removeAll();

        // ── Heading ───────────────────────────────────────────────────────────
        JLabel heading = new JLabel("Skill Search");
        heading.setFont(new Font("Arial", Font.BOLD, 18));
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // ── Search controls ───────────────────────────────────────────────────
        String[] categories          = {"Medical", "Language", "Trade"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        JButton searchBtn            = new JButton("Search");

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Category:"));
        searchPanel.add(categoryBox);
        searchPanel.add(searchBtn);

        // ── Results table (starts empty) ──────────────────────────────────────
        String[] columns      = {"Victim ID", "Victim Name", "Category",
                "Proficiency", "Details"};
        Object[][] emptyData  = new Object[0][5];
        JTable table          = new JTable(emptyData, columns);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);

        // ── Top panel: heading + search controls ──────────────────────────────
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(heading,     BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        // ── Search button action ──────────────────────────────────────────────
        searchBtn.addActionListener(e -> {
            String category = (String) categoryBox.getSelectedItem();
            ArrayList<Skill> results = controller.searchSkillsByCategory(category);

            // Build table data — one row per skill result
            Object[][] data = new Object[results.size()][5];
            for (int i = 0; i < results.size(); i++) {
                Skill skill             = results.get(i);
                DisasterVictim victim   = controller.getVictimByID(skill.getVictimID());

                data[i][0] = skill.getVictimID();
                data[i][1] = victim != null
                        ? victim.getFirstName() + " " +
                          (victim.getLastName() != null ? victim.getLastName() : "")
                        : "Unknown";
                data[i][2] = skill.getCategory();
                data[i][3] = skill.getProficiencyLevel();

                // Build details string based on skill subtype
                if (skill instanceof MedicalSkill ms) {
                    data[i][4] = ms.getCertificationType() +
                            " (exp: " + ms.getCertificationExpiryDate() + ")";
                } else if (skill instanceof LanguageSkill ls) {
                    data[i][4] = ls.getLanguageName() + " — " +
                            (ls.hasReadWrite()   ? "read/write "  : "") +
                            (ls.hasSpeakListen() ? "speak/listen" : "");
                } else if (skill instanceof TradeSkill ts) {
                    data[i][4] = ts.getTradeType();
                }
            }

            // Update table with new data
            javax.swing.table.DefaultTableModel model =
                    new javax.swing.table.DefaultTableModel(data, columns) {
                        @Override
                        public boolean isCellEditable(int row, int col) {
                            return false;
                        }
                    };
            table.setModel(model);

            // Show result count
            JOptionPane.showMessageDialog(contentPanel,
                    "Found " + results.size() + " result(s) for category: " + category,
                    "Search Results", JOptionPane.INFORMATION_MESSAGE);
        });

        // ── Assemble and display panel ────────────────────────────────────────
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(topPanel,    BorderLayout.NORTH);
        panel.add(scrollPane,  BorderLayout.CENTER);

        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}