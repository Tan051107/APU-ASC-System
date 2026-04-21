package ui.pages.Manager;

import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class ViewFeedbackPanel extends JDialog {
    
    // 1. Changed all JLabel declarations to JTextField
    public JTextField feedbackId = new JTextField("-");
    public JTextField appointmentId = new JTextField("-");
    public JTextField technicianId = new JTextField("-");
    public JTextField technicianName = new JTextField("-");
    public JTextField technicianFeedback = new JTextField("-");;
    //public JTextArea technicianFeedback = new JTextArea(3, 10);
    public JTextField staffId = new JTextField("-");
    public JTextField staffName = new JTextField("-");
    public JTextField customerId = new JTextField("-");
    public JTextField customerName = new JTextField("-");
    public JTextField comment = new JTextField("-");
    //public JTextArea comment = new JTextArea(3, 10);
    public JTextField technicianRating = new JTextField("-");
    public JTextField staffRating = new JTextField("-");
    public JButton closeButton;

    public ViewFeedbackPanel(JFrame parent) {
        super(parent, "View Feedback Details", true);

        setTitle("View Feedback Details");
        setSize(450, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 2. Make all text areas and text fields un-editable
        technicianFeedback.setEditable(false);
        comment.setEditable(false);
        feedbackId.setEditable(false);
        appointmentId.setEditable(false);
        technicianId.setEditable(false);
        technicianName.setEditable(false);
        staffId.setEditable(false);
        staffName.setEditable(false);
        customerId.setEditable(false);
        customerName.setEditable(false);
        technicianRating.setEditable(false);
        staffRating.setEditable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("View Feedback Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(UIUtils.createLabel("Feedback ID *"));
        feedbackId = UIUtils.createTextField();
        mainPanel.add(feedbackId);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Appointment ID *"));
        appointmentId = UIUtils.createTextField();
        mainPanel.add(appointmentId);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Technician ID *"));
        technicianId = UIUtils.createTextField();
        mainPanel.add(technicianId);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Technician Name *"));
        technicianName = UIUtils.createTextField();
        mainPanel.add(technicianName);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Technician Feedback *"));
        technicianFeedback = UIUtils.createTextField();
        mainPanel.add(technicianFeedback);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Staff ID *"));
        staffId = UIUtils.createTextField();
        mainPanel.add(staffId);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Staff Name *"));
        staffName = UIUtils.createTextField();
        mainPanel.add(staffName);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Customer ID *"));
        customerId = UIUtils.createTextField();
        mainPanel.add(customerId);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Customer Name *"));
        customerName = UIUtils.createTextField();
        mainPanel.add(customerName);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Customer Comment *"));
        comment = UIUtils.createTextField();
        mainPanel.add(comment);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Technician Rating *"));
        technicianRating = UIUtils.createTextField();
        mainPanel.add(technicianRating);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Staff Rating *"));
        staffRating = UIUtils.createTextField();
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