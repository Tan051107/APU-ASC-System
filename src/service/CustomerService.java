package service;

import exceptions.DeleteUserException;
import exceptions.GetUsersException;
import exceptions.SignUpException;
import model.Customer;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class CustomerService {

    private final String CUSTOMER_FILE = "txt_files/Customer.txt";
    private final UserService userService = new UserService();


    public List<Customer> getCustomers() throws GetUsersException{
        List<Customer> customers = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))){
            String line;
            while((line = reader.readLine()) != null){
                String[] customerOnlyData = line.split("\\|");
                if(customerOnlyData.length < 2){
                    throw new GetUsersException("Customer file contains missing customer data");
                }
                String customerId = customerOnlyData[0];
                System.out.println("Customer id:" +customerId);
                User user = userService.getUserById(customerId);
                if(user == null){
                    throw new GetUsersException("User id not found in user file");
                }
                String[] userData = user.toArray();
                Customer customer = new Customer(userData,customerOnlyData);
                customers.add(customer);
            }
        } catch (FileNotFoundException e) {
            throw new GetUsersException("File Not found");
        } catch (IOException e) {
            throw new GetUsersException("Couldn't read file");
        }
        return customers;
    }

    public Customer getCustomerById(String id) throws GetUsersException {
        List<Customer> customers = getCustomers();
        return customers.stream()
                .filter(customer -> customer.getId().equalsIgnoreCase(id))
                .findAny()
                .orElse(null);
    }

    public Customer getCustomerByEmail(String email) throws GetUsersException {
        List<Customer> customers = getCustomers();
        return customers.stream()
                .filter(customer -> customer.getEmail().equalsIgnoreCase(email))
                .findAny()
                .orElse(null);
    }

    public void signUpCustomer (Customer customerToSignUp) throws SignUpException {
        try{
            boolean customerHasExisted = getCustomerById(customerToSignUp.getId()) != null || getCustomerByEmail(customerToSignUp.getEmail()) != null;
            if(customerHasExisted){
                throw new SignUpException("User id has existed. Please select another user id to sign up user");
            }
            customerToSignUp.generateId();
            User signUpUser = customerToSignUp.toUser();
            String signUpCustomerString = customerToSignUp.toCustomerOnlyDataString();
            userService.signUpUser(signUpUser);
            try(PrintWriter customerFileWriter = new PrintWriter(new BufferedWriter(new FileWriter(CUSTOMER_FILE)))){
                customerFileWriter.println(signUpCustomerString);
            }
        } catch (Exception e) {
            throw new SignUpException(e.getMessage());
        }
    }

    public void deleteCustomer (String customerId) throws DeleteUserException {
        try{
            List<Customer> customers = getCustomers();
            userService.deleteUser(customerId);
            boolean hasRemovedCustomer = customers.removeIf(customer -> customer.getId().equalsIgnoreCase(customerId));
            if(!hasRemovedCustomer){
                throw new DeleteUserException("User id not found in customer file");
            }
            PrintWriter customerFileWriter = new PrintWriter(new BufferedWriter(new FileWriter(CUSTOMER_FILE)));
            for(Customer customer: customers){
                String customerString = customer.toCustomerOnlyDataString();
                customerFileWriter.println(customerString);
            }
        } catch (Exception e) {
            throw new DeleteUserException(e.getMessage());
        }
    }

}
