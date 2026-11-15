package ui;

import model.Admin;
import model.Feedback;
import model.FeedbackTarget;
import service.FeedbackService;
import service.ReportService;
import service.Reportable;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

/**
 * Administrator Portal Dashboard providing full GUI access to:
 * 1. View all student feedback submissions in a searchable table with category badges.
 * 2. Filter feedback by target category (Event, Course, Facility).
 * 3. Analytical Executive Summary report with KPI metrics and breakdown table.
 * 4. Polymorphic role inspection for OOP viva defense.
 * 
 * OOP Concepts Demonstrated:
 * 1. Interface Polymorphism: Invokes report generation via Reportable interface reference.
 * 2. Dynamic Dispatch: Polymorphically invokes admin.getRoleDescription().
 * 3. Custom Painting & Vector Icons: Eliminates emoji font dependency using IconFactory.
 */
public class AdminDashboard extends JPanel {
    private final Admin admin;
    private final FeedbackService feedbackService;
    private final ReportService reportService;
    private final Runnable onLogout;

    // Card navigation
    private final CardLayout contentCardLayout = new CardLayout();
    private final JPanel contentCards = new JPanel(contentCardLayout);

    // Sidebar navigation buttons
    private JButton navAllBtn;
    private JButton navFilterBtn;
    private JButton navReportBtn;
    private JButton navRoleBtn;

    // All Submissions View Components
    private DefaultTableModel allTableModel;
    private JTable allTable;
    private TableRowSorter<DefaultTableModel> tableSorter;
    private CustomComponents.ModernTextField searchField;
    private JLabel totalCountBadge;

    // Filter View Components
    private DefaultTableModel filterTableModel;
    private JTable filterTable;
    private JComboBox<String> filterCombo;
    private JLabel filterCountBadge;

    // Report View Components
    private CustomComponents.KpiCard totalKpi;
    private CustomComponents.KpiCard avgRatingKpi;
    private CustomComponents.KpiCard topTargetKpi;
    private CustomComponents.KpiCard lowTargetKpi;
    private JTextArea reportTextArea;

    public AdminDashboard(Admin admin, FeedbackService feedbackService, ReportService reportService, Runnable onLogout) {
        this.admin = admin;
        this.feedbackService = feedbackService;
        this.reportService = reportService;
        this.onLogout = onLogout;

        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        // 1. Left Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // 2. Right Center Cards
        setupCards();
        add(contentCards, BorderLayout.CENTER);

        // Default to All Submissions view
        showCard("ALL", navAllBtn);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(Theme.SIDEBAR_BG);
        sidebar.setBorder(new EmptyBorder(24, 16, 24, 16));

        // Top Brand & Profile
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);

        // Vector Logo Icon
        JLabel brand = new JLabel(" CampusFeedback", IconFactory.createLogoIcon(22, Theme.PRIMARY), SwingConstants.LEFT);
        brand.setFont(Theme.FONT_TITLE);
        brand.setForeground(Theme.TEXT_WHITE);

        JLabel brandSub = new JLabel("   Administrator Dashboard");
        brandSub.setFont(Theme.FONT_SMALL);
        brandSub.setForeground(Theme.TEXT_MUTED);

