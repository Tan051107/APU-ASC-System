package models;

import enums.FuelType;

import java.time.LocalDateTime;

public class CustomerCar extends BaseModel{

    private String customerId;
    private String carPlate;
    private String carModel;
    private String carBrand;
    private int manufactureYear;
    private double mileage;
    private FuelType fuelType;

    public  CustomerCar(){}

    public CustomerCar(String customerId, String carPlate, String carModel, String carBrand, int manufactureYear, double mileage, FuelType fuelType) {
        this.customerId = customerId;
        this.carPlate = carPlate;
        this.carModel = carModel;
        this.carBrand = carBrand;
        this.manufactureYear = manufactureYear;
        this.mileage = mileage;
        this.fuelType = fuelType;
    }

    public CustomerCar(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String customerId, String carPlate, String carModel, String carBrand, int manufactureYear, double mileage, FuelType fuelType) {
        super(id, createdAt, updatedAt);
        this.customerId = customerId;
        this.carPlate = carPlate;
        this.carModel = carModel;
        this.carBrand = carBrand;
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

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public double getManufactureYear() {
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
