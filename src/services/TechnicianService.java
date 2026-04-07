package services;

import enums.UserType;
import exceptions.FileCorruptedException;
import mapper.TechnicianMapper;
import models.Technician;
import repositories.CrudRepository;

import java.util.List;
import java.util.function.Predicate;

public class TechnicianService {
    private final String USER_FILE = "txt_files/User.txt";
    private final TechnicianMapper technicianMapper = new TechnicianMapper();
    private final CrudRepository<Technician> technicianCrudRepository = new CrudRepository<>(USER_FILE,technicianMapper);

    public List<Technician> getTechnicians() throws FileCorruptedException {
        return technicianCrudRepository.getAll(technician -> technician.getUserType().equals(UserType.TECHNICIAN));
    }

    public List<Technician> getTechnicians(Predicate<Technician> filter) throws FileCorruptedException {
        return technicianCrudRepository.getAll(filter);
    }

    public Technician getTechnicianById(String technicianId) throws FileCorruptedException {
        return technicianCrudRepository.getOne(technicianId);
    }
}
