package ui.pages;

import ui.controller.CustomerController;
import ui.controller.NotificationPanelController;
import ui.controller.ProfilePanelController;
import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class CustomerMenu extends JFrame {

    // Controller used to handle customer business logic and file-based data operations
    private final CustomerController controller;

    // CardLayout is used to switch between customer pages inside one dashboard window
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    // Sidebar toggle state
    private boolean isExpanded = true;
    private final JPanel sidebar;
    private final JButton toggleButton;

    // Sidebar navigation buttons
    private final JButton btnServiceHistory;
    private final JButton btnPaymentHistory;
    private final JButton btnNotification;
    private final JButton btnFeedback;
    private final JButton btnComment;
    private final JButton btnProfile;
    private final JButton btnLogout;

    // Tables used to display appointment, payment, and feedback records
    private JTable serviceHistoryTable;
    private JTable paymentHistoryTable;
    private JTable feedbackTable;

    // Search and filter fields for appointment history
    private JTextField serviceSearchField;
    private JComboBox<String> serviceStatusFilterComboBox;

    // Search and filter fields for payment history
    private JTextField paymentSearchField;
    private JComboBox<String> paymentYearFilterComboBox;
    private JComboBox<String> paymentMonthFilterComboBox;

    // Feedback form components
    private JComboBox<String> appointmentComboBox;
    private JTextField staffField;
    private JTextField technicianField;
    private JSlider staffRatingSlider;
    private JSlider technicianRatingSlider;
    private JTextArea commentTextArea;

    // Controllers for shared panels
    private ProfilePanelController profilePanelController;
    private NotificationPanelController notificationPanelController;

    public CustomerMenu(String customerId) {
        // Initialize customer controller using currently logged-in customer ID
        this.controller = new CustomerController(customerId);

        setTitle("APU-ASC Customer Dashboard");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // =========================
        // Create collapsible sidebar
        // =========================
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBackground(new Color(45, 52, 54));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Toggle button for expanding/collapsing sidebar
        toggleButton = new JButton("≡");
        styleToggleButton(toggleButton);
        toggleButton.addActionListener(e -> toggleSidebar());

        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        togglePanel.setOpaque(false);
        togglePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        togglePanel.add(toggleButton);
        sidebar.add(togglePanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Navigation buttons for different customer features
        btnServiceHistory = createSidebarButton("View Appointments");
        btnPaymentHistory = createSidebarButton("Payment History");
        btnNotification = createSidebarButton("Notifications");
        btnFeedback = createSidebarButton("My Feedback");
        btnComment = createSidebarButton("Provide Feedback");
        btnProfile = createSidebarButton("My Profile");
        btnLogout = createSidebarButton("Log Out");

        sidebar.add(btnServiceHistory);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnPaymentHistory);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnFeedback);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnComment);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnProfile);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnNotification);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        // =================================
        // Register all dashboard pages/cards
        // =================================
        contentPanel.add(createServiceHistoryPanel(), "SERVICE_HISTORY");
        contentPanel.add(createPaymentHistoryPanel(), "PAYMENT_HISTORY");
        contentPanel.add(createNotificationPanel(), "NOTIFICATION");
        contentPanel.add(createFeedbackPanel(), "FEEDBACK");
        contentPanel.add(createCommentPanel(), "COMMENT");
        contentPanel.add(createProfilePanel(), "PROFILE");

        add(contentPanel, BorderLayout.CENTER);

        // ======================
        // Navigation button logic
        // ======================
        btnServiceHistory.addActionListener(e -> {
            refreshServiceHistory();
            cardLayout.show(contentPanel, "SERVICE_HISTORY");
        });

        btnPaymentHistory.addActionListener(e -> {
            refreshPaymentHistory();
            cardLayout.show(contentPanel, "PAYMENT_HISTORY");
        });

        btnFeedback.addActionListener(e -> {
            refreshFeedback();
            cardLayout.show(contentPanel, "FEEDBACK");
        });

        btnNotification.addActionListener(e -> {
            // Refresh notifications every time user opens notification panel
            if (notificationPanelController != null) {
                notificationPanelController.refreshNotifications();
            }
            cardLayout.show(contentPanel, "NOTIFICATION");
        });

        btnComment.addActionListener(e -> {
            // Reload completed appointments without submitted feedback
            refreshFeedbackAppointmentDropdown();
            cardLayout.show(contentPanel, "COMMENT");
        });

        btnProfile.addActionListener(e -> {
            // Initialize profile panel with current user information
            if (profilePanelController != null) {
                profilePanelController.initProfile();
            }
            cardLayout.show(contentPanel, "PROFILE");
        });

        btnLogout.addActionListener(e -> {
            // Close current dashboard and return to login page
            dispose();
            new Login().createUI();
        });

        // ===========================
        // Initial refresh when opened
        // ===========================
        refreshServiceHistory();
        refreshPaymentHistory();
        refreshFeedback();
        refreshFeedbackAppointmentDropdown();
        cardLayout.show(contentPanel, "SERVICE_HISTORY");
    }

    private void toggleSidebar() {
        // Toggle between expanded and collapsed sidebar view
        isExpanded = !isExpanded;

        if (isExpanded) {
            sidebar.setPreferredSize(new Dimension(220, getHeight()));
            btnServiceHistory.setVisible(true);
            btnPaymentHistory.setVisible(true);
            btnNotification.setVisible(true);
            btnFeedback.setVisible(true);
            btnComment.setVisible(true);
            btnProfile.setVisible(true);
            btnLogout.setVisible(true);
            toggleButton.setText("≡");
        } else {
            sidebar.setPreferredSize(new Dimension(60, getHeight()));
            btnServiceHistory.setVisible(false);
            btnPaymentHistory.setVisible(false);
            btnNotification.setVisible(false);
            btnFeedback.setVisible(false);
            btnComment.setVisible(false);
            btnProfile.setVisible(false);
            btnLogout.setVisible(false);
            toggleButton.setText("»");
        }

        sidebar.revalidate();
        sidebar.repaint();
    }

    private void styleToggleButton(JButton btn) {
        // Apply styling for sidebar toggle button
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(new Color(45, 52, 54));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JButton createSidebarButton(String text) {
        // Create a reusable styled sidebar button
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(99, 110, 114));
        button.setForeground(Color.WHITE);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return button;
    }

    private JPanel createServiceHistoryPanel() {
        // Create appointment history panel with search and status filter
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(UIUtils.createMenuTitle("View Appointments"), BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        topPanel.add(new JLabel("Search:"));
        serviceSearchField = UIUtils.createTextField();
        serviceSearchField.setPreferredSize(new Dimension(240, 35));
        serviceSearchField.setMaximumSize(new Dimension(240, 35));
        topPanel.add(serviceSearchField);

        topPanel.add(new JLabel("Status:"));
        serviceStatusFilterComboBox = new JComboBox<>(new String[]{"Assigned", "Completed", "All"});
        serviceStatusFilterComboBox.setPreferredSize(new Dimension(140, 35));
        serviceStatusFilterComboBox.setSelectedItem("Assigned");
        topPanel.add(serviceStatusFilterComboBox);

        JButton filterButton = UIUtils.createPrimaryButton("Apply");
        filterButton.setPreferredSize(new Dimension(100, 35));
        filterButton.addActionListener(e -> refreshServiceHistory());
        topPanel.add(filterButton);

        panel.add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Load default appointment table with Assigned filter
        serviceHistoryTable = UIUtils.createTable(
                controller.getServiceHistoryTableModel("", "Assigned")
        );
        panel.add(new JScrollPane(serviceHistoryTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPaymentHistoryPanel() {
        // Create payment history panel with search, year filter, and month filter
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(UIUtils.createMenuTitle("Payment History"), BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        topPanel.add(new JLabel("Search:"));
        paymentSearchField = UIUtils.createTextField();
        paymentSearchField.setPreferredSize(new Dimension(220, 35));
        paymentSearchField.setMaximumSize(new Dimension(220, 35));
        topPanel.add(paymentSearchField);

        topPanel.add(new JLabel("Year:"));
        paymentYearFilterComboBox = new JComboBox<>();
        paymentYearFilterComboBox.setPreferredSize(new Dimension(110, 35));
        topPanel.add(paymentYearFilterComboBox);

        topPanel.add(new JLabel("Month:"));
        paymentMonthFilterComboBox = new JComboBox<>();
        paymentMonthFilterComboBox.setPreferredSize(new Dimension(130, 35));
        topPanel.add(paymentMonthFilterComboBox);

        JButton filterButton = UIUtils.createPrimaryButton("Apply");
        filterButton.setPreferredSize(new Dimension(100, 35));
        filterButton.addActionListener(e -> refreshPaymentHistory());
        topPanel.add(filterButton);

        panel.add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Load payment records into payment history table
        paymentHistoryTable = UIUtils.createTable(
                controller.getPaymentHistoryTableModel("", "All", "All")
        );
        panel.add(new JScrollPane(paymentHistoryTable), BorderLayout.CENTER);

        // Load available year and month choices from payment data
        refreshPaymentFilterOptions();

        return panel;
    }

    private JPanel createFeedbackPanel() {
        // Create feedback history panel to show all feedback submitted by customer
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(UIUtils.createMenuTitle("My Feedback"), BorderLayout.NORTH);

        feedbackTable = UIUtils.createTable(controller.getFeedbackTableModel());
        panel.add(new JScrollPane(feedbackTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createNotificationPanel() {
        // Create notification panel and link it with notification controller
        NotificationPanel notificationPanel = new NotificationPanel();
        notificationPanelController = new NotificationPanelController(
                notificationPanel,
                controller.getCustomerUser().getId()
        );
        return notificationPanel;
    }

    private JPanel createCommentPanel() {
        // Create feedback submission form for completed appointments
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Provide Feedback");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel hintLabel = new JLabel("Note: Each completed appointment can only be submitted once.");
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hintLabel.setForeground(new Color(120, 120, 120));
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(hintLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(UIUtils.createLabel("Completed Appointment *"));
        appointmentComboBox = new JComboBox<>();
        appointmentComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        appointmentComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(appointmentComboBox);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Counter Staff"));
        staffField = UIUtils.createTextField();
        staffField.setEditable(false);
        mainPanel.add(staffField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Counter Staff Rating *"));
        staffRatingSlider = createRatingSlider();
        mainPanel.add(staffRatingSlider);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Technician"));
        technicianField = UIUtils.createTextField();
        technicianField.setEditable(false);
        mainPanel.add(technicianField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Technician Rating *"));
        technicianRatingSlider = createRatingSlider();
        mainPanel.add(technicianRatingSlider);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Comment (Optional)"));
        commentTextArea = new JTextArea(5, 20);
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        commentTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane textAreaScroll = new JScrollPane(commentTextArea);
        textAreaScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        textAreaScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        mainPanel.add(textAreaScroll);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Auto-update staff and technician fields when customer selects another appointment
        appointmentComboBox.addActionListener(e -> updateSelectedAppointmentPeople());

        JButton submitButton = UIUtils.createPrimaryButton("Submit Feedback");
        submitButton.addActionListener(e -> submitComment());
        mainPanel.add(submitButton);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(mainPanel, BorderLayout.NORTH);
        return wrapper;
    }

    private JPanel createProfilePanel() {
        // Create profile page and connect it with shared profile panel controller
        ProfilePanel profilePanel = new ProfilePanel();
        profilePanelController = new ProfilePanelController(profilePanel, controller.getCustomerUser());
        return profilePanel;
    }

    private void refreshServiceHistory() {
        // Refresh appointment history based on current search text and selected status filter
        String search = serviceSearchField == null ? "" : serviceSearchField.getText().trim();
        String status = serviceStatusFilterComboBox == null ? "Assigned" : serviceStatusFilterComboBox.getSelectedItem().toString();
        serviceHistoryTable.setModel(controller.getServiceHistoryTableModel(search, status));
    }

    private void refreshPaymentHistory() {
        // Refresh payment history based on search text and selected year/month filters
        String search = paymentSearchField == null ? "" : paymentSearchField.getText().trim();
        String year = paymentYearFilterComboBox == null || paymentYearFilterComboBox.getSelectedItem() == null
                ? "All"
                : paymentYearFilterComboBox.getSelectedItem().toString();
        String month = paymentMonthFilterComboBox == null || paymentMonthFilterComboBox.getSelectedItem() == null
                ? "All"
                : paymentMonthFilterComboBox.getSelectedItem().toString();

        paymentHistoryTable.setModel(controller.getPaymentHistoryTableModel(search, year, month));
    }

    private void refreshPaymentFilterOptions() {
        // Load available year and month values into combo boxes
        paymentYearFilterComboBox.setModel(controller.getPaymentYearComboModel());
        paymentMonthFilterComboBox.setModel(controller.getPaymentMonthComboModel());
    }

    private void refreshFeedback() {
        // Reload feedback table after feedback creation or page change
        feedbackTable.setModel(controller.getFeedbackTableModel());
    }

    private void refreshFeedbackAppointmentDropdown() {
        // Reload available completed appointments for feedback submission
        appointmentComboBox.setModel(controller.getCompletedAppointmentComboModel());
        commentTextArea.setText("");

        // Reset default slider values to neutral middle rating
        if (staffRatingSlider != null) {
            staffRatingSlider.setValue(3);
        }
        if (technicianRatingSlider != null) {
            technicianRatingSlider.setValue(3);
        }

        updateSelectedAppointmentPeople();
    }

    private void updateSelectedAppointmentPeople() {
        // Update staff and technician display fields when appointment selection changes
        if (appointmentComboBox == null || appointmentComboBox.getSelectedItem() == null) {
            if (staffField != null) {
                staffField.setText("");
            }
            if (technicianField != null) {
                technicianField.setText("");
            }
            return;
        }

        String appointmentId = appointmentComboBox.getSelectedItem().toString();
        String[] details = controller.getAppointmentPeopleDetails(appointmentId);

        if (details != null) {
            staffField.setText(details[0]);
            technicianField.setText(details[1]);
        } else {
            staffField.setText("");
            technicianField.setText("");
        }
    }

    private void submitComment() {
        // Submit feedback for selected completed appointment
        if (appointmentComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No completed appointment available for feedback.");
            return;
        }

        controller.submitFeedback(
                appointmentComboBox.getSelectedItem().toString(),
                String.valueOf(staffRatingSlider.getValue()),
                String.valueOf(technicianRatingSlider.getValue()),
                commentTextArea.getText()
        );

        // Refresh feedback table and appointment dropdown after submission
        refreshFeedback();
        refreshFeedbackAppointmentDropdown();
    }

    private JSlider createRatingSlider() {
        // Create reusable styled rating slider from 1 to 5
        JSlider slider = new JSlider(1, 5, 3);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setAlignmentX(Component.LEFT_ALIGNMENT);
        slider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        slider.setBackground(Color.WHITE);
        slider.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Customize slider appearance to match application UI theme
        slider.setUI(new javax.swing.plaf.basic.BasicSliderUI(slider) {
            @Override
            protected Dimension getThumbSize() {
                return new Dimension(14, 20);
            }

            @Override
            public void paintTrack(Graphics g) {
                Rectangle trackBounds = trackRect;
                int trackHeight = 4;
                int trackY = trackBounds.y + (trackBounds.height - trackHeight) / 2;

                g.setColor(new Color(220, 220, 220));
                g.fillRect(trackBounds.x, trackY, trackBounds.width, trackHeight);

                int thumbX = thumbRect.x + thumbRect.width / 2;
                int filledWidth = thumbX - trackBounds.x;

                if (filledWidth > 0) {
                    g.setColor(new Color(37, 99, 235));
                    g.fillRect(trackBounds.x, trackY, filledWidth, trackHeight);
                }
            }

            @Override
            public void paintThumb(Graphics g) {
                int x = thumbRect.x;
                int y = thumbRect.y;
                int w = thumbRect.width;
                int h = thumbRect.height;

                int[] xPoints = {x, x + w - 1, x + w - 1, x + w / 2, x};
                int[] yPoints = {y, y, y + h - 6, y + h - 1, y + h - 6};

                g.setColor(new Color(245, 245, 245));
                g.fillPolygon(xPoints, yPoints, 5);

                g.setColor(new Color(150, 150, 150));
                g.drawPolygon(xPoints, yPoints, 5);
            }

            @Override
            public void paintFocus(Graphics g) {
            }
        });

        return slider;
    }
}
