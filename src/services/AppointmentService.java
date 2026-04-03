package services;

import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import exceptions.NotFoundException;
import exceptions.UpdateException;
import mapper.AppointmentMapper;
import models.Appointment;
import repositories.CrudRepository;

public class AppointmentService {
    private final String USER_FILE = "txt_files/Appointment.txt";
    private final AppointmentMapper appointmentMapper = new AppointmentMapper();
    private final CrudRepository<Appointment> appointmentRepository = new CrudRepository<>(USER_FILE , appointmentMapper);

    public void updateAppointment(Appointment appointmentToUpdate) throws FileCorruptedException, NotFoundException, GetEntityListException, UpdateException {
        appointmentRepository.update(appointmentToUpdate);
    }


}
