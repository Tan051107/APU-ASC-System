
import exceptions.FileCorruptedException;
import models.CustomerCar;
import services.CustomerCarService;

import java.util.Arrays;
import java.util.List;

public class TestMain {
    public static void main(String[] args) {
        CustomerCarService customerCarService = new CustomerCarService();
        try {
            List<CustomerCar> cars = customerCarService.getCars();
            System.out.println(cars.toString());
        } catch (FileCorruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
