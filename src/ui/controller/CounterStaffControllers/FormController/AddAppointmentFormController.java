package ui.controller.CounterStaffControllers.FormController;

import exceptions.FileCorruptedException;
import models.Customer;
import models.CustomerCar;
import services.CustomerCarService;
import services.CustomerService;
import ui.pages.CounterStaffPanels.forms.AddAppointmentForm;
import utils.DialogUtil;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddAppointmentFormController {
    private final AddAppointmentForm addAppointmentForm;
    Logger logger = Logger.getLogger(AddAppointmentFormController.class.getName());
    
    public AddAppointmentFormController(AddAppointmentForm addAppointmentForm){
        this.addAppointmentForm = addAppointmentForm;
        initForm();
    }
    
    public void initForm(){
        initializeNameField();
        hideCarPlateField();
        addAppointmentForm.customerSelectionCombo.addActionListener(e->initializeCarPlateField(Objects.requireNonNull(addAppointmentForm.customerSelectionCombo.getSelectedItem()).toString()));
    }
    
    private void initializeNameField(){
        CustomerService customerService = new CustomerService();
        try {
            List<Customer> customers = customerService.getCustomers();
            if(customers.isEmpty()){
                DialogUtil.showWarningMessage("No customer available" , "No customer available for appointment assignment");
            }
            else{
                for(Customer customer : customers){
                    addAppointmentForm.customerSelectionCombo.addItem(customer.getId());
                }
            }
        } catch (FileCorruptedException e) {
            DialogUtil.showErrorMessage("Init Form Failed" , "Failed to initialize form");
            logger.log(Level.SEVERE , e.getMessage());
        }
    }

    private void initializeCarPlateField(String customerSelected){
        CustomerCarService customerCarService = new CustomerCarService();
        hideCarPlateField();
        addAppointmentForm.carPlateSelectionCombo.removeAllItems();
        if(!customerSelected.equals("Select Customer")){
            try {
                List<CustomerCar> customerCars = customerCarService.getCustomerCars(customerSelected);
                if(customerCars.isEmpty()){
                    DialogUtil.showWarningMessage("No car available" , "No car available for appointment assignment");
                }
                else{
                    showCarPlateField();
                    for(CustomerCar customerCar :customerCars){
                        addAppointmentForm.carPlateSelectionCombo.addItem(customerCar.getCarPlate());
                    }
                }
            } catch (FileCorruptedException e) {
                DialogUtil.showErrorMessage("Retrieve car error" , "Failed to retrieve customer cars");
                logger.log(Level.SEVERE , e.getMessage());
            }
        }
    }

    private void hideCarPlateField(){
        addAppointmentForm.carPlateLabel.setVisible(false);
        addAppointmentForm.carPlateSelectionCombo.setVisible(false);
        addAppointmentForm.carPlateSpacing.setVisible(false);
    }

    private void showCarPlateField(){
        addAppointmentForm.carPlateLabel.setVisible(true);
        addAppointmentForm.carPlateSelectionCombo.setVisible(true);
        addAppointmentForm.carPlateSpacing.setVisible(true);
    }
}
