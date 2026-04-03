package utils;

import exceptions.FileCorruptedException;
import models.Customer;
import services.CustomerCarService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVExporter{

    private static final Logger logger = Logger.getLogger(CSVExporter.class.getName());

    public static void exportCustomerData(List<Customer> customers, String filePath) throws IOException, FileCorruptedException {
        CustomerCarService customerCarService = new CustomerCarService();
        try(FileWriter writer = new FileWriter(filePath)){
            writer.append("User Id,Name,Email, Phone Number, Total Registered Cars\n");
            for (Customer customer : customers){
                int carsOwned = customerCarService.getCustomerCars(customer.getId()).size();
                writer.append(String.valueOf(customer.getId())).append(",");
                writer.append(escapeCSV(customer.getName())).append(",");
                writer.append(escapeCSV(customer.getEmail())).append(",");
                writer.append(escapeCSV(customer.getContactNumber())).append(",");
                writer.append(escapeCSV(String.valueOf(carsOwned))).append("\n");
            }
            logger.log(Level.INFO , "Successfully exported customer data");
        }
    }

    private static String escapeCSV(String value){
        if(value.contains(",") || value.contains("\"")){
            value = value.replace("\"", "\"\"");
            value = "\"" + value + "\"";
        }
        return value;
    }
}
