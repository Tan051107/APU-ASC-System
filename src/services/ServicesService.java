package services;

import enums.NotificationTargetType;
import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import exceptions.NotFoundException;
import exceptions.UpdateException;
import mapper.ServicesMapper;
import models.Notification;
import models.Services;
import repositories.ServicesRepository;

import java.io.IOException;
import java.util.List;

public class ServicesService {
    private final String SERVICES_FILE = "txt_files/Services.txt";
    private final ServicesMapper servicesMapper = new ServicesMapper();
    private final ServicesRepository<Services> servicesRepository = new ServicesRepository<>(SERVICES_FILE , servicesMapper);
    //private final Logger logger = Logger.getLogger(UserService.class.getName());

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

    public Services getServicesByName(String name) throws GetEntityListException {
        try{
            return servicesRepository.getAll(services -> services.getName().equalsIgnoreCase(name)).getFirst();
        }
        catch (FileCorruptedException e){
            throw new GetEntityListException(e.getMessage());
        }
    }

    public void updateService(Services serviceToUpdate) throws FileCorruptedException, NotFoundException, GetEntityListException, UpdateException, IOException {

        servicesRepository.update(serviceToUpdate);
        createNotification("Service Information Updated" , "Service price is updated to RM " +serviceToUpdate.getPrice());
    }

    public Services findOne(String serviceId) throws FileCorruptedException {
        return servicesRepository.getOne(serviceId);
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
