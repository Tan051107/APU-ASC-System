package models;

import java.time.LocalDateTime;

import enums.UserType;

public class Technician extends User{
    public Technician() {
    }

    public Technician(String name, String email, String password, String contactNumber) {
        super(name, email, password, contactNumber);
        setUserType(UserType.TECHNICIAN);
    }

    public Technician(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String name, String email, String password, String contactNumber) {
        super(id, createdAt, updatedAt, name, email, password, contactNumber);
        setUserType(UserType.TECHNICIAN);
    }
}
