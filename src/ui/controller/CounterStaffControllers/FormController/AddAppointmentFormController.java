package ui.controller.CounterStaffControllers.FormController;

import enums.AppointmentStatus;
import exceptions.*;
import models.*;
import services.*;
import ui.pages.CounterStaffPanels.components.ComboBoxItems.CustomComboBoxItem;
import ui.pages.CounterStaffPanels.forms.AddAppointmentForm;
import utils.DialogUtil;
import utils.validators.ValidationResult;
import utils.validators.Validator;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddAppointmentFormController {
    private final AddAppointmentForm addAppointmentForm;
    Logger logger = Logger.getLogger(AddAppointmentFormController.class.getName());
    private final ServicesService servicesService = new ServicesService();
    private final AppointmentService appointmentService = new AppointmentService();
    private final CustomerCarService customerCarService = new CustomerCarService();
    
    public AddAppointmentFormController(AddAppointmentForm addAppointmentForm){
        this.addAppointmentForm = addAppointmentForm;
        initForm();
    }
    
    public void initForm(){
        initializeNameField();
        initializeServiceField();
        if(!addAppointmentForm.isEdit()){
            hideCarPlateField();
        }
        initializeFields();
        addAppointmentForm.customerSelectionCombo.addActionListener(e-> updateCarPlateField());
        addAppointmentForm.technicianSelectionCombo.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                updateAvailableTechnicians();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }

        });
        addAppointmentForm.createAppointmentBtn.addActionListener(e->{
            if(addAppointmentForm.isEdit()){
                updateAppointment();
            }
            else{
                createAppointment();
            }
        });
    }

    private void initializeFields() {
        if(addAppointmentForm.isEdit() && addAppointmentForm.getAppointmentToEdit() !=null){
            Appointment appointmentToEdit = addAppointmentForm.getAppointmentToEdit();
            String customerId = appointmentToEdit.getCustomerId();
            String serviceId = appointmentToEdit.getServiceId();
            String technicianId = appointmentToEdit.getTechnicianId();
            String appointmentTime = appointmentToEdit.getTime().toString();
            String appointmentDate = appointmentToEdit.getDate().toString();
            String description = appointmentToEdit.getDescription();
            try {

                String carPlate = appointmentToEdit.getCar().getCarPlate();
                String customerName = appointmentToEdit.getCustomer().getName();
                String serviceName = appointmentToEdit.getService().getServiceName();
                String technicianName = appointmentToEdit.getTechnician().getName();
                addAppointmentForm.customerSelectionCombo.setSelectedItem(new CustomComboBoxItem(customerId , " | " + customerName));
                addAppointmentForm.serviceTypeCombo.setSelectedItem(new CustomComboBoxItem(serviceId , " | " + serviceName));
                addAppointmentForm.timeField.setText(appointmentTime);
                addAppointmentForm.dateField.setText(appointmentDate);
                addAppointmentForm.descriptionArea.setText(description);
                getCarPlateSelections(); //Get car plates based on customer chosen
                getAvailableTechnicians(); //Get available technicians based on the time and date in the created appointment
                addAppointmentForm.carPlateSelectionCombo.setSelectedItem(carPlate);
                addAppointmentForm.technicianSelectionCombo.setSelectedItem(new CustomComboBoxItem(technicianId, " | " + technicianName));

            } catch (FileCorruptedException | GetEntityListException e) {
                logger.log(Level.SEVERE,e.getMessage());
                DialogUtil.showErrorMessage("Initialize fields error" , "Failed to initialize fields");
            }
        }
    }

    private void initializeServiceField(){
        try {
            List<Services> services = servicesService.getServices();
            if(services.isEmpty()){
                DialogUtil.showWarningMessage("No service available" , "No service available for appointment assignment");
            }
            for(Services service : services){
                addAppointmentForm.serviceTypeCombo.addItem(new CustomComboBoxItem(service.getId() ,service.getServiceName()));
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
                    CustomComboBoxItem customerComboBoxItem= new CustomComboBoxItem(customer.getId() , " | " + customer.getName());
                    addAppointmentForm.customerSelectionCombo.addItem(customerComboBoxItem);
                }
            }
        } catch (FileCorruptedException e) {
            DialogUtil.showErrorMessage("Init Form Failed" , "Failed to initialize form");
            logger.log(Level.SEVERE , e.getMessage());
        }
    }

    private Appointment fieldToAppointment() throws ValidationException {
        CustomComboBoxItem customerSelected = (CustomComboBoxItem) addAppointmentForm.customerSelectionCombo.getSelectedItem();
        CustomComboBoxItem serviceSelected = (CustomComboBoxItem) addAppointmentForm.serviceTypeCombo.getSelectedItem();
        CustomComboBoxItem technicianSelected = (CustomComboBoxItem)addAppointmentForm.technicianSelectionCombo.getSelectedItem();
        String description = addAppointmentForm.descriptionArea.getText();
        String carSelected = Objects.requireNonNull(addAppointmentForm.carPlateSelectionCombo.getSelectedItem()).toString();
        String appointmentDateString = addAppointmentForm.dateField.getText();
        String appointmentTimeString = addAppointmentForm.timeField.getText();
        if(customerSelected == null || serviceSelected == null || technicianSelected == null || carSelected.isEmpty()){
            throw new ValidationException("Please fill up required fields");
        }
        String customerIdSelected = customerSelected.getId();
        String serviceIdSelected = serviceSelected.getId();
        String technicianIdSelected = technicianSelected.getId();
        String carId;
        try {
            carId = customerCarService.getCarByCarPlate(carSelected).getId();
        } catch (FileCorruptedException e) {
            throw new RuntimeException(e);
        }
        ValidationResult validationResult = new ValidationResult();
        Validator.required(validationResult,"Customer" , customerIdSelected);
        Validator.required(validationResult,"Service" , serviceIdSelected);
        Validator.required(validationResult,"Technician" , technicianIdSelected);
        Validator.required(validationResult,"Appointment description" , description);
        Validator.required(validationResult,"Car" , carId);
        if(validationResult.hasError()){
            throw new ValidationException(validationResult.getErrors());

        }
        Appointment appointment = new Appointment();
        try{
            LocalDate appointmentDate = LocalDate.parse(appointmentDateString);
            LocalTime appointmentTime = LocalTime.parse(appointmentTimeString);
            appointment.setDate(appointmentDate);
            appointment.setTime(appointmentTime);

        }
        catch (DateTimeParseException e){
            throw new ValidationException("Please select valid appointment date and time");
        }
        appointment.setCustomerId(customerIdSelected);
        appointment.setServiceId(serviceIdSelected);
        appointment.setTechnicianId(technicianIdSelected);
        appointment.setDescription(description);
        appointment.setCarId(carId);
        return appointment;
    }

    private void createAppointment(){
        try {
            Appointment appointmentToCreate = fieldToAppointment();
            appointmentToCreate.setStatusService(AppointmentStatus.ASSIGNED);
            appointmentToCreate.setStaffId(addAppointmentForm.getLoginStaff().getId());
            appointmentService.createAppointment(appointmentToCreate);
            addAppointmentForm.dispose();
            DialogUtil.showInfoMessage("Created Successfully" , "Appointment created successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE , e.getMessage());
            DialogUtil.showWarningMessage("Error Creating Appointment" , e.getMessage());
        }
    }

    private void updateAppointment(){
        try {
            Appointment appointmentToUpdate = fieldToAppointment();
            appointmentToUpdate.setId(addAppointmentForm.getAppointmentToEdit().getId());
            appointmentToUpdate.setCreatedAt(addAppointmentForm.getAppointmentToEdit().getCreatedAt());
            appointmentToUpdate.setStatusService(addAppointmentForm.getAppointmentToEdit().getStatusService());
            appointmentToUpdate.setStaffId(addAppointmentForm.getAppointmentToEdit().getStaffId());
            appointmentService.updateAppointment(appointmentToUpdate);
            addAppointmentForm.dispose();
            DialogUtil.showInfoMessage("Updated Successfully" , "Appointment updated successfully");
        } catch (ValidationException |BusinessRuleException | NotFoundException e) {
            DialogUtil.showWarningMessage("Error Updating Appointment" , e.getMessage());;
        } catch (Exception e) {
            logger.log(Level.SEVERE,e.getMessage());
            DialogUtil.showWarningMessage("Error Updating Appointment" , "Failed to update appointment");
        }
    }

    private void getCarPlateSelections(){
        CustomComboBoxItem customerSelected = (CustomComboBoxItem) addAppointmentForm.customerSelectionCombo.getSelectedItem();
        if(customerSelected == null){
            DialogUtil.showErrorMessage("Validation Error" , "Customer is required");
            return;
        }
        String customerIdSelected = customerSelected.getId();
        if(!customerIdSelected.isEmpty()){
            try {
                List<CustomerCar> customerCars = customerCarService.getCustomerCars(customerIdSelected);
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

    private void updateCarPlateField(){
        hideCarPlateField();
        addAppointmentForm.carPlateSelectionCombo.removeAllItems();
        getCarPlateSelections();
    }

    private void getAvailableTechnicians(){
        String selectedAppointmentDate = addAppointmentForm.dateField.getText();
        String selectedAppointmentTime = addAppointmentForm.timeField.getText();
        CustomComboBoxItem serviceSelected = (CustomComboBoxItem) addAppointmentForm.serviceTypeCombo.getSelectedItem();
        if(!selectedAppointmentTime.isEmpty() && !selectedAppointmentDate.isEmpty() && !(serviceSelected == null)){
            String serviceSelectedId = serviceSelected.getId();
            try {
                int selectedServiceDuration = servicesService.getServicesById(serviceSelectedId).getServiceDuration();
                String appointmentDateTimeString = selectedAppointmentDate+" "+selectedAppointmentTime;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime appointmentDateTime = LocalDateTime.parse(appointmentDateTimeString, formatter);
                List<Technician> availableTechnicians;
                if(addAppointmentForm.isEdit() && addAppointmentForm.getAppointmentToEdit() !=null){
                    availableTechnicians = appointmentService.getAvailableTechnicians(appointmentDateTime,selectedServiceDuration,addAppointmentForm.getAppointmentToEdit().getId());
                }
                else{
                    availableTechnicians = appointmentService.getAvailableTechnicians(appointmentDateTime,selectedServiceDuration,"");
                }
                if(!availableTechnicians.isEmpty()){
                    for(Technician availableTechnician : availableTechnicians){
                        CustomComboBoxItem technicianComboBoxItem = new CustomComboBoxItem(availableTechnician.getId(), " | "+availableTechnician.getName());
                        addAppointmentForm.technicianSelectionCombo.addItem(technicianComboBoxItem);
                    }
                    showTechnicianField();
                }
            } catch (GetEntityListException | NotFoundException | BusinessRuleException e) {
                DialogUtil.showErrorMessage("Failed to get available technician" , e.getMessage());
            }
            catch (DateTimeParseException e){
                DialogUtil.showErrorMessage("Failed to get available technician" , "Please fill in appointment date and time");
                logger.log(Level.SEVERE , e.getMessage());
            } catch (FileCorruptedException e) {
                DialogUtil.showErrorMessage("Failed to get available technician" , "Encountered error when getting available technicians");
                logger.log(Level.SEVERE , e.getMessage());
            }
        }
    }

    private void updateAvailableTechnicians(){
        addAppointmentForm.technicianSelectionCombo.removeAllItems();
        getAvailableTechnicians();
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

    private void showTechnicianField(){
        addAppointmentForm.technicianLabel.setVisible(true);
        addAppointmentForm.technicianSelectionCombo.setVisible(true);
        addAppointmentForm.technicianSpacing.setVisible(true);
    }
}
