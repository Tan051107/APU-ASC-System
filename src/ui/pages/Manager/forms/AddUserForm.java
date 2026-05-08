package ui.pages.Manager.forms;

import models.User;
import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class AddUserForm extends JDialog {
    public JTextField nameField;
    public JTextField emailField;
    public JTextField phoneField;
    public JLabel userRoleLabel;
    public JComboBox<String> roleComboBox;
    public JLabel passwordLabel;
    public JTextField passwordField;
    public JLabel confirmPasswordLabel;
    public JTextField confirmPasswordField;
    public JButton addUserButton;
    private boolean isEdit;
    private User userToEdit;

    public User getUserToEdit() {
        return userToEdit;
    }

    public void setUserToEdit(User userToEdit) {
        this.userToEdit = userToEdit;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }


    public AddUserForm(Frame owner, boolean isEdit , User userToEdit) {
        super(owner, isEdit ? "Update User" : "Add New User", true);
        this.userToEdit = userToEdit;
        this.isEdit = isEdit;

        setSize(450, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel(isEdit ? "Update User Account" : "Create User Account");
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

        userRoleLabel = UIUtils.createLabel("User Role *");
        mainPanel.add(userRoleLabel);
        userRoleLabel.setVisible(!isEdit);
        String[] roles = {"Manager", "Counter Staff", "Technician"}; 
        roleComboBox = UIUtils.createJComboBox(roles);
        mainPanel.add(roleComboBox);
        roleComboBox.setVisible(!isEdit);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        passwordLabel = UIUtils.createLabel("Password *");
        mainPanel.add(passwordLabel);
        passwordField = UIUtils.createPasswordField();
        mainPanel.add(passwordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        confirmPasswordLabel = UIUtils.createLabel("Confirm Password *");
        mainPanel.add(confirmPasswordLabel);
        confirmPasswordField = UIUtils.createPasswordField();
        mainPanel.add(confirmPasswordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        addUserButton = UIUtils.createPrimaryButton(isEdit ? "Update User" : "Add User");
        mainPanel.add(addUserButton);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);
    }
}