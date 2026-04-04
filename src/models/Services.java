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
    private int serviceDuration;

    public Services() {
    }

    public Services(String serviceName, double servicePrice, String serviceDetails , int duration) {
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.serviceDetails = serviceDetails;
        this.serviceDuration = duration;
    }

    public Services(String id, LocalDateTime createdAt, LocalDateTime updatedAt,String serviceName, double servicePrice, String serviceDetails, int duration,LocalDate date, LocalTime time) {
        super(id, createdAt,updatedAt);
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.serviceDetails = serviceDetails;
        this.date = date;
        this.time = time;
        this.serviceDuration = duration;
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

    public int getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(int serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    @Override
    public String toString() {
        return "Services{" +
                "id='" + getId() + '\'' + 
                ", serviceName='" + serviceName + '\'' +
                ", servicePrice='" + servicePrice + '\'' +
                ", serviceDetails='" + serviceDetails + '\'' +
                ", serviceDuration='" + serviceDuration + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
