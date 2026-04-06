package services;

import java.util.List;

import exceptions.*;
import mapper.AppointmentMapper;
import models.Appointment;
import models.Technician;
import repositories.CrudRepository;
import utils.RandomIdGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class AppointmentService {
    private final String APPOINTMENT_FILE = "txt_files/Appointment.txt";
    private final AppointmentMapper appointmentMapper = new AppointmentMapper();
    private final CrudRepository<Appointment> appointmentRepository = new CrudRepository<>(APPOINTMENT_FILE, appointmentMapper);
    private final ServicesService servicesService = new ServicesService();

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

    //TODO If car got appointment within the date and time, don't allow add appointment
    public void createAppointment(Appointment appointmentToAdd) throws Exception {
        try {
            // Generate a unique ID and assign it before saving
            String appointmentId = generateId();
            appointmentToAdd.setId(appointmentId);
            LocalDateTime chosenAppointmentDateTime = LocalDateTime.of(appointmentToAdd.getDate(),appointmentToAdd.getTime());
            if(isNotValidAppointmentDateTime(chosenAppointmentDateTime)){
                throw new BusinessRuleException("Appointment date chosen must be within 14 days from now");
            }
            if(carHasClashAppointment(appointmentToAdd)){
                throw new BusinessRuleException("Car already has an appointment during the date and time chosen");
            }

            appointmentRepository.create(appointmentToAdd);
        } catch (Exception e) {
            throw new Exception("Failed to create appointment: " + e.getMessage());
        }
    }

    public void updateAppointment(Appointment appointmentToUpdate) throws FileCorruptedException, NotFoundException, GetEntityListException, UpdateException {
        appointmentRepository.update(appointmentToUpdate);
    }

    public List<Appointment> getAppointments() throws FileCorruptedException {
        return appointmentRepository.getAll();
    }

    public List<Appointment> getAppointmentsByTechnician(String technicianId) throws FileCorruptedException {
        return appointmentRepository.getAll(appointment -> appointment.getTechnicianId().equalsIgnoreCase(technicianId));
    }

    public List<Technician> getAvailableTechnicians(LocalDateTime appointmentDateTime ,int durationInHour) throws FileCorruptedException, NotFoundException, GetEntityListException, BusinessRuleException {
        if(isNotValidAppointmentDateTime(appointmentDateTime)){
            throw new BusinessRuleException("Appointment date chosen must be within 14 days from now");
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

    //Validate appointment must be within 14 days from now
    private boolean isNotValidAppointmentDateTime(LocalDateTime chosenAppointmentDateTime){
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime maxAllowedAppointmentDateTime = currentDateTime.plusWeeks(2);
        return chosenAppointmentDateTime.isBefore(currentDateTime) || chosenAppointmentDateTime.isAfter(maxAllowedAppointmentDateTime);
    }

    private boolean carHasClashAppointment(Appointment newAppointment) throws FileCorruptedException, GetEntityListException {
        String appointmentCar = newAppointment.getCarId();
        List<Appointment> carAppointments = appointmentRepository.getAll(appointment -> appointment.getCarId().equalsIgnoreCase(appointmentCar));
        if(carAppointments.isEmpty()){
            return false;
        }

        int newAppointmentDuration = servicesService.getServicesById(newAppointment.getServiceId()).getServiceDuration();
        LocalDateTime newAppointmentStartDateTime = LocalDateTime.of(newAppointment.getDate(),newAppointment.getTime());
        LocalDateTime newAppointmentEndDateTime = newAppointmentStartDateTime.plusHours(newAppointmentDuration);
        for(Appointment appointment : carAppointments){
            int existingAppointmentDuration = servicesService.getServicesById(newAppointment.getServiceId()).getServiceDuration();
            LocalDateTime existingAppointmentStartDateTime = LocalDateTime.of(appointment.getDate(),appointment.getTime());
            LocalDateTime existingAppointmentEndDateTime = newAppointmentStartDateTime.plusHours(existingAppointmentDuration);
            if(newAppointmentEndDateTime.isAfter(existingAppointmentStartDateTime) && newAppointmentStartDateTime.isBefore(existingAppointmentEndDateTime)){
                return true;
            }
        }
        return false;
    }

}
