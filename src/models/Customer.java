package models;

import enums.UserType;

import java.time.LocalDateTime;

public class Customer extends User{

    public Customer() {
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
