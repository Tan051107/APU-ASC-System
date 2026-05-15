package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Services extends BaseModel{
    private String name;
    private double price;
    private String details;
    private LocalDate date;
    private LocalTime time;
    private int duration;

    public Services() {
    }

    public Services(String name, double price, String details, int duration) {
        this.name = name;
        this.price = price;
        this.details = details;
        this.duration = duration;
    }

    public Services(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String name, double price, String details, int duration, LocalDate date, LocalTime time) {
        super(id, createdAt,updatedAt);
        this.name = name;
        this.price = price;
        this.details = details;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    // Getter and Setter

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public String getDetails(){
        return details;
    }

    public void setDetails(String details){
        this.details = details;
    }

    /*public LocalDate getDate() {
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
    }*/

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Services{" +
                "id='" + getId() + '\'' + 
                ", serviceName='" + name + '\'' +
                ", servicePrice='" + price + '\'' +
                ", serviceDetails='" + details + '\'' +
                ", serviceDuration='" + duration + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
