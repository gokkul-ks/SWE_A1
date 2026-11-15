package ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Centralized UI Theme providing a modern, clean web-like design palette,
 * typography, and styling helpers for Java Swing components.
 * 
 * OOP Concepts Demonstrated:
 * 1. Encapsulation: Centralizes UI constants and styling policies.
 * 2. Static Utility Methods: Reusable component stylers.
 * 3. Final Keyword: Immutable color tokens and font definitions.
 */
public final class Theme {

    // Private constructor to prevent instantiation
    private Theme() {}

    // --- Modern Color Palette (Indigo / Slate Web Theme) ---
    public static final Color PRIMARY         = new Color(79, 70, 229);   // #4F46E5 Indigo 600
    public static final Color PRIMARY_HOVER   = new Color(67, 56, 202);   // #4338CA Indigo 700
    public static final Color PRIMARY_LIGHT   = new Color(238, 242, 255); // #EEF2FF Indigo 50
    public static final Color PRIMARY_BORDER  = new Color(199, 210, 254); // #C7D2FE Indigo 200

    public static final Color BACKGROUND      = new Color(248, 250, 252); // #F8FAFC Slate 50
    public static final Color CARD_BG         = Color.WHITE;              // #FFFFFF
    public static final Color SIDEBAR_BG      = new Color(15, 23, 42);    // #0F172A Slate 900
    public static final Color SIDEBAR_HOVER   = new Color(30, 41, 59);    // #1E293B Slate 800
    public static final Color SIDEBAR_ACTIVE  = new Color(51, 65, 85);    // #334155 Slate 700

    public static final Color TEXT_DARK       = new Color(15, 23, 42);    // #0F172A Deep Slate
    public static final Color TEXT_MUTED      = new Color(100, 116, 139); // #64748B Slate 500
    public static final Color TEXT_PLACEHOLDER = new Color(148, 163, 184); // #94A3B8 Slate 400
    public static final Color TEXT_LIGHT      = new Color(241, 245, 249); // #F1F5F9 Slate 100
    public static final Color TEXT_WHITE      = Color.WHITE;

    public static final Color BORDER          = new Color(226, 232, 240); // #E2E8F0 Slate 200
    public static final Color BORDER_FOCUS    = new Color(79, 70, 229);   // #4F46E5 Indigo 600

    public static final Color SUCCESS         = new Color(16, 185, 129);  // #10B981 Emerald 500
    public static final Color SUCCESS_BG      = new Color(236, 253, 245); // #ECFDF5 Emerald 50
    public static final Color SUCCESS_BORDER  = new Color(167, 243, 208); // #A7F3D0 Emerald 200

    public static final Color DANGER          = new Color(239, 68, 68);   // #EF4444 Rose 500
    public static final Color DANGER_BG       = new Color(254, 242, 242); // #FEF2F2 Rose 50
    public static final Color DANGER_BORDER   = new Color(254, 202, 202); // #FECACA Rose 200

    public static final Color WARNING         = new Color(245, 158, 11);  // #F59E0B Amber 500
    public static final Color WARNING_BORDER  = new Color(217, 119, 6);   // #D97706 Amber 600
    public static final Color WARNING_BG      = new Color(254, 252, 232); // #FEFCE8 Amber 50
    public static final Color STAR_EMPTY_BG   = new Color(241, 245, 249); // Slate 100
    public static final Color STAR_EMPTY_BORDER = new Color(203, 213, 225); // Slate 300

    public static final Color EVENT_BADGE     = new Color(147, 51, 234);  // Purple
    public static final Color COURSE_BADGE    = new Color(2, 132, 199);   // Sky Blue
    public static final Color FACILITY_BADGE  = new Color(13, 148, 136);  // Teal

    // --- Typography (Clean Modern Sans-Serif Scale) ---
    private static final String FONT_NAME     = "Segoe UI";
    public static final Font FONT_HERO        = new Font(FONT_NAME, Font.BOLD, 22);
    public static final Font FONT_TITLE       = new Font(FONT_NAME, Font.BOLD, 17);
    public static final Font FONT_SUBTITLE    = new Font(FONT_NAME, Font.BOLD, 14);
    public static final Font FONT_BODY        = new Font(FONT_NAME, Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD   = new Font(FONT_NAME, Font.BOLD, 13);
    public static final Font FONT_SMALL       = new Font(FONT_NAME, Font.PLAIN, 11);
    public static final Font FONT_SMALL_BOLD  = new Font(FONT_NAME, Font.BOLD, 11);
    public static final Font FONT_MONO        = new Font("Consolas", Font.PLAIN, 12);

    // --- Helper Stylers ---

    /**
     * Creates a web-card style container JPanel with padding and border.
     */
    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(18, 20, 18, 20)
        ));
        return card;
    }

    /**
     * Styles a standard JTextField with web-like padding, border and font.
     */
    public static void styleTextField(JTextField tf) {
        tf.setFont(FONT_BODY);
        tf.setForeground(TEXT_DARK);
        tf.setBackground(Color.WHITE);
        tf.setCaretColor(PRIMARY);
        tf.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
    }

    /**
     * Styles a modern web-like JTable with custom header, row height, and alternating rows.
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER);
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(PRIMARY);
        table.setFillsViewportHeight(true);
        table.setBackground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BODY_BOLD);
        header.setBackground(new Color(241, 245, 249));
        header.setForeground(TEXT_DARK);
        header.setPreferredSize(new Dimension(0, 38));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        header.setReorderingAllowed(false);

        // Center-align or pad cells nicely
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
                }
                return c;
            }
        };
        table.setDefaultRenderer(Object.class, renderer);
    }
}
