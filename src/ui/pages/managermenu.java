package ui.pages;

import javax.swing.*;

import ui.controller.ManagerMenuController;

import java.awt.*;

public class managermenu extends JFrame {
    
    // Panel that will hold all the different views
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private final ManagerMenuController controller = new ManagerMenuController();

    public managermenu() {
        setTitle("APU-ASC Manager Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // 1. Create the Sidebar Navigation
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(6, 1, 10, 10));
        sidebar.setBackground(new Color(45, 52, 54)); // Dark grey background
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Create Navigation Buttons
        JButton btnManageUsers = createSidebarButton("Manage Users");
        JButton btnSetPrices = createSidebarButton("Service Pricing");
        JButton btnFeedback = createSidebarButton("View Feedback");
        JButton btnReports = createSidebarButton("Reporting");

        sidebar.add(btnManageUsers);
        sidebar.add(btnSetPrices);
        sidebar.add(btnFeedback);
        sidebar.add(btnReports);

        add(sidebar, BorderLayout.WEST);

        // 2. Create the Content Panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Add the individual function panels to the CardLayout
        // The string acts as an ID to call the specific panel later
        contentPanel.add(createManageUsersPanel(), "Manage Users");
        contentPanel.add(createSetPricesPanel(), "Service Pricing");
        contentPanel.add(createFeedbackPanel(), "View Feedback");
        contentPanel.add(createReportsPanel(), "Reporting");

        add(contentPanel, BorderLayout.CENTER);

        // 3. Add Action Listeners to swap cards when buttons are clicked
        btnManageUsers.addActionListener(e -> cardLayout.show(contentPanel, "Manage Users"));
        btnSetPrices.addActionListener(e -> cardLayout.show(contentPanel, "Service Pricing"));
        btnFeedback.addActionListener(e -> cardLayout.show(contentPanel, "View Feedback"));
        btnReports.addActionListener(e -> cardLayout.show(contentPanel, "Reporting"));
    }

    // Helper method to style buttons uniformly
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(99, 110, 114));
        button.setForeground(Color.WHITE);
        return button;
    }

    // --- View Generation Methods ---

    private JPanel createManageUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = displayMenuTitle("User Management (CRUD)");
        panel.add(title, BorderLayout.NORTH);
        
        JTable table = new JTable();
        table.setModel(controller.loadUserToTable());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // CRUD Action Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addUser = createCRUDButton("Add User");
        bottomPanel.add(addUser);
        JButton editUser = createCRUDButton("Edit User");
        bottomPanel.add(editUser);
        JButton deleteUser = createCRUDButton("Delete User");
        bottomPanel.add(deleteUser);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createSetPricesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = displayMenuTitle("Automotive Service Pricing Management");
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

    private JPanel createFeedbackPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = displayMenuTitle("Customer Feedback & Reviews");
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