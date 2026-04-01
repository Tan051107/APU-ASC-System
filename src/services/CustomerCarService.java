package services;

import exceptions.*;
import mapper.CustomerCarMapper;
import models.CustomerCar;
import repositories.CrudRepository;
import utils.RandomIdGenerator;

import java.io.*;
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

    public CustomerCar getCarById(String id) throws GetEntityListException {
        try {
            return customerCarCrudRepository.getOne(id);
        } catch (FileCorruptedException e) {
            throw new GetEntityListException(e.getMessage());
        }
    }

    public void addCar(CustomerCar carToAdd) throws AddCarException {
        try{
            boolean carPlateHasExisted = !customerCarCrudRepository.getAll(customerCar->customerCar.getCarPlate().equalsIgnoreCase(carToAdd.getCarPlate())).isEmpty();
            final int maxCarAllowed = 3;
            boolean hasReachedMaxCarAllowed = getCustomerCars(carToAdd.getCustomerId()).size() >=maxCarAllowed;
            if(carPlateHasExisted){
                throw new AddCarException("Car plate has existed");
            }
            if(hasReachedMaxCarAllowed){
                throw new AddCarException("Already reached maximum car allowed to be added");
            }
            String carId = generateCarId();
            carToAdd.setId(carId);
            customerCarCrudRepository.create(carToAdd);
        }
        catch (FileCorruptedException | IOException e){
            throw new AddCarException(e.getMessage());
        }
    }


    public void deleteCarById(String carId) throws DeleteException {
        customerCarCrudRepository.delete(carId);
    }

    public void deleteCarByCustomerId(String customerId) throws DeleteException {
        try {
            List<CustomerCar> cars = getCars();
            cars.removeIf(customerCar -> customerCar.getCustomerId().equals(customerId));
            customerCarCrudRepository.writeAll(cars);
        } catch (FileCorruptedException e) {
            throw new DeleteException(e.getMessage());
        }
    }

    private String generateCarId(){
        try {
            while(true){
                String carId = RandomIdGenerator.generateId("CAR" ,6);
                if(getCarById(carId) == null){
                    return carId;
                }
            }
        } catch (GetEntityListException e) {
            throw new RuntimeException(e);
        }
    }


}
