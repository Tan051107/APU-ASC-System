package ui.pages;

import javax.swing.*;
import java.awt.*;

public class NotificationPanel extends JPanel {
    public JPanel contentPanel;
    private final CardLayout bodyLayout = new CardLayout();
    private final JPanel bodyPanel = new JPanel(bodyLayout);

    public NotificationPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Notifications");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        add(title, BorderLayout.NORTH);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JPanel listWrapper = new JPanel(new BorderLayout());
        listWrapper.setOpaque(false);
        listWrapper.add(contentPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(listWrapper);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel emptyStatePanel = new JPanel(new GridBagLayout());
        emptyStatePanel.setOpaque(false);
        JLabel emptyStateLabel = new JLabel("No notifications");
        emptyStateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emptyStateLabel.setForeground(new Color(120, 120, 120));
        emptyStatePanel.add(emptyStateLabel);

        bodyPanel.setOpaque(false);
        bodyPanel.add(scrollPane, "LIST");
        bodyPanel.add(emptyStatePanel, "EMPTY");
        add(bodyPanel, BorderLayout.CENTER);

        refreshNotificationState();
    }

    public JPanel createNotificationCard(String title, String message, String createdDateTime) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(248, 250, 252));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(30, 41, 59));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea messageArea = new JTextArea(message);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageArea.setForeground(new Color(71, 85, 105));
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messageArea.setOpaque(false);
        messageArea.setFocusable(false);
        messageArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageArea.setBorder(null);

        JLabel dateTimeLabel = new JLabel("Created at: " + createdDateTime);
        dateTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateTimeLabel.setForeground(new Color(100, 116, 139));
        dateTimeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(messageArea);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(dateTimeLabel);

        return card;
    }

    public void addNotificationCard(String title, String message, String createdDateTime) {
        contentPanel.add(createNotificationCard(title, message, createdDateTime));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        refreshNotificationState();
    }

    public void clearNotifications() {
        contentPanel.removeAll();
        refreshNotificationState();
    }

    private void refreshNotificationState() {
        if (contentPanel.getComponentCount() == 0) {
            bodyLayout.show(bodyPanel, "EMPTY");
        } else {
            bodyLayout.show(bodyPanel, "LIST");
        }
        revalidate();
        repaint();
    }
}
