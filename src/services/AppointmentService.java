package services;

import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import exceptions.NotFoundException;
import exceptions.UpdateException;
import mapper.AppointmentMapper;
import models.Appointment;
import models.Technician;
import repositories.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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

    public List<Appointment> getAppointmentsByTechnician(String technicianId) throws FileCorruptedException {
        return appointmentRepository.getAll(appointment -> appointment.getTechnicianId().equalsIgnoreCase(technicianId));
    }

    //TODO If car got appointment within the date and time, don't allow add appointment
    public void addAppointment(Appointment appointment){
    }

    public List<Technician> getAvailableTechnicians(LocalDateTime appointmentDateTime ,int durationInHour) throws FileCorruptedException, NotFoundException, GetEntityListException {
        if(appointmentDateTime.isBefore(LocalDateTime.now())){
            throw new NotFoundException("Please select valid appointment date and time");
        }
        List<Technician> availableTechnicians = new ArrayList<>();
        TechnicianService technicianService = new TechnicianService();
        List<Technician> technicians = technicianService.getTechnicians();
        if(technicians.isEmpty()){
            throw new NotFoundException("Technician list is empty");
        }
        for(Technician technician : technicians){
            String technicianId = technician.getId();
            List<Appointment> assignedAppointments = getAppointmentsByTechnician(technicianId);
            if(assignedAppointments.isEmpty()){
                availableTechnicians.add(technician);
                continue;
            }
            if(technicianIsFree(assignedAppointments,appointmentDateTime,durationInHour)){
                availableTechnicians.add(technician);
            }
        }
        if(availableTechnicians.isEmpty()){
            throw new NotFoundException("No available technician");
        }
        return availableTechnicians;

    }

    private boolean technicianIsFree(List<Appointment> assignedAppointments , LocalDateTime newAppointmentDateTime , int newAppointmentDurationInHour) throws GetEntityListException {
        ServicesService servicesService = new ServicesService();
        for(Appointment assignedAppointment : assignedAppointments){
            LocalTime assignedAppointmentStartTime = assignedAppointment.getTime();
            int appointmentDuration = servicesService.getServicesById(assignedAppointment.getServiceId()).getServiceDuration();
            LocalTime assignedAppointmentEndTime = assignedAppointmentStartTime.plusHours(appointmentDuration);

            LocalDate newAppointmentDate = newAppointmentDateTime.toLocalDate();
            LocalDate assignedAppointmentDate = assignedAppointment.getDate();

            if(!assignedAppointmentDate.equals(newAppointmentDate)){
                continue;
            }

            LocalTime newAppointmentStartTime = newAppointmentDateTime.toLocalTime();
            LocalTime newAppointmentEndTime = newAppointmentStartTime.plusHours(newAppointmentDurationInHour);
            if(newAppointmentStartTime.isBefore(assignedAppointmentEndTime) &&
                    newAppointmentEndTime.isAfter(assignedAppointmentStartTime)){
                return false;
            }
        }
        return true;
    }



}
