package ui.pages.CounterStaffPanels;

import ui.utils.UIUtils;
import javax.swing.*;
import java.awt.*;

public class ManageAppointmentPanel extends JPanel {
    public ManageAppointmentPanel() {
        setLayout(new BorderLayout());
        JLabel title = UIUtils.createMenuTitle("Customer Appointment Management");
        add(title, BorderLayout.NORTH);

        String[][] data = {{"Standard Oil Change", "RM 120.00"}, {"Tire Rotation", "RM 40.00"}};
        String[] columns = {"Service Type", "Current Price"};
        JTable table = new JTable(data, columns);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton updatePrice = UIUtils.createCRUDButton("Update Price");
        bottomPanel.add(updatePrice);
        add(bottomPanel, BorderLayout.SOUTH);
    }

}
