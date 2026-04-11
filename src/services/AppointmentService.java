package services;

import java.time.DayOfWeek;
import java.util.List;

import enums.AppointmentStatus;
import exceptions.*;
import mapper.AppointmentMapper;
import models.*;
import repositories.CrudRepository;
import utils.RandomIdGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.function.Predicate;

public class AppointmentService {
    private final String APPOINTMENT_FILE = "txt_files/Appointment.txt";
    private final AppointmentMapper appointmentMapper = new AppointmentMapper();
    private final CrudRepository<Appointment> appointmentRepository = new CrudRepository<>(APPOINTMENT_FILE, appointmentMapper);
    private final CustomerCarService customerCarService = new CustomerCarService();
    private final TechnicianService technicianService = new TechnicianService();

    public List<Appointment> getAllAppointments() throws GetEntityListException {
        try {
            return appointmentRepository.getAll();
        } catch (FileCorruptedException e) {
            throw new GetEntityListException(e.getMessage());
        }
    }

    public List<Appointment> getAppointments(Predicate<Appointment> filter) throws FileCorruptedException {
        return appointmentRepository.getAll(filter);
    }

    public Appointment getAppointmentById(String id) throws GetEntityListException {
        try {
            return appointmentRepository.getOne(id);
        } catch (FileCorruptedException e) {
            throw new GetEntityListException(e.getMessage());
        }
    }


    //TODO Don't allow create appointment if the car already has an assigned appointment that not completed yet
    public void createAppointment(Appointment appointmentToAdd) throws Exception {
        PaymentRecordService paymentRecordService = new PaymentRecordService();
        try {
            // Generate a unique ID and assign it before saving
            String appointmentId = generateId();
            appointmentToAdd.setId(appointmentId);
            LocalDateTime chosenAppointmentDateTime = LocalDateTime.of(appointmentToAdd.getDate(),appointmentToAdd.getTime());
            if(isNotValidAppointmentDateTime(chosenAppointmentDateTime)){
                throw new BusinessRuleException("Appointment date chosen must be within 14 days from now");
            }
            if(carHasClashAppointment(appointmentToAdd, "")){
                throw new BusinessRuleException("Car already has an appointment during the date and time chosen");
            }
            if(carHasNotCompletedAppointment(appointmentToAdd, "")){
                throw new BusinessRuleException("Car already has an upcoming appointment");
            }
            if(isNotOperationHour(appointmentToAdd)){
                throw new BusinessRuleException("Operating hours are from 9 AM to 6 PM, Monday to Friday");
            }
            PaymentRecord paymentRecordToCreate = createPaymentRecordForAppointment(appointmentToAdd);
            appointmentRepository.create(appointmentToAdd);
            paymentRecordService.addPaymentRecord(paymentRecordToCreate);
        } catch (Exception e) {
            throw new Exception("Failed to create appointment: " + e.getMessage());
        }
    }

    public void updateAppointment(Appointment appointmentToUpdate) throws FileCorruptedException, NotFoundException, GetEntityListException, BusinessRuleException {
        LocalDateTime chosenAppointmentDateTime = LocalDateTime.of(appointmentToUpdate.getDate(),appointmentToUpdate.getTime());
        String appointmentToUpdateId = appointmentToUpdate.getId();
        if(isNotValidAppointmentDateTime(chosenAppointmentDateTime)){
            throw new BusinessRuleException("Appointment date chosen must be within 14 days from now");
        }
        if(carHasClashAppointment(appointmentToUpdate, appointmentToUpdateId)){
            throw new BusinessRuleException("Car already has an appointment during the date and time chosen");
        }
        if(carHasNotCompletedAppointment(appointmentToUpdate,appointmentToUpdateId)){
            throw new BusinessRuleException("Car already has an upcoming appointment");
        }
        if(isNotOperationHour(appointmentToUpdate)){
            throw new BusinessRuleException("Operating hours are from 9 AM to 6 PM, Monday to Friday");
        }
        appointmentRepository.update(appointmentToUpdate);
    }

