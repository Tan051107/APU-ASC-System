package ui.pages;

import dto.AddCarDto;
import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SignUpPanel extends JPanel {
    public JComboBox<String> userTypeSelectionComboBox;
    // Remove individual car field references
    public List<CarEntryFields> carEntries = new ArrayList<>();
    public JTextField nameField;
    public JTextField emailField;
    public JTextField phoneField;
    public JTextField passwordField;
    public JTextField confirmPasswordField;
    public JButton completeSignUpButton;
    public JButton addCarButton;
    public JPanel carFieldsContainer;

    public SignUpPanel(Login parent) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(title);
        add(createLeftAlignedRigidArea(0, 20));


        add(UIUtils.createLabel("Full Name *"));
        nameField = UIUtils.createTextField("Enter your full name");
        add(nameField);
        add(createLeftAlignedRigidArea(0, 15));

        add(UIUtils.createLabel("Email Address *"));
        emailField = UIUtils.createTextField("Enter your email");
        add(emailField);
        add(createLeftAlignedRigidArea(0, 15));

        add(UIUtils.createLabel("Phone Number *"));
        phoneField = UIUtils.createTextField("Enter your phone number");
        add(phoneField);
        add(createLeftAlignedRigidArea(0, 15));

        add(UIUtils.createLabel("User Type*"));
        String [] userTypes = {"Manager" , "Counter Staff" , "Technicians" , "Customer"};
        userTypeSelectionComboBox = UIUtils.createJComboBox(userTypes , "Select user type");
        add(userTypeSelectionComboBox);
        add(createLeftAlignedRigidArea(0, 15));

        // Container for Car Fields
        carFieldsContainer = new JPanel();
        carFieldsContainer.setOpaque(false);
        carFieldsContainer.setLayout(new BoxLayout(carFieldsContainer, BoxLayout.Y_AXIS));
        carFieldsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        carFieldsContainer.setVisible(false); // Initially hidden
        add(carFieldsContainer);

        // First Car Entry (Always present inside the container)
        addCarEntry();

        addCarButton = UIUtils.createSecondaryButton("+ Add Another Car");
        addCarButton.setMaximumSize(new Dimension(150, 30));
        addCarButton.setVisible(false); // Only visible for Customers
        add(addCarButton);
        add(createLeftAlignedRigidArea(0, 15));

        addCarButton.addActionListener(e -> {
            if (carFieldsContainer.getComponentCount() < 3) {
                addCarEntry();
                updateAddCarButtonVisibility();
                revalidate();
                repaint();
            }
        });

        add(UIUtils.createLabel("Password *"));
        passwordField = UIUtils.createPasswordField("Min.8 characters");
        add(passwordField);
        add(createLeftAlignedRigidArea(0, 25));

        add(UIUtils.createLabel("Confirm Password*"));
        confirmPasswordField = UIUtils.createPasswordField("Re-enter password");
        add(confirmPasswordField);
        add(createLeftAlignedRigidArea(0, 25));

        completeSignUpButton = UIUtils.createPrimaryButton("Create Account");
        add(completeSignUpButton);
        add(createLeftAlignedRigidArea(0, 20));

        // Link back to Sign In
        JPanel backPanel = getJPanel();
        add(backPanel);
    }

    private static JPanel getJPanel() {
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

        backPanel.add(backText);
        backPanel.add(signInLink);
        return backPanel;
    }

    public void updateAddCarButtonVisibility() {
        int selectedUserType = userTypeSelectionComboBox.getSelectedIndex();
        boolean isCustomer = selectedUserType == 4;
        addCarButton.setVisible(isCustomer && carFieldsContainer.getComponentCount() < 3);
    }

    private void addCarEntry() {
        int carIndex = carFieldsContainer.getComponentCount() + 1;
        JPanel entryPanel = new JPanel();
        entryPanel.setOpaque(false);
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
        entryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Header with title and remove button
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel carLabel = UIUtils.createLabel("Car " + carIndex);
        carLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.add(carLabel, BorderLayout.WEST);

        CarEntryFields fields = new CarEntryFields();

        if (carIndex > 1) {
            JLabel removeLink = new JLabel("Remove");
            removeLink.setForeground(Color.RED);
            removeLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            removeLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
            removeLink.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    carFieldsContainer.remove(entryPanel);
                    carEntries.remove(fields);
                    updateCarNumbers();
                    updateAddCarButtonVisibility();
                    revalidate();
                    repaint();
                }
            });
            header.add(removeLink, BorderLayout.EAST);
        }
        
        entryPanel.add(header);
        entryPanel.add(createLeftAlignedRigidArea(0, 10));

        entryPanel.add(UIUtils.createLabel("Car Model *"));
        fields.carModelField = UIUtils.createTextField("Enter car model");
        entryPanel.add(fields.carModelField);
        entryPanel.add(createLeftAlignedRigidArea(0, 10));

        entryPanel.add(UIUtils.createLabel("Car Plate *"));
        fields.carPlateField = UIUtils.createTextField("Enter car plate");
        entryPanel.add(fields.carPlateField);
        entryPanel.add(createLeftAlignedRigidArea(0, 15));

        carFieldsContainer.add(entryPanel);
        carEntries.add(fields);
    }

    public List<AddCarDto> getCarData() {
        List<AddCarDto> data = new ArrayList<>();
        for (CarEntryFields entry : carEntries) {
            String model = UIUtils.getActualText(entry.carModelField, "Enter car model").trim();
            String plate = UIUtils.getActualText(entry.carPlateField, "Enter car plate").trim();
            data.add(new AddCarDto(model, plate));
        }
        return data;
    }

    public static class CarEntryFields {
        public JTextField carModelField;
        public JTextField carPlateField;
    }

    private Component createLeftAlignedRigidArea(int width, int height) {
        Component area = Box.createRigidArea(new Dimension(width, height));
        if (area instanceof JComponent) {
            ((JComponent) area).setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        return area;
    }

    private void updateCarNumbers() {
        Component[] components = carFieldsContainer.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JPanel entry) {
                Component header = entry.getComponent(0);
                if (header instanceof JPanel) {
                    Component label = ((JPanel) header).getComponent(0);
                    if (label instanceof JLabel) {
                        ((JLabel) label).setText("Car " + (i + 1));
                    }
                }
            }
        }
    }
}
