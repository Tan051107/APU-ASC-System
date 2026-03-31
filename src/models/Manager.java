package models;

import java.time.LocalDateTime;

import enums.UserType;

public class Manager extends User {
    public Manager() {
    }

    public Manager(String name, String email, String password, String contactNumber) {
        super(name, email, password, contactNumber);
        setUserType(UserType.MANAGER);
    }

    public Manager(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String name, String email, String password, String contactNumber) {
        super(id, createdAt, updatedAt, name, email, password, contactNumber);
        setUserType(UserType.MANAGER);
    }
}
