package ui.controller.CounterStaffControllers;

import exceptions.DeleteException;
import exceptions.FileCorruptedException;
import models.Customer;
import services.CustomerService;
import ui.controller.CounterStaffControllers.FormController.AddCustomerFormController;
import ui.pages.CounterStaffPanels.forms.AddCustomerForm;
import ui.pages.CounterStaffPanels.ManageCustomerPanel;
import ui.pages.CounterStaffPanels.components.CustomerCard;
import ui.pages.CounterStaffPanels.components.VehicleRow;
import utils.CSVExporter;
import utils.DialogUtil;
import utils.exporters.CsvExporters.CustomerCsvExporter;

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
        this.vehicleController = new VehicleManagementController(this::loadCustomers);
        initListeners();
    }

    private void initListeners() {
        manageCustomerPanel.addCustomerBtn.addActionListener(e -> openAddCustomerForm(false, null));
        manageCustomerPanel.searchField.addActionListener(e->searchCustomers());
        manageCustomerPanel.exportBtn.addActionListener(e -> exportCustomerData());
        resetAllCustomers();
    }

    private void openAddCustomerForm(boolean isEdit, Customer customerToEdit) {
        Window parent = SwingUtilities.getWindowAncestor(manageCustomerPanel);
        AddCustomerForm form = new AddCustomerForm((Frame)parent, isEdit, customerToEdit);
        new AddCustomerFormController(form);
        // Reload customers after the form is closed
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                resetAllCustomers();
            }
        });
        form.setVisible(true);
    }

    private void resetAllCustomers(){
        try {
            List<Customer> customers = customerService.getCustomers();
            manageCustomerPanel.setCustomers(customers);
            loadCustomers();
        } catch (FileCorruptedException e) {
            DialogUtil.showErrorMessage("Encountered Error" , "Failed to get customers");
            logger.log(Level.SEVERE , e.getMessage());
        }
    }

    //Refresh customers
    private void loadCustomers(){
        List<Customer> customers = manageCustomerPanel.getCustomers();
        manageCustomerPanel.cardContainer.removeAll();
        manageCustomerPanel.customerCountLabel.setText(customers.size() + (customers.size() > 1 ? " customers" : " customer"));

        for (Customer customer : customers) {
            CustomerCard card = new CustomerCard(customer);

            // Add Listeners to Card Buttons
            card.editCustomerBtn.addActionListener(e -> openAddCustomerForm(true, customer));
            card.deleteCustomerBtn.addActionListener(e -> deleteCustomer(customer));
            if (card.addVehicleBtn != null) {
                card.addVehicleBtn.addActionListener(e -> vehicleController.addVehicle(customer));
            }

            // Add Listeners to each Vehicle Row via VehicleManagementController
            for (VehicleRow row : card.getVehicleRows()) {
                vehicleController.setupVehicleRowListeners(row, customer);
            }
            manageCustomerPanel.cardContainer.add(card);
            manageCustomerPanel.cardContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        manageCustomerPanel.cardContainer.revalidate();
        manageCustomerPanel.cardContainer.repaint();
    }


    private void deleteCustomer(Customer customer){
        boolean confirmDeleteCustomer = DialogUtil.showConfirmationMessage("Confirm Delete?" , String.format("Are you sure you want to delete %s?" , customer.getName()));
        if(confirmDeleteCustomer){
            try {
                customerService.deleteCustomer(customer.getId());
                resetAllCustomers();
                DialogUtil.showInfoMessage("Deleted Successfully" , "Successfully deleted customer");
            } catch (DeleteException e) {
                DialogUtil.showErrorMessage("Failed to Delete Customer" , e.getMessage());
            } catch (Exception e) {
                DialogUtil.showErrorMessage("Failed to Delete Customer" , "Encountered error when deleting customer");
                logger.log(Level.SEVERE , e.getMessage());
            }
        }
    }

    private void searchCustomers(){
        String keyword = manageCustomerPanel.searchField.getText();
        try {
            if(keyword.isEmpty()){
                resetAllCustomers();
            }
            else{
                List<Customer> foundCustomers = customerService.getCustomersByNameOrEmail(keyword);
                manageCustomerPanel.setCustomers(foundCustomers);
                loadCustomers();
            }
        } catch (FileCorruptedException e) {
            DialogUtil.showErrorMessage("Error encountered" , "Failed to search customer");
            logger.log(Level.SEVERE,e.getMessage());
        }
    }

    private void exportCustomerData(){
        List<Customer> customers = manageCustomerPanel.getCustomers();
        CustomerCsvExporter customerCsvExporter = new CustomerCsvExporter();
        CSVExporter<Customer> csvExporter = new CSVExporter<>();
        csvExporter.exportData(customers,"Customers" , customerCsvExporter);
    }

}
