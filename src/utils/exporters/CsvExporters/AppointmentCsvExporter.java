package utils.exporters.CsvExporters;

import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import models.Appointment;
import models.Services;
import services.*;
import utils.exporters.interfaces.CsvExporter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppointmentCsvExporter implements CsvExporter<Appointment> {
    private final Logger logger = Logger.getLogger(AppointmentCsvExporter.class.getName());

    @Override
    public void exportData(List<Appointment> data, String filePath) throws FileCorruptedException, IOException {
        try(FileWriter writer = new FileWriter(filePath)){
            writer.append("Appointment Id, Customer Name , Car Plate , Technician Name , Service Type, Appointment Date , Appointment Time , Duration, Status , Description\n");
            for(Appointment appointment : data){
                String appointmentId = appointment.getId();
                String customerName = appointment.getCustomer().getName();
                String carPlate =appointment.getCar().getCarPlate();
                String technicianName = appointment.getTechnician().getName();
                Services serviceChosen = appointment.getService();
                String serviceName = serviceChosen.getServiceName();
                String serviceDuration = String.valueOf(serviceChosen.getServiceDuration());
                String appointmentDate = appointment.getDate().toString();
                String appointmentTime = appointment.getTime().toString();
                String appointmentStatus = appointment.getStatusService().getDisplayAppointmentStatus();
                String appointmentDescription = appointment.getDescription();
                String dataRow = String.join("," ,
                        escapeCSV(appointmentId),
                        escapeCSV(customerName),
                        escapeCSV(carPlate),
                        escapeCSV(technicianName),
                        escapeCSV(serviceName),
                        escapeCSV(appointmentDate),
                        escapeCSV(appointmentTime),
                        escapeCSV(serviceDuration),
                        escapeCSV(appointmentStatus),
                        escapeCSV(appointmentDescription)
                        );
                writer.append(dataRow).append("\n");
            }
        } catch (GetEntityListException e) {
            logger.log(Level.SEVERE,e.getMessage());
        }

    }
}
