package models;

import enums.FuelType;

import java.time.LocalDateTime;

public class CustomerCar extends BaseModel{

    private String customerId;
    private String carPlate;
    private String model;
    private String brand;
    private int manufactureYear;
    private double mileage;
    private FuelType fuelType;

    public  CustomerCar(){}

    public CustomerCar(String customerId, String carPlate, String model, String brand, int manufactureYear, double mileage, FuelType fuelType) {
        this.customerId = customerId;
        this.carPlate = carPlate;
        this.model = model;
        this.brand = brand;
        this.manufactureYear = manufactureYear;
        this.mileage = mileage;
        this.fuelType = fuelType;
    }

    public CustomerCar(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String customerId, String carPlate, String model, String brand, int manufactureYear, double mileage, FuelType fuelType) {
        super(id, createdAt, updatedAt);
        this.customerId = customerId;
        this.carPlate = carPlate;
        this.model = model;
        this.brand = brand;
        this.manufactureYear = manufactureYear;
        this.mileage = mileage;
        this.fuelType = fuelType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(int manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }
}
