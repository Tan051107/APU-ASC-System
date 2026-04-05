package ui.pages.CounterStaffPanels;

import ui.utils.UIUtils;
import javax.swing.*;
import java.awt.*;

public class ManageCustomerPanel extends JPanel {
    public JPanel cardContainer;
    public JButton addCustomerBtn;
    public JLabel customerCountLabel;

    public ManageCustomerPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(249, 250, 251)); // Very light gray background
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- TOP HEADER ---
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setOpaque(false);
        topHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        customerCountLabel = new JLabel("0 customer");
        customerCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        customerCountLabel.setForeground(new Color(107, 114, 128));
        topHeader.add(customerCountLabel, BorderLayout.WEST);

        addCustomerBtn = UIUtils.createPrimaryButton("+ Add Customer");
        addCustomerBtn.setPreferredSize(new Dimension(150, 40));
        topHeader.add(addCustomerBtn, BorderLayout.EAST);

        add(topHeader, BorderLayout.NORTH);

        // --- CARD CONTAINER ---
        cardContainer = new JPanel();
        cardContainer.setOpaque(false);
        cardContainer.setLayout(new BoxLayout(cardContainer, BoxLayout.Y_AXIS));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(cardContainer, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }
}
