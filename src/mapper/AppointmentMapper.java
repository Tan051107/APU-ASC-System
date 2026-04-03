package mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import enums.AppointmentStatus;
import exceptions.FileCorruptedException;
import mapper.interfaces.Mapper;
import models.Appointment;

public class AppointmentMapper implements Mapper<Appointment>{
    @Override
    public Appointment toObject(String row) throws FileCorruptedException {
        String[] data = row.split("\\|");
        
        if (data.length != 11) {
            throw new FileCorruptedException("Appointment file contains extra data or has missing data");
        }

        Appointment appt = new Appointment();
        appt.setId(data[0]);
        appt.setCreatedAt(LocalDateTime.parse(data[9]));
        appt.setUpdatedAt(LocalDateTime.parse(data[10]));
        appt.setCustomerId(data[1]);
        appt.setStaffId(data[2]);
        appt.setTechnicianId(data[3]);
        appt.setServiceId(data[4]);
        appt.setDate(LocalDate.parse(data[5]));
        appt.setTime(LocalTime.parse(data[6]));
        appt.setStatusService(AppointmentStatus.valueOf(data[7])); 
        appt.setDescription(data[8]);

        return appt;
    }

    @Override
    public String toString(Appointment appt) {
        return String.join("|", 
                appt.getId(),appt.getCustomerId(), appt.getStaffId(), appt.getTechnicianId(),
                appt.getServiceId(),appt.getDate().toString(), appt.getTime().toString(),appt.getStatusService().name(),
                appt.getDescription(), appt.getCreatedAt().toString(), appt.getUpdatedAt().toString() 
        );
    }
}
