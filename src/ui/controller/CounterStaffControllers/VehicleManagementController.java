package ui.controller.CounterStaffControllers;

import exceptions.DeleteException;
import exceptions.FileCorruptedException;
import models.Customer;
import models.CustomerCar;
import services.CustomerCarService;
import ui.controller.CounterStaffControllers.FormController.AddVehicleFormController;
import ui.pages.CounterStaffPanels.components.VehicleRow;
import ui.pages.CounterStaffPanels.forms.AddVehicleForm;
import utils.DialogUtil;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VehicleManagementController {
    private final CustomerCarService customerCarService = new CustomerCarService();
    private final Runnable refreshCallback;
    Logger logger = Logger.getLogger(VehicleManagementController.class.getName());

    public VehicleManagementController(Runnable refreshCallback) {
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
        boolean confirmDelete = DialogUtil.showConfirmationMessage("Confirm Delete?" , String.format("Are you sure you want to delete %s" , car.getCarPlate()));
        if(confirmDelete){
            try {
                customerCarService.deleteCarById(car.getId());
                refreshCallback.run();
                DialogUtil.showInfoMessage("Deleted Successfully" , "Successfully deleted vehicle");
            } catch (DeleteException e) {
                DialogUtil.showErrorMessage("Failed to delete vehicle", e.getMessage());
            }
        }
    }

    public void addVehicle(Customer owner){
        try {
            int maxCarAllowed = 3;
            boolean hasReachedMaxCarAllowed = customerCarService.getCustomerCars(owner.getId()).size() >= maxCarAllowed;
            if(hasReachedMaxCarAllowed){
                DialogUtil.showWarningMessage("Error Adding Car" , "Only allowed to add a maximum of 3 cars. Please remove one of the cars to add new car");
                return;
            }
            openVehicleForm(owner,false,null);
        } catch (FileCorruptedException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
    }
}
