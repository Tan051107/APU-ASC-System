package ui.pages;

import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {
    public JTextField profileNameField;
    public JTextField profilePhoneField;
    public JTextField profileEmailField;
    public JButton updateButton;

    public ProfilePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("My Profile");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(UIUtils.createLabel("Current Name"));
        profileNameField = UIUtils.createTextField();
        mainPanel.add(profileNameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(UIUtils.createLabel("Current Phone Number"));
        profilePhoneField = UIUtils.createTextField();
        mainPanel.add(profilePhoneField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(UIUtils.createLabel("Current Email"));
        profileEmailField = UIUtils.createTextField();
        mainPanel.add(profileEmailField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        updateButton = UIUtils.createPrimaryButton("Update Profile");
        mainPanel.add(updateButton);

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.NORTH);
    }

}
