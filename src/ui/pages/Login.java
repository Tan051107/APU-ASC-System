package ui.pages;

import ui.controller.SignInController;
import ui.controller.SignUpController;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Login {

    private CardLayout cardLayout;
    private JPanel cardContainer;
    private JButton signInTab;
    private JButton signUpTab;

    public void createUI() {
        JFrame frame = new JFrame("APU - ASC");
        frame.setSize(500, 750);
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

        //logo background
        RoundedPanel logoBg = new RoundedPanel(20);
        logoBg.setBackground(new Color(37, 99, 235));
        logoBg.setLayout(new GridBagLayout());
        logoBg.setPreferredSize(new Dimension(80, 80));
        logoBg.setMaximumSize(new Dimension(80, 80));
        logoBg.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon logoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/car.png")));
        Image img = logoIcon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(img));
        logoBg.add(logo);

        JLabel appTitle = new JLabel("APU – ASC");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        appTitle.setForeground(new Color(30, 30, 30));
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Automotive Service Centre");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(logoBg);
        header.add(Box.createRigidArea(new Dimension(0, 15)));
        header.add(appTitle);
        header.add(Box.createRigidArea(new Dimension(0, 5)));
        header.add(subtitle);

        // ---------- AUTH CARD ----------
        RoundedPanel authCard = new RoundedPanel(25);
        authCard.setBackground(Color.WHITE);
        authCard.setLayout(new GridBagLayout());
        authCard.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        authCard.setPreferredSize(new Dimension(400, 580));
        authCard.setMinimumSize(new Dimension(400, 580));
        authCard.setMaximumSize(new Dimension(400, 580));
        authCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints ac = new GridBagConstraints();
        ac.gridx = 0;
        ac.gridy = 0;
        ac.weightx = 1;
        ac.weighty = 0;
        ac.fill = GridBagConstraints.HORIZONTAL;
        ac.anchor = GridBagConstraints.NORTHWEST;

        // Tab Switcher
        authCard.add(createTabSwitcher(), ac);

        // Card Content — full width so BoxLayout forms inside are not centered as a narrow column
        ac.gridy = 1;
        ac.insets = new Insets(25, 0, 0, 0);
        ac.weighty = 1;
        ac.fill = GridBagConstraints.BOTH;

        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setOpaque(false);

        SignUpPanel signUpPanel = new SignUpPanel(this);
        SignInPanel signInPanel = new SignInPanel(this);
        new SignUpController(signUpPanel);
        new SignInController(signInPanel);

        cardContainer.add(signInPanel, "SIGN_IN");
        cardContainer.add(signUpPanel, "SIGN_UP");

        JScrollPane scrollPane = new JScrollPane(cardContainer);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        authCard.add(scrollPane, ac);

        mainContainer.add(header);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 30)));
        mainContainer.add(authCard);

        background.add(mainContainer, gbc);

        frame.setContentPane(background);
        frame.setVisible(true);
    }

    private JPanel createTabSwitcher() {
        RoundedPanel container = new RoundedPanel(12);
        container.setBackground(new Color(245, 245, 245));
        container.setLayout(new GridLayout(1, 2, 5, 5));
        container.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        container.setAlignmentX(Component.LEFT_ALIGNMENT);

        signInTab = new JButton("Sign In");
        signUpTab = new JButton("Sign Up");

        styleTabButton(signInTab, true);
        styleTabButton(signUpTab, false);

        signInTab.addActionListener(e -> switchToSignIn());
        signUpTab.addActionListener(e -> switchToSignUp());

        container.add(signInTab);
        container.add(signUpTab);

        return container;
    }

    public void switchToSignIn() {
        cardLayout.show(cardContainer, "SIGN_IN");
        updateTabs(true);
    }

    public void switchToSignUp() {
        cardLayout.show(cardContainer, "SIGN_UP");
        updateTabs(false);
    }

    private void updateTabs(boolean isSignIn) {
        styleTabButton(signInTab, isSignIn);
        styleTabButton(signUpTab, !isSignIn);
    }

    private void styleTabButton(JButton btn, boolean isActive) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (isActive) {
            btn.setBackground(new Color(37, 99, 235));
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(245, 245, 245));
            btn.setForeground(new Color(150, 150, 150));
        }
    }

    // ---------- CUSTOM PANELS ----------

    class GradientPanel extends JPanel {
        public GradientPanel() {
            setBackground(new Color(245, 247, 250)); // modern light gray
        }
    }

    static class RoundedPanel extends JPanel {
        private final int radius;

        RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Card / Container
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            super.paintComponent(g);
        }
    }
}
