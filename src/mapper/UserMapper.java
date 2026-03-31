package mapper;

import enums.UserType;
import exceptions.FileCorruptedException;
import mapper.interfaces.Mapper;
import models.User;

import java.time.LocalDateTime;

public class UserMapper implements Mapper<User> {


    @Override
    public User toObject(String row) throws FileCorruptedException {
        User user = new User();
        String[] data = row.split("\\|");
        if(data.length !=8){
            throw new FileCorruptedException("User file contains extra data or has missing data");
        }
        user.setId(data[0]);
        user.setName(data[1]);
        user.setEmail(data[2]);
        user.setPassword(data[3]);
        user.setContactNumber(data[4]);
        user.setUserType(UserType.fromString(data[5]));
        user.setCreatedAt(LocalDateTime.parse(data[6]));
        user.setUpdatedAt(LocalDateTime.parse(data[7]));
        return user;
    }

    @Override
    public String toString(User user){
        return String.join("|" , user.getId(),user.getName(),user.getEmail(), user.getPassword(),user.getContactNumber(),user.getUserType().getDisplayUserType() , user.getCreatedAt().toString(),user.getUpdatedAt().toString());
    }
}
