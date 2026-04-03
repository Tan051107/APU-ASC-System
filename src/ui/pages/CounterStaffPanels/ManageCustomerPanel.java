package ui.pages.CounterStaffPanels;

import models.Customer;
import ui.utils.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManageCustomerPanel extends JPanel {
    public JPanel cardContainer;
    public JButton addCustomerBtn;
    public JButton exportBtn;
    public JTextField searchField;
    public JLabel customerCountLabel;
    private List<Customer> customers;

    public ManageCustomerPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(249, 250, 251)); // Very light gray background
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- TOP HEADER ---
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setOpaque(false);
        topHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Left: Count and Search
        JPanel leftHeader = new JPanel();
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        leftHeader.setOpaque(false);

        customerCountLabel = new JLabel("0 customer");
        customerCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        customerCountLabel.setForeground(new Color(107, 114, 128));
        customerCountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftHeader.add(customerCountLabel);

        leftHeader.add(Box.createRigidArea(new Dimension(0, 10)));

        searchField = UIUtils.createTextField();
        searchField.setPreferredSize(new Dimension(250, 45));
        searchField.setMaximumSize(new Dimension(250, 45));
        searchField.setAlignmentX(Component.LEFT_ALIGNMENT);
        //Search field placeholder
        searchField.setText("Search by name or email");
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search by name or email")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search by name or email...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        leftHeader.add(searchField);

        topHeader.add(leftHeader, BorderLayout.WEST);

        // Right: Buttons
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightHeader.setOpaque(false);

        exportBtn = UIUtils.createSecondaryButton("Export Data");
        exportBtn.setPreferredSize(new Dimension(120, 40));
        rightHeader.add(exportBtn);

        addCustomerBtn = UIUtils.createPrimaryButton("+ Add Customer");
        addCustomerBtn.setPreferredSize(new Dimension(150, 40));
        rightHeader.add(addCustomerBtn);

        topHeader.add(rightHeader, BorderLayout.EAST);

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

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
