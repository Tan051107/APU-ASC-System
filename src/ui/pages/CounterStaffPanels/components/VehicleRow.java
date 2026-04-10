package ui.pages.CounterStaffPanels.components;

import models.CustomerCar;
import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class VehicleRow extends JPanel {
    public JButton editBtn;
    public JButton deleteBtn;
    private final CustomerCar car;

    public CustomerCar getCar() {
        return car;
    }

    public VehicleRow(CustomerCar car) {
        this.car = car;
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        // Content
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        infoPanel.setOpaque(false);

        //For car plate display
        JLabel plateLabel = new JLabel(car.getCarPlate());
        plateLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        plateLabel.setForeground(new Color(31, 41, 55));

        //For car model display
        JLabel modelLabel = new JLabel(car.getModel());
        modelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        modelLabel.setForeground(new Color(75, 85, 99));

        //For manufacture year display
        JLabel yearLabel = new JLabel(String.valueOf(car.getManufactureYear())); // Placeholder
        yearLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        yearLabel.setForeground(new Color(156, 163, 175));

        //For mileage display
        JLabel mileageLabel = new JLabel(String.valueOf(car.getMileage()) + " km"); // Placeholder
        mileageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mileageLabel.setForeground(new Color(156, 163, 175));

        infoPanel.add(plateLabel);
        infoPanel.add(modelLabel);
        infoPanel.add(yearLabel);
        infoPanel.add(mileageLabel);
        infoPanel.add(UIUtils.createBadge(car.getFuelType().toString()));

        add(infoPanel, BorderLayout.WEST);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        actions.setOpaque(false);
        editBtn = UIUtils.createIconButton("✎", new Color(37, 99, 235));
        deleteBtn = UIUtils.createIconButton("🗑", new Color(239, 68, 68));
        actions.add(editBtn);
        actions.add(deleteBtn);

        add(actions, BorderLayout.EAST);
    }
}
