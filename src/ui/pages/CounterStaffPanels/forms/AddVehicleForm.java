package ui.pages.CounterStaffPanels.forms;

import models.Customer;
import models.CustomerCar;
import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class AddVehicleForm extends JDialog {
    public JTextField plateField;
    public JTextField brandField;
    public JTextField modelField;
    public JTextField yearField;
    public JTextField mileageField;
    public JComboBox<String> fuelTypeCombo;
    public JButton addVehicleButton;
    private final Customer customer;
    private final boolean isEdit;
    private final CustomerCar customerCar;


    public AddVehicleForm(Frame owner, Customer customer , boolean isEdit , CustomerCar customerCar) {
        super(owner, (isEdit ? "Update Vehicle for " : "Add New Vehicle for ") + customer.getName(), true);
        this.customer = customer;
        this.isEdit = isEdit;
        this.customerCar = customerCar;
        setSize(450, 600);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Vehicle Details");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(UIUtils.createLabel("Car Plate *"));
        plateField = UIUtils.createTextField();
        mainPanel.add(plateField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Brand *"));
        brandField = UIUtils.createTextField();
        mainPanel.add(brandField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Model *"));
        modelField = UIUtils.createTextField();
        mainPanel.add(modelField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Manufacture Year *"));
        yearField = UIUtils.createTextField();
        mainPanel.add(yearField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Mileage (km) *"));
        mileageField = UIUtils.createTextField();
        mainPanel.add(mileageField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Fuel Type *"));
        String[] fuelTypes = {"Petrol", "Diesel", "Electric", "Hybrid", "Plug-in Hybrid"};
        fuelTypeCombo = new JComboBox<>(fuelTypes);
        fuelTypeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        fuelTypeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(fuelTypeCombo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        addVehicleButton = UIUtils.createPrimaryButton(isEdit ? "Update Vehicle" :" Add Vehicle");
        mainPanel.add(addVehicleButton);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public CustomerCar getCustomerCar() {
        return customerCar;
    }
}
