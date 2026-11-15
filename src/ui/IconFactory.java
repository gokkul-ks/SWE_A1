package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Factory class generating 100% Java2D vector icons via the javax.swing.Icon interface.
 * 
 * Why this is superior to Unicode emojis:
 * 1. Zero font dependencies: Guaranteed to render identically across all OS versions (no broken '[]' glyphs).
 * 2. Resolution independent: Vector-drawn paths scale cleanly with antialiasing.
 * 3. Color coordinated: Uses the exact theme color palette for an integrated desktop look.
 * 
 * OOP Concepts Demonstrated:
 * 1. Interface Implementation: Anonymous classes implementing javax.swing.Icon.
 * 2. Polymorphism: Reusable Icon objects plugged into JButtons, JLabels, and JTables.
 * 3. Encapsulation: Mathematical path rendering encapsulated inside factory methods.
 */
public final class IconFactory {

    private IconFactory() {}

    /**
     * Creates an academic graduation cap / shield logo icon.
     */
    public static Icon createLogoIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);

                // Diamond mortarboard top
                GeneralPath cap = new GeneralPath();
                cap.moveTo(size * 0.5f, size * 0.15f);
                cap.lineTo(size * 0.92f, size * 0.38f);
                cap.lineTo(size * 0.5f, size * 0.62f);
                cap.lineTo(size * 0.08f, size * 0.38f);
                cap.closePath();

                g2.setColor(color);
                g2.fill(cap);

                // Lower skull cap
                GeneralPath base = new GeneralPath();
                base.moveTo(size * 0.25f, size * 0.52f);
                base.quadTo(size * 0.5f, size * 0.88f, size * 0.75f, size * 0.52f);
                base.lineTo(size * 0.75f, size * 0.65f);
                base.quadTo(size * 0.5f, size * 0.98f, size * 0.25f, size * 0.65f);
                base.closePath();
                g2.fill(base);

                // Tassel hanging down the right
                g2.setStroke(new BasicStroke(Math.max(1.5f, size * 0.06f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine((int)(size * 0.86f), (int)(size * 0.42f), (int)(size * 0.86f), (int)(size * 0.78f));
                g2.fillOval((int)(size * 0.82f), (int)(size * 0.75f), (int)(size * 0.12f), (int)(size * 0.14f));

                g2.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Creates a 5-point mathematical star icon with customizable fill and border colors.
     */
    public static Icon createStarIcon(int size, Color fill, Color border) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);

                double cx = size / 2.0;
                double cy = size / 2.0;
                double outerR = size * 0.46;
                double innerR = outerR * 0.40;

                Path2D star = new Path2D.Double();
                for (int i = 0; i < 10; i++) {
                    double r = (i % 2 == 0) ? outerR : innerR;
                    double angle = -Math.PI / 2.0 + i * Math.PI / 5.0;
                    double px = cx + r * Math.cos(angle);
                    double py = cy + r * Math.sin(angle);
                    if (i == 0) star.moveTo(px, py);
                    else star.lineTo(px, py);
                }
                star.closePath();

                if (fill != null) {
                    g2.setColor(fill);
                    g2.fill(star);
                }
                if (border != null) {
                    g2.setColor(border);
                    g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.draw(star);
                }
                g2.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Pencil / edit icon for feedback submission.
     */
    public static Icon createSubmitIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(Math.max(1.5f, size * 0.08f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                // Pencil body angled
                GeneralPath path = new GeneralPath();
                path.moveTo(size * 0.20f, size * 0.80f);
                path.lineTo(size * 0.18f, size * 0.82f);
                path.lineTo(size * 0.35f, size * 0.75f);
                path.lineTo(size * 0.80f, size * 0.30f);
                path.lineTo(size * 0.70f, size * 0.20f);
                path.lineTo(size * 0.25f, size * 0.65f);
                path.closePath();
                g2.draw(path);

                // Eraser cap top
                g2.drawLine((int)(size * 0.70f), (int)(size * 0.20f), (int)(size * 0.78f), (int)(size * 0.12f));
                g2.drawLine((int)(size * 0.80f), (int)(size * 0.30f), (int)(size * 0.88f), (int)(size * 0.22f));
                g2.drawLine((int)(size * 0.78f), (int)(size * 0.12f), (int)(size * 0.88f), (int)(size * 0.22f));

                // Underline base
                g2.drawLine((int)(size * 0.15f), (int)(size * 0.90f), (int)(size * 0.85f), (int)(size * 0.90f));
                g2.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Clock / history icon for student feedback history.
     */
    public static Icon createHistoryIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(Math.max(1.6f, size * 0.08f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                // Outer clock circle
                g2.drawOval((int)(size * 0.12f), (int)(size * 0.12f), (int)(size * 0.76f), (int)(size * 0.76f));

                // Clock hands (pointing to 3 o'clock)
                g2.drawLine((int)(size * 0.50f), (int)(size * 0.50f), (int)(size * 0.50f), (int)(size * 0.28f));
                g2.drawLine((int)(size * 0.50f), (int)(size * 0.50f), (int)(size * 0.72f), (int)(size * 0.50f));
                g2.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Clipboard / list icon for viewing all feedback submissions.
     */
    public static Icon createClipboardIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(Math.max(1.5f, size * 0.08f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                // Board outline
                g2.drawRoundRect((int)(size * 0.18f), (int)(size * 0.20f), (int)(size * 0.64f), (int)(size * 0.72f), 6, 6);

                // Top clip
                g2.drawRoundRect((int)(size * 0.35f), (int)(size * 0.10f), (int)(size * 0.30f), (int)(size * 0.16f), 4, 4);

                // Horizontal document lines
                g2.drawLine((int)(size * 0.30f), (int)(size * 0.44f), (int)(size * 0.70f), (int)(size * 0.44f));
                g2.drawLine((int)(size * 0.30f), (int)(size * 0.60f), (int)(size * 0.70f), (int)(size * 0.60f));
                g2.drawLine((int)(size * 0.30f), (int)(size * 0.76f), (int)(size * 0.56f), (int)(size * 0.76f));
                g2.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Filter / funnel icon for categorical feedback filtering.
     */
    public static Icon createFilterIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(Math.max(1.5f, size * 0.08f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                // Funnel top and spout
                GeneralPath funnel = new GeneralPath();
                funnel.moveTo(size * 0.15f, size * 0.20f);
                funnel.lineTo(size * 0.85f, size * 0.20f);
                funnel.lineTo(size * 0.58f, size * 0.55f);
                funnel.lineTo(size * 0.58f, size * 0.85f);
                funnel.lineTo(size * 0.42f, size * 0.78f);
                funnel.lineTo(size * 0.42f, size * 0.55f);
                funnel.closePath();
                g2.draw(funnel);
                g2.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Analytics / bar chart icon for executive summary reports.
     */
    public static Icon createChartIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                g2.setColor(color);

                // Baseline
                g2.setStroke(new BasicStroke(Math.max(1.5f, size * 0.07f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine((int)(size * 0.10f), (int)(size * 0.88f), (int)(size * 0.90f), (int)(size * 0.88f));

                // 3 Rising bars
                int barW = (int)(size * 0.16f);
                // Bar 1 (left)
                g2.fill(new RoundRectangle2D.Float(size * 0.18f, size * 0.55f, barW, size * 0.33f, 3, 3));
                // Bar 2 (center)
                g2.fill(new RoundRectangle2D.Float(size * 0.42f, size * 0.35f, barW, size * 0.53f, 3, 3));
                // Bar 3 (right)
                g2.fill(new RoundRectangle2D.Float(size * 0.66f, size * 0.18f, barW, size * 0.70f, 3, 3));

                g2.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Information / OOP inspection icon (circle with letter 'i').
     */
    public static Icon createInfoIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(Math.max(1.6f, size * 0.08f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                // Outer circle
                g2.drawOval((int)(size * 0.12f), (int)(size * 0.12f), (int)(size * 0.76f), (int)(size * 0.76f));

                // 'i' dot
                g2.fillOval((int)(size * 0.44f), (int)(size * 0.28f), (int)(size * 0.12f), (int)(size * 0.12f));

                // 'i' stem
                g2.drawLine((int)(size * 0.50f), (int)(size * 0.46f), (int)(size * 0.50f), (int)(size * 0.72f));
                g2.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Sign out / logout icon (door bracket with right exit arrow).
     */
    public static Icon createLogoutIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(Math.max(1.6f, size * 0.08f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                // Left door bracket
                GeneralPath door = new GeneralPath();
                door.moveTo(size * 0.52f, size * 0.18f);
                door.lineTo(size * 0.20f, size * 0.18f);
                door.lineTo(size * 0.20f, size * 0.82f);
                door.lineTo(size * 0.52f, size * 0.82f);
                g2.draw(door);

                // Right pointing exit arrow
                g2.drawLine((int)(size * 0.35f), (int)(size * 0.50f), (int)(size * 0.84f), (int)(size * 0.50f));
                g2.drawLine((int)(size * 0.68f), (int)(size * 0.34f), (int)(size * 0.84f), (int)(size * 0.50f));
                g2.drawLine((int)(size * 0.68f), (int)(size * 0.66f), (int)(size * 0.84f), (int)(size * 0.50f));
                g2.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Circular refresh / reload icon.
     */
    public static Icon createRefreshIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(Math.max(1.6f, size * 0.08f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                // 280-degree circular arc
                g2.drawArc((int)(size * 0.18f), (int)(size * 0.18f), (int)(size * 0.64f), (int)(size * 0.64f), 45, 275);

                // Arrow head at top end
                GeneralPath arrow = new GeneralPath();
                arrow.moveTo(size * 0.62f, size * 0.10f);
                arrow.lineTo(size * 0.82f, size * 0.22f);
                arrow.lineTo(size * 0.62f, size * 0.32f);
                g2.draw(arrow);
                g2.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Smooth checkmark icon for success alerts.
     */
    public static Icon createCheckIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(Math.max(2.0f, size * 0.12f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                GeneralPath p = new GeneralPath();
                p.moveTo(size * 0.18f, size * 0.52f);
                p.lineTo(size * 0.44f, size * 0.78f);
                p.lineTo(size * 0.84f, size * 0.24f);
                g2.draw(p);
                g2.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }

    /**
     * Cross 'X' icon for error alerts.
     */
    public static Icon createCrossIcon(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(Math.max(2.0f, size * 0.12f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                g2.drawLine((int)(size * 0.24f), (int)(size * 0.24f), (int)(size * 0.76f), (int)(size * 0.76f));
                g2.drawLine((int)(size * 0.76f), (int)(size * 0.24f), (int)(size * 0.24f), (int)(size * 0.76f));
                g2.dispose();
            }

            @Override
            public int getIconWidth() { return size; }
            @Override
            public int getIconHeight() { return size; }
        };
    }
}
