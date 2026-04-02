package mapper;

import enums.FuelType;
import exceptions.FileCorruptedException;
import mapper.interfaces.Mapper;
import models.CustomerCar;

import java.time.LocalDateTime;
import java.util.Arrays;

public class CustomerCarMapper implements Mapper<CustomerCar> {
    @Override
    public  CustomerCar toObject(String row) throws FileCorruptedException {
        String[] data = row.split("\\|");
        CustomerCar customerCar = new CustomerCar();
        if(data.length != 10){
            System.out.println(Arrays.toString(data));
            throw new FileCorruptedException("Car file contains extra data or missing data:"+ data.length);
        }
        customerCar.setId(data[0]);
        customerCar.setCustomerId(data[1]);
        customerCar.setCarModel(data[2]);
        customerCar.setCarBrand(data[3]);
        customerCar.setCarPlate(data[4]);
        customerCar.setMileage(Double.parseDouble(data[5]));
        customerCar.setFuelType(FuelType.fromString(data[6]));
        customerCar.setManufactureYear(Integer.parseInt(data[7]));
        customerCar.setCreatedAt(LocalDateTime.parse(data[8]));
        customerCar.setUpdatedAt(LocalDateTime.parse(data[9]));
        return customerCar;
    }

    @Override
    public String toString(CustomerCar customerCar) {
        return String.join(
                "|",
                customerCar.getId(),
                customerCar.getCustomerId(),
                customerCar.getCarModel(),
                customerCar.getCarBrand(),
                customerCar.getCarPlate(),
                String.valueOf(customerCar.getMileage()),
                customerCar.getFuelType().toString(),
                String.valueOf(customerCar.getManufactureYear()),
                customerCar.getCreatedAt().toString(),
                customerCar.getUpdatedAt().toString()
        );
    }
}