    public void cancelAppointment(Appointment appointmentToCancel) throws FileCorruptedException, NotFoundException, BusinessRuleException, DeleteException, GetEntityListException {
        PaymentRecordService paymentRecordService = new PaymentRecordService();
        appointmentToCancel.setStatusService(AppointmentStatus.CANCELLED);
        appointmentRepository.update(appointmentToCancel);
        PaymentRecord paymentRecord = paymentRecordService.getPaymentRecordByAppointment(appointmentToCancel.getId());
        paymentRecordService.deletePaymentRecord(paymentRecord);
    }

    public List<Appointment> getAppointments() throws FileCorruptedException {
        return appointmentRepository.getAll();
    }

    public List<Appointment> getAppointmentsByTechnician(String technicianId) throws FileCorruptedException {
        return appointmentRepository.getAll(appointment -> appointment.getTechnicianId().equalsIgnoreCase(technicianId));
    }

    public List<Appointment> searchAppointment(String keyword , AppointmentStatus appointmentStatus , String serviceTypeSelectionFilter) throws FileCorruptedException {
        Predicate<Appointment> appointmentPredicate = appointment -> true;
        if(!keyword.isEmpty()){
            String keywordLowerCase = keyword.toLowerCase();
            CustomerService customerService = new CustomerService();
            List<String> customerIdsFound = customerService.getCustomers(customer -> customer.getName().toLowerCase().contains(keywordLowerCase)).stream().map(Customer::getId).toList();
            List<String> carIdsFound = customerCarService.customerCars(customerCar->customerCar.getCarPlate().toLowerCase().contains(keywordLowerCase)).stream().map(CustomerCar::getId).toList();
            List<String> technicianIdsFound = technicianService.getTechnicians(technician -> technician.getName().toLowerCase().contains(keywordLowerCase)).stream().map(Technician::getId).toList();
            Predicate<Appointment> keywordPredicate = appointment -> appointment.getId().toLowerCase().contains(keywordLowerCase);
            if(!customerIdsFound.isEmpty()){
                keywordPredicate = keywordPredicate.or(appointment -> customerIdsFound.contains(appointment.getCustomerId()));
            }
            if(!carIdsFound.isEmpty()){
                keywordPredicate =  keywordPredicate.or(appointment -> carIdsFound.contains(appointment.getCarId()));
            }
            if(!technicianIdsFound.isEmpty()){
                keywordPredicate = keywordPredicate.or(appointment -> technicianIdsFound.contains(appointment.getTechnicianId()));
            }
            appointmentPredicate = appointmentPredicate.and(keywordPredicate);
        }
        if(appointmentStatus !=null){
            appointmentPredicate = appointmentPredicate.and(appointment -> appointment.getStatusService().equals(appointmentStatus));
        }
        if(!serviceTypeSelectionFilter.isEmpty()){
            appointmentPredicate = appointmentPredicate.and(appointment -> appointment.getServiceId().equalsIgnoreCase(serviceTypeSelectionFilter));
        }
        return getAppointments(appointmentPredicate);
    }

