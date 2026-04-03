package mapper;

import exceptions.FileCorruptedException;
import mapper.interfaces.Mapper;
import models.Services;

import java.time.LocalDateTime;

public class ServicesMapper implements Mapper<Services>{
    
    @Override
    public Services toObject(String row) throws FileCorruptedException {
        Services services = new Services();
        String[] data = row.split("\\|");
        if(data.length !=6){
            throw new FileCorruptedException("Service file contains extra data or has missing data");
        }
        services.setId(data[0]);
        services.setServiceName(data[1]);
        services.setServicePrice(Double.parseDouble(data[2]));
        services.setServiceDetails(data[3]);
        services.setCreatedAt(LocalDateTime.parse(data[4]));
        services.setUpdatedAt(LocalDateTime.parse(data[5]));
        return services;
    }

    @Override
    public String toString(Services services){
        return String.join("|" , services.getId(),services.getServiceName(),String.valueOf(services.getServicePrice()), services.getServiceDetails(), services.getCreatedAt().toString(),services.getUpdatedAt().toString());
    }
}
