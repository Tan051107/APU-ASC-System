package models;

import java.time.LocalDateTime;

public class Feedback extends BaseModel {
    private String appointmentId;
    private Integer staffRating;
    private Integer technicianRating;
    private String comment;

    public Feedback() {
        super();
    }

    public Feedback(String appointmentId,Integer staffRating, Integer technicianRating, String comment) {
        super(); // Calls BaseModel's default constructor
        this.appointmentId = appointmentId;
        this.staffRating = staffRating;                
        this.technicianRating = technicianRating;
        this.comment = comment;
    }

    public Feedback(String id, LocalDateTime createdAt, LocalDateTime updatedAt, 
                       String appointmentId, Integer staffRating, Integer technicianRating, String comment) {
        super(id, createdAt, updatedAt); // Passes ID and timestamps up to BaseModel
        this.appointmentId = appointmentId;
        this.staffRating = staffRating;
        this.technicianRating = technicianRating;
        this.comment = comment;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Integer getStaffRating() {
        return staffRating;
    }

    public void setStaffRating(Integer staffRating) {
        this.staffRating = staffRating;
    }

    public Integer getTechnicianRating() {
        return technicianRating;
    }

    public void setTechnicianRating(Integer technicianRating) {
        this.technicianRating = technicianRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id='" + getId() + '\'' + 
                ", appointmentId='" + appointmentId + '\'' +
                ", staffRating='" + staffRating + '\'' +
                ", technicianRating='" + technicianRating + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
