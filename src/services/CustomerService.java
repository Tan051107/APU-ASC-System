package services;

import dto.AddCarDto;
import dto.AddCustomerDto;
import enums.UserType;
import exceptions.*;
import mapper.CustomerMapper;
import models.Customer;
import models.CustomerCar;
import models.User;
import repositories.CrudRepository;

import java.util.List;

public class CustomerService {
    private final String USER_FILE = "txt_files/User.txt";
    private final UserService userService = new UserService();
    private final CustomerCarService customerCarService = new CustomerCarService();
    private final CustomerMapper customerMapper = new CustomerMapper();
    private final CrudRepository<Customer> customerCrudRepository = new CrudRepository<>(USER_FILE,customerMapper);

    public void addCustomer(User customerToSignUp) throws SignUpException {
        customerToSignUp.setUserType(UserType.CUSTOMER);
        userService.signUpUser(customerToSignUp);
    }

    //TODO:If got appointment don't allow delete
    public void deleteCustomer(String customerId) throws FileCorruptedException, DeleteException {
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

    public List<Customer> getCustomers() throws FileCorruptedException {
        return customerCrudRepository.getAll(customer -> customer.getUserType().equals(UserType.CUSTOMER));
    }

    public Customer findOne(String customerId) throws FileCorruptedException {
        return customerCrudRepository.getOne(customerId);
    }

    public List<Customer> getCustomersByNameOrEmail(String keyword) throws FileCorruptedException {
        String lowerCaseKeyword = keyword.toLowerCase();
        return getCustomers().stream()
                .filter(customer -> customer.getName().toLowerCase().contains(lowerCaseKeyword) || customer.getEmail().toLowerCase().contains(lowerCaseKeyword))
                .toList();
    }

}
