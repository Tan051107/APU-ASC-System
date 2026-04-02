package ui.controller.CounterStaffControllers;

import exceptions.DeleteException;
import exceptions.FileCorruptedException;
import models.Customer;
import models.CustomerCar;
import services.CustomerService;
import ui.pages.CounterStaffPanels.forms.AddCustomerForm;
import ui.pages.CounterStaffPanels.ManageCustomerPanel;
import ui.pages.CounterStaffPanels.components.CustomerCard;
import ui.pages.CounterStaffPanels.components.VehicleRow;
import utils.DialogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerManagementController {

    private final ManageCustomerPanel manageCustomerPanel;
    private final CustomerService customerService = new CustomerService();
    private final VehicleManagementController vehicleController;
    Logger logger = Logger.getLogger(CustomerManagementController.class.getName());

    public CustomerManagementController(ManageCustomerPanel manageCustomerPanel) {
        this.manageCustomerPanel = manageCustomerPanel;
        this.vehicleController = new VehicleManagementController(manageCustomerPanel, this::loadCustomers);
        initListeners();
        loadCustomers();
    }

    private void initListeners() {
        manageCustomerPanel.addCustomerBtn.addActionListener(e -> openAddCustomerForm(false, null));
    }

    private void openAddCustomerForm(boolean isEdit, Customer customerToEdit) {
        AddCustomerForm form = new AddCustomerForm(isEdit, customerToEdit);
        new AddCustomerFormController(form);
        form.setVisible(true);
        // We might want to reload customers after the form is closed
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                loadCustomers();
            }
        });
    }

    //Refresh customers
    private void loadCustomers() {
        manageCustomerPanel.cardContainer.removeAll();
        try {
            List<Customer> customers = customerService.getCustomers();
            manageCustomerPanel.customerCountLabel.setText(customers.size() + (customers.size() > 1 ? " customers" : " customer"));
            
            for (Customer customer : customers) {
                CustomerCard card = new CustomerCard(customer);

                // Add Listeners to Card Buttons
                card.editCustomerBtn.addActionListener(e -> openAddCustomerForm(true, customer));
                card.deleteCustomerBtn.addActionListener(e -> deleteCustomer(customer));
                if (card.addVehicleBtn != null) {
                    card.addVehicleBtn.addActionListener(e -> vehicleController.openVehicleForm(customer, false, null));
                }

                // Add Listeners to each Vehicle Row via VehicleManagementController
                for (VehicleRow row : card.getVehicleRows()) {
                    vehicleController.setupVehicleRowListeners(row, customer);
                }
                manageCustomerPanel.cardContainer.add(card);
                manageCustomerPanel.cardContainer.add(Box.createRigidArea(new Dimension(0, 20)));
            }
        } catch (FileCorruptedException e) {
            DialogUtil.showErrorMessage("Failed Loading Customers", e.getMessage());
        }
        manageCustomerPanel.cardContainer.revalidate();
        manageCustomerPanel.cardContainer.repaint();
    }

    private void deleteCustomer(Customer customer){
        boolean confirmDeleteCustomer = DialogUtil.showConfirmationMessage("Confirm Delete?" , String.format("Are you sure you want to delete %s?" , customer.getName()));
        if(confirmDeleteCustomer){
            try {
                customerService.deleteCustomer(customer.getId());
                loadCustomers();
            } catch (DeleteException e) {
                DialogUtil.showErrorMessage("Failed to Delete Customer" , e.getMessage());
            } catch (Exception e) {
                DialogUtil.showErrorMessage("Failed to Delete Customer" , "Encountered error when deleting customer");
                logger.log(Level.SEVERE , e.getMessage());
            }
        }
    }
}
