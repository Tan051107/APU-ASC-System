package ui.controller.CounterStaffControllers.FormController;

import enums.FuelType;
import exceptions.*;
import models.Customer;
import models.CustomerCar;
import services.CustomerCarService;
import ui.pages.CounterStaffPanels.forms.AddVehicleForm;
import utils.DialogUtil;
import utils.validators.ValidationResult;
import utils.validators.Validator;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddVehicleFormController {

    private final AddVehicleForm addVehicleForm;
    private final CustomerCarService customerCarService = new CustomerCarService();
    Logger logger = Logger.getLogger(AddVehicleFormController.class.getName());

    public AddVehicleFormController(AddVehicleForm addVehicleForm){
        this.addVehicleForm = addVehicleForm;
        initListeners();
    }

    private void initListeners(){
        initFields();
        addVehicleForm.addVehicleButton.addActionListener(e -> {
            if(addVehicleForm.isEdit()){
                updateVehicle();
            }
            else{
                addVehicle();
            }
        });
    }

    private void initFields(){
        if(addVehicleForm.isEdit() && addVehicleForm.getCustomerCar()!=null){
            CustomerCar customerCar = addVehicleForm.getCustomerCar();
            addVehicleForm.plateField.setText(customerCar.getCarPlate());
            addVehicleForm.brandField.setText(customerCar.getBrand());
            addVehicleForm.modelField.setText(customerCar.getModel());
            addVehicleForm.yearField.setText(String.valueOf(customerCar.getManufactureYear()));
            addVehicleForm.mileageField.setText(String.valueOf(customerCar.getMileage()));
            addVehicleForm.fuelTypeCombo.setSelectedItem(customerCar.getFuelType().toString());
        }
    }

    private CustomerCar fieldToCustomerCar() throws ValidationException {
        Customer vehicleOwner = addVehicleForm.getCustomer();
        String carPlate = addVehicleForm.plateField.getText();
        String brand = addVehicleForm.brandField.getText();
        String model = addVehicleForm.modelField.getText();
        String manufactureYear = addVehicleForm.yearField.getText();
        String mileAge = addVehicleForm.mileageField.getText();
        String fuelType = Objects.requireNonNull(addVehicleForm.fuelTypeCombo.getSelectedItem()).toString();
        ValidationResult validationResult = new ValidationResult();
        Validator.required(validationResult,"Car Plate",carPlate);
        Validator.required(validationResult,"Car Model" , model);
        Validator.required(validationResult,"Car Brand",brand);
        Validator.validateInteger(validationResult,"Manufacture Year" , manufactureYear);
        Validator.validateDouble(validationResult,"Mileage" , mileAge);
        if(validationResult.hasError()){
            throw new ValidationException(validationResult.getErrors());
        }
        CustomerCar customerCar = new CustomerCar();
        customerCar.setCarPlate(carPlate);
        customerCar.setBrand(brand);
        customerCar.setModel(model);
        customerCar.setManufactureYear(Integer.parseInt(manufactureYear));
        customerCar.setMileage(Double.parseDouble(mileAge));
        customerCar.setFuelType(FuelType.fromString(fuelType));
        customerCar.setCustomerId(vehicleOwner.getId());
        return customerCar;
    }

    private void addVehicle(){
        try {
            System.out.println("calling to add vehicle");
            CustomerCar vehicleToBeAdded = fieldToCustomerCar();
            customerCarService.addCar(vehicleToBeAdded);
            DialogUtil.showInfoMessage("Added Successfully" , String.format("Successfully added %s." , vehicleToBeAdded.getCarPlate()));
            addVehicleForm.dispose();
        } catch (BusinessRuleException | ValidationException e) {
            DialogUtil.showErrorMessage("Failed to Add Car" , e.getMessage());
        } catch (FileCorruptedException | IOException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
    }

    private void updateVehicle(){
        try {
            CustomerCar vehicleToBeUpdated = fieldToCustomerCar();
            vehicleToBeUpdated.setId(addVehicleForm.getCustomerCar().getId());
            vehicleToBeUpdated.setCreatedAt(addVehicleForm.getCustomerCar().getCreatedAt());
            customerCarService.updateCar(vehicleToBeUpdated);
            DialogUtil.showInfoMessage("Updated Successfully" , "Successfully updated vehicle");
            addVehicleForm.dispose();
        } catch (UpdateException | ValidationException e) {
            DialogUtil.showWarningMessage("Update Error" , e.getMessage());
        }
        catch (Exception e) {
            logger.log(Level.SEVERE,e.getMessage());
        }
    }

}
