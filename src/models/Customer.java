package models;

import enums.UserType;
import exceptions.FileCorruptedException;
import services.CustomerCarService;

import java.time.LocalDateTime;
import java.util.List;

public class Customer extends User{
    private final CustomerCarService customerCarService = new CustomerCarService();

    public Customer() {
    }

    public List<CustomerCar> getCars() throws FileCorruptedException {
        return customerCarService.getCustomerCars(getId());
    }

    public Customer(String name, String email, String password, String contactNumber) {
        super(name, email, password, contactNumber);
        setUserType(UserType.CUSTOMER);
    }

    public Customer(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String name, String email, String password, String contactNumber) {
        super(id, createdAt, updatedAt, name, email, password, contactNumber);
        setUserType(UserType.CUSTOMER);
    }

}
