package utils.exporters;

import exceptions.FileCorruptedException;
import models.Customer;
import services.CustomerCarService;
import utils.exporters.interfaces.CsvExporter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CustomerCsvExporter implements CsvExporter<Customer> {

    @Override
    public void exportData(List<Customer> data, String filePath) throws FileCorruptedException, IOException {
        CustomerCarService customerCarService = new CustomerCarService();
        try(FileWriter writer = new FileWriter(filePath)){
            writer.append("User Id,Name,Email, Phone Number, Total Registered Cars\n");
            for (Customer customer : data){
                int carsOwned = customerCarService.getCustomerCars(customer.getId()).size();
                writer.append(String.valueOf(customer.getId())).append(",");
                writer.append(escapeCSV(customer.getName())).append(",");
                writer.append(escapeCSV(customer.getEmail())).append(",");
                writer.append(escapeCSV(customer.getContactNumber())).append(",");
                writer.append(escapeCSV(String.valueOf(carsOwned))).append("\n");
            }
        }
    }
}
