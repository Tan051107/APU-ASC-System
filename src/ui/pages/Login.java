package ui.pages;

import ui.controller.SignInController;
import ui.utils.RoundedPanel;
import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Login extends Component {

    public JTextField emailField;
    public JPasswordField passwordField;
    public JButton signInButton;
    public JButton demoManagerButton;
    public JButton demoCustomerButton;
    public JButton demoTechnicianButton;
    public JButton demoCounterStaffButton;
    private CardLayout authCardLayout;
    private JPanel authCardContent;
    private JFrame frame;
    public JPasswordField forgotPasswordField;
    public JTextField forgotEmailField;
    public JButton resetPasswordButton;

    public void createUI() {
        frame = new JFrame("APU - ASC");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        GradientPanel background = new GradientPanel();
        background.setLayout(new GridBagLayout());
        background.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        // Container for everything to keep it centered
        JPanel mainContainer = new JPanel();
        mainContainer.setOpaque(false);
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        // ---------- HEADER ----------
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        //logo
        ImageIcon logoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/logo.png")));
        Image img = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(img));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        

//        JLabel appTitle = new JLabel("APU – ASC");
//        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
//        appTitle.setForeground(new Color(30, 30, 30));
//        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        JLabel subtitle = new JLabel("Automotive Service Centre");
//        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        subtitle.setForeground(new Color(100, 100, 100));
//        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
//
          header.add(logo);
//        header.add(Box.createRigidArea(new Dimension(0, 15)));
//        header.add(appTitle);
//        header.add(Box.createRigidArea(new Dimension(0, 5)));
//        header.add(subtitle);

        // ---------- AUTH CARD ----------
        RoundedPanel authCard = new RoundedPanel(25);
        authCard.setBackground(Color.WHITE);
        authCard.setLayout(new GridBagLayout());
        authCard.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        authCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints ac = new GridBagConstraints();
        ac.gridx = 0;
        ac.gridy = 0;
        ac.weightx = 1;
        ac.weighty = 1;
        ac.fill = GridBagConstraints.BOTH;
        ac.anchor = GridBagConstraints.NORTHWEST;

        authCardLayout = new CardLayout();
        authCardContent = new JPanel(authCardLayout);
        authCardContent.setOpaque(false);

        JPanel signInPanel = createSignInPanel();
        JPanel forgotPasswordPanel = createForgotPasswordPanel();
        new SignInController(this);

        authCardContent.add(signInPanel, "SIGN_IN");
        authCardContent.add(forgotPasswordPanel, "FORGOT_PASSWORD");
        authCard.add(authCardContent, ac);

        mainContainer.add(header);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContainer.add(authCard);

        background.add(mainContainer, gbc);

        frame.setContentPane(background);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void dispose() {
        if (frame != null) {
            frame.dispose();
        }
    }

    private JPanel createSignInPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(UIUtils.createLabel("Email *"));
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        emailField = UIUtils.createTextField();
        panel.add(emailField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        panel.add(UIUtils.createLabel("Password *"));
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        passwordField = UIUtils.createPasswordField();
        panel.add(passwordField);
        panel.add(createShowPasswordCheckBox(passwordField));
        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel forgotPasswordLabel = new JLabel("Forgot Password?");
        forgotPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPasswordLabel.setForeground(new Color(37, 99, 235));
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showForgotPasswordPanel();
            }
        });
        JPanel forgotWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        forgotWrapper.setOpaque(false);
        forgotWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        forgotWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        forgotWrapper.add(forgotPasswordLabel);
        panel.add(forgotWrapper);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        signInButton = UIUtils.createPrimaryButton("Sign In");
        panel.add(signInButton);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Quick Demo Login
        JLabel demoLabel = new JLabel("QUICK DEMO LOGIN");
        demoLabel.setForeground(new Color(140, 140, 140));
        demoLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        demoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(demoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel rolePanel = new JPanel(new GridLayout(2, 2, 6, 6));
        rolePanel.setOpaque(false);
        rolePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        demoManagerButton = createRoleButton("Manager");
        rolePanel.add(wrapRoleButton(demoManagerButton));
        demoCounterStaffButton = createRoleButton("Counter Staff");
        rolePanel.add(wrapRoleButton(demoCounterStaffButton));
        demoCustomerButton = createRoleButton("Customer");
        rolePanel.add(wrapRoleButton(demoCustomerButton));
        demoTechnicianButton = createRoleButton("Technician");
        rolePanel.add(wrapRoleButton(demoTechnicianButton));
        Dimension rolePref = rolePanel.getPreferredSize();
        rolePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rolePref.height));
        panel.add(rolePanel);

        panel.setPreferredSize(new Dimension(350, panel.getPreferredSize().height));
        return panel;
    }

    private JPanel createForgotPasswordPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Forgot Password");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        JLabel subtitle = new JLabel("Enter your email and new password to reset password.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(120, 120, 120));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(subtitle);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        panel.add(UIUtils.createLabel("Email *"));
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        forgotEmailField = UIUtils.createTextField();
        panel.add(forgotEmailField);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        panel.add(UIUtils.createLabel("New Password *"));
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        forgotPasswordField = UIUtils.createPasswordField();
        panel.add(forgotPasswordField);
        panel.add(createShowPasswordCheckBox(forgotPasswordField));
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        resetPasswordButton = UIUtils.createPrimaryButton("Reset Password");
        panel.add(resetPasswordButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel backToSignInLabel = new JLabel("Back to Sign In");
        backToSignInLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backToSignInLabel.setForeground(new Color(37, 99, 235));
        backToSignInLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToSignInLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        backToSignInLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showSignInPanel();
            }
        });
        panel.add(backToSignInLabel);

        panel.setPreferredSize(new Dimension(350, panel.getPreferredSize().height));
        return panel;
    }

    private void showForgotPasswordPanel() {
        authCardLayout.show(authCardContent, "FORGOT_PASSWORD");
    }

    public void showSignInPanel() {
        authCardLayout.show(authCardContent, "SIGN_IN");
    }

    private JCheckBox createShowPasswordCheckBox(JPasswordField passwordInput) {
        JCheckBox checkBox = new JCheckBox("Show password");
        checkBox.setOpaque(false);
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        final char defaultEchoChar = passwordInput.getEchoChar();
        checkBox.addActionListener(e ->
                passwordInput.setEchoChar(checkBox.isSelected() ? (char) 0 : defaultEchoChar)
        );
        return checkBox;
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

    private static JPanel wrapRoleButton(JButton btn) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        p.add(btn);
        return p;
    }

    // ---------- CUSTOM PANELS ----------

    class GradientPanel extends JPanel {
        public GradientPanel() {
            setBackground(new Color(245, 247, 250)); // modern light gray
        }
    }
}
