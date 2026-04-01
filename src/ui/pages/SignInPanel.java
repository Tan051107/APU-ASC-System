package ui.pages;

import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class SignInPanel extends JPanel {
    public JTextField emailField;
    public JTextField passwordField;
    public JButton signInButton;
    public SignInPanel(Login parent) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        add(UIUtils.createLabel("Email *"));
        add(Box.createRigidArea(new Dimension(0, 8)));
        emailField = UIUtils.createTextField("Ahmad@apu-asc.com");
        add(emailField);
        add(Box.createRigidArea(new Dimension(0, 15)));

        add(UIUtils.createLabel("Password *"));
        add(Box.createRigidArea(new Dimension(0, 8)));
        passwordField = UIUtils.createPasswordField("Enter password");
        add(passwordField);
        add(Box.createRigidArea(new Dimension(0, 25)));

        signInButton= UIUtils.createPrimaryButton("Sign In");
        add(signInButton);
        add(Box.createRigidArea(new Dimension(0, 30)));

        // Quick Demo Login
        JLabel demoLabel = new JLabel("QUICK DEMO LOGIN");
        demoLabel.setForeground(new Color(140, 140, 140));
        demoLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        demoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(demoLabel);
        add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel rolePanel = new JPanel(new GridLayout(2, 2, 6, 6));
        rolePanel.setOpaque(false);
        rolePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rolePanel.add(wrapRoleButton(createRoleButton("Manager")));
        rolePanel.add(wrapRoleButton(createRoleButton("Counter Staff")));
        rolePanel.add(wrapRoleButton(createRoleButton("Technician")));
        rolePanel.add(wrapRoleButton(createRoleButton("Customer")));
        // Last in a vertical BoxLayout: default max height is unbounded, so this panel
        // steals extra space and GridLayout spreads it between rows — huge row gap.
        Dimension rolePref = rolePanel.getPreferredSize();
        rolePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rolePref.height));
        add(rolePanel);
    }

    private JButton createRoleButton(String text) {
        JButton btn = UIUtils.createSecondaryButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        Dimension size = new Dimension(132, 26);
        btn.setPreferredSize(size);
        btn.setMinimumSize(size);
        btn.setMaximumSize(size);
        return btn;
    }

    /** GridLayout stretches children to fill cells; wrap so the button keeps a compact size. */
    private static JPanel wrapRoleButton(JButton btn) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        p.add(btn);
        return p;
    }
}
