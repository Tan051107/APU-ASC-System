package ui.pages;

import javax.swing.*;

import models.User;

import ui.controller.ManagerMenuController;
import ui.controller.UserManagementController;
import ui.utils.UIUtils;
import utils.DialogUtil;

import java.awt.*;

public class ManagerMenu extends JFrame {
    private final User user;
    public JButton addUser; 
    public JButton editUser;
    public JButton deleteUser;
    public JTable userTable;
    public JTable feedbackTable;
    public JButton viewFeedback;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private boolean isExpanded = true;
    private final JPanel sidebar;
    private final JButton toggleButton;
    private final JButton btnManageUsers;
    private final JButton btnSetPrices;
    private final JButton btnFeedback;
    private final JButton btnReports;
    public final JButton btnLogOut;
    private final ManagerMenuController controller;

    public ManagerMenu(User user) {
        this.user = user;
        controller = new ManagerMenuController(this);
        setTitle("APU-ASC Manager Dashboard");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(45, 52, 54));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
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
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnLogOut);

        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createManageUsersPanel(), "Manage Users");
        contentPanel.add(createSetPricesPanel(), "Service Pricing");
        contentPanel.add(createFeedbackPanel(), "View Feedback");
        contentPanel.add(createReportsPanel(), "Reporting");

        add(contentPanel, BorderLayout.CENTER);

        btnManageUsers.addActionListener(e -> cardLayout.show(contentPanel, "Manage Users"));
        btnSetPrices.addActionListener(e -> cardLayout.show(contentPanel, "Service Pricing"));
        btnFeedback.addActionListener(e -> cardLayout.show(contentPanel, "View Feedback"));
        btnReports.addActionListener(e -> cardLayout.show(contentPanel, "Reporting"));
        
        controller.initListeners();
        new UserManagementController(this); 
    
    }

    public User getUser() {
        return user;
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

    private JPanel createManageUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel title = displayMenuTitle("User Management (CRUD)");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(title); 

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        searchPanel.add(UIUtils.createLabel("Search ID: "));
        JTextField searchField = UIUtils.createTextField(); 
        searchField.setColumns(15);
        JButton searchButton = new JButton("Search");
        
        searchButton.setBackground(new Color(99, 110, 114));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        topPanel.add(searchPanel);
        panel.add(topPanel, BorderLayout.NORTH);  
        
        userTable = UIUtils.createTable(controller.loadUserToTable());;
        userTable.setModel(controller.loadUserToTable());
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setReorderingAllowed(false);
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String currenttext = searchField.getText().trim().toUpperCase();

            if (currenttext.matches("TP\\d{6}")) {
                boolean found = false;
                
                for (int i = 0; i < userTable.getRowCount(); i++) {
                    Object cellValue = userTable.getValueAt(i, 0); 
                    
                    if (cellValue != null) {
                        String searchid = cellValue.toString(); 
                        if (searchid.equals(currenttext)) {
                            userTable.setRowSelectionInterval(i, i); 
                            userTable.scrollRectToVisible(userTable.getCellRect(i, 0, true)); 
                            found = true;
                            break; 
                        }
                    }
                }
                
                if (!found) {
                    JOptionPane.showMessageDialog(this, "No User Found with ID: " + currenttext, "Search Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid format. Please use TP followed by 6 digits (e.g., TP012345).", "Format Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        searchField.addActionListener(e -> searchButton.doClick());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        addUser = UIUtils.createCRUDButton("Add User");
        bottomPanel.add(addUser);
        editUser = UIUtils.createCRUDButton("Edit User");
        bottomPanel.add(editUser);
        deleteUser = UIUtils.createCRUDButton("Delete User");
        bottomPanel.add(deleteUser);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createSetPricesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = displayMenuTitle("Automotive Service Pricing Management");
        panel.add(title, BorderLayout.NORTH);

        JTable table = UIUtils.createTable(controller.loadServiceToTable());;
        table.setModel(controller.loadServiceToTable());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton updatePrice = UIUtils.createCRUDButton("Update Price");
        bottomPanel.add(updatePrice);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        updatePrice.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a service to update.");
                return;
            }

            String clickedId = table.getValueAt(selectedRow, 0).toString();
            Object[] serviceData = controller.loadServiceDetails(clickedId);

            if (serviceData != null) {
                String name = serviceData[1].toString();
                String price = serviceData[2].toString();
                String details = serviceData[3] != null ? serviceData[3].toString() : "No details";
                String lastEdited = serviceData[4] != null ? serviceData[4].toString() : "Unknown";

                showUpdatePriceDialog(clickedId, name, price, details, lastEdited, table);
            }
        });
        
        return panel;
    }

    private void showUpdatePriceDialog(String id, String name, String price, String details,String lastEdited, JTable table) {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.add(new JLabel("Service ID:"));
        formPanel.add(new JLabel(id));


        formPanel.add(new JLabel("Service Name:"));
        formPanel.add(new JLabel(name));

        formPanel.add(new JLabel("New Price (RM):"));
        JTextField priceField = new JTextField(price);
        formPanel.add(priceField);

        formPanel.add(new JLabel("Service Details:"));
        formPanel.add(new JLabel(details));

        formPanel.add(new JLabel("Last Edited:"));
        formPanel.add(new JLabel(lastEdited));


        int result = JOptionPane.showConfirmDialog(
            this, 
            formPanel, 
            "Update Service Price", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                double newPrice = Double.parseDouble(priceField.getText().trim());
                newPrice = Math.round(newPrice * 100.0) / 100.0; 
                

                boolean isSuccess = controller.updateServicePrice(id, newPrice);

                if (isSuccess) {
                    table.setModel(controller.loadServiceToTable()); 
                    
                    JOptionPane.showMessageDialog(this, "Price successfully updated!");
                } else {
                    
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    this, 
                    "Invalid price format. Please enter numbers only.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private JPanel createFeedbackPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = displayMenuTitle("Customer Feedback & Reviews");
        panel.add(title, BorderLayout.NORTH);

        feedbackTable = UIUtils.createTable(controller.loadFeedbackToTable());
        feedbackTable.setModel(controller.loadFeedbackToTable());
        panel.add(new JScrollPane(feedbackTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        viewFeedback = UIUtils.createCRUDButton("View Details");
        bottomPanel.add(viewFeedback);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = displayMenuTitle("Service Centre Reporting & Analytics");
        panel.add(title, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String currentMonthAppointment = controller.getAppointmentTotal();
        
        gridPanel.add(createReportCard("Current Month Appointments", currentMonthAppointment, e -> {
            ui.pages.Manager.AppointmentReports viewPanel = new ui.pages.Manager.AppointmentReports(this);
            new ui.controller.AppointmentReportsController(viewPanel);
            viewPanel.setVisible(true);
        }));

        String currentMonthRevenue = controller.getRevenueTotal();
        gridPanel.add(createReportCard("Current Month Revenue", currentMonthRevenue, e -> {
            ui.pages.Manager.RevenueReports viewPanel = new ui.pages.Manager.RevenueReports(this);
            new ui.controller.RevenueReportsController(viewPanel);
            viewPanel.setVisible(true);
        }));
        
        gridPanel.add(createReportCard("Pending Feedbacks", "0", e -> {
            cardLayout.show(contentPanel, "View Feedback");
        }));

        panel.add(gridPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createReportCard(String titleText, String valueText, java.awt.event.ActionListener action) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1), 
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = UIUtils.createLabel(titleText);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = UIUtils.createLabel(valueText);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(Color.blue);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton viewMoreBtn = UIUtils.createCRUDButton("View More");
        viewMoreBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        if (action != null) {
            viewMoreBtn.addActionListener(action);
        }

        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(viewMoreBtn);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private JLabel displayMenuTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        /* label.setHorizontalTextPosition(SwingConstants.CENTER); */
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return label;
    }

    /* private JButton createCRUDButton(String text) {
        JButton button = new JButton(text);
        Dimension buttonSize = new Dimension(150, 50);
        button.setPreferredSize(buttonSize);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(99, 110, 114));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        return button;
    } */

    public void refreshUserTable() {
        if (userTable != null) {
            userTable.setModel(controller.loadUserToTable());
        }
    }
}