package ui.controller.CounterStaffControllers.FormController;

import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import exceptions.NotFoundException;
import models.Customer;
import models.CustomerCar;
import models.Services;
import models.Technician;
import services.*;
import ui.ComboBoxItems.ServiceComboBoxItem;
import ui.pages.CounterStaffPanels.forms.AddAppointmentForm;
import utils.DialogUtil;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

public class AddAppointmentFormController {
    private final AddAppointmentForm addAppointmentForm;
    Logger logger = Logger.getLogger(AddAppointmentFormController.class.getName());
    ServicesService servicesService = new ServicesService();
    
    public AddAppointmentFormController(AddAppointmentForm addAppointmentForm){
        this.addAppointmentForm = addAppointmentForm;
        initForm();
    }
    
    public void initForm(){
        initializeNameField();
        initializeServiceField();
        hideCarPlateField();
        addAppointmentForm.customerSelectionCombo.addActionListener(e->initializeCarPlateField(Objects.requireNonNull(addAppointmentForm.customerSelectionCombo.getSelectedItem()).toString()));
        addAppointmentForm.technicianSelectionCombo.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                getAvailableTechnicians();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }

        });
    }

    private void initializeServiceField(){
        try {
            List<Services> services = servicesService.getServices();
            if(services.isEmpty()){
                DialogUtil.showWarningMessage("No service available" , "No service available for appointment assignment");
            }
            for(Services service : services){
                addAppointmentForm.serviceTypeCombo.addItem(new ServiceComboBoxItem(service.getId() ,service.getServiceName()));
            }
        } catch (GetEntityListException e) {
            DialogUtil.showErrorMessage("Init Form Failed" , "Failed to initialize form");
            logger.log(Level.SEVERE , e.getMessage());
        }
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

    private void hideTechnicianField(){
        addAppointmentForm.technicianLabel.setVisible(false);
        addAppointmentForm.technicianSelectionCombo.setVisible(false);
        addAppointmentForm.technicianSpacing.setVisible(false);
    }

    private void showTechnicianField(){
        addAppointmentForm.technicianLabel.setVisible(true);
        addAppointmentForm.technicianSelectionCombo.setVisible(true);
        addAppointmentForm.technicianSpacing.setVisible(true);
    }

    private void getAvailableTechnicians(){
        addAppointmentForm.technicianSelectionCombo.removeAllItems();
        String selectedAppointmentDate = addAppointmentForm.dateField.getText();
        String selectedAppointmentTime = addAppointmentForm.timeField.getText();
        ServiceComboBoxItem serviceSelected = (ServiceComboBoxItem) addAppointmentForm.serviceTypeCombo.getSelectedItem();
        if(!selectedAppointmentTime.isEmpty() && !selectedAppointmentDate.isEmpty() && !(serviceSelected == null)){
            String serviceSelectedId = serviceSelected.getId();
            try {
                int selectedServiceDuration = servicesService.getServicesById(serviceSelectedId).getServiceDuration();
                String appointmentDateTimeString = selectedAppointmentDate+" "+selectedAppointmentTime;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime appointmentDateTime = LocalDateTime.parse(appointmentDateTimeString, formatter);
                AppointmentService appointmentService = new AppointmentService();
                List<Technician> availableTechnicians = appointmentService.getAvailableTechnicians(appointmentDateTime,selectedServiceDuration);
                if(!availableTechnicians.isEmpty()){
                    for(Technician availableTechnician : availableTechnicians){
                        addAppointmentForm.technicianSelectionCombo.addItem(availableTechnician.getId());
                    }
                    showTechnicianField();
                }
            } catch (GetEntityListException | NotFoundException e) {
                DialogUtil.showErrorMessage("Failed to get available technician" , e.getMessage());
            }
            catch (DateTimeParseException e){
                DialogUtil.showErrorMessage("Failed to get available technician" , "Please fill in appointment date and time");
                logger.log(Level.SEVERE , e.getMessage());
            } catch (FileCorruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
