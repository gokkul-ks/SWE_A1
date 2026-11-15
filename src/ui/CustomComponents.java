package ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Reusable, modern desktop widgets providing custom painting,
 * built-in placeholders, focus effects, and vector icon integration.
 * 
 * OOP Concepts Demonstrated:
 * 1. Inheritance: Subclasses standard Swing components (JButton, JTextField, JPasswordField, JPanel).
 * 2. Method Overriding: Overrides paintComponent(...) for custom antialiasing and placeholder rendering.
 * 3. Encapsulation: Encapsulates interactive state, hover logic, and visual transitions.
 */
public class CustomComponents {

    /**
     * Modern text field with built-in placeholder text and interactive focus border.
     */
    public static class ModernTextField extends JTextField {
        private String placeholder;
        private boolean isFocused = false;

        public ModernTextField(String placeholder, int columns) {
            super(columns);
            this.placeholder = placeholder;
            setFont(Theme.FONT_BODY);
            setForeground(Theme.TEXT_DARK);
            setBackground(Color.WHITE);
            setCaretColor(Theme.PRIMARY);
            updateBorder();

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isFocused = true;
                    updateBorder();
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    isFocused = false;
                    updateBorder();
                    repaint();
                }
            });
        }

        public ModernTextField(String placeholder) {
            this(placeholder, 20);
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
            repaint();
        }

        private void updateBorder() {
            Color borderColor = isFocused ? Theme.BORDER_FOCUS : Theme.BORDER;
            int thickness = isFocused ? 2 : 1;
            int padTop = isFocused ? 8 : 9;
            int padBottom = isFocused ? 8 : 9;
            int padLeft = isFocused ? 11 : 12;
            int padRight = isFocused ? 11 : 12;

            setBorder(new CompoundBorder(
                    new LineBorder(borderColor, thickness, true),
                    new EmptyBorder(padTop, padLeft, padBottom, padRight)
            ));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Paint placeholder text when empty
            if (placeholder != null && getText().isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(Theme.TEXT_PLACEHOLDER);
                g2.setFont(Theme.FONT_BODY);

                Insets insets = getInsets();
                FontMetrics fm = g2.getFontMetrics();
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(placeholder, insets.left + 2, y);
                g2.dispose();
            }
        }
    }

    /**
     * Modern password field with built-in placeholder text and interactive focus border.
     */
    public static class ModernPasswordField extends JPasswordField {
        private String placeholder;
        private boolean isFocused = false;

        public ModernPasswordField(String placeholder, int columns) {
            super(columns);
            this.placeholder = placeholder;
            setFont(Theme.FONT_BODY);
            setForeground(Theme.TEXT_DARK);
            setBackground(Color.WHITE);
            setCaretColor(Theme.PRIMARY);
            updateBorder();

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    isFocused = true;
                    updateBorder();
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    isFocused = false;
                    updateBorder();
                    repaint();
                }
            });
        }

        public ModernPasswordField(String placeholder) {
            this(placeholder, 20);
        }

        private void updateBorder() {
            Color borderColor = isFocused ? Theme.BORDER_FOCUS : Theme.BORDER;
            int thickness = isFocused ? 2 : 1;
            int padTop = isFocused ? 8 : 9;
            int padBottom = isFocused ? 8 : 9;
            int padLeft = isFocused ? 11 : 12;
            int padRight = isFocused ? 11 : 12;

            setBorder(new CompoundBorder(
                    new LineBorder(borderColor, thickness, true),
                    new EmptyBorder(padTop, padLeft, padBottom, padRight)
            ));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Paint placeholder when password is empty
            if (placeholder != null && getPassword().length == 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(Theme.TEXT_PLACEHOLDER);
                g2.setFont(Theme.FONT_BODY);

                Insets insets = getInsets();
                FontMetrics fm = g2.getFontMetrics();
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(placeholder, insets.left + 2, y);
                g2.dispose();
            }
        }
    }

    /**
     * Modern styled button with flat aesthetics, vector icon support, rounded corners, and hover transitions.
     */
    public static class ModernButton extends JButton {
        private Color normalColor;
        private Color hoverColor;
        private Color pressedColor;
        private boolean isHovered = false;
        private int cornerRadius = 8;

        public ModernButton(String text, Icon icon, Color bg, Color fg) {
            super(text, icon);
            this.normalColor = bg;
            this.hoverColor = brighten(bg, 0.12f);
            this.pressedColor = darken(bg, 0.15f);
            setForeground(fg);
            setFont(Theme.FONT_BODY_BOLD);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setIconTextGap(8);
            setBorder(new EmptyBorder(10, 18, 10, 18));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        public ModernButton(String text, Color bg, Color fg) {
            this(text, null, bg, fg);
        }

        public ModernButton(String text) {
            this(text, null, Theme.PRIMARY, Color.WHITE);
        }

        public void setColors(Color bg, Color fg) {
            this.normalColor = bg;
            this.hoverColor = brighten(bg, 0.12f);
            this.pressedColor = darken(bg, 0.15f);
            setForeground(fg);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(pressedColor);
            } else if (isHovered && isEnabled()) {
                g2.setColor(hoverColor);
            } else {
                g2.setColor(normalColor);
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
            super.paintComponent(g);
        }

        private static Color brighten(Color c, float fraction) {
            int r = Math.min(255, (int) (c.getRed() + (255 - c.getRed()) * fraction));
            int g = Math.min(255, (int) (c.getGreen() + (255 - c.getGreen()) * fraction));
            int b = Math.min(255, (int) (c.getBlue() + (255 - c.getBlue()) * fraction));
            return new Color(r, g, b);
        }

        private static Color darken(Color c, float fraction) {
            int r = Math.max(0, (int) (c.getRed() * (1 - fraction)));
            int g = Math.max(0, (int) (c.getGreen() * (1 - fraction)));
            int b = Math.max(0, (int) (c.getBlue() * (1 - fraction)));
            return new Color(r, g, b);
        }
    }

    /**
     * Modern interactive 5-star rating selector powered by vector IconFactory icons.
     * Guaranteed zero broken emoji or font glyph issues.
     */
    public static class StarRatingSelector extends JPanel {
        private int selectedRating = 5;
        private final JButton[] starButtons = new JButton[5];
        private final JLabel ratingDescriptionLabel = new JLabel("5 Stars (Excellent)");

        private static final Icon STAR_FILLED = IconFactory.createStarIcon(26, Theme.WARNING, Theme.WARNING_BORDER);
        private static final Icon STAR_EMPTY = IconFactory.createStarIcon(26, Theme.STAR_EMPTY_BG, Theme.STAR_EMPTY_BORDER);

        public StarRatingSelector() {
            setLayout(new FlowLayout(FlowLayout.LEFT, 4, 0));
            setOpaque(false);

            for (int i = 0; i < 5; i++) {
                final int starIndex = i + 1;
                JButton btn = new JButton(STAR_FILLED);
                btn.setBorderPainted(false);
                btn.setContentAreaFilled(false);
                btn.setFocusPainted(false);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btn.setMargin(new Insets(2, 2, 2, 2));

                btn.addActionListener(e -> setRating(starIndex));
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        highlightStars(starIndex);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        highlightStars(selectedRating);
                    }
                });

                starButtons[i] = btn;
                add(btn);
            }

            ratingDescriptionLabel.setFont(Theme.FONT_BODY_BOLD);
            ratingDescriptionLabel.setForeground(Theme.TEXT_MUTED);
            ratingDescriptionLabel.setBorder(new EmptyBorder(0, 10, 0, 0));
            add(ratingDescriptionLabel);

            setRating(5);
        }

        public void setRating(int rating) {
            this.selectedRating = Math.max(1, Math.min(5, rating));
            highlightStars(selectedRating);
            updateLabel();
        }

        public int getRating() {
            return selectedRating;
        }

        private void highlightStars(int count) {
            for (int i = 0; i < 5; i++) {
                if (i < count) {
                    starButtons[i].setIcon(STAR_FILLED);
                } else {
                    starButtons[i].setIcon(STAR_EMPTY);
                }
            }
        }

        private void updateLabel() {
            String[] labels = {
                "1 Star  (Poor)",
                "2 Stars (Fair)",
                "3 Stars (Average)",
                "4 Stars (Good)",
                "5 Stars (Excellent)"
            };
            ratingDescriptionLabel.setText(labels[selectedRating - 1]);
        }
    }

    /**
     * Web-style notification banner with vector Check and Cross icons.
     */
    public static class AlertBanner extends JPanel {
        private final JLabel iconLabel = new JLabel();
        private final JLabel messageLabel = new JLabel();

        public AlertBanner() {
            setLayout(new BorderLayout(10, 0));
            setBorder(new EmptyBorder(10, 14, 10, 14));
            setVisible(false);

            messageLabel.setFont(Theme.FONT_BODY);
            add(iconLabel, BorderLayout.WEST);
            add(messageLabel, BorderLayout.CENTER);
        }

        public void showSuccess(String message) {
            setBackground(Theme.SUCCESS_BG);
            iconLabel.setIcon(IconFactory.createCheckIcon(18, Theme.SUCCESS));
            iconLabel.setText("");
            messageLabel.setText(message);
            messageLabel.setForeground(Theme.SUCCESS);
            setVisible(true);
            revalidate();
            repaint();
        }

        public void showError(String message) {
            setBackground(Theme.DANGER_BG);
            iconLabel.setIcon(IconFactory.createCrossIcon(18, Theme.DANGER));
            iconLabel.setText("");
            messageLabel.setText(message);
            messageLabel.setForeground(Theme.DANGER);
            setVisible(true);
            revalidate();
            repaint();
        }

        public void clear() {
            setVisible(false);
            messageLabel.setText("");
            iconLabel.setIcon(null);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * KPI Metric summary card for admin executive analytics.
     */
    public static class KpiCard extends JPanel {
        private final JLabel titleLabel = new JLabel();
        private final JLabel valueLabel = new JLabel();
        private final JLabel subtitleLabel = new JLabel();

        public KpiCard(String title, String value, String subtitle, Color accentColor) {
            setLayout(new BorderLayout(0, 6));
            setBackground(Theme.CARD_BG);
            setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Theme.BORDER, 1, true),
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                            new EmptyBorder(14, 16, 14, 16)
                    )
            ));

            titleLabel.setText(title.toUpperCase());
            titleLabel.setFont(Theme.FONT_SMALL_BOLD);
            titleLabel.setForeground(Theme.TEXT_MUTED);

            valueLabel.setText(value);
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 21));
            valueLabel.setForeground(Theme.TEXT_DARK);

            subtitleLabel.setText(subtitle);
            subtitleLabel.setFont(Theme.FONT_SMALL);
            subtitleLabel.setForeground(Theme.TEXT_MUTED);

            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);
            top.add(titleLabel, BorderLayout.NORTH);
            top.add(valueLabel, BorderLayout.CENTER);

            add(top, BorderLayout.CENTER);
            add(subtitleLabel, BorderLayout.SOUTH);
        }

        public void setValue(String value, String subtitle) {
            valueLabel.setText(value);
            subtitleLabel.setText(subtitle);
            revalidate();
            repaint();
        }
    }

    /**
     * Pill badge for category tags (Event, Course, Facility, Role).
     */
    public static class Badge extends JLabel {
        private Color bgColor;

        public Badge(String text, Color bg, Color fg) {
            super(text);
            this.bgColor = bg;
            setForeground(fg);
            setFont(Theme.FONT_SMALL_BOLD);
            setHorizontalAlignment(SwingConstants.CENTER);
            setBorder(new EmptyBorder(4, 10, 4, 10));
            setOpaque(false);
        }

        public void setColors(Color bg, Color fg) {
            this.bgColor = bg;
            setForeground(fg);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Empty state component for tables and lists with clean vector icon and helpful message.
     */
    public static class EmptyStatePanel extends JPanel {
        public EmptyStatePanel(Icon icon, String title, String subtitle) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);
            setBorder(new EmptyBorder(40, 20, 40, 20));

            JLabel iconLbl = new JLabel(icon);
            iconLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel titleLbl = new JLabel(title);
            titleLbl.setFont(Theme.FONT_TITLE);
            titleLbl.setForeground(Theme.TEXT_DARK);
            titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel subLbl = new JLabel(subtitle);
            subLbl.setFont(Theme.FONT_BODY);
            subLbl.setForeground(Theme.TEXT_MUTED);
            subLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

            add(iconLbl);
            add(Box.createVerticalStrut(12));
            add(titleLbl);
            add(Box.createVerticalStrut(6));
            add(subLbl);
        }
    }
}
