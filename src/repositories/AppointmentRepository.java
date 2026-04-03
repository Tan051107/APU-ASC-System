package repositories;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import enums.AppointmentStatus;
import models.Appointment;

public class AppointmentRepository {
    private static final String FILE_PATH = "txt_files/Appointment.txt";

    public void save(Appointment appointment) {
        String newId = generateNextId();
        appointment.setId(newId);
        
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // Format the data as a comma-separated string
            String record = String.join("|",
                    appointment.getId(),
                    appointment.getCustomerId(),
                    appointment.getStaffId(),
                    appointment.getTechnicianId(),
                    appointment.getServiceId(),
                    appointment.getDate().toString(),
                    appointment.getTime().toString(),
                    appointment.getStatusService().name(), // "ASSIGNED"/"COMPLETED"
                    appointment.getCreatedAt().toString(),
                    appointment.getUpdatedAt().toString()
            );

            writer.write(record);
            writer.newLine();
            System.out.println("Successfully saved: " + newId);

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // Find last id and add 1
    private String generateNextId() {
        File file = new File(FILE_PATH);
        
        // If the file doesn't exist or is empty, start at APP-001
        if (!file.exists() || file.length() == 0) {
            return "APP-001";
        }

        String lastLine = "";
        
        // Read through the file to find the very last line
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.trim().isEmpty()) {
                    lastLine = currentLine;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return "APP-001"; // Fallback
        }

        // If for some reason the last line is completely empty
        if (lastLine.isEmpty()) {
            return "APP-001";
        }

        // Extract the ID from the last line
        String[] columns = lastLine.split("\\|");
        String lastId = columns[0]; // Gets "APP-045"

        try {
            int nextNumber = Integer.parseInt(lastId.split("-")[1]) + 1;
            
            return String.format("APP-%03d", nextNumber);
            
        } catch (Exception e) {
            System.err.println("Error parsing last ID. Starting fresh.");
            return "APP-001";
        }
    }

    //Read all entries in file
    public List<Appointment> findAll() {
        List<Appointment> appointments = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return appointments;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split("\\|");

                if (data.length < 10) {
                    System.err.println("Skipping uncomplete data line (expected 10 columns, found " + data.length + "): " + line);
                    continue; 
                }
                try {
                    Appointment appointment = new Appointment(
                            data[0],                         // id
                            LocalDateTime.parse(data[8]),    // createdAt (index 7)
                            LocalDateTime.parse(data[9]),    // updatedAt (index 8)
                            data[1],                         // customerId
                            data[2],
                            data[3],                         // technicianId
                            data[4],                         // serviceId
                            LocalDate.parse(data[5]),        // date
                            LocalTime.parse(data[6]),        // time
                            AppointmentStatus.valueOf(data[7]) // statusService
                    );

                    appointments.add(appointment);
                } catch (Exception parseEx) {
                    System.err.println("Error parsing row data: " + line + " | Error: " + parseEx.getMessage());
                }
                
            }
        } catch (Exception e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }

        return appointments;
    }

}
