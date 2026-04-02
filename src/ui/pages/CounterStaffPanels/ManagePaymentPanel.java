package ui.pages.CounterStaffPanels;

import ui.utils.UIUtils;
import javax.swing.*;
import java.awt.*;

public class ManagePaymentPanel extends JPanel {
    public ManagePaymentPanel() {
        setLayout(new BorderLayout());
        JLabel title = UIUtils.createMenuTitle("Customer Payment Management");
        add(title, BorderLayout.NORTH);

        // Placeholder for JTable
        String[][] data = {{"F001", "John Doe", "Car Kaput"}, {"F002", "Jane Smith", "HP increase"}};
        String[] columns = {"Feedback ID", "Customer Name", "Feedback Type"};
        JTable table = new JTable(data, columns);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton viewDetails = UIUtils.createCRUDButton("View Details");
        bottomPanel.add(viewDetails);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
