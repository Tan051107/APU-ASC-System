package services;

import dto.AddCarDto;
import dto.AddCustomerDto;
import enums.UserType;
import exceptions.*;
import mapper.CustomerMapper;
import models.Customer;
import models.CustomerCar;
import repositories.CrudRepository;

import java.util.List;

public class CustomerService {
    private final String USER_FILE = "txt_files/User.txt";
    private final UserService userService = new UserService();
    private final CustomerCarService customerCarService = new CustomerCarService();
    private final CustomerMapper customerMapper = new CustomerMapper();
    private final CrudRepository<Customer> customerCrudRepository = new CrudRepository<>(USER_FILE,customerMapper);

    public void addCustomer(AddCustomerDto addCustomerDto) throws SignUpException {
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

    public void deleteCustomer(String customerId) throws DeleteException {
        userService.deleteUser(customerId);
        customerCarService.deleteCarByCustomerId(customerId);
    }

    public void updateCustomer(Customer customer){
        try {
            customerCrudRepository.update(customer);
        } catch (FileCorruptedException | NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void getCustomers() throws FileCorruptedException {
        List<Customer> customers = customerCrudRepository.getAll(customer -> customer.getUserType().equals(UserType.CUSTOMER));
    }
}
