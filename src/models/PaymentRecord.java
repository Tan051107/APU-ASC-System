package models;

import java.time.LocalDateTime;

public class PaymentRecord extends BaseModel {
    private String appointmentId;
    private double amount;
    private LocalDateTime paymentDateTime;
    private String paymentMethod;  //TODO Need??
    private boolean hasPaid;
    private String issuedBy;

    public PaymentRecord(String appointmentId, double amount, LocalDateTime paymentDateTime, String paymentMethod, boolean hasPaid, String issuedBy) {
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.paymentDateTime = paymentDateTime;
        this.paymentMethod = paymentMethod;
        this.hasPaid = hasPaid;
        this.issuedBy = issuedBy;
    }

    public PaymentRecord(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String appointmentId, double amount, LocalDateTime paymentDateTime, String paymentMethod, boolean hasPaid, String issuedBy) {
        super(id, createdAt, updatedAt);
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.paymentDateTime = paymentDateTime;
        this.paymentMethod = paymentMethod;
        this.hasPaid = hasPaid;
        this.issuedBy = issuedBy;
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

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }
}
