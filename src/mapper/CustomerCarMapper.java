package mapper;

import exceptions.FileCorruptedException;
import mapper.interfaces.Mapper;
import models.CustomerCar;

import java.time.LocalDateTime;

public class CustomerCarMapper implements Mapper<CustomerCar> {
    @Override
    public CustomerCar toObject(String row) throws FileCorruptedException {
        String[] data = row.split("\\|");
        CustomerCar customerCar = new CustomerCar();
        if(data.length != 6){
            throw new FileCorruptedException("Car file contains extra data or missing data:"+ data.length);
        }
        customerCar.setId(data[0]);
        customerCar.setCustomerId(data[1]);
        customerCar.setCarModel(data[2]);
        customerCar.setCarPlate(data[3]);
        customerCar.setCreatedAt(LocalDateTime.parse(data[4]));
        customerCar.setUpdatedAt(LocalDateTime.parse(data[5]));
        return customerCar;
    }

    @Override
    public String toString(CustomerCar customerCar) {
        return String.join((CharSequence) "|", customerCar.getId(),customerCar.getCustomerId(),customerCar.getCarModel(),customerCar.getCarPlate(),customerCar.getCreatedAt().toString(),customerCar.getUpdatedAt().toString());
    }
}
