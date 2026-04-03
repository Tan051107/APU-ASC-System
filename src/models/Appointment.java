package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import enums.AppointmentStatus;

public class Appointment extends BaseModel{
    private String customerId;
    private String staffId;
    private String technicianId;
    private String serviceId;
    private LocalDate date;
    private LocalTime time;
    private AppointmentStatus statusService;
    private String description;

    public Appointment() {
        super();
    }

    public Appointment(String customerId,String staffId, String technicianId, String serviceId, 
                       LocalDate date, LocalTime time, AppointmentStatus statusService) {
        super(); // Calls BaseModel's default constructor
        this.customerId = customerId;
        this.staffId = staffId;                
        this.technicianId = technicianId;
        this.serviceId = serviceId;
        this.date = date;
        this.time = time;
        this.statusService = statusService;
    }

    public Appointment(String id, LocalDateTime createdAt, LocalDateTime updatedAt, 
                       String customerId, String staffId, String technicianId, String serviceId, 
                       LocalDate date, LocalTime time, AppointmentStatus statusService) {
        super(id, createdAt, updatedAt); // Passes ID and timestamps up to BaseModel
        this.customerId = customerId;
        this.staffId = staffId;
        this.technicianId = technicianId;
        this.serviceId = serviceId;
        this.date = date;
        this.time = time;
        this.statusService = statusService;
    }

    // --- Getters and Setters ---

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId){
        this.staffId = staffId;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public AppointmentStatus getStatusService() {
        return statusService;
    }

    public void setStatusService(AppointmentStatus statusService) {
        this.statusService = statusService;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + getId() + '\'' + 
                ", customerId='" + customerId + '\'' +
                ", staffId='" + staffId + '\'' +
                ", technicianId='" + technicianId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", statusService=" + statusService +
                '}';
    }
}
