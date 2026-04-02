package ui.pages.CounterStaffPanels.forms;

import models.Customer;
import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class AddCustomerForm extends JFrame {
    public JTextField nameField;
    public JTextField emailField;
    public JTextField phoneField;
    public JLabel passwordLabel;
    public JTextField passwordField;
    public JLabel confirmPasswordLabel;
    public JTextField confirmPasswordField;
    public JButton addCustomerButton;
    private boolean isEdit;
    private Customer customerToEdit;

    public Customer getCustomerToEdit() {
        return customerToEdit;
    }

    public void setCustomerToEdit(Customer customerToEdit) {
        this.customerToEdit = customerToEdit;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }


    public AddCustomerForm(boolean isEdit , Customer customerToEdit) {
        this.customerToEdit = customerToEdit;
        this.isEdit = isEdit;

        setTitle(isEdit ? "Update Customer" : "Add New Customer");
        setSize(450, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel(isEdit ? "Update Customer Account" : "Create Customer Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(UIUtils.createLabel("Full Name *"));
        nameField = UIUtils.createTextField();
        mainPanel.add(nameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Email Address *"));
        emailField = UIUtils.createTextField();
        mainPanel.add(emailField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Phone Number *"));
        phoneField = UIUtils.createTextField();
        mainPanel.add(phoneField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        passwordLabel = UIUtils.createLabel("Password *");
        mainPanel.add(passwordLabel);
        passwordLabel.setVisible(!isEdit);
        passwordField = UIUtils.createPasswordField();
        mainPanel.add(passwordField);
        passwordField.setVisible(!isEdit);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        confirmPasswordLabel = UIUtils.createLabel("Confirm Password *");
        mainPanel.add(confirmPasswordLabel);
        confirmPasswordLabel.setVisible(!isEdit);
        confirmPasswordField = UIUtils.createPasswordField();
        mainPanel.add(confirmPasswordField);
        confirmPasswordField.setVisible(!isEdit);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        addCustomerButton = UIUtils.createPrimaryButton(isEdit ? "Update Customer" : "Add Customer");
        mainPanel.add(addCustomerButton);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);
    }
}
