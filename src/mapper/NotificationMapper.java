package mapper;

import enums.NotificationTargetType;
import enums.UserType;
import exceptions.FileCorruptedException;
import mapper.interfaces.Mapper;
import models.Notification;

import java.time.LocalDateTime;

public class NotificationMapper implements Mapper<Notification> {
    @Override
    public Notification toObject(String row) throws FileCorruptedException {
        String[] data = row.split("\\|");
        if(data.length != 8){
            throw new FileCorruptedException("Notification file contains extra or missing data");
        }
        NotificationTargetType notificationTargetType = NotificationTargetType.fromString(data[3]);
        Notification notification = new Notification();
        notification.setId(data[0]);
        notification.setTitle(data[1]);
        notification.setMessage(data[2]);
        notification.setTargetType(notificationTargetType);
        notification.setCreatedAt(LocalDateTime.parse(data[6]));
        notification.setUpdatedAt(LocalDateTime.parse(data[7]));
        String userTypeString = data[4];
        String userIdString = data[5];
        switch (notificationTargetType){
            case ALL -> {
                notification.setUserId(null);
                notification.setUserType(null);
            }
            case ROLE -> {
                if(userTypeString.isEmpty()){
                    throw new IllegalArgumentException("User type not found for role specific notification");
                }
                notification.setUserType(UserType.fromString(userTypeString));
                notification.setUserId(null);
            }
            case USER -> {
                if(userIdString.isEmpty()){
                    throw new IllegalArgumentException("User id not found for user specific notification");
                }
                notification.setUserType(null);
                notification.setUserId(userIdString);
            }
            default -> {
                throw new IllegalArgumentException("Target type not found for notification");
            }
        }
        return notification;
    }

    @Override
    public String toString(Notification notification) {
        String userId = "";
        String userType = "";
        switch (notification.getTargetType()){
            case ROLE -> {
                if(notification.getTargetType().equals(NotificationTargetType.ROLE) && notification.getTargetType() == null){
                    throw new IllegalArgumentException("User type not found for role specific notification");
                }
                userType = notification.getUserType().getDisplayUserType();
            }
            case USER -> {
                if(notification.getTargetType().equals(NotificationTargetType.USER) && notification.getUserId().isEmpty()){
                    throw new IllegalArgumentException("User id not found for user specific notification");
                }
                userId = notification.getUserId();
            }
            default -> {
                userId = "";
                userType = "";
            }
        }
        return String.join("|" ,
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getTargetType().toString(),
                userType,
                userId,
                notification.getCreatedAt().toString(),
                notification.getUpdatedAt().toString()
                );
    }
}