        // Profile Card
        JPanel profileCard = new JPanel(new BorderLayout(10, 0));
        profileCard.setBackground(Theme.SIDEBAR_HOVER);
        profileCard.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel avatar = new JLabel("A");
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 17));
        avatar.setForeground(Color.WHITE);
        avatar.setOpaque(true);
        avatar.setBackground(new Color(220, 38, 38)); // Admin Red
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(36, 36));

        JPanel namePanel = new JPanel(new GridLayout(2, 1, 0, 2));
        namePanel.setOpaque(false);
        JLabel nameLbl = new JLabel(admin.getFullName());
        nameLbl.setFont(Theme.FONT_BODY_BOLD);
        nameLbl.setForeground(Theme.TEXT_WHITE);

        JLabel deptLbl = new JLabel(admin.getDepartment());
        deptLbl.setFont(Theme.FONT_SMALL);
        deptLbl.setForeground(Theme.TEXT_MUTED);

        namePanel.add(nameLbl);
        namePanel.add(deptLbl);

        profileCard.add(avatar, BorderLayout.WEST);
        profileCard.add(namePanel, BorderLayout.CENTER);

        top.add(brand);
        top.add(Box.createVerticalStrut(2));
        top.add(brandSub);
        top.add(Box.createVerticalStrut(20));
        top.add(profileCard);
        top.add(Box.createVerticalStrut(24));

        // Navigation with Vector Icons
        JPanel navPanel = new JPanel(new GridLayout(4, 1, 0, 8));
        navPanel.setOpaque(false);

        navAllBtn = createNavButton(" All Submissions", IconFactory.createClipboardIcon(16, Theme.TEXT_MUTED));
        navFilterBtn = createNavButton(" Filter by Category", IconFactory.createFilterIcon(16, Theme.TEXT_MUTED));
        navReportBtn = createNavButton(" Executive Report", IconFactory.createChartIcon(16, Theme.TEXT_MUTED));
        navRoleBtn = createNavButton(" Role & OOP Info", IconFactory.createInfoIcon(16, Theme.TEXT_MUTED));

        navAllBtn.addActionListener(e -> {
            loadAllSubmissions();
            showCard("ALL", navAllBtn);
        });
        navFilterBtn.addActionListener(e -> {
            applyCategoryFilter();
            showCard("FILTER", navFilterBtn);
        });
        navReportBtn.addActionListener(e -> {
            loadExecutiveReport();
            showCard("REPORT", navReportBtn);
        });
        navRoleBtn.addActionListener(e -> showCard("ROLE", navRoleBtn));

        navPanel.add(navAllBtn);
        navPanel.add(navFilterBtn);
        navPanel.add(navReportBtn);
        navPanel.add(navRoleBtn);

        top.add(navPanel);
        sidebar.add(top, BorderLayout.NORTH);

        // Bottom Logout with Vector Door Icon
        CustomComponents.ModernButton logoutBtn = new CustomComponents.ModernButton(
                "Sign Out", IconFactory.createLogoutIcon(16, Theme.DANGER), Theme.SIDEBAR_HOVER, Theme.DANGER
        );
        logoutBtn.setFont(Theme.FONT_SMALL_BOLD);
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to sign out?",
                    "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                onLogout.run();
            }
        });
        sidebar.add(logoutBtn, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createNavButton(String text, Icon icon) {
        JButton btn = new JButton(text, icon);
        btn.setFont(Theme.FONT_BODY);
        btn.setForeground(Theme.TEXT_LIGHT);
        btn.setBackground(Theme.SIDEBAR_BG);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(10);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 14, 10, 14));
        return btn;
    }

    private void showCard(String cardName, JButton activeBtn) {
        contentCardLayout.show(contentCards, cardName);

        JButton[] buttons = {navAllBtn, navFilterBtn, navReportBtn, navRoleBtn};
        for (JButton b : buttons) {
            b.setBackground(Theme.SIDEBAR_BG);
            b.setFont(Theme.FONT_BODY);
            b.setForeground(Theme.TEXT_LIGHT);
        }
        activeBtn.setBackground(Theme.SIDEBAR_ACTIVE);
        activeBtn.setFont(Theme.FONT_BODY_BOLD);
        activeBtn.setForeground(Color.WHITE);
    }

    private void setupCards() {
        contentCards.setOpaque(false);
        contentCards.add(createAllSubmissionsPanel(), "ALL");
        contentCards.add(createFilterPanel(), "FILTER");
        contentCards.add(createReportPanel(), "REPORT");
        contentCards.add(createRolePanel(), "ROLE");
    }

    // =========================================================================
    // 1. ALL SUBMISSIONS PANEL
    // =========================================================================
    private JPanel createAllSubmissionsPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(24, 30, 24, 30));

        // Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JPanel titleBlock = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleBlock.setOpaque(false);
        JLabel title = new JLabel("All Feedback Submissions");
        title.setFont(Theme.FONT_HERO);
        title.setForeground(Theme.TEXT_DARK);

        totalCountBadge = new JLabel("0 Total Records");
        totalCountBadge.setFont(Theme.FONT_SMALL_BOLD);
        totalCountBadge.setForeground(Theme.PRIMARY);
        totalCountBadge.setBackground(Theme.PRIMARY_LIGHT);
        totalCountBadge.setOpaque(true);
        totalCountBadge.setBorder(new EmptyBorder(4, 10, 4, 10));

        titleBlock.add(title);
        titleBlock.add(totalCountBadge);

        // Search & Refresh Controls with Placeholder
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        controls.setOpaque(false);

        searchField = new CustomComponents.ModernTextField("Search student, target, or comment...", 22);
        searchField.setToolTipText("Filter table in real-time");

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
            private void filterTable() {
                String text = searchField.getText().trim();
                if (text.isEmpty()) {
                    tableSorter.setRowFilter(null);
                } else {
                    tableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        CustomComponents.ModernButton refreshBtn = new CustomComponents.ModernButton(
                "Refresh", IconFactory.createRefreshIcon(14, Theme.TEXT_DARK), Theme.CARD_BG, Theme.TEXT_DARK
        );
        refreshBtn.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER, 1, true), new EmptyBorder(6, 12, 6, 12)));
        refreshBtn.addActionListener(e -> loadAllSubmissions());

        controls.add(searchField);
        controls.add(refreshBtn);

        topBar.add(titleBlock, BorderLayout.WEST);
        topBar.add(controls, BorderLayout.EAST);
        wrapper.add(topBar, BorderLayout.NORTH);

        // JTable
        String[] columns = {"Ref ID", "Student", "Category", "Target Name", "Rating", "Submitted At", "Comment"};
        allTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        allTable = new JTable(allTableModel);
        tableSorter = new TableRowSorter<>(allTableModel);
        allTable.setRowSorter(tableSorter);
        Theme.styleTable(allTable);

        // Custom Category Badge Renderer
        allTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFocus, int r, int c) {
                String type = val != null ? val.toString() : "";
                Color bg = type.equalsIgnoreCase("Event") ? Theme.EVENT_BADGE
                        : type.equalsIgnoreCase("Course") ? Theme.COURSE_BADGE : Theme.FACILITY_BADGE;
                return new CustomComponents.Badge(type, bg, Color.WHITE);
            }
        });

        allTable.getColumnModel().getColumn(0).setPreferredWidth(130);
        allTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        allTable.getColumnModel().getColumn(2).setPreferredWidth(85);
        allTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        allTable.getColumnModel().getColumn(4).setPreferredWidth(110);
        allTable.getColumnModel().getColumn(5).setPreferredWidth(140);
        allTable.getColumnModel().getColumn(6).setPreferredWidth(260);

        JScrollPane scroll = new JScrollPane(allTable);
        scroll.setBorder(new LineBorder(Theme.BORDER, 1, true));
        wrapper.add(scroll, BorderLayout.CENTER);

        loadAllSubmissions();
        return wrapper;
    }

    private void loadAllSubmissions() {
        allTableModel.setRowCount(0);
        List<Feedback> all = feedbackService.getAllFeedback();
        totalCountBadge.setText(all.size() + " Total Record" + (all.size() == 1 ? "" : "s"));

        for (Feedback fb : all) {
            allTableModel.addRow(new Object[]{
                    fb.getFeedbackId(),
                    fb.getStudentUsername(),
                    fb.getTargetType(),
                    fb.getTargetName(),
                    fb.getStarDisplay(),
                    fb.getTimestamp().format(Feedback.FORMATTER),
                    fb.getComment()
            });
        }
    }

    // =========================================================================
    // 2. CATEGORY FILTER PANEL
    // =========================================================================
    private JPanel createFilterPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(24, 30, 24, 30));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JPanel titleBlock = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleBlock.setOpaque(false);
        JLabel title = new JLabel("Filter Feedback by Category");
        title.setFont(Theme.FONT_HERO);
        title.setForeground(Theme.TEXT_DARK);

        filterCountBadge = new JLabel("0 Matching");
        filterCountBadge.setFont(Theme.FONT_SMALL_BOLD);
        filterCountBadge.setForeground(Theme.PRIMARY);
        filterCountBadge.setBackground(Theme.PRIMARY_LIGHT);
        filterCountBadge.setOpaque(true);
        filterCountBadge.setBorder(new EmptyBorder(4, 10, 4, 10));

        titleBlock.add(title);
        titleBlock.add(filterCountBadge);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);

        JLabel selectLabel = new JLabel("Category: ");
        selectLabel.setFont(Theme.FONT_BODY_BOLD);

        filterCombo = new JComboBox<>(new String[]{"Event", "Course", "Facility"});
        filterCombo.setFont(Theme.FONT_BODY);
        filterCombo.setBackground(Color.WHITE);
        filterCombo.addActionListener(e -> applyCategoryFilter());

        controls.add(selectLabel);
        controls.add(filterCombo);

        topBar.add(titleBlock, BorderLayout.WEST);
        topBar.add(controls, BorderLayout.EAST);
        wrapper.add(topBar, BorderLayout.NORTH);

        // Filter Table
        String[] columns = {"Ref ID", "Student", "Target Name", "Rating", "Submitted At", "Comment"};
        filterTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        filterTable = new JTable(filterTableModel);
        Theme.styleTable(filterTable);

        filterTable.getColumnModel().getColumn(0).setPreferredWidth(130);
        filterTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        filterTable.getColumnModel().getColumn(2).setPreferredWidth(210);
        filterTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        filterTable.getColumnModel().getColumn(4).setPreferredWidth(140);
        filterTable.getColumnModel().getColumn(5).setPreferredWidth(280);

        JScrollPane scroll = new JScrollPane(filterTable);
        scroll.setBorder(new LineBorder(Theme.BORDER, 1, true));
        wrapper.add(scroll, BorderLayout.CENTER);

        return wrapper;
    }

    private void applyCategoryFilter() {
        String category = (String) filterCombo.getSelectedItem();
        filterTableModel.setRowCount(0);
        if (category == null) return;

        List<Feedback> filtered = feedbackService.getFeedbackByTargetType(category);
        filterCountBadge.setText(filtered.size() + " " + category + " Review" + (filtered.size() == 1 ? "" : "s"));

        for (Feedback fb : filtered) {
            filterTableModel.addRow(new Object[]{
                    fb.getFeedbackId(),
                    fb.getStudentUsername(),
                    fb.getTargetName(),
                    fb.getStarDisplay(),
                    fb.getTimestamp().format(Feedback.FORMATTER),
                    fb.getComment()
            });
        }
    }

    // =========================================================================
    // 3. EXECUTIVE REPORT PANEL (Reportable Interface)
    // =========================================================================
    private JPanel createReportPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(24, 30, 24, 30));

        // Top Header
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        JLabel title = new JLabel("Campus Analytics & Executive Report");
        title.setFont(Theme.FONT_HERO);
        title.setForeground(Theme.TEXT_DARK);

        CustomComponents.ModernButton refreshBtn = new CustomComponents.ModernButton(
                "Recalculate Report", IconFactory.createRefreshIcon(14, Theme.TEXT_DARK), Theme.CARD_BG, Theme.TEXT_DARK
        );
        refreshBtn.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER, 1, true), new EmptyBorder(6, 12, 6, 12)));
        refreshBtn.addActionListener(e -> loadExecutiveReport());

        topBar.add(title, BorderLayout.WEST);
        topBar.add(refreshBtn, BorderLayout.EAST);

        // 4 Top KPI Cards Grid
        JPanel kpiGrid = new JPanel(new GridLayout(1, 4, 14, 0));
        kpiGrid.setOpaque(false);
        kpiGrid.setPreferredSize(new Dimension(0, 92));

        totalKpi = new CustomComponents.KpiCard("Total Feedback", "0", "Total student submissions", Theme.PRIMARY);
        avgRatingKpi = new CustomComponents.KpiCard("Overall Rating", "0.00 / 5.0", "Campus-wide average", Theme.WARNING);
        topTargetKpi = new CustomComponents.KpiCard("Top Rated Target", "N/A", "Highest average score", Theme.SUCCESS);
        lowTargetKpi = new CustomComponents.KpiCard("Lowest Rated Target", "N/A", "Lowest average score", Theme.DANGER);

        kpiGrid.add(totalKpi);
        kpiGrid.add(avgRatingKpi);
        kpiGrid.add(topTargetKpi);
        kpiGrid.add(lowTargetKpi);

        // Monospace Formatted Official Report Container
        JPanel reportCard = Theme.createCard();
        reportCard.setLayout(new BorderLayout(0, 10));

        JLabel reportLabel = new JLabel("Official Formatted Executive Report (Generated via Reportable.generateReport())");
        reportLabel.setFont(Theme.FONT_BODY_BOLD);
        reportLabel.setForeground(Theme.TEXT_DARK);

        reportTextArea = new JTextArea();
        reportTextArea.setFont(Theme.FONT_MONO);
        reportTextArea.setBackground(new Color(248, 250, 252));
        reportTextArea.setForeground(Theme.TEXT_DARK);
        reportTextArea.setEditable(false);
        reportTextArea.setBorder(new EmptyBorder(12, 14, 12, 14));

        JScrollPane reportScroll = new JScrollPane(reportTextArea);
        reportScroll.setBorder(new LineBorder(Theme.BORDER, 1, true));

        reportCard.add(reportLabel, BorderLayout.NORTH);
        reportCard.add(reportScroll, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 16));
        centerPanel.setOpaque(false);
        centerPanel.add(kpiGrid, BorderLayout.NORTH);
        centerPanel.add(reportCard, BorderLayout.CENTER);

        wrapper.add(topBar, BorderLayout.NORTH);
        wrapper.add(centerPanel, BorderLayout.CENTER);

        return wrapper;
    }

    private void loadExecutiveReport() {
        // Polymorphism: Calling generateReport through Reportable interface reference
        Reportable reportGenerator = reportService;
        String fullReport = reportGenerator.generateReport();
        reportTextArea.setText(fullReport);

        // Update top KPI cards
        List<Feedback> all = feedbackService.getAllFeedback();
        totalKpi.setValue(String.valueOf(all.size()), "Total submissions");

        if (all.isEmpty()) {
            avgRatingKpi.setValue("0.00 / 5.0", "No reviews yet");
            topTargetKpi.setValue("N/A", "Awaiting reviews");
            lowTargetKpi.setValue("N/A", "Awaiting reviews");
        } else {
            int sum = 0;
            for (Feedback fb : all) {
                sum += fb.getRating();
            }
            double avg = (double) sum / all.size();
            avgRatingKpi.setValue(String.format("%.2f / 5.0", avg), getStarDisplay(avg));

            // Compute highest and lowest targets
            computeTargetHighlights();
        }
    }

    private void computeTargetHighlights() {
        List<FeedbackTarget> targets = feedbackService.getPredefinedTargets();
        String highestName = "N/A";
        double highestAvg = -1.0;
        String lowestName = "N/A";
        double lowestAvg = 999.0;

        for (FeedbackTarget t : targets) {
            List<Feedback> all = feedbackService.getAllFeedback();
            int count = 0;
            int sum = 0;
            for (Feedback fb : all) {
                if (fb.getTargetId().equalsIgnoreCase(t.getId())) {
                    count++;
                    sum += fb.getRating();
                }
            }
            if (count > 0) {
                double avg = (double) sum / count;
                if (avg > highestAvg) {
                    highestAvg = avg;
                    highestName = t.getName();
                }
                if (avg < lowestAvg) {
                    lowestAvg = avg;
                    lowestName = t.getName();
                }
            }
        }

        if (highestAvg >= 0) {
            topTargetKpi.setValue(truncate(highestName, 18), String.format("%.2f / 5.0", highestAvg));
            lowTargetKpi.setValue(truncate(lowestName, 18), String.format("%.2f / 5.0", lowestAvg));
        }
    }

    private String getStarDisplay(double avg) {
        int stars = (int) Math.round(avg);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 1; i <= 5; i++) {
            sb.append(i <= stars ? "*" : "-");
        }
        sb.append("]");
        return sb.toString();
    }

    private String truncate(String text, int max) {
        if (text.length() <= max) return text;
        return text.substring(0, max - 3) + "...";
    }

    // =========================================================================
    // 4. ROLE & OOP DETAILS PANEL
    // =========================================================================
    private JPanel createRolePanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(24, 30, 24, 30));

        JLabel title = new JLabel("OOP Concept & Role Inspection");
        title.setFont(Theme.FONT_HERO);
        title.setForeground(Theme.TEXT_DARK);
        wrapper.add(title, BorderLayout.NORTH);

        JPanel card = Theme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        addInfoRow(card, "Runtime Class", admin.getClass().getName());
        addInfoRow(card, "Base Class", admin.getClass().getSuperclass().getName() + " (Abstract Class)");
        addInfoRow(card, "User Role", admin.getRole());
        addInfoRow(card, "Administrator Name", admin.getFullName());
        addInfoRow(card, "Department", admin.getDepartment());
        addInfoRow(card, "Username", admin.getUsername());
        addInfoRow(card, "Internal Admin ID", admin.getId());
        addInfoRow(card, "Polymorphic Method", "admin.getRoleDescription() -> Dynamic Method Dispatch");
        addInfoRow(card, "Role Description", admin.getRoleDescription());

        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    private void addInfoRow(JPanel container, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 0, 8, 0));

        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_BODY_BOLD);
        lbl.setForeground(Theme.TEXT_MUTED);
        lbl.setPreferredSize(new Dimension(200, 24));

        JLabel val = new JLabel(value);
        val.setFont(Theme.FONT_BODY);
        val.setForeground(Theme.TEXT_DARK);

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.CENTER);
        container.add(row);
        container.add(new JSeparator(SwingConstants.HORIZONTAL));
    }
}
