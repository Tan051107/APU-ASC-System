package services;

import enums.UserType;
import exceptions.FileCorruptedException;
import mapper.TechnicianMapper;
import models.Technician;
import repositories.CrudRepository;

import java.util.List;

public class TechnicianService {
    private final String USER_FILE = "txt_files/User.txt";
    private final TechnicianMapper technicianMapper = new TechnicianMapper();
    private final CrudRepository<Technician> technicianCrudRepository = new CrudRepository<>(USER_FILE,technicianMapper);

    public List<Technician> getTechnicians() throws FileCorruptedException {
        return technicianCrudRepository.getAll(technician -> technician.getUserType().equals(UserType.TECHNICIAN));
    }
}
