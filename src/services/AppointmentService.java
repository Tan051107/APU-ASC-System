package services;

import java.util.List;

import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import exceptions.NotFoundException;
import exceptions.UpdateException;
import mapper.AppointmentMapper;
import models.Appointment;
import repositories.CrudRepository;
import utils.RandomIdGenerator;

public class AppointmentService {
    private final String APPOINTMENT_FILE = "txt_files/Appointment.txt";
    private final AppointmentMapper appointmentMapper = new AppointmentMapper();
    private final CrudRepository<Appointment> appointmentRepository = new CrudRepository<>(APPOINTMENT_FILE , appointmentMapper);

    public List<Appointment> getAllAppointments() throws GetEntityListException {
        try {
            return appointmentRepository.getAll();
        } catch (FileCorruptedException e) {
            throw new GetEntityListException(e.getMessage());
        }
    }

    public Appointment getAppointmentById(String id) throws GetEntityListException {
        try {
            return appointmentRepository.getOne(id);
        } catch (FileCorruptedException e) {
            throw new GetEntityListException(e.getMessage());
        }
    }

    public void createAppointment(Appointment appointmentToAdd) throws Exception {
        try {
            // Generate a unique ID and assign it before saving
            String appointmentId = generateId();
            appointmentToAdd.setId(appointmentId);
            
            appointmentRepository.create(appointmentToAdd);
        } catch (Exception e) {
            throw new Exception("Failed to create appointment: " + e.getMessage());
        }
    }

    public void updateAppointment(Appointment appointmentToUpdate) throws FileCorruptedException, NotFoundException, GetEntityListException, UpdateException {
        appointmentRepository.update(appointmentToUpdate);
    }

    private String generateId() {
        while (true) {
            String appointmentId = RandomIdGenerator.generateId("APP-", 5); 
            try {
                if (getAppointmentById(appointmentId) == null) {
                    return appointmentId;
                }
            } catch (GetEntityListException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
