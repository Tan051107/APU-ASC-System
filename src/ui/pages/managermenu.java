package ui.pages;

import javax.swing.*;

import ui.controller.ManagerMenuController;

import java.awt.*;

public class ManagerMenu extends JFrame {
    
    // Panel that will hold all the different views
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private boolean isExpanded = true;
    private final JPanel sidebar;
    private final JButton toggleButton;
    private final JButton btnManageUsers;
    private final JButton btnSetPrices;
    private final JButton btnFeedback;
    private final JButton btnReports;
    private final JButton btnLogOut;
    private final ManagerMenuController controller = new ManagerMenuController();

    public ManagerMenu() {
        setTitle("APU-ASC Manager Dashboard");
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
        btnManageUsers = createSidebarButton("Manage Users");
        btnSetPrices = createSidebarButton("Service Pricing");
        btnFeedback = createSidebarButton("View Feedback");
        btnReports = createSidebarButton("Reporting");
        btnLogOut = createSidebarButton("Log Out");

        sidebar.add(btnManageUsers);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnSetPrices);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnFeedback);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnReports);
        
        // 1. Add this "glue" to push everything below it to the bottom
        sidebar.add(Box.createVerticalGlue()); 
        
        sidebar.add(btnLogOut);

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

    private void toggleSidebar() {
        isExpanded = !isExpanded;
        if (isExpanded) {
            sidebar.setPreferredSize(new Dimension(220, getHeight()));
            btnManageUsers.setVisible(true);
            btnSetPrices.setVisible(true);
            btnFeedback.setVisible(true);
            btnReports.setVisible(true);
            btnLogOut.setVisible(true);
            toggleButton.setText("≡");
        } else {
            sidebar.setPreferredSize(new Dimension(60, getHeight()));
            btnManageUsers.setVisible(false);
            btnSetPrices.setVisible(false);
            btnFeedback.setVisible(false);
            btnReports.setVisible(false);
            btnLogOut.setVisible(false);
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

        JTable table = new JTable();
        table.setModel(controller.loadServiceToTable());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton updatePrice = createCRUDButton("Update Price");
        bottomPanel.add(updatePrice);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        updatePrice.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            
            // 1. Check if a row is actually selected (-1 means no selection)
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Please select a service from the table to update.", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Extract the data from the selected row
            String serviceId = table.getValueAt(selectedRow, 0).toString();
            String serviceName = table.getValueAt(selectedRow, 1).toString();
            String currentPriceStr = table.getValueAt(selectedRow, 2).toString(); 

            // 3. Remove "RM " if it's there so the text field only shows the number
            String cleanPrice = currentPriceStr.replace("RM", "").trim();

            // 4. Open the custom popup dialog
            showUpdatePriceDialog(serviceId, serviceName, cleanPrice, table);
        });
        
        return panel;
    }

    private void showUpdatePriceDialog(String id, String name, String currentPrice, JTable table) {
        // Create a panel with a grid layout (3 rows, 2 columns) for the form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        // Row 1: ID (Read-only)
        formPanel.add(new JLabel("Service ID:"));
        formPanel.add(new JLabel(id));

        // Row 2: Name (Read-only)
        formPanel.add(new JLabel("Service Name:"));
        formPanel.add(new JLabel(name));

        // Row 3: Price Input
        formPanel.add(new JLabel("New Price (RM):"));
        JTextField priceField = new JTextField(currentPrice);
        formPanel.add(priceField);

        // Show the popup dialog
        int result = JOptionPane.showConfirmDialog(
            this, 
            formPanel, 
            "Update Service Price", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );

        // If the user clicks "OK"
        /* if (result == JOptionPane.OK_OPTION) {
            try {
                // Parse the user's input into a double
                double newPrice = Double.parseDouble(priceField.getText().trim());
                
                // Optional: Round to 2 decimal places to ensure clean data
                newPrice = Math.round(newPrice * 100.0) / 100.0;

                // TODO: Here you need to call a method in your ManagerMenuController 
                // to actually save the new price to the Services.txt file. 
                // Example: controller.updateServicePrice(id, newPrice);

                // Refresh the table to show the newly updated data
                table.setModel(controller.loadServiceToTable());
                
                JOptionPane.showMessageDialog(this, "Price successfully updated!");

            } catch (NumberFormatException ex) {
                // If they type letters or symbols instead of a valid number
                JOptionPane.showMessageDialog(
                    this, 
                    "Invalid price format. Please enter numbers only.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } */
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