    public List<Technician> getAvailableTechnicians(LocalDateTime appointmentDateTime ,int durationInHour, String appointmentToIgnore) throws FileCorruptedException, NotFoundException, GetEntityListException, BusinessRuleException {
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
            List<Appointment> assignedAppointments = getAppointmentsByTechnician(technicianId).stream()
                    .filter(appointment -> !appointment.getId().equalsIgnoreCase(appointmentToIgnore))
                    .toList(); //Remove appointment to ignore from checking
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
            if(!assignedAppointment.getStatusService().equals(AppointmentStatus.ASSIGNED)){
                continue;
            } //Skip appointments that are cancelled/completed - No need check
            LocalTime assignedAppointmentStartTime = assignedAppointment.getTime();
            int appointmentDuration = assignedAppointment.getService().getDuration();
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

    //Validate whether the appointment date time is within operation hour
    private boolean isNotOperationHour(Appointment appointment) throws GetEntityListException {
        final LocalTime operationStartTime = LocalTime.of(9,0);
        final LocalTime operationEndTime = LocalTime.of(18,0);
        LocalDate appointmentDate = appointment.getDate();
        LocalTime appointmentStartTime = appointment.getTime();
        int appointmentDuration = appointment.getService().getDuration();
        LocalTime appointmentEndTime = appointmentStartTime.plusHours(appointmentDuration);
        DayOfWeek appointmentDay = appointmentDate.getDayOfWeek();
        if(appointmentDay == DayOfWeek.SATURDAY || appointmentDay == DayOfWeek.SUNDAY){
            return true;
        }
        return appointmentStartTime.isBefore(operationStartTime) || appointmentEndTime.isAfter(operationEndTime);
    }

    private boolean carHasClashAppointment(Appointment newAppointment , String appointmentToIgnore) throws FileCorruptedException, GetEntityListException {
        String appointmentCar = newAppointment.getCarId();
        List<Appointment> carAppointments = appointmentRepository.getAll(appointment -> appointment.getCarId().equalsIgnoreCase(appointmentCar) && !appointment.getId().equalsIgnoreCase(appointmentToIgnore) && appointment.getStatusService().equals(AppointmentStatus.ASSIGNED)); //Remove appointment to ignore from checking
        if(carAppointments.isEmpty()){
            return false;
        }

        int newAppointmentDuration = newAppointment.getService().getDuration();
        LocalDateTime newAppointmentStartDateTime = LocalDateTime.of(newAppointment.getDate(),newAppointment.getTime());
        LocalDateTime newAppointmentEndDateTime = newAppointmentStartDateTime.plusHours(newAppointmentDuration);
        for(Appointment appointment : carAppointments){
            int existingAppointmentDuration = appointment.getService().getDuration();
            LocalDateTime existingAppointmentStartDateTime = LocalDateTime.of(appointment.getDate(),appointment.getTime());
            LocalDateTime existingAppointmentEndDateTime = existingAppointmentStartDateTime.plusHours(existingAppointmentDuration);
            if(newAppointmentEndDateTime.isAfter(existingAppointmentStartDateTime) && newAppointmentStartDateTime.isBefore(existingAppointmentEndDateTime)){
                return true;
            }
        }
        return false;
    }

    private boolean carHasNotCompletedAppointment(Appointment appointmentToSave, String appointmentToIgnore) throws FileCorruptedException, GetEntityListException {
        List<Appointment> appointments = getAppointments(appointment -> appointment.getCarId().equalsIgnoreCase(appointmentToSave.getCarId()) && appointment.getStatusService().equals(AppointmentStatus.ASSIGNED) && !appointment.getId().equalsIgnoreCase(appointmentToIgnore));
        if(appointments.isEmpty()){
            return false;
        }
        int appointmentToSaveDuration = appointmentToSave.getService().getDuration();
        LocalDateTime appointmentToSaveEndTime = LocalDateTime.of(appointmentToSave.getDate(),appointmentToSave.getTime()).plusHours(appointmentToSaveDuration);
        for(Appointment appointment :appointments){
            int existingAppointmentDuration = appointment.getService().getDuration();
            LocalDateTime existingAppointmentEndTime = LocalDateTime.of(appointment.getDate(),appointment.getTime()).plusHours(existingAppointmentDuration);
            if(existingAppointmentEndTime.isAfter(appointmentToSaveEndTime)){
                return true;
            }
        }
        return false;
    }

    private PaymentRecord createPaymentRecordForAppointment(Appointment appointment) throws GetEntityListException {
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setAppointmentId(appointment.getId());
        double servicePrice = appointment.getService().getPrice();
        paymentRecord.setAmount(servicePrice);
        return paymentRecord;
    }

}
