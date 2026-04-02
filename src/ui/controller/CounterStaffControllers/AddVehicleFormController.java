package ui.controller.CounterStaffControllers;

import models.CustomerCar;
import ui.pages.CounterStaffPanels.forms.AddVehicleForm;

public class AddVehicleFormController {

    private final AddVehicleForm addVehicleForm;

    public AddVehicleFormController(AddVehicleForm addVehicleForm){
        this.addVehicleForm = addVehicleForm;
        setUpListener();
    }

    public void setUpListener(){
        setUpForm();
    }

    public void setUpForm(){
        if(addVehicleForm.isEdit() && addVehicleForm.getCustomerCar()!=null){
            CustomerCar customerCar = addVehicleForm.getCustomerCar();
            addVehicleForm.plateField.setText(customerCar.getCarPlate());
            addVehicleForm.brandField.setText(customerCar.getCarBrand());
            addVehicleForm.modelField.setText(customerCar.getCarModel());
            addVehicleForm.yearField.setText(String.valueOf(customerCar.getManufactureYear()));
            addVehicleForm.mileageField.setText(String.valueOf(customerCar.getMileage()));
            addVehicleForm.fuelTypeCombo.setSelectedItem(customerCar.getFuelType().toString());
        }
    }
}
