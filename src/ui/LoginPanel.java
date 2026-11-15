package ui;

import exception.DuplicateUserException;
import exception.InvalidLoginException;
import model.Admin;
import model.Student;
import model.User;
import service.AuthService;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Authentication view containing modern web-like card forms for:
 * 1. Student Login
 * 2. Administrator Login
 * 3. Student Self-Registration (Sign Up)
 * 
 * OOP Concepts Demonstrated:
 * 1. Polymorphism: Authenticates base User and routes dynamically based on instanceof Student/Admin.
 * 2. Exception Handling: Catches domain checked exceptions (InvalidLoginException, DuplicateUserException).
 * 3. Functional Interfaces: Uses Consumer<User> callback for decoupled event-driven UI routing.
 */
public class LoginPanel extends JPanel {
    private final AuthService authService;
    private final Consumer<User> onLoginSuccess;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardsPanel = new JPanel(cardLayout);

    // Alert banner for feedback
    private final CustomComponents.AlertBanner alertBanner = new CustomComponents.AlertBanner();

    // Login Form Fields with Placeholders
    private final CustomComponents.ModernTextField usernameField = 
            new CustomComponents.ModernTextField("e.g. john_doe or admin", 20);
    private final CustomComponents.ModernPasswordField passwordField = 
            new CustomComponents.ModernPasswordField("Enter your account password", 20);
    private boolean isAdminMode = false;

    // Registration Form Fields with Placeholders
    private final CustomComponents.ModernTextField regUsernameField = 
            new CustomComponents.ModernTextField("Choose unique username (min 3 chars)", 20);
    private final CustomComponents.ModernPasswordField regPasswordField = 
            new CustomComponents.ModernPasswordField("Create secure password (min 6 chars)", 20);
    private final CustomComponents.ModernTextField regFullNameField = 
            new CustomComponents.ModernTextField("e.g. John Doe", 20);
    private final CustomComponents.ModernTextField regRollNumberField = 
            new CustomComponents.ModernTextField("e.g. CS2026-042", 20);

    // Tab buttons
    private CustomComponents.ModernButton studentTabBtn;
    private CustomComponents.ModernButton adminTabBtn;
    private CustomComponents.ModernButton registerTabBtn;

    public LoginPanel(AuthService authService, Consumer<User> onLoginSuccess) {
        this.authService = authService;
        this.onLoginSuccess = onLoginSuccess;

        setLayout(new GridBagLayout());
        setBackground(Theme.BACKGROUND);

        // Center card container
        JPanel centerCard = new JPanel(new BorderLayout(0, 16));
        centerCard.setBackground(Theme.CARD_BG);
        centerCard.setPreferredSize(new Dimension(530, 640));
        centerCard.setMaximumSize(new Dimension(550, 660));
        centerCard.setBorder(new CompoundBorder(
                new LineBorder(Theme.BORDER, 1, true),
                new EmptyBorder(26, 36, 26, 36)
        ));

        // 1. Top Brand Header with vector logo icon
        JPanel brandHeader = createBrandHeader();
        centerCard.add(brandHeader, BorderLayout.NORTH);

        // 2. Segmented Navigation Tabs
        JPanel tabSwitcher = createTabSwitcher();

        // 3. Form Cards (Login Card & Registration Card)
        setupForms();

        JPanel middleSection = new JPanel(new BorderLayout(0, 14));
        middleSection.setOpaque(false);
        middleSection.add(tabSwitcher, BorderLayout.NORTH);
        middleSection.add(alertBanner, BorderLayout.CENTER);
        middleSection.add(cardsPanel, BorderLayout.SOUTH);

        centerCard.add(middleSection, BorderLayout.CENTER);

        // 4. Quick Demo Helper Footer
        JPanel footer = createFooter();
        centerCard.add(footer, BorderLayout.SOUTH);

        add(centerCard, new GridBagConstraints());
    }

