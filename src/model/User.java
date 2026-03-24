package model;

import enums.Gender;
import enums.UserType;
import service.UserService;
import utils.RandomIdGenerator;

import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String contactNumber;
    private UserType userType;
    private final UserService userService = new UserService();

    public User() {

    }

    public User(String id, String name, String email, String password, String contactNumber, UserType userType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.contactNumber = contactNumber;
        this.userType = userType;
    }

    public User(String[] userData){
        this.id = userData[0];
        this.name = userData[1];
        this.email = userData[2];
        this.password = userData[3];
        this.contactNumber = userData[4];
        this.userType = UserType.fromString(userData[5]);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public String toString(){
        return id+"|" + name+ "|" +email + "|" + password + "|" + contactNumber + "|" + userType.getDisplayUserType();
    }

    public String[] toArray(){
        return new String[]{this.id,this.name,this.email,this.password, this.contactNumber,this.userType.getDisplayUserType()};
    }



}
