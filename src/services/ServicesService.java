package services;

import enums.NotificationTargetType;
import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import exceptions.NotFoundException;
import exceptions.UpdateException;
import mapper.ServicesMapper;
import models.Notification;
import models.Services;
import repositories.CrudRepository;

import java.io.IOException;
import java.util.List;

public class ServicesService {
    private final String SERVICES_FILE = "txt_files/Services.txt";
    private final ServicesMapper servicesMapper = new ServicesMapper();
    private final CrudRepository<Services> servicesRepository = new CrudRepository<>(SERVICES_FILE , servicesMapper);

    public List<Services> getServices() throws GetEntityListException {
        try{
            return servicesRepository.getAll();
        } catch (FileCorruptedException e) {
            throw new GetEntityListException(e.getMessage());
        }
    }

    public Services getServicesById(String id) throws GetEntityListException {
        try{
            return servicesRepository.getOne(id);
        } catch (FileCorruptedException e) {
            throw new GetEntityListException(e.getMessage());
        }
    }

    public void updateService(Services serviceToUpdate) throws FileCorruptedException, NotFoundException, GetEntityListException, UpdateException, IOException {

        servicesRepository.update(serviceToUpdate);
        createNotification("Service Information Updated" , "Service price is updated to RM " +serviceToUpdate.getPrice());
    }

    private void createNotification(String title , String message) throws IOException {
        NotificationService notificationService = new NotificationService();
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setTargetType(NotificationTargetType.ALL);
        notification.setUserId(null);
        notification.setUserType(null);
        notificationService.addNotification(notification);
    }
}
