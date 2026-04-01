package ui.pages;

import javax.swing.*;
import java.awt.*;

public class CounterStaffMenu extends JFrame {

    // Panel that will hold all the different views
    private final JPanel contentPanel;
    private final CardLayout cardLayout;
    private boolean isExpanded = true;
    private final JPanel sidebar;
    private final JButton toggleButton;
    private final JButton manageCustomerBtn;
    private final JButton manageAppointmentBtn;
    private final JButton managePaymentBtn;
    private final JButton myProfileBtn;

    public CounterStaffMenu() {
        setTitle("APU-ASC Counter Staff Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // 1. Create the Sidebar Navigation
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(45, 52, 54)); // Dark grey background
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Toggle Button
        toggleButton = new JButton("≡");
        styleToggleButton(toggleButton);
        toggleButton.addActionListener(e -> toggleSidebar());
        
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        togglePanel.setOpaque(false);
        togglePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        togglePanel.add(toggleButton);
        sidebar.add(togglePanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Create Navigation Buttons
        manageCustomerBtn = createSidebarButton("Manage Customers");
        manageAppointmentBtn = createSidebarButton("Manage Appointments");
        managePaymentBtn = createSidebarButton("Manage Payment");
        myProfileBtn = createSidebarButton("My Profile");

        sidebar.add(manageCustomerBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(manageAppointmentBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(managePaymentBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(myProfileBtn);

        add(sidebar, BorderLayout.WEST);

        // 2. Create the Content Panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Add the individual function panels to the CardLayout
        // The string acts as an ID to call the specific panel later
        contentPanel.add(createManageCustomerPanel(), "Manage Users");
        contentPanel.add(createManageAppointmentPanel(), "Service Pricing");
        contentPanel.add(createManagePaymentPanel(), "View Feedback");
        contentPanel.add(createReportsPanel(), "Reporting");

        add(contentPanel, BorderLayout.CENTER);

        // 3. Add Action Listeners to swap cards when buttons are clicked
        manageCustomerBtn.addActionListener(e -> cardLayout.show(contentPanel, "Manage Users"));
        manageAppointmentBtn.addActionListener(e -> cardLayout.show(contentPanel, "Service Pricing"));
        managePaymentBtn.addActionListener(e -> cardLayout.show(contentPanel, "View Feedback"));
    }

    private void toggleSidebar() {
        isExpanded = !isExpanded;
        if (isExpanded) {
            sidebar.setPreferredSize(new Dimension(220, getHeight()));
            manageCustomerBtn.setVisible(true);
            manageAppointmentBtn.setVisible(true);
            managePaymentBtn.setVisible(true);
            toggleButton.setText("≡");
        } else {
            sidebar.setPreferredSize(new Dimension(60, getHeight()));
            manageCustomerBtn.setVisible(false);
            manageAppointmentBtn.setVisible(false);
            managePaymentBtn.setVisible(false);
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

    // Helper method to style buttons uniformly
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

    // --- View Generation Methods ---

    private JPanel createManageCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = displayMenuTitle("Customer Account Management");
        panel.add(title, BorderLayout.NORTH);

        // Placeholder for JTable
        String[][] data = {{"1", "John Doe", "Customer"}, {"2", "Jane Smith", "Technician"}};
        String[] columns = {"User ID", "Name", "Role"};
        JTable table = new JTable(data, columns);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // CRUD Action Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addCustomerBtn = createCRUDButton("Add Customer");
        bottomPanel.add(addCustomerBtn);
        JButton editCustomerBtn = createCRUDButton("Edit Customer");
        bottomPanel.add(editCustomerBtn);
        JButton deleteCustomerBtn = createCRUDButton("Delete Customer");
        bottomPanel.add(deleteCustomerBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createManageAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = displayMenuTitle("Customer Appointment Management");
        panel.add(title, BorderLayout.NORTH);
        String[][] data = {{"Standard Oil Change", "RM 120.00"}, {"Tire Rotation", "RM 40.00"}};
        String[] columns = {"Service Type", "Current Price"};
        JTable table = new JTable(data, columns);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton updatePrice = createCRUDButton("Update Price");
        bottomPanel.add(updatePrice);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createManagePaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = displayMenuTitle("Customer Payment Management");
        panel.add(title, BorderLayout.NORTH);

        /* JTextArea feedbackArea = new JTextArea("Review ID 101: Great service on my car, highly recommended!\n\nReview ID 102: Waiting time was a bit longer than expected.");
        feedbackArea.setEditable(false);
        feedbackArea.setMargin(new Insets(10, 10, 10, 10));
        panel.add(new JScrollPane(feedbackArea), BorderLayout.CENTER); */

        // Placeholder for JTable
        String[][] data = {{"F001", "John Doe", "Car Kaput"}, {"F002", "Jane Smith", "HP increase"}};
        String[] columns = {"Feedback ID", "Customer Name", "Feedback Type"};
        JTable table = new JTable(data, columns);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton viewDetails = createCRUDButton("View Details");
        bottomPanel.add(viewDetails);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = displayMenuTitle("Service Centre Reporting & Analytics");
        panel.add(title, BorderLayout.NORTH);

        JTextArea reportArea = new JTextArea("--- Monthly Summary ---\nTotal Revenue: RM 15,400\nTotal Vehicles Serviced: 142\nMost Popular Service: Standard Oil Change");
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setMargin(new Insets(10, 10, 10, 10));
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        return panel;
    }

    private JLabel displayMenuTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        /* label.setHorizontalTextPosition(SwingConstants.CENTER); */
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return label;
    }

    private JButton createCRUDButton(String text) {
        JButton button = new JButton(text);
        Dimension buttonSize = new Dimension(150, 50);
        button.setPreferredSize(buttonSize);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(99, 110, 114));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        return button;
    }


}