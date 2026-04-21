package models;

import exceptions.GetEntityListException;
import services.AppointmentService;
import services.UserService;

import java.time.LocalDateTime;

public class PaymentRecord extends BaseModel {
    private String appointmentId;
    private double amount;
    private LocalDateTime paymentDateTime;
    private String paymentMethod;
    private boolean hasPaid;
    private String paymentCollectedBy;

    public PaymentRecord(){}

    public PaymentRecord(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String appointmentId, double amount, LocalDateTime paymentDateTime, String paymentMethod, boolean hasPaid, String paymentCollectedBy) {
        super(id, createdAt, updatedAt);
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.paymentDateTime = paymentDateTime;
        this.paymentMethod = paymentMethod;
        this.hasPaid = hasPaid;
        this.paymentCollectedBy = paymentCollectedBy;
    }

    public PaymentRecord(String appointmentId, double amount, LocalDateTime paymentDateTime, String paymentMethod, boolean hasPaid, String paymentCollectedBy) {
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.paymentDateTime = paymentDateTime;
        this.paymentMethod = paymentMethod;
        this.hasPaid = hasPaid;
        this.paymentCollectedBy = paymentCollectedBy;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(LocalDateTime paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isHasPaid() {
        return hasPaid;
    }

    public void setHasPaid(boolean hasPaid) {
        this.hasPaid = hasPaid;
    }

    public String getPaymentCollectedBy() {
        return paymentCollectedBy;
    }

    public void setPaymentCollectedBy(String paymentCollectedBy) {
        this.paymentCollectedBy = paymentCollectedBy;
    }

    public Appointment getAppointment() throws GetEntityListException {
        AppointmentService appointmentService = new AppointmentService();
        return appointmentService.getAppointmentById(appointmentId);
    }

    public User getStaffCollectPayment() throws GetEntityListException {
        UserService userService = new UserService();
        return userService.getUserById(paymentCollectedBy);
    }
}
