package ui.controller.CounterStaffControllers;

import models.Customer;
import models.CustomerCar;
import ui.pages.CounterStaffPanels.components.VehicleRow;
import ui.pages.CounterStaffPanels.forms.AddVehicleForm;
import utils.DialogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VehicleManagementController {
    private final Component parent;
    private final Runnable refreshCallback;

    public VehicleManagementController(Component parent, Runnable refreshCallback) {
        this.parent = parent;
        this.refreshCallback = refreshCallback;
    }

    public void setupVehicleRowListeners(VehicleRow row, Customer owner) {
        row.editBtn.addActionListener(e -> openVehicleForm(owner, true, row.getCar()));
        row.deleteBtn.addActionListener(e -> deleteVehicle(row.getCar()));
    }

    public void openVehicleForm(Customer owner, boolean isEdit, CustomerCar car) {
        AddVehicleForm form = new AddVehicleForm(owner, isEdit, car);
        form.setVisible(true);
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (refreshCallback != null) {
                    refreshCallback.run();
                }
            }
        });
        new AddVehicleFormController(form);
    }

    private void deleteVehicle(CustomerCar car) {
        boolean confirm = DialogUtil.showConfirmationMessage(
                "Confirm Delete?",
                String.format("Are you sure you want to delete vehicle: %s?", car.getCarPlate())
        );
        if (confirm) {
            // Logic for deleting vehicle would go here, calling a service
            JOptionPane.showMessageDialog(parent, "Vehicle delete logic not yet implemented for: " + car.getCarPlate());
            if (refreshCallback != null) {
                refreshCallback.run();
            }
        }
    }
}
