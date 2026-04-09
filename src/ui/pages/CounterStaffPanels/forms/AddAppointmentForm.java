package ui.pages.CounterStaffPanels.forms;

import models.Appointment;
import models.User;
import ui.pages.CounterStaffPanels.components.ComboBoxItems.CustomComboBoxItem;
import ui.pages.CounterStaffPanels.components.ComboBoxItems.ServiceComboBoxItem;
import ui.utils.UIUtils;
import javax.swing.*;
import java.awt.*;

public class AddAppointmentForm extends JDialog {
    public JComboBox<CustomComboBoxItem> customerSelectionCombo;
    public JComboBox<ServiceComboBoxItem> serviceTypeCombo;
    public JFormattedTextField dateField;
    public JFormattedTextField timeField;
    public JTextArea descriptionArea;
    public JLabel carPlateLabel;
    public JComboBox<String> carPlateSelectionCombo;
    public Component carPlateSpacing;
    public JLabel technicianLabel;
    public JComboBox<CustomComboBoxItem> technicianSelectionCombo;
    public Component technicianSpacing;
    public JButton createAppointmentBtn;
    private Appointment appointmentToEdit;
    private boolean isEdit;
    private final User loginStaff;

    public AddAppointmentForm(Frame owner, boolean isEdit , Appointment appointmentToEdit, User loginStaff) {
        super(owner, "Create New Appointment", true);
        this.loginStaff =loginStaff;
        this.appointmentToEdit = appointmentToEdit;
        this.isEdit  = isEdit;
        setSize(500, 750);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

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
        CustomComboBoxItem[] options = {new CustomComboBoxItem("" , "Select a customer")};
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
        ServiceComboBoxItem[] serviceTypes = {};
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

        //--- Available Technician ---
        technicianLabel = UIUtils.createLabel("Select Available Technician *");
        mainPanel.add(technicianLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        CustomComboBoxItem[] technicianOptions = {new CustomComboBoxItem("" , "Select a technician")}; // Placeholders
        technicianSelectionCombo = UIUtils.createJComboBox(technicianOptions);
        mainPanel.add(technicianSelectionCombo);
        technicianSpacing = Box.createRigidArea(new Dimension(0, 20));
        mainPanel.add(technicianSpacing);

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
        createAppointmentBtn = UIUtils.createPrimaryButton(isEdit ? "Update Appointment" : "Create Appointment");
        createAppointmentBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        mainPanel.add(createAppointmentBtn);

        JScrollPane mainScroll = new JScrollPane(mainPanel);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll);
    }

    public Appointment getAppointmentToEdit() {
        return appointmentToEdit;
    }

    public void setAppointmentToEdit(Appointment appointmentToEdit) {
        this.appointmentToEdit = appointmentToEdit;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public User getLoginStaff() {
        return loginStaff;
    }
}
