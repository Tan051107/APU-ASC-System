package services;

import dto.AddCarDto;
import dto.AddCustomerDto;
import exceptions.AddCarException;
import exceptions.SignUpException;
import models.CustomerCar;

public class CustomerService {

    private final UserService userService = new UserService();
    private final CustomerCarService customerCarService = new CustomerCarService();

    public void signUpCustomer(AddCustomerDto addCustomerDto) throws SignUpException {
        userService.signUpUser(addCustomerDto.getUser());
        if(addCustomerDto.getCarsToBeAdded().isEmpty()){
            throw new SignUpException("At least one car must be added");
        }
        for(AddCarDto car : addCustomerDto.getCarsToBeAdded()){
            CustomerCar customerCar = new CustomerCar();
            customerCar.setId(car.getCarPlate());
            customerCar.setCustomerId(addCustomerDto.getUser().getId());
            customerCar.setCarModel(car.getCarModel());
            customerCar.setCarPlate(car.getCarPlate());
            try {
                customerCarService.addCar(customerCar);
            } catch (AddCarException e) {
                throw new SignUpException(e.getMessage());
            }
        }
    }
}
