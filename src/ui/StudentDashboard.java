package ui;

import exception.DuplicateFeedbackException;
import exception.InvalidFeedbackException;
import model.Feedback;
import model.FeedbackTarget;
import model.Student;
import service.FeedbackService;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Student Portal Dashboard providing full GUI access to:
 * 1. Target selection and feedback submission with live star selector.
 * 2. Personal feedback history in a sortable, responsive modern table with
 * custom badges.
 * 3. Polymorphic role inspection card illustrating OOP principles.
 * 
 * OOP Concepts Demonstrated:
 * 1. Polymorphism: Interacts with abstract FeedbackTarget and calls
 * student.getRoleDescription().
 * 2. Event Handling: Implements ActionListener and DocumentListener for dynamic
 * validation.
 * 3. Custom Painting & Icons: Uses IconFactory vector icons (zero broken font
 * glyphs).
 */
public class StudentDashboard extends JPanel {
    private final Student student;
    private final FeedbackService feedbackService;
    private final Runnable onLogout;

    // Card navigation
    private final CardLayout contentCardLayout = new CardLayout();
    private final JPanel contentCards = new JPanel(contentCardLayout);

    // Sidebar navigation buttons
    private JButton navSubmitBtn;
    private JButton navHistoryBtn;
    private JButton navRoleBtn;

    // Submit View Components
    private final JComboBox<String> categoryCombo = new JComboBox<>(new String[] { "Event", "Course", "Facility" });
    private final JComboBox<FeedbackTarget> targetCombo = new JComboBox<>();
    private final CustomComponents.StarRatingSelector starSelector = new CustomComponents.StarRatingSelector();
    private final JTextArea commentArea = new JTextArea(4, 30);
    private final JLabel charCountLabel = new JLabel("0 / 300 characters (minimum 5 required)");
    private final CustomComponents.AlertBanner submitAlert = new CustomComponents.AlertBanner();

    // History View Components
    private DefaultTableModel historyTableModel;
    private JTable historyTable;
    private JLabel historyCountBadge;
    private JTextArea historyDetailArea;
    private JPanel historyTableCard;
    private CustomComponents.EmptyStatePanel historyEmptyState;

    public StudentDashboard(Student student, FeedbackService feedbackService, Runnable onLogout) {
        this.student = student;
        this.feedbackService = feedbackService;
        this.onLogout = onLogout;

        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        // 1. Left Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // 2. Right Center Cards
        setupCards();
        add(contentCards, BorderLayout.CENTER);

        // Default to Submit view
        showCard("SUBMIT", navSubmitBtn);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(Theme.SIDEBAR_BG);
        sidebar.setBorder(new EmptyBorder(24, 16, 24, 16));

        // Top: Brand & Student Profile
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);

