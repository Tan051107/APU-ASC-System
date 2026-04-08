package mapper;

import enums.UserType;
import exceptions.FileCorruptedException;
import mapper.interfaces.Mapper;
import models.Technician;

import java.time.LocalDateTime;

public class TechnicianMapper implements Mapper<Technician> {
    @Override
    public Technician toObject(String row) throws FileCorruptedException {
        Technician technician = new Technician();
        String[] data = row.split("\\|");
        if(data.length !=8){
            throw new FileCorruptedException("User file contains extra data or has missing data");
        }
        technician.setId(data[0]);
        technician.setName(data[1]);
        technician.setEmail(data[2]);
        technician.setPassword(data[3]);
        technician.setContactNumber(data[4]);
        technician.setUserType(UserType.fromString(data[5]));
        technician.setCreatedAt(LocalDateTime.parse(data[6]));
        technician.setUpdatedAt(LocalDateTime.parse(data[7]));
        return technician;
    }

    @Override
    public String toString(Technician technician) {
        return String.join("|" , technician.getId(),technician.getName(),technician.getEmail(), technician.getPassword(),technician.getContactNumber(),technician.getUserType().getDisplayUserType() , technician.getCreatedAt().toString(),technician.getUpdatedAt().toString());
    }
}
