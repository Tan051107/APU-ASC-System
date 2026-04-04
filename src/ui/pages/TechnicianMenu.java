package ui.pages;

import javax.swing.*;

import models.Appointment;
import models.User;
import ui.controller.TechnicianMenuController;
import ui.pages.TechnicianPanels.ViewAppointment;
import ui.utils.UIUtils;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TechnicianMenu extends JFrame {
    private final TechnicianMenuController controller;
    private final JPanel contentPanel;
    private final CardLayout cardLayout;
    private boolean isExpanded = true;
    private final JPanel sidebar;
    private final JButton toggleButton;
    private final JButton appointmentsBtn;
    private final JButton historyBtn;
    private final JButton myProfileBtn;
    private final JButton logOutBtn;
    private Runnable refreshAppointmentsTask;
    private Runnable refreshHistoryTask;

    public TechnicianMenu(User user) {
        this.controller = new TechnicianMenuController(user.getId());
        setTitle("APU-ASC Technnician Dashboard");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(45, 52, 54)); // Dark grey background
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Toggle Buttons
        toggleButton = new JButton("≡");
        styleToggleButton(toggleButton);
        toggleButton.addActionListener(e -> toggleSidebar());
        
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        togglePanel.setOpaque(false);
        togglePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        togglePanel.add(toggleButton);
        sidebar.add(togglePanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Sidebar Buttons
        appointmentsBtn = createSidebarButton("Appointments");
        historyBtn = createSidebarButton("History");
        myProfileBtn = createSidebarButton("My Profile");
        logOutBtn = createSidebarButton("Log Out");

        sidebar.add(appointmentsBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(historyBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(myProfileBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(logOutBtn);

        add(sidebar, BorderLayout.WEST);

        // 2. Create the Content Panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createAppointmentsPanel(), "Appointments");
        contentPanel.add(createHistoryPanel(), "History");
        /* contentPanel.add(createReportsPanel(), "Reporting"); */

        add(contentPanel, BorderLayout.CENTER);

        // Action Listeners
        appointmentsBtn.addActionListener(e -> {
            if (refreshAppointmentsTask != null) {
                refreshAppointmentsTask.run();
            }
            cardLayout.show(contentPanel, "Appointments");
        });

        historyBtn.addActionListener(e -> {
            if (refreshHistoryTask != null) {
                refreshHistoryTask.run();
            }
            cardLayout.show(contentPanel, "History");
        });

        logOutBtn.addActionListener(e -> {
            this.dispose();
            new Login().createUI(); 
        });

    }

    private void toggleSidebar() {
        isExpanded = !isExpanded;
        if (isExpanded) {
            sidebar.setPreferredSize(new Dimension(220, getHeight()));
            appointmentsBtn.setVisible(true);
            historyBtn.setVisible(true);
            myProfileBtn.setVisible(true);
            logOutBtn.setVisible(true);
            toggleButton.setText("≡");
        } else {
            sidebar.setPreferredSize(new Dimension(60, getHeight()));
            appointmentsBtn.setVisible(false);
            historyBtn.setVisible(false);
            myProfileBtn.setVisible(false);
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

    //Button Styles
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

    //Main Content Builder

    private JPanel createAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

        JLabel title = displayMenuTitle("Technician Appointment Management");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        topContainer.add(title);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        // Data
        JTable table = UIUtils.createTable(controller.getAppointmentsTableModel());
        table.setModel(controller.getAppointmentsTableModel());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        // Search Bar:START
        controlsPanel.add(UIUtils.createLabel("Search: "));
        JTextField searchField = UIUtils.createTextField();
        searchField.setColumns(20);
        // Search Bar:END

        // Date Switcher:START
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate[] currentDate = { LocalDate.now() }; 
        Dimension rectangleSize = new Dimension(30, 42);

        JButton prevDateBtn = UIUtils.createPrimaryButton("<");
        prevDateBtn.setFocusPainted(false);
        prevDateBtn.setPreferredSize(rectangleSize);

        JTextField dateField = UIUtils.createTextField();
        dateField.setText(currentDate[0].format(formatter));
        dateField.setColumns(10);
        dateField.setHorizontalAlignment(JTextField.CENTER);
        dateField.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton nextDateBtn = UIUtils.createPrimaryButton(">");
        nextDateBtn.setFocusPainted(false);
        nextDateBtn.setPreferredSize(rectangleSize);
        // Date Switcher:END

        // Table reset event listener
        refreshAppointmentsTask = () -> {
            dateField.setText(currentDate[0].format(formatter));
            String currentSearch = searchField.getText();
            
            table.setModel(controller.getAppointmentsTableModel(currentDate[0], currentSearch));

            table.revalidate();
            table.repaint();
        };

        // Listeners
        searchField.addActionListener(e -> refreshAppointmentsTask.run());

        prevDateBtn.addActionListener(e -> {
            currentDate[0] = currentDate[0].minusDays(1);
            refreshAppointmentsTask.run();
        });

        nextDateBtn.addActionListener(e -> {
            currentDate[0] = currentDate[0].plusDays(1);
            refreshAppointmentsTask.run();
        });

        dateField.addActionListener(e -> {
            try {
                // Try to parse what the user typed
                currentDate[0] = LocalDate.parse(dateField.getText(), formatter);
                refreshAppointmentsTask.run();
            } catch (DateTimeParseException ex) {
                // If they type gibberish, show an error and revert to the last valid date
                JOptionPane.showMessageDialog(panel, "Invalid date format. Please use YYYY-MM-DD", "Date Error", JOptionPane.ERROR_MESSAGE);
                dateField.setText(currentDate[0].format(formatter));
            }
        });

        controlsPanel.add(searchField);
        controlsPanel.add(Box.createHorizontalStrut(30)); 
        controlsPanel.add(prevDateBtn);
        controlsPanel.add(dateField);
        controlsPanel.add(nextDateBtn);

        // Add the controls under the title, then put the whole thing in NORTH
        topContainer.add(controlsPanel);
        panel.add(topContainer, BorderLayout.NORTH);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Action Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton viewBtn = createCRUDButton("View");

        // Action Buttons Listener
        viewBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, 
                    "Please select an appointment from the table first.", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            String selectedId = table.getValueAt(selectedRow, 0).toString();

            try {
                Appointment selectedAppointment = controller.findAppointmentById(selectedId);
                
                if (selectedAppointment != null) {
                    User customer = controller.findCustomerById(selectedAppointment.getCustomerId());
                    User staff = controller.findStaffById(selectedAppointment.getStaffId()); 
                    
                    ViewAppointment viewPopup = new ViewAppointment(selectedAppointment, customer, staff);
                    viewPopup.completeButton.addActionListener(event -> {
                        controller.completeAppointment(selectedAppointment, viewPopup);
                        if (refreshAppointmentsTask != null) {
                            refreshAppointmentsTask.run();
                        } 
                    });

                    viewPopup.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, 
                        "Error: Could not find appointment details.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                // Catch FileCorruptedException or general exceptions from reading the files
                JOptionPane.showMessageDialog(null, 
                    "Error reading data: " + ex.getMessage(), 
                    "Data Error", 
                    JOptionPane.ERROR_MESSAGE);
            }


        });
        bottomPanel.add(viewBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

        JLabel title = displayMenuTitle("Appointment History");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        topContainer.add(title);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        // Data
        JTable table = UIUtils.createTable(controller.getHistoryAppointmentsTableModel());
        table.setModel(controller.getHistoryAppointmentsTableModel());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        // Search Bar:START
        controlsPanel.add(UIUtils.createLabel("Search: "));
        JTextField searchField = UIUtils.createTextField();
        searchField.setColumns(30);

        // Search Bar:END

        // Table reset event listener
        refreshHistoryTask = () -> {
            String currentSearch = searchField.getText();
            
            table.setModel(controller.getHistoryAppointmentsTableModel(currentSearch));

            table.revalidate();
            table.repaint();
        };

        // Listeners
        searchField.addActionListener(e -> refreshHistoryTask.run());

        controlsPanel.add(searchField);
        controlsPanel.add(Box.createHorizontalStrut(30)); 
        
        topContainer.add(controlsPanel);
        panel.add(topContainer, BorderLayout.NORTH);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Action Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton viewBtn = createCRUDButton("View");
        bottomPanel.add(viewBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    /* private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = displayMenuTitle("Service Centre Reporting & Analytics");
        panel.add(title, BorderLayout.NORTH);

        JTextArea reportArea = new JTextArea("--- Monthly Summary ---\nTotal Revenue: RM 15,400\nTotal Vehicles Serviced: 142\nMost Popular Service: Standard Oil Change");
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setMargin(new Insets(10, 10, 10, 10));
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        return panel;
    } */

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
        Color base = new Color(37, 99, 235);
        Color hover = new Color(29, 78, 216);

        button.setPreferredSize(buttonSize);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(base);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(base);
            }
        });

        return button;
    }


}