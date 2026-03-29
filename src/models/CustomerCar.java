package models;

import java.time.LocalDateTime;

public class CustomerCar extends BaseModel{

    private String carPlate;
    private String carModel;
    private String customerId;

    public CustomerCar(){}

    public CustomerCar(String carPlate, String carModel, String customerId) {
        this.carPlate = carPlate;
        this.carModel = carModel;
        this.customerId = customerId;
    }

    public CustomerCar(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String carPlate, String carModel, String customerId) {
        super(id, createdAt, updatedAt);
        this.carPlate = carPlate;
        this.carModel = carModel;
        this.customerId = customerId;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
