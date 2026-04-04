package ui.pages.CounterStaffPanels;

import models.Appointment;
import ui.utils.RoundedPanel;
import ui.utils.UIUtils;
import ui.pages.CounterStaffPanels.forms.AddAppointmentForm;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManageAppointmentPanel extends JPanel {
    private JPanel rowsContainer;
    public JButton newAppointmentBtn;
    private List<Appointment> appointments;

    public ManageAppointmentPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(249, 250, 251)); // Very light gray background
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- TOP HEADER ---
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setOpaque(false);
        topHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Appointments");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(31, 41, 55));
        topHeader.add(title, BorderLayout.WEST);

        newAppointmentBtn = UIUtils.createPrimaryButton("+ New Appointment");
        newAppointmentBtn.setPreferredSize(new Dimension(180, 40));
        newAppointmentBtn.addActionListener(e -> {
            new AddAppointmentForm().setVisible(true);
        });
        topHeader.add(newAppointmentBtn, BorderLayout.EAST);

        add(topHeader, BorderLayout.NORTH);

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
        header.setBackground(new Color(249, 250, 251));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        String[] cols = {"ID", "Customer", "Vehicle", "Technician", "Type", "Date", "Time", "Duration", "Status", "Actions"};
        for (String col : cols) {
            JLabel label = new JLabel(col);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(new Color(107, 114, 128));
            header.add(label);
        }
        return header;
    }

    public void addAppointmentRow(Appointment appointment) {
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
        JLabel custLbl = new JLabel(appointment.getCustomerId());
        custLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        custLbl.setForeground(new Color(31, 41, 55));
        row.add(custLbl);

        // Vehicle
        row.add(createLabel("AJM1207"));
        // Tech
        row.add(createLabel(appointment.getTechnicianId()));
        // Type
        row.add(createLabel(appointment.getServiceId()));
        // Date
        row.add(createLabel(appointment.getDate().toString()));
        // Time
        row.add(createLabel(appointment.getTime().toString()));
        // Duration
        row.add(createLabel("3 hrs"));

        // Status Badge
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusPanel.setOpaque(false);
        statusPanel.add(createStatusBadge(appointment.getStatusService().getDisplayAppointmentStatus()));
        row.add(statusPanel);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        actions.setOpaque(false);
        JButton editBtn = UIUtils.createIconButton("✎", new Color(37, 99, 235));
        JButton deleteBtn = UIUtils.createIconButton("🗑", new Color(239, 68, 68));
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
        switch (status) {
            case "Completed":
                bg = new Color(220, 252, 231);
                fg = new Color(22, 101, 52);
                break;
            case "Assigned":
                bg = new Color(219, 234, 254);
                fg = new Color(30, 64, 175);
                break;
            case "In Progress":
                bg = new Color(254, 243, 199);
                fg = new Color(146, 64, 14);
                break;
            default:
                bg = new Color(243, 244, 246);
                fg = new Color(107, 114, 128);
        }

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
    }
}
