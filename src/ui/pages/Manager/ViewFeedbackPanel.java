package ui.pages.Manager;

import models.Feedback;
import models.User;
import models.Appointment;
import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class ViewFeedbackPanel extends JFrame{
    public JLabel feedbackId = new JLabel("-");
    public JLabel appointmentId = new JLabel("-");
    public JLabel technicianId = new JLabel("-");
    public JLabel technicianName = new JLabel("-");
    public JTextArea technicianFeedback = new JTextArea(3, 10);
    public JLabel staffId = new JLabel("-");
    public JLabel staffName = new JLabel("-");
    public JLabel customerId = new JLabel("-");
    public JLabel customerName = new JLabel("-");
    public JTextArea comment = new JTextArea(3, 10);
    public JLabel technicianRating = new JLabel("-");
    public JLabel staffRating = new JLabel("-");
    public JButton closeButton;

    public ViewFeedbackPanel() {
        setTitle("View Feedback Details");
        setSize(450, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Make text areas look clean and un-editable
        technicianFeedback.setEditable(false);

        comment.setEditable(false);
        

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("View Feedback Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(UIUtils.createLabel("Feedback ID *"));
        mainPanel.add(feedbackId);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Appointment ID *"));
        mainPanel.add(appointmentId);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Technician ID *"));
        mainPanel.add(technicianId);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Technician Name *"));
        mainPanel.add(technicianName);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Technician Feedback *"));
        mainPanel.add(technicianFeedback);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Staff ID *"));
        mainPanel.add(staffId);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Staff Name *"));
        mainPanel.add(staffName);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Customer ID *"));
        mainPanel.add(customerId);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Customer Name *"));
        mainPanel.add(customerName);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Customer Comment *"));
        mainPanel.add(comment);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Technician Rating *"));
        mainPanel.add(technicianRating);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Staff Rating *"));
        mainPanel.add(staffRating);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        closeButton = UIUtils.createPrimaryButton("Close");
        mainPanel.add(closeButton);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);
    }
}
