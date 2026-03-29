package mapper;

import enums.UserType;
import exceptions.FileCorruptedException;
import mapper.interfaces.Mapper;
import models.Customer;

import java.time.LocalDateTime;

public class CustomerMapper implements Mapper<Customer> {
    @Override
    public Customer toObject(String row) throws FileCorruptedException {
        Customer customer = new Customer();
        String[] data = row.split("\\|");
        if(data.length !=8){
            throw new FileCorruptedException("User file contains extra data or has missing data");
        }
        customer.setId(data[0]);
        customer.setName(data[1]);
        customer.setEmail(data[2]);
        customer.setPassword(data[3]);
        customer.setContactNumber(data[4]);
        customer.setUserType(UserType.fromString(data[5]));
        customer.setCreatedAt(LocalDateTime.parse(data[6]));
        customer.setUpdatedAt(LocalDateTime.parse(data[7]));
        return customer;
    }

    @Override
    public String toString(Customer customer){
        return String.join("|" , customer.getId(),customer.getName(),customer.getEmail(), customer.getPassword(),customer.getContactNumber(),customer.getUserType().getDisplayUserType() , customer.getCreatedAt().toString(),customer.getUpdatedAt().toString());
    }
}
