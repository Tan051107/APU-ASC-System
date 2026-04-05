package ui.pages;

import ui.controller.CustomerController;
import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class CustomerMenu extends JFrame {

    private final CustomerController controller;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);

    private boolean isExpanded = true;
    private final JPanel sidebar;
    private final JButton toggleButton;

    private final JButton btnServiceHistory;
    private final JButton btnPaymentHistory;
    private final JButton btnFeedback;
    private final JButton btnComment;
    private final JButton btnProfile;
    private final JButton btnLogout;

    private JTable serviceHistoryTable;
    private JTable paymentHistoryTable;
    private JTable feedbackTable;

    private JTextField serviceSearchField;
    private JComboBox<String> serviceStatusFilterComboBox;

    private JTextField paymentSearchField;
    private JComboBox<String> paymentYearFilterComboBox;
    private JComboBox<String> paymentMonthFilterComboBox;

    private JComboBox<String> appointmentComboBox;
    private JTextField staffField;
    private JTextField technicianField;
    private JSlider staffRatingSlider;
    private JSlider technicianRatingSlider;
    private JTextArea commentTextArea;

    private JTextField profileNameField;
    private JTextField profilePhoneField;

    public CustomerMenu(String customerId) {
        this.controller = new CustomerController(customerId);

        setTitle("APU-ASC Customer Dashboard");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBackground(new Color(45, 52, 54));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        toggleButton = new JButton("≡");
        styleToggleButton(toggleButton);
        toggleButton.addActionListener(e -> toggleSidebar());

        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        togglePanel.setOpaque(false);
        togglePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        togglePanel.add(toggleButton);
        sidebar.add(togglePanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        btnServiceHistory = createSidebarButton("View Appointments");
        btnPaymentHistory = createSidebarButton("Payment History");
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
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        contentPanel.add(createServiceHistoryPanel(), "SERVICE_HISTORY");
        contentPanel.add(createPaymentHistoryPanel(), "PAYMENT_HISTORY");
        contentPanel.add(createFeedbackPanel(), "FEEDBACK");
        contentPanel.add(createCommentPanel(), "COMMENT");
        contentPanel.add(createProfilePanel(), "PROFILE");

        add(contentPanel, BorderLayout.CENTER);

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

        btnComment.addActionListener(e -> {
            refreshFeedbackAppointmentDropdown();
            cardLayout.show(contentPanel, "COMMENT");
        });

        btnProfile.addActionListener(e -> {
            loadProfileData();
            cardLayout.show(contentPanel, "PROFILE");
        });

        btnLogout.addActionListener(e -> {
            dispose();
            new Login().createUI();
        });

        refreshServiceHistory();
        refreshPaymentHistory();
        refreshFeedback();
        refreshFeedbackAppointmentDropdown();
        loadProfileData();
        cardLayout.show(contentPanel, "SERVICE_HISTORY");
    }

    private void toggleSidebar() {
        isExpanded = !isExpanded;
        if (isExpanded) {
            sidebar.setPreferredSize(new Dimension(220, getHeight()));
            btnServiceHistory.setVisible(true);
            btnPaymentHistory.setVisible(true);
            btnFeedback.setVisible(true);
            btnComment.setVisible(true);
            btnProfile.setVisible(true);
            btnLogout.setVisible(true);
            toggleButton.setText("≡");
        } else {
            sidebar.setPreferredSize(new Dimension(60, getHeight()));
            btnServiceHistory.setVisible(false);
            btnPaymentHistory.setVisible(false);
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
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setBackground(new Color(45, 52, 54));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JButton createSidebarButton(String text) {
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

        serviceHistoryTable = UIUtils.createTable(
                controller.getServiceHistoryTableModel("", "Assigned")
        );
        panel.add(new JScrollPane(serviceHistoryTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPaymentHistoryPanel() {
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

        paymentHistoryTable = UIUtils.createTable(
                controller.getPaymentHistoryTableModel("", "All", "All")
        );
        panel.add(new JScrollPane(paymentHistoryTable), BorderLayout.CENTER);

        refreshPaymentFilterOptions();

        return panel;
    }

    private JPanel createFeedbackPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(UIUtils.createMenuTitle("My Feedback"), BorderLayout.NORTH);

        feedbackTable = UIUtils.createTable(controller.getFeedbackTableModel());
        panel.add(new JScrollPane(feedbackTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCommentPanel() {
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

        mainPanel.add(UIUtils.createLabel("Technician"));
        technicianField = UIUtils.createTextField();
        technicianField.setEditable(false);
        mainPanel.add(technicianField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Staff Rating *"));
        staffRatingSlider = createRatingSlider();
        mainPanel.add(staffRatingSlider);
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

        appointmentComboBox.addActionListener(e -> updateSelectedAppointmentPeople());

        JButton submitButton = UIUtils.createPrimaryButton("Submit Feedback");
        submitButton.addActionListener(e -> submitComment());
        mainPanel.add(submitButton);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(mainPanel, BorderLayout.NORTH);
        return wrapper;
    }

    private JPanel createProfilePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("My Profile");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(UIUtils.createLabel("Current Name"));
        profileNameField = UIUtils.createTextField();
        mainPanel.add(profileNameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Current Phone Number"));
        profilePhoneField = UIUtils.createTextField();
        mainPanel.add(profilePhoneField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton updateButton = UIUtils.createPrimaryButton("Update Profile");
        updateButton.addActionListener(e -> updateProfile());
        mainPanel.add(updateButton);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(mainPanel, BorderLayout.NORTH);
        return wrapper;
    }

    private void refreshServiceHistory() {
        String search = serviceSearchField == null ? "" : serviceSearchField.getText().trim();
        String status = serviceStatusFilterComboBox == null ? "Assigned" : serviceStatusFilterComboBox.getSelectedItem().toString();
        serviceHistoryTable.setModel(controller.getServiceHistoryTableModel(search, status));
    }

    private void refreshPaymentHistory() {
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
        paymentYearFilterComboBox.setModel(controller.getPaymentYearComboModel());
        paymentMonthFilterComboBox.setModel(controller.getPaymentMonthComboModel());
    }

    private void refreshFeedback() {
        feedbackTable.setModel(controller.getFeedbackTableModel());
    }

    private void refreshFeedbackAppointmentDropdown() {
        appointmentComboBox.setModel(controller.getCompletedAppointmentComboModel());
        commentTextArea.setText("");
        if (staffRatingSlider != null) {
            staffRatingSlider.setValue(3);
        }
        if (technicianRatingSlider != null) {
            technicianRatingSlider.setValue(3);
        }
        updateSelectedAppointmentPeople();
    }

    private void updateSelectedAppointmentPeople() {
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

    private void loadProfileData() {
        controller.loadProfileIntoFields(profileNameField, profilePhoneField);
    }

    private void updateProfile() {
        controller.updateProfile(profileNameField.getText(), profilePhoneField.getText());
    }

    private void submitComment() {
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

        refreshFeedback();
        refreshFeedbackAppointmentDropdown();
    }
    
    private JSlider createRatingSlider() {
        JSlider slider = new JSlider(1, 5, 3);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setAlignmentX(Component.LEFT_ALIGNMENT);
        slider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        slider.setBackground(Color.WHITE);
        return slider;
    }
}
