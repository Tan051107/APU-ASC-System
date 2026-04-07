package services;

import enums.AppointmentStatus;
import enums.UserType;
import exceptions.*;
import mapper.CustomerMapper;
import models.Appointment;
import models.Customer;
import models.User;
import repositories.CrudRepository;

import java.util.List;
import java.util.function.Predicate;

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

    public void deleteCustomer(String customerId) throws FileCorruptedException, DeleteException {
        AppointmentService appointmentService = new AppointmentService();
        List<Appointment> notCompletedAppointments = appointmentService.getAppointments(appointment -> appointment.getCustomerId().equalsIgnoreCase(customerId) && appointment.getStatusService().equals(AppointmentStatus.ASSIGNED));
        if(!notCompletedAppointments.isEmpty()){
            throw new DeleteException("Customer couldn't be deleted because still have upcoming appointments");
        }
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

    public List<Customer> getCustomers(Predicate<Customer> filter) throws FileCorruptedException {
        return customerCrudRepository.getAll(filter);
    }

    public Customer getCustomerById(String customerId) throws FileCorruptedException {
        return customerCrudRepository.getOne(customerId);
    }

    public List<Customer> getCustomersByNameOrEmail(String keyword) throws FileCorruptedException {
        String lowerCaseKeyword = keyword.toLowerCase();
        return getCustomers().stream()
                .filter(customer -> customer.getName().toLowerCase().contains(lowerCaseKeyword) || customer.getEmail().toLowerCase().contains(lowerCaseKeyword))
                .toList();
    }

}
