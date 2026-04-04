package services;

import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import exceptions.NotFoundException;
import exceptions.UpdateException;
import mapper.AppointmentMapper;
import models.Appointment;
import repositories.CrudRepository;

import java.util.List;

public class AppointmentService {
    private final String APPOINTMENT_FILE = "txt_files/Appointment.txt";
    private final AppointmentMapper appointmentMapper = new AppointmentMapper();
    private final CrudRepository<Appointment> appointmentRepository = new CrudRepository<>(APPOINTMENT_FILE, appointmentMapper);

    public void updateAppointment(Appointment appointmentToUpdate) throws FileCorruptedException, NotFoundException, GetEntityListException, UpdateException {
        appointmentRepository.update(appointmentToUpdate);
    }

    public List<Appointment> getAppointments() throws FileCorruptedException {
        return appointmentRepository.getAll();
    }


}
