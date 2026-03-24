package model;

import enums.Gender;
import enums.UserType;
import exceptions.GetUsersException;
import service.CustomerService;
import utils.RandomIdGenerator;

import java.util.List;

public class Customer extends User{

    private String carPlate;
    private String carModel;
    private final CustomerService customerService = new CustomerService();

    public Customer() {
        super();

    }

    public Customer(String id, String name, String email, String password, String contactNumber, UserType userType, String carPlate, String carModel) {
        super(id, name, email, password, contactNumber, userType);
        this.carPlate = carPlate;
        this.carModel = carModel;
    }

    public Customer(String [] userData, String [] customerOnlyData){
        String[] fullCustomerData = new String[userData.length + customerOnlyData.length];
        System.arraycopy(userData,0,fullCustomerData,0,userData.length);
        System.arraycopy(customerOnlyData,0,fullCustomerData,userData.length,customerOnlyData.length);
        setId(fullCustomerData[0]);
        setName(fullCustomerData[1]);
        setEmail(fullCustomerData[2]);
        setPassword(fullCustomerData[3]);
        setContactNumber(fullCustomerData[4]);
        setUserType(UserType.fromString(fullCustomerData[5]));
        this.carPlate = fullCustomerData[6];
        this.carModel = fullCustomerData[7];
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String toCustomerOnlyDataString(){
        return getId()+ "|" + carPlate + "|" + carModel;
    }

    public String toFullUserDataString(){
        String userToString = super.toString();
        return userToString + "|" + carPlate + "|" + carModel;
    }

    public User toUser(){
        User user = new User();
        user.setId(getId());
        user.setName(getName());
        user.setEmail(getEmail());
        user.setPassword(getPassword());
        user.setContactNumber(getContactNumber());
        user.setUserType(getUserType());
        return user;
    }

    public void generateId() throws GetUsersException {
        while(true){
            String id = RandomIdGenerator.generateId("TP" , 5);
            Customer customer = customerService.getCustomerById(id);
            if(customer == null){
                setId(id);
                break;
            }
        }
    }



}
