package ui.pages.CounterStaffPanels;

import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import models.*;
import services.CustomerCarService;
import ui.pages.CounterStaffPanels.components.ComboBoxItems.ServiceComboBoxItem;
import ui.utils.RoundedPanel;
import ui.utils.UIUtils;
import utils.DialogUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageAppointmentPanel extends JPanel {
    private final Logger logger = Logger.getLogger(ManageAppointmentPanel.class.getName());
    private final JPanel rowsContainer;
    public JButton newAppointmentBtn;
    public JButton exportBtn;
    public JTextField searchField;
    public JComboBox<ServiceComboBoxItem> serviceTypeFilterCombo;
    public JComboBox<String> statusFilterCombo;
    private List<Appointment> appointments;
    private final User loginStaff;

    public ManageAppointmentPanel(User loginStaff) {
        this.loginStaff = loginStaff;
        setLayout(new BorderLayout());
        setBackground(new Color(249, 250, 251)); // Very light gray background
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- TOP HEADER ---
        JPanel headerContainer = new JPanel();
        headerContainer.setLayout(new BoxLayout(headerContainer, BoxLayout.Y_AXIS));
        headerContainer.setOpaque(false);
        headerContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Row 1: Title and Buttons
        JPanel titleAndButtonsPanel = new JPanel(new BorderLayout());
        titleAndButtonsPanel.setOpaque(false);

        JLabel title = new JLabel("Appointments");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(31, 41, 55));
        titleAndButtonsPanel.add(title, BorderLayout.WEST);

        // Right Header: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);

        exportBtn = UIUtils.createSecondaryButton("Export Appointments");
        exportBtn.setPreferredSize(new Dimension(180, 40));
        buttonPanel.add(exportBtn);

        newAppointmentBtn = UIUtils.createPrimaryButton("+ New Appointment");
        newAppointmentBtn.setPreferredSize(new Dimension(180, 40));
        buttonPanel.add(newAppointmentBtn);

        titleAndButtonsPanel.add(buttonPanel, BorderLayout.EAST);
        headerContainer.add(titleAndButtonsPanel);

        // Row 2: Spacing and Search Field
        headerContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        searchPanel.setOpaque(false);
        JLabel searchLabel = UIUtils.createLabel("Search:");
        searchPanel.add(searchLabel);
        searchField = UIUtils.createTextField();
        searchField.setPreferredSize(new Dimension(300, 45));
        searchField.setMaximumSize(new Dimension(300, 45));
        searchField.setToolTipText("Search by ID, Technician, Customer, or Vehicle");
        searchPanel.add(searchField);

        // --- Service Type Filter ---
        JPanel serviceTypeFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        serviceTypeFilterPanel.setOpaque(false);
        JLabel serviceLabel = UIUtils.createLabel("Service Type:");
        serviceTypeFilterPanel.add(serviceLabel);
        ServiceComboBoxItem serviceComboBoxItem = new ServiceComboBoxItem("" , "All");
        ServiceComboBoxItem[] serviceOptions = {serviceComboBoxItem};
        serviceTypeFilterCombo = UIUtils.createJComboBox(serviceOptions);
        serviceTypeFilterCombo.setPreferredSize(new Dimension(180, 45));
        serviceTypeFilterPanel.add(serviceTypeFilterCombo);
        searchPanel.add(serviceTypeFilterPanel);

        // --- Status Filter ---
        JPanel statusFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        statusFilterPanel.setOpaque(false);
        JLabel statusLabel = UIUtils.createLabel("Status:");
        statusFilterPanel.add(statusLabel);
        String[] statusOptions = {"All", "Assigned", "Completed", "Cancelled"};
        statusFilterCombo = UIUtils.createJComboBox(statusOptions);
        statusFilterCombo.setPreferredSize(new Dimension(150, 45));
        statusFilterPanel.add(statusFilterCombo);
        searchPanel.add(statusFilterPanel);

        headerContainer.add(searchPanel);

        add(headerContainer, BorderLayout.NORTH);

        // --- TABLE CONTAINER ---
        RoundedPanel tableCard = new RoundedPanel(15);
        tableCard.setBackground(Color.WHITE);
        tableCard.setLayout(new BorderLayout());

        // Table Header
        JPanel tableHeader = createTableHeader();
        tableCard.add(tableHeader, BorderLayout.NORTH);

        // Rows Container
        rowsContainer = new JPanel();
        rowsContainer.setOpaque(false);
        rowsContainer.setLayout(new BoxLayout(rowsContainer, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(rowsContainer);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        tableCard.add(scrollPane, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);
    }

    private JPanel createTableHeader() {
        JPanel header = new JPanel(new GridLayout(1, 10, 10, 0));
        header.setBackground(new Color(37, 99, 235));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        String[] cols = {"ID", "Customer", "Car Plate", "Technician", "Service", "Date", "Time", "Duration (hr)", "Status", "Actions"};
        for (String col : cols) {
            JLabel label = new JLabel(col);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(new Color(255, 255, 255));
            header.add(label);
        }
        return header;
    }

    public void addAppointmentRow(Appointment appointment, Consumer<Appointment> onEdit, Consumer<Appointment> onDelete , boolean showActionButtons) {
        CustomerCarService customerCarService =new CustomerCarService();
        String serviceName = "Service";
        String serviceDuration = "0";
        String customerName = "Unknown";
        String technicianName = "Unassigned";
        try {
            Services selectedService = appointment.getService();
            if (selectedService != null) {
                serviceName = selectedService.getName();
                serviceDuration = String.valueOf(selectedService.getDuration());
            }
            
            Customer customer = appointment.getCustomer();
            if (customer != null) {
                customerName = customer.getName();
            }

            Technician technician = appointment.getTechnician();
            if (technician != null) {
                technicianName = technician.getName();
            }
        } catch (GetEntityListException | FileCorruptedException e) {
            DialogUtil.showErrorMessage("Encountered error" , "Failed to fetch appointment details");
            logger.log(Level.SEVERE , e.getMessage());
        }
        
        JPanel row = new JPanel(new GridLayout(1, 10, 10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(243, 244, 246)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // ID
        JLabel idLbl = new JLabel(appointment.getId());
        idLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLbl.setForeground(new Color(107, 114, 128));
        row.add(idLbl);

        // Customer
        JLabel customerLbl = new JLabel(customerName);
        customerLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        customerLbl.setForeground(new Color(31, 41, 55));
        row.add(customerLbl);

        // Car Plate
        try {
            CustomerCar car = appointment.getCar();
            String carPlate = (car != null) ? car.getCarPlate() : "Unknown";
            row.add(createLabel(carPlate));
        } catch (GetEntityListException e) {
            row.add(createLabel("Unknown"));
        }

        // Technician
        row.add(createLabel(technicianName));

        // Service
        row.add(createLabel(serviceName));

        // Date
        row.add(createLabel(appointment.getDate().toString()));

        // Time
        row.add(createLabel(appointment.getTime().toString()));

        // Duration
        row.add(createLabel(serviceDuration));

        // Status Badge
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusPanel.setOpaque(false);
        statusPanel.add(createStatusBadge(appointment.getStatusService().getDisplayAppointmentStatus()));
        row.add(statusPanel);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        actions.setOpaque(false);
        JButton editBtn = UIUtils.createIconButton("✎", new Color(37, 99, 235));
        JButton deleteBtn = UIUtils.createIconButton("\uD83D\uDEAB", new Color(239, 68, 68));
        editBtn.setVisible(showActionButtons);
        deleteBtn.setVisible(showActionButtons);
        
        editBtn.addActionListener(e -> onEdit.accept(appointment));
        deleteBtn.addActionListener(e -> onDelete.accept(appointment));
        
        actions.add(editBtn);
        actions.add(deleteBtn);
        row.add(actions);

        rowsContainer.add(row);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(55, 65, 81));
        return label;
    }

    private JPanel createStatusBadge(String status) {
        Color bg, fg;
        fg = switch (status) {
            case "Completed" -> {
                bg = new Color(220, 252, 231);
                yield new Color(22, 101, 52);
            }
            case "Assigned" -> {
                bg = new Color(219, 234, 254);
                yield new Color(30, 64, 175);
            }
            case "Cancelled"->{
                bg = new Color(254, 242, 242);
                yield new Color(185, 28, 28);
            }
            default -> {
                bg = new Color(243, 244, 246);
                yield new Color(107, 114, 128);
            }
        };

        RoundedPanel badge = new RoundedPanel(12);
        badge.setBackground(bg);
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 4));
        JLabel label = new JLabel(status);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(fg);
        badge.add(label);
        return badge;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
        clearAppointments();
    }

    public void clearAppointments() {
        rowsContainer.removeAll();
        rowsContainer.revalidate();
        rowsContainer.repaint();
    }

    public User getLoginStaff() {
        return loginStaff;
    }
}
