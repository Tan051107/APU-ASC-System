package services;

import exceptions.DeleteException;
import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import exceptions.SignUpException;
import mapper.ServicesMapper;
import mapper.UserMapper;
import models.Services;
import models.User;
import repositories.ServicesRepository;
import utils.RandomIdGenerator;
import utils.validators.ValidationResult;
import utils.validators.Validator;

import java.util.List;
import java.util.logging.Logger;

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
            return servicesRepository.getAll(services -> services.getServiceName().equalsIgnoreCase(name)).getFirst();
        }
        catch (FileCorruptedException e){
            throw new GetEntityListException(e.getMessage());
        }
    }
}
