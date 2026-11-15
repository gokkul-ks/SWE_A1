package ui;

import model.Admin;
import model.Student;
import model.User;
import service.AuthService;
import service.AutoSaveService;
import service.FeedbackService;
import service.ReportService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main application window (JFrame) hosting the single-window modern GUI.
 * Manages view transitions between Login, Student Dashboard, and Admin Dashboard.
 * 
 * OOP Concepts Demonstrated:
 * 1. Inheritance: Extends javax.swing.JFrame.
 * 2. Event Handling: Implements WindowListener to guarantee persistent data flush on exit.
 * 3. Polymorphism: Routes users dynamically based on their runtime concrete subclass (Student vs Admin).
 * 4. Encapsulation: Manages application lifecycle and service coordination.
 */
public class MainFrame extends JFrame {
    private final AuthService authService;
    private final FeedbackService feedbackService;
    private final ReportService reportService;
    private final AutoSaveService autoSaveService;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel rootPanel = new JPanel(cardLayout);

    public MainFrame(AuthService authService, FeedbackService feedbackService,
                     ReportService reportService, AutoSaveService autoSaveService) {
        super("Campus Feedback System — Modern Java GUI Portal");
        this.authService = authService;
        this.feedbackService = feedbackService;
        this.reportService = reportService;
        this.autoSaveService = autoSaveService;

        // Apply clean system typography and look
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Window properties
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1020, 680));
        setPreferredSize(new Dimension(1120, 750));
        setLocationRelativeTo(null);

        // Ensure safe persistent save on window close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleApplicationExit();
            }
        });

        // Setup Root Views
        setupViews();
        getContentPane().add(rootPanel);

        pack();
        setLocationRelativeTo(null);
    }

    private void setupViews() {
        // Initial Login View
        LoginPanel loginPanel = new LoginPanel(authService, this::handleUserAuthenticated);
        rootPanel.add(loginPanel, "LOGIN");
        cardLayout.show(rootPanel, "LOGIN");
    }

    private void handleUserAuthenticated(User user) {
        if (user instanceof Student) {
            Student student = (Student) user;
            StudentDashboard studentDashboard = new StudentDashboard(
                    student, feedbackService, this::handleLogout
            );
            rootPanel.add(studentDashboard, "STUDENT");
            cardLayout.show(rootPanel, "STUDENT");
            setTitle("Campus Feedback System — Student Portal (" + student.getFullName() + ")");
        } else if (user instanceof Admin) {
            Admin admin = (Admin) user;
            AdminDashboard adminDashboard = new AdminDashboard(
                    admin, feedbackService, reportService, this::handleLogout
            );
            rootPanel.add(adminDashboard, "ADMIN");
            cardLayout.show(rootPanel, "ADMIN");
            setTitle("Campus Feedback System — Administrator Portal (" + admin.getFullName() + ")");
        }
        revalidate();
        repaint();
    }

    private void handleLogout() {
        setTitle("Campus Feedback System — Modern Java GUI Portal");
        // Re-create login panel to ensure clean inputs
        LoginPanel loginPanel = new LoginPanel(authService, this::handleUserAuthenticated);
        rootPanel.add(loginPanel, "LOGIN");
        cardLayout.show(rootPanel, "LOGIN");
        revalidate();
        repaint();
    }

    private void handleApplicationExit() {
        // Gracefully shutdown background auto-save thread
        if (autoSaveService != null) {
            autoSaveService.stop();
        }

        // Persist final state to CSV files
        authService.saveUsers();
        feedbackService.saveFeedback();

        dispose();
        System.out.println("[GUI System] All data saved safely to data/. Application closed.");
        System.exit(0);
    }
}
