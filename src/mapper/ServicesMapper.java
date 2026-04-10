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
        if(data.length !=7){
            throw new FileCorruptedException("Service file contains extra data or has missing data");
        }
        services.setId(data[0]);
        services.setName(data[1]);
        services.setPrice(Double.parseDouble(data[2]));
        services.setDetails(data[3]);
        services.setDuration(Integer.parseInt(data[4]));
        services.setCreatedAt(LocalDateTime.parse(data[5]));
        services.setUpdatedAt(LocalDateTime.parse(data[6]));
        return services;
    }

    @Override
    public String toString(Services services){
        String formattedPrice = String.format("%.2f", services.getPrice());
        return String.join("|" , services.getId(),services.getName(),formattedPrice, services.getDetails(), String.valueOf(services.getDuration()) ,services.getCreatedAt().toString(),services.getUpdatedAt().toString());
    }
}
