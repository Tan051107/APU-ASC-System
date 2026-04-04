package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Services extends BaseModel{
    private String serviceName;
    private double servicePrice;
    private String serviceDetails;
    private LocalDate date;
    private LocalTime time;

    public Services() {
    }

    public Services(String serviceName, double servicePrice, String serviceDetails) {
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.serviceDetails = serviceDetails;
    }

    public Services(String id, LocalDateTime createdAt, LocalDateTime updatedAt,String serviceId,String serviceName, double servicePrice, String serviceDetails, LocalDate date, LocalTime time) {
        super(id, createdAt,updatedAt);
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.serviceDetails = serviceDetails;
        this.date = date;
        this.time = time;
    }

    // Getter and Setter

    public String getServiceName(){
        return serviceName;
    }

    public void setServiceName(String serviceName){
        this.serviceName = serviceName;
    }
    public double getServicePrice(){
        return servicePrice;
    }

    public void setServicePrice(double servicePrice){
        this.servicePrice = servicePrice;
    }

    public String getServiceDetails(){
        return serviceDetails;
    }

    public void setServiceDetails(String serviceDetails){
        this.serviceDetails = serviceDetails;
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

    @Override
    public String toString() {
        return "Services{" +
                "id='" + getId() + '\'' + 
                ", serviceName='" + serviceName + '\'' +
                ", servicePrice='" + servicePrice + '\'' +
                ", serviceDetails='" + serviceDetails + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
