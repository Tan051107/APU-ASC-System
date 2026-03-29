package services;

import exceptions.*;
import mapper.CustomerCarMapper;
import models.CustomerCar;
import repositories.CrudRepository;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CustomerCarService {

    private final String CUSTOMER_CAR_FILE = "txt_files/CustomerCar.txt";
    private final CustomerCarMapper customerCarMapper = new CustomerCarMapper();
    private final CrudRepository<CustomerCar> customerCarCrudRepository = new CrudRepository<>(CUSTOMER_CAR_FILE,customerCarMapper);


    public List<CustomerCar> getCars() throws FileCorruptedException {
        return customerCarCrudRepository.getAll();
    }

    public List<CustomerCar> getCustomerCars(String customerId) throws FileCorruptedException {
        return customerCarCrudRepository.getAll(customerCar -> customerCar.getCustomerId().equalsIgnoreCase(customerId));
    }

    public void addCar(CustomerCar carToAdd) throws AddCarException {
        try{
            Path path = Paths.get(CUSTOMER_CAR_FILE);
            System.out.println(path);
            boolean carPlateHasExisted = !customerCarCrudRepository.getAll(customerCar->customerCar.getCarPlate().equalsIgnoreCase(carToAdd.getCarPlate())).isEmpty();
            final int maxCarAllowed = 3;
            boolean hasReachedMaxCarAllowed = getCustomerCars(carToAdd.getCustomerId()).size() >=maxCarAllowed;
            if(carPlateHasExisted){
                throw new AddCarException("Car plate has existed");
            }
            if(hasReachedMaxCarAllowed){
                throw new AddCarException("Already reached maximum car allowed to be added");
            }
            customerCarCrudRepository.create(carToAdd);
        }
        catch (FileCorruptedException | IOException e){
            throw new AddCarException(e.getMessage());
        }
    }


    public void deleteCar(String carPlate) throws DeleteException {
        customerCarCrudRepository.delete(carPlate);
    }


}
