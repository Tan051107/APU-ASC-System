package ui.pages.TechnicianPanels;

import javax.swing.*;

import enums.AppointmentStatus;

import java.awt.*;
import models.Appointment;
import models.User;
import ui.utils.UIUtils;

public class ViewAppointment extends JDialog{
    public JTextField appointmentIdField;
    public JTextField customerField;
    public JTextField carField;
    public JTextField staffField;
    public JTextField datetimeField;
    public JTextField statusField;
    public JTextArea descriptionArea;
    public JButton completeButton;

    private Appointment appointment;
    private User customer;
    private User staff;

    public ViewAppointment(Appointment appointment, User customer, User staff) {
        this.appointment = appointment;
        this.customer = customer;
        this.staff = staff;

        setModal(true);
        setAlwaysOnTop(true);

        setTitle("View Appointment Details");
        setSize(450, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Appointment Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        appointmentIdField = addReadOnlyField(mainPanel, "Appointment ID:");
        customerField = addReadOnlyField(mainPanel, "Customer:");
        carField = addReadOnlyField(mainPanel, "Car:");
        staffField = addReadOnlyField(mainPanel, "Counter Staff:");
        datetimeField = addReadOnlyField(mainPanel, "Date/Time:");
        statusField = addReadOnlyField(mainPanel, "Status:");
        descriptionArea = addReadOnlyArea(mainPanel, "Description:");

        // Populate field
        if (this.appointment != null && this.customer != null && this.staff != null) {
            appointmentIdField.setText(appointment.getId());
            customerField.setText(appointment.getCustomerId()+" | "+customer.getName());
            carField.setText(appointment.getCarId()+" | ");
            staffField.setText(appointment.getStaffId()+" | "+staff.getName());
            datetimeField.setText(appointment.getDate().toString()+" "+appointment.getTime().toString());
            statusField.setText(appointment.getStatusService().getDisplayAppointmentStatus());
            descriptionArea.setText(appointment.getDescription());
        }

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        completeButton = UIUtils.createPrimaryButton("Mark as COMPLETED");
        
        // Disable button if already Completed
        if (appointment != null && AppointmentStatus.COMPLETED.equals(appointment.getStatusService())) {
            completeButton.setEnabled(false);
        }
        
        mainPanel.add(completeButton);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane);
    }

    private JTextField addReadOnlyField(JPanel panel, String labelText) {
        panel.add(UIUtils.createLabel(labelText));
        JTextField field = UIUtils.createTextField();
        field.setEditable(false);
        field.setFocusable(false);
        field.setHighlighter(null);
        field.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        return field;
    }

    private JTextArea addReadOnlyArea(JPanel panel, String labelText) {
        panel.add(UIUtils.createLabel(labelText));

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setForeground(Color.BLACK);
        textArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        textArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setHighlighter(null);
        textArea.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        textArea.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
        
        panel.add(textArea);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        return textArea;
    }
}
