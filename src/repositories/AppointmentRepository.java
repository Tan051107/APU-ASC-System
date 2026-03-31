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
            String record = String.join(",",
                    appointment.getId(),
                    appointment.getCustomerId(),
                    appointment.getTechnicianId(),
                    appointment.getServiceId(),
                    appointment.getDate().toString(),
                    appointment.getTime().toString(),
                    appointment.getStatusService().name(), // Saves "ASSIGNED" or "COMPLETED"
                    appointment.getCreatedAt().toString(),
                    appointment.getUpdatedAt().toString()
            );

            writer.write(record);
            writer.newLine(); // Move to the next line for the next entry
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
        // Assuming format is: APP-001,timestamp,timestamp,customerId...
        String[] columns = lastLine.split(",");
        String lastId = columns[0]; // Gets "APP-045"

        try {
            // Extract the number part ("045"), convert to integer, and add 1
            int nextNumber = Integer.parseInt(lastId.split("-")[1]) + 1;
            
            // Format it back into a 3-digit string (e.g., 46 becomes "APP-046")
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

        // If the file doesn't exist yet, just return an empty list
        if (!file.exists()) {
            return appointments;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip any blank lines
                if (line.trim().isEmpty()) continue;

                // Split the comma-separated line into an array
                String[] data = line.split(",");

                // Reconstruct the Appointment object
                // Note: The order here MUST match the exact order you saved them in!
                Appointment appointment = new Appointment(
                        data[0],                         // id
                        LocalDateTime.parse(data[7]),    // createdAt (Now at index 7)
                        LocalDateTime.parse(data[8]),    // updatedAt (Now at index 8)
                        data[1],                         // customerId
                        data[2],                         // technicianId
                        data[3],                         // serviceId
                        LocalDate.parse(data[4]),        // date
                        LocalTime.parse(data[5]),        // time
                        AppointmentStatus.valueOf(data[6]) // statusService
                );

                appointments.add(appointment);
            }
        } catch (Exception e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }

        return appointments;
    }

    // find by appointmentID
    public Appointment findById(String targetId) {
        return findAll().stream()
                .filter(appt -> appt.getId().equals(targetId))
                .findFirst()
                .orElse(null); // Not found
    }

    // find by date
    public List<Appointment> findByDate(LocalDate targetDate) {
        List<Appointment> matchingAppointments = new ArrayList<>();
        for (Appointment appt : findAll()) {
            if (appt.getDate().equals(targetDate)) {
                matchingAppointments.add(appt);
            }
        }
        return matchingAppointments;
    }

}
