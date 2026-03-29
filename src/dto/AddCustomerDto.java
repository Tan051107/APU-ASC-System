package dto;

import models.User;

import java.util.List;

public class AddCustomerDto {
    private User user;
    private List<AddCarDto> carsToBeAdded;

    public AddCustomerDto(User user, List<AddCarDto> carsToBeAdded) {
        this.user = user;
        this.carsToBeAdded = carsToBeAdded;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<AddCarDto> getCarsToBeAdded() {
        return carsToBeAdded;
    }

    public void setCarsToBeAdded(List<AddCarDto> carsToBeAdded) {
        this.carsToBeAdded = carsToBeAdded;
    }
}
