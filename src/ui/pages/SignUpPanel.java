package ui.pages;

import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class SignUpPanel extends JPanel {
    public JComboBox<String> userTypeSelectionComboBox;
    public JLabel carModelLabel;
    public JTextField carModelField;
    public Component carModelBottomSpacing;
    public JLabel carPlateLabel;
    public JTextField carPlateField;
    public Component carPlateBottomSpacing;
    public JTextField nameField;
    public JTextField emailField;
    public JTextField phoneField;
    public JTextField passwordField;
    public JTextField confirmPasswordField;
    public JButton completeSignUpButton;
    public SignUpPanel(Login parent) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(title);
        add(Box.createRigidArea(new Dimension(0, 20)));


        add(UIUtils.createLabel("Full Name *"));
        nameField = UIUtils.createTextField("Enter your full name");
        add(nameField);
        add(Box.createRigidArea(new Dimension(0, 15)));

        add(UIUtils.createLabel("Email Address *"));
        emailField = UIUtils.createTextField("Enter your email");
        add(emailField);
        add(Box.createRigidArea(new Dimension(0, 15)));

        add(UIUtils.createLabel("Phone Number *"));
        phoneField = UIUtils.createTextField("Enter your phone number");
        add(phoneField);
        add(Box.createRigidArea(new Dimension(0, 15)));

        add(UIUtils.createLabel("User Type*"));
        String [] userTypes = {"Manager" , "Counter Staff" , "Technicians" , "Customer"};
        userTypeSelectionComboBox = UIUtils.createJComboBox(userTypes , "Select user type");
        add(userTypeSelectionComboBox);
        add(Box.createRigidArea(new Dimension(0, 15)));

        carModelLabel = UIUtils.createLabel("Car Model *");
        carModelField = UIUtils.createTextField("Honda Civic");
        carModelBottomSpacing = Box.createRigidArea(new Dimension(0, 15));
        add(carModelLabel);
        add(carModelField);
        add(carModelBottomSpacing);
        carModelLabel.setVisible(false);
        carModelField.setVisible(false);
        carModelBottomSpacing.setVisible(false);

        carPlateLabel = UIUtils.createLabel("Car Plate *");
        carPlateField = UIUtils.createTextField("ABC 4728");
        carPlateBottomSpacing = Box.createRigidArea(new Dimension(0, 15));
        add(carPlateLabel);
        add(carPlateField);
        add(carPlateBottomSpacing);
        carPlateLabel.setVisible(false);
        carPlateField.setVisible(false);
        carPlateBottomSpacing.setVisible(false);

        add(UIUtils.createLabel("Password *"));
        passwordField = UIUtils.createPasswordField("Min.8 characters");
        add(passwordField);
        add(Box.createRigidArea(new Dimension(0, 25)));

        add(UIUtils.createLabel("Confirm Password*"));
        confirmPasswordField = UIUtils.createPasswordField("Re-enter password");
        add(confirmPasswordField);
        add(Box.createRigidArea(new Dimension(0, 25)));

        completeSignUpButton = UIUtils.createPrimaryButton("Create Account");
        add(completeSignUpButton);
        add(Box.createRigidArea(new Dimension(0, 20)));

        // Link back to Sign In
        JPanel backPanel = getJPanel(parent);
        add(backPanel);
    }

    private static JPanel getJPanel(Login parent) {
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        backPanel.setOpaque(false);
        backPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        backPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JLabel backText = new JLabel("Already have an account? ");
        backText.setForeground(new Color(150, 150, 150));
        backText.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel signInLink = new JLabel("Sign In");
        signInLink.setForeground(new Color(37, 99, 235));
        signInLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        signInLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signInLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                parent.switchToSignIn();
            }
        });

        backPanel.add(backText);
        backPanel.add(signInLink);
        return backPanel;
    }
}
