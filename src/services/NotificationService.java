package services;

import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import mapper.NotificationMapper;
import models.Notification;
import models.User;
import repositories.CrudRepository;
import utils.RandomIdGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private final String NOTIFICATION_FILE = "txt_files/Notification.txt";
    private final NotificationMapper notificationMapper = new NotificationMapper();
    private final CrudRepository<Notification> notificationCrudRepository = new CrudRepository<>(NOTIFICATION_FILE,notificationMapper);

    public void addNotification(Notification notification) throws IOException {
        String notificationId = generateId();
        notification.setId(notificationId);
        notificationCrudRepository.create(notification);
    }

    public Notification getNotification(String notificationId) throws FileCorruptedException {
        return notificationCrudRepository.getOne(notificationId);
    }

    public List<Notification> getNotifications(String userId) throws FileCorruptedException, GetEntityListException {
        UserService userService = new UserService();
        User user = userService.getUserById(userId);
        List<Notification> allNotifications = notificationCrudRepository.getAll();
        List<Notification> userNotifications = new ArrayList<>();
        for(Notification notification : allNotifications){
            switch (notification.getTargetType()){
                case ALL -> userNotifications.add(notification);
                case ROLE -> {
                    if(notification.getUserType().equals(user.getUserType())){
                        userNotifications.add(notification);
                    }
                }
                case USER -> {
                    if(notification.getUserId().equals(userId)){
                        userNotifications.add(notification);
                    }
                }
            }
        }
        return userNotifications;
    }

    private String generateId(){
        while (true){
            String id = RandomIdGenerator.generateId("N" ,5);
            try {
                Notification notification = getNotification(id);
                if(notification == null){
                    return id;
                }
            } catch (FileCorruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
