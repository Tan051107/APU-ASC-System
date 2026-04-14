package ui.pages;

import models.User;
import ui.controller.CounterStaffControllers.AppointmentManagementController;
import ui.controller.CounterStaffControllers.CustomerManagementController;
import ui.controller.CounterStaffControllers.PaymentRecordManagementController;
import ui.controller.NotificationPanelController;
import ui.controller.ProfilePanelController;
import ui.pages.CounterStaffPanels.ManageAppointmentPanel;
import ui.pages.CounterStaffPanels.ManageCustomerPanel;
import ui.pages.CounterStaffPanels.ManagePaymentPanel;

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
    private final JButton notificationBtn;
    public final JButton logOutBtn;
    private final User loginStaff;
    private NotificationPanelController notificationPanelController;

    public CounterStaffMenu(User loginStaff) {
        this.loginStaff = loginStaff;
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
        notificationBtn = createSidebarButton("Notifications");
        logOutBtn = createSidebarButton("Log Out");

        sidebar.add(manageCustomerBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(manageAppointmentBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(managePaymentBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(myProfileBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(notificationBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(logOutBtn);

        add(sidebar, BorderLayout.WEST);

        // 2. Create the Content Panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Add the individual function panels to the CardLayout
        // The string acts as an ID to call the specific panel later
        ManageCustomerPanel manageCustomerPanel = new ManageCustomerPanel();
        new CustomerManagementController(manageCustomerPanel);
        contentPanel.add(manageCustomerPanel, "Manage Customer");
        ManageAppointmentPanel manageAppointmentPanel = new ManageAppointmentPanel(loginStaff);
        contentPanel.add(manageAppointmentPanel, "Manage Appointment");
        ManagePaymentPanel managePaymentPanel = new ManagePaymentPanel(loginStaff);
        contentPanel.add(managePaymentPanel, "Manage Payment");
        new AppointmentManagementController(manageAppointmentPanel , managePaymentPanel);
        new PaymentRecordManagementController(managePaymentPanel);
        ProfilePanel profilePanel = new ProfilePanel();
        contentPanel.add(profilePanel , "My Profile");
        new ProfilePanelController(profilePanel,loginStaff);
        contentPanel.add(createNotificationPanel(), "Notifications");

        add(contentPanel, BorderLayout.CENTER);

        // 3. Add Action Listeners to swap cards when buttons are clicked
        manageCustomerBtn.addActionListener(e -> cardLayout.show(contentPanel, "Manage Customer"));
        manageAppointmentBtn.addActionListener(e -> cardLayout.show(contentPanel, "Manage Appointment"));
        managePaymentBtn.addActionListener(e -> cardLayout.show(contentPanel, "Manage Payment"));
        myProfileBtn.addActionListener(e -> cardLayout.show(contentPanel , "My Profile"));
        notificationBtn.addActionListener(e -> {
            if (notificationPanelController != null) {
                notificationPanelController.refreshNotifications();
            }
            cardLayout.show(contentPanel, "Notifications");
        });
        logOutBtn.addActionListener(e->logOut());
    }

    private void toggleSidebar() {
        isExpanded = !isExpanded;
        if (isExpanded) {
            sidebar.setPreferredSize(new Dimension(220, getHeight()));
            manageCustomerBtn.setVisible(true);
            manageAppointmentBtn.setVisible(true);
            managePaymentBtn.setVisible(true);
            myProfileBtn.setVisible(true);
            notificationBtn.setVisible(true);
            logOutBtn.setVisible(true);
            toggleButton.setText("≡");
        } else {
            sidebar.setPreferredSize(new Dimension(60, getHeight()));
            manageCustomerBtn.setVisible(false);
            manageAppointmentBtn.setVisible(false);
            managePaymentBtn.setVisible(false);
            myProfileBtn.setVisible(false);
            notificationBtn.setVisible(false);
            logOutBtn.setVisible(false);
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

    private void logOut(){
        this.dispose();
        new Login().createUI();
    }

    private JPanel createNotificationPanel() {
        NotificationPanel notificationPanel = new NotificationPanel();
        notificationPanelController = new NotificationPanelController(notificationPanel, loginStaff.getId());
        return notificationPanel;
    }

    public User getLoginStaff() {
        return loginStaff;
    }
}