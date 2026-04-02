package ui.pages.CounterStaffPanels.components;

import exceptions.FileCorruptedException;
import models.Customer;
import models.CustomerCar;
import ui.utils.RoundedPanel;
import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerCard extends RoundedPanel {
    public JButton editCustomerBtn;
    public JButton deleteCustomerBtn;
    public JButton addVehicleBtn;

    public CustomerCard(Customer customer) {
        super(15);
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        info.setOpaque(false);

        JLabel idLabel = new JLabel(customer.getId());
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLabel.setForeground(new Color(156, 163, 175));

        JLabel nameLabel = new JLabel(customer.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(31, 41, 55));

        JLabel emailLabel = new JLabel(customer.getEmail());
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(107, 114, 128));

        info.add(idLabel);
        info.add(nameLabel);
        info.add(emailLabel);
        header.add(info, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        editCustomerBtn = UIUtils.createIconButton("✎", new Color(37, 99, 235));
        deleteCustomerBtn = UIUtils.createIconButton("🗑", new Color(239, 68, 68));
        actions.add(editCustomerBtn);
        actions.add(deleteCustomerBtn);
        header.add(actions, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // --- CONTENT (VEHICLES) ---
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Vehicle Header
        JPanel vehicleHeader = new JPanel(new BorderLayout());
        vehicleHeader.setOpaque(false);
        vehicleHeader.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(243, 244, 246)));

        addVehicleBtn = UIUtils.createLinkButton("+ Add Vehicle");
        vehicleHeader.add(addVehicleBtn, BorderLayout.EAST);
        content.add(vehicleHeader);

        try {
            List<CustomerCar> cars = customer.getCars();
            JLabel vehicleCountLabel = new JLabel(cars.size() + " vehicle(s)");
            vehicleCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            vehicleCountLabel.setForeground(new Color(107, 114, 128));
            vehicleCountLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 0));
            vehicleHeader.add(vehicleCountLabel, BorderLayout.WEST);
            // Vehicle Rows
            for (CustomerCar car : cars) {
                content.add(new VehicleRow(car));
            }
        } catch (FileCorruptedException e) {
            content.add(new JLabel("Error loading vehicles"));
        }

        add(content, BorderLayout.CENTER);
    }
}
