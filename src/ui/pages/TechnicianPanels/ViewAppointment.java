package ui.pages.TechnicianPanels;

import javax.swing.*;

import enums.AppointmentStatus;

import java.awt.*;
import models.Appointment;
import models.User;
import models.CustomerCar;
import models.Feedback;
import ui.utils.UIUtils;

public class ViewAppointment extends JDialog{
    public JTextField appointmentIdField;
    public JTextField customerField;
    public JTextField carField;
    public JTextField staffField;
    public JTextField datetimeField;
    public JTextField statusField;
    public JTextArea descriptionArea;
    public JTextArea feedbackArea;
    public JButton completeButton;

    private Appointment appointment;
    private User customer;
    private User staff;
    private CustomerCar car;
    private Feedback feedback;

    public ViewAppointment(Appointment appointment, User customer, User staff, CustomerCar car, Feedback feedback) {
        this.appointment = appointment;
        this.customer = customer;
        this.staff = staff;
        this.car = car;
        if (feedback != null){
            this.feedback = feedback;
        }

        setModal(true);
        setAlwaysOnTop(true);

        setTitle("View Appointment Details");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainContent = new JPanel(new BorderLayout(20, 20));
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Appointment Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(50, 50, 50));
        mainContent.add(title, BorderLayout.NORTH);

        JPanel centerSplitPanel = new JPanel(new GridLayout(1, 2, 30, 0));

        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.insets = new Insets(10, 10, 10, 10);

        appointmentIdField = addFormRow(leftPanel, gbcLeft, 0, "Appointment ID:");
        customerField = addFormRow(leftPanel, gbcLeft, 1, "Customer:");
        carField = addFormRow(leftPanel, gbcLeft, 2, "Car Plate:");
        staffField = addFormRow(leftPanel, gbcLeft, 3, "Staff:");
        datetimeField = addFormRow(leftPanel, gbcLeft, 4, "Date & Time:");
        statusField = addFormRow(leftPanel, gbcLeft, 5, "Status:");
        descriptionArea = addFormAreaRow(leftPanel, gbcLeft, 6, "Description:");

        gbcLeft.gridy = 6;
        gbcLeft.weighty = 1.0;
        leftPanel.add(Box.createVerticalGlue(), gbcLeft);
        centerSplitPanel.add(leftPanel);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        JLabel feedbackLabel = UIUtils.createLabel("Technician Servicing Feedback:");
        rightPanel.add(feedbackLabel, BorderLayout.NORTH);

        feedbackArea = new JTextArea();
        feedbackArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        feedbackArea.setForeground(Color.BLACK);
        feedbackArea.setBackground(Color.WHITE);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setEditable(true);
        feedbackArea.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        rightPanel.add(scrollPane, BorderLayout.CENTER);
        centerSplitPanel.add(rightPanel);
        mainContent.add(centerSplitPanel, BorderLayout.CENTER);
        // Populate fields
        if (this.appointment != null && this.customer != null && this.staff != null && this.car != null) {
            appointmentIdField.setText(appointment.getId());
            customerField.setText(appointment.getCustomerId() + "  " + customer.getName());
            carField.setText(car.getCarPlate());
            staffField.setText(appointment.getStaffId() + "  " + staff.getName());
            datetimeField.setText(appointment.getDate().toString() + " " + appointment.getTime().toString());
            statusField.setText(appointment.getStatusService().getDisplayAppointmentStatus());
            descriptionArea.setText(appointment.getDescription());
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        completeButton = UIUtils.createPrimaryButton("Mark as Completed");
        completeButton.setPreferredSize(new Dimension(250, 45));
        
        // Disable button and feedbackArea if already Completed
        if (appointment != null && AppointmentStatus.COMPLETED.equals(appointment.getStatusService())) {
            completeButton.setEnabled(false);
            feedbackArea.setEditable(false);
            feedbackArea.setBackground(new Color(250, 250, 250));
            if (this.feedback != null && this.feedback.getTechnicianFeedback() != null) {
                feedbackArea.setText(this.feedback.getTechnicianFeedback());
            }
        }
        
        bottomPanel.add(completeButton);
        mainContent.add(bottomPanel, BorderLayout.SOUTH);

        add(mainContent);
    }

    private JTextField addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.15;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(UIUtils.createLabel(labelText), gbc);

        // Constraints for the Text Field
        gbc.gridx = 1;
        gbc.weightx = 0.85;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField field = UIUtils.createTextField();
        field.setEditable(false);
        field.setFocusable(false);
        field.setHighlighter(null);
        field.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        field.setBackground(new Color(250, 250, 250)); // Slight grey to emphasize read-only
        
        panel.add(field, gbc);
        return field;
    }

    private JTextArea addFormAreaRow(JPanel panel, GridBagConstraints gbc, int row, String labelText) {
        // Constraints for the Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.15;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST; 
        panel.add(UIUtils.createLabel(labelText), gbc);

        // Constraints for the Text Area
        gbc.gridx = 1;
        gbc.weightx = 0.85;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setForeground(Color.BLACK);
        textArea.setBackground(new Color(250, 250, 250));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setHighlighter(null);
        textArea.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, gbc);
        
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weighty = 0.0;
        
        return textArea;
    }
}
