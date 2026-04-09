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
    private VictimManagementUI victimManagementUI;
    private VictimUI victimUI;
    private SupplyUI supplyUI;
    private InquiryUI inquiryUI;
    private SkillUI skillUI;

   public MainFrame(ReliefController controller){
       this.controller = controller;
       this.victimUI = new VictimUI(controller);
       this.victimManagementUI = new VictimManagementUI(controller, victimUI);
       this.inquiryUI = new InquiryUI(controller);
       this.supplyUI = new SupplyUI(controller);
       this.skillUI = new SkillUI(controller);

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


    public void showVictimManagement() { victimManagementUI.show(contentPanel); }

    public void showSupplyManagement() {
        supplyUI.show(contentPanel);
    }

    public void showInquiryManagement() {
        inquiryUI.show(contentPanel);
    }

    public void showSkillManagement() { skillUI.show(contentPanel); }



}