        // Brand Label with Vector Logo Icon
        JLabel brand = new JLabel(" CampusFeedback", IconFactory.createLogoIcon(22, Theme.PRIMARY),
                SwingConstants.LEFT);
        brand.setFont(Theme.FONT_TITLE);
        brand.setForeground(Theme.TEXT_WHITE);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel brandSub = new JLabel("   Student Portal");
        brandSub.setFont(Theme.FONT_SMALL);
        brandSub.setForeground(Theme.TEXT_MUTED);
        brandSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Profile Badge Card
        JPanel profileCard = new JPanel(new BorderLayout(10, 0));
        profileCard.setBackground(Theme.SIDEBAR_HOVER);
        profileCard.setBorder(new EmptyBorder(12, 12, 12, 12));
        profileCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel avatar = new JLabel(student.getFullName().substring(0, 1).toUpperCase());
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 17));
        avatar.setForeground(Color.WHITE);
        avatar.setOpaque(true);
        avatar.setBackground(Theme.PRIMARY);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(36, 36));

        JPanel namePanel = new JPanel(new GridLayout(2, 1, 0, 2));
        namePanel.setOpaque(false);
        JLabel nameLbl = new JLabel(student.getFullName());
        nameLbl.setFont(Theme.FONT_BODY_BOLD);
        nameLbl.setForeground(Theme.TEXT_WHITE);

        JLabel rollLbl = new JLabel(student.getRollNumber());
        rollLbl.setFont(Theme.FONT_SMALL);
        rollLbl.setForeground(Theme.TEXT_MUTED);

        namePanel.add(nameLbl);
        namePanel.add(rollLbl);

        profileCard.add(avatar, BorderLayout.WEST);
        profileCard.add(namePanel, BorderLayout.CENTER);

        top.add(brand);
        top.add(Box.createVerticalStrut(2));
        top.add(brandSub);
        top.add(Box.createVerticalStrut(20));
        top.add(profileCard);
        top.add(Box.createVerticalStrut(24));

        // Navigation Links with Vector Icons
        JPanel navPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        navPanel.setOpaque(false);

        navSubmitBtn = createNavButton(" Submit Feedback", IconFactory.createSubmitIcon(16, Theme.TEXT_MUTED));
        navHistoryBtn = createNavButton(" My History", IconFactory.createHistoryIcon(16, Theme.TEXT_MUTED));
        navRoleBtn = createNavButton(" Role & OOP Info", IconFactory.createInfoIcon(16, Theme.TEXT_MUTED));

        navSubmitBtn.addActionListener(e -> showCard("SUBMIT", navSubmitBtn));
        navHistoryBtn.addActionListener(e -> {
            loadStudentHistory();
            showCard("HISTORY", navHistoryBtn);
        });
        navRoleBtn.addActionListener(e -> showCard("ROLE", navRoleBtn));

        navPanel.add(navSubmitBtn);
        navPanel.add(navHistoryBtn);
        navPanel.add(navRoleBtn);

        top.add(navPanel);
        sidebar.add(top, BorderLayout.NORTH);

        // Bottom: Logout with Vector Door Icon
        CustomComponents.ModernButton logoutBtn = new CustomComponents.ModernButton(
                "Sign Out", IconFactory.createLogoutIcon(16, Theme.DANGER), Theme.SIDEBAR_HOVER, Theme.DANGER);
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

        JButton[] buttons = { navSubmitBtn, navHistoryBtn, navRoleBtn };
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
        contentCards.add(createSubmitPanel(), "SUBMIT");
        contentCards.add(createHistoryPanel(), "HISTORY");
        contentCards.add(createRolePanel(), "ROLE");
    }

    // =========================================================================
    // 1. SUBMIT FEEDBACK PANEL
    // =========================================================================
    private JPanel createSubmitPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(24, 30, 24, 30));

        // Header
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setOpaque(false);
        JLabel title = new JLabel("Submit Campus Feedback");
        title.setFont(Theme.FONT_HERO);
        title.setForeground(Theme.TEXT_DARK);
        JLabel subtitle = new JLabel("Choose an Event, Course, or Facility and share your ratings and honest review.");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_MUTED);
        header.add(title);
        header.add(subtitle);

        wrapper.add(header, BorderLayout.NORTH);

        // Form Card
        JPanel formCard = Theme.createCard();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));

        formCard.add(submitAlert);
        formCard.add(Box.createVerticalStrut(10));

        // Category Selection
        JLabel catLabel = new JLabel("Target Category");
        catLabel.setFont(Theme.FONT_BODY_BOLD);
        categoryCombo.setFont(Theme.FONT_BODY);
        categoryCombo.setBackground(Color.WHITE);
        categoryCombo.addActionListener(e -> updateTargetDropdown());

        formCard.add(catLabel);
        formCard.add(Box.createVerticalStrut(6));
        formCard.add(categoryCombo);
        formCard.add(Box.createVerticalStrut(14));

        // Target Selection
        JLabel targetLabel = new JLabel("Select Target");
        targetLabel.setFont(Theme.FONT_BODY_BOLD);
        targetCombo.setFont(Theme.FONT_BODY);
        targetCombo.setBackground(Color.WHITE);
        targetCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof FeedbackTarget) {
                    FeedbackTarget t = (FeedbackTarget) value;
                    setText(t.getName() + "  [" + t.getDetails() + "]");
                }
                setBorder(new EmptyBorder(6, 10, 6, 10));
                return this;
            }
        });

        formCard.add(targetLabel);
        formCard.add(Box.createVerticalStrut(6));
        formCard.add(targetCombo);
        formCard.add(Box.createVerticalStrut(16));

        // Star Rating with interactive vector stars
        JLabel ratingLabel = new JLabel("Your Star Rating");
        ratingLabel.setFont(Theme.FONT_BODY_BOLD);
        formCard.add(ratingLabel);
        formCard.add(Box.createVerticalStrut(6));
        formCard.add(starSelector);
        formCard.add(Box.createVerticalStrut(16));

        // Comment Area with placeholder support
        JLabel commentLabel = new JLabel("Review Comment (5 to 300 characters)");
        commentLabel.setFont(Theme.FONT_BODY_BOLD);

        commentArea.setFont(Theme.FONT_BODY);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setBorder(new LineBorder(Theme.BORDER, 1, true));

        // Character counter
        charCountLabel.setFont(Theme.FONT_SMALL);
        charCountLabel.setForeground(Theme.TEXT_MUTED);

        commentArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateCount();
            }

            public void removeUpdate(DocumentEvent e) {
                updateCount();
            }

            public void changedUpdate(DocumentEvent e) {
                updateCount();
            }

            private void updateCount() {
                int len = commentArea.getText().trim().length();
                charCountLabel.setText(len + " / 300 characters (minimum 5 required)");
                if (len < 5 || len > 300) {
                    charCountLabel.setForeground(Theme.DANGER);
                } else {
                    charCountLabel.setForeground(Theme.SUCCESS);
                }
            }
        });

        formCard.add(commentLabel);
        formCard.add(Box.createVerticalStrut(6));
        formCard.add(commentScroll);
        formCard.add(Box.createVerticalStrut(4));
        formCard.add(charCountLabel);
        formCard.add(Box.createVerticalStrut(20));

        // Submit Button Row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);

        CustomComponents.ModernButton resetBtn = new CustomComponents.ModernButton("Reset", Theme.BACKGROUND,
                Theme.TEXT_MUTED);
        resetBtn.addActionListener(e -> resetSubmitForm());

        CustomComponents.ModernButton submitBtn = new CustomComponents.ModernButton(
                "Submit Feedback", IconFactory.createSubmitIcon(16, Color.WHITE), Theme.PRIMARY, Color.WHITE);
        submitBtn.addActionListener(e -> handleSubmitFeedback());

        btnRow.add(resetBtn);
        btnRow.add(submitBtn);

        formCard.add(btnRow);
        wrapper.add(formCard, BorderLayout.CENTER);

        // Initial target dropdown load
        updateTargetDropdown();
        return wrapper;
    }

    private void updateTargetDropdown() {
        String category = (String) categoryCombo.getSelectedItem();
        targetCombo.removeAllItems();
        if (category != null) {
            List<FeedbackTarget> targets = feedbackService.getTargetsByType(category);
            for (FeedbackTarget t : targets) {
                targetCombo.addItem(t);
            }
        }
    }

    private void resetSubmitForm() {
        submitAlert.clear();
        starSelector.setRating(5);
        commentArea.setText("");
        if (targetCombo.getItemCount() > 0) {
            targetCombo.setSelectedIndex(0);
        }
    }

    private void handleSubmitFeedback() {
        submitAlert.clear();
        FeedbackTarget target = (FeedbackTarget) targetCombo.getSelectedItem();
        if (target == null) {
            submitAlert.showError("Please select a target.");
            return;
        }

        int rating = starSelector.getRating();
        String comment = commentArea.getText().trim();

        try {
            // Invokes business service method with validation and 24h duplication rule
            Feedback fb = feedbackService.submitFeedback(
                    student.getUsername(),
                    target,
                    rating,
                    comment);

            submitAlert.showSuccess(String.format(
                    "Feedback submitted successfully! Reference ID: %s  (Rating: %d/5)",
                    fb.getFeedbackId(), fb.getRating()));

            // Reset inputs after success
            commentArea.setText("");
            starSelector.setRating(5);

        } catch (DuplicateFeedbackException e) {
            submitAlert.showError(e.getMessage());
        } catch (InvalidFeedbackException e) {
            submitAlert.showError(e.getMessage());
        }
    }

    // =========================================================================
    // 2. MY FEEDBACK HISTORY PANEL
    // =========================================================================
    private JPanel createHistoryPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(24, 30, 24, 30));

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JPanel titleBlock = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleBlock.setOpaque(false);
        JLabel title = new JLabel("My Submission History");
        title.setFont(Theme.FONT_HERO);
        title.setForeground(Theme.TEXT_DARK);

        historyCountBadge = new JLabel("0 Submissions");
        historyCountBadge.setFont(Theme.FONT_SMALL_BOLD);
        historyCountBadge.setForeground(Theme.PRIMARY);
        historyCountBadge.setBackground(Theme.PRIMARY_LIGHT);
        historyCountBadge.setOpaque(true);
        historyCountBadge.setBorder(new EmptyBorder(4, 10, 4, 10));

        titleBlock.add(title);
        titleBlock.add(historyCountBadge);

        CustomComponents.ModernButton refreshBtn = new CustomComponents.ModernButton(
                "Refresh", IconFactory.createRefreshIcon(14, Theme.TEXT_DARK), Theme.CARD_BG, Theme.TEXT_DARK);
        refreshBtn.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER, 1, true), new EmptyBorder(6, 12, 6, 12)));
        refreshBtn.addActionListener(e -> loadStudentHistory());

        topBar.add(titleBlock, BorderLayout.WEST);
        topBar.add(refreshBtn, BorderLayout.EAST);
        wrapper.add(topBar, BorderLayout.NORTH);

        // Table Model & JTable
        String[] columns = { "Ref ID", "Category", "Target Name", "Rating", "Submitted Date", "Review Comment" };
        historyTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = new JTable(historyTableModel);
        Theme.styleTable(historyTable);

        // Custom Category Badge Renderer
        historyTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFocus, int r,
                    int c) {
                String type = val != null ? val.toString() : "";
                Color bg = type.equalsIgnoreCase("Event") ? Theme.EVENT_BADGE
                        : type.equalsIgnoreCase("Course") ? Theme.COURSE_BADGE : Theme.FACILITY_BADGE;
                return new CustomComponents.Badge(type, bg, Color.WHITE);
            }
        });

        // Set column widths
        historyTable.getColumnModel().getColumn(0).setPreferredWidth(130);
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(90);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        historyTable.getColumnModel().getColumn(4).setPreferredWidth(140);
        historyTable.getColumnModel().getColumn(5).setPreferredWidth(280);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(new LineBorder(Theme.BORDER, 1, true));
        scrollPane.setBackground(Color.WHITE);

        // Detail Inspector Card below table
        JPanel detailCard = Theme.createCard();
        detailCard.setLayout(new BorderLayout(0, 6));
        detailCard.setPreferredSize(new Dimension(0, 110));

        JLabel detailHeader = new JLabel("Selected Feedback Review:");
        detailHeader.setFont(Theme.FONT_BODY_BOLD);
        detailHeader.setForeground(Theme.TEXT_DARK);

        historyDetailArea = new JTextArea("Click on any row above to inspect the complete feedback details.");
        historyDetailArea.setFont(Theme.FONT_BODY);
        historyDetailArea.setForeground(Theme.TEXT_MUTED);
        historyDetailArea.setLineWrap(true);
        historyDetailArea.setWrapStyleWord(true);
        historyDetailArea.setEditable(false);
        historyDetailArea.setOpaque(false);

        detailCard.add(detailHeader, BorderLayout.NORTH);
        detailCard.add(historyDetailArea, BorderLayout.CENTER);

        historyTable.getSelectionModel().addListSelectionListener(e -> {
            int row = historyTable.getSelectedRow();
            if (row >= 0) {
                String id = (String) historyTableModel.getValueAt(row, 0);
                String cat = (String) historyTableModel.getValueAt(row, 1);
                String target = (String) historyTableModel.getValueAt(row, 2);
                String stars = (String) historyTableModel.getValueAt(row, 3);
                String time = (String) historyTableModel.getValueAt(row, 4);
                String comment = (String) historyTableModel.getValueAt(row, 5);

                historyDetailArea.setText(String.format(
                        "[%s] %s (%s)  |  Rating: %s  |  Time: %s\n\"%s\"",
                        id, target, cat, stars, time, comment));
            }
        });

        historyTableCard = new JPanel(new BorderLayout(0, 12));
        historyTableCard.setOpaque(false);
        historyTableCard.add(scrollPane, BorderLayout.CENTER);
        historyTableCard.add(detailCard, BorderLayout.SOUTH);

        // Empty state panel
        historyEmptyState = new CustomComponents.EmptyStatePanel(
                IconFactory.createHistoryIcon(38, Theme.TEXT_MUTED),
                "No Feedback Submissions Yet",
                "You have not submitted any reviews. Click 'Submit Feedback' to add your first review.");

        wrapper.add(historyTableCard, BorderLayout.CENTER);
        return wrapper;
    }

    private void loadStudentHistory() {
        historyTableModel.setRowCount(0);
        List<Feedback> list = feedbackService.getFeedbackByStudent(student.getUsername());
        historyCountBadge.setText(list.size() + " Submission" + (list.size() == 1 ? "" : "s"));

        for (Feedback fb : list) {
            historyTableModel.addRow(new Object[] {
                    fb.getFeedbackId(),
                    fb.getTargetType(),
                    fb.getTargetName(),
                    fb.getStarDisplay(),
                    fb.getTimestamp().format(Feedback.FORMATTER),
                    fb.getComment()
            });
        }

        if (list.isEmpty()) {
            historyDetailArea
                    .setText("No feedback submissions found yet. Click 'Submit Feedback' to create your first review.");
        } else {
            historyTable.setRowSelectionInterval(0, 0);
        }
    }

    // =========================================================================
    // 3. ROLE & OOP DETAILS PANEL
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

        addInfoRow(card, "Runtime Class", student.getClass().getName());
        addInfoRow(card, "Base Class", student.getClass().getSuperclass().getName() + " (Abstract Class)");
        addInfoRow(card, "User Role", student.getRole());
        addInfoRow(card, "Full Name", student.getFullName());
        addInfoRow(card, "Roll Number", student.getRollNumber());
        addInfoRow(card, "Username", student.getUsername());
        addInfoRow(card, "Internal User ID", student.getId());
        addInfoRow(card, "Polymorphic Method", "student.getRoleDescription() -> Dynamic Method Dispatch");
        addInfoRow(card, "Role Description", student.getRoleDescription());

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
