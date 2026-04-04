package ui.pages.CounterStaffPanels.forms;

import ui.utils.UIUtils;
import javax.swing.*;
import java.awt.*;

public class AddAppointmentForm extends JFrame {
    public JComboBox<String> customerSelectionCombo;
    public JComboBox<String> serviceTypeCombo;
    public JFormattedTextField dateField;
    public JFormattedTextField timeField;
    public JTextArea descriptionArea;
    public JLabel carPlateLabel;
    public JComboBox<String> carPlateSelectionCombo;
    public Component carPlateSpacing;
    public JButton createAppointmentBtn;

    public AddAppointmentForm() {
        setTitle("Create New Appointment");
        setSize(500, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Appointment Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // --- Customer Selection ---
        mainPanel.add(UIUtils.createLabel("Select Customer *"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));// Placeholders
        String [] options = {"Select Customer"};
        customerSelectionCombo = UIUtils.createJComboBox(options);
        mainPanel.add(customerSelectionCombo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Car Plate Selection ---
        carPlateLabel = UIUtils.createLabel("Select Car Plate *");
        mainPanel.add(carPlateLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        String[] carPlateOptions = {}; // Placeholders
        carPlateSelectionCombo = UIUtils.createJComboBox(carPlateOptions);
        mainPanel.add(carPlateSelectionCombo);
        carPlateSpacing = Box.createRigidArea(new Dimension(0, 20));
        mainPanel.add(carPlateSpacing);

        // --- Service Type ---
        mainPanel.add(UIUtils.createLabel("Service Type *"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        String[] serviceTypes = {"Normal", "Major"};
        serviceTypeCombo = UIUtils.createJComboBox(serviceTypes);
        mainPanel.add(serviceTypeCombo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Date ---
        mainPanel.add(UIUtils.createLabel("Date (YYYY-MM-DD) *"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        dateField = UIUtils.createDateField();
        mainPanel.add(dateField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Time ---
        mainPanel.add(UIUtils.createLabel("Time (HH:MM) *"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        timeField = UIUtils.createTimeField();
        mainPanel.add(timeField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Description ---
        mainPanel.add(UIUtils.createLabel("Description / Notes"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        mainPanel.add(descScroll);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- Submit Button ---
        createAppointmentBtn = UIUtils.createPrimaryButton("Create Appointment");
        createAppointmentBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        mainPanel.add(createAppointmentBtn);

        JScrollPane mainScroll = new JScrollPane(mainPanel);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll);
    }
}
