package services;

import enums.AppointmentStatus;
import exceptions.*;
import mapper.CustomerCarMapper;
import models.Appointment;
import models.CustomerCar;
import repositories.CrudRepository;
import utils.RandomIdGenerator;

import java.io.*;
import java.time.Year;
import java.util.List;
import java.util.function.Predicate;

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

    public List<CustomerCar> getCars(Predicate<CustomerCar> filter) throws FileCorruptedException {
        return customerCarCrudRepository.getAll(filter);
    }

    public void addCar(CustomerCar carToAdd) throws BusinessRuleException, IOException, FileCorruptedException {
        boolean carPlateHasExisted = !customerCarCrudRepository.getAll(customerCar->customerCar.getCarPlate().equalsIgnoreCase(carToAdd.getCarPlate())).isEmpty();
        final int maxCarAllowed = 3;
        boolean hasReachedMaxCarAllowed = getCustomerCars(carToAdd.getCustomerId()).size() >=maxCarAllowed;
        if(carPlateHasExisted){
            throw new BusinessRuleException("Car plate has existed");
        }
        if(hasReachedMaxCarAllowed){
            throw new BusinessRuleException("Already reached maximum car allowed to be added");
        }
        if(hasExceededCurrentYear(carToAdd.getManufactureYear())){
            throw new BusinessRuleException("Invalid manufacture year");
        }
        String carId = generateCarId();
        carToAdd.setId(carId);
        customerCarCrudRepository.create(carToAdd);
    }

    public void deleteCarById(String carId) throws DeleteException, FileCorruptedException, BusinessRuleException {
        customerCarCrudRepository.delete(carId);
        if(hasNotCompletedAppointment(carId)){
            throw new BusinessRuleException("Not allowed to delete car when still have upcoming appointments");
        }
    }

    public void deleteCarByCustomerId(String customerId) throws FileCorruptedException, BusinessRuleException {
        List<CustomerCar> customerCars = customerCarCrudRepository.getAll(
                customerCar -> customerCar.getCustomerId().equals(customerId));
        for(CustomerCar customerCar : customerCars){
            if(hasNotCompletedAppointment(customerCar.getId())){
                throw new BusinessRuleException("Not allowed to delete car when still have upcoming appointments");
            }
        }
        List<CustomerCar> cars = getCars();
        cars.removeIf(customerCar -> customerCar.getCustomerId().equals(customerId));
        customerCarCrudRepository.writeAll(cars);
    }

    public void updateCar(CustomerCar carToUpdate) throws FileCorruptedException,UpdateException {
        boolean carPlateHasExisted = !customerCarCrudRepository.getAll(customerCar ->customerCar.getCarPlate().equalsIgnoreCase(carToUpdate.getCarPlate()) && !customerCar.getId().equalsIgnoreCase(carToUpdate.getId())).isEmpty();
        if(carPlateHasExisted){
            throw new UpdateException("Car plate has already been recorded");
        }
        if(hasExceededCurrentYear(carToUpdate.getManufactureYear())){
            throw new UpdateException("Invalid manufacture year");
        }
        try {
            customerCarCrudRepository.update(carToUpdate);
        } catch (NotFoundException e) {
            throw new UpdateException(e.getMessage());
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

    private boolean hasExceededCurrentYear(int year){
        int currentYear = Year.now().getValue();
        return year > currentYear;
    }

    private boolean hasNotCompletedAppointment(String carId) throws FileCorruptedException {
        AppointmentService appointmentService = new AppointmentService();
        List<Appointment> upcomingAppointments =
                appointmentService.getAppointments(appointment -> appointment.getCarId().equals(carId)
                        && appointment.getStatusService().equals(AppointmentStatus.ASSIGNED));
        return !upcomingAppointments.isEmpty();
    }

}
