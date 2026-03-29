package models;

import enums.UserType;
import services.UserService;

import java.time.LocalDateTime;

public class User extends BaseModel {
    private String name;
    private String email;
    private String password;
    private String contactNumber;
    private UserType userType;

    public User(){

    }

    public User(String name, String email, String password, String contactNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.contactNumber = contactNumber;
    }

    public User(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String name, String email, String password, String contactNumber) {
        super(id, createdAt, updatedAt);
        this.name = name;
        this.email = email;
        this.password = password;
        this.contactNumber = contactNumber;
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


}