    private JPanel createBrandHeader() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        // Vector Logo Label (Zero font or emoji dependency)
        JLabel logoPill = new JLabel(" CAMPUS FEEDBACK SYSTEM", IconFactory.createLogoIcon(24, Theme.PRIMARY), SwingConstants.CENTER);
        logoPill.setFont(Theme.FONT_SMALL_BOLD);
        logoPill.setForeground(Theme.PRIMARY);
        logoPill.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Welcome Back");
        title.setFont(Theme.FONT_HERO);
        title.setForeground(Theme.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign in to submit ratings or review campus analytics");
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(logoPill);
        p.add(Box.createVerticalStrut(6));
        p.add(title);
        p.add(Box.createVerticalStrut(4));
        p.add(subtitle);
        return p;
    }

    private JPanel createTabSwitcher() {
        JPanel switcher = new JPanel(new GridLayout(1, 3, 6, 0));
        switcher.setOpaque(false);

        studentTabBtn = new CustomComponents.ModernButton("Student Login", Theme.PRIMARY, Color.WHITE);
        adminTabBtn = new CustomComponents.ModernButton("Admin Login", Theme.BACKGROUND, Theme.TEXT_MUTED);
        registerTabBtn = new CustomComponents.ModernButton("Sign Up", Theme.BACKGROUND, Theme.TEXT_MUTED);

        studentTabBtn.setFont(Theme.FONT_SMALL_BOLD);
        adminTabBtn.setFont(Theme.FONT_SMALL_BOLD);
        registerTabBtn.setFont(Theme.FONT_SMALL_BOLD);

        studentTabBtn.addActionListener(e -> selectTab(false, false));
        adminTabBtn.addActionListener(e -> selectTab(true, false));
        registerTabBtn.addActionListener(e -> selectTab(false, true));

        switcher.add(studentTabBtn);
        switcher.add(adminTabBtn);
        switcher.add(registerTabBtn);
        return switcher;
    }

    private void selectTab(boolean admin, boolean register) {
        alertBanner.clear();
        this.isAdminMode = admin;

        if (register) {
            studentTabBtn.setColors(Theme.BACKGROUND, Theme.TEXT_MUTED);
            adminTabBtn.setColors(Theme.BACKGROUND, Theme.TEXT_MUTED);
            registerTabBtn.setColors(Theme.PRIMARY, Color.WHITE);
            cardLayout.show(cardsPanel, "REGISTER");
        } else if (admin) {
            studentTabBtn.setColors(Theme.BACKGROUND, Theme.TEXT_MUTED);
            adminTabBtn.setColors(Theme.PRIMARY, Color.WHITE);
            registerTabBtn.setColors(Theme.BACKGROUND, Theme.TEXT_MUTED);
            cardLayout.show(cardsPanel, "LOGIN");
        } else {
            studentTabBtn.setColors(Theme.PRIMARY, Color.WHITE);
            adminTabBtn.setColors(Theme.BACKGROUND, Theme.TEXT_MUTED);
            registerTabBtn.setColors(Theme.BACKGROUND, Theme.TEXT_MUTED);
            cardLayout.show(cardsPanel, "LOGIN");
        }
    }

    private void setupForms() {
        cardsPanel.setOpaque(false);

        // --- 1. Login Card ---
        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setOpaque(false);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(Theme.FONT_BODY_BOLD);
        userLabel.setForeground(Theme.TEXT_DARK);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(Theme.FONT_BODY_BOLD);
        passLabel.setForeground(Theme.TEXT_DARK);

        CustomComponents.ModernButton signInBtn = new CustomComponents.ModernButton("Sign In to Portal", Theme.PRIMARY, Color.WHITE);
        signInBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        signInBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signInBtn.addActionListener(e -> handleLogin());

        // Press Enter to submit
        usernameField.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin());

        loginCard.add(userLabel);
        loginCard.add(Box.createVerticalStrut(6));
        loginCard.add(usernameField);
        loginCard.add(Box.createVerticalStrut(12));
        loginCard.add(passLabel);
        loginCard.add(Box.createVerticalStrut(6));
        loginCard.add(passwordField);
        loginCard.add(Box.createVerticalStrut(20));
        loginCard.add(signInBtn);

        // --- 2. Register Card ---
        JPanel regCard = new JPanel();
        regCard.setLayout(new BoxLayout(regCard, BoxLayout.Y_AXIS));
        regCard.setOpaque(false);

        JLabel regUserLabel = new JLabel("Choose Username (min 3 chars)");
        regUserLabel.setFont(Theme.FONT_BODY_BOLD);

        JLabel regPassLabel = new JLabel("Password (min 6 chars)");
        regPassLabel.setFont(Theme.FONT_BODY_BOLD);

        JLabel regNameLabel = new JLabel("Full Name");
        regNameLabel.setFont(Theme.FONT_BODY_BOLD);

        JLabel regRollLabel = new JLabel("Student Roll Number");
        regRollLabel.setFont(Theme.FONT_BODY_BOLD);

        CustomComponents.ModernButton createAccBtn = new CustomComponents.ModernButton("Create Student Account", Theme.SUCCESS, Color.WHITE);
        createAccBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        createAccBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccBtn.addActionListener(e -> handleRegister());

        regCard.add(regUserLabel);
        regCard.add(Box.createVerticalStrut(4));
        regCard.add(regUsernameField);
        regCard.add(Box.createVerticalStrut(8));
        regCard.add(regPassLabel);
        regCard.add(Box.createVerticalStrut(4));
        regCard.add(regPasswordField);
        regCard.add(Box.createVerticalStrut(8));
        regCard.add(regNameLabel);
        regCard.add(Box.createVerticalStrut(4));
        regCard.add(regFullNameField);
        regCard.add(Box.createVerticalStrut(8));
        regCard.add(regRollLabel);
        regCard.add(Box.createVerticalStrut(4));
        regCard.add(regRollNumberField);
        regCard.add(Box.createVerticalStrut(16));
        regCard.add(createAccBtn);

        cardsPanel.add(loginCard, "LOGIN");
        cardsPanel.add(regCard, "REGISTER");
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
                new EmptyBorder(12, 0, 0, 0)
        ));

        JLabel hint = new JLabel("<html><b>Default Admin:</b> <font color='#4F46E5'>admin</font> / <font color='#4F46E5'>admin123</font></html>");
        hint.setFont(Theme.FONT_SMALL);
        hint.setForeground(Theme.TEXT_MUTED);

        JButton fillAdminBtn = new JButton("Use Default Admin");
        fillAdminBtn.setFont(Theme.FONT_SMALL_BOLD);
        fillAdminBtn.setForeground(Theme.PRIMARY);
        fillAdminBtn.setBorderPainted(false);
        fillAdminBtn.setContentAreaFilled(false);
        fillAdminBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fillAdminBtn.addActionListener(e -> {
            selectTab(true, false);
            usernameField.setText("admin");
            passwordField.setText("admin123");
            alertBanner.clear();
        });

        footer.add(hint, BorderLayout.WEST);
        footer.add(fillAdminBtn, BorderLayout.EAST);
        return footer;
    }

    private void handleLogin() {
        alertBanner.clear();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            alertBanner.showError("Please enter both username and password.");
            return;
        }

        try {
            User user = authService.login(username, password);

            // Role segregation check
            if (isAdminMode && !(user instanceof Admin)) {
                alertBanner.showError("Access Denied: '" + username + "' is a Student account. Please switch to Student Login.");
                return;
            }
            if (!isAdminMode && !(user instanceof Student)) {
                alertBanner.showError("Access Notice: '" + username + "' is an Admin account. Please switch to Admin Login.");
                return;
            }

            // Authentication succeeded
            onLoginSuccess.accept(user);

        } catch (InvalidLoginException e) {
            alertBanner.showError(e.getMessage());
        }
    }

    private void handleRegister() {
        alertBanner.clear();
        String u = regUsernameField.getText().trim();
        String p = new String(regPasswordField.getPassword()).trim();
        String name = regFullNameField.getText().trim();
        String roll = regRollNumberField.getText().trim();

        try {
            Student student = authService.registerStudent(u, p, name, roll);
            // Pre-fill login
            usernameField.setText(student.getUsername());
            passwordField.setText("");
            selectTab(false, false);
            alertBanner.showSuccess("Account created successfully for " + student.getFullName() + "! Please log in.");

            // Clear registration inputs
            regUsernameField.setText("");
            regPasswordField.setText("");
            regFullNameField.setText("");
            regRollNumberField.setText("");

        } catch (DuplicateUserException e) {
            alertBanner.showError(e.getMessage());
        } catch (IllegalArgumentException e) {
            alertBanner.showError(e.getMessage());
        }
    }
}
